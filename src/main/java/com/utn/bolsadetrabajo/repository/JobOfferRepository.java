package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.JobOffer;
import com.utn.bolsadetrabajo.model.enums.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    @Query(value = "SELECT * FROM JOBOFFER WHERE state = :state", nativeQuery = true)
    List<JobOffer> findAllByState(String state);

    @Query(value = "SELECT * FROM JOBOFFER WHERE id = :id", nativeQuery = true)
    boolean findSearchById(Long id);
}
