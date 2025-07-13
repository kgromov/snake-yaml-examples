package org.kgromov;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.error.YAMLException;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SnakeYamlReadUtilsTest {

    private static final String TEST_RESOURCES = "src/test/resources/";
    private static final String TYPED_SAMPLE = TEST_RESOURCES + "typed-sample.yml";
    private static final String TYPED_COLLECTION = TEST_RESOURCES + "typed-collection.yml";
    private static final String DUMMY_YML = TEST_RESOURCES + "dummy.yml";

    @Test
    void readYaml_WithPath_ShouldReturnContent() {
        String content = SnakeYamlReadUtils.readYaml(Path.of(DUMMY_YML));
        assertThat(content).isNotNull().isNotEmpty();
    }

    @Test
    void readYaml_WithStringContent_ShouldReturnContent() {
        String yamlContent = "key: value\nnumber: 42";
        String result = SnakeYamlReadUtils.readYaml(yamlContent);
        assertThat(result).contains("key=value");
    }

    @Test
    void readYaml_WithPathAndClass_ShouldReturnTypedObject() {
        TypeDescription typeDescription = new TypeDescription(IssueTrackerSettings.class);
        typeDescription.substituteProperty("base-url", String.class, "getBaseUrl", "setBaseUrl");
        typeDescription.substituteProperty("project-key", String.class, "getProjectKey", "setProjectKey");
        typeDescription.substituteProperty("project-name", String.class, "getProjectName", "setProjectName");
        typeDescription.setExcludes("baseUrl", "projectKey", "projectName");

        IssueTrackerSettings settings = SnakeYamlReadUtils.readYaml(
                Path.of(TYPED_SAMPLE),
                IssueTrackerSettings.class,
                typeDescription,
                new LoaderOptions()
        );

        assertThat(settings).isNotNull();
        assertThat(settings.getBaseUrl()).isEqualTo("http://127.0.0.1:8080");
        assertThat(settings.getProjectKey()).isEqualTo("PROJ");
        assertThat(settings.getProjectName()).isEqualTo("Secret Project");
    }

    @Test
    void readYaml_WithCollection_ShouldReturnTypedCollection() {
        ProjectTeams projectTeams = SnakeYamlReadUtils.readYaml(Path.of(TYPED_COLLECTION), ProjectTeams.class);
        assertThat(projectTeams).isNotNull();
        assertThat(projectTeams.getTeams())
                .hasSize(4)
                .extracting(TeamSettings::getName)
                .containsExactly("Backend team", "Frontend team", "Automation team", "DevOps team");
    }


    @Test
    void readYaml_WithInvalidYaml_ShouldThrowException() {
        String invalidYaml = "invalid: yaml: content";
        assertThatThrownBy(() -> SnakeYamlReadUtils.readYaml(invalidYaml))
                .isInstanceOf(YAMLException.class);
    }
}
