#Decision tree
import matplotlib.pyplot as plt
import pandas as pd
dataset = pd.read_csv('C:/Users/MLogix/Desktop/7th Semester/dataset.csv')

# drop the missing values
dataset.dropna(inplace=True)

X = dataset.iloc[1:21, [0,1]].values
y = dataset.iloc[1:21, 2].values


# encoding categorical values is to use a technique called label encoding.
# Label encoding is simply converting each value in a column to a number

#Import tree from sklearn to for decision tree algorithm
from sklearn import tree

# Splitting the dataset into the Training set and Test set
from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.25, random_state = 1)
model = tree.DecisionTreeClassifier()
model.fit(X_train, y_train.ravel())
# Making the Confusion Matrix
from sklearn.metrics import confusion_matrix
y_pred = model.predict(X_test)
cm = confusion_matrix(y_test, y_pred)
cm
plt.matshow(cm)
cm

import pickle 

# Save the trained model as a pickle string. 
#from sklearn.externals import joblib
# save the model to disk
pickle.dump(model, open('C:/Users/MLogix/Desktop/7th Semester/dataset.modeldt.model', 'wb'))
# Load the pickled model 
model = pickle.load(open('C:/Users/MLogix/Desktop/7th Semester/dataset.modeldt.model', 'rb'))
result_val = model.score(X_train, y_train)
result_test = model.score(X_test, y_test)
#from sklearn.externals import joblib
# save the model to disk


