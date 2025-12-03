package io.github.bsayli.codegen.initializr.bootstrap.wiring.application.project;

import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsSelector;
import io.github.bsayli.codegen.initializr.application.port.out.archive.ProjectArchiverPort;
import io.github.bsayli.codegen.initializr.application.usecase.project.CreateProjectHandler;
import io.github.bsayli.codegen.initializr.application.usecase.project.CreateProjectUseCase;
import io.github.bsayli.codegen.initializr.application.usecase.project.ProjectBlueprintMapper;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootPort;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectWriterPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectUseCaseConfig {

  @Bean
  public ProjectBlueprintMapper projectBlueprintMapper() {
    return new ProjectBlueprintMapper();
  }

  @Bean
  public CreateProjectUseCase createProjectHandler(
      ProjectBlueprintMapper mapper,
      ProjectRootPort rootPort,
      ProjectArtifactsSelector artifactsSelector,
      ProjectWriterPort writerPort,
      ProjectArchiverPort archiverPort) {

    return new CreateProjectHandler(mapper, rootPort, artifactsSelector, writerPort, archiverPort);
  }
}
