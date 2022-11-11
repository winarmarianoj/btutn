package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.mapper.FlutterMapper;
import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.JobApplication;
import com.utn.bolsadetrabajo.model.JobOffer;
import com.utn.bolsadetrabajo.model.User;
import com.utn.bolsadetrabajo.model.enums.State;
import com.utn.bolsadetrabajo.repository.JobOfferRepository;
import com.utn.bolsadetrabajo.repository.UserRepository;
import com.utn.bolsadetrabajo.security.authentication.AuthenticationRequest;
import com.utn.bolsadetrabajo.security.utilSecurity.JwtUtilService;
import com.utn.bolsadetrabajo.service.crud.Readable;
import com.utn.bolsadetrabajo.service.interfaces.FlutterService;
import com.utn.bolsadetrabajo.util.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlutterServiceImpl implements FlutterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtilService jwtTokenUtil;
    @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private MessageSource messageSource;
    @Autowired private UserRepository userRepository;
    @Autowired private FlutterMapper flutterMapper;
    @Autowired private Readable readableService;
    @Autowired private JobOfferRepository jobOfferRepository;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> createJwtByFlutter(AuthenticationRequest authenticationRequest) {
        User user = userRepository.findByUsernameByStateActive(authenticationRequest.getUsername());
        try {
            if (user.getState().equals(State.ACTIVE)){
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            }
        }catch (BadCredentialsException e) {
            LOGGER.error(messageSource.getMessage("authentication.create.jwt.failed " + e.getMessage(), new Object[] {e}, null));
            errors.logError(messageSource.getMessage("authentication.create.jwt.failed " + e.getMessage(), new Object[] {e}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("authentication.create.jwt.failed", new Object[] {authenticationRequest.getUsername()}, null));
        }final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(flutterMapper.responseLoginUserJasonByFlutter(user, jwt));
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

    private ResponseEntity<?> getResponseEntity(List<JobApplication> jobApplications) {
        return ResponseEntity.status(HttpStatus.OK).body(flutterMapper.toResponseJobApplication(jobApplications));
    }

}
