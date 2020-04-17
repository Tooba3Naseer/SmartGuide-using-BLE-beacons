# Random Forest Classification on Tensorflow


from __future__ import print_function
import pandas as pd
import tensorflow as tf
from tensorflow.python.ops import resources
from tensorflow.contrib.tensor_forest.python import tensor_forest
from tensorflow.python.ops import resource_variable_ops
from tensorflow.python.eager import context
from tensorflow.python.framework.ops import disable_eager_execution

disable_eager_execution()
#X = dataset.iloc[1:21, [0,1]].values
#y = dataset.iloc[1:21, 2].values
dataset = pd.read_csv('C:/Users/MLogix/Desktop/7th Semester/dataset/Algorithms/dataset.csv')

# drop the missing values
dataset.dropna(inplace=True)

# Ignore all GPUs, tf random forest does not benefit from it.
num_steps = 500 # Total steps to train
batch_size = 512 # The number of samples per batch
num_classes = 24 # The 10 digits
num_features = 784 # Each image is 28x28 pixels
num_trees = 10
max_nodes = 1000

input_x = dataset.iloc[1:5200, [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23]].values
input_y = dataset.iloc[1:5200, 24].values

# Splitting the dataset into the Training set and Test set

from sklearn.model_selection import train_test_split

X_train, X_test, y_train, y_test = train_test_split(input_x, input_y, test_size = 0.25, random_state = 0)
tf.reset_default_graph() 

hparams = tensor_forest.ForestHParams(num_classes=num_classes, num_features=num_features,
                                      num_trees=num_trees,
                                      max_nodes=max_nodes).fill()
graph = tensor_forest.RandomForestGraphs(hparams)


# Input and Target placeholders 

X = tf.placeholder(tf.float32, shape=[None, 24])

#X = tf.reshape(X , [2, num_features])
Y = tf.placeholder(tf.int64, shape=[None])
#Get training graph and loss

train_op = graph.training_graph(X, Y)

loss_op = graph.training_loss(X, Y)

#tf.map_fn(tensor_forest.ForestHParams(), num_classes,num_features, num_trees, max_nodes)
# Measure the accuracy


infer_op, _, _ = graph.inference_graph(X)
correct_prediction = tf.equal(tf.argmax(infer_op, 1), tf.cast(Y, tf.int64))
accuracy_op = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))



#Initialize the variables (i.e. assign their default value) and forest resources

init_vars = tf.group(tf.global_variables_initializer(), resources.initialize_resources(resources.shared_resources()))
#
#    
#
## Start TensorFlow session
#
sess = tf.Session()
#
#
#
## Run the initializer
#
sess.run(init_vars)
#
#
#
##Training
#
for i in range(1, num_steps + 1):
#
#    _, l = sess.run([train_op, loss_op], feed_dict={X: X_train, Y: y_train})
#datase
#    if i % 50 == 0 or i == 1:
#
#        acc = sess.run(accuracy_op, feed_dict={X: X_train, Y: y_train})
#
#        print('Step %i, Loss: %f, Acc: %f' % (i, l, acc))
#
    _, l = sess.run([train_op, loss_op], feed_dict={X: X_train, Y: y_train})
    if i % 50 == 0 or i == 1:
        acc = sess.run(accuracy_op, feed_dict={X: X_train, Y: y_train})
        print('Step %i, Loss: %f, Acc: %f' % (i, l, acc))


print("Test Accuracy:", sess.run(accuracy_op, feed_dict={X: X_test, Y: y_test}))
#from tensorflow import keras
#keras_file = "rf.h5"
#keras.models.save_model(sess.run, keras_file)

oSaver = tf.train.Saver()

oSess = sess
oSaver.save(oSess,'C:/Users/MLogix/Desktop/7th Semester/dataset/Algorithms/dataset.ckpt')
  #filename ends with .ckpt
# Convert the model into TF Lite.

oSaver.save(oSess, "C:/Users/MLogix/Desktop/7th Semester/dataset/Algorithms/datasetmodel.h5")

#
#converter = tf.lite.TFLiteConverter.from_saved_model('datasetmodel.pb')
#tflite_model = converter.convert()
#open("converted_model.tflite", "wb").write(tflite_model)
from tensorflow import keras
from tensorflow.contrib import lite
#keras_file = "rf.h5"
#keras.models.save_model(sess, keras_file)
keras.models.save_model(oSess, 'C:/Users/MLogix/Desktop/7th Semester/dataset/Algorithms/datasetmodel')

converter = lite.TFLiteConverter.from_saved_model('datasetmodel')
tflite_model = converter.convert()
open("converted_model.tflite", "wb").write(tflite_model)
