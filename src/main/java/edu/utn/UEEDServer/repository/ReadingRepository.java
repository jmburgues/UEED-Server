package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReadingRepository extends JpaRepository<Reading,Integer> {

    @Query(value = "SELECT * from readings where meterSerialNumber = ?1",nativeQuery = true)
    List<Reading> findByMeterId(UUID serialNumber);

    @Query(value= "SELECT * from readings where meterSerialNumber = ?1 and billId=null",nativeQuery = true)
    List<Reading> getNotBilledReadings(UUID meterSerialNumber);

    // [PROG] - ITEM 5 - Query readings by date range
    @Query(value = "SELECT * FROM readings WHERE meterSerialNumber = ?1 readDate BETWEEN ?2 AND ?3", nativeQuery = true)
    List<Reading> getReadingsBetweenDates(LocalDateTime from, LocalDateTime to);





    @Query(value = "SELECT * FROM BILLS WHERE clientId = ?1 AND billedDate BETWEEN ?2 AND ?3", nativeQuery = true)
    List<Bill> dateAndClientFilter(Integer clientId, LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT * FROM BILLS WHERE billedDate BETWEEN ?2 AND ?3",nativeQuery = true)
    List<Bill> dateFilter(LocalDateTime from, LocalDateTime to);

}
