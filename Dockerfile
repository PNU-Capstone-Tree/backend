FROM gradle:8.5.0-jdk17

WORKDIR /app

COPY gradle/ gradle/
COPY src/ src/
COPY build.gradle .
COPY gradlew .
COPY gradlew.bat .
COPY settings.gradle .

ENV LANG=ko_KR.UTF-8 \
    LANGUAGE=ko_KR.UTF-8 \
    LC_ALL=ko_KR.UTF-8

RUN ./gradlew build -x test

CMD ["java", "-jar", "build/libs/tree-0.0.1-SNAPSHOT.jar"]
