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
    username varchar(40) not null,
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
    end //

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
end //

## ITEM 3
# TRIGGER updates meter attributes with last readings and calculates consumption price.
## Its ok to do subquerys or is it preferable to create a trigger and update a column to gain efficiency???
/**
  * WARNING: MUST ADD readingPrice variable on Reading model !!!
 */
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

END

## ITEM 3 - Second part
## Updates
##CREATE TRIGGER `tai_watchRates` AFTER UPDATE ON RATES FOR EACH ROW
##    BEGIN
##        CALL getKwPrice()
##        // working..
##    end;
##
##DELIMITER ;

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


#INDEXES
/*to prevent duplicates addresses*/
CREATE UNIQUE INDEX index_address ON addresses (street,number)
USING BTREE;

#TRIGGERS
/*Adjust reading price after update on rate price*/
DELIMITER $$
CREATE TRIGGER tau_readingPriceAdjustment AFTER UPDATE ON rates
    FOR EACH ROW
BEGIN
    UPDATE readings SET reading_price = new.kw_price;
END

#VIEWS
#Item 4
CREATE VIEW report_readings_by_date_n_user_view AS
SELECT c.name,c.surname,m.serialNumber,r.readDate,r.totalKw,r.readingPrice
FROM addresses a JOIN meters m ON a.addressId=m.addressId JOIN
     clients c ON c.clientId = a.clientId JOIN readings r ON r.meterSerialNumber =m.serialNumber;

# INSERT VALUES

insert into BRANDS(name) values ('Motorola');
insert into MODELS(name,brandId) values ('M001',1);
insert into RATES(category, kwPrice) values ('RESIDENTIAL','1');
insert into USERS(username, password, name, surname) values ('user1','1234','User','One');
insert into CLIENTS(username) values ('user1');
insert into ADDRESSES(street, number, clientId, rateId) values ('Calle Falsa',123,1,1);
insert into METERS(serialNumber, modelId, password, addressId) values ('001',1,1234,1);
insert into READINGS(readDate, totalKw, meterSerialNumber, readingPrice) values (now(),11,'001',null);

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
LIMIT 20