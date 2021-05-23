package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Meter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeterRepository extends JpaRepository<Meter, String> {
    @Query(value = "SELECT * FROM METERS " +
            "WHERE addressId = ?1", nativeQuery = true)
    Meter findByAddressId(Integer addressId);
}
