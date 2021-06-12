create user 'meters'@'%' identifed by '1234'
create user 'clients'@'%' identifed by '1234'
create user 'backoffice'@'%' identifed by '1234'
create user 'billing'@'%' identifed by '1234'

### METER PERMISIONS
grant insert on UEED_DB.READINGS

### CLIENT PERMISIONS
# Select de BILLS (Filtrar por Cliente y Dia)
# Select de BILLS (traer facturas impagas)
grant select on UEED_DB.BILLS to 'user'

# Client consumption (StoredProcedure)
DELIMITER $$
CREATE definer = 'root'@'localhost' PROCEDURE clientConsumption(inClientId INT, inFrom DATETIME, inTo DATETIME)
begin
    SELECT SUM(R.totalKw) AS totalConsumption, SUM(R.readingPrice) AS totalPrice
    FROM READINGS R
    INNER JOIN METERS M
    ON R.meterSerialNumber = M.serialNumber
    INNER JOIN ADDRESSES A
    ON A.addressId = M.addressId
    INNER JOIN CLIENTS C
    ON A.clientId = C.clientId
    WHERE C.clientId = inClientId AND R.readDate BETWEEN inFrom AND inTo
    GROUP BY C.clientId
end $$

# Client reading by date
DELIMITER $$
CREATE definer = 'root'@'localhost' PROCEDURE clientConsumption(inClientId INT, infrom DATETIME, inTo DATETIME)
begin
    SELECT * FROM READINGS R
    INNER JOIN METERS M
    ON R.meterSerialNumber = M.serialNumber
    INNER JOIN ADDRESSES A
    ON A.addressId = M.addressId
    INNER JOIN CLIENTS C
    ON A.clientId = C.clientId
    WHERE C.clientId = inClientId AND R.readDate BETWEEN infrom AND inTo
end $$
DELIMITER ;
### BACKOFFICE PERMISIONS
