# Build the Docker image
docker build -t my-web-server .

# Run the Docker container
docker run -d -p 5566:5566 my-web-server
