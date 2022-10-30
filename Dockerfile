FROM gradle:7.2-jdk11-alpine AS builder
WORKDIR /
COPY . .
RUN gradle build -x test

FROM amazoncorretto:11.0.14-alpine3.15
ENV TZ="America/Bogota"
COPY --from=builder build/libs/authentication-api-0.0.1-SNAPSHOT.jar app.jar
CMD [ "java", "-jar", "app.jar" ]