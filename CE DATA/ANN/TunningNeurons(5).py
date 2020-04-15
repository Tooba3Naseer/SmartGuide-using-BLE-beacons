# Use scikit-learn to grid search the number of neurons

from sklearn.model_selection import GridSearchCV
from keras.models import Sequential
from keras.layers import Dense
import pandas as pd
from keras.wrappers.scikit_learn import KerasClassifier
from sklearn.preprocessing import LabelEncoder

from keras.utils import to_categorical

# Function to create model, required for KerasClassifier
def create_model(neurons=1):
	# create model
	model = Sequential()
	model.add(Dense(neurons, input_dim=24,kernel_initializer='uniform', activation='relu'))
	model.add(Dense(17,kernel_initializer='uniform', activation='sigmoid'))
	# Compile model
	model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['accuracy'])
	return model

# load dataset
dataset = pd.read_csv('F:/CE DATA/dataset.csv')
# split into input (X) and output (Y) variables
X = dataset.iloc[1:, 0:24].values
Y = dataset.iloc[1:, 24].values

# Label encoder encode classes that are in string format into integers
labelencoder_X_5 = LabelEncoder()
Y = labelencoder_X_5.fit_transform(Y)

# convert a class vector into binary class matrix
# e.g if class is 1 and total classes are three then it is converted into [0,1,0]
Y = to_categorical(Y)


# create model
model = KerasClassifier(build_fn=create_model, epochs=250, batch_size=40, verbose=1)
# defining the grid search parameters
neurons = [10,12,25,27]
param_grid = dict(neurons=neurons)
grid = GridSearchCV(estimator=model, param_grid=param_grid, cv=5)
grid_result = grid.fit(X, Y)
# summarize results
print("Best: %f using %s" % (grid_result.best_score_, grid_result.best_params_))
means = grid_result.cv_results_['mean_test_score']
stds = grid_result.cv_results_['std_test_score']
params = grid_result.cv_results_['params']
for mean, stdev, param in zip(means, stds, params):
    print("%f (%f) with: %r" % (mean, stdev, param))