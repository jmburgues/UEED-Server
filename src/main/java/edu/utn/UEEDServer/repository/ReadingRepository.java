package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.model.projections.ConsumersDTO;
import edu.utn.UEEDServer.model.projections.ClientConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ReadingRepository extends JpaRepository<Reading,Integer> {

    @Query(value = "SELECT R.* FROM READINGS R " +
            "INNER JOIN METERS M " +
            "ON R.meterSerialNumber = M.serialNumber " +
            "INNER JOIN ADDRESSES A " +
            "ON M.addressId = A.addressId " +
            "WHERE A.addressId = ?1 AND readDate BETWEEN ?2 AND ?3 " +
            "LIMIT ?4,?5", nativeQuery = true)
    List<Reading> getAddressReadingsByDate(Integer addressId, Date from, Date to, Integer page, Integer size);

    @Query(value = "SELECT R.* FROM READINGS R " +
            "INNER JOIN METERS M " +
            "ON R.meterSerialNumber = M.serialNumber " +
            "INNER JOIN ADDRESSES A " +
            "ON A.addressId = M.addressId " +
            "INNER JOIN CLIENTS C " +
            "ON A.clientId = C.clientId " +
            "WHERE C.clientId = ?1 AND R.readDate BETWEEN ?2 AND ?3 " +
            "LIMIT ?4,?5", nativeQuery = true)
    List<Reading> getClientReadingsByDate(Integer clientId, Date from, Date to, Integer page, Integer size);

    @Query(value = "SELECT SUM(R.totalKw) AS totalConsumption, SUM(R.readingPrice) AS totalPrice " +
            "FROM READINGS R " +
            "INNER JOIN METERS M " +
            "ON R.meterSerialNumber = M.serialNumber " +
            "INNER JOIN ADDRESSES A " +
            "ON A.addressId = M.addressId " +
            "INNER JOIN CLIENTS C " +
            "ON A.clientId = C.clientId " +
            "WHERE C.clientId = ?1 AND R.readDate BETWEEN ?2 AND ?3 " +
            "GROUP BY C.clientId", nativeQuery = true)
    ClientConsumption getClientConsumption(Integer clientId, Date from, Date to);

    @Query(value =
            "SELECT ONE.clientId as clientId, ONE.name as name, ONE.surname as surname, ONE.consumption as consumption " +
                    "FROM( " +
                    "SELECT C.clientId, C.name, C.surname, MAX(R.totalKw) - MIN(R.TotalKw) as consumption " +
                    "FROM READINGS R " +
                    "INNER JOIN METERS M " +
                    "ON R.meterSerialNumber = M.serialNumber " +
                    "INNER JOIN ADDRESSES A " +
                    "ON A.addressId = M.addressId " +
                    "INNER JOIN CLIENTS C " +
                    "ON C.clientId = A.clientId " +
                    "WHERE R.readDate BETWEEN ?1 AND ?2 " +
                    "GROUP BY C.clientId, C.name, C.surname) AS ONE " +
                    "GROUP BY ONE.clientId, ONE.name, ONE.surname, ONE.consumption " +
                    "ORDER BY SUM(consumption) DESC " +
                    "LIMIT 10", nativeQuery = true)
    List<ConsumersDTO> getTopConsumers(Date from, Date to);
}

