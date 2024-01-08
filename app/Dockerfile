FROM alpine:3.19.0 as lib

FROM ghcr.io/graalvm/native-image:ol9-java17-22.3.3 AS gradle-cache

WORKDIR /work

COPY gradle/ ./gradle/
COPY *.gradle *.gradle.kts gradlew ./
RUN microdnf install -y findutils-1:4.8.0-5.el9 && microdnf clean all
RUN ./gradlew --console=plain --no-daemon -q printVersion

FROM gradle-cache AS builder

COPY src/ ./src/

RUN ./gradlew --console=plain --no-daemon -q clean build -Dquarkus.package.type=native -x check

FROM gcr.io/distroless/base-nossl-debian12:nonroot

WORKDIR /app

COPY --from=lib /lib/libz.so.1 /lib/libz.so.1
COPY --from=lib /lib/libc.musl-x86_64.so.1 /lib/libc.musl-x86_64.so.1
COPY --chown=nonroot:nonroot --from=builder /work/build/*-runner /app/application

EXPOSE 8080
USER nonroot

ENV PORT 8080

CMD ["/app/application", "-Dquarkus.http.host=0.0.0.0"]
