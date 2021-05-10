package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill,Integer> {

    @Query(value = "SELECT * FROM BILLS WHERE clientId=?1",nativeQuery = true)
    List<Bill> getByClientId(Integer clientId);

    @Query(value = "SELECT * FROM BILLS WHERE paid=false",nativeQuery = true)
    List<Bill> getUnpaid();

    @Query(value = "SELECT * from bills where clientId =?1 and paid=false",nativeQuery = true)
    List<Bill> getUnpaidByClient(Integer clientId);

    @Query(value = "SELECT * FROM BILLS WHERE clientId = ?1 AND billedDate BETWEEN ?2 AND ?3", nativeQuery = true)
    List<Bill> dateAndClientFilter(Integer clientId, LocalDateTime from, LocalDateTime to);

   @Query(value = "SELECT * FROM BILLS WHERE billedDate BETWEEN ?2 AND ?3",nativeQuery = true)
   List<Bill> dateFilter(LocalDateTime from, LocalDateTime to);

}
