#!/bin/bash

set -e

# Check if docker-compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "docker-compose is not installed. Please install it and try again."
    exit 1
fi

echo "Starting Redis with docker..."

# Start Redis using docker-compose
docker-compose up -d

echo "Waiting for Redis to be healthy..."

# Retry mechanism to check Redis health
RETRY_COUNT=10
RETRY_DELAY=5
for ((i=1; i<=RETRY_COUNT; i++)); do
    HEALTH_STATUS=$(docker inspect --format='{{.State.Health.Status}}' $(docker-compose ps -q redis) 2>/dev/null || echo "unhealthy")
    if [ "$HEALTH_STATUS" == "healthy" ]; then
        echo "Redis is healthy."
        break
    fi
    if [ "$i" -eq "$RETRY_COUNT" ]; then
        echo "Redis did not become healthy after $((RETRY_COUNT * RETRY_DELAY)) seconds."
        exit 1
    fi
    echo "Retrying in $RETRY_DELAY seconds... ($i/$RETRY_COUNT)"
    sleep $RETRY_DELAY
done

echo "Redis is healthy, starting the application..."

# Build and run the application
echo "Building and running the Spring Boot application..."
./mvnw clean package
java -jar target/product-catalog-api-0.0.1-SNAPSHOT.jar