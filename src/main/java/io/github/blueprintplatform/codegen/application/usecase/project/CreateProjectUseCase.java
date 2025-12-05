package io.github.blueprintplatform.codegen.application.usecase.project;

public interface CreateProjectUseCase {
  CreateProjectResult handle(CreateProjectCommand command);
}
