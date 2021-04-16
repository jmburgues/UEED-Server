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
    /*
     * AGREGAR MAS INFO. DEUDA, ULTIMO PAGO, FORMA DE PAGO...
     */
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

CREATE TABLE ADDRESS
(
    addressId int auto_increment,
    street   varchar(40),
    number   int not null,
    clientId int not null,
    rateId   int not null,
    CONSTRAINT pk_adressId primary key (addressId),
    CONSTRAINT fk_ADDRESS_clientId foreign key (clientId) references CLIENTS(clientId),
    CONSTRAINT fk_ADDRESS_rateId foreign key (rateId) references RATES (rateId)
);

CREATE TABLE METERS
(
    serialNumber  varchar(40),
    modelId  int not null,
    /*
     * AGREGAR KW ACUMULADOS Y ULTIMA FECHA DE MEDICION ??????? CONSULTAR
     */
    CONSTRAINT pk_serialNumber primary key (serialNumber),
    CONSTRAINT fk_METERS_modelId foreign key (modelId) references MODELS (modelId)

);

CREATE TABLE BILLS(
    billId int auto_increment,
    dateFrom datetime not null,
    dateTo datetime not null,
    initialConsumption float default 0,
    finalConsumption float default 0,
    totalConsumption float default 0,
    meterId varchar(40) not null,
    rateCategory varchar(40) not null,
    ratePrice float not null,
    totalPrice float default 0,
    clientId int not null,
    CONSTRAINT pk_billId primary key (billId),
    CONSTRAINT fk_BILLS_clientId foreign key (clientId) references CLIENTS(clientId)
);

CREATE TABLE MEASUREMENTS(
    measurementId int auto_increment,
    measuredDate datetime not null,
    totalKw float default 0,
    meterSerialNumber varchar(40) not null,
    billId int default null,
    CONSTRAINT pk_mId primary key (measurementId),
    CONSTRAINT fk_MEASUREMENTS_meterSN foreign key (meterSerialNumber) references METERS(serialNumber),
    CONSTRAINT fk_MEASUREMENTS_billId foreign key (billId) references BILLS(billId)
);

#
#CALCULAR TOTAL DE FACUTRA
#buscar tarifa del medidor en dicho momento,
#calcularlo contra la medicion
#guardarlo en factura.