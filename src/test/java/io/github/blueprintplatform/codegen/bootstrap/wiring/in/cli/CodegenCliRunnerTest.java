package io.github.blueprintplatform.codegen.bootstrap.wiring.in.cli;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.blueprintplatform.codegen.adapter.in.cli.CodegenCliExceptionHandler;
import io.github.blueprintplatform.codegen.adapter.in.cli.CodegenCommand;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

@Tag("unit")
@Tag("bootstrap")
class CodegenCliRunnerTest {

  @Test
  @DisplayName("--cli should be removed and remaining arguments preserved")
  void extractCliArgs_shouldRemoveCliFlag() throws Exception {
    var runner = new CodegenCliRunner(dummyExecutor());

    String[] source = {"--cli", "springboot", "--group-id", "com.acme"};

    String[] result = invokeExtractCliArgs(runner, source);

    assertThat(result).containsExactly("springboot", "--group-id", "com.acme");
  }

  @Test
  @DisplayName("--spring.* option with inline value should be filtered out")
  void extractCliArgs_shouldFilterSpringOptionWithInlineValue() throws Exception {
    var runner = new CodegenCliRunner(dummyExecutor());

    String[] source = {
            "--cli", "--spring.profiles.active=cli", "springboot", "--artifact-id", "demo-app"
    };

    String[] result = invokeExtractCliArgs(runner, source);

    assertThat(result).containsExactly("springboot", "--artifact-id", "demo-app");
  }

  @Test
  @DisplayName("--spring.* option with separate value should skip both option and value")
  void extractCliArgs_shouldFilterSpringOptionWithSeparateValue() throws Exception {
    var runner = new CodegenCliRunner(dummyExecutor());

    String[] source = {
            "--cli",
            "--spring.config.location",
            "application-test.yml",
            "springboot",
            "--group-id",
            "com.acme"
    };

    String[] result = invokeExtractCliArgs(runner, source);

    assertThat(result).containsExactly("springboot", "--group-id", "com.acme");
  }

  @Test
  @DisplayName("Non-filtered options should pass through unchanged")
  void extractCliArgs_shouldKeepNonFilteredOptions() throws Exception {
    var runner = new CodegenCliRunner(dummyExecutor());

    String[] source = {
            "--cli", "springboot", "--group-id", "com.acme", "--artifact-id", "demo-app"
    };

    String[] result = invokeExtractCliArgs(runner, source);

    assertThat(result)
            .containsExactly("springboot", "--group-id", "com.acme", "--artifact-id", "demo-app");
  }

  private String[] invokeExtractCliArgs(CodegenCliRunner runner, String[] source) throws Exception {
    Method m = CodegenCliRunner.class.getDeclaredMethod("extractCliArgs", String[].class);
    m.setAccessible(true);
    return (String[]) m.invoke(runner, new Object[] {source});
  }

  private CodegenCliExecutor dummyExecutor() {
    return new CodegenCliExecutor(dummyCommand(), dummyFactory(), dummyHandler());
  }

  private CodegenCommand dummyCommand() {
    return new CodegenCommand();
  }

  private CommandLine.IFactory dummyFactory() {
    return CommandLine.defaultFactory();
  }

  private CodegenCliExceptionHandler dummyHandler() {
    return null;
  }
}