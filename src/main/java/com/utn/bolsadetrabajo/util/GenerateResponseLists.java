package com.utn.bolsadetrabajo.util;

import com.utn.bolsadetrabajo.controller.JobOfferController;
import com.utn.bolsadetrabajo.dto.response.ResponseJobApplicationDto;
import com.utn.bolsadetrabajo.dto.response.ResponseJobOfferDto;
import com.utn.bolsadetrabajo.model.JobOffer;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GenerateResponseLists {


    private ParametersRepository parametersRepository;

    public GenerateResponseLists(ParametersRepository parametersRepository) {
        this.parametersRepository = parametersRepository;
    }

    public Pageable generatePageable(int number) {
        int pageSizeParameters = Integer.parseInt(parametersRepository.getSizePage("sizePage"));
        return PageRequest.of(number, pageSizeParameters);
    }

    public List<Link> getAllWithFilter(int numberPage, Page<JobOffer> page, Class<JobOfferController> jobOfferControllerClass) {
        List<Link> links = new ArrayList<>();
        if (page.getContent().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        links.add(linkTo(methodOn(jobOfferControllerClass).getAll(numberPage)).withSelfRel());

        if (page.hasPrevious()) {
            links.add(linkTo(methodOn(jobOfferControllerClass).getAll(numberPage - 1)).withRel("prev"));
        }
        if (page.hasNext()) {
            links.add(linkTo(methodOn(jobOfferControllerClass).getAll(numberPage + 1)).withRel("next"));
        }

        return links;
    }

    public List<Link> getJobApplicantAllByApplicant(int numberPage, Page<ResponseJobApplicationDto> page, Class<JobOfferController> jobOfferControllerClass) {
        List<Link> links = new ArrayList<>();
        if (page.getContent().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        links.add(linkTo(methodOn(jobOfferControllerClass).getJobApplicantAllByApplicant(numberPage)).withSelfRel());

        if (page.hasPrevious()) {
            links.add(linkTo(methodOn(jobOfferControllerClass).getJobApplicantAllByApplicant(numberPage - 1)).withRel("prev"));
        }
        if (page.hasNext()) {
            links.add(linkTo(methodOn(jobOfferControllerClass).getJobApplicantAllByApplicant(numberPage + 1)).withRel("next"));
        }

        return links;
    }


    public List<Link> getAll(int numberPage, Page<ResponseJobOfferDto> page, Class<JobOfferController> jobOfferControllerClass) {
        List<Link> links = new ArrayList<>();
        if (page.getContent().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        links.add(linkTo(methodOn(jobOfferControllerClass).getAll(numberPage)).withSelfRel());

        if (page.hasPrevious()) {
            links.add(linkTo(methodOn(jobOfferControllerClass).getAll(numberPage - 1)).withRel("prev"));
        }
        if (page.hasNext()) {
            links.add(linkTo(methodOn(jobOfferControllerClass).getAll(numberPage + 1)).withRel("next"));
        }

        return links;
    }
}
