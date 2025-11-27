package io.github.bsayli.codegen.initializr.application.usecase.project;

public interface CreateProjectUseCase {
  CreateProjectResult handle(CreateProjectCommand command);
}
