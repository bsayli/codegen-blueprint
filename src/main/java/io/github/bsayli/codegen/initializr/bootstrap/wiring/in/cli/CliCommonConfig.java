package io.github.bsayli.codegen.initializr.bootstrap.wiring.in.cli;

import io.github.bsayli.codegen.initializr.adapter.in.cli.CodegenCliExceptionHandler;
import io.github.bsayli.codegen.initializr.adapter.in.cli.CodegenCommand;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CliCommonConfig {

  @Bean
  public CodegenCommand codegenCommand() {
    return new CodegenCommand();
  }

  @Bean
  public CodegenCliExceptionHandler codegenCliExceptionHandler(MessageSource messageSource) {
    return new CodegenCliExceptionHandler(messageSource);
  }
}
