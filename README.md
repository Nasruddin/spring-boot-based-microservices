# spring-boot-based-microservices

Basic skeleton for Spring Boot Microservices. It includes spring security for basic Auth. Spring cloud gateway is also implemented as an Edge Service. Lots of the spring cloud component integrated.

# How to run

- Navigate to root of the project
```
    cd spring-boot-based-microservices
```
- Build the project
```
    mvn clean package -DskipTests 
    
    ![Maven Build](https://github.com/Nasruddin/spring-boot-based-microservices/blob/master/images/build.png?raw=true)

    
```
- Locate the docker directory from the root directory and run docker compose to startup the containers
```
    cd docker && docker-compose up --build
```
- Let's check if all the services are up and running. We will reach to eureka server using gateway. 
Open the browser and hit http://localhost:8443/eureka/web You will need to authenticate yourself before accessing the endpoint.
```
username : user
password : password
```
- Now, we have a look at our gateway endpoints configurations as well. Hit http://localhost:8443/actuator/gateway/routes in the browser again and 
you should be able to find all the routes configured.

- Coming to swagger/openapi specs, here is the address to access them - http://localhost:8443/openapi/swagger-ui.html

- Please use the below curl to generate the access token with both read and write scope. 
```
curl -k http://writer:secret-writer@localhost:8443/oauth2/token -d grant_type=client_credentials -d scope="course:read course:write" 
```

## Note : Currently this project uses Spring Authorization server. Keep in mind, I'm planning to enhance the whole OAuth flow because it's in shaky state right now and, you may some face issues. We may also move to keycloak.