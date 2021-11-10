package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.mapper.UserDetailsMapper;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepo.findByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("Invalid username or password or user not found");

        return UserDetailsMapper.build(user);
    }

}
