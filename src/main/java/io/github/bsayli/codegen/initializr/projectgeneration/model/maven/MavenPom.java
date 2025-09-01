package io.github.bsayli.codegen.initializr.projectgeneration.model.maven;

import io.github.bsayli.codegen.initializr.projectgeneration.model.Dependency;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import java.util.ArrayList;
import java.util.List;

public class MavenPom {

  private final String modelVersion;
  private final String version;
  private final ProjectMetadata projectMetadata;
  private final List<Dependency> dependencies;
  private final List<MavenPlugin> plugins;

  private MavenPom(MavenPomBuilder builder) {
    this.modelVersion = builder.modelVersion;
    this.version = builder.version;
    this.projectMetadata = builder.projectMetadata;
    this.dependencies = builder.dependencies;
    this.plugins = builder.plugins;
  }

  public String getModelVersion() {
    return modelVersion;
  }

  public String getVersion() {
    return version;
  }

  public ProjectMetadata getProjectMetadata() {
    return projectMetadata;
  }

  public List<Dependency> getDependencies() {
    return dependencies;
  }

  public List<MavenPlugin> getPlugins() {
    return plugins;
  }

  public static class MavenPomBuilder {
    private String modelVersion;
    private String version;
    private ProjectMetadata projectMetadata;
    private List<Dependency> dependencies = new ArrayList<>();
    private List<MavenPlugin> plugins = new ArrayList<>();

    public MavenPomBuilder modelVersion(String modelVersion) {
      this.modelVersion = modelVersion;
      return this;
    }

    public MavenPomBuilder version(String version) {
      this.version = version;
      return this;
    }

    public MavenPomBuilder projectMetadata(ProjectMetadata projectMetadata) {
      this.projectMetadata = projectMetadata;
      return this;
    }

    public MavenPomBuilder addDependencies(List<Dependency> dependencies) {
      this.dependencies = new ArrayList<>(dependencies);
      return this;
    }

    public MavenPomBuilder addPlugins(List<MavenPlugin> plugins) {
      this.plugins = new ArrayList<>(plugins);
      return this;
    }

    public MavenPom build() {
      return new MavenPom(this);
    }
  }
}
