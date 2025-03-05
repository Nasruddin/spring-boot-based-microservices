# Define and build course-service
docker_build(
    "course-service",
    context="./microservices/course-service",
    dockerfile="./microservices/course-service/Dockerfile",
    live_update=[
        sync("./microservices/course-service/src", "/application/src"),  # Sync only Java files
        run("mvn package -DskipTests", trigger=["/application/src"]),    # Rebuild JAR when code changes
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
        sync("./microservices/review-service/src", "/application/src"),  # Sync only Java files
        run("mvn package -DskipTests", trigger=["/application/src"]),    # Rebuild JAR when code changes
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

# Define and build course-aggregate-service
docker_build(
    "course-composite-service",
    context="./microservices/course-composite-service",
    dockerfile="./microservices/course-composite-service/Dockerfile",
    live_update=[
        sync("./microservices/course-composite-service/src", "/application/src"),  # Sync only Java files
        run("mvn package -DskipTests", trigger=["/application/src"]),    # Rebuild JAR when code changes
    ]
)
k8s_yaml([
    "microservices/course-composite-service/kubernetes/deployment.yml",
    "microservices/course-composite-service/kubernetes/service.yml"
])
k8s_resource(
    "course-composite-service",
    port_forwards="5000:5000",
    labels=["services"]
)


# Define and build gateway-service
docker_build(
    "gateway-service",
    context="./spring-cloud/gateway-service",
    dockerfile="./spring-cloud/gateway-service/Dockerfile",
    live_update=[
        sync("./spring-cloud/gateway-service/src", "/application/src"),  # Sync only Java files
        run("mvn package -DskipTests", trigger=["/application/src"]),    # Rebuild JAR when code changes
    ]
)
k8s_yaml([
    "spring-cloud/gateway-service/kubernetes/deployment.yml",
    "spring-cloud/gateway-service/kubernetes/service.yml",
    "spring-cloud/gateway-service/kubernetes/ingress.yml"
])

k8s_resource(
    "gateway-service",
#    port_forwards="9000:9000",  # Maps host port 9000 to container port 9000
     labels=["services"]         # Optional: Group in Tilt UI
)