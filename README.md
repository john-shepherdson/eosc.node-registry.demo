# EOSC Node Registry (demo)

This repository contains the source code for a demonstration version of the EOSC Node Registry.
See the EOSC Node Registry Architecture document for further details.

## Prerequisites

Java 21 or greater is required to build and run this application.

## Quick Start

1. Check prerequisites and install any required software.
2. Clone the repository to your local workspace.
3. Build the application using `.\mvnw clean verify`.
4. Run the application using the following command: `.\mvnw exec:java`.

## Getting Started

To compile this application, run:

```bash
mvn install 
```

This will create a WAR suitable for deploying on Java Application servers (e.g. Tomcat, Jetty...).

To compile and run this application locally run:

```bash
mvn jetty:run
```

The application will run locally at <http://localhost:8080/>.

## Docker

To create a Docker image, run:

```bash
mvn install jib:dockerBuild -Dimage=IMAGE_TAG
```

Substitute `IMAGE_TAG` for a desired image tag. The application can then be run using
`docker run -p 8080:8080 IMAGE_TAG` and will be hosted on <http://localhost:8080/>.

## Options

To set url environment variable for nodedRegistry component:

```bash
export NODEREGISTRY_URL="http://localhost:1336"
```

## Project Structure

This project uses the standard Maven project structure.

```
<ROOT>
├── .mvn                # Maven wrapper.
├── src                 # Contains all source code and assets for the application.
|   ├── main
|   |   ├── java        # Contains release source code of the application.
|   |   └── resources   # Contains release resources assets.
|   └── test
|       ├── java        # Contains test source code.
|       └── resources   # Contains test resource assets.
└── target              # The output directory for the build.
```

## Technology Stack

Several frameworks are used in this application.

| Framework/Technology                               | Description                                               |
| -------------------------------------------------- | --------------------------------------------------------- |
| [Spring-boot](https://github.com/spring-projects/spring-boot) | Use to create Spring-powered, production-grade applications and services |
| [com.h2database](https://github.com/h2database/h2database) | Java SQL database |
| [org.apache.logging.log4j](https://github.com/apache/logging-log4j2) | Java logging framework |
| [Springdoc-openapi](https://github.com/springdoc/springdoc-openapi) | Helps with automating the generation of API documentation using Spring Boot projects |
| [junit-jupiter-api](https://github.com/junit-team/junit5 ) | Java unit testing framework |
| [Gson](https://github.com/google/gson) | Java library used to convert Java Objects and/or Strings into their JSON representation |
| [Apache HttpComponents client](https://github.com/apache/httpcomponents-client) | Http client |
| [com.google.cloud.tools](https://github.com/GoogleContainerTools/jib) | Use for building Docker and OCI images for Java applications |
| [org.sonarsource.scanner.maven](https://github.com/SonarSource/sonar-scanner-maven) | SonarQube scanner cfor Maven|
| [com.mycila](https://github.com/mathieucarbou/license-maven-plugin) | Checks that each source file has licence header |
| [org.jacoco](https://github.com/jacoco/jacoco) | Java code coverage library |
| [org.apache.maven.plugins](https://github.com/apache/maven-surefire) |  Executes the unit tests of a Maven application or project |

## Configuration

The initial list of nodes (and their attributes) is configured using [`nodes.cvs`](src/main/resources/nodes.cvs).

```
Each line contains the details of a single node as a set of blocks.ttp client
Each block is separated using a comma. Lists of items within a block are separate using a semicolon.
The order of blocks is
 *     node ID
 *     node name
 *     logo URI
 *     PID URI
 *     legal entity [Legal Entity Name;Legal Entity ROR ID]
 *     capabilities (list of triples [capability name;capability endpoint URI;capability version] separated by a semicolon)

For example
1,CESSDA Node,https://example.com/cessdalogo.png,hdl:20.500.12345/cessda,[CESSDA ERIC;https://cessda.eu/RORID],https://cessda.eu/api,[Service Monitoring;https://example.com/api/service-monitoring;1.2];[Resource Catalogue;https://example.com/api/resource-catalogue;3.0];[Management System (including Helpdesk);https://example.com/api/management-system;1.3]
```

## Resources

[Issue Tracker](https://github.com/john-shepherdson/eosc.node-registry.demo?status=new&status=open)

[Swagger API docs](http://localhost:8080/swagger-ui/)
