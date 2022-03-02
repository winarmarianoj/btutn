package com.utn.bolsadetrabajo.service;

import com.utn.bolsadetrabajo.dto.request.PublisherDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Publisher;
import org.springframework.http.ResponseEntity;

public interface PublisherService {
    ResponseEntity<?> save(PublisherDTO publisher) throws PersonException;

    ResponseEntity<?> getPublisherById(Long id);

    ResponseEntity<?> getPublisherByCuit(String cuit);

    ResponseEntity<?> update(Long id, PublisherDTO publisherDTO);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> getAllPublishers(int page);

    void addJobOffer(Publisher publisher);

    ResponseEntity<?> getByIdUser(Long id);
}
