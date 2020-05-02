# Training of data using Aritifical Neural Networks
import numpy as np
import pandas as pd
from sklearn.preprocessing import LabelEncoder
from keras.utils import to_categorical
from sklearn.model_selection import train_test_split
from keras.models import Sequential
from keras.layers import Dense
from sklearn.metrics import classification_report
import matplotlib.pyplot as plt
from sklearn.metrics import confusion_matrix
from sklearn.metrics import accuracy_score 


# Read the csv dataset file from PC
dataset = pd.read_csv('F:/CE DATA/dataset.csv')

# X contain feature values for all instances
# y contain labelled outputs for all instanses
X = dataset.iloc[1:, 0:24].values
y = dataset.iloc[1:, 24].values

# Label encoder encode classes that are in string format into integers
labelencoder_X_5 = LabelEncoder()
y = labelencoder_X_5.fit_transform(y)

# convert a class vector into binary class matrix
# e.g if class is 1 and total classes are three then it is converted into [0,1,0]
y = to_categorical(y)

# split training and testing data, here startify will use so that the train and test sets have approximately the same percentage of samples of each target class as the complete set.
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.25,stratify=y)


# create model
# here we use 1 hidden layer
classifier = Sequential()
classifier.add(Dense(output_dim = 28, init = 'uniform', activation = 'relu', input_dim = 24))
classifier.add(Dense(output_dim = 17, init = 'uniform', activation = 'sigmoid'))
# compile model
# adam is optimizing function that is used to update internal parameters of model(weights)
classifier.compile(optimizer = 'adam', loss = 'binary_crossentropy', metrics = ['accuracy'])
# one epoch is one complete pass through dataset, one epoch has feedforward and back propagation
# batch size represents no of samples used to calculate error and then that error is used to update weights
classifier.fit(X_train, y_train, batch_size = 40, nb_epoch = 250)
y_pred = classifier.predict_classes(X_test)

# convert each entry again into int from vector
# Set axis=-1 means, extract largest indices in each row
y1 = np.argmax(y_test, axis=-1)

# print precision, recall for test data
print("Classification Report for test data")
print(classification_report(y1, y_pred))
print('Accuracy score on test data:')
print(accuracy_score(y1, y_pred) )
# convert each entry again into int from vector
# Set axis=-1 means, extract largest indices in each row
y2 = np.argmax(y_train, axis=-1)

y_pred1 = classifier.predict_classes(X_train)
# print precision, recall for train data
print("Classification Report for train data")
print(classification_report(y2, y_pred1))
print('Accuracy score on train data:')
print(accuracy_score(y2, y_pred1 ) )

# save model and architecture to single file
classifier.save("model.h5")
print("Saved model to disk")


classes =[0, 1, 2,3, 4, 5,6,7 ,8 , 9,10,11,12,13,14,15,16]

# plotting of confusion matrix

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
cm = confusion_matrix(y1, y_pred, labels = classes)
np.set_printoptions(precision=2)

# Plot confusion matrix

# set the height and width of confusion matrix
plt.figure(figsize=(8,8))

# use above function
plot_confusion_matrix(cm, classes=classes,
                      title='Confusion matrix')


