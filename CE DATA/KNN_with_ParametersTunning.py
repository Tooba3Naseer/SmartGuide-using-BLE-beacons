# Hypertunning of parameters of kNN and final Training using best parameters

import numpy as np
import pandas as pd
from sklearn.neighbors import KNeighborsClassifier
from sklearn.model_selection import GridSearchCV
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt
from sklearn.metrics import confusion_matrix
import pickle


# Read the csv dataset file from PC
dataset = pd.read_csv('F:/CE DATA/dataset.csv')

# X contain feature values for all instances
# y contain labelled outputs for all instanses
X = dataset.iloc[:, 0:24].values
y = dataset.iloc[:, 24].values


#create a kNN model
classifier = KNeighborsClassifier()
#create a dictionary of all values we want to test for parameters of kNN
param_grid = {'n_neighbors': np.arange(1, 26), 
              'weights' : ['distance', 'uniform'], 'metric' : ['euclidean','manhattan' ]}
# use GridsearchCV to test for all possible combinations of n_neighbors, weights, distance in order to find out best parameters at which accuracy is maximum
# cv = 5 means we use 5 folds cross validation
# compute 
kNN_GSCV = GridSearchCV(classifier, param_grid, cv=5, verbose = 2)
# fit model to data
kNN_GSCV.fit(X, y)

# check top performance parameters
print(kNN_GSCV.best_params_)

# check mean score for the top performing values of parameters
print(kNN_GSCV.best_score_)

# print full grid with scores,std and params
# print(knn_gscv.grid_scores_)

# Get values of accuracies for each combination and store in a list
means = kNN_GSCV.cv_results_['mean_test_score']
stds = kNN_GSCV.cv_results_['std_test_score']

# plot 4 graphs

scores=[]
neighbors=[]
for mean, std, params in zip(means, stds, kNN_GSCV.cv_results_['params']):
    if params['metric'] == 'manhattan' and params['weights'] == 'uniform':
        neighbors.append(params['n_neighbors'])
        scores.append(mean)

plt.plot(neighbors, scores)
plt.ylabel('Accuracy')
plt.xlabel('Value of K for KNN')
plt.title("Accuracy score using distance = manhattan and weights = uniform")
plt.show()

scores=[]
neighbors=[]
for mean, std, params in zip(means, stds, kNN_GSCV.cv_results_['params']):
    if params['metric'] =='manhattan' and params['weights'] == 'distance':
        neighbors.append(params['n_neighbors'])
        scores.append(mean)

plt.plot(neighbors, scores)
plt.ylabel('Accuracy')
plt.xlabel('Value of K for KNN')
plt.title("Accuracy score using distance = manhattan and weights = distance")
plt.show()

scores=[]
neighbors=[]
for mean, std, params in zip(means, stds, kNN_GSCV.cv_results_['params']):
    if params['metric'] == 'euclidean' and params['weights'] == 'uniform':
        neighbors.append(params['n_neighbors'])
        scores.append(mean)

plt.plot(neighbors, scores)
plt.ylabel('Accuracy')
plt.xlabel('Value of K for KNN')
plt.title("Accuracy score using distance = euclidean and weights = uniform")
plt.show()

scores=[]
neighbors=[]
for mean, std, params in zip(means, stds, kNN_GSCV.cv_results_['params']):
    if params['metric'] == 'euclidean' and params['weights'] == 'distance':
        neighbors.append(params['n_neighbors'])
        scores.append(mean)

plt.plot(neighbors, scores)
plt.ylabel('Accuracy')
plt.xlabel('Value of K for KNN')
plt.title("Accuracy score using distance = euclidean and weights = distance")
plt.show()
    
     


# Now we tarined our model using best parameters

# split training and testing data, here startify will use so that the train and test sets have approximately the same percentage of samples of each target class as the complete set.
X_train, X_test, y_train, y_test =train_test_split(X, y, test_size = 0.25, stratify=y)

# Fitting K-NN to the Training set
classifier = KNeighborsClassifier(n_neighbors = kNN_GSCV.best_params_['n_neighbors'], metric = kNN_GSCV.best_params_['metric'], weights=kNN_GSCV.best_params_['weights'])
classifier.fit(X_train, y_train)
# Predicting the Test set results
y_pred = classifier.predict(X_test)

# Predicting the Training set results
y_pred1 = classifier.predict(X_train)

# Making the Confusion Matrix for test data
cm = confusion_matrix(y_test, y_pred)
cm
plt.matshow(cm)
cm

# calculate precison and recall of test data
recall = np.diag(cm) / np.sum(cm, axis = 1)
precision = np.diag(cm) / np.sum(cm, axis = 0)

# To get overall measures of precision and recall, use then
print('Testing Precision: %f' % np.mean(precision))
print('Testing Recall: %f' % np.mean(recall))

# Making the Confusion Matrix for training data
cm = confusion_matrix(y_train, y_pred1)
cm
plt.matshow(cm)
cm

# calculate precison and recall of train data
recall1 = np.diag(cm) / np.sum(cm, axis = 1)
precision1 = np.diag(cm) / np.sum(cm, axis = 0)

# To get overall measures of precision and recall, use then
print('Training Precision: %f' % np.mean(precision1))
print('Training Recall: %f' % np.mean(recall1))


# Save the trained model as a pickle string. 
# Save the model to disk
pickle.dump(classifier, open('F:/CE DATA/TrainedModel.model', 'wb'))
# Load the pickled model
model = pickle.load(open('F:/CE DATA/TrainedModel.model', 'rb'))
# calculate accuracy on training data
training_accuracy = model.score(X_train, y_train)
print('Training Accuracy: %f' % training_accuracy)
# calculate accuracy on test data
testing_accuracy = model.score(X_test, y_test)
print('Testing Accuracy: %f' % testing_accuracy)


