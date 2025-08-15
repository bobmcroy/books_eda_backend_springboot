#!/bin/bash
set -e

# 🔹 CONFIG
SPRING_PROFILE=local
DYNAMODB_PORT=8000
DOCKER_COMPOSE_FILE=docker-compose.yml
WAIT_INTERVAL=2
MAX_WAIT=60   # seconds to wait for DynamoDB
DYNAMODB_CONTAINER_NAME=dynamodb-local
SPRING_CONTAINER_NAME=book-service

# 🔹 Function to wait for DynamoDB
wait_for_dynamodb() {
  echo "Waiting for DynamoDB to be ready on port $DYNAMODB_PORT..."
  local elapsed=0
  while ! nc -z localhost $DYNAMODB_PORT >/dev/null 2>&1; do
    sleep $WAIT_INTERVAL
    elapsed=$((elapsed + WAIT_INTERVAL))
    if [ $elapsed -ge $MAX_WAIT ]; then
      echo "Timed out waiting for DynamoDB on port $DYNAMODB_PORT"
      exit 1
    fi
  done
  echo "DynamoDB is ready ✅"
}

# 🔹 Step 1: Check Docker daemon
if ! docker info >/dev/null 2>&1; then
  echo "Docker is not running. Attempting to start..."
  
  if [[ "$OSTYPE" == "darwin"* ]]; then
    open -a Docker
    echo "Waiting for Docker to start..."
    while ! docker info >/dev/null 2>&1; do
      sleep 2
    done
  else
    echo "Please start Docker manually."
    exit 1
  fi
fi
echo "Docker is running ✅"

# 🔹 Step 2: Handle existing DynamoDB container
if docker ps -a --format '{{.Names}}' | grep -q "^${DYNAMODB_CONTAINER_NAME}$"; then
  if [ "$(docker inspect -f '{{.State.Running}}' $DYNAMODB_CONTAINER_NAME)" == "true" ]; then
    echo "Stopping running container '${DYNAMODB_CONTAINER_NAME}'..."
    docker stop $DYNAMODB_CONTAINER_NAME
  fi
  echo "Starting existing container '${DYNAMODB_CONTAINER_NAME}'..."
  docker start $DYNAMODB_CONTAINER_NAME
else
  echo "No existing DynamoDB container found. It will be created via docker-compose."
fi

# 🔹 Step 3: Build the Spring Boot jar
echo "Building the application..."
mvn clean package -Dspring-boot.profile=$SPRING_PROFILE

# 🔹 Step 4: Start Spring Boot container via Docker Compose
echo "Starting Spring Boot container..."
docker compose -f $DOCKER_COMPOSE_FILE up --build -d $SPRING_CONTAINER_NAME

# 🔹 Step 5: Wait for DynamoDB to be ready
wait_for_dynamodb

# 🔹 Step 6: Tail Spring Boot logs
echo "Tailing Spring Boot logs..."
docker compose logs -f $SPRING_CONTAINER_NAME
