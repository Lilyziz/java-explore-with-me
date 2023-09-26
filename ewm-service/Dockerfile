FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar ewm_service.jar
ENTRYPOINT ["java","-jar","/ewm_service.jar"]