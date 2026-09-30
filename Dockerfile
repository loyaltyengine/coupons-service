FROM docker.io/maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy dependecies files
COPY libs/ ./libs
COPY pom.xml .

COPY src ./src

# Package the application
RUN mvn clean package  -DskipTests

FROM eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app

# Install curl
RUN apk add --no-cache curl

ARG JAR_FILE=./app/target/*.jar

COPY --from=builder ${JAR_FILE} coupons-service.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","coupons-service.jar"]