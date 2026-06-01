FROM amazoncorretto:21 as builder

ARG SRC
ARG VERSION=1

RUN yum install -y tar gzip \
    && yum clean all

COPY $SRC /opt/app
WORKDIR /opt/app

RUN /opt/app/mvnw \
    -Dapp.version=${VERSION} \
    -Dmaven.test.skip=true \
    clean package


FROM amazoncorretto:21 as runner

ARG VERSION=1

COPY --from=builder /opt/app/api/target/api-${VERSION}.jar /opt/app.jar

ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
