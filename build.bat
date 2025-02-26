@echo off
setlocal

call mvn -B package --file pom.xml

REM 创建 dist 目录及其子目录
mkdir dist\admin
mkdir dist\buyer-api
mkdir dist\common-api
mkdir dist\consumer
mkdir dist\im-api
mkdir dist\manager-api
mkdir dist\seller-api
mkdir dist\xxl-job

REM 复制文件到相应的目录
copy admin\target\admin-4.3.jar dist\admin\
copy admin\Dockerfile dist\admin\

copy buyer-api\target\buyer-api-4.3.jar dist\buyer-api\
copy buyer-api\Dockerfile dist\buyer-api\

copy common-api\target\common-api-4.3.jar dist\common-api\
copy common-api\Dockerfile dist\common-api\

copy consumer\target\consumer-4.3.jar dist\consumer\
copy consumer\Dockerfile dist\consumer\

copy im-api\target\im-api-4.3.jar dist\im-api\
copy im-api\Dockerfile dist\im-api\

copy manager-api\target\manager-api-4.3.jar dist\manager-api\
copy manager-api\Dockerfile dist\manager-api\

copy seller-api\target\seller-api-4.3.jar dist\seller-api\
copy seller-api\Dockerfile dist\seller-api\

copy xxl-job\xxl-job-admin-2.3.0-SNAPSHOT.jar dist\xxl-job\xxl-job-admin-2.3.0-SNAPSHOT.jar
copy xxl-job\Dockerfile dist\xxl-job\
copy xxl-job\application.properties dist\xxl-job\

copy docker-image.sh dist\
copy docker-compose.yml dist\


cd dist

cd admin
docker build --rm -f ./Dockerfile -t admin:latest .
cd ..

cd buyer-api
docker build --rm -f ./Dockerfile -t buyer-api:latest .
cd ..

cd common-api
docker build --rm -f ./Dockerfile -t common-api:latest .
cd ..

cd consumer
docker build --rm -f ./Dockerfile -t consumer:latest .

cd im-api
docker build --rm -f ./Dockerfile -t im-api:latest .
cd ..

cd manager-api
docker build --rm -f ./Dockerfile -t manager-api:latest .
cd ..

cd seller-api
docker build --rm -f ./Dockerfile -t seller-api:latest .
cd ..

cd xxl-job
docker build --rm -f ./Dockerfile -t xxl-job:latest .
cd ..

endlocal