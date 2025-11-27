# ${projectName}

${projectDescription}

---

## Tech Stack

* **Framework:** ${framework}
* **Language:** ${language}
* **Build Tool:** ${buildTool}
* **Java:** ${javaVersion}
* **Spring Boot:** ${springBootVersion}

**Coordinates**

* `groupId`: `${groupId}`
* `artifactId`: `${artifactId}`
* `package`: `${packageName}`

---

## Quick Start

```bash
# Build (using wrapper)
./mvnw clean package

# Run
./mvnw spring-boot:run
```

> If Maven is installed globally, you can also use `mvn` instead of `./mvnw`.

---

## Configuration

Default configuration file: `src/main/resources/application.yml`
You can override settings via environment variables or Spring profiles.

---

## Project Layout (short)

```
src
├─ main
│  ├─ java/…/${packageName?replace('.', '/')}
│  └─ resources/
└─ test
└─ java/…/${packageName?replace('.', '/')}
```

<#-- Optional hexagonal example section -->
<#if hasHexSample?? && hasHexSample>
    ------------------------------------

    ## Hexagonal Sample (optional)

    An example domain/application/adapter structure has been included to illustrate a minimal "Create" use case.
</#if>

---

## Dependencies (selected)

<#if dependencies?has_content>
    <#list dependencies as d>
        * ${d.groupId}:${d.artifactId}<#if d.version?? && d.version?has_content>:${d.version}</#if><#if d.scope?? && d.scope?has_content> (${d.scope})</#if>
    </#list>
<#else>
    * No additional dependencies selected.
</#if>

---

## Next Steps

* Update port/log levels in `application.yml`.
* Add additional dependencies if required.
* Extend the project with CI/CD or containerization steps.
