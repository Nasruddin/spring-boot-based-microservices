cd spring-boot-based-microservices
java -jar microservices/product-composite-service/build/libs/product-composite-service-1.0.0-SNAPSHOT.jar &
java -jar microservices/product-service/build/libs/product-service-1.0.0-SNAPSHOT.jar &
java -jar microservices/recommendation-service/build/libs/recommendation-service-1.0.0-SNAPSHOT.jar &
java -jar microservices/review-service/build/libs/review-service-1.0.0-SNAPSHOT.jar &