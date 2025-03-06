echo "Building Docker images for Kubernetes using Minikube..."

# Define an array of services
SERVICES=(
  "microservices/course-composite-service:course-composite-service"
  "microservices/course-service:course-service"
  "microservices/review-service:review-service"
  "spring-cloud/gateway-service:gateway-service"
)

eval $(minikube docker-env --profile microservice-deployment)

# Iterate over services and build each one
for SERVICE in "${SERVICES[@]}"; do
  IFS=":" read -r DIR IMAGE <<< "$SERVICE"
  echo "Building $IMAGE..."
  cd "$DIR" || { echo "Failed to enter directory $DIR"; exit 1; }
  docker build -t "$IMAGE" .
  cd - >/dev/null || exit 1
  echo "$IMAGE built successfully!"
done

echo "All images built successfully!"
