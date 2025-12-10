package io.github.blueprintplatform.codegen.bootstrap.wiring.in.cli;

import io.github.blueprintplatform.codegen.adapter.in.cli.CodegenCliExceptionHandler;
import io.github.blueprintplatform.codegen.adapter.in.cli.CodegenCommand;
import io.github.blueprintplatform.codegen.adapter.in.cli.shared.KeyedEnumConverter;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.dependency.SpringBootDependencyAlias;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeLevel;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class CodegenCliExecutor {

  private final CodegenCommand codegenCommand;
  private final CommandLine.IFactory factory;
  private final CodegenCliExceptionHandler exceptionHandler;

  public CodegenCliExecutor(
      CodegenCommand codegenCommand,
      CommandLine.IFactory factory,
      CodegenCliExceptionHandler exceptionHandler) {

    this.codegenCommand = codegenCommand;
    this.factory = factory;
    this.exceptionHandler = exceptionHandler;
  }

  public int execute(String[] args) {
    CommandLine cmd =
        new CommandLine(codegenCommand, factory)
            .registerConverter(BuildTool.class, new KeyedEnumConverter<>(BuildTool::fromKey))
            .registerConverter(Language.class, new KeyedEnumConverter<>(Language::fromKey))
            .registerConverter(
                ProjectLayout.class, new KeyedEnumConverter<>(ProjectLayout::fromKey))
            .registerConverter(JavaVersion.class, new KeyedEnumConverter<>(JavaVersion::fromKey))
            .registerConverter(
                SpringBootVersion.class, new KeyedEnumConverter<>(SpringBootVersion::fromKey))
            .registerConverter(
                SampleCodeLevel.class, new KeyedEnumConverter<>(SampleCodeLevel::fromKey))
            .registerConverter(SpringBootDependencyAlias.class, SpringBootDependencyAlias::fromKey);

    cmd.setExecutionExceptionHandler(exceptionHandler);

    return cmd.execute(args);
  }
}
