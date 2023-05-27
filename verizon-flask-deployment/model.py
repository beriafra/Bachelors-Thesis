# Importing the libraries
import pandas as pd
import numpy as np
from IPython.display import display
import pickle

data_raw = pd.read_csv('cancel2.csv')
data = pd.read_csv('cancel2.csv')

# dont forget to check this! deleting just to test
data = data.drop(columns = ['order_create_date','due_date','FIRSTOFFEREDDATE'])

# dont forget to check this! deleting just to test
data_raw = data_raw.drop(columns = ['order_create_date','due_date','FIRSTOFFEREDDATE'])

# detecting features which contains more than 50% null of total samples
nully_features = [features for features in data.columns if data[features].isnull().sum() > len(data[features])/2]
print(nully_features)
# dropping nully features
data = data.drop([feature for feature in nully_features if feature != 'cancel_ind'], axis = 1)

# replacing nan values with 0 for the target feature for convenience
data.cancel_ind.replace(np.nan, 0, inplace=True)

# replacing other values
data.waitingdayofcustomers.replace('#VALUE!', np.nan ,inplace=True)
data.waitingdayforcompany.replace('#VALUE!', np.nan ,inplace=True)

# label encoding
from sklearn.preprocessing import LabelEncoder

y=data.cancel_ind
le = LabelEncoder()
y=le.fit_transform(y)

for x in data.columns:
    le.fit(data[x])
    data[x]=le.transform(data[x])

y = data['cancel_ind'].astype(int)
X = data.drop('cancel_ind', axis=1)

from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X,y,test_size=0.2,stratify=y)

from xgboost import XGBClassifier
from sklearn.metrics import accuracy_score

# hyper tuned classifier
classifier = XGBClassifier(colsample_bytree= 0.5, learning_rate= 0.13, max_depth= 10, min_child_weight= 3.0, reg_alpha= 0.5, subsample= 1.0)

# X and y are input and target arrays of numeric variables
classifier.fit(X,y)

predictions = classifier.predict(X_test)
print(accuracy_score(y_test, predictions))

# Saving model to disk
pickle.dump(classifier, open('model.pkl','wb'))

# Loading model to compare the results
model = pickle.load(open('model.pkl','rb'))

test_sample_raw = data_raw.iloc[0,:-1]
test_sample = data.iloc[0:3,:-1]

print(model.predict(test_sample))

# to feed the app.py
data = data.iloc[:,:-1]
