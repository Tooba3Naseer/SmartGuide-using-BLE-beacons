# load and evaluate a saved model

from keras.models import load_model
import numpy as np
from sklearn.preprocessing import LabelEncoder
import pandas as pd
from keras.utils import to_categorical

# Read the csv dataset file from PC
dataset = pd.read_csv('F:/CE DATA/dataset.csv')


# X contain feature values for all instances
# y contain labelled outputs for all instanses
X = dataset.iloc[1:, 0:24].values
y1 = dataset.iloc[1:, 24].values

# Label encoder encode classes that are in string format into integers
labelencoder_X_5 = LabelEncoder()
y = labelencoder_X_5.fit_transform(y1)

# convert a class vector into binary class matrix
# e.g if class is 1 and total classes are three then it is converted into [0,1,0]
y = to_categorical(y)


# load model
model = load_model('model.h5')
# summarize model.
model.summary()

# evaluate the model
score = model.evaluate(X, y)

print("%s: %.2f%%" % (model.metrics_names[1], score[1]*100))
a = np.array([[-73,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100], [-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-71,-73,-100,-100,-100,-100,-100],
              [-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-68,-100,-100,-100,-100,-100,-100,-73,-100,-100,-100], [-100,-100,-100,-100,-100,-100,-100,-100,-68,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100]])
y_pred = model.predict_classes(a)
print(y_pred)
labels=['L1','L10','L11','L12','L13','L15','L16','L19','L2','L20','L21','L3','L4','L5','L6','L8','L9']
#should print L1,L16,L13,L8
for each in y_pred:
    print( labels[each])




