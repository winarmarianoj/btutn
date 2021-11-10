package com.utn.bolsadetrabajo.repository;

import com.utn.bolsadetrabajo.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Publisher findByIdentification(String cuit);
}
