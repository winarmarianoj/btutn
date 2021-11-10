package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.controller.ApplicantController;
import com.utn.bolsadetrabajo.dto.request.ApplicantDTO;
import com.utn.bolsadetrabajo.dto.response.ResponseApplicantList;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.ApplicantMapper;
import com.utn.bolsadetrabajo.mapper.PersonMapper;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.ApplicantRepository;
import com.utn.bolsadetrabajo.repository.ParametersRepository;
import com.utn.bolsadetrabajo.service.ApplicantService;
import com.utn.bolsadetrabajo.service.EmailService;
import com.utn.bolsadetrabajo.validation.Validator;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class ApplicantServiceImpl implements ApplicantService {

    private ApplicantRepository repository;
    private ParametersRepository parametersRepository;
    private EmailService emailService;
    private ApplicantMapper applicantMapper;
    private PersonMapper personMapper;
    private MessageSource messageSource;
    private Validator validator;

    private List<Link> links;

    @Autowired
    public ApplicantServiceImpl(ApplicantRepository repository, ParametersRepository parametersRepository, ApplicantMapper applicantMapper, MessageSource messageSource, PersonMapper personMapper, EmailService emailService, Validator validator) {
        this.repository = repository;
        this.parametersRepository = parametersRepository;
        this.applicantMapper = applicantMapper;
        this.messageSource = messageSource;
        this.personMapper = personMapper;
        this.emailService = emailService;
        this.validator = validator;
    }

    @Override
    public ResponseEntity<?> save(ApplicantDTO dto) throws PersonException {
        try{
            Applicant app = applicantMapper.toModel(null, dto);
            validator.validPerson(app);
            Applicant applicant = repository.save(app);
            emailService.createEmailPerson(applicant);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    personMapper.toResponsePerson(applicant,
                            messageSource.getMessage("applicant.created.success", null,null)));
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(messageSource.getMessage("applicant.created.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> getApplicantById(Long id) {
        Applicant app = repository.findById(id).get();
        return sendGetApplicantByRequest(app, String.valueOf(id));
    }

    @Override
    public ResponseEntity<?> getApplicantByDni(String dni) {
        Applicant app = repository.findByIdentification(dni);
        return sendGetApplicantByRequest(app, dni);
    }

    @Override
    public ResponseEntity<?> update(Long id, ApplicantDTO applicantDTO) {
        try{
            Applicant app = repository.findById(id).get();
            Applicant newApplicant = applicantMapper.toModel(app, applicantDTO);
            validator.validPerson(newApplicant);
            Applicant aux = repository.save(newApplicant);
            return ResponseEntity.status(HttpStatus.OK).body(
                    personMapper.toResponsePerson(aux,
                            messageSource.getMessage("applicant.update.success", null,null))
            );
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .body(messageSource.getMessage("applicant.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Applicant app = repository.findById(id).get();
            app.setDeleted(true);
            app.getUser().setState(State.DELETED);
            repository.save(app);
            return ResponseEntity.status(HttpStatus.OK).body(
                            messageSource.getMessage("applicant.deleted.success", null,null)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("applicant.deleted.failed",
                            new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAllApplicants(int numberPage) {
        int pageSizeParameters = Integer.parseInt(parametersRepository.getSizePage("sizePage"));
        Pageable pageable = PageRequest.of(numberPage, pageSizeParameters);
        List<Applicant> applicants = repository.findAll();
        List<ResponseApplicantList> lists = applicantMapper.toApplicantList(applicants);
        Page<ResponseApplicantList> page = new PageImpl<>(lists, pageable, lists.size());

        links = new ArrayList<>();
        try{
            if(page.getContent().isEmpty()){ throw new ResponseStatusException(HttpStatus.NO_CONTENT); }
            links.add(linkTo(methodOn(ApplicantController.class).getAll(numberPage)).withSelfRel());

            if(page.hasPrevious()){
                links.add(linkTo(methodOn(ApplicantController.class)
                        .getAll(numberPage - 1)).withRel("prev"));
            }
            if(page.hasNext()){
                links.add(linkTo(methodOn(ApplicantController.class)
                        .getAll(numberPage + 1)).withRel("next"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(CollectionModel.of(page, links));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("applicant.all.applicant.failed",
                            null, null));
        }
    }

    @Override
    public void postulateJobOffer(Applicant applicant) {
        repository.save(applicant);
    }

    private ResponseEntity<?> sendGetApplicantByRequest(Applicant app, String request) {
        try{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    personMapper.toResponsePerson(app,
                            messageSource.getMessage("applicant.response.object.success", null,null))
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(messageSource.getMessage("applicant.search.failed",
                            new Object[] {request}, null));
        }
    }
}
