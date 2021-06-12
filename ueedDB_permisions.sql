create user 'meters'@'%' identified by '1234';
create user 'clients'@'%' identified by '1234';
create user 'boffice'@'%' identified by '1234';
create user 'billings'@'%' identified by '1234';

### METER PERMISIONS
grant insert on UEED_DB.READINGS to 'meters';

### CLIENT PERMISIONS
# Select de BILLS (Filtrar por Cliente y Dia)
# Select de BILLS (traer facturas impagas)
grant select on UEED_DB.BILLS to 'clients';

# Client consumption (StoredProcedure)
DELIMITER $$
CREATE definer = 'root'@'localhost' PROCEDURE clientConsumption(inClientId INT, dateFrom DATETIME, dateTo DATETIME)
begin
    SELECT SUM(R.totalKw) AS totalConsumption, SUM(R.readingPrice) AS totalPrice
    FROM READINGS R
    INNER JOIN METERS M
    ON R.meterSerialNumber = M.serialNumber
    INNER JOIN ADDRESSES A
    ON A.addressId = M.addressId
    INNER JOIN CLIENTS C
    ON A.clientId = C.clientId
    WHERE C.clientId = inClientId AND R.readDate BETWEEN dateFrom AND dateTo
    GROUP BY C.clientId;
end $$

GRANT execute ON procedure UEED_DB.clientConsumption to 'clients';

# Client reading by date
DELIMITER $$
CREATE definer = 'root'@'localhost' PROCEDURE getClientReadings(inClientId INT, dateFrom DATETIME, dateTo DATETIME)
begin
    SELECT * FROM READINGS R
    INNER JOIN METERS M
    ON R.meterSerialNumber = M.serialNumber
    INNER JOIN ADDRESSES A
    ON A.addressId = M.addressId
    INNER JOIN CLIENTS C
    ON A.clientId = C.clientId
    WHERE C.clientId = inClientId AND R.readDate BETWEEN dateFrom AND dateTo;
end $$
DELIMITER ;

GRANT execute ON procedure UEED_DB.getClientReadings to 'clients';

### BACKOFFICE PERMISIONS
# Rates, Addresses and Meters CRUD
grant select, insert, update, delete on UEED_DB.RATES to 'boffice';
grant select, insert, update, delete on UEED_DB.ADDRESSES to 'boffice';
grant select, insert, update, delete on UEED_DB.METERS to 'boffice';

# filter bills By Client And Date
# get unpaid bills by Client
grant select on UEED_DB.BILLS to 'boffice';

# get Unpaid Bills by Address
DELIMITER $$
CREATE definer = 'root'@'localhost' PROCEDURE getUnpaidByAddress(procAddresId INT)
begin
    SELECT * FROM BILLS B
    INNER JOIN CLIENTS C
    ON B.clientId = C.clientId
    INNER JOIN ADDRESSES A
    ON A.clientId = C.clientId
    WHERE addressId = procAddresId and paid = false;
end $$
DELIMITER ;

GRANT execute ON procedure UEED_DB.getUnpaidByAddress to 'boffice';

# Get readings by address and date getAddressReadingsByDate()
DELIMITER $$
CREATE definer = 'root'@'localhost' PROCEDURE getAddressReadingsByDate(procAddresId INT, dateFrom DATETIME, dateTo DATETIME)
begin
    SELECT * FROM READINGS R
    INNER JOIN METERS M
    ON R.meterSerialNumber = M.serialNumber
    INNER JOIN ADDRESSES A
    ON M.addressId = A.addressId
    WHERE A.addressId = procAddresId AND readDate BETWEEN dateFrom AND dateTo;
end $$
DELIMITER ;

GRANT execute ON procedure UEED_DB.getAddressReadingsByDate to 'boffice';

# getTopConsumers
DELIMITER $$
CREATE definer = 'root'@'localhost' PROCEDURE getTopConsumers(dateFrom DATETIME, dateTo DATETIME)
begin
    SELECT ONE.clientId as clientId, ONE.name as name, ONE.surname as surname, ONE.consumption as consumption
        FROM(
        SELECT C.clientId, C.name, C.surname, MAX(R.totalKw) - MIN(R.TotalKw) as consumption
        FROM READINGS R
        INNER JOIN METERS M
        ON R.meterSerialNumber = M.serialNumber
        INNER JOIN ADDRESSES A
        ON A.addressId = M.addressId
        INNER JOIN CLIENTS C
        ON C.clientId = A.clientId
        WHERE R.readDate BETWEEN dateFrom AND dateTo
        GROUP BY C.clientId, C.name, C.surname) AS ONE
    GROUP BY ONE.clientId, ONE.name, ONE.surname, ONE.consumption
    ORDER BY SUM(consumption) DESC
    LIMIT 10;
end $$
DELIMITER ;

GRANT execute ON procedure UEED_DB.getTopConsumers to 'boffice';