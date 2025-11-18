package io.github.bsayli.codegen.initializr.domain.port.out.artifact;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
@DisplayName("Unit Test: GeneratedFile")
class GeneratedFileTest {

  @Test
  @DisplayName("Text with valid args should be created successfully")
  void text_validArgs_shouldCreateInstance() {
    GeneratedFile.Text text = new GeneratedFile.Text(Path.of("pom.xml"), "<xml/>", UTF_8);

    assertThat(text.relativePath()).isEqualTo(Path.of("pom.xml"));
    assertThat(text.content()).isEqualTo("<xml/>");
    assertThat(text.charset()).isEqualTo(UTF_8);
  }

  @Test
  @DisplayName("Text with null path should fail with file.path.not.blank")
  void text_nullPath_shouldFailPathNotBlank() {
    assertThatThrownBy(() -> new GeneratedFile.Text(null, "x", UTF_8))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("file.path.not.blank"));
  }

  @Test
  @DisplayName("Text with null content should fail with file.content.not.blank")
  void text_nullContent_shouldFailContentNotBlank() {
    assertThatThrownBy(() -> new GeneratedFile.Text(Path.of("pom.xml"), null, UTF_8))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("file.content.not.blank"));
  }

  @Test
  @DisplayName("Binary should defensively copy bytes in ctor and accessor")
  void binary_shouldDefensivelyCopyBytes() {
    byte[] original = new byte[] {1, 2, 3};
    GeneratedFile.Binary binary = new GeneratedFile.Binary(Path.of("bin.dat"), original);

    original[0] = 9;

    byte[] fromGetter = binary.bytes();
    assertThat(fromGetter).containsExactly(1, 2, 3);

    fromGetter[1] = 8;

    byte[] fromGetterAgain = binary.bytes();
    assertThat(fromGetterAgain).containsExactly(1, 2, 3);
  }

  @Test
  @DisplayName("Binary with null path should fail with file.path.not.blank")
  void binary_nullPath_shouldFailPathNotBlank() {
    assertThatThrownBy(() -> new GeneratedFile.Binary(null, new byte[] {1}))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("file.path.not.blank"));
  }

  @Test
  @DisplayName("Binary with null bytes should fail with file.content.not.blank")
  void binary_nullBytes_shouldFailContentNotBlank() {
    assertThatThrownBy(() -> new GeneratedFile.Binary(Path.of("bin.dat"), null))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("file.content.not.blank"));
  }

  @Test
  @DisplayName("Binary equals/hashCode should depend on path and bytes")
  void binary_equalsAndHashCode_shouldDependOnPathAndBytes() {
    GeneratedFile.Binary b1 = new GeneratedFile.Binary(Path.of("bin.dat"), new byte[] {1, 2, 3});
    GeneratedFile.Binary b2 = new GeneratedFile.Binary(Path.of("bin.dat"), new byte[] {1, 2, 3});
    GeneratedFile.Binary b3 = new GeneratedFile.Binary(Path.of("other.bin"), new byte[] {1, 2, 3});

    assertThat(b1).isEqualTo(b2).hasSameHashCodeAs(b2).isNotEqualTo(b3);
  }
}
