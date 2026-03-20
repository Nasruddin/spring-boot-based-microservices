# Course Management System

Spring Boot microservices sample for managing courses and reviews with:

- Spring Cloud Gateway
- OAuth2 resource server security via Keycloak
- PostgreSQL for courses
- MongoDB for reviews
- Prometheus, Grafana, Loki, Tempo, and Fluent Bit for observability
- Docker Compose and Minikube/Tilt deployment options

The previous README is preserved as [README.backup.md](README.backup.md).

## Table of Contents

- [Overview](#overview)
- [Repository Layout](#repository-layout)
- [Architecture](#architecture)
- [Ports and Endpoints](#ports-and-endpoints)
- [Prerequisites](#prerequisites)
- [Testing](#testing)
- [Run Locally](#run-locally)
- [Run with Docker Compose](#run-with-docker-compose)
- [Run with Minikube and Tilt](#run-with-minikube-and-tilt)
- [Security and Roles](#security-and-roles)
- [API Notes](#api-notes)
- [Observability](#observability)
- [Useful Files](#useful-files)
- [Troubleshooting](#troubleshooting)

## Overview

This repository contains four runtime services:

| Service | Purpose | Default local port |
|---|---|---:|
| `gateway-service` | Public entrypoint and routing | `9000` |
| `course-composite-service` | Aggregates course and review data | `5000` |
| `course-service` | CRUD for courses backed by PostgreSQL | `9001` |
| `review-service` | CRUD for reviews backed by MongoDB | `9002` |

Gateway routes:

- `/courses/**` -> `course-service`
- `/reviews/**` -> `review-service`
- `/course-aggregate/**` -> `course-composite-service`

## Repository Layout

| Path | Description |
|---|---|
| `microservices/course-service` | Course domain service |
| `microservices/review-service` | Review domain service |
| `microservices/course-composite-service` | Aggregate service |
| `spring-cloud/gateway-service` | API gateway |
| `util` | Shared utility module |
| `docker` | Compose files and observability config |
| `kubernetes` | Kubernetes manifests |
| `api-specs` | Bruno, Postman, and OpenAPI files |
| `grafana-dashboard` | Dashboard exports |
| `notes` | Architecture notes and images |

## Architecture

### Runtime flow

1. A client calls the gateway.
2. Gateway validates JWTs and forwards the request to a downstream service.
3. `course-composite-service` fans out to `course-service` and `review-service` for aggregate responses.
4. Core services persist data in PostgreSQL and MongoDB.
5. Metrics, traces, and logs flow to Prometheus, Tempo, Loki, and Grafana.

### Diagrams

- Context: `notes/images/context-level1.png`
- Container: `notes/images/container-level2.png`
- Component: `notes/images/component-level3.png`
- Deployment: `notes/images/deployment-level4.png`

## Ports and Endpoints

### Local host-mode services

| Component | URL |
|---|---|
| Gateway | `http://localhost:9000` |
| Course aggregate service | `http://localhost:5000` |
| Course service | `http://localhost:9001` |
| Review service | `http://localhost:9002` |
| Keycloak | `http://localhost:8081` |
| PostgreSQL | `localhost:5432` |
| MongoDB | `localhost:27017` |
| Grafana | `http://localhost:3000` |
| Prometheus | `http://localhost:9090` |
| Loki | `http://localhost:3100` |
| Tempo | `http://localhost:3200` |
| Fluent Bit | `localhost:24224` |

### Main gateway routes

| Route | Upstream behavior |
|---|---|
| `GET /courses/**` | Rewritten to `/api/courses/**` |
| `GET /reviews/**` | Rewritten to `/api/reviews/**` |
| `GET /course-aggregate/**` | Rewritten to `/api/course-aggregate/**` |

## Prerequisites

- Java 17+
- Maven 3.8+
- Docker and Docker Compose
- Minikube and Tilt if using Kubernetes
- `curl` or `httpie`
- `jq` is useful for token inspection

## Testing

Run the full reactor test suite from the repository root:

```bash
./mvnw test
```

Current test behavior:

- `course-service` uses H2 during tests, so local PostgreSQL is not required.
- `review-service` smoke tests still initialize the default MongoDB client; tests pass without MongoDB running, but connection-refused log noise is expected.
- The full reactor currently passes with `./mvnw test`.

## Run Locally

This mode runs the Spring Boot jars on the host machine instead of Docker containers.

### 1. Start infrastructure

```bash
cd docker
docker compose -f docker-compose-infra.yml up --build
```

This starts:

- PostgreSQL on `5432`
- MongoDB on `27017`
- Keycloak on `8081`

### 2. Optional: start observability

```bash
cd docker
docker compose -f docker-compose-observability.yml up --build
```

This starts:

- Grafana
- Prometheus
- Loki
- Tempo
- Fluent Bit

### 3. Start the microservices

From the repository root:

```bash
sh run.sh
```

Notes about `run.sh`:

- It builds the project with `mvn clean package -DskipTests`.
- It starts `course-composite-service`, `course-service`, and `review-service`.
- It kills processes on ports `5000`, `9000`, `9001`, and `9002` before starting.
- It does not start the gateway in host-mode. If you want gateway routing locally, start it separately:

```bash
./mvnw -pl spring-cloud/gateway-service spring-boot:run
```

## Run with Docker Compose

This mode runs the application services in Docker using the `docker` Spring profile.

### 1. Start infrastructure

```bash
cd docker
docker compose -f docker-compose-infra.yml up --build
```

### 2. Optional: start observability

```bash
cd docker
docker compose -f docker-compose-observability.yml up --build
```

### 3. Start application services

From the repository root:

```bash
sh run.sh docker
```

This executes `docker compose -f docker-compose-base.yml up --build` and exposes:

- Gateway on `http://localhost:9000`
- Course aggregate service on `http://localhost:8080`

Notes:

- `course-service` and `review-service` are not published directly to host ports in `docker-compose-base.yml`.
- External traffic is expected to go through the gateway on `9000`, or directly to the aggregate service on `8080` if you want to bypass the gateway.

Behind the scenes, the docker profile uses internal container names:

- `course`
- `review`
- `course-composite`
- `gateway-service`
- `postgres`
- `mongodb`
- `keycloak`

## Run with Minikube and Tilt

### 1. Start Minikube

```bash
minikube start \
  --profile=microservice-deployment \
  --memory=4g \
  --cpus=4 \
  --disk-size=30g \
  --kubernetes-version=v1.31.0 \
  --driver=docker
```

### 2. Enable ingress

```bash
minikube addons enable ingress --profile microservice-deployment
```

### 3. Point Docker CLI at Minikube

```bash
eval "$(minikube -p microservice-deployment docker-env)"
```

### 4. Build service images

```bash
sh build-images.sh
```

This builds:

- `course-composite-service`
- `course-service`
- `review-service`
- `gateway-service`

### 5. Start Tilt

```bash
tilt up
```

Useful commands:

```bash
tilt get uiresources
kubectl get pods,svc,ing
```

### 6. Access ingress

On macOS and Windows with Minikube Docker driver, run:

```bash
minikube tunnel --profile microservice-deployment
```

Add host entries if needed:

```text
127.0.0.1 grafana.local
127.0.0.1 keycloak.local
127.0.0.1 prometheus.local
```

Kubernetes ingress endpoints:

- Gateway: `http://127.0.0.1`
- Grafana: `http://grafana.local`
- Keycloak: `http://keycloak.local`
- Prometheus: `http://prometheus.local`

On Linux, `minikube ip --profile microservice-deployment` may be directly routable.

## Security and Roles

Keycloak realm import file:

- `course-management-realm-realm.json`

Admin console:

- `http://localhost:8081/admin`

Default Keycloak admin credentials:

- username: `admin`
- password: `admin`

### Import the realm

1. Open the Keycloak admin console.
2. Create or import a realm.
3. Import `course-management-realm-realm.json`.
4. Verify clients, roles, and users.

### Seeded users

| Username | Password | Roles |
|---|---|---|
| `nasruddin` | `password` | `admin`, `guest`, `course-read`, `course-write`, `review-read`, `review-write` |
| `courseuser` | `password` | `course-read`, `course-write` |
| `reviewuser` | `password` | `review-read`, `review-write` |

### Get an access token

```bash
curl -X POST http://localhost:8081/realms/course-management-realm/protocol/openid-connect/token \
  -d "grant_type=password" \
  -d "client_id=course-app" \
  -d "client_secret=v1sCIPjANbvyJ87RsTkYeI9xHonDqZh7" \
  -d "username=nasruddin" \
  -d "password=password" \
  -d "scope=openid roles" | jq
```

### Important authorization behavior

- `course-service` requires `COURSE-READ` or `COURSE-WRITE` for `/api/courses/**`.
- `review-service` requires `REVIEW-READ` or `REVIEW-WRITE` for `/api/reviews/**`.
- `course-composite-service` now requires both `COURSE-READ` and `REVIEW-READ` for `/api/course-aggregate/**`, because every aggregate request reads from both downstream services.

## API Notes

### Current behavior

- `PUT /api/courses/{id}` updates an existing course.
- `PUT /api/courses/{id}` returns `404 Not Found` if the target course does not exist.
- `GET /api/reviews/{id}` returns `404 Not Found` for a missing review.
- `DELETE /api/reviews/{id}` returns `404 Not Found` for a missing review.

### Useful starting endpoints

Direct service endpoints:

- `GET http://localhost:9001/api/courses`
- `POST http://localhost:9001/api/courses`
- `GET http://localhost:9002/api/reviews`
- `POST http://localhost:9002/api/reviews`
- `GET http://localhost:5000/api/course-aggregate/{id}/with-details`

Gateway endpoints:

- `GET http://localhost:9000/courses`
- `GET http://localhost:9000/reviews`
- `GET http://localhost:9000/course-aggregate/{id}/with-details`

When running with Docker Compose:

- Gateway entrypoint: `http://localhost:9000`
- Aggregate service direct endpoint: `http://localhost:8080/api/course-aggregate/{id}/with-details`

API spec assets:

- OpenAPI: `api-specs/openapi-specs/OpenApi.yml`
- Bruno collection: `api-specs/bruno`
- Postman collection: `api-specs/postman/postman-apis.json`

## Observability

### Service metrics

Each service exposes actuator metrics and Prometheus output.

Examples:

- `http://localhost:9001/actuator/prometheus`
- `http://localhost:9002/actuator/prometheus`
- `http://localhost:5000/actuator/prometheus`
- `http://localhost:9000/actuator/prometheus`

### UIs

| Tool | URL |
|---|---|
| Grafana | `http://localhost:3000` |
| Prometheus | `http://localhost:9090` |
| Loki | `http://localhost:3100` |
| Tempo | `http://localhost:3200` |

Grafana dashboards are available under:

- `grafana-dashboard/Spring Boot 3.x Statistics.json`
- `grafana-dashboard/Spring Boot Observability.json`

## Useful Files

| File | Purpose |
|---|---|
| `run.sh` | Build and run services locally or via Compose |
| `build-images.sh` | Build service images for Minikube |
| `docker/docker-compose-infra.yml` | PostgreSQL, MongoDB, Keycloak |
| `docker/docker-compose-base.yml` | Core application services |
| `docker/docker-compose-observability.yml` | Grafana, Prometheus, Loki, Tempo, Fluent Bit |
| `course-management-realm-realm.json` | Keycloak realm export |
| `Tiltfile` | Tilt entrypoint |

## Troubleshooting

### Tests fail because of databases

- `course-service` should no longer require PostgreSQL during tests.
- If tests fail in another module, rerun with `./mvnw -e test` and inspect the module-specific report in `target/surefire-reports`.

### Review-service logs MongoDB connection errors during tests

That is currently expected for the default smoke test if MongoDB is not running locally. The test still passes because it only verifies application startup.

### Aggregate endpoint returns `403`

The token must contain both:

- `ROLE_COURSE-READ`
- `ROLE_REVIEW-READ`

Using `courseuser` alone or `reviewuser` alone is not sufficient for `/course-aggregate/**`.

### Local run script does not start gateway

`sh run.sh` starts only:

- `course-composite-service`
- `course-service`
- `review-service`

Start the gateway separately if you want gateway routing in host-mode.

### Port conflicts

Check these common ports:

- `5000`
- `9000`
- `9001`
- `9002`
- `8081`
- `5432`
- `27017`
- `3000`
- `9090`
