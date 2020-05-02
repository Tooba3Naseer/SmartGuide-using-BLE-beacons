# model training, print accuracy, precision, recall for both train and test data and plotting of confusion matrix 

import numpy as np
import pandas as pd
from sklearn.neighbors import KNeighborsClassifier
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

# split training and testing data, here startify will use so that the train and test sets have approximately the same percentage of samples of each target class as the complete set.
X_train, X_test, y_train, y_test =train_test_split(X, y, test_size = 0.25, stratify=y)

# Fitting K-NN to the Training set
classifier = KNeighborsClassifier(n_neighbors = 22 , metric = 'euclidean', weights=  'uniform')
classifier.fit(X_train, y_train)
# Predicting the Test set results
y_pred = classifier.predict(X_test)
classes=['L1', 'L2', 'L3', 'L4', 'L5', 'L6', 'L8', 'L9', 'L10', 'L11', 'L12', 'L13', 'L15', 'L16', 'L19', 'L20', 'L21']


# made this function for better visualization of confusion matrix
def plot_confusion_matrix(cm, classes,
                          normalize=False,
                          title='Confusion matrix',
                          cmap=plt.cm.Blues):
    """
    This function prints and plots the confusion matrix.
    Normalization can be applied by setting `normalize=True`.
    """
    import itertools
    if normalize:
        cm = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]
        print("Normalized confusion matrix")
    else:
        print('Confusion matrix, without normalization')

    print(cm)

    plt.imshow(cm, interpolation='nearest', cmap=cmap)
    plt.title(title)
    plt.colorbar()
    tick_marks = np.arange(len(classes))
    plt.xticks(tick_marks, classes, rotation=45)
    plt.yticks(tick_marks, classes)

    fmt = '.2f' if normalize else 'd'
    thresh = cm.max() / 2.
    for i, j in itertools.product(range(cm.shape[0]), range(cm.shape[1])):
        plt.text(j, i, format(cm[i, j], fmt),
                 horizontalalignment="center",
                 color="white" if cm[i, j] > thresh else "black")

    plt.ylabel('True label')
    plt.xlabel('Predicted label')
    plt.tight_layout()
    

# Making the Confusion Matrix for test data
cm = confusion_matrix(y_test, y_pred, labels = classes)
np.set_printoptions(precision=2)

# Plot confusion matrix

# set the height and width of confusion matrix
plt.figure(figsize=(8,8))

# use above function
plot_confusion_matrix(cm, classes=classes,
                      title='Confusion matrix')


# calculate precison and recall of test data
recall = np.diag(cm) / np.sum(cm, axis = 1)
precision = np.diag(cm) / np.sum(cm, axis = 0)

# To get overall measures of precision and recall, use then
print('Testing Precision: %f' % np.mean(precision))
print('Testing Recall: %f' % np.mean(recall))

# Predicting the Training set results
y_pred1 = classifier.predict(X_train)

# Making the Confusion Matrix for training data
cm = confusion_matrix(y_train, y_pred1)

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
