package io.github.bsayli.codegen.initializr.projectgeneration.model;

import java.nio.file.Path;
import java.util.List;

public class ProjectMetadata {

  private final String name;
  private final String description;
  private final String groupId;
  private final String artifactId;
  private final String packageName;
  private final List<Dependency> dependencies;
  private final Path projectLocation;

  protected ProjectMetadata(ProjectMetadataBuilder builder) {
    this.name = builder.name;
    this.description = builder.description;
    this.groupId = builder.groupId;
    this.artifactId = builder.artifactId;
    this.packageName = builder.packageName;
    this.dependencies = builder.dependencies;
    this.projectLocation = builder.projectLocation;
  }

  public String getName() {
    return name;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getDescription() {
    return description;
  }

  public List<Dependency> getDependencies() {
    return dependencies;
  }

  public Path getProjectLocation() {
    return projectLocation;
  }

  public static class ProjectMetadataBuilder {

    private String name;
    private String description;
    private String groupId;
    private String artifactId;
    private String packageName;
    private List<Dependency> dependencies;
    private Path projectLocation;

    public ProjectMetadataBuilder name(String name) {
      this.name = name;
      return this;
    }

    public ProjectMetadataBuilder description(String description) {
      this.description = description;
      return this;
    }

    public ProjectMetadataBuilder groupId(String groupId) {
      this.groupId = groupId;
      return this;
    }

    public ProjectMetadataBuilder artifactId(String artifactId) {
      this.artifactId = artifactId;
      return this;
    }

    public ProjectMetadataBuilder packageName(String packageName) {
      this.packageName = packageName;
      return this;
    }

    public ProjectMetadataBuilder dependencies(List<Dependency> dependencies) {
      this.dependencies = dependencies;
      return this;
    }

    public ProjectMetadataBuilder projectLocation(Path projectLocation) {
      this.projectLocation = projectLocation;
      return this;
    }

    public ProjectMetadata build() {
      return new ProjectMetadata(this);
    }
  }
}
