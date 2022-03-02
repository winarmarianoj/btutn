package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.controller.JobOfferController;
import com.utn.bolsadetrabajo.dto.request.JobOfferDTO;
import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationDTO;
import com.utn.bolsadetrabajo.dto.response.ResponseJobApplicationDto;
import com.utn.bolsadetrabajo.dto.response.ResponseJobOfferDto;
import com.utn.bolsadetrabajo.exception.JobOfferException;
import com.utn.bolsadetrabajo.mapper.JobApplicationMapper;
import com.utn.bolsadetrabajo.mapper.JobOfferMapper;
import com.utn.bolsadetrabajo.model.*;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.JobApplicationRepository;
import com.utn.bolsadetrabajo.repository.JobOfferRepository;
import com.utn.bolsadetrabajo.service.*;
import com.utn.bolsadetrabajo.util.GenerateResponseLists;
import com.utn.bolsadetrabajo.util.UserConnectedService;
import com.utn.bolsadetrabajo.validation.ValidJobOffer;
import com.utn.bolsadetrabajo.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobOfferServiceImpl implements JobOfferService {

    private JobOfferRepository repository;
    private JobApplicationRepository jobApplicationRepository;
    private JobOfferMapper mapper;
    private MessageSource messageSource;
    private EmailService emailService;
    private PublisherService publisherService;
    private ApplicantService applicantService;
    private UserConnectedService userConnectedService;
    private PersonService personService;
    private JobApplicationMapper jobApplicationMapper;
    private GenerateResponseLists generateResponseLists;
    private Validator validJobOffer;

    @Autowired
    public JobOfferServiceImpl(JobOfferRepository repository, JobApplicationRepository jobApplicationRepository, JobOfferMapper mapper, MessageSource messageSource, EmailService emailService, PublisherService publisherService, ApplicantService applicantService, UserConnectedService userConnectedService, PersonService personService, JobApplicationMapper jobApplicationMapper, GenerateResponseLists generateResponseLists, Validator validJobOffer) {
        this.repository = repository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.mapper = mapper;
        this.messageSource = messageSource;
        this.emailService = emailService;
        this.publisherService = publisherService;
        this.applicantService = applicantService;
        this.userConnectedService = userConnectedService;
        this.personService = personService;
        this.jobApplicationMapper = jobApplicationMapper;
        this.generateResponseLists = generateResponseLists;
        this.validJobOffer = validJobOffer;
    }

    @Override
    public ResponseEntity<?> save(JobOfferDTO jobOfferDTO) {
        String username = userConnectedService.userConected();
        Publisher publisher = (Publisher) personService.getPersonByUsername(username);

        try {
            JobOffer newJobOffer = mapper.toModel(jobOfferDTO, publisher);
            validJobOffer.validJobOffer(newJobOffer);
            JobOffer jobOffer = repository.save(newJobOffer);
            publisher.getJobOfferList().add(jobOffer);
            publisherService.addJobOffer(publisher);
            emailService.createEmailJobOfferPublicated(jobOffer, publisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    mapper.toResponsePublisherJobOffer(jobOffer,
                            messageSource.getMessage("joboffer.created.success", null, null)));
        } catch (JobOfferException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("joboffer.created.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobOfferById(Long id) {
        try {
            JobOffer jobOffer = repository.findById(id).get();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    mapper.toResponsePublisherJobOffer(jobOffer,
                            messageSource.getMessage("joboffer.response.object.success", null, null))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("joboffer.search.failed",
                            new Object[]{id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long id, JobOfferDTO jobOfferDTO) {
        try {
            JobOffer jobOffer = repository.findById(id).get();
            JobOffer newJobOffer = mapper.updateJobOffer(jobOffer, jobOfferDTO);
            validJobOffer.validJobOffer(newJobOffer);
            JobOffer aux = repository.save(newJobOffer);
            return ResponseEntity.status(HttpStatus.OK).body(
                    mapper.toResponsePublisherJobOffer(aux,
                            messageSource.getMessage("joboffer.update.success", null, null))
            );
        } catch (JobOfferException e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .body(messageSource.getMessage("joboffer.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try {
            JobOffer jobOffer = repository.findById(id).get();
            jobOffer.setDeleted(true);
            jobOffer.setDeletedDay(LocalDate.now());
            repository.save(jobOffer);
            return ResponseEntity.status(HttpStatus.OK).body(
                    messageSource.getMessage("joboffer.deleted.success", null, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("joboffer.deleted.failed",
                            new Object[]{id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAllWithFilter(int numberPage, State state) {
        try {
            List<JobOffer> jobOffers = repository.findAllByState(state.toString());
            Pageable pageable = generateResponseLists.generatePageable(numberPage);
            List<ResponseJobOfferDto> lists = mapper.toJobOfferListSimplePublisher(jobOffers);
            Page<ResponseJobOfferDto> page = new PageImpl<>(lists, pageable, lists.size());
            //List<Link> links = generateResponseLists.getAllWithFilter(numberPage, page, JobOfferController.class);
            return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(page));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("joboffer.all.joboffer.failed",
                            null, null));
        }
    }

    @Override
    public ResponseEntity<?> postulate(Long id) {
        try {
            String username = userConnectedService.userConected();
            Applicant applicant = (Applicant) personService.getPersonByUsername(username);
            JobOffer jobOffer = repository.findById(id).get();
            JobApplication jobApplication = saveJobApplication(applicant, jobOffer);
            applicant.getJobApplications().add(jobApplication);
            jobOffer.getJobApplications().add(jobApplication);
            applicantService.postulateJobOffer(applicant);
            repository.save(jobOffer);
            emailService.createEmailPostulate(jobOffer, applicant);
            return ResponseEntity.status(HttpStatus.OK).body(
                    messageSource.getMessage("applicant.postulate.success", null, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("applicant.postulate.failed",
                            new Object[]{id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobApplicantAllByApplicant(int numberPage) {
        try {
            Pageable pageable = generateResponseLists.generatePageable(numberPage);
            String username = userConnectedService.userConected();
            Applicant applicant = (Applicant) personService.getPersonByUsername(username);
            return getResponseEntity(numberPage, pageable, applicant.getJobApplications());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("jobapplicant.all.applicant.failed",
                            null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobApplicantAllByJobOfferSimplePublisher(int numberPage, Long id) {
        try {
            Pageable pageable = generateResponseLists.generatePageable(numberPage);
            JobOffer jobOffer = repository.findById(id).get();
            return getResponseEntity(numberPage, pageable, jobOffer.getJobApplications());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("jobapplicant.all.applicant.failed",
                            null, null));
        }
    }

    @Override
    public ResponseEntity<?> getEvaluationAllJobOffers(JobOfferEvaluationDTO jobOfferEvaluationDTO) {
        try {
            JobOffer jobOffer = repository.findById(jobOfferEvaluationDTO.getId()).get();
            JobOffer jobOfferModify = mapper.modifyJobOffer(jobOffer, jobOfferEvaluationDTO);
            JobOffer aux = repository.save(jobOfferModify);
            return ResponseEntity.status(HttpStatus.OK).body(
                    mapper.toResponsePublisherJobOffer(aux,
                            messageSource.getMessage("joboffer.evaluation.success", null, null))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("joboffer.evaluation.failed",
                            new Object[]{jobOfferEvaluationDTO.getId()}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAllJobOfferSimplePublisher(int numberPage, Category filter) {
        try {
            Pageable pageable = generateResponseLists.generatePageable(numberPage);
            String username = userConnectedService.userConected();
            Publisher publisher = (Publisher) personService.getPersonByUsername(username);
            List<JobOffer> jobOffers = publisher.getJobOfferList();
            List<ResponseJobOfferDto> lists = mapper.toJobOfferListSimplePublisherByFilter(jobOffers, filter);
            Page<ResponseJobOfferDto> page = new PageImpl<>(lists, pageable, lists.size());
            //List<Link> links = generateResponseLists.getAllJobOfferSimplePublisher(numberPage, page, JobOfferController.class);
            return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(page));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("publisehr.all.joboffer.failed",
                            null, null));
        }
    }

    @Override
    public ResponseEntity<?> getAll(int numberPage) {
        try {
            List<JobOffer> jobOffers = repository.findAll();
            Pageable pageable = generateResponseLists.generatePageable(numberPage);
            List<ResponseJobOfferDto> lists = mapper.toJobOfferListSimplePublisher(jobOffers);
            Page<ResponseJobOfferDto> page = new PageImpl<>(lists, pageable, lists.size());
            List<Link> links = generateResponseLists.getAll(numberPage, page, JobOfferController.class);
            return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(page, links));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("joboffer.all.joboffer.failed",
                            null, null));
        }
    }

    @Override
    public ResponseEntity<?> getAllWithoutPage() {
        try {
            List<JobOffer> jobOffers = repository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toJobOfferListSimplePublisher(jobOffers));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("joboffer.all.joboffer.failed",
                            null, null));
        }
    }

    private JobApplication saveJobApplication (Applicant applicant, JobOffer jobOffer){
        JobApplication job = mapper.toModelJobApplication(applicant, jobOffer);
        for (JobApplication ele : applicant.getJobApplications()) {
            if (ele.getJobOffer().getTitle().equals(jobOffer.getTitle())) {
                return null;
            }
        }
        return jobApplicationRepository.save(job);
    }

    private ResponseEntity<?> getResponseEntity(int numberPage, Pageable pageable, List<JobApplication> jobApplications2) {
        List<JobApplication> jobApplications = jobApplications2;
        List<ResponseJobApplicationDto> lists = jobApplicationMapper.toResponseJobApplication(jobApplications);
        Page<ResponseJobApplicationDto> page = new PageImpl<>(lists, pageable, lists.size());
        List<Link> links = generateResponseLists.getJobApplicantAllByApplicant(numberPage, page, JobOfferController.class);
        return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(page, links));
    }

}