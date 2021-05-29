package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.model.dto.ConsumersDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReadingRepository extends JpaRepository<Reading,Integer> {

    @Query(value = "SELECT * FROM READINGS " +
            "WHERE meterSerialNumber = ?1 AND readDate BETWEEN ?2 AND ?3", nativeQuery = true)
    List<Reading> getReadingsByMeterAndDate(String meterSerialNumber, Date from, Date to);

    @Query(value = "SELECT * FROM READINGS R " +
            "INNER JOIN ADDRESSES A " +
            "ON R.meterSerialNumber = A.meterId " +
            "INNER JOIN CLIENTS C " +
            "ON A.clientId = C.clientId " +
            "WHERE C.clientId = ?1 AND R.readDate BETWEEN ?2 AND ?3", nativeQuery = true)
    List<Reading> getClientReadingsByDate(Integer clientId, Date from, Date to);

    @Query(value = "SELECT SUM(R.totalKw) AS TOTAL_CONSUMPTION, SUM(R.readingPrice) AS TOTAL_PRICE " +
            "FROM READINGS R " +
            "INNER JOIN ADDRESSES A " +
            "ON R.meterSerialNumber = A.meterId " +
            "INNER JOIN CLIENTS C " +
            "ON A.clientId = C.clientId " +
            "WHERE C.clientId = ?1 AND R.readDate BETWEEN ?2 AND ?3 " +
            "GROUP BY R.readingId", nativeQuery = true)
    Map<String, Float> getClientConsumption(Integer clientId, Date from, Date to);

    // VERIFICAR QUE FUNCIONE DEVOLVIENDO LIST<CLIENT> en lugar de un MAP
    // Puede tener ORDER BY SUM(consumption) y no estar en el primer SELECT de linea 31 ??
    @Query(value =
            "SELECT ONE.clientId, ONE.consumption " +
                    "FROM( " +
                    "SELECT C.clientId, R.meterSerialNumber, MAX(R.totalKw) - MIN(R.TotalKw) as consumption " +
                    "FROM READINGS R " +
                    "INNER JOIN METERS M " +
                    "ON R.meterSerialNumber = M.serialNumber " +
                    "INNER JOIN ADDRESSES A " +
                    "ON A.addressId = M.addressId " +
                    "INNER JOIN CLIENTS C " +
                    "ON C.clientId = A.clientId " +
                    "WHERE R.readDate BETWEEN ?1 AND ?2 " +
                    "GROUP BY C.clientId, R.meterSerialNumber) AS ONE " +
                    "GROUP BY ONE.clientId, ONE.consumption " +
                    "ORDER BY SUM(consumption) DESC " +
                    "LIMIT 20", nativeQuery = true)
    List<ConsumersDTO> getTopConsumers(Date from, Date to);

    /*
    @Query(value =
            "SELECT C.clientId, C.dni, C.name, C.surname, SUM(consumption) " +
            "FROM( " +
                "SELECT C.clientId, R.meterSerialNumber, MAX(R.totalKw) - MIN(R.TotalKw) as consumption " +
                "FROM READINGS R " +
                "INNER JOIN ADDRESSES A " +
                "ON R.meterSerialNumber = A.meterId " +
                "INNER JOIN CLIENTES C " +
                "ON C.clientId = A.clientId " +
                "WHERE R.readDate BETWEEN ?2 AND ?3 " +
                "GROUP BY C.clientId, R.meterSerialNumber " +
            ")" +
            "GROUP BY C.clientId, C.dni, C.name, C.surname " +
            "ORDER BY SUM(consumption) DESC " +
            "LIMIT 20;", nativeQuery = true)
    List<Client> getTopConsumers(Date from, Date to);
     */
}

