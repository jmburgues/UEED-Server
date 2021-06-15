CREATE DATABASE UEED_DB;
# DROP DATABASE UEED_DB;
USE UEED_DB;

CREATE TABLE RATES(
    rateId int auto_increment,
    category varchar(40) unique,
    kwPrice float CHECK(kwPrice>0),
    CONSTRAINT pk_rateId primary key (rateId)
);

CREATE TABLE USERS(
    username varchar(40),
    password varchar(40) not null,
    name varchar(40) not null,
    surname varchar(40) not null,
    employee bool default 0,
    CONSTRAINT pk_username primary key (username)
);

CREATE TABLE CLIENTS(
    clientId int auto_increment,
    username varchar(40) not null unique,
    name varchar(50),
    surname varchar(50),
    dni long,
    CONSTRAINT pk_clientId primary key (clientId),
    CONSTRAINT fk_CLIENTS_username foreign key (username) references USERS(username)
);

CREATE TABLE BRANDS(
    brandId int auto_increment,
    name varchar(40) not null,
    CONSTRAINT pk_brandId primary key (brandId)
);

CREATE TABLE MODELS(
    modelId int auto_increment,
    name varchar(40) not null,
    brandId int not null,
    CONSTRAINT pk_modelId primary key (modelId),
    CONSTRAINT fk_MODELS_brandId foreign key (brandId) references BRANDS(brandId)
);

CREATE TABLE ADDRESSES
(
    addressId int auto_increment,
    street   varchar(40),
    number   int not null,
    clientId int not null,
    rateId   int not null,
    CONSTRAINT pk_addressId primary key (addressId),
    CONSTRAINT fk_ADDRESS_clientId foreign key (clientId) references CLIENTS(clientId),
    CONSTRAINT fk_ADDRESS_rateId foreign key (rateId) references RATES (rateId)
);

CREATE TABLE METERS
(
    serialNumber VARCHAR(40),
    lastReading datetime default null, # This field will be set by a trigger
    accumulatedConsumption double default 0,  # This field will be set by a trigger
    modelId int not null,
    password VARCHAR(40) not null,
    addressId int not null unique,
    CONSTRAINT pk_serialNumber primary key (serialNumber),
    CONSTRAINT fk_METERS_modelId foreign key (modelId) references MODELS (modelId),
    CONSTRAINT fk_METERS_addressId foreign key (addressId) references ADDRESSES(addressId)
);

CREATE TABLE BILLS(
    billId int auto_increment,
    description varchar(100),
    billedDate datetime not null,
    initialReadingDate datetime,
    finalReadingDate datetime,
    initialConsumption float default 0,
    finalConsumption float default 0,
    totalConsumption float default 0,
    meterId varchar(40) not null,
    rateCategory varchar(40) not null,
    ratePrice float not null,
    totalPrice float default 0,
    clientId int not null,
    paid bool default 0,
    CONSTRAINT pk_billId primary key (billId),
    CONSTRAINT fk_BILLS_clientId foreign key (clientId) references CLIENTS(clientId)
);

CREATE TABLE READINGS(
    readingId int auto_increment,
    readDate datetime not null,
    totalKw float default 0,
    meterSerialNumber varchar(40) not null,
    readingPrice float default null, # DB Requirement nª 3
    billId int default null,
    CONSTRAINT pk_mId primary key (readingId),
    CONSTRAINT fk_READINGS_meterSN foreign key (meterSerialNumber) references METERS(serialNumber),
    CONSTRAINT fk_READINGS_billId foreign key (billId) references BILLS(billId)
);


# TRIGGER prevents unregistered meters from storing data.

DELIMITER //
CREATE TRIGGER `tbi_checkRegisteredMeter` BEFORE INSERT ON READINGS FOR EACH ROW
    BEGIN
        IF(NOT EXISTS (SELECT * FROM METERS WHERE serialNumber = new.meterSerialNumber)) THEN
            SIGNAL SQLSTATE '50000' SET MESSAGE_TEXT = 'Operation not allowed: Meter is not registered.';
        end if;
    end; //

## ITEM 3
# STORED PROCEDURE: gets actual Rate price from meter Serial Number.
DELIMITER //
CREATE PROCEDURE getKwPrice(IN meterSerialNumber VARCHAR(40), OUT actualPrice FLOAT)
BEGIN
    SELECT R.kwPrice INTO actualPrice
    FROM RATES R
         INNER JOIN
         ADDRESSES A
         ON R.rateId = A.rateId
         INNER JOIN
         METERS M
         ON M.addressId = A.addressId
    WHERE M.serialNumber = meterSerialNumber;
end; //

