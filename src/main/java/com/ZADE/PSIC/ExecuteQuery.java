package com.ZADE.PSIC;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class ExecuteQuery {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("");
        dataSource.setUrl("");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    void executeQueriesFromFile(String filePath) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("PTest Results");

            List<String> queries = readQueriesFromFile(filePath);
            int rowIndex = 0;

            for (int i = 0; i < queries.size(); i++) {
                String query = queries.get(i);
                log.info("Executing Query:"+(i+1)+"--> " + query);
                List<List<String>> queryResults = executeQuery(query);
                rowIndex = appendQueryResultsToSheet(sheet, rowIndex, queryResults);
            }
            try (FileOutputStream outputStream = new FileOutputStream("PTest-Result.xlsx")) {
                workbook.write(outputStream);
                log.info("All queries executed, and results saved to PTest-Result.xlsx");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readQueriesFromFile(String filePath) throws IOException {
        List<String> queries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder queryBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                queryBuilder.append(line).append("\n");
                if (line.trim().endsWith(";")) {
                    queries.add(queryBuilder.toString());
                    queryBuilder.setLength(0);
                }
            }
        }
        return queries;
    }

    private List<List<String>> executeQuery(String query) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());

        return jdbcTemplate.query(query, (resultSet, rowNum) -> {
            int columnCount = resultSet.getMetaData().getColumnCount();
            List<String> rowData = new ArrayList<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                rowData.add(resultSet.getString(i));
            }
            return rowData;
        });
    }

    private int appendQueryResultsToSheet(Sheet sheet, int rowIndex, List<List<String>> queryResults) {
        for (List<String> rowData : queryResults) {
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            for (String cellData : rowData) {
                row.createCell(cellIndex++).setCellValue(cellData);
            }
        }
        return rowIndex;
    }
}
