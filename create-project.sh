#!/usr/bin/env bash

# Common configuration
SPRING_BOOT_VERSION="3.4.3"
JAVA_VERSION="17"
BUILD_TOOL="maven"
PACKAGING="jar"
BASE_VERSION="1.0.0"
BASE_GROUP="io.javatab.microservices"
DEPENDENCIES="actuator,webflux"

# Function to initialize a Spring Boot microservice
init_microservice() {
    local service_name=$1
    local package_suffix=$2
    
    spring init \
        --boot-version="$SPRING_BOOT_VERSION" \
        --build="$BUILD_TOOL" \
        --java-version="$JAVA_VERSION" \
        --packaging="$PACKAGING" \
        --name="${service_name}-service" \
        --package-name="${BASE_GROUP}.${package_suffix}" \
        --groupId="${BASE_GROUP}.${package_suffix}" \
        --dependencies="$DEPENDENCIES" \
        --version="$BASE_VERSION" \
        "${service_name}-service"
}

# Main execution
main() {
    # Create and enter microservices directory
    mkdir -p microservices
    cd microservices || exit 1

    # Initialize each microservice
    init_microservice "course" "core.course"
    init_microservice "review" "core.review"
    init_microservice "course-composite" "composite.course"

    # Return to parent directory
    cd .. || exit 1
}

# Execute main function
main