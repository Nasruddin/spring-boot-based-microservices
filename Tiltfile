# Define and build infrastructure services
k8s_yaml([
    "kubernetes/infrastructure/keycloak/keycloak.yml",
    "kubernetes/infrastructure/postgres/postgres.yml",
    "kubernetes/infrastructure/mongodb/mongodb.yml",
    "kubernetes/infrastructure/prometheus/prometheus.yml",
    "kubernetes/infrastructure/fluent-bit/fluent-bit.yml",
    "kubernetes/infrastructure/loki/loki.yml",
    "kubernetes/infrastructure/tempo/tempo.yml",
    "kubernetes/infrastructure/grafana/grafana.yml"

])

# Define infrastructure resources
k8s_resource("keycloak", labels=["infra"], auto_init=True)
k8s_resource("prometheus", labels=["observability"], auto_init=True)
k8s_resource("fluent-bit", labels=["observability"], auto_init=True)
k8s_resource("loki", labels=["observability"], auto_init=True)
k8s_resource("tempo", labels=["observability"], auto_init=True)
k8s_resource("grafana", labels=["observability"], auto_init=True)
k8s_resource("course-postgres", labels=["infra"], auto_init=True)
k8s_resource("review-mongodb", labels=["infra"], auto_init=True)

# Define and build course-service
docker_build(
    "course-service",
    context="./microservices/course-service",
    dockerfile="./microservices/course-service/Dockerfile",
    live_update=[
        sync("./microservices/course-service/src", "/application/src"),
        run("mvn package -DskipTests", trigger=["/application/src"]),
    ]
)
k8s_yaml([
    "microservices/course-service/kubernetes/deployment.yml",
    "microservices/course-service/kubernetes/service.yml"
])
k8s_resource(
    "course-service",
    port_forwards="9001:9001",
    labels=["services"]
)

# Define and build review-service
docker_build(
    "review-service",
    context="./microservices/review-service",
    dockerfile="./microservices/review-service/Dockerfile",
    live_update=[
        sync("./microservices/review-service/src", "/application/src"),
        run("mvn package -DskipTests", trigger=["/application/src"]),
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

# Define and build course-composite-service
docker_build(
    "course-composite-service",
    context="./microservices/course-composite-service",
    dockerfile="./microservices/course-composite-service/Dockerfile",
    live_update=[
        sync("./microservices/course-composite-service/src", "/application/src"),
        run("mvn package -DskipTests", trigger=["/application/src"]),
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
        sync("./spring-cloud/gateway-service/src", "/application/src"),
        run("mvn package -DskipTests", trigger=["/application/src"]),
    ]
)
k8s_yaml([
    "spring-cloud/gateway-service/kubernetes/deployment.yml",
    "spring-cloud/gateway-service/kubernetes/service.yml",
    "spring-cloud/gateway-service/kubernetes/ingress.yml"
])
k8s_resource(
    "gateway-service",
    labels=["services"]
)