## ITEM 3
# TRIGGER updates READINGS attributes with last readings and calculates consumption price.
DELIMITER //
CREATE TRIGGER `tbi_setReadingPrice` BEFORE INSERT ON READINGS FOR EACH ROW
BEGIN
    DECLARE pReadDate DATETIME DEFAULT NULL;
    DECLARE pLastReading FLOAT DEFAULT 0;

    CALL getKwPrice(new.meterSerialNumber,@actualPrice);

    SET pReadDate=(SELECT MAX(readDate) FROM READINGS WHERE readDate<new.readDate AND meterSerialNumber=new.meterSerialNumber LIMIT 1);

    IF(pReadDate IS NOT NULL)THEN
        SET pLastReading=(SELECT MAX(totalKw) FROM READINGS WHERE meterSerialNumber = new.meterSerialNumber AND readDate=pReadDate);
        SET new.readingPrice = (new.totalKw-pLastReading)* @actualPrice;
    END IF;

    IF(pReadDate IS NULL) THEN
        SET new.readingPrice = new.totalKw*@actualPrice;
    END IF;
END;

# TRIGGER Update METERS table after insert on READINGS
DELIMITER $$
CREATE TRIGGER `tai_updateMetersAfterReading` AFTER INSERT ON READINGS FOR EACH ROW
    BEGIN
        UPDATE METERS
        SET accumulatedConsumption = new.totalKw, lastReading = new.readDate
        WHERE METERS.serialNumber = new.meterSerialNumber;
    end;


#ITEM 3 PART II
#TRIGGER UPDATE READING PRICES AFTER UPDATES ON RATES
#drop trigger tau_updateReadingPrice
DELIMITER $$
CREATE TRIGGER tau_updateReadingPrice AFTER UPDATE ON RATES FOR EACH ROW
BEGIN
        DECLARE endLoop INT DEFAULT 0;
        DECLARE pReadingPrice,pTotalKw FLOAT;
        DECLARE pReadingId INT;
        DECLARE pSerialNumber VARCHAR(20);
        DECLARE rUpdate CURSOR FOR SELECT readingId,totalKw,meterSerialNumber,readingPrice FROM READINGS r
                                    WHERE r.meterSerialNumber IN(
                                                                SELECT m.serialNumber FROM METERS m
                                                                JOIN ADDRESSES a
                                                                ON m.addressId = a.addressId
                                                                JOIN RATES ra
                                                                ON a.rateId = ra.rateId
                                                                WHERE ra.rateId = old.rateId
                                                                );
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET endLoop=1;

        IF (OLD.kwPrice <> NEW.kwPrice) THEN

            OPEN rUpdate;
            foreach: LOOP
                FETCH rUpdate INTO pReadingId,pTotalKw,pSerialNumber,pReadingPrice;
                IF endLoop = 1 THEN
                    LEAVE foreach;
                END IF;
                UPDATE READINGS SET readingPrice=(pReadingPrice/old.kwPrice)*new.kwPrice WHERE meterSerialNumber=pSerialNumber AND readingId = pReadingId;
            END LOOP foreach;
            CLOSE rUpdate;
        END IF;
END;

   # /*Calculate consumed kws between intervals of time*/
DELIMITER $$
CREATE PROCEDURE sp_consumeBtwTimes (pSerialNumber VARCHAR(40),pDateFrom DATETIME ,pDateTo DATETIME,OUT pConsume FLOAT)
BEGIN
    DECLARE  consumeFrom FLOAT DEFAULT 0;
    DECLARE consumeTo FLOAT DEFAULT 0;
    SELECT totalKw INTO consumeFrom FROM READINGS WHERE meterSerialNumber = pSerialNumber AND readDate = pDateFrom;
    SELECT totalKw INTO consumeTo FROM READINGS WHERE meterSerialNumber = pSerialNumber AND readDate = pDateTo;

    SET pConsume = consumeTo-consumeFrom;
END $$
DELIMITER ;


#VIEWS
#Item 4
CREATE VIEW report_readings_by_date_n_user_view AS
SELECT c.name,c.surname,m.serialNumber,r.readDate,r.totalKw,r.readingPrice
FROM ADDRESSES a JOIN METERS m ON a.addressId=m.addressId JOIN
     CLIENTS c ON c.clientId = a.clientId JOIN READINGS r ON r.meterSerialNumber =m.serialNumber;

