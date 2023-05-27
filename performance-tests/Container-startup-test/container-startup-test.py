import requests
import time
import subprocess
import os

# Performance test scenario
url = 'http://localhost:5566'  # App address
command = "docker run -it -d -p 5566:5566 my-testing"

def start_docker_and_measure_startup_time():
    # Start the timer
    start_time = time.time()
    # Start the Docker container
    subprocess.run(command.split(), check=True)
    # Wait for the container to start
    time.sleep(1)  # Wait a bit for the container to start
    while True:
        try:
            response = requests.get(url)
            if response.status_code == 200:
                # Stop the timer
                end_time = time.time()
                # Calculate and return startup time
                startup_time = end_time - start_time
                return startup_time
        except requests.exceptions.ConnectionError:
            # If the connection fails, wait a bit before trying again
            time.sleep(0.1)

startup_time = start_docker_and_measure_startup_time()
print(f"Container startup time: {startup_time:.5f} seconds")
