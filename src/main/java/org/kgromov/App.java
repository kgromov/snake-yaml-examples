package org.kgromov;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class App {
    public static void main( String[] args ) {
        ClassLoader resourcesClassLoader = YamlReadUtils.class.getClassLoader();
        try(InputStream inputStream1 = resourcesClassLoader.getResourceAsStream("typed-sample.yml");
            InputStream inputStream2 = resourcesClassLoader.getResourceAsStream("typed-collection.yml");
            InputStream inputStream3 = resourcesClassLoader.getResourceAsStream("dummy.yml")) {
            Path path = Path.of(resourcesClassLoader.getResource("typed-sample.yml").toURI());

            System.out.println(YamlReadUtils.readYaml(path));
            var content = String.join("\n", Files.readAllLines(path));
            System.out.println(YamlReadUtils.readYaml(content));

            System.out.println(YamlReadUtils.readYaml(inputStream1));
            System.out.println(YamlReadUtils.readYaml(inputStream2));
            System.out.println(YamlReadUtils.readYaml(inputStream3));

            System.out.println(YamlReadUtils.readYaml(inputStream3, Map.class));
            System.out.println(YamlReadUtils.readYaml(inputStream1, IssueTrackerSettings.class));
            System.out.println(YamlReadUtils.readYaml(inputStream2, ProjectTeams.class));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
