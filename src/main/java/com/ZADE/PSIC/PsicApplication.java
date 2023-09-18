package com.ZADE.PSIC;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.ZADE.PSIC.MultipleValueMap.addValueToKey;
import static com.ZADE.PSIC.ReadExcelFile.readExcelFile;
import static com.ZADE.PSIC.SanitizeQuery.sanitizeQuery;



@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "com.ZADE.PSIC")
public class PsicApplication {
//	private ExecuteQuery executeQuery;
	public static void main(String[] args) throws Exception {

		ExecuteQuery app = null;
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ExecuteQuery.class)) {
			app = context.getBean(ExecuteQuery.class);
			String filePath = "D:\\Study\\pcis\\PSIC\\PTest.xlsx";
			String columnNameToFilter1 = "Name";
			String columnNameToFilter2 = "Marks1";

			List<String> rowDataList1 = readExcelFile(filePath, columnNameToFilter1);
			List<String> rowDataList2 = readExcelFile(filePath, columnNameToFilter2);

			FileWriter fileWriter = new FileWriter("D:\\Study\\pcis\\PSIC\\PTest.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			Map<String, List<String>> mapSiteAudit = new HashMap<>();
			if (rowDataList1.size() == rowDataList2.size()) {
				IntStream.range(0, rowDataList1.size()).forEach(i -> addValueToKey(mapSiteAudit, rowDataList1.get(i), rowDataList2.get(i)));
			} else {
				log.error("Lists are of different sizes, Cannot create a map");
			}
			for (Map.Entry<String, List<String>> entry : mapSiteAudit.entrySet()) {
				bufferedWriter.write("SELECT *FROM student WHERE Name = '" + entry.getKey()+"'" + " AND Marks1 = " + entry.getValue() + ";");
				bufferedWriter.newLine();
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
			fileWriter.close();
			log.info("Query file generated successfully..!!");
		} catch (IOException e) {
			e.printStackTrace();

		}
		sanitizeQuery();

		app.executeQueriesFromFile("D:\\Study\\pcis\\PSIC\\PTest.txt");

	}
}
