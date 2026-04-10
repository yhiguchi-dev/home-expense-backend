#!/bin/bash

set -euo pipefail

postgres_container_name=my_postgres
postgres_admin_user=postgres

db_user=expense
db_password=password
db_name=expense

cd "$(dirname "$0")"/..

source scripts/setup_postgres.sh

echo "Create User and Database for Local Development"

echo "CREATE USER :db_user WITH PASSWORD :'db_password';" | container exec -i "${postgres_container_name}" psql -U "${postgres_admin_user}" -v db_user="${db_user}" -v db_password="${db_password}" 2>/dev/null || true

echo "CREATE DATABASE :db_name OWNER :db_user;" | container exec -i "${postgres_container_name}" psql -U "${postgres_admin_user}" -v db_user="${db_user}" -v db_name="${db_name}" 2>/dev/null || true

echo "GRANT ALL PRIVILEGES ON DATABASE :db_name TO :db_user;" | container exec -i "${postgres_container_name}" psql -U "${postgres_admin_user}" -v db_user="${db_user}" -v db_name="${db_name}"

db_host=$(container inspect "${postgres_container_name}" | jq -r --arg name "${postgres_container_name}" '.[] | select(.configuration.id == $name) | .networks[] | select(.hostname == $name) | .ipv4Address' | sed 's/\/.*//')
echo "Database Host: ${db_host}"

echo "Building Flyway migration container..."
container build -t flyway-migration ./flyway

container run --rm \
  --name db-migration \
  -e FLYWAY_URL="jdbc:postgresql://${db_host}:5432/${db_name}" \
  -e FLYWAY_USER="${db_user}" \
  -e FLYWAY_PASSWORD="${db_password}" \
  -e FLYWAY_CONNECT_RETRIES=60 \
  -e FLYWAY_CLEAN_DISABLED=false \
  -e FLYWAY_BASELINE_VERSION=0.0.0 \
  -e FLYWAY_SCHEMAS=expense \
  -e FLYWAY_DEFAULT_SCHEMA=public \
  flyway-migration clean migrate

echo "Database setup completed."
echo "  Host: ${db_host}"
echo "  Port: 5432"
echo "  Database: ${db_name}"
echo "  User: ${db_user}"
