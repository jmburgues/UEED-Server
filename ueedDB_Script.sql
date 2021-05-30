CREATE DATABASE UEED_DB;
# DROP DATABASE UEED_DB;
USE UEED_DB;

CREATE TABLE RATES(
    rateId int auto_increment,
    category varchar(40) unique,
    kwPrice float,
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
    lastReading datetime default now(), # This field will be set by a trigger
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
        IF(NOT EXISTS (SELECT * FROM METERS WHERE meterSerialNumber = new.meterSerialNumber)) THEN
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
# TRIGGER updates meter attributes with last readings and calculates consumption price.

DELIMITER //
CREATE TRIGGER `tbi_setReadingPrice` BEFORE INSERT ON READINGS FOR EACH ROW
BEGIN
    DECLARE pReadDate DATETIME DEFAULT NULL;
    DECLARE pPrice FLOAT DEFAULT 0;
    DECLARE pRateId,pAddressId INT;
    DECLARE pLastReading FLOAT DEFAULT 0;

    CALL getKwPrice(new.meterSerialNumber,@actualPrice);

    SET pReadDate=(SELECT MAX(readDate) FROM READINGS WHERE readDate<new.readDate AND meterSerialNumber=new.meterSerialNumber);

    IF(pReadDate IS NOT NULL)THEN

        SET pLastReading=(SELECT totalKw FROM READINGS WHERE meterSerialNumber = new.meterSerialNumber AND readDate=pReadDate);
        SET new.readingPrice = (new.totalKw-pLastReading)* @actualPrice;
    END IF;
    IF(pReadDate IS NULL) THEN
        SET new.readingPrice = new.totalKw*@actualPrice;
    END IF;

END;

#ITEM 3 PART II
#TRIGGER UPDATE READING PRICES AFTER UPDATES ON RATES
DELIMITER $$
CREATE TRIGGER tau_updateReadingPrice AFTER UPDATE ON rates
    FOR EACH ROW
BEGIN

    DECLARE endLoop INT DEFAULT 0;
    DECLARE pReadingPrice,pTotalKw FLOAT;
    DECLARE pReadingId INT;
    DECLARE pSerialNumber VARCHAR(20);
    DECLARE rUpdate CURSOR FOR SELECT readingId,totalKw,meterSerialNumber,readingPrice FROM readings r
                                WHERE r.meterSerialNumber IN(
                                                            SELECT m.serialNumber FROM meters m
                                                            JOIN addresses a
                                                            ON m.addressId = a.addressId
                                                            JOIN rates ra
                                                            ON a.rateId = ra.rateId
                                                            WHERE ra.rateId = old.rateId
                                                            );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET endLoop=1;
    OPEN rUpdate;
    foreach: LOOP
        FETCH rUpdate INTO pReadingId,pTotalKw,pSerialNumber,pReadingPrice;
        IF endLoop = 1 THEN
            LEAVE foreach;
        END IF;
        UPDATE readings SET readingPrice=(pReadingPrice/old.kwPrice)*new.kwPrice WHERE meterSerialNumber=pSerialNumber AND readingId = pReadingId;

    END LOOP foreach;
    CLOSE rUpdate;
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
END;



#VIEWS
#Item 4
CREATE VIEW report_readings_by_date_n_user_view AS
SELECT c.name,c.surname,m.serialNumber,r.readDate,r.totalKw,r.readingPrice
FROM addresses a JOIN meters m ON a.addressId=m.addressId JOIN
     clients c ON c.clientId = a.clientId JOIN readings r ON r.meterSerialNumber =m.serialNumber;



#TOP CONSUMERS
SELECT ONE.clientId, ONE.consumption
FROM(
SELECT C.clientId, R.meterSerialNumber, MAX(R.totalKw) - MIN(R.TotalKw) as consumption
FROM READINGS R
INNER JOIN METERS M
ON R.meterSerialNumber = M.serialNumber
INNER JOIN ADDRESSES A
ON A.addressId = M.addressId
INNER JOIN CLIENTS C
ON C.clientId = A.clientId
WHERE R.readDate BETWEEN '2021/05/01' AND '2021/06/01'
GROUP BY C.clientId, R.meterSerialNumber) AS ONE
GROUP BY ONE.clientId, ONE.consumption
ORDER BY SUM(consumption) DESC
LIMIT 20;

#ITEM 4
#----- Generate Bill --------#
DELIMITER $$
CREATE PROCEDURE sp_generateBill (p_addressId INT)
BEGIN
    DECLARE p_meterId VARCHAR(100);
    DECLARE p_dateFrom,p_dateTo DATETIME;
    DECLARE p_clientId,p_rateId,p_rateCategory INT;
    DECLARE p_initialConsumption,p_finalConsumption,p_totalConsumption FLOAT;
    DECLARE p_totalPrice,p_ratePrice FLOAT;

    SELECT serialNumber INTO p_meterId FROM meters  WHERE addressId = p_addressId;

    SELECT MIN(readDate)INTO p_dateFrom
    FROM readings WHERE ISNULL(billId) AND meterSerialNumber=p_meterId;
    SELECT MAX(readDate)INTO p_dateTo FROM readings
    WHERE ISNULL(billId) AND meterSerialNumber=p_meterId;

    SELECT clientId INTO p_clientId FROM addresses WHERE addressId = p_addressId;
    SELECT totalKw INTO p_initialConsumption FROM readings WHERE readDate = p_dateFrom AND meterSerialNumber=p_meterId;
    SELECT totalKw INTO p_finalConsumption FROM readings WHERE readDate = p_dateTo AND meterSerialNumber=p_meterId;
    SELECT totalKw INTO p_totalConsumption FROM readings WHERE readDate = p_dateTo AND meterSerialNumber=p_meterId;
    SELECT rateId INTO p_rateId FROM addresses WHERE addressId = p_addressId;
    SELECT rateId INTO p_rateCategory FROM rates WHERE rateId=p_rateId;
    SELECT kwPrice INTO p_ratePrice FROM rates WHERE  rateId=p_rateId;

    SET p_totalPrice = p_ratePrice*p_totalConsumption;

    IF p_dateFrom IS NOT NULL THEN
        INSERT INTO bills (clientId,billedDate,initialReadingDate,finalReadingDate,finalConsumption,initialConsumption,meterId,rateCategory,ratePrice,
                           totalConsumption,totalPrice) VALUES
        (p_clientId,NOW(),p_dateFrom,p_dateTo,p_finalConsumption,p_initialConsumption,p_meterId,p_rateCategory,
         p_ratePrice,p_totalConsumption,p_totalPrice);

    END IF;
END;

#-------------------generate all bills --------------------
DELIMITER $$
CREATE PROCEDURE billAll()
BEGIN

    DECLARE endLoop INT DEFAULT 0;
    DECLARE pAddressId INT;
    DECLARE billCursor CURSOR FOR SELECT addressId FROM addresses;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET endLoop = 1;

    OPEN billCursor;

    foreach: LOOP

        FETCH billCursor INTO pAddressId;
             IF  endLoop = 1 THEN
             LEAVE foreach;
             END IF;
        CALL sp_generateBill(pAddressId);

    END LOOP foreach;
    CLOSE billCursor;
END;

#----------------------------------#
# Event for billing once a month

CREATE EVENT billAllAddresses
ON SCHEDULE EVERY 1 MINUTE STARTS NOW()
DO CALL billAll();



#INDEXES
/*to prevent duplicates addresses*/
CREATE UNIQUE INDEX index_address ON addresses (street,number)
    USING BTREE;