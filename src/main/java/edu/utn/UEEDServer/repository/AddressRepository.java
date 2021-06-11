package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {
    @Query(value = "SELECT * FROM ADDRESSES LIMIT ?1,?2", nativeQuery = true)
    List<Address> findAllPageable(Integer page, Integer size);
}
