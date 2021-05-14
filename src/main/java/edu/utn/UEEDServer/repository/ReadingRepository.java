package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReadingRepository extends JpaRepository<Reading,Integer> {

    @Query(value = "SELECT * from readings where meterSerialNumber = ?1",nativeQuery = true)
    List<Reading> findByMeterId(UUID serialNumber);

    @Query(value= "SELECT * FROM READINGS WHERE meterSerialNumber = ?1 AND billId=null",nativeQuery = true)
    List<Reading> getNotBilledReadings(UUID meterSerialNumber);

    // [PROG] - ITEM 5 - Query readings by date range
    @Query(value = "SELECT * FROM READINGS WHERE meterSerialNumber = ?1 readDate BETWEEN ?2 AND ?3", nativeQuery = true)
    List<Reading> getByDate(UUID meterSerialNumber, LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT SUM(totalKw) AS TOTAL_CONSUMPTION,SUM(readingPrice) AS TOTAL_PRICE FROM READINGS WHERE meterSerialNumber = ?1 AND readDate BETWEEN ?2 AND ?3 GROUP BY readingId", nativeQuery = true)
    Map<String, Float> getConsuption(UUID meterSerialNumber, LocalDateTime from, LocalDateTime to);

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
    Map<Integer, Float> getTopConsumers(LocalDateTime from, LocalDateTime to);
}

