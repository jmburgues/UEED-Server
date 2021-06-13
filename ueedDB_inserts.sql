# INSERT VALUES

insert into BRANDS(name) values ('Motorola');
insert into BRANDS(name) values ('Zyxel');
insert into MODELS(name,brandId) values ('M001',1);
insert into MODELS(name,brandId) values ('Z001',2);
insert into RATES(category, kwPrice) values ('RESIDENTIAL','1');
insert into RATES(category, kwPrice) values ('INDUSTRIAL','1.5');
insert into USERS(username, password, name, surname) values ('user1','1234','User','One');
insert into USERS(username, password, name, surname,employee) values ('employee','1234','User','One',1);
insert into USERS(username, password, name, surname,employee) values ('user2','1234','User2','Two',0);
insert into CLIENTS(username, name, surname, dni) values ('user1','Jorge','Amarilla','32001234');
insert into CLIENTS(username, name, surname, dni) values ('user2','User2','Two','3331234');
insert into ADDRESSES(street, number, clientId, rateId) values ('Calle Falsa',123,1,1);
insert into ADDRESSES(street, number, clientId, rateId) values ('Calle Residencial',123,1,1);
insert into ADDRESSES(street, number, clientId, rateId) values ('Av. Siempre viva',442,2,2);
insert into METERS(serialNumber, modelId, password, addressId) values ('001',1,1234,1);
insert into METERS(serialNumber, modelId, password, addressId) values ('002',1,1234,2);
insert into METERS(serialNumber, modelId, password, addressId) values ('003',2,1234,3);
insert into READINGS(readDate, totalKw, meterSerialNumber, readingPrice) values (now(),11,'001',null);
insert into READINGS(readDate, totalKw, meterSerialNumber, readingPrice) values (now(),12,'002',null);
insert into READINGS (readDate, totalKw, meterSerialNumber, readingPrice) values (now(),13,'002',null);
insert into READINGS(readDate, totalKw, meterSerialNumber, readingPrice) values (now(),18,'002',null);
insert into READINGS(readDate, totalKw, meterSerialNumber, readingPrice) values (now(),15,'001',null);
insert into READINGS(readDate, totalKw, meterSerialNumber, readingPrice) values (now(),43,'001',null);
insert into READINGS(readDate, totalKw, meterSerialNumber, readingPrice) values (now(),32,'003',null);