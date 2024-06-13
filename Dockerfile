#
# Build stage
#
FROM public.ecr.aws/docker/library/maven:3.8.1-openjdk-17-slim AS build
COPY src /home/app/src
COPY settings.xml /home/app
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package install -s /home/app/settings.xml -DskipTests

#
# Package stage
#
FROM public.ecr.aws/docker/library/openjdk:17-jdk-oracle
ENV JAVA_OPTS=""
EXPOSE 8080
ARG JAR_FILE=/home/app/target/*.jar
COPY --from=build $JAR_FILE app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
