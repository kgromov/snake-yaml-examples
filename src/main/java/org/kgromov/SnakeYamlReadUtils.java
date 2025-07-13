package org.kgromov;

import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class SnakeYamlReadUtils {

    public static String readYaml(Path path) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return readYaml(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readYaml(InputStream input) {
        Object yaml = new Yaml().load(input);
        return yaml.toString();
    }

    public static String readYaml(String yamlContent) {
        Object yaml = new Yaml().load(yamlContent);
        return yaml.toString();
    }


    public static <T> T readYaml(Path path, Class<T> clazz) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return readYaml(inputStream, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readYaml(InputStream inputStream, Class<T> clazz) {
        return new Yaml().loadAs(inputStream, clazz);
    }

    public static <T> T readYaml(String yamlContent, Class<T> clazz) {
        return new Yaml(new Constructor(clazz, new LoaderOptions())).load(yamlContent);
    }

    public static <T> T readYaml(Path path,
                                 Class<T> clazz,
                                 TypeDescription typeDescription,
                                 LoaderOptions options) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return readYaml(inputStream, clazz, typeDescription, options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readYaml(InputStream inputStream,
                                 Class<T> clazz,
                                 TypeDescription typeDescription,
                                 LoaderOptions options) {
        Constructor constructor = new Constructor(clazz, options);
        constructor.addTypeDescription(typeDescription);
        return new Yaml(constructor).loadAs(inputStream, clazz);
    }
}
