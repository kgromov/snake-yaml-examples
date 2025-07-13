package org.kgromov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SnakeYamlWriteUtilsTest {

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
        SnakeYamlWriteUtils.writeYaml(testSettings, outputFile);
        
        assertThat(outputFile).exists();
        String content = Files.readString(outputFile);
        assertThat(content).contains("baseUrl: 'http://test.url'")
                .contains("projectKey: TEST")
                .contains("projectName: Test Project");
    }

    @Test
    void writeYaml2_WithDifferentMethods_ShouldProduceSimilarOutputAndContainsType() throws IOException {
        Path output = tempDir.resolve("output.yml");

        SnakeYamlWriteUtils.writeYaml2(testSettings, output);
        
        String content = Files.readString(output);
        
        assertThat(content).startsWith("!!" + IssueTrackerSettings.class.getName());
        assertThat(content).contains("baseUrl: 'http://test.url'");
        assertThat(content).contains("projectKey: TEST");
        assertThat(content).contains("projectName: Test Project");
    }
    
    @Test
    void writeYaml_WithCustomTypeDescription_ShouldHandleNamingConvention() throws IOException {
        Path outputFile = tempDir.resolve("custom_output.yml");
        
        TypeDescription typeDescription = new TypeDescription(IssueTrackerSettings.class);
        typeDescription.substituteProperty("base-url", String.class, "getBaseUrl", "setBaseUrl");
        typeDescription.substituteProperty("project-key", String.class, "getProjectKey", "setProjectKey");
        typeDescription.substituteProperty("project-name", String.class, "getProjectName", "setProjectName");
        typeDescription.setExcludes("baseUrl", "projectKey", "projectName");
        
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        
        SnakeYamlWriteUtils.writeYaml(
                testSettings,
                IssueTrackerSettings.class,
                typeDescription,
                options,
                outputFile
        );
        
        String content = Files.readString(outputFile);
        assertThat(content).contains("base-url: http://test.url");
        assertThat(content).contains("project-key: TEST");
        assertThat(content).contains("project-name: Test Project");
    }
    
    @Test
    void writeYaml_WithCollection_ShouldWriteAllItems() throws IOException {
        Path outputFile = tempDir.resolve("teams_output.yml");
        SnakeYamlWriteUtils.writeYaml(testTeams, outputFile);
        
        String content = Files.readString(outputFile);
        assertThat(content).contains("Team A").contains("Team B");
        
        ProjectTeams readBack = new Yaml().loadAs(Files.newInputStream(outputFile), ProjectTeams.class);
        assertThat(readBack.getTeams()).hasSize(2);
    }
    
    @Test
    void writeYaml_WithNullObject_ShouldThrowException() {
        Path outputFile = tempDir.resolve("null_output.yml");
        assertThatThrownBy(() -> SnakeYamlWriteUtils.writeYaml(null, outputFile))
                .isInstanceOf(NullPointerException.class);
    }
}
