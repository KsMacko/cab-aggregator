package com.internship.passengerservice.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class JsonFileReader {

    public static String readJsonFile(String path) {
        try (InputStream is = JsonFileReader.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + path);
            }
            return new Scanner(is, StandardCharsets.UTF_8)
                    .useDelimiter("\\A")
                    .next();
        } catch (Exception e) {
            throw new RuntimeException("Error reading resource: " + path, e);
        }
    }
}