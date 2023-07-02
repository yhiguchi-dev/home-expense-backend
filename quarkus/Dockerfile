FROM ghcr.io/graalvm/native-image:ol9-java17-22.3.2 AS gradle-cache

WORKDIR /work

COPY gradle/ ./gradle/
COPY *.gradle *.gradle.kts gradlew ./

RUN microdnf install -y findutils-1:4.8.0-5.el9 && microdnf clean all
RUN ./gradlew dependencies

FROM gradle-cache AS builder

COPY src/ ./src/

RUN ./gradlew clean build -Dquarkus.package.type=native -x test -x check

FROM debian:bullseye-20230612

WORKDIR /app

COPY --from=builder /work/build/*-runner /app/application

EXPOSE 8080

CMD ["/app/application", "-Dquarkus.http.host=0.0.0.0"]
