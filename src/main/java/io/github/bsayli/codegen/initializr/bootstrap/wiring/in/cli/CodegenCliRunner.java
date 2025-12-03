package io.github.bsayli.codegen.initializr.bootstrap.wiring.in.cli;

import io.github.bsayli.codegen.initializr.adapter.in.cli.CodegenCliExceptionHandler;
import io.github.bsayli.codegen.initializr.adapter.in.cli.CodegenCommand;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@Profile("cli")
public class CodegenCliRunner implements ApplicationRunner {

  private static final List<String> FILTERED_OPTION_PREFIXES = List.of("--spring.");

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
    String[] cliArgs = extractCliArgs(args);

    CommandLine cmd = new CommandLine(codegenCommand, factory);
    cmd.setExecutionExceptionHandler(exceptionHandler);

    int exitCode = cmd.execute(cliArgs);
    System.exit(exitCode);
  }

  private String[] extractCliArgs(ApplicationArguments args) {
    String[] source = args.getSourceArgs();
    List<String> cli = new ArrayList<>(source.length);

    int index = 0;
    int length = source.length;

    while (index < length) {
      String arg = source[index];

      if (isFilteredOption(arg)) {
        index = skipOptionWithPossibleValue(source, index, length);
        continue;
      }

      cli.add(arg);
      index++;
    }

    return cli.toArray(String[]::new);
  }

  private boolean isFilteredOption(String arg) {
    for (String prefix : FILTERED_OPTION_PREFIXES) {
      if (arg.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }

  private int skipOptionWithPossibleValue(String[] source, int index, int length) {
    String option = source[index];
    int next = index + 1;

    if (option.contains("=")) {
      return next;
    }

    if (next >= length) {
      return next;
    }

    String candidate = source[next];
    if (candidate.startsWith("--")) {
      return next;
    }

    return next + 1;
  }
}
