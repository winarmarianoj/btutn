package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.mapper.UserDetailsMapper;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.repository.UserRepository;
import com.utn.bolsadetrabajo.util.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired private UserRepository userRepo;
    @Autowired private Errors errors;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepo.findByUsername(username);
        if(user == null){
            LOGGER.error("Invalid username or password or user not found in UserDetailsServiceImpl");
            errors.logError("Invalid username or password or user not found in UserDetailsServiceImpl");
            throw new UsernameNotFoundException("Invalid username or password or user not found in UserDetailsServiceImpl");
        }

        return UserDetailsMapper.build(user);
    }

}
