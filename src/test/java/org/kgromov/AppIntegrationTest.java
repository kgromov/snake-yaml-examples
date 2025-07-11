package org.kgromov;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AppIntegrationTest {

    @TempDir
    Path tempDir;
    
    @Test
    void testAppEndToEnd() throws IOException {
        // Given
        Path testYaml = tempDir.resolve("test-output.yml");
        Path testCollectionYaml = tempDir.resolve("test-collection-output.yml");
        
        // Create test settings
        IssueTrackerSettings settings = new IssueTrackerSettings(
                "http://test.url",
                "TEST",
                "Integration Test Project"
        );
        
        // Create test teams
        ProjectTeams teams = new ProjectTeams(List.of(
                new TeamSettings(1, "Integration Team", 1001)
        ));
        
        // Test writing YAML
        YamlWriteUtils.writeYaml(settings, testYaml);
        YamlWriteUtils.writeYaml(teams, testCollectionYaml);
        
        // Test reading back
        IssueTrackerSettings readSettings = YamlReadUtils.readYaml(testYaml, IssueTrackerSettings.class);
        ProjectTeams readTeams = YamlReadUtils.readYaml(testCollectionYaml, ProjectTeams.class);
        
        // Assert
        assertThat(readSettings.getBaseUrl()).isEqualTo(settings.getBaseUrl());
        assertThat(readSettings.getProjectKey()).isEqualTo(settings.getProjectKey());
        assertThat(readSettings.getProjectName()).isEqualTo(settings.getProjectName());
        
        assertThat(readTeams.getTeams())
                .hasSize(1)
                .first()
                .extracting(TeamSettings::getName)
                .isEqualTo("Integration Team");
        
        // Test with custom type description
        TypeDescription typeDescription = new TypeDescription(IssueTrackerSettings.class);
        typeDescription.substituteProperty("base-url", String.class, "getBaseUrl", "setBaseUrl");
        typeDescription.substituteProperty("project-key", String.class, "getProjectKey", "setProjectKey");
        typeDescription.substituteProperty("project-name", String.class, "getProjectName", "setProjectName");
        typeDescription.setExcludes("baseUrl", "projectKey", "projectName");
        
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        
        Path customOutput = tempDir.resolve("custom-output.yml");
        YamlWriteUtils.writeYaml(
                settings,
                IssueTrackerSettings.class,
                typeDescription,
                options,
                customOutput
        );
        
        String customContent = Files.readString(customOutput);
        assertThat(customContent)
                .contains("base-url: http://test.url")
                .contains("project-key: TEST")
                .contains("project-name: Integration Test Project");
    }
}
