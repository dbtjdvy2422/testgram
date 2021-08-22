FROM openjdk:8-jdk-alpine

RUN mkdir upload-file
RUN mkdir jks-file
COPY ./target/*.war /app.war

CMD ["java","-jar","/app.war"]