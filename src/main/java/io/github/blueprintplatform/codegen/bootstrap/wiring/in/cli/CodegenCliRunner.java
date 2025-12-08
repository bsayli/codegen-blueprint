package io.github.blueprintplatform.codegen.bootstrap.wiring.in.cli;

import io.github.blueprintplatform.codegen.adapter.in.cli.CodegenCliExceptionHandler;
import io.github.blueprintplatform.codegen.adapter.in.cli.CodegenCommand;
import io.github.blueprintplatform.codegen.adapter.in.cli.shared.KeyedEnumConverter;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.dependency.SpringBootDependencyAlias;
import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class CodegenCliRunner implements ApplicationRunner {

  private static final String CLI_OPTION_NAME = "cli";
  private static final String CLI_FLAG = "--" + CLI_OPTION_NAME;
  private static final String LONG_OPTION_PREFIX = "--";

  private static final List<String> FILTERED_PREFIXES = List.of("--spring.");

  private final CodegenCommand codegenCommand;
  private final CommandLine.IFactory factory;
  private final CodegenCliExceptionHandler exceptionHandler;

  public CodegenCliRunner(
      CodegenCommand codegenCommand,
      CommandLine.IFactory factory,
      CodegenCliExceptionHandler exceptionHandler) {
    this.codegenCommand = codegenCommand;
    this.factory = factory;
    this.exceptionHandler = exceptionHandler;
  }

  @Override
  public void run(ApplicationArguments args) {
    if (!args.containsOption(CLI_OPTION_NAME)) {
      return;
    }

    var cliArgs = extractCliArgs(args.getSourceArgs());

    var cmd =
        new CommandLine(codegenCommand, factory)
            .registerConverter(BuildTool.class, new KeyedEnumConverter<>(BuildTool::fromKey))
            .registerConverter(Language.class, new KeyedEnumConverter<>(Language::fromKey))
            .registerConverter(
                ProjectLayout.class, new KeyedEnumConverter<>(ProjectLayout::fromKey))
            .registerConverter(JavaVersion.class, new KeyedEnumConverter<>(JavaVersion::fromKey))
            .registerConverter(
                SpringBootVersion.class, new KeyedEnumConverter<>(SpringBootVersion::fromKey))
                .registerConverter(SpringBootDependencyAlias.class, SpringBootDependencyAlias::fromKey);

    cmd.setExecutionExceptionHandler(exceptionHandler);

    System.exit(cmd.execute(cliArgs));
  }

  @SuppressWarnings("java:S135")
  private String[] extractCliArgs(String[] source) {
    var cli = new ArrayList<String>(source.length);
    boolean skipNextValue = false;

    for (int i = 0; i < source.length; i++) {
      var arg = source[i];

      if (CLI_FLAG.equals(arg)) {
        continue;
      }
      if (skipNextValue) {
        skipNextValue = false;
        continue;
      }

      if (shouldFilter(arg)) {
        if (requiresValueSkip(arg, source, i)) {
          skipNextValue = true;
        }
        continue;
      }

      cli.add(arg);
    }

    return cli.toArray(String[]::new);
  }

  private boolean shouldFilter(String arg) {
    return FILTERED_PREFIXES.stream().anyMatch(arg::startsWith);
  }

  private boolean requiresValueSkip(String arg, String[] source, int index) {
    var nextIndex = index + 1;
    return !arg.contains("=")
        && nextIndex < source.length
        && !source[nextIndex].startsWith(LONG_OPTION_PREFIX);
  }
}
