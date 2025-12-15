<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>${springBootVersion}</version>
        <relativePath/>
    </parent>

    <groupId>${groupId}</groupId>
    <artifactId>${artifactId}</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <name>${projectName}</name>
    <description>${projectDescription}</description>

    <properties>
        <java.version>${javaVersion}</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <#if pomProperties??>
        <#list pomProperties?keys?sort as k>
        <${k}>${pomProperties[k]}</${k}>
    </#list>
    </#if>
    </properties>

    <dependencies>
        <#list dependencies as d>
            <dependency>
                <groupId>${d.groupId}</groupId>
                <artifactId>${d.artifactId}</artifactId>
                <#if d.versionPropertyKey?? && d.versionPropertyKey?has_content>
                    <version>${"$"}{${d.versionPropertyKey}}</version>
                <#elseif d.version?? && d.version?has_content>
                    <version>${d.version}</version>
                </#if>
                <#if d.scope?? && d.scope?has_content>
                    <scope>${d.scope}</scope>
                </#if>
            </dependency>
        </#list>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>