package com.nio_webflux_crud.io_csv_zip;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
class CsvZipController {

    private final CsvZipGenerator csvZipGenerator;

    @GetMapping("/zip")
    public Mono<ResponseEntity<ByteArrayResource>> downloadZip() {
        return csvZipGenerator.generateZip()
                .map(zipResult -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipResult.getT1())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(zipResult.getT2())
                );
    }
}
