package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.exception.PersonException;
import com.utn.bolsadetrabajo.mapper.ApplicantMapper;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.ApplicantRepository;
import com.utn.bolsadetrabajo.service.ApplicantService;
import com.utn.bolsadetrabajo.service.EmailService;
import com.utn.bolsadetrabajo.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicantServiceImpl implements ApplicantService {

    @Autowired ApplicantRepository repository;
    @Autowired EmailService emailService;
    @Autowired ApplicantMapper applicantMapper;
    @Autowired MessageSource messageSource;
    @Autowired Validator validator;

    @Override
    public ResponseEntity<?> getById(Long id) {
        Applicant app = repository.findById(id).get();
        return sendGetApplicantByRequest(app, id);
    }

    @Override
    public ResponseEntity<?> getByIdentification(String dni) {
        Applicant app = repository.findByIdentification(dni);
        return sendGetApplicantByRequest(app, Long.valueOf(dni));
    }

    @Override
    public ResponseEntity<?> getByIdUser(Long id) {return null;}

    @Override
    public ResponseEntity<?> update(Long id, PersonDTO applicantDTO) {
        try{
            Applicant app = repository.findById(id).get();
            Applicant newApplicant = applicantMapper.toUpdate(app, applicantDTO);
            validator.validPerson(newApplicant);
            Applicant aux = repository.save(newApplicant);
            return ResponseEntity.status(HttpStatus.OK).body(applicantMapper.toResponseApplicant(aux, messageSource.getMessage("applicant.update.success", null,null)));
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("applicant.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Applicant applicant = repository.findById(id).get();
            applicant.setDeleted(true);
            applicant.getUser().setState(State.DELETED);
            repository.save(applicant);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("applicant.deleted.success", null,null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("applicant.deleted.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> save(PersonDTO dto) throws PersonException {
        try{
            Applicant app = applicantMapper.toModel(null, dto);
            validator.validPerson(app);
            Applicant applicant = repository.save(app);
            emailService.createEmailPerson(applicant);
            return ResponseEntity.status(HttpStatus.CREATED).body(applicantMapper.toResponseApplicant(applicant, messageSource.getMessage("applicant.created.success", null,null)));
        }catch (PersonException e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("applicant.created.failed",new Object[] {e.getMessage()}, null));
        }
    }

    @Override
    public ResponseEntity<?> getByIdUserApp(User user) {
        Applicant applicant = repository.findByUser(user);
        return sendGetApplicantByRequest(applicant, applicant.getId());
    }

    @Override
    public void postulateJobOffer(Applicant applicant) {
        repository.save(applicant);
    }

    @Override
    public ResponseEntity<?> sendGetApplicantByRequest(Applicant applicant, Long id) {
        try{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(applicantMapper.toResponseApplicant(applicant, messageSource.getMessage("applicant.response.object.success", null,null)));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("applicant.search.failed", new Object[] {id}, null));
        }
    }

    @Override
    public List<Applicant> getAll() {
        return repository.findAll();
    }

}
