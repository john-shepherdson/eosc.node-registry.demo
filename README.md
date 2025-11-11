# EOSC Node Registry (demo)

[![SQAaaS badge](https://github.com/EOSC-synergy/SQAaaS/raw/master/badges/badges_150x116/badge_software_silver.png)](https://api.eu.badgr.io/public/assertions/WwFLpKJ0SqqREmM0OZtzWw "SQAaaS silver badge achieved")

This repository contains the source code for a demonstration version of the
EOSC Node Registry.
See the EOSC Node Registry Architecture document for further details.

## Prerequisites

Java 21 or greater is required to build and run this application.

## Quick Start

1. Check prerequisites and install any required software.
2. Clone the repository to your local workspace.
3. Build the application using `mvn clean verify`.
4. Run the application using the following command: `mvn exec:java`.

## Getting started as developer

### Execute tests and run the application

```shell
# Execute all tests locally with default config
mvn clean test

# Run the app locally with default config and pre-populated database
mvn clean spring-boot:run
# Browse to http://localhost:8080 
# Stop the app with Ctrl+C
```

### Create and run service environment with docker-compose

```shell
# Package Java jar file and build docker image with required settings
mvn -DskipTests clean package docker:build -Pdocker-compose

# Create and start the environment in daemon mode (-d)
# as specified in docker-compose.yml
docker-compose -f target/docker/context/docker-compose.yml up -d

# Show all (-a) containers, service must be healthy to be available
docker ps -a

# Check out with our browser: http://localhost:8080
# Check out with our browser: http://localhost:8080/actuator

# Stop the environment
# All containers and the local network are stopped, but not deleted
docker-compose -f target/docker/context/docker-compose.yml stop

# Start the existing environment
# All containers and the local network are started
docker-compose -f target/docker/context/docker-compose.yml start

# Open a shell within the running container as specified in docker-compose.yml
# Exit the container shell again by Ctrl+C
docker exec -it $CONTAINERID /bin/sh
# Checkout filesystem within container with 'ls -la'

# Shutdown the environment
# All containers and the local network are stopped and deleted
docker-compose -f target/docker/context/docker-compose.yml down
```

## Project Structure

This project uses the standard Maven project structure.

``` text

<ROOT>
├── .mvn                # Maven wrapper.
├── src                 # Contains all source code and assets for the application.
|   ├── main
|   |   ├── java        # Contains release source code of the application.
|   |   └── resources   # Contains release resources assets.
|       └── wepapp      # Contains home page.
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
| [org.sonarsource.scanner.maven](https://github.com/SonarSource/sonar-scanner-maven) | SonarQube scanner for Maven|
| [com.mycila](https://github.com/mathieucarbou/license-maven-plugin) | Checks that each source file has licence header |
| [org.jacoco](https://github.com/jacoco/jacoco) | Java code coverage library |
| [org.apache.maven.plugins](https://github.com/apache/maven-surefire) |  Executes the unit tests of a Maven application or project |

## Configuration

The initial list of nodes (and their attributes) is configured using [`nodes.cvs`](src/main/resources/nodes.cvs).

``` text
Each line contains the details of a single node as a set of blocks.ttp client
Each block is separated using a comma. Lists of items within a block are separate
using a semicolon.
The order of blocks is
 *  node ID
 *  node name
 *  logo URI
 *  PID URI
 *   legal entity [Legal Entity Name;Legal Entity ROR ID]
 *   capabilities
      - list of triples separated by a semicolon
      - [capability name;capability endpoint URI; capability version]

For example
1,CESSDA Node,https://example.com/cessdalogo.png,hdl:20.500.12345/cessda
[CESSDA ERIC;https://cessda.eu/RORID],
https://cessda.eu/api,[Service Monitoring;https://example.com/api/service-monitoring;1.2];
[Resource Catalogue;https://example.com/api/resource-catalogue;3.0];
[Management System (including Helpdesk);https://example.com/api/management-system;1.3]
```

## Resources

[Issue Tracker](https://github.com/john-shepherdson/eosc.node-registry.demo?status=new&status=open)

[Swagger API docs](http://localhost:8080/swagger-ui/)

### Running Swagger tests

- for `Search for node by ID`, use any value between 1 to 10 inclusive
- for `Search for nodes by capability`, use one of
  - Resource Catalogue
  - Identity Management
  - Application Workflow Management
  - Service Monitoring
  - Service Accounting
  - Order Management
  - Management System (including Helpdesk)
