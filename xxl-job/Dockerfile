FROM java:8
MAINTAINER Chopper
# 设置工作目录
WORKDIR /app
ADD xxl-job-admin-2.3.0-SNAPSHOT.jar /app/xxl-job.jar
ADD application.properties /app/application.properties
EXPOSE 9001

# RUN "java -jar /xxl-job.jar --spring.config.location=/app/application.properties"
# 设置容器的默认命令
CMD ["java", "-jar", "-Dspring.config.location=application.properties", "xxl-job.jar"]