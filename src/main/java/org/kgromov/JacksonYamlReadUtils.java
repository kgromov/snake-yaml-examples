package org.kgromov;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactoryBuilder;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@UtilityClass
public class JacksonYamlReadUtils {

    public static <T> T readYaml(String yamlContent, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        return objectMapper.readValue(yamlContent, clazz);
    }

    public static <T> T readYaml(Path yamlFilePath, Class<T> clazz) throws IOException {
        return readYaml(yamlFilePath.toFile(), clazz);
    }

    public static <T> T readYaml(File yamlFile, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        return objectMapper.readValue(yamlFile, clazz);
    }

    public static <T> T readYaml(InputStream inputStream, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        return objectMapper.readValue(inputStream, clazz);
    }

    public static <T> T readYaml(InputStream inputStream, Class<T> clazz, YAMLMapper.Builder builder) throws IOException {
        ObjectMapper objectMapper = builder.build();
        return objectMapper.readValue(inputStream, clazz);
    }

    public static <T> T readYaml(InputStream inputStream, Class<T> clazz, YAMLFactoryBuilder builder) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(builder.build());
        return objectMapper.readValue(inputStream, clazz);
    }
}
