package io.github.blueprintplatform.codegen.domain.model.value.metadata;

import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectDescription;
import io.github.blueprintplatform.codegen.domain.model.value.naming.ProjectName;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;

public record ProjectMetadata(
    ProjectIdentity identity,
    ProjectName name,
    ProjectDescription description,
    PackageName packageName) {}
