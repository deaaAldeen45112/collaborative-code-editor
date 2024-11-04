
# FROM openjdk:20-jdk-bullseye
# RUN apt-get update && \
#     apt-get install -y docker.io && \
#     apt-get clean
# WORKDIR /app
# COPY ./code-editor-storage /app/code-editor-storage
# COPY ./scripts /app/scripts
# RUN chmod +x /app/scripts/*
# COPY ./target/editor-0.0.1-SNAPSHOT.jar app.jar
# ENTRYPOINT ["java", "-jar", "app.jar"]

FROM maven:3-amazoncorretto-20 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:20-jdk-bullseye
RUN apt-get update && \
    apt-get install -y docker.io && \
    apt-get clean

WORKDIR /app
COPY ./code-editor-storage /app/code-editor-storage
COPY ./scripts /app/scripts
RUN chmod +x /app/scripts/*
COPY --from=builder /app/target/editor-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
