# syntax=docker/dockerfile:1.23.0
FROM amazoncorretto:25.0.2-al2023 AS builder
WORKDIR /app
ENV GRADLE_USER_HOME=/gradle-cache

RUN dnf install -y findutils && dnf clean all && rm -rf /var/cache/dnf

COPY gradlew ./
COPY gradle/ ./gradle/
COPY settings.gradle.kts build.gradle.kts ./
RUN --mount=type=cache,target=/gradle-cache \
    --mount=type=cache,target=/app/.gradle \
    ./gradlew resolveDependencies

COPY src/ ./src/
RUN --mount=type=cache,target=/gradle-cache \
    --mount=type=cache,target=/app/.gradle \
    ./gradlew installDist

FROM gcr.io/distroless/java25-debian13:nonroot AS runtime

ARG VERSION=unspecified
ARG GIT_SHA=unspecified
LABEL org.opencontainers.image.title="home-expense-db-migration" \
      org.opencontainers.image.description="Flyway-based database migration runner for home-expense" \
      org.opencontainers.image.source="https://github.com/yhiguchi-dev/home-expense-backend" \
      org.opencontainers.image.licenses="MIT" \
      org.opencontainers.image.version="${VERSION}" \
      org.opencontainers.image.revision="${GIT_SHA}"

WORKDIR /app

COPY --from=builder --chown=nonroot:nonroot /app/build/install/db-migration/lib /app/lib
COPY --chown=nonroot:nonroot flyway.conf ./
COPY --chown=nonroot:nonroot ./sql ./sql

USER nonroot

ENTRYPOINT ["java", "-cp", "/app/lib/*", "dev.higuchi.DBMigrationApp"]
