import requests
import time
import threading

def send_request(port):
    url = f'http://localhost:{port}?n=30'

    start_time = time.time()
    response = requests.get(url)
    end_time = time.time()

    print(f"Response from port {port}: {response.text}")
    print(f"Time taken: {end_time - start_time:.4f} seconds")

send_request(5566)
