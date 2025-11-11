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
FROM eclipse-temurin
WORKDIR /src

# Copy in the Maven Wrapper
COPY mvnw .
COPY .mvn .mvn

# Resovle dependencies
COPY pom.xml .
RUN ./mvnw dependency:resolve && ./mvnw dependency:resolve-plugins

# Compile
COPY . .
RUN ./mvnw verify

# Package
FROM eclipse-temurin:21 AS final
WORKDIR /app
COPY --from=build /src/target/node-registry-1.0-SNAPSHOT.jar /libs/node-registry-1.0-SNAPSHOT.jar

# Entrypoint - Start Admin
#ENTRYPOINT ["java", "-jar", "libs/node-registry-1.0-SNAPSHOT.jar"]
ENTRYPOINT ["java", "-file", "com.eosc.node.registry.Main"]