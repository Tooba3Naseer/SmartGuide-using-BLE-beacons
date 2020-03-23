#Linear Regression
import pandas as pd
dataset = pd.read_csv('C:/Users/MLogix/Desktop/7th Semester/dataset.csv')
from sklearn import linear_model

#dataset_X, dataset_y = datasets.load_dataset(return_X_y=True)

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

import pandas as pd
# create linear regression object 
reg = linear_model.LinearRegression() 

# train the model using the training sets 
reg.fit(X_train, y_train.ravel()) 
# regression coefficients 
print('Coefficients: \n', reg.coef_) 
  
# variance score: 1 means perfect prediction 
print('Variance score: {}'.format(reg.score(X_test, y_test))) 


import pickle 

# Save the trained model as a pickle string. 
#from sklearn.externals import joblib
# save the model to disk
pickle.dump(reg, open('C:/Users/MLogix/Desktop/7th Semester/datasetlinear.model', 'wb'))
# Load the pickled model 
model = pickle.load(open('C:/Users/MLogix/Desktop/7th Semester/datasetlinear.model', 'rb'))
result_val = model.score(X_train, y_train)
result_test = model.score(X_test, y_test)



