# eu escolhi buildar toda vez para facilitar minhas alterações,
# mas podem remover este trecho após o primeiro build.
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]


