package io.github.blueprintplatform.codegen.domain.port.out.artifact;

import static io.github.blueprintplatform.codegen.domain.policy.file.GeneratedFilePolicy.*;

import java.nio.file.Path;
import java.util.Arrays;

@SuppressWarnings("java:S6206")
public final class GeneratedBinaryResource implements GeneratedResource {

  private final Path relativePath;
  private final byte[] bytes;

  public GeneratedBinaryResource(Path relativePath, byte[] bytes) {
    requireRelativePath(relativePath);
    requireBinaryContent(bytes);
    this.relativePath = relativePath;
    this.bytes = Arrays.copyOf(bytes, bytes.length);
  }

  @Override
  public Path relativePath() {
    return relativePath;
  }

  public byte[] bytes() {
    return Arrays.copyOf(bytes, bytes.length);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GeneratedBinaryResource other)) return false;
    return relativePath.equals(other.relativePath) && Arrays.equals(bytes, other.bytes);
  }

  @Override
  public int hashCode() {
    int result = relativePath.hashCode();
    result = 31 * result + Arrays.hashCode(bytes);
    return result;
  }

  @Override
  public String toString() {
    return "GeneratedBinaryResource[" + relativePath + ", size=" + bytes.length + "]";
  }
}
