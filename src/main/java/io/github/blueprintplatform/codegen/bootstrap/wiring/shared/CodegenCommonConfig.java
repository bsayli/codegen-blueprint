package io.github.blueprintplatform.codegen.bootstrap.wiring.shared;

import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactPipelineExecutor;
import io.github.blueprintplatform.codegen.adapter.out.shared.templating.ClasspathTemplateScanner;
import io.github.blueprintplatform.codegen.adapter.shared.naming.StringCaseFormatter;
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

  @Bean
  public ClasspathTemplateScanner classpathTemplateScanner() {
    return new ClasspathTemplateScanner();
  }

  @Bean
  public ArtifactPipelineExecutor artifactPipelineExecutor() {
    return new ArtifactPipelineExecutor();
  }
}
