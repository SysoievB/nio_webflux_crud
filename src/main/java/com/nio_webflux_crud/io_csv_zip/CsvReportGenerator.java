package com.nio_webflux_crud.io_csv_zip;

import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
class CsvReportGenerator {

    Map<String, InputStream> generateCsvReports() {
        Map<String, InputStream> csvMap = new HashMap<>();

        csvMap.put("first.csv", createCsvStream("Name,Age\nAlice,30\nBob,25"));
        csvMap.put("second.csv", createCsvStream("City,Country\nNYC,USA\nLondon,UK"));

        return csvMap;
    }

    private ByteArrayInputStream createCsvStream(String content) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
            writer.write(content);
            writer.flush(); // ⚠️ IMPORTANT: flush to make data available
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate CSV stream", e);
        }
    }
}

