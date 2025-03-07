#!/bin/bash

# Kill services running on ports 5000, 9001, and 9002
kill_service_on_port() {
  local port=$1
  local pid=$(lsof -ti :$port)
  if [[ -n "$pid" ]]; then
    echo "Killing process on port $port (PID: $pid)"
    kill -9 $pid
  fi
}

# Build all services before running them
mvn clean package -DskipTests

# Function to find and run the latest JAR file in a given service directory
run_service() {
  kill_service_on_port 5000
  kill_service_on_port 9000
  kill_service_on_port 9001
  kill_service_on_port 9002
  local service_dir=$1
  local jar_file=$(find "microservices/$service_dir/target" -type f -name "*.jar" | head -n 1)

  if [[ -z "$jar_file" ]]; then
    echo "No JAR file found for $service_dir!"
    return 1
  fi

  echo "Starting $service_dir using $jar_file..."
  java -jar "$jar_file" &
}

# Check if the first argument is 'docker'
if [[ "$1" == "docker" ]]; then
  echo "CD to docker dir..."
  cd docker
  echo "Stopping services using Docker Compose..."
  docker compose -f docker-compose-base.yml down -v
  echo "Starting services using Docker Compose..."
  docker compose -f docker-compose-base.yml up --build
else
  echo "Starting services using local JAR files..."
  # Run each service in the background
  run_service "course-composite-service"
  run_service "course-service"
  run_service "review-service"

  # Wait for all background processes to finish
  wait

  echo "All services started successfully."
fi
