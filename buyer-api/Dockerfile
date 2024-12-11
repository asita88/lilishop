FROM java:8
MAINTAINER Chopper
# 设置工作目录
WORKDIR /app
ADD buyer-api-4.3.jar /app/buyer-api-4.3.jar
# 创建日志目录
RUN mkdir /var/log/app

EXPOSE 8888
# RUN "java -jar /app/buyer-api-4.3.jar"
# 设置容器的默认命令
CMD ["java", "-jar", "buyer-api-4.3.jar"]