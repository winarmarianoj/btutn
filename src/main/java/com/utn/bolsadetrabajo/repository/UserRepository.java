package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query(value = "SELECT * FROM USER WHERE username = :username AND state like 'REVIEW'", nativeQuery = true)
    User findByUsernameByState(String username);

    @Query(value = "SELECT * FROM USER WHERE username = :username AND state like 'ACTIVE'", nativeQuery = true)
    User findByUsernameByStateActive(String username);
}
