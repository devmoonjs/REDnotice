FROM openjdk:17-jdk-alpine
WORKDIR /app

# Actions에서 빌드된 JAR 파일을 Docker 이미지로 복사
COPY build/libs/*.jar REDnotice.jar

# 컨테이너 시작 시 실행할 명령어
ENTRYPOINT ["java", "-jar", "REDnotice.jar"]
