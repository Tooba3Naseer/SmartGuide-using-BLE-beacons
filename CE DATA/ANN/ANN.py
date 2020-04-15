# Training of data using Aritifical Neural Networks
import numpy as np
import pandas as pd
from sklearn.preprocessing import LabelEncoder
from keras.utils import to_categorical
from sklearn.model_selection import train_test_split
from keras.models import Sequential
from keras.layers import Dense
from sklearn.metrics import classification_report


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
classifier.add(Dense(output_dim = 27, init = 'uniform', activation = 'relu', input_dim = 24))
classifier.add(Dense(output_dim = 17, init = 'uniform', activation = 'sigmoid'))
# compile model
# adam is optimizing function that is used to update internal parameters of model(weights)
classifier.compile(optimizer = 'adam', loss = 'binary_crossentropy', metrics = ['accuracy'])
# one epoch is one complete pass through dataset, one epoch has feedforward and back propagation
# batch size represents no of samples used to calculate error and then that error is used to update weights
classifier.fit(X_train, y_train, batch_size = 40, nb_epoch = 250)
y_pred = classifier.predict_classes(X_test)

# Calculate accuracy on test data
acc_test = classifier.evaluate(X_test,  y_test)
print("Accuracy on test data:")
print(acc_test[1])

# Calculate accuracy on training data
acc_train = classifier.evaluate(X_train,  y_train)
print("Accuracy on train data:")
print(acc_train[1])

# convert each entry again into int from vector
# Set axis=-1 means, extract largest indices in each row
y1 = np.argmax(y_test, axis=-1)

# print precision, recall for test data
print("Classification Report for test data")
print(classification_report(y1, y_pred))

# convert each entry again into int from vector
# Set axis=-1 means, extract largest indices in each row
y1 = np.argmax(y_train, axis=-1)

y_pred = classifier.predict_classes(X_train)
# print precision, recall for train data
print("Classification Report for train data")
print(classification_report(y1, y_pred))

# save model and architecture to single file
classifier.save("model.h5")
print("Saved model to disk")



