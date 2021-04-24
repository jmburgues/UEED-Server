package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate,Integer> {
}
