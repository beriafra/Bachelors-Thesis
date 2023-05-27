docker build -t my-cpu-load-app .
docker run -d -p 5566:5566 my-cpu-load-app


# Run the below code for testing on 2 containers:
# docker run -d -p 5566:5566 my-cpu-load-app
# docker run -d -p 5567:5566 my-cpu-load-app
