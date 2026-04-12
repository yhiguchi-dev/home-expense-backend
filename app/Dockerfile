FROM ghcr.io/graalvm/native-image-community:25.0.2-ol9-20260120 AS gradle-cache

WORKDIR /work

COPY gradle/ ./gradle/
COPY *.gradle *.gradle.kts gradlew ./
RUN microdnf install -y findutils && microdnf clean all
RUN ./gradlew --console=plain --no-daemon -q printVersion

FROM gradle-cache AS builder

COPY src/ ./src/

RUN ./gradlew --console=plain --no-daemon -q build \
    -Dquarkus.native.enabled=true \
    -Dquarkus.package.jar.enabled=false \
    -x check

FROM gcr.io/distroless/base-nossl-debian13:nonroot

WORKDIR /app

COPY --chown=nonroot:nonroot --from=builder /work/build/*-runner /app/application

EXPOSE 8080
USER nonroot

CMD ["/app/application", "-Dquarkus.http.host=0.0.0.0"]
