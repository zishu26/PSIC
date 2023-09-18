package com.ZADE.PSIC;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class SanitizeQuery {
    public static void sanitizeQuery() {
        try {
            String filePath = "D:\\Study\\pcis\\PSIC\\PTest.txt";
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            String modifiedContent = content.toString().replace("[", "(").replace("]", ")").replace(".0", "");
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(modifiedContent);
            writer.close();
            log.info("Queries sanitized to SQL syntax..");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
