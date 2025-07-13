package org.kgromov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactoryBuilder;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        //        File path = new File(YamlWriteUtils.class.getResource("App.class").getPath());
        Path target = new File(YamlWriteUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                .getParentFile().toPath();
//        String property = System.getProperty("java.class.path");
        ClassLoader resourcesClassLoader = YamlReadUtils.class.getClassLoader();
        try (InputStream inputStream1 = resourcesClassLoader.getResourceAsStream("dummy.yml");
             InputStream inputStream2 = resourcesClassLoader.getResourceAsStream("typed-sample.yml");
             InputStream inputStream3 = resourcesClassLoader.getResourceAsStream("typed-collection.yml")) {
            // read from String instead of InputStream
            Path path = Path.of(resourcesClassLoader.getResource("typed-sample.yml").toURI());
            System.out.println("\n################# SnakeYaml #################");
            System.out.println(YamlReadUtils.readYaml(path));
            var content = String.join("\n", Files.readAllLines(path));
            System.out.println(YamlReadUtils.readYaml(content));
            // Implicit types - LinkedHashMap
//            System.out.println(YamlReadUtils.readYaml(inputStream1));
//            System.out.println(YamlReadUtils.readYaml(inputStream2));
//            System.out.println(YamlReadUtils.readYaml(inputStream3));
            // how to map differ to camelCase strategy
            TypeDescription typeDescription = new TypeDescription(IssueTrackerSettings.class);
            typeDescription.substituteProperty("base-url", String.class, "getBaseUrl", "setBaseUrl");
            typeDescription.substituteProperty("project-key", String.class, "getProjectKey", "setProjectKey");
            typeDescription.substituteProperty("project-name", String.class, "getProjectName", "setProjectName");
            typeDescription.setExcludes("baseUrl", "projectKey", "projectName");

            Constructor constructor = new Constructor(IssueTrackerSettings.class, new LoaderOptions());
            constructor.addTypeDescription(typeDescription);

            DumperOptions options = new DumperOptions();
            options.setPrettyFlow(true);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Representer representer = new Representer(options);
            representer.addTypeDescription(typeDescription);

            Yaml yaml = new Yaml(constructor, representer, options);
            IssueTrackerSettings projectSettings = yaml.loadAs(inputStream2, IssueTrackerSettings.class);
            YamlWriteUtils.writeYaml(projectSettings, target.resolve("typed-sample_1.yml"));
            YamlWriteUtils.writeYaml2(projectSettings, target.resolve("typed-sample_2.yml"));
            YamlWriteUtils.writeYaml3(projectSettings, target.resolve("typed-sample_3.yml"));
            YamlWriteUtils.writeYaml4(projectSettings, yaml, target.resolve("typed-sample_4.yml"));
            YamlWriteUtils.writeYaml(
                    projectSettings,
                    IssueTrackerSettings.class,
                    typeDescription,
                    options,
                    target.resolve("typed-sample_5.yml")
            );
            System.out.println(projectSettings);

            System.out.println(YamlReadUtils.readYaml(inputStream1, Map.class));
//            System.out.println(YamlReadUtils.readYaml(inputStream2, IssueTrackerSettings.class));
            ProjectTeams projectTeams = YamlReadUtils.readYaml(inputStream3, ProjectTeams.class);
            YamlWriteUtils.writeYaml(projectTeams, target.resolve("project-teams_1.yml"));
            System.out.println(projectTeams);

            // jackson
            System.out.println("\n################# Jackson Yaml #################");
            var builder = YAMLMapper.builder()
                    .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                    .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
            var trackerSettings = JacksonYamlReadUtils.readYaml(
                    resourcesClassLoader.getResourceAsStream("typed-sample.yml"),
                    IssueTrackerSettings.class,
                    builder
            );
            var teams = JacksonYamlReadUtils.readYaml(
                    resourcesClassLoader.getResourceAsStream("typed-collection.yml"),
                    ProjectTeams.class,
                    builder
            );
            System.out.println(trackerSettings);
            System.out.println(teams);
            IssueTrackerSettings trackerSettings2 = JacksonYamlReadUtils.readYaml(
                    resourcesClassLoader.getResourceAsStream("typed-sample.yml"),
                    IssueTrackerSettings.class);
            System.out.println(trackerSettings2);

            // the same with factory
            YAMLFactoryBuilder factoryBuilder = YAMLFactory.builder().dumperOptions(options);
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            // does not work for deserialization
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

            JacksonYamlWriteUtils.writeYaml(trackerSettings, target.resolve("typed-sample_jackson_1.yml"));
            JacksonYamlWriteUtils.writeYaml(trackerSettings, target.resolve("typed-sample_jackson_2.yml"), builder);
            JacksonYamlWriteUtils.writeYaml(projectTeams, target.resolve("typed-sample_jackson_3.yml"), factoryBuilder);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
