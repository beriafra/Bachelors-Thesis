import requests
import time
import threading

def send_requests(port):
    start_time = time.time()
    num_requests = 1000
    url = f'http://localhost:{port}'

    for i in range(num_requests):
        response = requests.get(url)
        print(f"Request {i+1} - Status Code: {response.status_code}")

    end_time = time.time()
    elapsed_time = end_time - start_time
    average_response_time = elapsed_time / num_requests

    print(f"\nTotal time for port {port}: {elapsed_time:.4f} seconds")
    print(f"Average response time for port {port}: {average_response_time:.5f} seconds")

thread1 = threading.Thread(target=send_requests, args=(5566,))
thread2 = threading.Thread(target=send_requests, args=(5567,))

thread1.start()
thread2.start()

thread1.join()
thread2.join()