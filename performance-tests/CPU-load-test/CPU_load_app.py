from flask import Flask, request
import time

app = Flask(__name__)

def fibonacci(n):
    if n == 0:
        return 0
    elif n == 1:
        return 1
    else:
        return fibonacci(n-1) + fibonacci(n-2)

@app.route('/')
def hello_world():
    n = request.args.get('n', default = 30, type = int)
    start_time = time.time()
    fib_n = fibonacci(n)
    end_time = time.time()
    return f'Fibonacci({n}) = {fib_n}. Calculated in {end_time - start_time:.2f} seconds.'

if __name__ == '__main__':
    app.run(port=5566,host='0.0.0.0')
