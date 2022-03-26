package com.utn.bolsadetrabajo.service.interfaces;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ReportService {

    void getApplicantExcel(HttpServletResponse response) throws IOException;
}
