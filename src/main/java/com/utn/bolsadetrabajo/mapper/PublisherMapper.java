package com.utn.bolsadetrabajo.mapper;

import com.utn.bolsadetrabajo.dto.request.PublisherDTO;
import com.utn.bolsadetrabajo.dto.response.ResponsePublisherDto;
import com.utn.bolsadetrabajo.dto.response.ResponsePublisherList;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.model.Publisher;
import com.utn.bolsadetrabajo.model.Role;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.Roles;
import com.utn.bolsadetrabajo.repository.RoleRepository;
import com.utn.bolsadetrabajo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Component
public class PublisherMapper {

    private final RoleRepository roleRepository;
    private final UserService userService;

    @Autowired
    public PublisherMapper(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    public Publisher toModel(Publisher pub, PublisherDTO dto) throws PersonException {
        Role role = roleRepository.getByRole(Roles.PUBLISHER);
        User user = userService.saveUser(dto.getEmail(), dto.getPassword(), role);
        if (pub == null) pub = new Publisher();

        buildPerson(pub, dto);
        pub.setUser(user);
        return pub;
    }

    public Publisher toUpdate(Publisher pub, PublisherDTO dto) {
        buildPerson(pub, dto);
        pub.setUser(userService.update(pub, dto.getEmail(), dto.getPassword()));
        return pub;
    }

    private void buildPerson(Publisher pub, PublisherDTO dto){
        pub.setOficialName(dto.getOficialName());
        pub.setLastName(dto.getLastName());
        pub.setIdentification(dto.getCuit());
        pub.setPhoneNumber(dto.getPhoneNumber());
        pub.setWebPage(dto.getWebPage());
        pub.setDeleted(false);
    }

    public List<ResponsePublisherList> toPublisherList(List<Publisher> publishers) {
        ResponsePublisherList res = new ResponsePublisherList();
        List<ResponsePublisherList> list = new ArrayList<>();
        for (Publisher pub : publishers) {
            res.setOficialName(pub.getOficialName());
            res.setLastName(pub.getOficialName());
            res.setPhoneNumber(pub.getPhoneNumber());
            res.setDni(pub.getIdentification());
            res.setEmail(pub.getUser().getUsername());
            res.setWeb(pub.getWebPage());
            list.add(res);
        }
        return list;
    }

    public ResponsePublisherDto toResponsePublisher(Publisher publisher, String message) {
        ResponsePublisherDto dto = ResponsePublisherDto.builder()
                .id(publisher.getId())
                .oficialName(publisher.getOficialName())
                .lastName(publisher.getLastName())
                .cuit(publisher.getIdentification())
                .phoneNumber(publisher.getPhoneNumber())
                .email(publisher.getUser().getUsername())
                .webPage(publisher.getWebPage())
                .message(message)
                .uri(ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/publisher/{id}")
                        .buildAndExpand(publisher.getUser().getUserId()).toUri())
                .build();
        return dto;
    }

}


