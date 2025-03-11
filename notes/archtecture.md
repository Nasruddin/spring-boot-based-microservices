# Course Management System


This section describes the C4 model for the Course Management System, a Spring Boot microservices application with Course, Review, and CourseComposite services, deployed using Docker Compose and Kubernetes.

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