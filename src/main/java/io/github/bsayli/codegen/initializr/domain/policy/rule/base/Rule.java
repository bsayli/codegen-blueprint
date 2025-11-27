package io.github.bsayli.codegen.initializr.domain.policy.rule.base;

@FunctionalInterface
public interface Rule<T> {
  void check(T value);
}
