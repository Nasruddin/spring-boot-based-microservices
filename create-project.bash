#!/usr/bin/env bash

mkdir spring-boot-based-microservices
cd spring-boot-based-microservices 

spring init \
--boot-version=2.5.2 \
--build=maven \
--java-version=17 \
--packaging=jar \
--name=student-service \
--package-name=io.javatab.microservices.core.student \
--groupId=io.javatab.microservices.core.student \
--dependencies=actuator,webflux \
--version=1.0.0 \
student-service

spring init \
--boot-version=2.5.2 \
--build=maven \
--java-version=17 \
--packaging=jar \
--name=course-service \
--package-name=io.javatab.microservices.core.course \
--groupId=io.javatab.microservices.core.course \
--dependencies=actuator,webflux \
--version=1.0.0 \
course-service

spring init \
--boot-version=2.5.2 \
--build=maven \
--java-version=17 \
--packaging=jar \
--name=vote-service \
--package-name=io.javatab.microservices.core.vote \
--groupId=io.javatab.microservices.core.vote \
--dependencies=actuator,webflux \
--version=1.0.0 \
vote-service

spring init \
--boot-version=2.5.2 \
--build=maven \
--java-version=17 \
--packaging=jar \
--name=search-service \
--package-name=io.javatab.microservices.core.search \
--groupId=io.javatab.microservices.core.search \
--dependencies=actuator,webflux \
--version=1.0.0 \
search-service

spring init \
--boot-version=2.5.2 \
--build=maven \
--java-version=17 \
--packaging=jar \
--name=course-composite-service \
--package-name=io.javatab.microservices.composite.course \
--groupId=io.javatab.microservices.composite.course \
--dependencies=actuator,webflux \
--version=1.0.0 \
course-composite-service

cd ..
