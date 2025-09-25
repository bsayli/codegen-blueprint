package io.github.bsayli.codegen.initializr.application.usecase.createproject;

import static io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootExistencePolicy.FAIL_IF_EXISTS;

import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.application.port.out.archive.ProjectArchiverPort;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectRootPort;
import io.github.bsayli.codegen.initializr.domain.port.out.filesystem.ProjectWriterPort;
import java.nio.file.Path;

public class CreateProjectService implements CreateProjectUseCase {

  private final ProjectBlueprintMapper mapper;
  private final ProjectRootPort rootPort;
  private final ProjectArtifactsPort artifactsPort;
  private final ProjectWriterPort writerPort;
  private final ProjectArchiverPort archiverPort;

  public CreateProjectService(
      ProjectBlueprintMapper mapper,
      ProjectRootPort rootPort,
      ProjectArtifactsPort artifactsPort,
      ProjectWriterPort writerPort,
      ProjectArchiverPort archiverPort) {
    this.mapper = mapper;
    this.rootPort = rootPort;
    this.artifactsPort = artifactsPort;
    this.writerPort = writerPort;
    this.archiverPort = archiverPort;
  }

  @Override
  public CreateProjectResult execute(CreateProjectCommand command) {
    ProjectBlueprint bp = mapper.toDomain(command);

    Path projectRoot =
        rootPort.prepareRoot(
            command.targetDirectory(), bp.getIdentity().artifactId().value(), FAIL_IF_EXISTS);

    var files = artifactsPort.generate(bp);
    writerPort.write(projectRoot, files);

    String baseName = bp.getIdentity().artifactId().value();
    Path archive = archiverPort.archive(projectRoot, baseName);

    return new CreateProjectResult(archive);
  }
}