#ITEM 4
#----- Generate Bill --------#
DELIMITER $$
CREATE PROCEDURE sp_generateBill (p_addressId INT)
#drop procedure sp_generateBill
BEGIN
    DECLARE p_meterId VARCHAR(100);
    DECLARE p_dateFrom,p_dateTo DATETIME;
    DECLARE p_clientId,p_rateId,p_rateCategory INT;
    DECLARE p_initialConsumption,p_finalConsumption,p_totalConsumption FLOAT;
    DECLARE p_totalPrice,p_ratePrice FLOAT;

    SELECT serialNumber INTO p_meterId FROM METERS  WHERE addressId = p_addressId;

    SELECT MIN(readDate) INTO p_dateFrom
    FROM READINGS WHERE ISNULL(billId) AND meterSerialNumber=p_meterId LIMIT 1;

    SELECT MAX(readDate)INTO p_dateTo FROM READINGS
    WHERE ISNULL(billId) AND meterSerialNumber=p_meterId LIMIT 1;

    SELECT clientId INTO p_clientId FROM ADDRESSES WHERE addressId = p_addressId;
    SELECT MAX(totalKw) INTO p_initialConsumption FROM READINGS WHERE readDate = p_dateFrom AND meterSerialNumber=p_meterId;
    SELECT MAX(totalKw) INTO p_finalConsumption FROM READINGS WHERE readDate = p_dateTo AND meterSerialNumber=p_meterId;
    SELECT rateId INTO p_rateId FROM ADDRESSES WHERE addressId = p_addressId;
    SELECT rateId INTO p_rateCategory FROM RATES WHERE rateId=p_rateId;
    SELECT kwPrice INTO p_ratePrice FROM RATES WHERE  rateId=p_rateId;

    SET p_totalConsumption = p_finalConsumption-p_initialConsumption;
    SET p_totalPrice = p_ratePrice*p_totalConsumption;

    IF p_dateFrom IS NOT NULL THEN
        INSERT INTO BILLS (description,clientId,billedDate,initialReadingDate,finalReadingDate,finalConsumption,initialConsumption,meterId,rateCategory,ratePrice,
                           totalConsumption,totalPrice) VALUES
        ('Monthly',p_clientId,NOW(),p_dateFrom,p_dateTo,p_finalConsumption,p_initialConsumption,p_meterId,p_rateCategory,
         p_ratePrice,p_totalConsumption,p_totalPrice);

    END IF;

END $$
DELIMITER ;

#-------------------generate all bills --------------------
DELIMITER $$
CREATE definer = 'root'@'localhost' PROCEDURE billAll()
BEGIN
        DECLARE endLoop INT DEFAULT 0;
        DECLARE pAddressId INT;
        DECLARE billCursor CURSOR FOR SELECT addressId FROM ADDRESSES;
        DECLARE CONTINUE HANDLER FOR NOT FOUND SET endLoop = 1;

        SET autocommit = 0;
        SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;

            OPEN billCursor;

            foreach: LOOP

                FETCH billCursor INTO pAddressId;
                     IF  endLoop = 1 THEN
                     LEAVE foreach;
                     END IF;
                CALL sp_generateBill(pAddressId);

            END LOOP foreach;
            CLOSE billCursor;

        COMMIT;
        SET autocommit = 1;
END $$
DELIMITER ;

#----------------------------------#
# Event for billing once a month

CREATE EVENT billAllAddresses
ON SCHEDULE EVERY 1 MINUTE STARTS NOW()
DO CALL billAll();

# ----TRIGGER SET BILL ID AFTER INSERT ON BILLS ----#
DELIMITER $$
CREATE TRIGGER tai_setBillId AFTER INSERT ON BILLS FOR EACH ROW
BEGIN
    UPDATE READINGS SET billId = new.billId WHERE meterSerialNumber = new.meterId;

END;

#----------ADJUSTMENT BILL TRIGGER -------------
#drop trigger UEED_DB.tau_adjustmentBill
DELIMITER $$
CREATE TRIGGER tau_adjustmentBill AFTER UPDATE ON RATES
    FOR EACH ROW
BEGIN
    DECLARE endLoop INT DEFAULT 0;
    DECLARE pBillId,pClientId INT;
    DECLARE pDateFrom,pDateTo DATETIME;
    DECLARE pMeterId VARCHAR (20);
    DECLARE pDescription VARCHAR (100);
    DECLARE pInitialConsumption,pFinalConsumption,pTotalConsumption,pRatePrice,pTotalPrice FLOAT;

    DECLARE adjust CURSOR FOR SELECT billId,description,initialReadingDate,finalReadingDate,initialConsumption,finalConsumption,totalConsumption,meterId,ratePrice,totalPrice,clientId
                              FROM BILLS WHERE rateCategory = old.rateId AND ratePrice=old.KwPrice;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET endLoop = 1;

    OPEN adjust;
    foreach: LOOP
        FETCH adjust INTO pBillId,pDescription,pDateFrom,pDateTo,pInitialConsumption,pFinalConsumption,pTotalConsumption,pMeterId,pRatePrice,pTotalPrice,pClientId;
        IF endLoop = 1 THEN
            LEAVE foreach;
        END IF;
        IF old.kwPrice <> new.kwPrice THEN
            SET pTotalPrice = -1*pTotalConsumption*(old.kwPrice-new.kwPrice);
        END IF;
        INSERT INTO BILLS
        (description,billedDate,initialReadingDate,finalReadingDate,initialConsumption,finalConsumption,totalConsumption,meterId,rateCategory,ratePrice,totalPrice,clientId)
        VALUES
        ('Adjustment',NOW(),pDateFrom,pDateTo,pInitialConsumption,pFinalConsumption,pTotalConsumption,pMeterId,new.rateId,new.kwPrice,pTotalPrice,pClientId);
    END LOOP foreach;
    CLOSE adjust;
END $$
DELIMITER ;

#INDEXES
/*to prevent duplicates addresses*/
CREATE UNIQUE INDEX index_address ON ADDRESSES (street,number)
    USING BTREE;

## ITEM 4
# Indexes to optimize Reading querys by username and date
CREATE INDEX `idx_readings_dates` ON READINGS(meterSerialNumber,readDate) USING BTREE;
