package com.utn.bolsadetrabajo.service.reports.impl;

import com.utn.bolsadetrabajo.model.Applicant;
import com.utn.bolsadetrabajo.repository.ApplicantRepository;
import com.utn.bolsadetrabajo.service.reports.ApplicantReport;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ApplicantReportImpl implements ApplicantReport {

    @Autowired private ApplicantRepository applicantRepository;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @Autowired
    public ApplicantReportImpl(ApplicantRepository applicantRepository) {
        this.workbook = new XSSFWorkbook();
        this.applicantRepository = applicantRepository;
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();

        ServletOutputStream outputStream=response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell=row.createCell(columnCount);
        if(value instanceof Long) {
            cell.setCellValue((Long) value);
        }else if(value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }else if(value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue(String.valueOf(value));
        }
        cell.setCellStyle(style);
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Applicant");

        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(40);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        createCell(row,0,"Applicant Information",style);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));
        font.setFontHeightInPoints((short)(10));

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "NOMBRE OFICIAL", style);
        createCell(row, 2, "SEGUNDO NOMBRE", style);
        createCell(row, 3, "IDENTIFICACION", style);
        createCell(row, 4, "TELEFONO", style);
        createCell(row, 5, "BORRADO", style);
        createCell(row, 6, "GENERO", style);
        createCell(row, 7, "CUMPLEAÑOS", style);
        createCell(row, 8, "TIPO ESTUDIANTE", style);
        createCell(row, 9, "USERNAME", style);

        int rowCount=2;

        CellStyle style1=workbook.createCellStyle();
        XSSFFont font1=workbook.createFont();
        font.setFontHeight(14);
        style1.setFont(font1);

        List<Applicant> applicants = applicantRepository.findAll();
        for(Applicant app : applicants) {
            Row row1 = sheet.createRow(rowCount++);
            int columnCount=0;
            createCell(row1, columnCount++, app.getId(), style1);
            createCell(row1, columnCount++, app.getOficialName(), style1);
            createCell(row1, columnCount++, app.getLastName(), style1);
            createCell(row1, columnCount++, app.getIdentification(), style1);
            createCell(row1, columnCount++, app.getPhoneNumber(), style1);
            createCell(row1, columnCount++, app.isDeleted(), style1);
            createCell(row1, columnCount++, app.getGenre().name(), style1);
            createCell(row1, columnCount++, app.getBirthDate(), style1);
            createCell(row1, columnCount++, app.getTypeStudent().name(), style1);
            createCell(row1, columnCount++, app.getUser().getUsername(), style1);
        }
    }


}
