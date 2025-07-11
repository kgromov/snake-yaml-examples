package org.kgromov;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class YamlReadUtilsTest {

    private static final String TEST_RESOURCES = "src/test/resources/";
    private static final String TYPED_SAMPLE = TEST_RESOURCES + "typed-sample.yml";
    private static final String TYPED_COLLECTION = TEST_RESOURCES + "typed-collection.yml";
    private static final String DUMMY_YML = TEST_RESOURCES + "dummy.yml";

    @Test
    void readYaml_WithPath_ShouldReturnContent() {
        String content = YamlReadUtils.readYaml(Path.of(DUMMY_YML));
        assertThat(content).isNotNull().isNotEmpty();
    }

    @Test
    void readYaml_WithStringContent_ShouldReturnContent() {
        String yamlContent = "key: value\nnumber: 42";
        String result = YamlReadUtils.readYaml(yamlContent);
        assertThat(result).contains("key=value");
    }

    @Disabled
    @Test
    void readYaml_WithPathAndClass_ShouldReturnTypedObject() {
        IssueTrackerSettings settings = YamlReadUtils.readYaml(Path.of(TYPED_SAMPLE), IssueTrackerSettings.class);
        assertThat(settings).isNotNull();
        assertThat(settings.getBaseUrl()).isEqualTo("http://127.0.0.1:8080");
        assertThat(settings.getProjectKey()).isEqualTo("PROJ");
        assertThat(settings.getProjectName()).isEqualTo("Secret Project");
    }

    @Test
    void readYaml_WithCollection_ShouldReturnTypedCollection() {
        ProjectTeams projectTeams = YamlReadUtils.readYaml(Path.of(TYPED_COLLECTION), ProjectTeams.class);
        assertThat(projectTeams).isNotNull();
        assertThat(projectTeams.getTeams())
                .hasSize(4)
                .extracting(TeamSettings::getName)
                .containsExactly("Backend team", "Frontend team", "Automation team", "DevOps team");
    }

    @Disabled
    @Test
    void readYaml_WithInvalidYaml_ShouldThrowException() {
        String invalidYaml = "invalid: yaml: content";
        assertThatThrownBy(() -> YamlReadUtils.readYaml(invalidYaml))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(Throwable.class);
    }
}
