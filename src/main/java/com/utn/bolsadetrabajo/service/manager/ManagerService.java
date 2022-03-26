package com.utn.bolsadetrabajo.service.manager;

import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.model.Publisher;
import org.springframework.http.ResponseEntity;

public interface ManagerService {

    ResponseEntity<?> getById(Long id);

    ResponseEntity<?> getByIdentification(String identification);

    ResponseEntity<?> getByIdUser(Long id);

    Applicant getPersonTypeApplicantByIdUser(Long id);

    Publisher getPersonTypePublisherByIdUser(Long id);
}
