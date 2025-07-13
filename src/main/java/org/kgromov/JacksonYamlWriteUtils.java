package org.kgromov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactoryBuilder;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Path;

@UtilityClass
public class JacksonYamlWriteUtils {

    public static <T> void writeYaml(T object, Path path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.writeValue(path.toFile(), object);
    }

    public static <T> void writeYaml(T object, Path path, YAMLMapper.Builder builder) throws IOException {
        ObjectMapper objectMapper = builder.build();
        objectMapper.writeValue(path.toFile(), object);
    }

    public static <T> void writeYaml(T object, Path path, YAMLFactoryBuilder builder) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(builder.build());
        objectMapper.writeValue(path.toFile(), object);
    }
}
