package com.utn.bolsadetrabajo.controller;

import com.utn.bolsadetrabajo.controller.interfaces.Messages;
import com.utn.bolsadetrabajo.service.reports.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Api(value = "Report Controller", description = "Controlador con los endpoints que actúan sobre los Report.")
@RequestMapping("/reports")
public class ReportController implements Messages {
    @Autowired
    ReportService reportService;

    @ApiOperation(value = "${report.exportToExcelApplicant} - Devuelve Archivo Excel de Applicants", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 202, message = ACCEPTED),
            @ApiResponse(code = 304, message = NOT_MODIFIED),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 406, message = NOT_ACCEPTABLE)
    })
    @GetMapping("/applicant-excel")
    public void exportToExcelApplicant(HttpServletResponse response) throws IOException {
        reportService.getApplicantExcel(response);
    }

}
