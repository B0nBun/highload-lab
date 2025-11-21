#!/bin/sh

set -euo pipefail

set -a
source .env
set +a
java -jar ./app/build/libs/app.jar