FROM amazoncorretto:21 as builder

ARG SRC
ARG VERSION=1

COPY $SRC /opt/app
WORKDIR /opt/app

RUN ./mvnw -Dapp.version="${VERSION}" clean package


FROM amazoncorretto:21 as runner

COPY --from=builder /opt/app/api/target/api-${VERSION}.jar /opt/app.jar

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
