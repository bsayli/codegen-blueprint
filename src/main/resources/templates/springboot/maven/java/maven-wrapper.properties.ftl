# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#   https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

# Maven Wrapper core version (kept for visibility & tests)
wrapperVersion=${wrapperVersion}

# Maven distribution to be used by the wrapper
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${mavenVersion}/apache-maven-${mavenVersion}-bin.zip
<#-- Optional checksum for Maven distribution -->
<#if distributionSha256Sum?? && distributionSha256Sum?has_content>
    distributionSha256Sum=${distributionSha256Sum}
</#if>

# Maven Wrapper JAR location (aligns with wrapperVersion above)
wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/${wrapperVersion}/maven-wrapper-${wrapperVersion}.jar
<#-- Optional checksum for Wrapper jar -->
<#if wrapperSha256Sum?? && wrapperSha256Sum?has_content>
    wrapperSha256Sum=${wrapperSha256Sum}
</#if>