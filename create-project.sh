mkdir microservices
cd microservices

spring init \
--boot-version=3.4.3 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=product-service \
--package-name=com.example.microservices.core.product \
--groupId=com.example.microservices.core.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-service

spring init \
--boot-version=3.4.3 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=review-service \
--package-name=com.example.microservices.core.review \
--groupId=com.example.microservices.core.review \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
review-service

spring init \
--boot-version=3.4.3 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=recommendation-service \
--package-name=com.example.microservices.core.recommendation \
--groupId=com.example.microservices.core.recommendation \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
recommendation-service

spring init \
--boot-version=3.4.3 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=product-composite-service \
--package-name=com.example.microservices.composite.product \
--groupId=com.example.microservices.composite.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-composite-service

cd ..