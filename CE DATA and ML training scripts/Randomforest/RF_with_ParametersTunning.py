# Hypertunning of parameters of Random Forest and final Training using best parameters

import numpy as np
import matplotlib.pyplot as plt
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
max_depth = [25,32]
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

# plot 3 graphs

scores=[]
estimators=[]
for mean, std, params in zip(means, stds, RFC_random.cv_results_['params']):
    if params['max_features'] == 'sqrt' and params['max_depth'] == 25 :
        estimators.append(params['n_estimators'])
        scores.append(mean)

plt.plot(estimators, scores)
plt.ylabel('Accuracy')
plt.xlabel('Number of Decision Trees')
plt.title("Impact of Number of Trees")
plt.show()

scores=[]
maxDepth=[]
for mean, std, params in zip(means, stds, RFC_random.cv_results_['params']):
    if params['max_features'] == 'sqrt' and params['n_estimators'] == 500 :
        maxDepth.append(params['max_depth'])
        scores.append(mean)

plt.plot(maxDepth, scores)
plt.ylabel('Accuracy')
plt.xlabel(' Maximum Depth')
plt.title("Impact of Maximum Depth")
plt.show()

scores=[]
max_features=[]
for mean, std, params in zip(means, stds, RFC_random.cv_results_['params']):
    if params['n_estimators'] == 500 and params['max_depth'] == 25 :
        if params['max_features'] == 'sqrt':
            max_features.append(5)
        else:
            max_features.append(24)
        scores.append(mean)

plt.plot(max_features, scores)
plt.ylabel('Accuracy')
plt.xlabel('Maximum Features')
plt.title("Impact of Maximum Features")
plt.show()





# Now we tarined our model using best parameters

# split training and testing data, here startify will use so that the train and test sets have approximately the same percentage of samples of each target class as the complete set.
X_train, X_test, y_train, y_test =train_test_split(X, y, test_size = 0.25, stratify=y)

# Fitting Random Forest to the Training set
forest = RandomForestClassifier(n_estimators = RFC_random.best_params_['n_estimators'],  max_depth= RFC_random.best_params_['max_depth'], max_features= RFC_random.best_params_['max_features'])
forest.fit(X_train,y_train)
# Predicting the Test set results
y_pred = forest.predict(X_test)

# Predicting the Training set results
y_pred1 = forest.predict(X_train)

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
pickle.dump(forest, open('F:/CE DATA/TrainedModel1.model', 'wb'))
# Load the pickled model
model = pickle.load(open('F:/CE DATA/TrainedModel1.model', 'rb'))
# calculate accuracy on training data
training_accuracy = model.score(X_train, y_train)
print('Training Accuracy: %f' % training_accuracy)
# calculate accuracy on test data
testing_accuracy = model.score(X_test, y_test)
print('Testing Accuracy: %f' % testing_accuracy)


