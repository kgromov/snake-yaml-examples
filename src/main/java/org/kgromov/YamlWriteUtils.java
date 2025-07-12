package org.kgromov;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class YamlWriteUtils {

    private YamlWriteUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static <T> void writeYaml(T object, Path path) throws IOException {
        Yaml yaml = new Yaml(new Constructor(object.getClass(), new LoaderOptions()));
        String yamlContent = yaml.dumpAs(object, Tag.MAP, null);
        Files.write(path, yamlContent.getBytes());
    }

    // use tag
    public static <T> void writeYaml2(T object, Path path) throws IOException {
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(object, writer);
        Files.write(path, writer.toString().getBytes());
    }

    public static <T> void writeYaml3(T object, Path path) {
        Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(path.toFile())) {
            yaml.dump(object, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void writeYaml4(T object, Yaml yaml, Path path) throws IOException {
        String yamlContent = yaml.dumpAs(object, Tag.MAP, null);
        Files.write(path, yamlContent.getBytes());
    }


    public static <T> void writeYaml(T object,
                                     Class<T> clazz,
                                     TypeDescription typeDescription,
                                     DumperOptions options,
                                     Path path) throws IOException {
        Constructor constructor = new Constructor(clazz, new LoaderOptions());
        constructor.addTypeDescription(typeDescription);

        Representer representer = new Representer(options);
        representer.addTypeDescription(typeDescription);

        Yaml yaml = new Yaml(constructor, representer, options);
        String yamlContent = yaml.dumpAs(object, Tag.MAP, options.getDefaultFlowStyle());
        Files.write(path, yamlContent.getBytes());
    }

}
