FROM amazoncorretto:21-al2023 AS dependencies
WORKDIR /app

COPY gradle/ ./gradle/
COPY *.gradle *.gradle.kts gradlew ./
COPY src/ ./src/

RUN dnf install -y findutils
RUN ./gradlew --no-daemon dependencies

FROM dependencies AS build
RUN ./gradlew --no-daemon shadowJar

FROM amazoncorretto:21-al2023-headless AS runtime

WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar

COPY ./sql ./sql
COPY flyway.conf ./
COPY entrypoint.sh ./

ENTRYPOINT ["./entrypoint.sh"]
