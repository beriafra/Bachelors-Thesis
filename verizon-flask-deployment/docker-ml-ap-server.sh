docker network create ml-network
docker build -t ml-api-server -f docker-ml-api-server.yml .
docker run -it -d -p 5555:5000 --network ml-network ml-api-server