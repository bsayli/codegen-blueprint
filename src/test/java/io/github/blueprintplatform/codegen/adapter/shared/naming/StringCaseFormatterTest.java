package io.github.blueprintplatform.codegen.adapter.shared.naming;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("unit")
@Tag("adapter")
class StringCaseFormatterTest {

  private final StringCaseFormatter formatter = new StringCaseFormatter();

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"   ", "---___***   ###"})
  @DisplayName("null, blank or delimiters-only input should return empty string")
  void nullBlankOrDelimitersOnly_shouldReturnEmpty(String input) {
    String result = formatter.toPascalCase(input);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("single word should be capitalized")
  void singleWord_shouldBeCapitalized() {
    String result = formatter.toPascalCase("hello");

    assertThat(result).isEqualTo("Hello");
  }

  @Test
  @DisplayName("single word with mixed case should be normalized to PascalCase")
  void singleWord_mixedCase_shouldBeNormalized() {
    String result = formatter.toPascalCase("hELLo");

    assertThat(result).isEqualTo("Hello");
  }

  @Test
  @DisplayName("already PascalCase word should be normalized to first upper and rest lower")
  void pascalCaseWord_shouldNormalizeRestToLower() {
    String result = formatter.toPascalCase("MyValue");

    assertThat(result).isEqualTo("Myvalue");
  }

  @Test
  @DisplayName("space separated words should become concatenated PascalCase")
  void spaceSeparatedWords_shouldBecomePascalCase() {
    String result = formatter.toPascalCase("hello world example");

    assertThat(result).isEqualTo("HelloWorldExample");
  }

  @Test
  @DisplayName("multiple spaces and leading/trailing spaces should be ignored")
  void multipleSpaces_shouldBeHandled() {
    String result = formatter.toPascalCase("  hello   world   ");

    assertThat(result).isEqualTo("HelloWorld");
  }

  @Test
  @DisplayName("kebab-case should become PascalCase")
  void kebabCase_shouldBecomePascalCase() {
    String result = formatter.toPascalCase("my-service-name");

    assertThat(result).isEqualTo("MyServiceName");
  }

  @Test
  @DisplayName("snake_case should become PascalCase")
  void snakeCase_shouldBecomePascalCase() {
    String result = formatter.toPascalCase("my_service_name");

    assertThat(result).isEqualTo("MyServiceName");
  }

  @Test
  @DisplayName("mixed delimiters and digits should be handled correctly")
  void mixedDelimiters_andDigits_shouldBeHandled() {
    String result = formatter.toPascalCase(" my-service_42 NAME ");

    assertThat(result).isEqualTo("MyService42Name");
  }

  @Test
  @DisplayName("non-alphanumeric delimiters should be treated as separators")
  void nonAlphanumericDelimiters_shouldBeSeparators() {
    String result = formatter.toPascalCase("app@core#service!");

    assertThat(result).isEqualTo("AppCoreService");
  }
}
