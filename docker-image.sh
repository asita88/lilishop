cd /opt/server-api/
cd admin
docker build --rm -f ./Dockerfile -t admin:latest .

cd buyer-api
docker build --rm -f ./Dockerfile -t buyer-api:latest .

cd common-api
docker build --rm -f ./Dockerfile -t common-api:latest .

cd consumer
docker build --rm -f ./Dockerfile -t consumer:latest .

cd im-api
docker build --rm -f ./Dockerfile -t im-api:latest .

cd manager-api
docker build --rm -f ./Dockerfile -t manager-api:latest .

cd seller-api
docker build --rm -f ./Dockerfile -t seller-api:latest .

cd xxl-job
docker build --rm -f ./Dockerfile -t xxl-job:latest .

#docker-compose -f docker-compose.yml restart