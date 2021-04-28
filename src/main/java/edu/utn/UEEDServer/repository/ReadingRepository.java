package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReadingRepository extends JpaRepository<Reading,Integer> {


    @Query(value = "SELECT * from readings where meterSerialNumber = ?1",nativeQuery = true)
    List<Reading> findByMeterId(UUID serialNumber);


    @Query(value= "SELECT * from readings where meterSerialNumber = ?1 and billId=null",nativeQuery = true)
    List<Reading> getNotBilledReadings(UUID meterSerialNumber);
}
