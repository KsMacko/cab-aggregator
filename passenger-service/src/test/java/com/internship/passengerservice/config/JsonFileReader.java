package com.internship.passengerservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class JsonFileReader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T readJson(String path, Class<T> clazz) throws Exception {
        try (InputStream inputStream = JsonFileReader.class.getResourceAsStream(path)) {
            return mapper.readValue(inputStream, clazz);
        }
    }

    public static String readString(String path) throws Exception {
        try (InputStream inputStream = JsonFileReader.class.getResourceAsStream(path)) {
            return new String(inputStream.readAllBytes());
        }
    }
}