import base64
import io
import requests
import dash
from flask import jsonify
from dash.dependencies import Input, Output
from dash import html, dcc
import pandas as pd

app = dash.Dash(__name__)

app.layout = html.Div([
    dcc.Upload(
        id='upload-data',
        children=html.Div([
            'Drag and Drop or ',
            html.A('Select Files')
        ]),
        style={
            'width': '100%',
            'height': '60px',
            'lineHeight': '60px',
            'borderWidth': '1px',
            'borderStyle': 'dashed',
            'borderRadius': '5px',
            'textAlign': 'center',
            'margin': '10px'
        },
        multiple=False,
        accept='text/csv'
    ),
    dcc.Input(id='api_server_ip', type='text', placeholder='Api server ip adresi'),
    html.Div(id='output-data-upload'),
])

#
@app.callback(Output('output-data-upload', 'children'),
              [Input('upload-data', 'contents'),
               Input('upload-data', 'filename'),
               Input('api_server_ip', 'value')
               ])
#
def parse_contents(contents, filename, api_server_ip):
    print(contents,filename)
    if contents == None and filename == None:
        return ''

    print(filename)
    content_type, content_string = contents.split(',')
    decoded = base64.b64decode(content_string)
    try:
        if 'csv' in filename:
            # Assume that the user uploaded a CSV file
            df = pd.read_csv(
                io.StringIO(decoded.decode('utf-8')))
        elif 'xls' in filename:
            # Assume that the user uploaded an excel file
            df = pd.read_excel(io.BytesIO(decoded))
    except Exception as e:
        print(e)
        return html.Div([
            'There was an error processing this file.'
        ])
    
    # posting csv file to get response from the prediction API
    csv_file = df.to_csv(index=False)
    url = f'http://{api_server_ip}:5555/dash2predict'
    print(url)

    response = requests.post(url, files={"file":csv_file})
    if response.status_code == 200:
        print("file uploaded successfully")
    else:
        print("file upload failed")
    return f"results are: {response.text}"
    #return html.Div(response.text)
    
if __name__ == "__main__":
    app.run(port=1000, host='0.0.0.0')