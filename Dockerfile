FROM openjdk:17-jdk

WORKDIR /app

COPY target/*.jar /app/authentication.jar

EXPOSE 8080

CMD ["java","-jar","authentication.jar"]