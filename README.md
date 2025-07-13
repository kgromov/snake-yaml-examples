# Snake YAML Examples

A comprehensive Java project demonstrating YAML processing using both **SnakeYAML** and **Jackson YAML** libraries. This project showcases various approaches to reading and writing YAML files, including type mapping, custom property naming conventions, and collection handling.

## Features

- **SnakeYAML Integration**: Direct usage of the SnakeYAML library for YAML processing
- **Jackson YAML Integration**: Using Jackson's YAML dataformat for seamless object mapping
- **Type Mapping**: Custom type descriptions for mapping YAML properties to Java objects
- **Property Naming**: Support for kebab-case to camelCase property mapping
- **Collection Handling**: Examples of working with YAML collections and arrays
- **Multiple Write Strategies**: Various approaches to writing YAML files with different formatting options

## Dependencies

- **Java 24**: Latest Java version with modern language features
- **SnakeYAML 2.4**: Core YAML processing library
- **Jackson YAML 2.18.2**: Jackson's YAML dataformat module
- **Lombok 1.18.38**: Reduces boilerplate code
- **JUnit 5.13.3**: Testing framework
- **AssertJ 3.27.3**: Fluent assertions for testing

## Project Structure

```
src/
├── main/java/org/kgromov/
│   ├── App.java                    # Main application demonstrating all features
│   ├── IssueTrackerSettings.java   # Model class with custom property mapping
│   ├── ProjectTeams.java           # Model class for collection handling
│   ├── TeamSettings.java           # Team model class
│   ├── SnakeYamlReadUtils.java     # SnakeYAML reading utilities
│   ├── SnakeYamlWriteUtils.java    # SnakeYAML writing utilities
│   ├── JacksonYamlReadUtils.java   # Jackson YAML reading utilities
│   └── JacksonYamlWriteUtils.java  # Jackson YAML writing utilities
├── main/resources/
│   ├── dummy.yml                   # Simple YAML example
│   ├── typed-sample.yml            # YAML with kebab-case properties
│   └── typed-collection.yml        # YAML with collections
└── test/
    ├── java/org/kgromov/           # Comprehensive test suite
    └── resources/                  # Test YAML files
```

## Usage Examples

### Basic YAML Reading

```java
// Read YAML as string
String content = SnakeYamlReadUtils.readYaml(Path.of("config.yml"));

// Read YAML into typed object
IssueTrackerSettings settings = SnakeYamlReadUtils.readYaml(
    inputStream, 
    IssueTrackerSettings.class
);
```

### Custom Property Mapping

The project demonstrates handling kebab-case properties in YAML files:

```yaml
# typed-sample.yml
base-url: "http://127.0.0.1:8080"
project-key: PROJ
project-name: Secret Project
```

Mapped to Java class with camelCase properties:

```java
public class IssueTrackerSettings {
    @JsonProperty("base-url")
    private String baseUrl;
    
    @JsonProperty("project-key")
    private String projectKey;
    
    @JsonProperty("project-name")
    private String projectName;
}
```

### SnakeYAML Custom Type Descriptions

```java
TypeDescription typeDescription = new TypeDescription(IssueTrackerSettings.class);
typeDescription.substituteProperty("base-url", String.class, "getBaseUrl", "setBaseUrl");
typeDescription.substituteProperty("project-key", String.class, "getProjectKey", "setProjectKey");
typeDescription.substituteProperty("project-name", String.class, "getProjectName", "setProjectName");

Constructor constructor = new Constructor(IssueTrackerSettings.class, new LoaderOptions());
constructor.addTypeDescription(typeDescription);
```

### Jackson YAML Configuration

```java
// Configure Jackson YAML mapper
var builder = YAMLMapper.builder()
    .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
    .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);

// Read with custom configuration
IssueTrackerSettings settings = JacksonYamlReadUtils.readYaml(
    inputStream, 
    IssueTrackerSettings.class, 
    builder
);
```

### Collection Handling

```yaml
# typed-collection.yml
teams:
  - id: 1
    name: Backend team
    boardId: 1001
  - id: 2
    name: Frontend team
    boardId: 1002
  - {id: 3, name: DevOps team, boardId: 1003} # compact form
```

```java
ProjectTeams projectTeams = SnakeYamlReadUtils.readYaml(
    inputStream, 
    ProjectTeams.class
);
```

### Writing YAML Files

Multiple approaches for writing YAML files:

```java
// Basic writing
SnakeYamlWriteUtils.writeYaml(object, outputPath);

// With custom formatting
DumperOptions options = new DumperOptions();
options.setPrettyFlow(true);
options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

SnakeYamlWriteUtils.writeYaml(
    object, 
    ObjectClass.class, 
    typeDescription, 
    options, 
    outputPath
);

// Jackson YAML writing
JacksonYamlWriteUtils.writeYaml(object, outputPath, yamlMapperBuilder);
```

## Running the Application

```bash
# Compile and run
mvn clean compile
mvn exec:java -Dexec.mainClass="org.kgromov.App"

# Run tests
mvn test

# Package
mvn package
```

## Key Features Demonstrated

### SnakeYAML Features
- Basic YAML loading and dumping
- Custom type descriptions for property mapping
- Different flow styles (block vs flow)
- Constructor and Representer customization
- Multiple writing strategies

### Jackson YAML Features
- Seamless object mapping
- Custom YAML generator features
- Property naming strategies
- Builder pattern configuration
- Integration with Jackson ecosystem

### Property Mapping Strategies
- `@JsonProperty` annotations for Jackson
- TypeDescription property substitution for SnakeYAML
- Handling kebab-case to camelCase conversion
- Excluding properties from serialization

## Testing

The project includes comprehensive tests covering:
- Basic YAML reading and writing operations
- Custom type mapping functionality
- Collection handling
- Error scenarios
- Integration tests with temporary files

## Best Practices Demonstrated

1. **Separation of Concerns**: Separate utility classes for reading and writing
2. **Type Safety**: Strong typing with custom model classes
3. **Error Handling**: Proper exception handling in utility methods
4. **Resource Management**: Proper handling of InputStreams and file resources
5. **Testing**: Comprehensive test coverage with both unit and integration tests
6. **Documentation**: Well-documented code with clear examples

## Configuration Options

### SnakeYAML DumperOptions
- `setPrettyFlow(true)`: Enable pretty printing
- `setDefaultFlowStyle(BLOCK)`: Use block style formatting
- `setWidth(80)`: Set line width for wrapping

### Jackson YAML Features
- `WRITE_DOC_START_MARKER`: Control document start marker (---)
- `MINIMIZE_QUOTES`: Reduce unnecessary quotes in output
- `LITERAL_BLOCK_STYLE`: Use literal block style for multi-line strings
