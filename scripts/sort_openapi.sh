#!/bin/bash

set -euo pipefail

FILE="${1:?Usage: sort_openapi.sh <openapi.yaml>}"

# Phase 1: Sort all keys alphabetically
yq -i -P 'sort_keys(..)' "$FILE"

# Phase 2: Override with JSON Schema property order
# https://spec.openapis.org/oas/3.1/schema/2022-10-07

# Root object
yq -i -P \
  'pick(["openapi","info","jsonSchemaDialect","servers","paths","webhooks","components","security","tags","externalDocs"])' \
  "$FILE"

# Info object
yq -i -P \
  '.info |= pick(["title","summary","description","termsOfService","contact","license","version"])' \
  "$FILE"

# Components object
yq -i -P \
  '.components |= pick(["schemas","responses","parameters","examples","requestBodies","headers","securitySchemes","links","callbacks","pathItems"])' \
  "$FILE"

# Path item: HTTP method order
yq -i -P \
  '.paths[] |= pick(["summary","description","servers","parameters","get","put","post","delete","options","head","patch","trace"])' \
  "$FILE"

# Operation object
yq -i -P \
  '.paths[][] |= pick(["tags","summary","description","externalDocs","operationId","parameters","requestBody","responses","callbacks","deprecated","security","servers"])' \
  "$FILE"

# Contact object
yq -i -P \
  '.info.contact |= pick(["name","url","email"])' \
  "$FILE"

# License object
yq -i -P \
  '.info.license |= pick(["name","identifier","url"])' \
  "$FILE"

# Server object (preserve x-* extensions)
yq -i -P \
  '.servers[] |= (pick(["url","description","variables"]) * (to_entries | map(select(.key | test("^x-"))) | from_entries))' \
  "$FILE"

# Tag object
yq -i -P \
  '.tags[] |= pick(["name","description","externalDocs"])' \
  "$FILE"

# Parameter object
yq -i -P \
  '.components.parameters[] |= pick(["name","in","description","required","deprecated","schema","content","allowEmptyValue","style","explode","allowReserved","example","examples"])' \
  "$FILE"

# Response object
yq -i -P \
  '.components.responses[] |= pick(["description","headers","content","links"])' \
  "$FILE"

# Request body object
yq -i -P \
  '.components.requestBodies[] |= pick(["description","content","required"])' \
  "$FILE"

# Media type object
yq -i -P \
  '(.components.responses[] | select(has("content")).content.[], .components.requestBodies[].content.[]) |= pick(["schema","encoding","example","examples"])' \
  "$FILE"

# Security scheme object
yq -i -P \
  '.components.securitySchemes[] |= pick(["type","description","name","in","scheme","bearerFormat","flows","openIdConnectUrl"])' \
  "$FILE"
