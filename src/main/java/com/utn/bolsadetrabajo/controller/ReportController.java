package com.utn.bolsadetrabajo.controller;

import com.utn.bolsadetrabajo.service.ReportService;
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

import static com.utn.bolsadetrabajo.controller.interfaces.Controllers.*;

@RestController
@Api(value = "Report Controller", description = "Controlador con los endpoints que actúan sobre los Report.")
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    ReportService reportService;

    @ApiOperation(value = "${report.exportToExcelApplicant} - Devuelve Archivo Excel de Applicants", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE)
    })
    @GetMapping("/applicant-excel")
    public void exportToExcelApplicant(HttpServletResponse response) throws IOException {
        reportService.getApplicantExcel(response);
    }

}
