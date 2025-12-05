package io.github.blueprintplatform.codegen.adapter.in.cli;

import io.github.blueprintplatform.codegen.adapter.in.cli.springboot.SpringBootGenerateCommand;
import picocli.CommandLine.Command;

@Command(
    name = "codegen",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    description = "Hexagonal project code generator CLI",
    subcommands = {SpringBootGenerateCommand.class})
public class CodegenCommand {}
