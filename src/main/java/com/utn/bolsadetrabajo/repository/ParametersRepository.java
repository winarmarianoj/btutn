package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.Parameters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametersRepository extends JpaRepository<Parameters, Long> {
    @Query(value = "SELECT value FROM PARAMETERS WHERE permit = :minAgeApplicant", nativeQuery = true)
    int findByValue(String minAgeApplicant);

    @Query(value = "SELECT value FROM PARAMETERS WHERE permit = :sizePage", nativeQuery = true)
    String getSizePage(String sizePage);
}
