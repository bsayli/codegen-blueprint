package io.github.bsayli.codegen.initializr.projectgeneration.generator.base;

import io.github.bsayli.codegen.initializr.projectgeneration.generator.ProjectGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ApplicationYamlGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.FrameworkProjectStarterClassGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.FrameworkSpecificTestUnitGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.GitIgnoreGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectArchiver;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectBuildGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectDirectoryInitializer;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectDocumentationGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectLayoutGenerator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public abstract class AbstractProjectGenerator implements ProjectGenerator {

  private final ProjectDirectoryInitializer projectDirectoryInitializer;
  private final GitIgnoreGenerator gitIgnoreGenerator;
  private final ProjectArchiver projectArchiver;
  private final ProjectLayoutGenerator projectLayoutGenerator;
  private final ProjectBuildGenerator projectBuildGenerator;
  private final ApplicationYamlGenerator applicationYamlGenerator;
  private final FrameworkProjectStarterClassGenerator frameworkProjectStarterClassGenerator;
  private final FrameworkSpecificTestUnitGenerator frameworkSpecificTestUnitGenerator;
  private final ProjectDocumentationGenerator projectDocumentationGenerator;

  protected AbstractProjectGenerator(
      ProjectDirectoryInitializer projectDirectoryInitializer,
      GitIgnoreGenerator gitIgnoreGenerator,
      ProjectArchiver projectArchiver,
      ProjectLayoutGenerator projectLayoutGenerator,
      ProjectBuildGenerator projectBuildGenerator,
      ApplicationYamlGenerator applicationYamlGenerator,
      FrameworkProjectStarterClassGenerator frameworkProjectStarterClassGenerator,
      FrameworkSpecificTestUnitGenerator frameworkSpecificTestUnitGenerator,
      ProjectDocumentationGenerator projectDocumentationGenerator) {
    this.projectDirectoryInitializer = projectDirectoryInitializer;
    this.gitIgnoreGenerator = gitIgnoreGenerator;
    this.projectArchiver = projectArchiver;
    this.projectLayoutGenerator = projectLayoutGenerator;
    this.projectBuildGenerator = projectBuildGenerator;
    this.applicationYamlGenerator = applicationYamlGenerator;
    this.frameworkProjectStarterClassGenerator = frameworkProjectStarterClassGenerator;
    this.frameworkSpecificTestUnitGenerator = frameworkSpecificTestUnitGenerator;
    this.projectDocumentationGenerator = projectDocumentationGenerator;
  }

  @Override
  public final Path generateProject(ProjectMetadata projectMetadata) throws IOException {
    Path projectDestinationPath = initializeProjectDirectory(projectMetadata);
    File projectDestination = projectDestinationPath.toFile();

    generateGitIgnoreContent(projectDestination);
    generateProjectLayout(projectDestination, projectMetadata);
    generateBuildConfiguration(projectDestination, projectMetadata);
    generateApplicationProperties(projectDestination, projectMetadata);
    generateProjectStarterClass(projectDestination, projectMetadata);
    generateTestClass(projectDestination, projectMetadata);
    generateProjectDocument(projectDestination, projectMetadata);
    return archiveProject(projectDestination, projectMetadata);
  }

  protected Path initializeProjectDirectory(ProjectMetadata projectMetadata) throws IOException {
    if (projectMetadata.getProjectLocation() != null) {
      return projectDirectoryInitializer.initializeProjectDirectory(
          projectMetadata.getArtifactId(), projectMetadata.getProjectLocation());
    } else {
      return projectDirectoryInitializer.initializeProjectDirectory(
          projectMetadata.getArtifactId());
    }
  }

  protected void generateGitIgnoreContent(File projectDestination) throws IOException {
    List<String> ignoreList = Collections.emptyList();
    gitIgnoreGenerator.generateGitIgnoreContent(projectDestination, ignoreList);
  }

  protected void generateProjectLayout(File projectDestination, ProjectMetadata projectMetadata)
      throws IOException {
    projectLayoutGenerator.generateProjectLayout(projectDestination, projectMetadata);
  }

  protected void generateBuildConfiguration(
      File projectDestination, ProjectMetadata projectMetadata) throws IOException {
    projectBuildGenerator.generateBuildConfiguration(projectDestination, projectMetadata);
  }

  protected void generateApplicationProperties(
      File projectDestination, ProjectMetadata projectMetadata) throws IOException {
    applicationYamlGenerator.generateApplicationYaml(projectDestination, projectMetadata);
  }

  protected void generateProjectStarterClass(
      File projectDestination, ProjectMetadata projectMetadata) throws IOException {
    frameworkProjectStarterClassGenerator.generateProjectStarterClass(
        projectDestination, projectMetadata);
  }

  protected void generateTestClass(File projectDestination, ProjectMetadata projectMetadata)
      throws IOException {
    frameworkSpecificTestUnitGenerator.generateTestClass(projectDestination, projectMetadata);
  }

  protected void generateProjectDocument(File projectDestination, ProjectMetadata projectMetadata)
      throws IOException {
    projectDocumentationGenerator.generateProjectDocument(projectDestination, projectMetadata);
  }

  protected Path archiveProject(File projectDestination, ProjectMetadata projectMetadata)
      throws IOException {
    return projectArchiver.archiveProject(projectDestination, projectMetadata.getName());
  }
}
