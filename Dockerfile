FROM amazoncorretto:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} traffic-location-system-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/traffic-location-system-0.0.1-SNAPSHOT.jar"]