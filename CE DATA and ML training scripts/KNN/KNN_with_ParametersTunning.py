# Hypertunning of parameters of kNN

import numpy as np
import pandas as pd
from sklearn.neighbors import KNeighborsClassifier
from sklearn.model_selection import GridSearchCV
import matplotlib.pyplot as plt


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



# Get values of accuracies for each combination and store in a list
means = kNN_GSCV.cv_results_['mean_test_score']

stds = kNN_GSCV.cv_results_['std_test_score']
params = kNN_GSCV.cv_results_['params']

# print full grid with scores,std and params
for mean, stdev, param in zip(means, stds, params):
    print("%f (%f) with: %r" % (mean, stdev, param))

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

#check impact of weights on accuracy score
scores =[0,0]
for mean, std, params in zip(means, stds, kNN_GSCV.cv_results_['params']):
    if params['metric'] == 'euclidean' and params['n_neighbors'] == 22 and params['weights'] == 'distance':
        scores[0]= mean
    if params['metric'] == 'euclidean' and params['n_neighbors'] == 22 and params['weights'] == 'uniform':
        scores[1]= mean 
        
print(scores)

#check impact of distance measure on accuracy score
scores =[0,0]
for mean, std, params in zip(means, stds, kNN_GSCV.cv_results_['params']):
    if params['metric'] == 'euclidean' and params['n_neighbors'] == 22 and params['weights'] == 'uniform':
        scores[0]= mean
    if params['metric'] == 'manhattan' and params['n_neighbors'] == 22 and params['weights'] == 'uniform':
        scores[1]= mean
print(scores)
    


