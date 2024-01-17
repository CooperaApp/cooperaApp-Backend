package com.coopera.cooperaApp.utilities;

import com.coopera.cooperaApp.exceptions.CooperaException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HtmlFileUtility {

    public static String getFileTemplateFromClasspath(String resourcePath) throws CooperaException {
        try (InputStream inputStream = HtmlFileUtility.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new CooperaException("Resource not found: " + resourcePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.lines().collect(Collectors.joining());
            }
        } catch (IOException exception) {
            throw new CooperaException("Failed to read resource: " + resourcePath + " - " + exception.getMessage());
        }
    }
}
