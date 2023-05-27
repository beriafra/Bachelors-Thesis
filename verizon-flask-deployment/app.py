import numpy as np
from flask import Flask, request, jsonify, render_template
import requests
import pandas as pd
import pickle
from model import data, data_raw
import socket

server_ip = socket.gethostbyname(socket.gethostname())

app = Flask(__name__)
model = pickle.load(open('model.pkl', 'rb'))

@app.route('/')
def home():
    return render_template('index.html', server_ip=server_ip)

@app.route('/predict',methods=['POST'])
def predict():
    '''
    For rendering results on HTML GUI
    '''
    requested_list = [item for item in request.form]
    requested = pd.Series(request.form.values())
    print(requested)
    print(type(requested))
    #print(requested_list)

    df_blank = pd.DataFrame(columns=['mon', 'state', 'originalagency', 'speed', 'wirecenter', 'InstallType', 'competitorname', 'ContractType', 'salesrepid', 'order_create_date', 'canceldate', 'endclientsystem', 'hfws_ind', 'due_date', 'FIRSTOFFEREDDATE', 'waitingdayofcustomers', 'waitingdayforcompany', 'BUNDLE', 'BundleType', 'ONTRequired', 'ONTInstall', 'strOrderType', 'strIsWinbackIndicator', 'notruckrollrequiredreason', 'droptype', 'premisetype', 'ONTselfinstallcapable', 'saleschannel', 'bundlename', 'smartcart'])
    df_blank.loc[0] = requested.values
    df_blank = df_blank.drop(columns=['canceldate', 'hfws_ind', 'notruckrollrequiredreason'])
    #transpose = pd.DataFrame(df_blank).T
    #transpose
    print(df_blank)
    print(type(data))

    data_raw_dropped = data_raw.drop(columns = ['canceldate', 'hfws_ind', 'notruckrollrequiredreason', 'cancel_ind'])

    def zip_cols(data_dict, data):
        df_result = pd.DataFrame()
        for col in data_dict.columns:
            df_result[col] = list(zip(data_dict[col], data[col]))
        return df_result
    print(data_raw_dropped.head(5))

    df_result = zip_cols(data, data_raw_dropped)
    print(df_result.head())

    df_feed = df_blank.copy()
    print(df_feed)
    
    # last method to feed
    df_feedTwo = data.iloc[0,:].copy()
    transpose = pd.DataFrame(df_feedTwo).T
    df_feedTwo = transpose

    for col in range(0,len(data.columns)):
        for row in range(0,len(data)):
            if df_blank.iloc[0,col] in df_result.iloc[row,col]:
                df_feedTwo.iloc[0,col] = df_result.iloc[row,col][0]
                print(f"formatted df_feedTwo: {df_feedTwo.iloc[0,col]}")
                break
          
    print(df_feed)
    print(df_feed.dtypes)

    # last method to feed
    print(df_feedTwo.dtypes)
    print(df_feedTwo)
    print(df_blank)
    print(df_feedTwo.iloc[0,2])
    #final_features = [np.array(features)]
    prediction = model.predict(df_feedTwo)


    if prediction == 0:
        return render_template('index.html', prediction_text='Result = {}, Customer will cancel the subscription by 96 percent chance'.format(prediction))
    elif prediction == 1:
        return render_template('index.html', prediction_text='Result = {}, Customer will hold the subscription by 96 percent chance'.format(prediction))
    

@app.route('/predict_api',methods=['POST'])
def predict_api():
    '''
    For direct API calls trought request
    '''
    data = request.get_json(force=True)
    prediction = model.predict([np.array(list(data.values()))])

    #output = prediction[0]
    #return jsonify(output)

