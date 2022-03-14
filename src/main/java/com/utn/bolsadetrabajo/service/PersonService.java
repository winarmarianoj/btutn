package com.utn.bolsadetrabajo.service;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Person;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface PersonService {
    String SIZE_PAGE = "sizePage";
    String PREV = "prev";
    String NEXT = "next";
    List<Link> links = new ArrayList<>();

    ResponseEntity<?> sendGetPersonByRequest(Person person, Long id);

    ResponseEntity<?> update(Person person, PersonDTO personDTO);

    ResponseEntity<?> delete(Person person);

    ResponseEntity<?> save(PersonDTO personDTO) throws PersonException;

    ResponseEntity<?> getAll(int numberPage);

    Person getPersonByUsername(String username);

    ResponseEntity<?> getAllApplicant(int page);

    ResponseEntity<?> getAllPublisher(int page);
}
