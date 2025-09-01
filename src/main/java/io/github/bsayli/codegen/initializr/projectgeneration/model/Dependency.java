package io.github.bsayli.codegen.initializr.projectgeneration.model;

public class Dependency {
  private final String groupId;
  private final String artifactId;
  private final String version;
  private final String scope;

  private Dependency(DependencyBuilder builder) {
    this.groupId = builder.groupId;
    this.artifactId = builder.artifactId;
    this.version = builder.version;
    this.scope = builder.scope;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public String getVersion() {
    return version;
  }

  public String getScope() {
    return scope;
  }

  @Override
  public String toString() {
    return toShortDefinition();
  }

  public String toShortDefinition() {
    return "Dependency [artifactId=" + artifactId + "]";
  }

  public String toLongDefinition() {
    return "Dependency [groupId="
        + groupId
        + ", artifactId="
        + artifactId
        + ", version="
        + version
        + ", scope="
        + scope
        + "]";
  }

  public static class DependencyBuilder {

    private String groupId;
    private String artifactId;
    private String version;
    private String scope;

    public DependencyBuilder groupId(String groupId) {
      this.groupId = groupId;
      return this;
    }

    public DependencyBuilder artifactId(String artifactId) {
      this.artifactId = artifactId;
      return this;
    }

    public DependencyBuilder version(String version) {
      this.version = version;
      return this;
    }

    public DependencyBuilder scope(String scope) {
      this.scope = scope;
      return this;
    }

    public Dependency build() {
      if (groupId == null || artifactId == null) {
        throw new IllegalStateException("groupId and artifactId are required fields");
      }
      return new Dependency(this);
    }
  }
}
