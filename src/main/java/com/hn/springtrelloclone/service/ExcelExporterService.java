package com.hn.springtrelloclone.service;

import com.hn.springtrelloclone.dao.ExcelExportDAO;
import com.hn.springtrelloclone.dto.ExcelExportDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExporterService {

    private final ExcelExportDAO excelExportDAO;

    private XSSFSheet sheet;

    public List<ExcelExportDTO> listAll() {
        return excelExportDAO.getWorkDetail();
    }


    private void writeHeaderLine(XSSFWorkbook workbook) {
        sheet = workbook.createSheet("trello_clone");

        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);

        createCell(row, 0, "Board Name", style);
        createCell(row, 1, "List Name", style);
        createCell(row, 2, "Card Name", style);
        createCell(row, 3, "First Name", style);
        createCell(row, 4, "Last Name", style);
        createCell(row, 5, "E-mail", style);
        createCell(row, 6, "About Account", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(XSSFWorkbook workbook) {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        style.setFont(font);

        for (ExcelExportDTO data : listAll()) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, data.getBoardName(), style);
            createCell(row, columnCount++, data.getListName(), style);
            createCell(row, columnCount++, data.getCardName(), style);
            createCell(row, columnCount++, data.getFirstName(), style);
            createCell(row, columnCount++, data.getLastName(), style);
            createCell(row, columnCount++, data.getEmail(), style);
            createCell(row, columnCount++, data.getAccountInfo(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        writeHeaderLine( workbook);
        writeDataLines( workbook);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);

        workbook.close();
        outputStream.close();
    }
}
