FROM amazoncorretto:17-al2023 AS gradle-cache

WORKDIR /work

COPY gradle/ ./gradle/
COPY *.gradle *.gradle.kts gradlew ./

RUN ./gradlew --console=plain --no-daemon -q printVersion

FROM gradle-cache AS builder

COPY src/ ./src/

RUN ./gradlew --console=plain --no-daemon -q clean build -Dquarkus.package.type=native -Dquarkus.native.container-build=true -x check

FROM gcr.io/distroless/base-debian12:nonroot

WORKDIR /app

COPY --chown=nonroot:nonroot --from=builder /work/build/*-runner /app/application

EXPOSE 8080
USER nonroot

ENV PORT 8080

CMD ["/app/application", "-Dquarkus.http.host=0.0.0.0"]
