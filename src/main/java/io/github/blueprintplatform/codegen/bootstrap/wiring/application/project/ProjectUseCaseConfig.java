package io.github.blueprintplatform.codegen.bootstrap.wiring.application.project;

import io.github.blueprintplatform.codegen.application.port.out.ProjectArtifactsSelector;
import io.github.blueprintplatform.codegen.application.port.out.archive.ProjectArchiverPort;
import io.github.blueprintplatform.codegen.application.usecase.project.CreateProjectHandler;
import io.github.blueprintplatform.codegen.application.usecase.project.CreateProjectUseCase;
import io.github.blueprintplatform.codegen.application.usecase.project.ProjectBlueprintMapper;
import io.github.blueprintplatform.codegen.domain.port.out.filesystem.ProjectRootPort;
import io.github.blueprintplatform.codegen.domain.port.out.filesystem.ProjectWriterPort;
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
