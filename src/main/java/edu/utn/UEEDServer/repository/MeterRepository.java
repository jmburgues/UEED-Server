package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Meter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeterRepository extends JpaRepository<Meter, String> {
}
