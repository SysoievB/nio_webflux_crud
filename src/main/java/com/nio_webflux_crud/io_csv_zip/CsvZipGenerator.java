package com.nio_webflux_crud.io_csv_zip;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
class CsvZipGenerator {

    private final CsvReportGenerator csvReportGenerator;

    Mono<Tuple2<String, ByteArrayResource>> generateZip() {
        return Mono.fromSupplier(csvReportGenerator::generateCsvReports)
                .map(this::createZipFile);
    }

    private Tuple2<String, ByteArrayResource> createZipFile(Map<String, InputStream> csvs) {
        List<Tuple2<String, byte[]>> csvByteArrays = csvs.entrySet().stream()
                .map(entry -> {
                    try (InputStream input = entry.getValue()) {
                        return Tuples.of(entry.getKey(), IOUtils.toByteArray(input));
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to read CSV: " + entry.getKey(), e);
                    }
                }).toList();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(baos)) {

            for (Tuple2<String, byte[]> csv : csvByteArrays) {
                zipOut.putNextEntry(new ZipEntry(csv.getT1()));
                zipOut.write(csv.getT2());
                zipOut.closeEntry();
            }

            zipOut.finish();
            return Tuples.of("csv-report.zip", new ByteArrayResource(baos.toByteArray()));

        } catch (IOException e) {
            throw new RuntimeException("ZIP creation failed", e);
        }
    }
}

