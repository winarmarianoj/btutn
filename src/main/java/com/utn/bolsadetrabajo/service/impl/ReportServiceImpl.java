package com.utn.bolsadetrabajo.service.impl;

import com.utn.bolsadetrabajo.service.interfaces.ReportService;
import com.utn.bolsadetrabajo.service.reports.impl.ApplicantReportImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class ReportServiceImpl implements ReportService {

    private ApplicantReportImpl applicantReportImpl;

    @Autowired
    public ReportServiceImpl(ApplicantReportImpl applicantReportImpl) {
        this.applicantReportImpl = applicantReportImpl;
    }

    @Override
    public void getApplicantExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headervalue = "attachment; filename=Applicant_info.xlsx";

        response.setHeader(headerKey, headervalue);
        applicantReportImpl.export(response);
    }

}
