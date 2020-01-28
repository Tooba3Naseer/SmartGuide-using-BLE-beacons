# KNN

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from sklearn import preprocessing  as pp
#Read the csv dataset file from PC
dataset = pd.read_csv('F:/FYP Project Data/dataset.csv')


# drop the missing values
dataset.dropna(inplace=True)

X = dataset.iloc[:, 0:24].values
y = dataset.iloc[:, 24].values

from sklearn.metrics import accuracy_score
from sklearn.neighbors import KNeighborsClassifier
from sklearn.cross_validation import cross_val_score
import matplotlib.pyplot as plt
# Splitting the dataset into the Training set and Test set
from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test =train_test_split(X, y, test_size = 0.25, stratify=y)


# search for an optimal value of K for KNN

# list of integers 1 to 30
# integers we want to try
k_range = range(1, 31)

# list of scores from k_range
k_scores = []
train_scores = []

# 1. we will loop through reasonable values of k
for k in k_range:
    # Fitting K-NN to the Training set
    classifier = KNeighborsClassifier(n_neighbors = k, p = 1, weights='uniform')
    classifier.fit(X_train, y_train)
    # Predicting the Test set results
    y_pred = classifier.predict(X_test)
    a = accuracy_score(y_pred, y_test)
    k_scores.append(a)
    y_pred2 = classifier.predict(X_train)
    b = accuracy_score(y_pred2, y_train)
    train_scores.append(b)
print(k_scores)
print(train_scores)

# plot the value of K for KNN (x-axis) versus accuracy (y-axis)

from matplotlib.legend_handler import HandlerLine2D
line1, = plt.plot(k_range, train_scores, 'b', label="Train ACC")
line2, = plt.plot(k_range, k_scores, 'r', label="Test ACC")
plt.legend(handler_map={line1: HandlerLine2D(numpoints=2)})
plt.ylabel('Accuracy')
plt.xlabel('Value of K for KNN')
plt.title("Accuracy score using Manhattan distance on different values of K")
plt.show()

# search for an optimal value of K for KNN

# list of integers 1 to 30
# integers we want to try
k_range = range(1, 31)

# list of scores from k_range
k_scores = []
train_scores = []

# 1. we will loop through reasonable values of k
for k in k_range:
    # Fitting K-NN to the Training set
    classifier = KNeighborsClassifier(n_neighbors = k, p = 2, weights='uniform')
    classifier.fit(X_train, y_train)
    # Predicting the Test set results
    y_pred = classifier.predict(X_test)
    a = accuracy_score(y_pred, y_test)
    k_scores.append(a)
    y_pred2 = classifier.predict(X_train)
    b = accuracy_score(y_pred2, y_train)
    train_scores.append(b)
print(k_scores)
print(train_scores)

# plot the value of K for KNN (x-axis) versus accuracy (y-axis)

from matplotlib.legend_handler import HandlerLine2D
line1, = plt.plot(k_range, train_scores, 'b', label="Train ACC")
line2, = plt.plot(k_range, k_scores, 'r', label="Test ACC")
plt.legend(handler_map={line1: HandlerLine2D(numpoints=2)})
plt.ylabel('Accuracy')
plt.xlabel('Value of K for KNN')
plt.title("Accuracy score using Euclidean distance on different values of K")
plt.show()


"""
# Making the Confusion Matrix
from sklearn.metrics import confusion_matrix
cm = confusion_matrix(y_test, y_pred)
cm
plt.matshow(cm)
cm

import pickle 

# Save the trained model as a pickle string. 
#from sklearn.externals import joblib
# save the model to disk
pickle.dump(classifier, open('F:/FYP Project Data/dataset.model', 'wb'))
# Load the pickled model
model = pickle.load(open('F:/FYP Project Data/dataset.model', 'rb'))
result_val = model.score(X_train, y_train)
result_test = model.score(X_test, y_test)

"""
