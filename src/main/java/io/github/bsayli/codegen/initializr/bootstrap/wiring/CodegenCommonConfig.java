package io.github.bsayli.codegen.initializr.bootstrap.wiring;

import io.github.bsayli.codegen.initializr.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.bsayli.codegen.initializr.adapter.shared.naming.StringCaseFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodegenCommonConfig {

  @Bean
  public StringCaseFormatter stringCaseFormatter() {
    return new StringCaseFormatter();
  }

  @Bean
  public PomDependencyMapper pomDependencyMapper() {
    return new PomDependencyMapper();
  }
}
