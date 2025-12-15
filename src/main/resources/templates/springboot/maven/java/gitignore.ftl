# Compiled source #
###################
*.class
*.o
*.so
*.dll
*.exe
*.com

# Packages #
############
# It's better to unpack archives and commit the raw sources.
# Git has its own built-in compression.
*.7z
*.dmg
*.gz
*.iso
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
###################################
.classpath
.project
.settings/
.idea/
*.iws
*.iml
*.ipr
.vscode/

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