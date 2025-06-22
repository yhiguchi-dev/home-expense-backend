#!/bin/bash

set -e

exec java -jar app.jar "$@" 2>/dev/null
