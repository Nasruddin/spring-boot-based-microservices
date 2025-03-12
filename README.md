# Table of Contents
- [Background](#background)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Run the application](#run-the-application)
- [Check the endpoints](#check-the-endpoints)
- [Keycloak](#keycloak)
- [Observability](#observability)
- [Guide to grafana](#guide-to-grafana)
- [Verify the APIs](#verify-the-apis)
- [Troubleshoot & Tips](#troubleshoot-and-tips)


# Background
A foundational framework for building **Spring Boot**-based microservices, designed for a **modular**, **scalable**, and **observable** system to manage **courses** and **reviews**. It incorporates **Spring Security** with **OAuth 2.0** via **Keycloak** for **authentication** and **Spring Cloud Gateway** as the **API gateway**. The architecture integrates a modern **observability stack**, including **OpenTelemetry (OTel)**, **Grafana**, **Loki**, **Tempo**, and **Prometheus**. **MongoDB** and **PostgreSQL** serve as **persistent storage** solutions. Deployment is supported through **Docker Compose** for **local environments** and **Kubernetes** for **scalable deployments**. The system utilizes **Spring Boot** and **Spring Cloud** to enable seamless **microservices communication**, **security**, and **observability**.

# Container Diagram

## Description
The **Course Management System** consists of multiple Spring Boot microservices, a gateway, databases, and external systems for authentication and observability, deployed via Docker Compose or Kubernetes (Tilt).

## Containers
| Container                | Technology              | Description                                      |
|--------------------------|-------------------------|--------------------------------------------------|
| **Gateway Service**      | Spring Cloud Gateway    | Routes requests to microservices, handles load balancing. |
| **Course Service**       | Spring Boot             | Manages course data, interacts with Postgres.    |
| **Review Service**       | Spring Boot             | Manages review data, interacts with MongoDB.     |
| **Course Composite Service** | Spring Boot         | Aggregates data from Course and Review Services. |
| **Postgres Database**    | PostgreSQL              | Stores course data for Course Service.           |
| **MongoDB Database**     | MongoDB                 | Stores review data for Review Service.           |
| **Keycloak**             | Keycloak                | External auth server for SSO and role-based access. |
| **Grafana**              | Grafana                 | Visualization for metrics, logs, and traces.     |
| **Loki**                 | Loki                    | Log aggregation system.                          |
| **Tempo**                | Tempo                   | Distributed tracing system.                      |
| **Fluent-bit**           | Fluent-bit              | Log forwarding agent.                            |
| **Otel Collector**       | OpenTelemetry           | Collects and exports telemetry data.             |

## Interactions
- **Users/Admin** → **Gateway Service**: HTTP requests via browser/API client.
- **Gateway Service** → **Keycloak**: Authenticates requests (OAuth2/JWT).
- **Gateway Service** → **Course Service**: Routes course-related requests.
- **Gateway Service** → **Review Service**: Routes review-related requests.
- **Gateway Service** → **Course Composite Service**: Routes aggregate requests.
- **Course Service** ↔ **Postgres Database**: CRUD operations for course data.
- **Review Service** ↔ **MongoDB Database**: CRUD operations for review data.
- **Course Composite Service** → **Course Service**: Fetches course data.
- **Course Composite Service** → **Review Service**: Fetches review data.
- **All Services** → **Fluent-bit**: Sends logs.
- **All Services** → **Otel Collector**: Sends metrics and traces.
- **Fluent-bit** → **Loki**: Forwards logs.
- **Otel Collector** → **Tempo**: Sends traces.
- **Otel Collector** → **Grafana**: Sends metrics.
- **Grafana** ← **Loki**: Queries logs.
- **Grafana** ← **Tempo**: Queries traces.

# Architecture
---
### Level 1: System Context Diagram
**Description**: High-level view of the system and its external actors.

- **System**: Course Management System
    - A microservices-based application for managing courses and reviews.
- **Actors**:
    - **User**: Browses courses and submits reviews (via browser/API).
    - **Administrator**: Manages content and monitors system health.
- **External Systems**:
    - **Keycloak**: Authentication and authorization (OIDC/OAuth2).
    - **Grafana Observability Stack**: Grafana, Loki, Tempo, Fluent-bit, and OpenTelemetry (Otel) for monitoring and logging.
- **Interactions**:
    - User → System: HTTP requests via Gateway.
    - System → Keycloak: Authenticates users.
    - System → Grafana Stack: Sends observability data.
![System context](notes/images/context-level1.png)
---
### Level 2: Container Diagram
**Description**: Breaks the system into deployable units.

- **Containers**:
    1. **API Gateway (Spring Cloud Gateway)**
        - Tech: Spring Boot + Spring Cloud Gateway
        - Role: Routes requests, enforces security.
        - Interactions: User → Gateway → CourseComposite.
    2. **CourseComposite (Aggregate Microservice)**
        - Tech: Spring Boot
        - Role: Aggregates Course and Review data.
        - Interactions: Gateway → CourseComposite → Course/Review.
    3. **Course (Core Microservice)**
        - Tech: Spring Boot
        - Role: Manages course data.
        - Database: PostgreSQL
        - Interactions: CourseComposite → Course → PostgreSQL.
    4. **Review (Core Microservice)**
        - Tech: Spring Boot
        - Role: Manages review data.
        - Database: MongoDB
        - Interactions: CourseComposite → Review → MongoDB.
    5. **Keycloak**
        - Role: External auth provider.
        - Interactions: Gateway → Keycloak.
    6. **Observability Stack**:
        - **Fluent-bit**: Log collection.
        - **OpenTelemetry (OTel)**: Trace.
        - **Loki**: Log storage.
        - **Tempo**: Trace storage.
        - **Grafana**: Visualization.
        - Interactions: Microservices → Fluent-bit/OTel → Loki/Tempo → Grafana.
![Container](notes/images/container-level2.png)
---
### Level 3: Component Diagram
**Description**: Key components within containers.

- **API Gateway**:
    - Routing Component (Spring Cloud Gateway).
    - Security Component (Keycloak integration).
    - Observability Agent (OTel + Fluent-bit).
- **CourseComposite**:
    - Course Client (REST).
    - Review Client (REST).
    - Aggregation Logic.
    - Observability Agent.
- **Course**:
    - Course Controller (REST).
    - Course Service (Logic).
    - Course Repository (JPA/PostgreSQL).
    - Observability Agent.
- **Review**:
    - Review Controller (REST).
    - Review Service (Logic).
    - Review Repository (MongoDB).
    - Observability Agent.

![Component](notes/images/component-level3.png)
---
### Level 4: Deployment Notes
**Description**: Two deployment setups.

1. **Docker Compose Setup**:
    - Containers: Gateway, CourseComposite, Course + PostgreSQL, Review + MongoDB, Keycloak, Observability Stack.
    - Networking: Single Docker network.
    - Config: `docker-compose.yml`.

2. **Kubernetes Setup with Tilt**:
    - Pods: Gateway, CourseComposite, Course + PostgreSQL, Review + MongoDB, Keycloak, Observability Pods.
    - Resources: Ingress, Services, ConfigMaps/Secrets, PVCs.
    - Tilt: Automates dev with live updates.
    - Observability: Fluent-bit DaemonSet
![Deployment](notes/images/deployment-level4.png)

### Additional Notes
- **Tech**: Spring Boot, Spring Cloud, JPA, MongoDB driver, OTel, Fluent-bit.
- **Interactions**: REST/HTTP, JDBC, MongoDB protocol.
- **Scalability**: Kubernetes supports replicas; Docker Compose for local dev.


# Prerequisites

- **Java 17+** (Recommended for Spring Boot 3.x)
- **Maven 3.8+**
- **Docker & Docker Compose** (for local containerized deployment)
- **Minikube & Tilt** (for Kubernetes-based deployment)
- **Httpie / cURL** (for API testing)
- **Postman / Bruno**

# Run the application

You can start the application using either **Docker (Docker Compose)** or **Kubernetes (Tilt)**.

Ensure that your **Docker Engine** is running before proceeding.

### Running with Docker or Individual Services on Host OS

1. Start Persistent Services:
```shell
  cd docker && docker compose -f docker-compose-infra.yml up --build
```
This command initializes **PostgreSQL** and **MongoDB**.

2. Launch the Observability Stack:
 ```shell
    cd docker && docker compose -f docker-compose-observability.yml up --build
```
This set up **Grafana**, **Tempo**, **Loki**, **Fluent-bit**, **Prometheus**
3. Start the Microservices
```shell
    sh run.sh docker
```
or 
```shell
    sh run.sh
```
This will start all microservices on their respective ports

### Running with Tilt in Kubernetes env `(Minikube)`
1. Let's Start minikube
```shell
    minikube start \
                --profile=microservice-deployment \
                --memory=4g \
                --cpus=4 \
                --disk-size=30g \
                --kubernetes-version=v1.27 \
                --driver=docker
```
```html
😄  [microservice-deployment] minikube v1.35.0 on Darwin 15.3.1
    ▪ MINIKUBE_ACTIVE_DOCKERD=microservice-deployment
👉  Using Kubernetes 1.27.16 since patch version was unspecified
✨  Using the docker driver based on user configuration
📌  Using Docker Desktop driver with root privileges
👍  Starting "microservice-deployment" primary control-plane node in "microservice-deployment" cluster
🚜  Pulling base image v0.0.46 ...
🔥  Creating docker container (CPUs=4, Memory=4096MB) ...
🐳  Preparing Kubernetes v1.27.16 on Docker 27.4.1 ...
    ▪ Generating certificates and keys ...
    ▪ Booting up control plane ...
    ▪ Configuring RBAC rules ...
🔗  Configuring bridge CNI (Container Networking Interface) ...
🔎  Verifying Kubernetes components...
    ▪ Using image gcr.io/k8s-minikube/storage-provisioner:v5
🌟  Enabled addons: storage-provisioner, default-storageclass
🏄  Done! kubectl is now configured to use "microservice-deployment" cluster and "default" namespace by default
```
2. Enable Ingress addon
```shell
    minikube addons enable ingress --profile microservice-deployment
```
```markdown
💡  ingress is an addon maintained by Kubernetes. For any concerns contact minikube on GitHub.
    You can view the list of minikube maintainers at: https://github.com/kubernetes/minikube/blob/master/OWNERS
💡  After the addon is enabled, please run "minikube tunnel" and your ingress resources would be available at "127.0.0.1"
    ▪ Using image registry.k8s.io/ingress-nginx/controller:v1.11.3
    ▪ Using image registry.k8s.io/ingress-nginx/kube-webhook-certgen:v1.4.4
    ▪ Using image registry.k8s.io/ingress-nginx/kube-webhook-certgen:v1.4.4
🔎  Verifying ingress addon...
🌟  The 'ingress' addon is enabled
```
3. Run this to make sure minikube can read images from local registry
```shell
    eval $(minikube -p microservice-deployment docker-env)
```
4. Let's build the images
```shell
    sh build-images.sh      
```
```html
Building Docker images for Kubernetes using Minikube...
Building course-composite-service...
[+] Building 31.5s (14/14) FINISHED                                                                                                                                                                            docker:default
 => [internal] load build definition from Dockerfile                                                                                                                                                                     0.0s
 => => transferring dockerfile: 871B                                                                                                                                                                                     0.0s
.
.
.
.
gateway-service built successfully!
All images built successfully!
```
5. List the images 
```shell
    docker images
```
or
```shell
    docker image ls
```

```html
REPOSITORY                                           TAG        IMAGE ID       CREATED         SIZE
gateway-service                                      latest     2e07a4894e3d   3 minutes ago   339MB
review-service                                       latest     857bf2e92e8d   3 minutes ago   336MB
course-service                                       latest     ad9a018bd4a3   3 minutes ago   359MB
course-composite-service                             latest     5e1868bd77bc   3 minutes ago   328MB
registry.k8s.io/ingress-nginx/controller             <none>     ee44bc236803   5 months ago    293MB 
registry.k8s.io/ingress-nginx/kube-webhook-certgen   <none>     a62eeff05ba5   5 months ago    63.8MB 
registry.k8s.io/kube-apiserver                       v1.27.16   1113933272f1   7 months ago    123MB
registry.k8s.io/kube-controller-manager              v1.27.16   2db343b95a4c   7 months ago    115MB
registry.k8s.io/kube-scheduler                       v1.27.16   91ad8454afdd   7 months ago    57.7MB
registry.k8s.io/kube-proxy                           v1.27.16   ea1f910af975   7 months ago    79.9MB
registry.k8s.io/etcd                                 3.5.12-0   3861cfcd7c04   13 months ago   149MB
registry.k8s.io/coredns/coredns                      v1.10.1    ead0a4a53df8   2 years ago     53.6MB
registry.k8s.io/pause                                3.9        e6f181688397   2 years ago     744kB
gcr.io/k8s-minikube/storage-provisioner              v5         6e38f40d628d   3 years ago     31.5MB
```

You can also get Table view
```shell
    minikube image ls --format table --profile microservice-deployment
```
```html
|----------------------------------------------------|----------|---------------|--------|
|                       Image                        |   Tag    |   Image ID    |  Size  |
|----------------------------------------------------|----------|---------------|--------|
| docker.io/library/review-service                   | latest   | 857bf2e92e8d6 | 336MB  |
| docker.io/library/course-composite-service         | latest   | 5e1868bd77bc2 | 328MB  |
| registry.k8s.io/kube-controller-manager            | v1.27.16 | 2db343b95a4c2 | 115MB  |
| registry.k8s.io/kube-scheduler                     | v1.27.16 | 91ad8454afddc | 57.7MB |
| docker.io/library/gateway-service                  | latest   | 2e07a4894e3d5 | 339MB  |
| docker.io/library/course-service                   | latest   | ad9a018bd4a3c | 359MB  |
| registry.k8s.io/ingress-nginx/kube-webhook-certgen | <none>   | a62eeff05ba51 | 63.8MB | 
| registry.k8s.io/kube-apiserver                     | v1.27.16 | 1113933272f1e | 123MB  | 
| registry.k8s.io/kube-proxy                         | v1.27.16 | ea1f910af975c | 79.9MB |
| gcr.io/k8s-minikube/storage-provisioner            | v5       | 6e38f40d628db | 31.5MB |
| registry.k8s.io/coredns/coredns                    | v1.10.1  | ead0a4a53df89 | 53.6MB |
| registry.k8s.io/pause                              | 3.9      | e6f1816883972 | 744kB  |
| registry.k8s.io/ingress-nginx/controller           | <none>   | ee44bc2368033 | 293MB  |
| registry.k8s.io/etcd                               | 3.5.12-0 | 3861cfcd7c04c | 149MB  |
|----------------------------------------------------|----------|---------------|--------|
```
6. Let's start the Microservices
```shell
    Tilt up
```
```html
Tilt started on http://localhost:10350/
v0.33.22, built 2025-01-03

(space) to open the browser
(s) to stream logs (--stream=true)
(t) to open legacy terminal mode (--legacy=true)
(ctrl-c) to exit
```
```shell
    tilt get uiresources
```
```html
NAME                       CREATED AT
course-composite-service   2025-03-11T10:37:08Z
prometheus                 2025-03-11T10:37:08Z
fluent-bit                 2025-03-11T10:37:08Z
uncategorized              2025-03-11T10:37:08Z
review-service             2025-03-11T10:37:08Z
gateway-service            2025-03-11T10:37:08Z
keycloak                   2025-03-11T10:37:08Z
review-mongodb             2025-03-11T10:37:08Z
tempo                      2025-03-11T10:37:08Z
loki                       2025-03-11T10:37:08Z
course-service             2025-03-11T10:37:08Z
grafana                    2025-03-11T10:37:08Z
course-postgres            2025-03-11T10:37:08Z
(Tiltfile)                 2025-03-11T10:37:08Z
```
```shell
    kubectl get pods,svc,ingress
```
```markdown
NAME                                            READY   STATUS              RESTARTS   AGE
pod/course-composite-service-7b57d566c4-6tz89   1/1     Running             0          85s
pod/course-postgres-5d748fd4cc-v72rq            0/1     ContainerCreating   0          2m43s
pod/course-service-76b45b858b-z6dwx             1/1     Running             0          83s
pod/fluent-bit-7f54dc97f-4zfxl                  0/1     ContainerCreating   0          2m43s
pod/gateway-service-588c7cff7b-f4gfl            1/1     Running             0          14s
pod/grafana-7d8cd9f95d-6qrcd                    0/1     ContainerCreating   0          2m43s
pod/keycloak-dd58c7c99-9gb6j                    0/1     ContainerCreating   0          2m43s
pod/loki-0                                      0/1     ContainerCreating   0          2m43s
pod/prometheus-74c59fb86c-792vs                 0/1     ContainerCreating   0          2m43s
pod/review-mongodb-695dc8799-4lpfm              0/1     ContainerCreating   0          2m43s
pod/review-service-9fc59c466-nkg9z              1/1     Running             0          84s
pod/tempo-54cb98d67f-kqzhv                      0/1     ContainerCreating   0          2m43s

NAME                               TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE
service/course-composite-service   ClusterIP   10.105.126.22    <none>        80/TCP                       86s
service/course-postgres            ClusterIP   10.97.180.213    <none>        5432/TCP                     2m44s
service/course-service             ClusterIP   10.107.49.7      <none>        80/TCP                       83s
service/fluent-bit                 ClusterIP   10.102.247.251   <none>        24224/TCP                    2m43s
service/gateway-service            ClusterIP   10.96.187.185    <none>        80/TCP                       14s
service/grafana                    ClusterIP   10.96.161.239    <none>        3000/TCP                     2m43s
service/keycloak                   ClusterIP   10.109.159.232   <none>        8080/TCP                     2m44s
service/kubernetes                 ClusterIP   10.96.0.1        <none>        443/TCP                      4m36s
service/loki                       ClusterIP   10.100.62.50     <none>        3100/TCP                     2m43s
service/prometheus                 ClusterIP   10.99.78.150     <none>        9090/TCP                     2m43s
service/review-mongodb             ClusterIP   10.101.104.79    <none>        27017/TCP                    2m44s
service/review-service             ClusterIP   10.101.51.125    <none>        80/TCP                       84s
service/tempo                      ClusterIP   10.106.248.234   <none>        4317/TCP,4318/TCP,3200/TCP   2m43s

NAME                                           CLASS   HOSTS              ADDRESS        PORTS   AGE
ingress.networking.k8s.io/gateway-ingress      nginx   *                  192.168.49.2   80      2m44s
ingress.networking.k8s.io/grafana-ingress      nginx   grafana.local      192.168.49.2   80      2m44s
ingress.networking.k8s.io/keycloak-ingress     nginx   keycloak.local     192.168.49.2   80      2m44s
ingress.networking.k8s.io/prometheus-ingress   nginx   prometheus.local   192.168.49.2   80      2m44s
```
![Tilt web page](notes/images/tilt.png)
---
# Check the endpoints
> [!NOTE]
> On macOS and Windows, the Minikube ingress add-on doesn't support using the cluster's IP when running on Docker, so minikube tunnel --profile polar is required to expose the cluster locally via 127.0.0.1, similar to kubectl port-forward but for the entire cluster.
>```shell
>  minikube tunnel --profile microservice-deployment
>```
> Add below in your /etc/hosts
> ```shell
>   vi /etc/hosts
>```
> 
> ```
>    127.0.0.1       grafana.local
> 
>    127.0.0.1       keycloak.local
> 
>    127.0.0.1       prometheus.local
> ```
 

| **Components**      | **Docker**                             | **Kubernetes on Mac**                | Note                                           |
|---------------------|----------------------------------------|--------------------------------------|------------------------------------------------|
| **Gateway**         | http://localhost:9000                  | http://127.0.0.1:80                  | /course-aggregate/{courseid}/with-details      |
| **CourseComposite** | http://localhost:8080/course-aggregate | http://127.0.0.1:80/course-aggregate |                                                |
| **Course**          | http://localhost:8080/courses          | http://127.0.0.1:80/courses          |                                                |
| **Review**          | http://localhost:8080/reviews          | http://127.0.0.1:80/reviews          |                                                |
| **Grafana**         | http://localhost:3000                  | http://grafana.local                 | Add `127.0.0.1 grafana.local` in /etc/hosts    |
| **Loki**            | http://loki:3100                       | http://loki:3100                     |                                                |
| **Tempo**           | http://tempo:3200                      | http://tempo:3200                    |                                                |
| **Fluent-bit**      | http://fluent-bit:24224                | http://fluent-bit:24224              | http on 4318 and grpc on 4317                  |
| **Prometheus**      | http://localhost:9090                  | http://prometheus.local              | Add `127.0.0.1 prometheus.local` in /etc/hosts |
| **Keycloak**        | http://localhost:8081                  | http://keycloak.local                | Add `127.0.0.1 keycloak.local` in /etc/hosts   |

> [!TIP]
> On Linux, Minikube runs as a native process directly on the host machine, rather than inside a virtual machine or a Docker container. This allows it to acquire a real, routable IP address that can be accessed from the host system without extra configuration.
>
> ```
>   $ minikube ip --profile microservice-deployment
>   192.154.19.8 
> ```
> 
> Now, all the above tabular endpoints available at http://192.154.19.8/**

Also, please use **OpenAPI specs**, **bruno** or **postman** for API details. I will add Swagger/SpringDoc as when I get time!!

# Keycloak
### Head to Keycloak using above table ```http://localhost:8081/admin``` or ```http://keycloak.local/admin```
Create realm using `course-management-realm-realm.json` provided in the repo
- Login to Keycloak → Go to http://localhost:8080/admin admin/admin
- Create Realm → Click "Create Realm" in the top-left dropdown
- Import Realm → Click "Import", select couse-management-realm-realm.json, and click "Create"
- Verify → Check Clients, Users, Roles, and Mappers in the new realm

![Keycloak web page](notes/images/keycloak.png)

### Keycloak Users in `course-management-realm`

We have **3** users configured in **Keycloak** with the following credentials and roles:

| Username    | Password  | Assigned Roles                                      |
|------------|----------|------------------------------------------------------|
| `nasruddin`  | `password` | `admin`, `guest`, `course-read`, `course-write`, `review-read`, `review-write` |
| `courseuser` | `password` | `course-read`, `course-write`                     |
| `reviewuser` | `password` | `review-read`, `review-write`                     |

> [!Notes]
>- **`nasruddin`** has **full access** to courses and reviews, along with admin privileges.
>- **`courseuser`** can only read and write courses.
>- **`reviewuser`** can only read and write reviews.


> [!TIP] Call the access token API to obtain a token, then visit jwt.io to inspect it.
> 
> ```shell
>  curl -X POST http://localhost:8081/realms/course-management-realm/protocol/openid-connect/token  -d "grant_type=password"  -d "client_id=course-app"  -d "client_secret=v1sCIPjANbvyJ87RsTkYeI9xHonDqZh7"  -d "username=nasruddin"  -d "password=password" -d "scope=openid roles" | jq
>```
>```markdown
>{
>"access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0T1lYQVlZUXF6T2ZibE1wRjF0dmNvLW1UY2dEODVjai1Qak4xVnhuUExzIn0.eyJleHAiOjE3NDE3NzkxODYsImlhdCI6MTc0MTc0MzE4NiwianRpIjoiMTY0NGJlOTQtOTk3Ny00OTA4LWFiNWEtMzkwMzVmYmYzZGY0IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9jb3Vyc2UtbWFuYWdlbWVudC1yZWFsbSIsImF1ZCI6InJldmlldy1hcHAiLCJzdWIiOiIzNzY2NmQ1Ni1jYTJkLTRkNDQtOTRmMS1kNDk4ZTdhZmVhOTUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjb3Vyc2UtYXBwIiwic2lkIjoiYzk3NWQ2ZTgtODliOC00ZTE1LTg2N2YtMzdmNTZmYWQ3ZGNlIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiR1VFU1QiLCJBRE1JTiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJldmlldy1hcHAiOnsicm9sZXMiOlsiUkVWSUVXLVJFQUQiLCJSRVZJRVctV1JJVEUiXX0sImNvdXJzZS1hcHAiOnsicm9sZXMiOlsiQ09VUlNFLVdSSVRFIiwiQ09VUlNFLVJFQUQiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiTmFzcnVkZGluIE5hc3J1ZGRpbiIsInByZWZlcnJlZF91c2VybmFtZSI6Im5hc3J1ZGRpbiIsImdpdmVuX25hbWUiOiJOYXNydWRkaW4iLCJmYW1pbHlfbmFtZSI6Ik5hc3J1ZGRpbiIsImVtYWlsIjoibmFzcnVkZGluQGdtYWlsLmNvbSJ9.E9k1reSQHmsidXaVYizlYs4ULMBjHE2hPzyGwDIQtM_ZszgqQzZH6CE5Y9fNdmN-ky4RLfvx3_C5DRQoDAn_PWLvlEFCsAVfWGopETHcd-NVm6rovbjuoBGrGPwZC9T49pzDEXTyudLEONnbHzXybhW88sTHbgj-8huS1tJFhjQ0rtROQHG5tK382z-bRxpo-Akzx9OP3W9YELf8V9TwW2sJ781WwOFeDP_k7EI8VbWdXJhEwKQJbE31roVk9PddK8_VUX4krCBMEUV6zzor9E3r7_OUTQ8-wnHrZrXBU8Sl2yO7SwUNb8l7zAzGf72vYS9cvF7ygIGA4_7EXIbIqw",
>"expires_in": 36000,
>"refresh_expires_in": 1799,
>"refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJhZjM3ZGFiMi1iMTQwLTRlY2EtYTNjNi05YmFkM2JkNzY0ODcifQ.eyJleHAiOjE3NDE3NDQ5ODYsImlhdCI6MTc0MTc0MzE4NiwianRpIjoiMTU0OTRkNDMtMDJlYS00ZGZjLWJiYTQtMDFhNWJkZmRkZjcyIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9jb3Vyc2UtbWFuYWdlbWVudC1yZWFsbSIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MS9yZWFsbXMvY291cnNlLW1hbmFnZW1lbnQtcmVhbG0iLCJzdWIiOiIzNzY2NmQ1Ni1jYTJkLTRkNDQtOTRmMS1kNDk4ZTdhZmVhOTUiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoiY291cnNlLWFwcCIsInNpZCI6ImM5NzVkNmU4LTg5YjgtNGUxNS04NjdmLTM3ZjU2ZmFkN2RjZSIsInNjb3BlIjoib3BlbmlkIHdlYi1vcmlnaW5zIHJvbGVzIGFjciBiYXNpYyBlbWFpbCBwcm9maWxlIn0.WkDLs8k7EEAXjJwMUHK2XurjaXXY-0-y5br1JlNSUIyGNzVBUrfmfN8Te7ysWvSvGlhrO9k6ali6oqGiqxXTBA",
>"token_type": "Bearer",
>"id_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0T1lYQVlZUXF6T2ZibE1wRjF0dmNvLW1UY2dEODVjai1Qak4xVnhuUExzIn0.eyJleHAiOjE3NDE3NzkxODYsImlhdCI6MTc0MTc0MzE4NiwianRpIjoiNWNkODRkM2UtOWY3YS00YmYxLWIzYTAtMjU4MWY4NzJmODAzIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9jb3Vyc2UtbWFuYWdlbWVudC1yZWFsbSIsImF1ZCI6ImNvdXJzZS1hcHAiLCJzdWIiOiIzNzY2NmQ1Ni1jYTJkLTRkNDQtOTRmMS1kNDk4ZTdhZmVhOTUiLCJ0eXAiOiJJRCIsImF6cCI6ImNvdXJzZS1hcHAiLCJzaWQiOiJjOTc1ZDZlOC04OWI4LTRlMTUtODY3Zi0zN2Y1NmZhZDdkY2UiLCJhdF9oYXNoIjoiTE9yZGRCcHlyTzZHTDlmU21tbkVnZyIsImFjciI6IjEiLCJyZXNvdXJjZV9hY2Nlc3MiOnsicmV2aWV3LWFwcCI6eyJyb2xlcyI6WyJSRVZJRVctUkVBRCIsIlJFVklFVy1XUklURSJdfSwiY291cnNlLWFwcCI6eyJyb2xlcyI6WyJDT1VSU0UtV1JJVEUiLCJDT1VSU0UtUkVBRCJdfX0sImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkdVRVNUIiwiQURNSU4iXX0sIm5hbWUiOiJOYXNydWRkaW4gTmFzcnVkZGluIiwicHJlZmVycmVkX3VzZXJuYW1lIjoibmFzcnVkZGluIiwiZ2l2ZW5fbmFtZSI6Ik5hc3J1ZGRpbiIsImZhbWlseV9uYW1lIjoiTmFzcnVkZGluIiwiZW1haWwiOiJuYXNydWRkaW5AZ21haWwuY29tIn0.T2QhItWJXsACMZhsQ1HJcm8GGF60upkS4fq6QyHWsKi7A1wojhOk5qrcD3LF8tiDLIbBr1QHQQOnxY4LHYa0v13bsCS-HJrs90NaT679pHYyGVRsLbpE0U3xwqxtX0o0XrQIpuP8SB_YYcQ2JtxkxaGW3k9X8BwBvgL5GDAK-_C1tO3hBlIxAD6ecEv44G_FD4f6Zrc6h4EXV_TKOnrXfjUxWTUmvIYuAnBfHFqCv3Yx_RONqKoAlwh4J349i3GuYdwbtR97Q_TBDRqGSgfkYY7d-GrGScPzM-ao3lfOGSq8VBqQh4ZXCWix8hAj1IUSiRWF5Tr9m_SIBN8WAyq07A",
>"not-before-policy": 0,
>"session_state": "c975d6e8-89b8-4e15-867f-37f56fad7dce",
>"scope": "openid email profile"
>}
>```
> ![Jwt web page](notes/images/jwt.png)
---
# Observability

- **Prometheus**:
    - Open the browser - http://prometheus.local and make sure all the 4 microservices health status is listed
![Prometheus web page](notes/images/prometheus.png)
> [!TIP]
> Hit - http://127.0.0.1/actuator/prometheus in your browser and check all the metrics at one place
> ![prometheus metrics](notes/images/metrics.png)
- **Fluent-bit**
    - Make sure fluent bit is running and listening on 0.0.0.0:24224
```shell
    kubectl logs fluent-bit
```
```shell
    docker logs fluent-bit
```
```markdown
[2025/03/12 00:40:53] [ info] [input:forward:forward.0] listening on 0.0.0.0:24224
level=info caller=out_grafana_loki.go:64 id=0 [flb-go]="Starting fluent-bit-go-loki" version="(version=, branch=, revision=94e00299ec9b36ad97c147641566b6922268c54e-modified)"
level=info caller=out_grafana_loki.go:66 id=0 [flb-go]="provided parameter" URL=http://loki:3100/loki/api/v1/push
level=info caller=out_grafana_loki.go:67 id=0 [flb-go]="provided parameter" TenantID=
level=info caller=out_grafana_loki.go:68 id=0 [flb-go]="provided parameter" BatchWait=1.000s
level=info caller=out_grafana_loki.go:69 id=0 [flb-go]="provided parameter" BatchSize=1001024
level=info caller=out_grafana_loki.go:70 id=0 [flb-go]="provided parameter" Timeout=10.000s
level=info caller=out_grafana_loki.go:71 id=0 [flb-go]="provided parameter" MinBackoff=0.500s
level=info caller=out_grafana_loki.go:72 id=0 [flb-go]="provided parameter" MaxBackoff=300.000s
level=info caller=out_grafana_loki.go:73 id=0 [flb-go]="provided parameter" MaxRetries=10
level=info caller=out_grafana_loki.go:74 id=0 [flb-go]="provided parameter" Labels="[job=fluent-bit]"
level=info caller=out_grafana_loki.go:75 id=0 [flb-go]="provided parameter" LogLevel=info
level=info caller=out_grafana_loki.go:76 id=0 [flb-go]="provided parameter" AutoKubernetesLabels=false
level=info caller=out_grafana_loki.go:77 id=0 [flb-go]="provided parameter" RemoveKeys="[source container_id]"
level=info caller=out_grafana_loki.go:78 id=0 [flb-go]="provided parameter" LabelKeys=[container_name]
level=info caller=out_grafana_loki.go:79 id=0 [flb-go]="provided parameter" LineFormat=0
level=info caller=out_grafana_loki.go:80 id=0 [flb-go]="provided parameter" DropSingleKey=true
level=info caller=out_grafana_loki.go:81 id=0 [flb-go]="provided parameter" LabelMapPath=map[]
level=info caller=out_grafana_loki.go:82 id=0 [flb-go]="provided parameter" Buffer=false
level=info caller=out_grafana_loki.go:83 id=0 [flb-go]="provided parameter" BufferType=dque
level=info caller=out_grafana_loki.go:84 id=0 [flb-go]="provided parameter" DqueDir=/tmp/flb-storage/loki
level=info caller=out_grafana_loki.go:85 id=0 [flb-go]="provided parameter" DqueSegmentSize=500
level=info caller=out_grafana_loki.go:86 id=0 [flb-go]="provided parameter" DqueSync=false
level=info caller=out_grafana_loki.go:87 id=0 [flb-go]="provided parameter" ca_file=
level=info caller=out_grafana_loki.go:88 id=0 [flb-go]="provided parameter" cert_file=
level=info caller=out_grafana_loki.go:89 id=0 [flb-go]="provided parameter" key_file=
level=info caller=out_grafana_loki.go:90 id=0 [flb-go]="provided parameter" insecure_skip_verify=false
[2025/03/12 00:40:53] [ info] [sp] stream processor started
```

- **Tempo**
    - Also, check tempo logs for it's availability.
```shell
    kubectl logs docker-tempo-1
```
```shell
    docker logs docker-tempo-1
```
```markdown
ts=2025-03-12T00:15:15Z level=info msg="Starting GRPC server" component=tempo endpoint=0.0.0.0:4317
ts=2025-03-12T00:15:15Z level=info msg="Starting HTTP server" component=tempo endpoint=0.0.0.0:4318
level=info ts=2025-03-12T00:15:15.886518261Z caller=tempodb.go:576 msg="polling enabled" interval=5m0s blocklist_concurrency=50
level=info ts=2025-03-12T00:15:15.893078036Z caller=wal.go:109 msg="beginning replay" file=4b37865a-8f01-4d27-ad63-f90c86ee326b+single-tenant+vParquet4 size=4096
level=info ts=2025-03-12T00:15:15.89624768Z caller=worker.go:249 msg="total worker concurrency updated" totalConcurrency=20
level=warn ts=2025-03-12T00:15:15.897771963Z caller=wal.go:115 msg="failed to replay block. removing." file=4b37865a-8f01-4d27-ad63-f90c86ee326b+single-tenant+vParquet4 err="error reading wal meta json: /var/tempo/wal/4b37865a-8f01-4d27-ad63-f90c86ee326b+single-tenant+vParquet4/meta.json: open /var/tempo/wal/4b37865a-8f01-4d27-ad63-f90c86ee326b+single-tenant+vParquet4/meta.json: no such file or directory"
level=info ts=2025-03-12T00:15:15.898190804Z caller=wal.go:109 msg="beginning replay" file=724d6348-1e29-456e-920d-0bfa7b4e8bdd+single-tenant+vParquet4 size=4096
level=info ts=2025-03-12T00:15:15.902527103Z caller=poller.go:287 msg="writing tenant index" tenant=single-tenant metas=3 compactedMetas=2
level=info ts=2025-03-12T00:15:15.917231924Z caller=poller.go:144 msg="blocklist poll complete" seconds=0.023366227
level=info ts=2025-03-12T00:15:15.917417761Z caller=compactor.go:159 msg="enabling compaction"
level=info ts=2025-03-12T00:15:15.919806486Z caller=tempodb.go:540 msg="compaction and retention enabled."
level=info ts=2025-03-12T00:15:15.920241565Z caller=compactor.go:142 msg="starting compaction cycle" tenantID=single-tenant offset=0
level=info ts=2025-03-12T00:15:15.920400513Z caller=compactor.go:155 msg="compaction cycle complete. No more blocks to compact" tenantID=single-tenant
level=info ts=2025-03-12T00:15:16.414958877Z caller=wal.go:136 msg="replay complete" file=724d6348-1e29-456e-920d-0bfa7b4e8bdd+single-tenant+vParquet4 duration=516.810746ms
level=warn ts=2025-03-12T00:15:16.415046915Z caller=wal.go:99 msg="unowned file entry ignored during wal replay" file=blocks err=null
level=info ts=2025-03-12T00:15:16.421910578Z caller=ingester.go:448 msg="wal replay complete"
level=info ts=2025-03-12T00:15:16.422119251Z caller=ingester.go:462 msg="reloading local blocks" tenants=0
level=info ts=2025-03-12T00:15:16.42224564Z caller=lifecycler.go:677 msg="not loading tokens from file, tokens file path is empty"
level=info ts=2025-03-12T00:15:16.42315586Z caller=lifecycler.go:704 msg="instance not found in ring, adding with no tokens" ring=ingester
level=info ts=2025-03-12T00:15:16.423323314Z caller=app.go:205 msg="Tempo started"
```

# Guide to Grafana
### Accessing Loki, Tempo, Prometheus, and Dashboards in Grafana

## Step 1: Log In to Grafana
1. Open your browser and go to your Grafana instance (e.g., `http://localhost:3000`).
2. Log in with your credentials (default: `admin`/`admin`). Update the password if prompted.

---

## Step 2: Add Data Sources (Loki, Tempo, Prometheus)
Configure Loki, Tempo, and Prometheus as data sources in Grafana.

1. **Navigate to Connections**:
    - From the left sidebar, hover over the **Connections** icon (plug symbol) and click **Data sources**.  
      Or use the hamburger menu (☰) > **Connections** > **Data sources**.

2. **Add a New Data Source**:
    - Click **+ Add new data source** in the top-right corner.

3. **Configure Prometheus**:
    - Search for `Prometheus` and select it.
    - Set **Name** (e.g., "Prometheus").
    - Enter **URL** (e.g., `http://prometheus:9090`).
    - Leave defaults unless specific settings (e.g., authentication) are needed.
    - Click **Save & test**. Confirm "Data source is working."

4. **Configure Loki**:
    - Search for `Loki` and select it.
    - Set **Name** (e.g., "Loki").
    - Enter **URL** (e.g., `http://loki:3100`).
    - Optionally, set **Max lines** (e.g., 1000) under **Additional settings**.
    - Click **Save & test**. Verify it works.

5. **Configure Tempo**:
    - Search for `Tempo` and select it.
    - Set **Name** (e.g., "Tempo").
    - Enter **URL** (e.g., `http://tempo:3200`).
    - Optionally, link to Loki for trace-to-log correlation via **Derived Field** (e.g., match `traceID=(\w+)`).
    - Click **Save & test**. Ensure it’s operational.

---

## Step 3: Access via Explore
Use the **Explore** view to query data directly from Loki, Tempo, and Prometheus.

1. **Open Explore**:
    - Click the **Explore** icon (compass) in the left sidebar, or use the hamburger menu (☰) > **Explore**.

2. **Select a Data Source**:
    - Use the dropdown at the top to choose:
        - **Prometheus**: Metrics (e.g., `rate(http_server_requests_seconds_count[5m])`).
        - **Loki**: Logs (e.g., `{job="fluent-bit"} |= "error"`).
        - **Tempo**: Traces (e.g., search by trace ID or service).

3. **Run Queries**:
    - **Prometheus**: Enter a PromQL query and click **Run query**. View as graph or table.
    - **Loki**: Use LogQL or the **Builder** tab to filter logs. Click **Run query** for logs or metrics.
    - **Tempo**: Input a trace ID or use the **Search** tab (filter by service, duration, tags). View trace visualizations.

4. **Switch Views**:
    - Toggle between **Logs**, **Graph**, or **Traces** tabs above the results.

---

## Step 4: Access and Create Dashboards
Visualize data from Loki, Tempo, and Prometheus using dashboards.

1. **View Existing Dashboards**:
    - Click the **Dashboards** icon (four-square grid) in the sidebar.
    - Select **Browse** to see available dashboards (e.g., from `Spring Boot 3.x Statistic`).

2. **Create a New Dashboard**:
    - Go to **Dashboards** > **+ New** > **New dashboard**.
    - Click **+ Add visualization**.
    - Choose a data source (Prometheus, Loki, or Tempo).
    - Add queries:
        - **Prometheus**: Metric query (e.g., `http_server_requests_seconds_count`), visualize as Graph.
        - **Loki**: Log query (e.g., `{job="fluent-bit"}`), use **Logs** or **Time series**.
        - **Tempo**: Grab a Trace ID or search query, visualize as a trace timeline.
    - Customize panels and click **Apply**.

3. **Save the Dashboard**:
    - Click the **Save** icon (floppy disk) in the top-right.
    - Name and save the dashboard.

4. **Import Pre-Built Dashboards**:
    - Go to **Dashboards** > **+ New** > **Import**.
    - Upload JSON files provided in the directory /grafana-dashboard.
    - Map data sources and import.
---

### Spring Boot Observability Dashboard
![Observability Dashboard](/notes/images/observability.png)

### Spring Boot Statistic Dashboard
![Observability Dashboard](/notes/images/statistic.png)

### Tempo Traces
![Tempo trace](/notes/images/tempo1.png)
![Tempo trace](/notes/images/tempo2.png)

### Loki Logs
![Loki Logs](/notes/images/loki.png)

---
# Verify the APIs

## Accessing APIs with Bearer Token Authentication



- `access-token`: `http://localhost:8081/realms/course-management-realm/protocol/openid-connect/token` or `http://keycloak.local/realms/course-management-realm/protocol/openid-connect/token`
- `course-aggregate`: `http://9000/course-aggregate`
- `courses`: `http://9000/courses`
- `reviews`: `http://9000/reviews`

---

## Steps to Access APIs with Authorization

## 1. Fetch Access Token
You need to get an access token from the **Access Point API**.

### Request:

```shell
    curl -X POST http://localhost:8081/realms/course-management-realm/protocol/openid-connect/token  \
     -d "grant_type=password" \
     -d "client_id=course-app"  \
     -d "client_secret=v1sCIPjANbvyJ87RsTkYeI9xHonDqZh7" \
     -d "username=nasruddin"  \
     -d "password=password" \
     -d "scope=openid roles" | jq
```
```shell
    http -f POST http://localhost:8081/realms/course-management-realm/protocol/openid-connect/token \
    grant_type=password \
    client_id=course-app \
    client_secret=v1sCIPjANbvyJ87RsTkYeI9xHonDqZh7 \
    username=nasruddin \
    password=password \
    scope="openid roles" | jq
```
```markdown
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0T1lYQVlZUXF6T2ZibE1wRjF0dmNvLW1UY2dEODVjai1Qak4xVnhuUExzIn0.eyJleHAiOjE3NDE3NzcxNDMsImlhdCI6MTc0MTc0MTE0MywianRpIjoiOTFjMjU1YTEtOGY2Zi00MDk2LTg3M2EtNGU2MWJiNTI3MmZmIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9jb3Vyc2UtbWFuYWdlbWVudC1yZWFsbSIsImF1ZCI6InJldmlldy1hcHAiLCJzdWIiOiIzNzY2NmQ1Ni1jYTJkLTRkNDQtOTRmMS1kNDk4ZTdhZmVhOTUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjb3Vyc2UtYXBwIiwic2lkIjoiNDEwZjE5ZDEtZTcwNi00MzQ0LWJmMGQtYjRhZDVhYzIzMmE1IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiR1VFU1QiLCJBRE1JTiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJldmlldy1hcHAiOnsicm9sZXMiOlsiUkVWSUVXLVJFQUQiLCJSRVZJRVctV1JJVEUiXX0sImNvdXJzZS1hcHAiOnsicm9sZXMiOlsiQ09VUlNFLVdSSVRFIiwiQ09VUlNFLVJFQUQiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiTmFzcnVkZGluIE5hc3J1ZGRpbiIsInByZWZlcnJlZF91c2VybmFtZSI6Im5hc3J1ZGRpbiIsImdpdmVuX25hbWUiOiJOYXNydWRkaW4iLCJmYW1pbHlfbmFtZSI6Ik5hc3J1ZGRpbiIsImVtYWlsIjoibmFzcnVkZGluQGdtYWlsLmNvbSJ9.adXke6N_RB5bFZCet_ZoMo3Q8Xm-LP-iM2ahgSS4zGubHx4738-n0lzyG2Aa4grtLM9OXbLD_wcm7aXruckQOKzb2YbAJSUR1X3Ul63PQLngT3Qh7xkFRxKT0DK8eN9cJQ2iHndiSS17L3NUZ5lqf4NVBqhW0t2YjoZlTwNzDCQsQA1wCWRypW3AcGRuos-KSjTBLGhipJD2wo8REGf0vTnsX1kd-3HYtoRSA-P3p1p6WJmxfOxzwQyjemi-GBsRcS8R86F14MXfBPSuOc1pk2wumOlHxcE_sJLo5rVJM72YL95wSjJmuOrZ8Wx7TSk-InYSCxYOkm6_CajhTOYHaw",
    "expires_in": 36000,
    "refresh_expires_in": 1800,
    "refresh_token": "eyJhbGciOiJIUzUxMiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJhZjM3ZGFiMi1iMTQwLTRlY2EtYTNjNi05YmFkM2JkNzY0ODcifQ.eyJleHAiOjE3NDE3NDI5NDMsImlhdCI6MTc0MTc0MTE0MywianRpIjoiZjZlZmUwNTQtNGJkZC00MDNmLWJkNWYtYzU4MTdkNmMwM2IyIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9jb3Vyc2UtbWFuYWdlbWVudC1yZWFsbSIsImF1ZCI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MS9yZWFsbXMvY291cnNlLW1hbmFnZW1lbnQtcmVhbG0iLCJzdWIiOiIzNzY2NmQ1Ni1jYTJkLTRkNDQtOTRmMS1kNDk4ZTdhZmVhOTUiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoiY291cnNlLWFwcCIsInNpZCI6IjQxMGYxOWQxLWU3MDYtNDM0NC1iZjBkLWI0YWQ1YWMyMzJhNSIsInNjb3BlIjoib3BlbmlkIHdlYi1vcmlnaW5zIHJvbGVzIGFjciBiYXNpYyBlbWFpbCBwcm9maWxlIn0.-aR1XC4o26s0nlORQV-MZas9_hXOqzevmNQjMVzIdyqWdO7r6cJC3O6jphNBXfsW1KRYa0hoojlVXuAOJVYNfg",
    "token_type": "Bearer",
    "id_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0T1lYQVlZUXF6T2ZibE1wRjF0dmNvLW1UY2dEODVjai1Qak4xVnhuUExzIn0.eyJleHAiOjE3NDE3NzcxNDMsImlhdCI6MTc0MTc0MTE0MywianRpIjoiNTA5OGU2NTMtYjgzMC00ZmVhLWE4ZDMtZDBlOWUyNDZhMzc0IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9jb3Vyc2UtbWFuYWdlbWVudC1yZWFsbSIsImF1ZCI6ImNvdXJzZS1hcHAiLCJzdWIiOiIzNzY2NmQ1Ni1jYTJkLTRkNDQtOTRmMS1kNDk4ZTdhZmVhOTUiLCJ0eXAiOiJJRCIsImF6cCI6ImNvdXJzZS1hcHAiLCJzaWQiOiI0MTBmMTlkMS1lNzA2LTQzNDQtYmYwZC1iNGFkNWFjMjMyYTUiLCJhdF9oYXNoIjoiNFNWZjROYk1ldUY1bWdvWGlRd2UzdyIsImFjciI6IjEiLCJyZXNvdXJjZV9hY2Nlc3MiOnsicmV2aWV3LWFwcCI6eyJyb2xlcyI6WyJSRVZJRVctUkVBRCIsIlJFVklFVy1XUklURSJdfSwiY291cnNlLWFwcCI6eyJyb2xlcyI6WyJDT1VSU0UtV1JJVEUiLCJDT1VSU0UtUkVBRCJdfX0sImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkdVRVNUIiwiQURNSU4iXX0sIm5hbWUiOiJOYXNydWRkaW4gTmFzcnVkZGluIiwicHJlZmVycmVkX3VzZXJuYW1lIjoibmFzcnVkZGluIiwiZ2l2ZW5fbmFtZSI6Ik5hc3J1ZGRpbiIsImZhbWlseV9uYW1lIjoiTmFzcnVkZGluIiwiZW1haWwiOiJuYXNydWRkaW5AZ21haWwuY29tIn0.UrhX9BfGUgNFpxRbk85_0GVbkU8PKDC1toWVNJbTT1LlADmsEk9miC8DUR-oSAgmyWsk4qptt_w22ESihmrfK8-lnVT_92g3tocxqNNymJFzbQdBx5U-v7_UOOEUNVkWgUAssMJ20KmhCEJwFAeUdHmv4sMeBYaZgihhTbz766V_S5zNuKRGCwkMt_OIGda7nYJ4aUI8jP5F4jViMpSYjDWEg2rKKRxbwIKVN3THDJt_Z3jfxh_GXq-5pR07S6De1koAEMqiHQSvqxvg2X3hsph7EBx5R8hoTDv-5nhdOZLNGBKCU3q74UmBvhwlEuXmJgRu3iLriJEVrm5qLnh1DQ",
    "not-before-policy": 0,
    "session_state": "410f19d1-e706-4344-bf0d-b4ad5ac232a5",
    "scope": "openid email profile"
}
```

## 2. Use the Access Token in API Requests 
Once you have the access token, include it in the Authorization header as a Bearer token when calling the other APIs. 

> [!NOTE]
> All the below APIs are being accessed via gateway. However, you can also access them using their respective endpoints but make sure you append **api** in front of context. eg. *http://9001/api/courses* or *http://9002/api/reviews*.   

### Access `course-aggregate` API:
```shell
     http :9000/course-aggregate/1/with-details "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0T1lYQVlZUXF6T2ZibE1wRjF0dmNvLW1UY2dEODVjai1Qak4xVnhuUExzIn0.eyJleHAiOjE3NDE3NzcyMDQsImlhdCI6MTc0MTc0MTIwNCwianRpIjoiOGUyMzg3YzEtMWEzOS00Yjg0LTlhMjAtYTg3M2ZlYTIzNDYxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9jb3Vyc2UtbWFuYWdlbWVudC1yZWFsbSIsImF1ZCI6InJldmlldy1hcHAiLCJzdWIiOiIzNzY2NmQ1Ni1jYTJkLTRkNDQtOTRmMS1kNDk4ZTdhZmVhOTUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjb3Vyc2UtYXBwIiwic2lkIjoiOTJiZDBmZTQtNjhlYi00ZDViLTliNmEtMWMyMzMzZmNiOWRkIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiR1VFU1QiLCJBRE1JTiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJldmlldy1hcHAiOnsicm9sZXMiOlsiUkVWSUVXLVJFQUQiLCJSRVZJRVctV1JJVEUiXX0sImNvdXJzZS1hcHAiOnsicm9sZXMiOlsiQ09VUlNFLVdSSVRFIiwiQ09VUlNFLVJFQUQiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiTmFzcnVkZGluIE5hc3J1ZGRpbiIsInByZWZlcnJlZF91c2VybmFtZSI6Im5hc3J1ZGRpbiIsImdpdmVuX25hbWUiOiJOYXNydWRkaW4iLCJmYW1pbHlfbmFtZSI6Ik5hc3J1ZGRpbiIsImVtYWlsIjoibmFzcnVkZGluQGdtYWlsLmNvbSJ9.DsifGnS890ljND6P4ibZnDV47K9snnbmitSYDzejt4OdR5Z4dykew2pYDAv9fOEYMAURVMQRJShs-eahNupiaIiC8QUnNWdL3cFZVX5VONS4DHhu9885yH8t6QB1RDLAjO0saK0S3kqgmR0cZIOh4Ps9kv2W4Zxq8UW25dXiRWvA-m5vozZ10x-9iT6-x_Vxr9do4oMVAT_q_S7qvIEy9EpMFPQa7RyxHToYhzpAJ2BHoJjSFZ26FaqvEtfRUTYUsH0uRgD88hEzIaMGcKLTwMox8MjD0cSe2WQVc2wzTvjJOw2086xLMGFYca8T-Wz-LlgaVYpp5AJ5OIM6gye35Q"
```
```markdown
HTTP/1.1 200 OK
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Length: 2150
Content-Type: application/json
Expires: 0
Pragma: no-cache
Referrer-Policy: no-referrer
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0

{
    "course": {
        "author": "John Doe",
        "id": 1,
        "price": 29.79,
        "publisher": "Whatsapp",
        "title": "Microservices with Quarkus"
    },
    "reviews": [
        {
            "author": "John Doe",
            "content": "Amazing book and loved reading it",
            "courseId": 1,
            "email": "abc@xyz.com",
            "id": "67cfd9f2ab40f0347b1a3f15"
        },
        {
            "author": "John Doe",
            "content": "Amazing book and loved reading it",
            "courseId": 1,
            "email": "abc@xyz.com",
            "id": "67cfd9f2ab40f0347b1a3f14"
        }
    ]
}
```

### Access `courses` API:
```shell
    http POST :9000/courses title="Microservices with Golang" author="John Doe" price:=29.79 publisher="GitHub" "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0T1lYQVlZUXF6T2ZibE1wRjF0dmNvLW1UY2dEODVjai1Qak4xVnhuUExzIn0.eyJleHAiOjE3NDE3NzcyMDQsImlhdCI6MTc0MTc0MTIwNCwianRpIjoiOGUyMzg3YzEtMWEzOS00Yjg0LTlhMjAtYTg3M2ZlYTIzNDYxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9jb3Vyc2UtbWFuYWdlbWVudC1yZWFsbSIsImF1ZCI6InJldmlldy1hcHAiLCJzdWIiOiIzNzY2NmQ1Ni1jYTJkLTRkNDQtOTRmMS1kNDk4ZTdhZmVhOTUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjb3Vyc2UtYXBwIiwic2lkIjoiOTJiZDBmZTQtNjhlYi00ZDViLTliNmEtMWMyMzMzZmNiOWRkIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiR1VFU1QiLCJBRE1JTiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJldmlldy1hcHAiOnsicm9sZXMiOlsiUkVWSUVXLVJFQUQiLCJSRVZJRVctV1JJVEUiXX0sImNvdXJzZS1hcHAiOnsicm9sZXMiOlsiQ09VUlNFLVdSSVRFIiwiQ09VUlNFLVJFQUQiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiTmFzcnVkZGluIE5hc3J1ZGRpbiIsInByZWZlcnJlZF91c2VybmFtZSI6Im5hc3J1ZGRpbiIsImdpdmVuX25hbWUiOiJOYXNydWRkaW4iLCJmYW1pbHlfbmFtZSI6Ik5hc3J1ZGRpbiIsImVtYWlsIjoibmFzcnVkZGluQGdtYWlsLmNvbSJ9.DsifGnS890ljND6P4ibZnDV47K9snnbmitSYDzejt4OdR5Z4dykew2pYDAv9fOEYMAURVMQRJShs-eahNupiaIiC8QUnNWdL3cFZVX5VONS4DHhu9885yH8t6QB1RDLAjO0saK0S3kqgmR0cZIOh4Ps9kv2W4Zxq8UW25dXiRWvA-m5vozZ10x-9iT6-x_Vxr9do4oMVAT_q_S7qvIEy9EpMFPQa7RyxHToYhzpAJ2BHoJjSFZ26FaqvEtfRUTYUsH0uRgD88hEzIaMGcKLTwMox8MjD0cSe2WQVc2wzTvjJOw2086xLMGFYca8T-Wz-LlgaVYpp5AJ5OIM6gye35Q"
```
```markdown
HTTP/1.1 201 Created
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Length: 210
Content-Type: application/json
Expires: 0
Pragma: no-cache
Referrer-Policy: no-referrer
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0

{
    "author": "John Doe",
    "createdDate": "2025-03-12T01:17:35.565586049Z",
    "id": 5,
    "lastModifiedDate": "2025-03-12T01:17:35.565586049Z",
    "price": 29.79,
    "publisher": "GitHub",
    "title": "Microservices with Golang",
    "version": 0
}
```
### Access `reviews` API:
```shell
    http POST :9000/reviews courseId:=1 author="John Doe" content="Amazing book"  email="abc@xyz.com" "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI0T1lYQVlZUXF6T2ZibE1wRjF0dmNvLW1UY2dEODVjai1Qak4xVnhuUExzIn0.eyJleHAiOjE3NDE3NzcyMDQsImlhdCI6MTc0MTc0MTIwNCwianRpIjoiOGUyMzg3YzEtMWEzOS00Yjg0LTlhMjAtYTg3M2ZlYTIzNDYxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgxL3JlYWxtcy9jb3Vyc2UtbWFuYWdlbWVudC1yZWFsbSIsImF1ZCI6InJldmlldy1hcHAiLCJzdWIiOiIzNzY2NmQ1Ni1jYTJkLTRkNDQtOTRmMS1kNDk4ZTdhZmVhOTUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjb3Vyc2UtYXBwIiwic2lkIjoiOTJiZDBmZTQtNjhlYi00ZDViLTliNmEtMWMyMzMzZmNiOWRkIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiR1VFU1QiLCJBRE1JTiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJldmlldy1hcHAiOnsicm9sZXMiOlsiUkVWSUVXLVJFQUQiLCJSRVZJRVctV1JJVEUiXX0sImNvdXJzZS1hcHAiOnsicm9sZXMiOlsiQ09VUlNFLVdSSVRFIiwiQ09VUlNFLVJFQUQiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiTmFzcnVkZGluIE5hc3J1ZGRpbiIsInByZWZlcnJlZF91c2VybmFtZSI6Im5hc3J1ZGRpbiIsImdpdmVuX25hbWUiOiJOYXNydWRkaW4iLCJmYW1pbHlfbmFtZSI6Ik5hc3J1ZGRpbiIsImVtYWlsIjoibmFzcnVkZGluQGdtYWlsLmNvbSJ9.DsifGnS890ljND6P4ibZnDV47K9snnbmitSYDzejt4OdR5Z4dykew2pYDAv9fOEYMAURVMQRJShs-eahNupiaIiC8QUnNWdL3cFZVX5VONS4DHhu9885yH8t6QB1RDLAjO0saK0S3kqgmR0cZIOh4Ps9kv2W4Zxq8UW25dXiRWvA-m5vozZ10x-9iT6-x_Vxr9do4oMVAT_q_S7qvIEy9EpMFPQa7RyxHToYhzpAJ2BHoJjSFZ26FaqvEtfRUTYUsH0uRgD88hEzIaMGcKLTwMox8MjD0cSe2WQVc2wzTvjJOw2086xLMGFYca8T-Wz-LlgaVYpp5AJ5OIM6gye35Q"
```
```markdown
HTTP/1.1 201 Created
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Content-Length: 125
Content-Type: application/json
Expires: 0
Pragma: no-cache
Referrer-Policy: no-referrer
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 0

{
    "author": "John Doe",
    "content": "Amazing book",
    "courseId": 1,
    "email": "abc@xyz.com",
    "id": "67d0e14a2bb2a161bb104cef",
    "version": 1
}

```
---
# Troubleshoot and Tips
