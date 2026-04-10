#!/bin/bash

set -euo pipefail

postgres_container_name=my_postgres
postgres_admin_user=postgres

test_db_user=expense
test_db_password=password
test_db_name=expense_test

cd "$(dirname "$0")"/..

source scripts/setup_postgres.sh

echo "Setting up test database..."

echo "CREATE DATABASE :db_name OWNER :db_user;" | container exec -i "${postgres_container_name}" psql -U "${postgres_admin_user}" -v db_user="${test_db_user}" -v db_name="${test_db_name}" 2>/dev/null || true

container exec -i "${postgres_container_name}" psql -U "${test_db_user}" -d "${test_db_name}" < app/src/test/resources/init-test-schema.sql

echo "Test database setup completed."
echo "  Database: ${test_db_name}"
echo "  User: ${test_db_user}"
