package com.utn.bolsadetrabajo.service.reports;

import com.utn.bolsadetrabajo.controller.PersonController;
import com.utn.bolsadetrabajo.dto.response.ResponsePersonDto;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class GenerateListTypePerson {

    @Autowired ParametersRepository parametersRepository;

    private static final String SIZE_PAGE = "sizePage";
    private static final String PREV = "prev";
    private static final String NEXT = "next";
    private static final int AMOUNT_PAGE = 1;

    public Page<ResponsePersonDto> getPage(List<ResponsePersonDto> lists, int numberPage) {
        int pageSizeParameters = Integer.parseInt(parametersRepository.getSizePage(SIZE_PAGE));
        Pageable pageable = PageRequest.of(numberPage, pageSizeParameters);
        return new PageImpl<>(lists, pageable, lists.size());
    }

    /*public List<Link> generatePersonLinks(Page<ResponsePersonDto> page, int numberPage) {
        List<Link> links = new ArrayList<>();
        if(page.getContent().isEmpty()){ throw new ResponseStatusException(HttpStatus.NO_CONTENT); }
        links.add(linkTo(methodOn(PersonController.class).getAll(numberPage)).withSelfRel());

        if(page.hasPrevious()){
            links.add(linkTo(methodOn(PersonController.class).getAll(numberPage - AMOUNT_PAGE)).withRel(PREV));
        }
        if(page.hasNext()){
            links.add(linkTo(methodOn(PersonController.class).getAll(numberPage + AMOUNT_PAGE)).withRel(NEXT));
        }
        return links;
    }*/

    public List<Link> generateApplicantLinks(Page<ResponsePersonDto> page, int numberPage) {
        List<Link> links = new ArrayList<>();
        if(page.getContent().isEmpty()){ throw new ResponseStatusException(HttpStatus.NO_CONTENT); }
        links.add(linkTo(methodOn(PersonController.class).getAllApplicant(numberPage)).withSelfRel());

        if(page.hasPrevious()){
            links.add(linkTo(methodOn(PersonController.class).getAllApplicant(numberPage - AMOUNT_PAGE)).withRel(PREV));
        }
        if(page.hasNext()){
            links.add(linkTo(methodOn(PersonController.class).getAllApplicant(numberPage + AMOUNT_PAGE)).withRel(NEXT));
        }
        return links;
    }

    public List<Link> generatePublisherLinks(Page<ResponsePersonDto> page, int numberPage) {
        List<Link> links = new ArrayList<>();
        if(page.getContent().isEmpty()){ throw new ResponseStatusException(HttpStatus.NO_CONTENT); }
        links.add(linkTo(methodOn(PersonController.class).getAllPublisher(numberPage)).withSelfRel());

        if(page.hasPrevious()){
            links.add(linkTo(methodOn(PersonController.class).getAllPublisher(numberPage - AMOUNT_PAGE)).withRel(PREV));
        }
        if(page.hasNext()){
            links.add(linkTo(methodOn(PersonController.class).getAllPublisher(numberPage + AMOUNT_PAGE)).withRel(NEXT));
        }
        return links;
    }
}
