package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.controller.UserController;
import com.utn.bolsadetrabajo.dto.request.ForgotDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.UserMapper;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Role;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.repository.UserRepository;
import com.utn.bolsadetrabajo.service.interfaces.UserService;
import com.utn.bolsadetrabajo.util.Errors;
import com.utn.bolsadetrabajo.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired private UserRepository repository;
    @Autowired private ParametersRepository parametersRepository;
    @Autowired private UserMapper userMapper;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private MessageSource messageSource;
    @Autowired private Validator validator;
    @Autowired private Errors errors;

    @Override
    public User saveUser(String email, String password, Role role) throws PersonException {
        return userMapper.toModel(email, role, bCryptPasswordEncoder.encode(password));
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        User user = repository.findById(id).get();
        try{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userMapper.toUserResponseDto(user, messageSource.getMessage("user.get.success", new Object[] {id}, null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("user.get.failure " + e.getMessage(),new Object[] {id}, null));
            errors.logError(messageSource.getMessage("user.get.failure " + e.getMessage(),new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("user.get.failure",new Object[] {id}, null));
        }
    }

    @Override
    public User findByIdUser(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public ResponseEntity<?> getAllUsers(int numberPage) {
        int pageSizeParameters = Integer.parseInt(parametersRepository.getSizePage("sizePage"));
        Pageable pageable = PageRequest.of(numberPage, pageSizeParameters);
        Page<User> page = repository.findAll(pageable);
        List<Link> links = new ArrayList<>();

        try{
            if(page.getContent().isEmpty()){ throw new ResponseStatusException(HttpStatus.NO_CONTENT); }
            links.add(linkTo(methodOn(UserController.class).allUsers(numberPage)).withSelfRel());

            if(page.hasPrevious()){
                links.add(linkTo(methodOn(UserController.class).allUsers(numberPage - 1)).withRel("prev"));
            }
            if(page.hasNext()){
                links.add(linkTo(methodOn(UserController.class).allUsers(numberPage + 1)).withRel("next"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(page, links));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("user.all.users.failed " + e.getMessage(),null, null));
            errors.logError(messageSource.getMessage("user.all.users.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("user.all.users.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> activateAccount(String username, String hash) {
        try {
            User user = repository.findByUsernameByState(username);
            if(user.getVerificationCode().equals(hash)){
                user.setState(State.ACTIVE);
                repository.save(user);
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageSource.getMessage("user.activate.success", null,null));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("user.activate.failed " + e.getMessage(), new Object[] {username}, null));
            errors.logError(messageSource.getMessage("user.activate.failed " + e.getMessage(), new Object[] {username}, null));
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(messageSource.getMessage("user.activate.failed", new Object[] {username}, null));
        }
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public User update(Person pub, String email, String password) {
        User newUser = updateUser(pub.getUser(), email, bCryptPasswordEncoder.encode(password));
        return repository.save(newUser);
    }

    @Override
    public ResponseEntity<?> forgot(ForgotDTO forgotDTO) {
        try {
            User user = findByUsername(forgotDTO.getUsername());
            validator.isValidForgot(user, forgotDTO);
            User userModify = updateUser(user, user.getUsername(), bCryptPasswordEncoder.encode(forgotDTO.getFirstPassword()));
            repository.save(userModify);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageSource.getMessage("user.forgot.success", null,null));
        }catch (Exception e){
            errors.logError(messageSource.getMessage("user.forgot.failed " + e.getMessage(), new Object[] {forgotDTO.getUsername()}, null));
            LOGGER.error(messageSource.getMessage("user.forgot.failed " + e.getMessage(), new Object[] {forgotDTO.getUsername()}, null));
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(messageSource.getMessage("user.forgot.failed", new Object[] {forgotDTO.getUsername()}, null));
        }
    }

    @Override
    public User findByUsernameByStateActive(String username) {
        return repository.findByUsernameByStateActive(username);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    private User updateUser(User user, String email, String password){
        return userMapper.update(user, email, password);
    }
}
