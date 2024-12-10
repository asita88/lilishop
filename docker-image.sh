
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

#docker-compose -f docker-compose.yml restart