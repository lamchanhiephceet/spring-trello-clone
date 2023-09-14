//package com.hn.springtrelloclone.service;
//
//import com.sun.rowset.internal.Row;
//import org.springframework.stereotype.Service;
//
//@Service
//@AllArgsConstructor
//public class ExcelExporterService {
//   private XSSFWorkbook workbook;
//   private XSSFSheet sheet;
//
//   private void writeHeaderLine(){
//      sheet = workbook.create("Board");
//
//      Row row = sheet.createRow(0);
//      CellStyle style = workbook.createCellStyle();
//      XSSFFont font = workbook.createFont();
//      font.setBold(true);
//      font.setFontHeight(16);
//      style.setFont(font);
//
//      createCell()
//   }
//}
