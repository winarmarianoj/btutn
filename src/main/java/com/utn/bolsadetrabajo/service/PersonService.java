package com.utn.bolsadetrabajo.service;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import org.springframework.http.ResponseEntity;

public interface PersonService {

    ResponseEntity<?> getById(Long id);

    ResponseEntity<?> getByDni(String dni);

    ResponseEntity<?> update(Long id, PersonDTO personDTO);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> save(PersonDTO personDTO) throws PersonException;

    Person getPersonByUsername(String username);

    Person findByIdUser_Id(Long id);
}
