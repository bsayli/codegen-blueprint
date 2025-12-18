package io.github.blueprintplatform.codegen.bootstrap.wiring.in.cli;

import io.github.blueprintplatform.codegen.adapter.in.cli.CodegenCommand;
import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.option.*;
import java.io.PrintStream;
import java.io.PrintWriter;
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
    CommandLine cmd =  getCommandLine();
    return cmd.execute(args);
  }

  public int execute(String[] args, PrintStream out, PrintStream err) {
    CommandLine cmd = getCommandLine();

    cmd.setOut(new PrintWriter(out, true));
    cmd.setErr(new PrintWriter(err, true));

    return cmd.execute(args);
  }

  private CommandLine getCommandLine() {
    CommandLine commandLine =
            new CommandLine(codegenCommand, factory)
                    .registerConverter(SpringBootBuildToolOption.class, SpringBootBuildToolOption::fromKey)
                    .registerConverter(SpringBootLanguageOption.class, SpringBootLanguageOption::fromKey)
                    .registerConverter(
                            SpringBootJavaVersionOption.class, SpringBootJavaVersionOption::fromKey)
                    .registerConverter(SpringBootVersionOption.class, SpringBootVersionOption::fromKey)
                    .registerConverter(SpringBootLayoutOption.class, SpringBootLayoutOption::fromKey)
                    .registerConverter(
                            SpringBootArchitectureEnforcementOption.class,
                            SpringBootArchitectureEnforcementOption::fromKey)
                    .registerConverter(
                            SpringBootSampleCodeOption.class, SpringBootSampleCodeOption::fromKey)
                    .registerConverter(
                            SpringBootDependencyOption.class, SpringBootDependencyOption::fromKey);

    commandLine.setExecutionExceptionHandler(exceptionHandler);
    return commandLine;
  }
}
