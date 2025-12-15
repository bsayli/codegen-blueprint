spring:
application:
name: ${applicationName}

# You can activate profiles per environment if needed
# profiles:
#   active: dev

<#-- H2: only when spring-boot-starter-data-jpa is selected -->
<#if features?has_content && (features.h2)!false>
    datasource:
    url: jdbc:h2:mem:${applicationName}
    driver-class-name: org.h2.Driver
    username: sa
    password:
    jpa:
    hibernate:
    ddl-auto: update
    open-in-view: false
    h2:
    console:
    enabled: true
</#if>

<#-- Actuator: minimal defaults -->
<#if features?has_content && (features.actuator)!false>
    management:
    endpoints:
    web:
    exposure:
    include: health,info
</#if>

# Optional
# server:
#   port: 8080
#
# logging:
#   level:
#     root: INFO