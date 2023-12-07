#!/bin/sh

# Check if docker is available
command -v docker >/dev/null 2>&1 || { echo >&2 "Docker is not installed. Aborting."; exit 1; }
command -v docker-compose >/dev/null 2>&1 || { echo >&2 "Docker Compose is not installed. Aborting."; exit 1; }

# Navigate to the root directory where docker-compose.yml resides
cd ..

# Stop and remove the existing containers
docker-compose down

# Build app image
docker build -t contact_book:latest .

# Run the services using docker-compose
docker-compose up -d

echo "Services are now up and running!"
