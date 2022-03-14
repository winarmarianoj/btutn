package com.utn.bolsadetrabajo.service;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.service.common.GenericService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PublisherService extends GenericService<PersonDTO> {

    ResponseEntity<?> getByIdUserPub(User user);

    void addJobOffer(Publisher publisher);

    ResponseEntity<?> sendGetPublisherByRequest(Publisher publisher, Long id);

    List<Publisher> getAll();

}
