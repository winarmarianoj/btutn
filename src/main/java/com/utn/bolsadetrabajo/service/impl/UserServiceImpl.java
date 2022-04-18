package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.controller.UserController;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.UserMapper;
import com.utn.bolsadetrabajo.model.Person;
import com.utn.bolsadetrabajo.model.Role;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.repository.UserRepository;
import com.utn.bolsadetrabajo.security.filter.JwtRequestFilter;
import com.utn.bolsadetrabajo.security.utilSecurity.JwtUtilService;
import com.utn.bolsadetrabajo.service.interfaces.UserService;
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

    private UserRepository repository;
    private ParametersRepository parametersRepository;
    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private JwtRequestFilter jwtRequestFilter;
    private JwtUtilService jwtUtilService;
    private MessageSource messageSource;

    @Autowired
    public UserServiceImpl(UserRepository repository, ParametersRepository parametersRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder,
                           JwtRequestFilter jwtRequestFilter, JwtUtilService jwtUtilService, MessageSource messageSource) {
        this.repository = repository;
        this.parametersRepository = parametersRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtUtilService = jwtUtilService;
        this.messageSource = messageSource;
    }

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
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(messageSource.getMessage("user.activate.failed", new Object[] {username}, null));
        }
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public User update(Person pub, String email, String password) {
        User newUser = userMapper.update(pub.getUser(), email, bCryptPasswordEncoder.encode(password));
        return repository.save(newUser);
    }


}
