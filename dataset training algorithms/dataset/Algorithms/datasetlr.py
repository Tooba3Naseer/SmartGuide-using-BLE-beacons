# Logistic regression

import matplotlib.pyplot as plt
import pandas as pd
from sklearn.linear_model import LogisticRegression
dataset = pd.read_csv('C:/Users/MLogix/Desktop/7th Semester/dataset.csv')

# drop the missing values
dataset.dropna(inplace=True)

X = dataset.iloc[1:21, [0,1]].values
y = dataset.iloc[1:21, 2].values

# Splitting the dataset into the Training set and Test set
from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.25, random_state = 1)
# Feature Scaling
import seaborn as sns
sns.set()
from sklearn.model_selection import train_test_split
import pandas as pd

#Appling logistic regression algorithm to dataset
lr = LogisticRegression()
lr.fit(X_train, y_train)
print(lr.coef_)
print(lr.intercept_)
y_pred = lr.predict(X_test)

#from sklearn.metrics import confusion_matrix
from sklearn.metrics import confusion_matrix
confusion_matrix(y_test, y_pred)
lr.predict_proba(X_test)
# Making the Confusion Matrix
cm = confusion_matrix(y_test, y_pred)
cm
plt.matshow(cm)
cm

import pickle 

# Save the trained model as a pickle string. 
#from sklearn.externals import joblib
# save the model to disk
pickle.dump(lr, open('C:/Users/MLogix/Desktop/7th Semester/dataset.modellr.model', 'wb'))
# Load the pickled model 
model = pickle.load(open('C:/Users/MLogix/Desktop/7th Semester/dataset.modellr.model', 'rb'))
result_val = model.score(X_train, y_train)
result_test = model.score(X_test, y_test)



