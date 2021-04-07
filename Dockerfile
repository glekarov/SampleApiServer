FROM adoptopenjdk/openjdk8:latest
RUN apt update && apt -y upgrade && apt install -y mime-support file
RUN mkdir -p /spring
WORKDIR /spring
ARG JAR_FILE=build/output/
COPY ${JAR_FILE} ./
ENTRYPOINT ["java","-cp","lib/*","-jar","service-url.jar"]