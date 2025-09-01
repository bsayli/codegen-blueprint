package io.github.bsayli.codegen.initializr.projectgeneration.model.spring;

import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;

public class SpringBootJavaProjectMetadata extends ProjectMetadata {

  private String springBootVersion;
  private String javaVersion;

  protected SpringBootJavaProjectMetadata(
      ProjectMetadataBuilder builder, String springBootVersion, String javaVersion) {
    super(builder);
    this.springBootVersion = springBootVersion;
    this.javaVersion = javaVersion;
  }

  public String getSpringBootVersion() {
    return springBootVersion;
  }

  public void setSpringBootVersion(String springBootVersion) {
    this.springBootVersion = springBootVersion;
  }

  public String getJavaVersion() {
    return javaVersion;
  }

  public void setJavaVersion(String javaVersion) {
    this.javaVersion = javaVersion;
  }

  public static class SpringBootJavaProjectMetadataBuilder extends ProjectMetadataBuilder {
    private String springBootVersion;
    private String javaVersion;

    public SpringBootJavaProjectMetadataBuilder springBootVersion(String springBootVersion) {
      this.springBootVersion = springBootVersion;
      return this;
    }

    public SpringBootJavaProjectMetadataBuilder javaVersion(String javaVersion) {
      this.javaVersion = javaVersion;
      return this;
    }

    @Override
    public SpringBootJavaProjectMetadata build() {
      return new SpringBootJavaProjectMetadata(this, springBootVersion, javaVersion);
    }
  }
}
