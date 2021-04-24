package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingRepository extends JpaRepository<Reading,Integer> {
}
