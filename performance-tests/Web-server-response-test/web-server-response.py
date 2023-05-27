import requests
import time
num_requests = 1000  # Num of requests
url = 'http://localhost:5566'  # App address

start_time = time.time()

for i in range(num_requests):
    response = requests.get(url)
    print(f"Request {i+1} - Status Code: {response.status_code}")

end_time = time.time()

# Show results
elapsed_time = end_time - start_time
average_response_time = elapsed_time / num_requests

print(f"\nTotal time: {elapsed_time:.4f} seconds")
print(f"Average response time: {average_response_time:.5f} seconds")
