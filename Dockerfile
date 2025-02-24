#
# Copyright © 2025 EOSC Beyond (${email})
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# This image uses the Tomcat server
# Built using Maven 3 and JDK 21

# Compile
FROM eclipse-temurin@sha256:a0beb73cc0482c579ccff1a2df91024d8c01783d0772ad0c26f3817d3e9ad0b9 AS build
WORKDIR /app
COPY pom.xml .
# Copy the Node data
COPY src/main/resources/nodes.csv nodes.csv
RUN mvn dependency:resolve && mvn dependency:resolve-plugins
COPY . .
RUN mvn verify

# Package
FROM tomcat:10.1-jdk21 AS final
WORKDIR /usr/local/tomcat/webapps
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /src/target/*.war /usr/local/tomcat/webapps/ROOT.war