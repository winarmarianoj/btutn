package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.service.ReportService;
import com.utn.bolsadetrabajo.util.reports.ApplicantReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class ReportServiceImpl implements ReportService {

    private ApplicantReport applicantReport;

    @Autowired
    public ReportServiceImpl(ApplicantReport applicantReport) {
        this.applicantReport = applicantReport;
    }

    @Override
    public void getApplicantExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headervalue = "attachment; filename=Applicant_info.xlsx";

        response.setHeader(headerKey, headervalue);
        applicantReport.export(response);
    }

}
