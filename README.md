# Codegen Spring Boot Initializr

A customizable **Spring Boot project generator** designed to streamline Java project scaffolding. This tool enables developers to quickly bootstrap Spring Boot applications with predefined configurations, folder structures, and initial test classes, eliminating repetitive setup tasks.

## Features

* **Spring Boot Project Scaffolding**: Generate a complete Maven-based Spring Boot project with a single command.
* **Custom Package Naming**: Easily define the package name for generated projects.
* **Automatic Test Class Generation**: Creates `@SpringBootTest` annotated test classes with proper naming conventions.
* **Configurable Source Structure**: Supports custom Java source and test directory configurations.
* **Maven & Common Libraries Integration**: Preconfigured dependencies for popular libraries such as:

    * Apache Commons IO
    * Apache Commons Lang
    * Apache Commons Compress
    * FreeMarker
* **Spring Configuration Processor Support** for metadata generation.

## Requirements

* Java 21+
* Maven 3.9+

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/bsayli/codegen-springboot-initializr.git
cd codegen-springboot-initializr
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

### 4. Generate a New Project

The generator can be invoked via service classes or integrated into your own CLI/automation tools.

Example usage in Java:

```java
ProjectMetadata metadata = new ProjectMetadata.ProjectMetadataBuilder()
    .name("demo-project")
    .packageName("com.example.demo")
    .build();

generator.generateProject(new File("/path/to/output"), metadata);
```

## Project Structure

```
codegen-springboot-initializr/
├── src/
│   ├── main/java/...       # Core generator logic
│   └── test/java/...       # Unit and integration tests
├── pom.xml                 # Maven configuration
└── README.md               # Project documentation
```

## Testing

Run the full test suite with:

```bash
mvn test
```

## License

This project is licensed under the **MIT License**. You are free to use, modify, and distribute this project with attribution.

---

**Author:** Barış Saylı
**GitHub:** [bsayli](https://github.com/bsayli)
