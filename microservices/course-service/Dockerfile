FROM eclipse-temurin:17.0.5_8-jre-focal as builder
WORKDIR extracted

ADD ./target/*.jar app.jar

RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR application

# Copy the dependencies layer from the builder stage
COPY --from=builder extracted/dependencies/ ./
# Copy the Spring Boot loader layer from the builder stage
COPY --from=builder extracted/spring-boot-loader/ ./
# Copy the snapshot dependencies layer from the builder stage
COPY --from=builder extracted/snapshot-dependencies/ ./
# Copy the application layer from the builder stage
COPY --from=builder extracted/application/ ./

# Expose port 8080
EXPOSE 8080

# Set the entry point to launch the application
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

