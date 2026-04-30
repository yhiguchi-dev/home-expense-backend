#!/bin/bash
# Library: ensure the local PostgreSQL container is running.
# Intended to be sourced, not executed directly.

set -euo pipefail

postgres_container_name="${POSTGRES_CONTAINER_NAME:-my_postgres}"
postgres_version="${POSTGRES_VERSION:-18.3}"
postgres_admin_user="${POSTGRES_ADMIN_USER:-postgres}"
postgres_admin_password="${POSTGRES_ADMIN_PASSWORD:-password}"
postgres_admin_db_name="${POSTGRES_ADMIN_DB_NAME:-postgres}"

echo "Checking container system status..."
unavailable=false
container system status > /dev/null 2>&1 || {
  echo "Container system is not installed. Please install it first."
  unavailable=true
}

if [ "${unavailable}" = true ]; then
  echo "Starting container system..."
  container system start
fi

until container system status > /dev/null 2>&1 || true; do
  echo "Waiting for container system to be available..."
  sleep 2
done

container_exists=$(container inspect "${postgres_container_name}" | jq '. | length > 0')
echo "Container exists: ${container_exists}"

if [ "${container_exists}" = false ]; then
  echo "Starting PostgreSQL container..."
  container run -d \
  --name "${postgres_container_name}" \
  -e POSTGRES_USER="${postgres_admin_user}" \
  -e POSTGRES_PASSWORD="${postgres_admin_password}" \
  -e POSTGRES_DB="${postgres_admin_db_name}" \
  -p 5432:5432 \
  postgres:"${postgres_version}"
fi

container_status=$(container inspect "${postgres_container_name}" | jq -r --arg name "${postgres_container_name}" '.[] | select(.configuration.id == $name) | .status')

if [ "${container_status}" != "running" ]; then
  echo "Starting existing PostgreSQL container..."
  container start "${postgres_container_name}"
fi

echo "Waiting for PostgreSQL to be ready..."
until container exec -it "${postgres_container_name}" pg_isready -U "${postgres_admin_user}"; do
  sleep 2
done

echo "PostgreSQL is ready."
