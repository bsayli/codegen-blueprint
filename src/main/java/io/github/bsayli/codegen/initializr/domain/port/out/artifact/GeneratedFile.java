package io.github.bsayli.codegen.initializr.domain.port.out.artifact;

import static io.github.bsayli.codegen.initializr.domain.policy.file.GeneratedFilePolicy.*;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Arrays;

public sealed interface GeneratedFile permits GeneratedFile.Text, GeneratedFile.Binary {

  Path relativePath();

  record Text(Path relativePath, String content, Charset charset) implements GeneratedFile {
    public Text {
      requireRelativePath(relativePath);
      requireTextContent(content, charset);
    }
  }

  @SuppressWarnings("java:S2384") // Representation exposure false-positive; defensive copy applied
  record Binary(Path relativePath, byte[] bytes) implements GeneratedFile {

    public Binary(Path relativePath, byte[] bytes) {
      requireRelativePath(relativePath);
      requireBinaryContent(bytes);
      this.relativePath = relativePath;
      this.bytes = Arrays.copyOf(bytes, bytes.length);
    }

    @Override
    public byte[] bytes() {
      return Arrays.copyOf(bytes, bytes.length);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Binary(Path path, byte[] bytes1))) {
        return false;
      }
      return relativePath.equals(path) && Arrays.equals(bytes, bytes1);
    }

    @Override
    public int hashCode() {
      int result = relativePath.hashCode();
      result = 31 * result + Arrays.hashCode(bytes);
      return result;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
      return "GeneratedFile.Binary[" + relativePath + ", size=" + bytes.length + "]";
    }
  }
}

