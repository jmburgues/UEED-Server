package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Meter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeterRepository extends JpaRepository<Meter, String> {
    @Query(value = "SELECT * FROM METERS " +
            "WHERE addressId = ?1", nativeQuery = true)
    Meter findByAddressId(Integer addressId);

    @Query(value = "SELECT * FROM METERS LIMIT ?1,?2", nativeQuery = true)
    List<Meter> findAllPagable(Integer page, Integer size);
}
