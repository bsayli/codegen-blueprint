package io.github.bsayli.codegen.initializr.bootstrap.wiring.in.cli;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class PicocliSpringFactory implements CommandLine.IFactory {

  private static final String CLI_COMMAND_BASE_PACKAGE =
      "io.github.bsayli.codegen.initializr.adapter.in.cli";

  private final ApplicationContext applicationContext;
  private final CommandLine.IFactory defaultFactory = CommandLine.defaultFactory();

  public PicocliSpringFactory(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public <K> K create(Class<K> cls) throws Exception {
    if (isCliCommandType(cls)) {
      return applicationContext.getBean(cls);
    }
    return defaultFactory.create(cls);
  }

  private boolean isCliCommandType(Class<?> cls) {
    String pkg = cls.getPackageName();
    return pkg.startsWith(CLI_COMMAND_BASE_PACKAGE);
  }
}
