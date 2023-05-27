docker build -t ml-predict-app -f docker-ml-predict-app.yml .
docker run -it -d -p 1000:1000 ml-predict-app