# spring-boot-docker
This project is used to containerize and deploy a Spring Boot sample application to [AWS Fargate](https://aws.amazon.com/fargate/) using [AWS Copilot CLI](https://github.com/aws/copilot-cli).

### Prerequisites
The following items should be installed in your system:
* Java 8 or newer (full JDK not a JRE).
* git command line tool (https://help.github.com/articles/set-up-git)
* Your preferred IDE 
  * Eclipse with the m2e plugin. Note: when m2e is available, there is an m2 icon in `Help -> About` dialog. If m2e is not there, just follow the install process here: https://www.eclipse.org/m2e/
  * [Spring Tools Suite](https://spring.io/tools) (STS)
  * IntelliJ IDEA
  * [VS Code](https://code.visualstudio.com)
* [Docker Desktop](https://www.docker.com/products/docker-desktop)
* [AWS Copilot CLI](https://github.com/aws/copilot-cli/releases)
* [AWS Account](https://aws.amazon.com/free/)
  
## Running locally

The Spring Quickstart Guide is a [Spring Boot](https://spring.io/quickstart) application built using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line (it should work just as well with Java 8, 11 or 17):

```
git clone https://github.com/henryc/spring-boot-docker.git
cd spring-boot-docker
mvn clean package
mvn spring-boot:run
```

You can then access the spring boot application here: 
* http://localhost:8080/hello
* http://localhost:8080/actuator/health


## Building and tagging the Container

```
docker build -t springio/spring-boot-docker .
```
## Running the Container

```
docker run -p 8080:8080 springio/spring-boot-docker
```

## Other useful Docker commands

```
# List all the locally stored docker images
docker images
# List all the containers
docker ps
# Stop a running container
docker stop <container_id or container_name>
# Remove a stopped container
docker rmi <image_id>
```

## Dockerizing using Dockerfile

```
FROM public.ecr.aws/docker/library/maven:3.8.5-jdk-8-slim AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

FROM amazoncorretto:8-alpine-jdk
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY --chown=spring:spring --from=builder /app/${JAR_FILE} spring-boot-docker-demo.jar
HEALTHCHECK --interval=5m --timeout=3s CMD curl -f http://localhost:8080/actuator/health/ || exit 1
EXPOSE 8080
ENTRYPOINT ["java","-jar","/spring-boot-docker-demo.jar"]
```

## Deploying the container on existing infrastructure with AWS Copilot CLI

Create a new application with an existing domain name in Amazon Route53
```
copilot app init --domain <domain_name>
``` 

Creates an environment with imported VPC resources
```
copilot env init --import-vpc-id <vpc-id> \
--import-public-subnets <subnet-id, subnet-id> \
--import-private-subnets <subnet-id, subnet-id>
``` 
  
Create a "springboot-example" load balanced web service
```
copilot svc init --name springboot-example --svc-type "Load Balanced Web Service" --dockerfile ./Dockerfile
```

Delete all resources associated with the application
```
copilot app delete
```