package io.github.blueprintplatform.codegen.domain.port.out.artifact;

import static io.github.blueprintplatform.codegen.domain.policy.file.GeneratedFilePolicy.requireBinaryContent;

import java.util.Arrays;

public record BinaryContent(byte[] bytes) {

  public BinaryContent(byte[] bytes) {
    requireBinaryContent(bytes);
    this.bytes = Arrays.copyOf(bytes, bytes.length);
  }

  @Override
  public byte[] bytes() {
    return Arrays.copyOf(bytes, bytes.length);
  }

  @Override
  public boolean equals(Object o) {
    return (this == o) || (o instanceof BinaryContent(byte[] other) && Arrays.equals(bytes, other));
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
