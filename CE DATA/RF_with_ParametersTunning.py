# Hypertunning of parameters of Random Forest and final Training using best parameters

import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits import mplot3d
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import confusion_matrix
from sklearn.model_selection import GridSearchCV
import pickle 

# Read the csv dataset file from PC
dataset = pd.read_csv('F:/CE DATA/dataset.csv')

# X contain feature values for all instances
# y contain labelled outputs for all instanses
X = dataset.iloc[:, 0:24].values
y = dataset.iloc[:, 24].values


# number of trees in random forest
n_estimators = [200,500,1000]
# number of features at every split
# When we use auto, RF takes all features, while sqrt takes random features equal to sqrt of total features
max_features = ['auto', 'sqrt']

# max depth of tree, by using max depth, we can avoid overfitting
max_depth = [10,25,100]
# create random grid
random_grid = {
 'n_estimators': n_estimators,
 'max_features': max_features,
 'max_depth': max_depth
 }

RFC = RandomForestClassifier()
# Random search of parameters
# By using verbose > 0 it will tell everything about whats going on all the time and prints on console
RFC_random = GridSearchCV(RFC, random_grid, cv = 5, verbose = 2)
# Fit the model
RFC_random.fit(X, y)
# print results
print(RFC_random.best_params_)

# check mean score for the top performing values of parameters
print(RFC_random.best_score_)

# Get values of accuracies for each combination and store in a list
means = RFC_random.cv_results_['mean_test_score']
stds = RFC_random.cv_results_['std_test_score']

# plot 2 graphs

scores=[]
maxDepths =[]
estimators=[]
for mean, std, params in zip(means, stds, RFC_random.cv_results_['params']):
    if params['max_features'] == 'auto':
        estimators.append(params['n_estimators'])
        maxDepths.append(params['max_depth'])
        scores.append(mean)

# plotting of 3d figure
fig = plt.figure()
ax = plt.axes(projection='3d')

# Data for a three-dimensional line
ax.plot3D(estimators, maxDepths, scores, 'blue')
ax.set_title("Accuracy score using maximum features = auto")
ax.set_ylabel('Max depth of trees')
ax.set_xlabel('Number of decision trees')
ax.set_zlabel('Accuracy')

scores=[]
maxDepths =[]
estimators=[]
for mean, std, params in zip(means, stds, RFC_random.cv_results_['params']):
    if params['max_features'] == 'sqrt':
        estimators.append(params['n_estimators'])
        maxDepths.append(params['max_depth'])
        scores.append(mean)

# plotting of 3d figure
fig = plt.figure()
ax = plt.axes(projection='3d')

# Data for a three-dimensional line
ax.plot3D(estimators, maxDepths, scores, 'blue')
ax.set_title("Accuracy score using maximum features = sqrt")
ax.set_ylabel('Max depth of trees')
ax.set_xlabel('Number of decision trees')
ax.set_zlabel('Accuracy')




# Now we tarined our model using best parameters

# split training and testing data, here startify will use so that the train and test sets have approximately the same percentage of samples of each target class as the complete set.
X_train, X_test, y_train, y_test =train_test_split(X, y, test_size = 0.25, stratify=y)

# Fitting Random Forest to the Training set
forest = RandomForestClassifier(n_estimators = RFC_random.best_params_['n_estimators'],  max_depth= RFC_random.best_params_['max_depth'], max_features= RFC_random.best_params_['max_features'])
forest.fit(X_train,y_train)
# Predicting the Test set results
y_pred = forest.predict(X_test)

# Making the Confusion Matrix
cm = confusion_matrix(y_test, y_pred)
cm
plt.matshow(cm)
cm

# calculate precison and recall
recall = np.diag(cm) / np.sum(cm, axis = 1)
precision = np.diag(cm) / np.sum(cm, axis = 0)

# To get overall measures of precision and recall, use then
print('Precision: %f' % np.mean(precision))
print('Recall: %f' % np.mean(recall))


# Save the trained model as a pickle string. 
# Save the model to disk
pickle.dump(forest, open('F:/FYP Project Data/TrainedModel1.model', 'wb'))
# Load the pickled model
model = pickle.load(open('F:/FYP Project Data/TrainedModel1.model', 'rb'))
# calculate accuracy on training data
training_accuracy = model.score(X_train, y_train)
# calculate accuracy on test data
testing_accuracy = model.score(X_test, y_test)
print('Accuracy: %f' % testing_accuracy)
