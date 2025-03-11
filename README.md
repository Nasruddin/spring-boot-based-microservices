# Table of Contents
- [Background](#background)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Run the application](#run-the-application)
- [Test the endpoints](#test-the-endpoints)
- [Keycloak](#keycloak)
- [Observability](#Observability)


# Background
A foundational framework for building **Spring Boot**-based microservices, designed for a **modular**, **scalable**, and **observable** system to manage **courses** and **reviews**. It incorporates **Spring Security** with **OAuth 2.0** via **Keycloak** for **authentication** and **Spring Cloud Gateway** as the **API gateway**. The architecture integrates a modern **observability stack**, including **OpenTelemetry (OTel)**, **Grafana**, **Loki**, **Tempo**, and **Prometheus**. **MongoDB** and **PostgreSQL** serve as **persistent storage** solutions. Deployment is supported through **Docker Compose** for **local environments** and **Kubernetes** for **scalable deployments**. The system utilizes **Spring Boot** and **Spring Cloud** to enable seamless **microservices communication**, **security**, and **observability**.

# Architecture
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

# Test the endpoints
> [!NOTE]
```shell
  minikube tunnel --profile microservice-deployment
```


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

Also, please use **OpenAPI specs**, **bruno** or **postman** for API details. I will add Swagger/SpringDoc as when I get time!!
# Keycloak
### Head to Keycloak using above table ```http://localhost:8081/admin``` or ```http://keycloak.local/admin```
Create realm using `course-management-realm-realm.json` provided in the repo
- Login to Keycloak → Go to http://localhost:8080/admin admin/admin
- Create Realm → Click "Create Realm" in the top-left dropdown
- Import Realm → Click "Import", select realm.json, and click "Create"
- Verify → Check Clients, Users, Roles, and Mappers in the new realm

![Keycloak web page](notes/images/keycloak.png)

# Observability
In progress

