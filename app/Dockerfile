# syntax=docker/dockerfile:1.23.0
FROM ghcr.io/graalvm/native-image-community:25.0.2-ol9-20260120 AS builder

WORKDIR /work
ENV GRADLE_USER_HOME=/gradle-cache

RUN microdnf install -y findutils && microdnf clean all && rm -rf /var/cache/dnf

COPY gradlew ./
COPY gradle/ ./gradle/
COPY settings.gradle.kts build.gradle.kts ./
RUN --mount=type=cache,target=/gradle-cache \
    --mount=type=cache,target=/work/.gradle \
    ./gradlew resolveDependencies

COPY src/ ./src/

RUN --mount=type=cache,target=/gradle-cache \
    --mount=type=cache,target=/work/.gradle \
    ./gradlew build \
    -Dquarkus.native.enabled=true \
    -Dquarkus.package.jar.enabled=false \
    -x check

FROM gcr.io/distroless/base-nossl-debian13:nonroot AS runtime

ARG VERSION=unspecified
ARG GIT_SHA=unspecified
LABEL org.opencontainers.image.title="home-expense-api" \
      org.opencontainers.image.description="Home expense management API (Quarkus native)" \
      org.opencontainers.image.source="https://github.com/yhiguchi-dev/home-expense-backend" \
      org.opencontainers.image.licenses="MIT" \
      org.opencontainers.image.version="${VERSION}" \
      org.opencontainers.image.revision="${GIT_SHA}"

WORKDIR /app

COPY --chown=nonroot:nonroot --from=builder /work/build/*-runner /app/application

ENV QUARKUS_HTTP_HOST=0.0.0.0

EXPOSE 8080
USER nonroot
STOPSIGNAL SIGTERM

CMD ["/app/application"]
