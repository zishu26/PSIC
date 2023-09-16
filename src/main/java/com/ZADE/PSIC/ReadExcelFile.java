package com.ZADE.PSIC;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Slf4j
public class ReadExcelFile {
    public static List<String> readExcelFile(String filePath, String columnNameToFilter) throws IOException {
        List<String> rowDataList = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);

        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int columnIndex = -1;
        Row headerRow = sheet.getRow(0);
        Iterator<Cell> cellIterator = headerRow.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell.getStringCellValue().equalsIgnoreCase(columnNameToFilter)) {
                columnIndex = cell.getColumnIndex();
                break;
            }
        }
        if (columnIndex == -1) {
            log.error("Column not found.");
            return rowDataList;
        }
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Skip the header row
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell cell = row.getCell(columnIndex);
            if (cell != null) {
                rowDataList.add(cell.toString());
            }
        }
        fis.close();
        workbook.close();

        return rowDataList;
    }
}
