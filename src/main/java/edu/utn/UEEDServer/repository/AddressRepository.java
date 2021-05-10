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
}
