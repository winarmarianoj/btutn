package com.utn.bolsadetrabajo.service.reports;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ApplicantReport {

    void export(HttpServletResponse response) throws IOException;
}
