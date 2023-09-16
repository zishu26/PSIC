package com.ZADE.PSIC;

import lombok.extern.slf4j.Slf4j;
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
public class PsicApplication {

	public static void main(String[] args)  {
		try {
			String filePath = "D:\\Study\\pcis\\PSIC\\PCIS-testing.xlsx";
			String columnNameToFilter1 = "Site";
			String columnNameToFilter2 = "Audit";

			List<String> rowDataList1 = readExcelFile(filePath, columnNameToFilter1);
			List<String> rowDataList2 = readExcelFile(filePath, columnNameToFilter2);

			FileWriter fileWriter = new FileWriter("D:\\Study\\pcis\\PSIC\\PCIS-testing.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			Map<String, List<String>> mapSiteAudit = new HashMap<>();
			if (rowDataList1.size() == rowDataList2.size()) {
				IntStream.range(0, rowDataList1.size()).forEach(i -> addValueToKey(mapSiteAudit,rowDataList1.get(i),rowDataList2.get(i) ));
			} else {
				log.error("Lists are of different sizes, Cannot create a map");
			}
			for (Map.Entry<String, List<String>> entry : mapSiteAudit.entrySet()) {
					bufferedWriter.write("SELECT *FROM TABLE_NAME WHERE SITE = "+ entry.getKey() + " AND AUDIT IN " + entry.getValue() + ";");
					bufferedWriter.newLine();
					bufferedWriter.newLine();
			}
				bufferedWriter.close();
				fileWriter.close();
				log.info("Query file generated successfully..!!");
		}catch (IOException e) {
			e.printStackTrace();

		}
		sanitizeQuery();
	}
}
