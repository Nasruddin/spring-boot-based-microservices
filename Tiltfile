# Define and build course-service
docker_build(
    "course-service",
    context="./microservices/course-service",
    dockerfile="./microservices/course-service/Dockerfile",
    live_update=[
        sync("./microservices/course-service/src", "/app/src"),  # Sync only Java files
        run("mvn package -DskipTests", trigger=["/app/src"]),    # Rebuild JAR when code changes
    ]
)
k8s_yaml([
    "microservices/course-service/kubernetes/deployment.yml",
    "microservices/course-service/kubernetes/service.yml"
])
k8s_resource(
    "course-service",
    port_forwards="9001:9001",  # Maps host port 9001 to container port 9001
    labels=["services"]         # Optional: Group in Tilt UI
)

# Define and build review-service
docker_build(
    "review-service",
    context="./microservices/review-service",
    dockerfile="./microservices/review-service/Dockerfile",
    live_update=[
        sync("./microservices/review-service/src", "/app/src"),  # Sync only Java files
        run("mvn package -DskipTests", trigger=["/app/src"]),    # Rebuild JAR when code changes
    ]
)
k8s_yaml([
    "microservices/review-service/kubernetes/deployment.yml",
    "microservices/review-service/kubernetes/service.yml"
])
k8s_resource(
    "review-service",
    port_forwards="9002:9002",
    labels=["services"]
)