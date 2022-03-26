package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Person;
import org.springframework.http.ResponseEntity;

public interface PersonService {

    ResponseEntity<?> sendGetPersonByRequest(Person person, Long id);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> update(Long id, PersonDTO personDTO);

    ResponseEntity<?> save(PersonDTO personDTO) throws PersonException;

    ResponseEntity<?> getAll();

    Person getPersonByUsername(String username);

    ResponseEntity<?> getAllApplicant(int page);

    ResponseEntity<?> getAllPublisher(int page);

}
