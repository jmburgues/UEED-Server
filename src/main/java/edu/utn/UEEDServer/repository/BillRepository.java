package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill,Integer> {

    @Query(value = "SELECT A.* FROM BILLS B " +
            "INNER JOIN CLIENTS C " +
            "ON B.clientId = C.clientId " +
            "INNER JOIN ADDRESSES A " +
            "ON A.clientId = C.clientId " +
            "WHERE addressId = ?1 and paid = false",nativeQuery = true)
    List<Bill> getUnpaidByAddress(Integer addressId);

    @Query(value = "SELECT * from BILLS where clientId =?1 and paid = false",nativeQuery = true)
    List<Bill> getUnpaidByClient(Integer clientId);

    @Query(value = "SELECT * FROM BILLS WHERE clientId = ?1 AND billedDate BETWEEN ?2 AND ?3", nativeQuery = true)
    List<Bill> dateAndClientFilter(Integer clientId, Date from, Date to);

   @Query(value = "SELECT * FROM BILLS WHERE billedDate BETWEEN ?2 AND ?3",nativeQuery = true)
   List<Bill> dateFilter(Date from, Date to);
}
