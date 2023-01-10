package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.dto.request.JobOfferEvaluationFlutterDTO;
import com.utn.bolsadetrabajo.dto.request.PersonDTO;
import com.utn.bolsadetrabajo.dto.response.ResponsePersonDto;
import com.utn.bolsadetrabajo.mapper.FlutterMapper;
import com.utn.bolsadetrabajo.model.*;
import com.utn.bolsadetrabajo.model.enums.Roles;
import com.utn.bolsadetrabajo.repository.JobOfferRepository;
import com.utn.bolsadetrabajo.repository.UserRepository;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import com.utn.bolsadetrabajo.security.utilSecurity.JwtUtilService;
import com.utn.bolsadetrabajo.service.crud.Readable;
import com.utn.bolsadetrabajo.service.interfaces.*;
import com.utn.bolsadetrabajo.util.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class FlutterServiceImpl implements FlutterService, Urls {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired private JwtUtilService jwtTokenUtil;
    @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private MessageSource messageSource;
    @Autowired private UserRepository userRepository;
    @Autowired private FlutterMapper flutterMapper;
    @Autowired private Readable readableService;
    @Autowired private JobOfferRepository jobOfferRepository;
    @Autowired private Errors errors;
    @Autowired private ApplicantService applicantService;
    @Autowired private PublisherService publisherService;
    @Autowired private PersonService personService;
    @Autowired private RestTemplate restTemplate;

    @Override
    public ResponseEntity<?> createJwtByFlutter(AuthenticationRequest authenticationRequest) {
        User user;
        try {
            user = userRepository.findByUsernameByStateActive(authenticationRequest.getUsername());
            LOGGER.info(user.getUsername() + user.getPassword() + user.getState());
        }catch (Exception e) {
            LOGGER.error("Incorrecto usuario y/o contraseña - {0}. Asegurese que su cuente este Activa." + e.getMessage());
            errors.logError("Incorrecto usuario y/o contraseña - {0}. Asegurese que su cuente este Activa." + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("authentication.create.jwt.failed", new Object[] {authenticationRequest.getUsername()}, null));
        }final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        user.setConected(true);
        User userChanged = userRepository.save(user);
        String jwt = jwtTokenUtil.generateToken(userDetails);
        Person person = null;
        Applicant app = null;
        Publisher pub = null;
        if(user.getRole().getRole().equals(Roles.APPLICANT)){
            app = applicantService.getApplicantByUser(user);
        }else if (user.getRole().getRole().equals(Roles.PUBLISHER)){
            pub = publisherService.getPublisherByUser(user);
        }else{
            person = personService.getPersonByUsername(user.getUsername());
        }
        return ResponseEntity.status(HttpStatus.OK).body(flutterMapper.responseLoginUserJasonByFlutter(userChanged, jwt, person, app, pub));
    }

    @Override
    public ResponseEntity<?> logoutUserFlutter(AuthenticationRequest authenticationRequest) {
        try {
            User user = userRepository.findByUsernameByStateActive(authenticationRequest.getUsername());
            user.setConected(false);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("user.conected",null, null));
        }catch (Exception e){
            LOGGER.error("No se ha podido desloguear correctamente");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("user.isnotconected",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobApplicantAllByApplicantByFlutter(Long id) {
        try {
            Applicant applicant = readableService.getPersonTypeApplicantByIdUser(id);
            return getResponseEntity(applicant.getJobApplications());
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("jobapplicant.all.applicant.failed " + e.getMessage(), null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("jobapplicant.all.applicant.failed", null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobOfferAllByPublisher(Long id) {
        try {
            List<JobOffer> jobOffers = readableService.getPersonTypePublisherByIdUser(id).getJobOfferList();
            return ResponseEntity.status(HttpStatus.OK).body(flutterMapper.toJobOfferList(jobOffers));
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("publisehr.all.joboffer.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisehr.all.joboffer.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getAllAppliedByJobOffer(Long id) {
        try {
            return getResponseEntity(jobOfferRepository.findById(id).get().getJobApplications());
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("jobapplicant.all.applicant.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("jobapplicant.all.applicant.failed",null, null));
        }
    }
    @Override
    public ResponseEntity<?> getJobOfferEvaluation(JobOfferEvaluationFlutterDTO dto){
        return null;
    }

    @Override
    public ResponseEntity<?> create(PersonDTO personDTO) {
        ResponseEntity<?> newEntity = personDTO.getRole().equals(Roles.APPLICANT.name()) ?
                restTemplate.postForEntity(URL_APPLICANT_CREATE_AND_UPDATE, personDTO, ResponsePersonDto.class)
                : restTemplate.postForEntity(URL_PUBLISHER_CREATE_AND_UPDATE, personDTO, ResponsePersonDto.class);
        return ResponseEntity.status(newEntity.getStatusCode()).body(flutterMapper.toResponseCreateUserByFlutterDTO((ResponsePersonDto) Objects.requireNonNull(newEntity.getBody())));
    }

    @Override
    public ResponseEntity<?> getById(Long id) {
        ResponseEntity<?> newEntity = restTemplate.getForEntity(URL_PERSON_CREATE_AND_UPDATE + id, ResponsePersonDto.class);
        return ResponseEntity.status(newEntity.getStatusCode()).body(flutterMapper.toResponseCreateUserByFlutterDTO((ResponsePersonDto) Objects.requireNonNull(newEntity.getBody())));
    }

    private ResponseEntity<?> getResponseEntity(List<JobApplication> jobApplications) {
        return ResponseEntity.status(HttpStatus.OK).body(flutterMapper.toResponseJobApplication(jobApplications,
                messageSource.getMessage("jobapplicant.all.applicant.success",null, null)));
    }

}
