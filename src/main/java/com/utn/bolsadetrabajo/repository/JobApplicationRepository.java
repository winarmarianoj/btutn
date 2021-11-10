package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
}
