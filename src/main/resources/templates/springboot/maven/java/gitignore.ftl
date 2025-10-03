# Compiled source #
###################
*.com
*.class
*.dll
*.exe
*.o
*.so

# Packages #
############
# it's better to unpack these files and commit the raw source
# git has its own built in compression methods
*.7z
*.dmg
*.gz
*.iso
*.jar
*.rar
*.tar

# Logs and databases #
######################
*.log
*.sql
*.sqlite

# OS generated files #
######################
.DS_Store
.DS_Store?
._*
.Spotlight-V100
.Trashes
ehthumbs.db
Thumbs.db

# Eclipse / IntelliJ / VSCode IDE #
##################################
.classpath
.project
.settings/
.idea/
*.iws
*.iml
*.ipr

# Build directories #
#####################
target/
build/
bin/

# Maven specific #
##################
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
pom.xml.bak
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/

# Generated source folders #
############################
generated-sources/
generated-classes/

# Add placeholders for project-specific ignores (optional)
<#if ignoreList?has_content && ignoreList?size gt 0>
    <#list ignoreList as pattern>
        ${pattern}
    </#list>
</#if>