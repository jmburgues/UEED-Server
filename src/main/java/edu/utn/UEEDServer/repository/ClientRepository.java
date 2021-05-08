package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client,Integer> {

    @Query(value = "SELECT * from bills where clientId =?1 and payed=false",nativeQuery = true)
    List<Bill> getUnpaidBills(Integer clientId);
}
