package org.kgromov;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueTrackerSettings {
    @JsonProperty("base-url")
    private String baseUrl;
    @JsonProperty("project-key")
    private String projectKey;
    @JsonProperty("project-name")
    private String projectName;
}