@app.route('/api_predict',methods=['POST'])
def api_predict(df):
    # this just gets df because it is handled in get_api_call.csv
    
    '''
    For direct API calls trought request
    '''

    print(df.head(10))

    # detecting features which contains more than 50% null of total samples
    nully_features = [features for features in df.columns if df[features].isnull().sum() > len(df[features])/2]
    print(nully_features)

    # dropping nully features
    df = df.drop([feature for feature in nully_features if feature != 'cancel_ind'], axis = 1)
    
    # replacing cancel_ind
    df = df.drop(columns = ["cancel_ind"])

    # replacing other values
    df.waitingdayofcustomers.replace('#VALUE!', np.nan ,inplace=True)
    df.waitingdayforcompany.replace('#VALUE!', np.nan ,inplace=True)

    data_raw_dropped = data_raw.drop(columns = ['canceldate', 'hfws_ind', 'notruckrollrequiredreason', 'cancel_ind'])

    # zip raw and encoded dfs into one df
    def zip_cols(data_dict, data):
        df_result = pd.DataFrame()
        for col in data_dict.columns:
            df_result[col] = list(zip(data_dict[col], data[col]))
        return df_result
    print(data_raw_dropped.head(5))

    df_result = zip_cols(data, data_raw_dropped)

    # dont forget to check this! deleting just to test, because of int conversion problem
    df = df.drop(columns=['order_create_date','due_date','FIRSTOFFEREDDATE'])

    # change the given df values to corresponding encoded values
    for col in range(0,len(data.columns)):
        for row in range(0,len(data)):
            if row == len(df):
                break
            if df.iloc[row,col] in df_result.iloc[row,col]:
                df.iloc[row,col] = df_result.iloc[row,col][0]
                #print(f"formatted df_feedTwo: {df_feedTwo.iloc[0,col]}")
    
    #df.iloc[row,9] = df_result.iloc[row,9][0]

    print(len(df))
    print(len(df_result))
    #print(df_result.iloc[0,9][0])
    print(df.head())
    print(df.iloc[0,:])
    df = df.apply(lambda row: row.astype(int))

    prediction = model.predict(df)
  
    return prediction


    #output = prediction[0]
    #return jsonify(output)

@app.route('/show_table', methods=['POST'])
def show_table():
    df = pd.read_csv('cancel2.csv')
    df = df.iloc[:20,:-1]
    table = df.to_html()
    return render_template('index.html', table=table)

@app.route('/upload', methods=['POST'])
def upload_predict():
    data_csv = request.files['file']
    df = pd.read_csv(data_csv)

    print(df.head(10))

    # detecting features which contains more than 50% null of total samples
    nully_features = [features for features in df.columns if df[features].isnull().sum() > len(df[features])/2]
    print(nully_features)

    # dropping nully features
    df = df.drop([feature for feature in nully_features if feature != 'cancel_ind'], axis = 1)
    
    # replacing cancel_ind
    df = df.drop(columns = ["cancel_ind"])

    # replacing other values
    df.waitingdayofcustomers.replace('#VALUE!', np.nan ,inplace=True)
    df.waitingdayforcompany.replace('#VALUE!', np.nan ,inplace=True)

    data_raw_dropped = data_raw.drop(columns = ['canceldate', 'hfws_ind', 'notruckrollrequiredreason', 'cancel_ind'])

    # zip raw and encoded dfs into one df
    def zip_cols(data_dict, data):
        df_result = pd.DataFrame()
        for col in data_dict.columns:
            df_result[col] = list(zip(data_dict[col], data[col]))
        return df_result
    print(data_raw_dropped.head(5))

    df_result = zip_cols(data, data_raw_dropped)

    # dont forget to check this! deleting just to test, because of int conversion problem
    df = df.drop(columns=['order_create_date','due_date','FIRSTOFFEREDDATE'])

    # change the given df values to corresponding encoded values
    for col in range(0,len(data.columns)):
        for row in range(0,len(data)):
            if row == len(df):
                break
            if df.iloc[row,col] in df_result.iloc[row,col]:
                df.iloc[row,col] = df_result.iloc[row,col][0]
                #print(f"formatted df_feedTwo: {df_feedTwo.iloc[0,col]}")
    
    #df.iloc[row,9] = df_result.iloc[row,9][0]

    print(len(df))
    print(len(df_result))
    #print(df_result.iloc[0,9][0])
    print(df.head())
    print(df.iloc[0,:])
    df = df.apply(lambda row: row.astype(int))

    prediction = model.predict(df)
  
    return render_template('index.html', upload_prediction_text='Result = {}'.format(prediction))

from flask import Flask, request, render_template,jsonify

@app.route('/get_model_V1', methods=['GET'])
def my_form_get():
    params = request.args.get('params')
    headers = request.args.get('headers')
    print('params:',params)
    print(headers)
    result = {
        "output": 2
    }
    result = {str(key): value for key, value in result.items()}
    return jsonify(result=result)

@app.route('/dash2predict', methods=['GET', 'POST'])
def dash2predict():
    # had to save the file in before processing it
    from werkzeug.datastructures import FileStorage
    
    file = request.files["file"]
    file = FileStorage(stream=file)
    file.save("file")

    df = pd.read_csv("file")
    print(df.head())
    prediction = api_predict(df)

    # had to convert to str in order to return the response
    return str(prediction)

@app.route('/test', methods=['GET', 'POST'])
def test_api():
    data = request.get_json()
    #text = data.decode('utf-8')
    print(type(data))
    print(data.values())
    return str(data)

if __name__ == "__main__":
    app.run(port=5000, host='0.0.0.0')