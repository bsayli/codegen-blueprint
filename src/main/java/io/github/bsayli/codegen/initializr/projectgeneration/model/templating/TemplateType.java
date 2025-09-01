package io.github.bsayli.codegen.initializr.projectgeneration.model.templating;

public enum TemplateType {
  GITIGNORE(".gitignore", "gitignore.ftl"),
  SPRING_BOOT_JAVA_POM("pom.xml", "springBootJavaPom.xml.ftl"),
  SPRING_BOOT_MAVEN_JAVA_README("README.md", "springBootMavenJavaReadMe.ftl"),
  MAVEN_WRAPPER("maven-wrapper.properties", "maven-wrapper.properties.ftl"),
  SPRING_BOOT_APPLICATION_YAML("application.yml", "springBootApplication.yml.ftl");

  private final String fileName;
  private final String templateFileName;

  TemplateType(String fileName, String templateFileName) {
    this.fileName = fileName;
    this.templateFileName = templateFileName;
  }

  public String getFileName() {
    return fileName;
  }

  public String getTemplateFileName() {
    return templateFileName;
  }
}
