#!/bin/bash

if [ -z "$1" ]; then
    echo "Usage: $0 <path_to_cpp_files>"
    exit 1
fi

cpp_files_path="$1"

container_id=$(docker run -d --rm -w /usr/src/myapp gcc:latest tail -f /dev/null)

if [ -z "$container_id" ]; then
    echo "Failed to start the container"
    exit 1
fi

if ! docker cp "$cpp_files_path/." "$container_id":/usr/src/myapp; then
    echo "Failed to copy files to the container"
    docker stop "$container_id" > /dev/null 2>&1
    exit 1
fi

docker exec -i "$container_id" sh -c "find . -name '*.cpp' | xargs g++ -o /tmp/myapp && /tmp/myapp"

docker stop "$container_id" > /dev/null 2>&1
