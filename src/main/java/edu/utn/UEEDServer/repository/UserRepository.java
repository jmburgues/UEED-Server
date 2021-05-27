package edu.utn.UEEDServer.repository;

import edu.utn.UEEDServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query(value = "SELECT * FROM USERS WHERE username = ?1 AND password = ?2", nativeQuery = true)
    User findByUserAndPass(String username, String password);
}
