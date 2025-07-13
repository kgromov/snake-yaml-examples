package org.kgromov;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.yaml.snakeyaml.DumperOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonYamlWriteUtilsTest {
    @TempDir
    Path tempDir;

    private IssueTrackerSettings testSettings;
    private ProjectTeams testTeams;

    @BeforeEach
    void setUp() {
        testSettings = new IssueTrackerSettings(
                "http://test.url",
                "TEST",
                "Test Project"
        );

        testTeams = new ProjectTeams(List.of(
                new TeamSettings(1, "Team A", 1001),
                new TeamSettings(2, "Team B", 1002)
        ));
    }

    @Test
    void writeYaml_ShouldWriteObjectToFile() throws IOException {
        Path outputFile = tempDir.resolve("output.yml");
        JacksonYamlWriteUtils.writeYaml(testSettings, outputFile);

        assertThat(outputFile).exists();
        String content = Files.readString(outputFile);
        assertThat(content)
                .contains("base-url: \"http://test.url\"")
                .contains("project-key: \"TEST\"")
                .contains("project-name: \"Test Project\"");
    }

    @Test
    void writeYaml_WithCustomTypeDescription_ShouldHandleNamingConvention() throws IOException {
        Path outputFile = tempDir.resolve("custom_output.yml");

        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        JacksonYamlWriteUtils.writeYaml(
                testSettings,
                outputFile,
                YAMLFactory.builder().dumperOptions(options)
        );

        String content = Files.readString(outputFile);
        assertThat(content)
                .contains("base-url: \"http://test.url\"")
                .contains("project-key: \"TEST\"")
                .contains("project-name: \"Test Project\"");
    }

    @Test
    void writeYaml_WithYamlMapperBuilder_ShouldHandleNamingConvention() throws IOException {
        Path outputFile = tempDir.resolve("custom_output.yml");

        var builder = YAMLMapper.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);

        JacksonYamlWriteUtils.writeYaml(testSettings, outputFile, builder);

        String content = Files.readString(outputFile);
        assertThat(content).contains("base-url: http://test.url");
        assertThat(content).contains("project-key: TEST");
        assertThat(content).contains("project-name: Test Project");
    }
}