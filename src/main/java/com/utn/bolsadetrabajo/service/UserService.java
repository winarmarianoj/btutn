package com.utn.bolsadetrabajo.service;

import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.Role;
import com.utn.bolsadetrabajo.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    User saveUser(String email, String password, Role role) throws PersonException;

    ResponseEntity<?> findById(Long id);

    User findByIdUser(Long id);

    ResponseEntity<?> getAllUsers(int page);

    ResponseEntity<?> activateAccount(String username, String hash);

    User findByUsername(String username);

    User update(Person pub, String email, String password);
}
