#!/bin/sh

set -euo pipefail

gradle update
java -jar ./app/build/libs/app.jar