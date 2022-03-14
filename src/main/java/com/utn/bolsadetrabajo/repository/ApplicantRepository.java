package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    Applicant findByIdentification(String dni);

    Applicant findByUser(User user);
}
