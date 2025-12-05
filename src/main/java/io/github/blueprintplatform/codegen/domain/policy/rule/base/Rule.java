package io.github.blueprintplatform.codegen.domain.policy.rule.base;

@FunctionalInterface
public interface Rule<T> {
  void check(T value);
}
