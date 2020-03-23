# Linear Regression

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from sklearn import preprocessing  as pp
from sklearn.linear_model import LogisticRegression
dataset = pd.read_csv('C:/Users/MLogix/Downloads/BLE_RSSI_dataset/BLE_RSSI_dataset/iBeacon_RSSI_Labeled1.csv')

# drop the missing values
dataset.dropna(inplace=True)

y = dataset.iloc[1:1000, [0]].values
X = dataset.iloc[1:1000, [1,2,3,4,5,6,7,8,9,10,11,12,13]].values


from sklearn.preprocessing import LabelEncoder, OneHotEncoder



# encoding categorical values is to use a technique called label encoding.
# Label encoding is simply converting each value in a column to a number



# Splitting the dataset into the Training set and Test set
from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.25, random_state = 1)
# Feature Scaling
from sklearn.datasets import make_classification
import seaborn as sns
sns.set()
from sklearn.model_selection import train_test_split
import pandas as pd
lr = LogisticRegression()
lr.fit(X_train, y_train)
print(lr.coef_)
print(lr.intercept_)
y_pred = lr.predict(X_test)
from sklearn.metrics import confusion_matrix
confusion_matrix(y_test, y_pred)
lr.predict_proba(X_test)
# Making the Confusion Matrix
#from sklearn.metrics import confusion_matrix
#cm = confusion_matrix(y_test, y_pred)
#cm
#plt.matshow(cm)
#cm

import pickle 

#from sklearn.externals import joblib
# Save the trained model as a pickle string. 
# save the model to disk
pickle.dump(lr, open('C:/Users/MLogix/Downloads/BLE_RSSI_dataset/BLE_RSSI_dataset/iBeacon_RSSI_Labeled1lr.model', 'wb'))
# Load the pickled model 
model = pickle.load(open('C:/Users/MLogix/Downloads/BLE_RSSI_dataset/BLE_RSSI_dataset/iBeacon_RSSI_Labeled1lr.model', 'rb'))
result_val = model.score(X_train, y_train)
result_test = model.score(X_test, y_test)


