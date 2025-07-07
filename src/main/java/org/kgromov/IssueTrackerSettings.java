package org.kgromov;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueTrackerSettings {
    private String baseUrl;
    private String projectKey;
    private String projectName;
}

