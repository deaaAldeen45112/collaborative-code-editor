#!/bin/bash

TARGET_PATH=$1
docker run --rm -i \
    -v backend_code-editor-storage:/app/code-editor-storage:ro \
    -w "$TARGET_PATH" \
    gcc:latest sh -c "find . -name '*.cpp' | xargs g++ -o /tmp/myapp && /tmp/myapp"
