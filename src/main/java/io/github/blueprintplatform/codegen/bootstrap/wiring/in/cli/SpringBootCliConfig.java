package io.github.blueprintplatform.codegen.bootstrap.wiring.in.cli;

import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.CreateProjectCommandMapper;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.SpringBootGenerateCommand;
import io.github.blueprintplatform.codegen.application.usecase.project.CreateProjectUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBootCliConfig {

  @Bean
  public CreateProjectCommandMapper springBootCreateProjectCommandMapper() {
    return new CreateProjectCommandMapper();
  }

  @Bean
  public SpringBootGenerateCommand springBootGenerateCommand(
      CreateProjectCommandMapper mapper, CreateProjectUseCase createProjectUseCase) {

    return new SpringBootGenerateCommand(mapper, createProjectUseCase);
  }
}
