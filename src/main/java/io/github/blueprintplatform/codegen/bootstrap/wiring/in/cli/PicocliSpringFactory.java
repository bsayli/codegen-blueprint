package io.github.blueprintplatform.codegen.bootstrap.wiring.in.cli;

import io.github.blueprintplatform.codegen.adapter.in.cli.CodegenCommand;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
public class PicocliSpringFactory implements CommandLine.IFactory {

  private static final String CLI_COMMAND_BASE_PACKAGE =
      CodegenCommand.class.getPackage().getName();

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
