FROM azul/zulu-openjdk:21-jdk-crac AS gradle-cache

WORKDIR /work

COPY gradle/ ./gradle/
COPY *.gradle *.gradle.kts gradlew ./
#RUN apt-get install -y findutils
RUN ./gradlew --console=plain --no-daemon -q dependencies

FROM gradle-cache AS builder

COPY spring-boot ./spring-boot
COPY json-validation ./json-validation
COPY postgresql-mybatis-adapter ./postgresql-mybatis-adapter
COPY network ./network
COPY core ./core
COPY json-validation-jackson-adapter ./json-validation-jackson-adapter
COPY retry ./retry

RUN ./gradlew --console=plain --no-daemon -q clean build -x check

FROM azul/zulu-openjdk:21-jdk-crac AS checkpoint

WORKDIR /root

COPY --from=builder /work/spring-boot/build/libs/spring-boot-0.0.1-SNAPSHOT.jar /root/app.jar

# RUN ls -ltar $JAVA_HOME/lib/criu
# RUN apt-get update && apt-get install -y sudo
# RUN sudo chown root:root $JAVA_HOME/lib/criu
# RUN sudo chmod u+s $JAVA_HOME/lib/criu
RUN java -Dspring.context.checkpoint=onRefresh -XX:CRaCEngine=warp -XX:CRaCCheckpointTo=./my_checkpoint -jar /root/app.jar | cat

FROM azul/zulu-openjdk:21-jre-crac AS runtime

WORKDIR /app
COPY --from=checkpoint /root/my_checkpoint /app/my_checkpoint
COPY --from=builder /work/spring-boot/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENV PORT 8080

CMD ["java", "-XX:CRaCRestoreFrom=./my_checkpoint"]
