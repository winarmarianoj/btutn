package com.utn.bolsadetrabajo.service.interfaces;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.service.crud.Removable;
import com.utn.bolsadetrabajo.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

public interface PersonService extends Removable, Writeable<PersonDTO> {

    ResponseEntity<?> sendGetPersonByRequest(Person person, Long id);

    ResponseEntity<?> getAll();

    Person getPersonByUsername(String username);

    ResponseEntity<?> getAllApplicant(int page);

    ResponseEntity<?> getAllPublisher(int page);

}
