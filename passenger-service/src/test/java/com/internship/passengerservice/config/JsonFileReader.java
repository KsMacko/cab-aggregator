package com.internship.passengerservice.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
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
    public static MultiValueMap<String, String> parseJsonToQueryParams(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(json, new TypeReference<>() {});

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        map.forEach((key, value) -> {
            if (value != null) {
                params.add(key, value.toString());
            }
        });

        return params;
    }
}