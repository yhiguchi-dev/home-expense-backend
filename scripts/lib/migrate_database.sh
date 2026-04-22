#!/bin/bash
# Library: run Flyway migration against a database.
# Intended to be sourced, not executed directly.
#
# Required variables in the caller's scope:
#   db_host, db_user, db_password, db_name

echo "Building Flyway migration container..."
container build -t flyway-migration ./db-migration

container run --rm \
  --name db-migration \
  -e DB_HOST="${db_host}" \
  -e DB_USER="${db_user}" \
  -e DB_PORT=5432 \
  -e DB_PASSWORD="${db_password}" \
  -e DB_NAME="${db_name}" \
  flyway-migration clean migrate

echo "Flyway migration completed."
