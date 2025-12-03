package io.github.bsayli.codegen.initializr.adapter.in.cli;

import io.github.bsayli.codegen.initializr.adapter.error.exception.AdapterException;
import io.github.bsayli.codegen.initializr.application.error.exception.ApplicationException;
import io.github.bsayli.codegen.initializr.bootstrap.error.exception.InfrastructureException;
import io.github.bsayli.codegen.initializr.domain.error.exception.DomainException;
import java.io.IOException;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;

public class CodegenCliExceptionHandler implements IExecutionExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(CodegenCliExceptionHandler.class);

  private final MessageSource messageSource;

  public CodegenCliExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public int handleExecutionException(Exception ex, CommandLine cmd, ParseResult parseResult) {

    if (ex instanceof ParameterException parameterException) {
      cmd.getErr().println("codegen: usage error: " + parameterException.getMessage());
      cmd.usage(cmd.getErr());
      return 1;
    }

    Throwable cause = (ex.getCause() != null) ? ex.getCause() : ex;

    return switch (cause) {
      case DomainException domainException -> {
        printLocalizedError(cmd, domainException.getMessageKey(), domainException.getArgs());
        yield 1;
      }
      case ApplicationException applicationException -> {
        printLocalizedError(
            cmd, applicationException.getMessageKey(), applicationException.getArgs());
        yield 2;
      }
      case AdapterException adapterException -> {
        printLocalizedError(cmd, adapterException.getMessageKey(), adapterException.getArgs());
        yield 3;
      }
      case InfrastructureException infrastructureException -> {
        printLocalizedError(
            cmd, infrastructureException.getMessageKey(), infrastructureException.getArgs());
        yield 3;
      }
      case IOException ioException -> {
        cmd.getErr().println("codegen: error: I/O error occurred: " + ioException.getMessage());
        log.error("I/O error in CLI execution", ioException);
        yield 3;
      }
      default -> {
        log.error("Unexpected CLI error", cause);
        cmd.getErr()
            .println("codegen: error: Unexpected failure. Please try again or open an issue.");
        yield 99;
      }
    };
  }

  private void printLocalizedError(CommandLine cmd, String key, Object[] args) {
    String resolved = messageSource.getMessage(key, args, key, Locale.getDefault());
    cmd.getErr().println("codegen: error: " + resolved);
    cmd.getErr().println("(code: " + key + ")");
  }
}
