package com.nio_webflux_crud.nio_text;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/{filename}")
    Mono<String> createFile(@PathVariable String filename, @RequestBody String content) {
        return fileService.createFile(filename, content);
    }

    @GetMapping("/{filename}")
    Mono<String> readFile(@PathVariable String filename) {
        return fileService.readFile(filename);
    }

    @GetMapping
    Mono<List<String>> getAllFiles() {
        return fileService.getAllFiles();
    }

    @PutMapping("/{filename}")
    Mono<String> updateFile(@PathVariable String filename, @RequestBody String content) {
        return fileService.updateFile(filename, content);
    }

    @DeleteMapping("/{filename}")
    Mono<String> deleteFile(@PathVariable String filename) {
        return fileService.deleteFile(filename);
    }
}
