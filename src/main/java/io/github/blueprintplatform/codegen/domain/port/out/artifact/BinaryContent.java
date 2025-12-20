package io.github.blueprintplatform.codegen.domain.port.out.artifact;

import static io.github.blueprintplatform.codegen.domain.policy.file.GeneratedFilePolicy.requireBinaryContent;

import java.util.Arrays;

public record BinaryContent(byte[] bytes) {

  public BinaryContent {
    requireBinaryContent(bytes);
    bytes = Arrays.copyOf(bytes, bytes.length);
  }

  public byte[] bytes() {
    return Arrays.copyOf(bytes, bytes.length);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    return o instanceof BinaryContent(byte[] other) && Arrays.equals(bytes, other);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(bytes);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public String toString() {
    return "BinaryContent[size=" + bytes.length + "]";
  }
}
