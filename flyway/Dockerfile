FROM flyway/flyway:10.12.0 AS runtime

RUN find /flyway/drivers/* ! -name "postgresql*" | xargs -I {} rm -rf {}

COPY ./migration /flyway/sql
