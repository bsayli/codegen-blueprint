package io.github.blueprintplatform.codegen.domain.port.out.artifact;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
class GeneratedResourceTest {

  @Test
  @DisplayName("Text with valid args should be created successfully")
  void text_validArgs_shouldCreateInstance() {
    Path relativePath = Path.of("pom.xml");
    GeneratedTextResource text = new GeneratedTextResource(relativePath, "<xml/>", UTF_8);

    assertThat(text.relativePath()).isEqualTo(relativePath);
    assertThat(text.content()).isEqualTo("<xml/>");
    assertThat(text.charset()).isEqualTo(UTF_8);
  }

  @Test
  @DisplayName("Text with null path should fail with file.path.not.blank")
  void text_nullPath_shouldFailPathNotBlank() {
    assertThatThrownBy(() -> new GeneratedTextResource(null, "x", UTF_8))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("file.path.not.blank"));
  }

  @Test
  @DisplayName("Text with null content should fail with file.content.not.blank")
  void text_nullContent_shouldFailContentNotBlank() {
    assertThatThrownBy(() -> new GeneratedTextResource(Path.of("pom.xml"), null, UTF_8))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("file.content.not.blank"));
  }

  @Test
  @DisplayName("Binary with null content should fail with file.content.not.blank")
  void binary_nullContent_shouldFailContentNotBlank() {
    assertThatThrownBy(() -> new GeneratedBinaryResource(Path.of("bin.dat"), null))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("file.content.not.blank"));
  }

  @Test
  @DisplayName(
      "BinaryContent should defensively copy, and equals/hashCode/toString should be stable")
  void binaryContent_shouldBeImmutableAndValueBased() {
    byte[] original = new byte[] {1, 2, 3};

    BinaryContent c1 = new BinaryContent(original);

    original[0] = 9;

    assertThat(c1.bytes()).containsExactly(1, 2, 3);

    byte[] view = c1.bytes();
    view[1] = 8;

    assertThat(c1.bytes()).containsExactly(1, 2, 3);

    BinaryContent c2 = new BinaryContent(new byte[] {1, 2, 3});
    BinaryContent c3 = new BinaryContent(new byte[] {1, 2, 4});

    assertThat(c1).isEqualTo(c2).hasSameHashCodeAs(c2).isNotEqualTo(c3);

    assertThat(c1.toString()).contains("BinaryContent").contains("size=3");
  }

  @Test
  @DisplayName("Binary should defensively copy bytes in ctor and accessor")
  void binary_shouldDefensivelyCopyBytes() {
    byte[] original = new byte[] {1, 2, 3};

    GeneratedBinaryResource binary =
        new GeneratedBinaryResource(Path.of("bin.dat"), new BinaryContent(original));

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
    assertThatThrownBy(() -> new GeneratedBinaryResource(null, new BinaryContent(new byte[] {1})))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("file.path.not.blank"));
  }

  @Test
  @DisplayName("Binary with null bytes should fail with file.content.not.blank")
  void binary_nullBytes_shouldFailContentNotBlank() {
    assertThatThrownBy(() -> new BinaryContent(null))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("file.content.not.blank"));
  }

  @Test
  @DisplayName("Binary equals/hashCode should depend on path and bytes")
  void binary_equalsAndHashCode_shouldDependOnPathAndBytes() {
    Path binRelativePath = Path.of("bin.dat");

    GeneratedBinaryResource b1 =
        new GeneratedBinaryResource(binRelativePath, new BinaryContent(new byte[] {1, 2, 3}));
    GeneratedBinaryResource b2 =
        new GeneratedBinaryResource(binRelativePath, new BinaryContent(new byte[] {1, 2, 3}));
    GeneratedBinaryResource b3 =
        new GeneratedBinaryResource(Path.of("other.bin"), new BinaryContent(new byte[] {1, 2, 3}));

    assertThat(b1).isEqualTo(b2).hasSameHashCodeAs(b2).isNotEqualTo(b3);
  }

  @Test
  @DisplayName("Directory with valid path should be created successfully")
  void directory_validPath_shouldCreateInstance() {
    Path relativePath = Path.of("src/main/java");
    GeneratedDirectory dir = new GeneratedDirectory(relativePath);

    assertThat(dir.relativePath()).isEqualTo(relativePath);
    assertThat(dir.toString()).contains("GeneratedDirectory");
  }

  @Test
  @DisplayName("Directory with null path should fail with file.path.not.blank")
  void directory_nullPath_shouldFailPathNotBlank() {
    assertThatThrownBy(() -> new GeneratedDirectory(null))
        .isInstanceOfSatisfying(
            DomainViolationException.class,
            dve -> assertThat(dve.getMessageKey()).isEqualTo("file.path.not.blank"));
  }
}
