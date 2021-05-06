package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Address;
import edu.utn.UEEDServer.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {

    ///Esto no me gusta porque devuelve las dos tablas, se puede solucionar con una vista o con un procedure ??
    // o habira que hacer otro endpoint que los traiga desde el addressId???
    @Query("SELECT * from readings r join addresses a on r.meterSerialNumber = a.meterId where readDate bewtween (?1) and (?2) ")
    List<Reading> getReadingsBetweenDatesByAddress(Integer addressId, LocalDateTime from, LocalDateTime to);

    @Query(value = "SELECT * from readings where readDate between (?1) and (?2)",nativeQuery = true)
    List<Reading> getReadingsBetweenDates(LocalDateTime from, LocalDateTime to);
}
