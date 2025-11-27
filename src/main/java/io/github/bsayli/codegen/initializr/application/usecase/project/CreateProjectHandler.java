package io.github.bsayli.codegen.initializr.application.usecase.project;

import static io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootExistencePolicy.FAIL_IF_EXISTS;

import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsSelector;
import io.github.bsayli.codegen.initializr.application.port.out.archive.ProjectArchiverPort;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootPort;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectWriterPort;
import java.nio.file.Path;

public class CreateProjectHandler implements CreateProjectUseCase {

  private final ProjectBlueprintMapper mapper;
  private final ProjectRootPort rootPort;
  private final ProjectArtifactsSelector artifactsSelector;
  private final ProjectWriterPort writerPort;
  private final ProjectArchiverPort archiverPort;

  public CreateProjectHandler(
      ProjectBlueprintMapper mapper,
      ProjectRootPort rootPort,
      ProjectArtifactsSelector artifactsSelector,
      ProjectWriterPort writerPort,
      ProjectArchiverPort archiverPort) {
    this.mapper = mapper;
    this.rootPort = rootPort;
    this.artifactsSelector = artifactsSelector;
    this.writerPort = writerPort;
    this.archiverPort = archiverPort;
  }

  @Override
  public CreateProjectResult handle(CreateProjectCommand command) {
    ProjectBlueprint bp = mapper.from(command);

    Path projectRoot =
        rootPort.prepareRoot(
            command.targetDirectory(), bp.getIdentity().artifactId().value(), FAIL_IF_EXISTS);

    ProjectArtifactsPort port = artifactsSelector.select(bp.getTechStack());
    var files = port.generate(bp);

    writerPort.write(projectRoot, files);

    String baseName = bp.getIdentity().artifactId().value();
    Path archive = archiverPort.archive(projectRoot, baseName);

    return new CreateProjectResult(archive);
  }
}
