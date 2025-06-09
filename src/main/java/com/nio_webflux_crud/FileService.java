package com.nio_webflux_crud;

import lombok.val;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.List;

/**
 * ByteBuffer has 2 main modes:
 * Write mode: When you're filling the buffer (e.g., from a channel)
 * Read mode: When you're reading data out of the buffer (e.g., into a String)
 * */
@Component
public class FileService {
    private static final Path BASE_DIR = Paths.get("data");

    public FileService() {
        try {
            if (!Files.exists(BASE_DIR)) {
                Files.createDirectories(BASE_DIR);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create base directory", e);
        }
    }

    Mono<List<String>> getAllFiles() {
        return Mono.fromCallable(() -> Files.walk(BASE_DIR))
                .flatMapMany(Flux::fromStream)
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .map(Path::toString)
                .collectList();
    }


    Mono<String> createFile(String filename, String content) {
        return Mono.fromCallable(() -> {
            val path = BASE_DIR.resolve(filename + ".txt");
            try (val channel = FileChannel.open(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
                val buffer = ByteBuffer.wrap(content.getBytes());
                channel.write(buffer);
                return "File created";
            }
        });
    }

    Mono<String> readFile(String filename) {
        return Mono.fromCallable(() -> {
            val path = BASE_DIR.resolve(filename + ".txt");
            try (val channel = FileChannel.open(path, StandardOpenOption.READ)) {
                val buffer = ByteBuffer.allocate((int) channel.size());
                channel.read(buffer);
                buffer.flip();//Switches from Write to Read Mode
                return new String(buffer.array(), 0, buffer.limit());
            }
        });
    }

    Mono<String> updateFile(String filename, String content) {
        return Mono.fromCallable(() -> {
            val path = BASE_DIR.resolve(filename + ".txt");
            try (val channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                val buffer = ByteBuffer.wrap(content.getBytes());
                channel.write(buffer);
                return "File updated";
            }
        });
    }

    Mono<String> deleteFile(String filename) {
        return Mono.fromCallable(() -> {
            val path = BASE_DIR.resolve(filename + ".txt");
            Files.deleteIfExists(path);
            return "File deleted";
        });
    }
}
