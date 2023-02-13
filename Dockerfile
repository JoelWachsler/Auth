FROM adoptopenjdk:11-jdk

WORKDIR /server
RUN mkdir -p /server
COPY auth.jar .

ENV JAVA_OPTS="-Xmx1024m -Xms512m"

CMD ["java", "-jar", "auth.jar", "$JAVA_OPTS"]
