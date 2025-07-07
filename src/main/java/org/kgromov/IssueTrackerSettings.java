package org.kgromov;

import java.util.Objects;

public class IssueTrackerSettings {
    private String baseUrl;
    private String projectKey;
    private String projectName;

    public IssueTrackerSettings() {}

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueTrackerSettings that = (IssueTrackerSettings) o;
        return Objects.equals(baseUrl, that.baseUrl) &&
                Objects.equals(projectKey, that.projectKey) &&
                Objects.equals(projectName, that.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseUrl, projectKey, projectName);
    }

    @Override
    public String toString() {
        return "IssueTrackerSettings[" +
                "baseUrl=" + baseUrl + ", " +
                "projectKey=" + projectKey + ", " +
                "projectName=" + projectName + ']';
    }
}

