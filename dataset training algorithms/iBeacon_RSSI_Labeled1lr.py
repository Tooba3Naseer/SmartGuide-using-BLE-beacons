# -*- coding: utf-8 -*-

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from sklearn import preprocessing  as pp
dataset = pd.read_csv('C:/Users/MLogix/Downloads/BLE_RSSI_dataset/BLE_RSSI_dataset/iBeacon_RSSI_Labeled1.csv')

# drop the missing values
dataset.dropna(inplace=True)

y = dataset.iloc[1:1000, [0]].values
X = dataset.iloc[1:1000, [1,2,3,4,5,6,7,8,9,10,11,12,13]].values


from sklearn.preprocessing import LabelEncoder, OneHotEncoder



# encoding categorical values is to use a technique called label encoding.
# Label encoding is simply converting each value in a column to a number


from sklearn.metrics import accuracy_score
# Splitting the dataset into the Training set and Test set
from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.25, random_state = 0)
# Feature Scaling

# Fitting K-NN to the Training set
from sklearn.neighbors import KNeighborsClassifier
classifier = KNeighborsClassifier(n_neighbors = 7, metric = 'minkowski', p = 2)
classifier.fit(X_train, y_train)
# Predicting the Test set results
y_pred = classifier.predict(X_test)
a = accuracy_score(y_pred, y_test)
# Making the Confusion Matrix
from sklearn.metrics import confusion_matrix
cm = confusion_matrix(y_test, y_pred)
cm
plt.matshow(cm)
cm

import pickle 

# Save the trained model as a pickle string. 
#saved_model = pickle.dumps(classifier)
# Load the pickled model 
#knn_from_pickle = pickle.loads(saved_model) 

# Use the loaded pickled model to make predictions 
#knn_from_pickle.predict(X_test) 
# save the model to disk
pickle.dump(classifier, open('C:/Users/MLogix/Downloads/BLE_RSSI_dataset/BLE_RSSI_dataset/iBeacon_RSSI_Labeled1knn.model', 'wb'))

model = pickle.load(open('C:/Users/MLogix/Downloads/BLE_RSSI_dataset/BLE_RSSI_dataset/iBeacon_RSSI_Labeled1.modelknn', 'rb'))
result_val = model.score(X_train, y_train)
result_test = model.score(X_test, y_test)
#from sklearn.externals import joblib
# save the model to disk


