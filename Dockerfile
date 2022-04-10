FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
#FROM openjdk:11
#WORKDIR /finplay
#COPY . .
#RUN ./gradlew clean build
#CMD ./gradlew bootRun