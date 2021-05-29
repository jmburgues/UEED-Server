package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface ClientRepository extends JpaRepository<Client,Integer> {
    @Query(value = "SELECT * FROM CLIENTS WHERE username = ?1", nativeQuery = true)
    Client getByUsername(String username);
}
