package com.utn.bolsadetrabajo.service.reports.impl;

import com.utn.bolsadetrabajo.controller.ReportListsController;
import com.utn.bolsadetrabajo.dto.response.ResponseJobOfferDto;
import com.utn.bolsadetrabajo.mapper.JobApplicationMapper;
import com.utn.bolsadetrabajo.mapper.JobOfferMapper;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Category;
import com.utn.bolsadetrabajo.model.JobApplication;
import com.utn.bolsadetrabajo.model.JobOffer;
import com.utn.bolsadetrabajo.repository.JobOfferRepository;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.service.crud.Readable;
import com.utn.bolsadetrabajo.service.reports.ReportLists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ReportListsImpl implements ReportLists {

    @Autowired private ParametersRepository parametersRepository;
    @Autowired private JobOfferRepository jobOfferRepository;
    @Autowired private MessageSource messageSource;
    @Autowired private JobOfferMapper jobOfferMapper;
    @Autowired private Readable readableService;
    @Autowired private JobApplicationMapper jobApplicationMapper;

    @Override
    public Pageable generatePageable(int number) {
        int pageSizeParameters = Integer.parseInt(parametersRepository.getSizePage("sizePage"));
        return PageRequest.of(number, pageSizeParameters);
    }

    @Override
    public ResponseEntity<?> getAllWithPage(int numberPage) {
        try {
            List<JobOffer> jobOffers = jobOfferRepository.findAll();
            Pageable pageable = generatePageable(numberPage);
            List<ResponseJobOfferDto> lists = jobOfferMapper.toJobOfferListSimplePublisher(jobOffers);
            Page<ResponseJobOfferDto> page = new PageImpl<>(lists, pageable, lists.size());
            List<Link> links = getJobOfferAllLinks(numberPage, page);
            return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(page, links));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("joboffer.all.joboffer.failed",
                            null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobOfferAllWithFilter(String state) {
        try {
            List<JobOffer> jobOffers = jobOfferRepository.findAllByState(state);
            return ResponseEntity.status(HttpStatus.OK).body(jobOfferMapper.toJobOfferListSimplePublisher(jobOffers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("joboffer.all.joboffer.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobApplicantAllByApplicant(Long id) {
        try {
            Applicant applicant = readableService.getPersonTypeApplicantByIdUser(id);
            return getResponseEntity(applicant.getJobApplications());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("jobapplicant.all.applicant.failed", null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobApplicantAllByJobOfferSimplePublisher(Long id) {
        try {
            return getResponseEntity(jobOfferRepository.findById(id).get().getJobApplications());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("jobapplicant.all.applicant.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobOfferAllByPublisher(Long id) {
        try {
            List<JobOffer> jobOffers = readableService.getPersonTypePublisherByIdUser(id).getJobOfferList();
            return ResponseEntity.status(HttpStatus.OK).body(jobOfferMapper.toJobOfferList(jobOffers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisehr.all.joboffer.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobOfferAllSimplePublisher(Category filter, Long id) {
        try {
            List<JobOffer> jobOffers = readableService.getPersonTypePublisherByIdUser(id).getJobOfferList();
            return ResponseEntity.status(HttpStatus.OK).body(jobOfferMapper.toJobOfferListSimplePublisherByFilter(jobOffers, filter));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisehr.all.joboffer.failed",null, null));
        }
    }

    private ResponseEntity<?> getResponseEntity(List<JobApplication> jobApplications) {
        return ResponseEntity.status(HttpStatus.OK).body(jobApplicationMapper.toResponseJobApplication(jobApplications));
    }

    private List<Link> getJobOfferAllLinks(int numberPage, Page<ResponseJobOfferDto> page) {
        List<Link> links = new ArrayList<>();
        if (page.getContent().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        links.add(linkTo(methodOn(ReportListsController.class).getAllWithPage(numberPage)).withSelfRel());

        if (page.hasPrevious()) {
            links.add(linkTo(methodOn(ReportListsController.class).getAllWithPage(numberPage - 1)).withRel("prev"));
        }
        if (page.hasNext()) {
            links.add(linkTo(methodOn(ReportListsController.class).getAllWithPage(numberPage + 1)).withRel("next"));
        }

        return links;
    }

}
