''' Run this code using tensorflow version 1.13 and keras version 2.3
, also add show_graph.py file in directory before running'''
from __future__ import print_function
import keras
from keras.datasets import mnist
from keras.models import Sequential
from keras.layers import Dense, Dropout, Flatten
from keras.layers import Conv2D, MaxPooling2D
from keras import backend as K
from keras.models import load_model

#If a model containing BatchNormization layers
# This line must be executed before loading Keras model.
K.set_learning_phase(0)

model=keras.models.load_model("model.h5", compile=False )    
model.summary()

# gives name of input and output node
print("model.outputs="+str(model.outputs))
print("model.inputs="+str(model.inputs))

from IPython.display import display, HTML,Image
from show_graph import show_graph
from keras import backend as K
import tensorflow as tf
sess = K.get_session()
graph_def = sess.graph.as_graph_def()
# graph_def
show_graph(graph_def)

#################################################################3
def freeze_session(session, keep_var_names=None, output_names=None, clear_devices=True):
    """
    Freezes the state of a session into a pruned computation graph.

    Creates a new computation graph where variable nodes are replaced by
    constants taking their current value in the session. The new graph will be
    pruned so subgraphs that are not necessary to compute the requested
    outputs are removed.
    @param session The TensorFlow session to be frozen.
    @param keep_var_names A list of variable names that should not be frozen,
                          or None to freeze all the variables in the graph.
    @param output_names Names of the relevant graph outputs.
    @param clear_devices Remove the device directives from the graph for better portability.
    @return The frozen graph definition.
    """
    from tensorflow.python.framework.graph_util import convert_variables_to_constants
    graph = session.graph
    with graph.as_default():
        freeze_var_names = list(set(v.op.name for v in tf.global_variables()).difference(keep_var_names or []))
        output_names = output_names or []
        output_names += [v.op.name for v in tf.global_variables()]
        # Graph -> GraphDef ProtoBuf
        input_graph_def = graph.as_graph_def()
        if clear_devices:
            for node in input_graph_def.node:
                node.device = ""
        frozen_graph = convert_variables_to_constants(session, input_graph_def,
                                                      output_names, freeze_var_names)
        return frozen_graph
    
from keras import backend as K
import tensorflow as tf

frozen_graph = freeze_session(K.get_session(),
                              output_names=[out.op.name for out in model.outputs])

tf.train.write_graph(frozen_graph, "model", "model.pb", as_text=False)

'''========================Load the pb file and inference==================='''
import tensorflow as tf
import os
import sys
from tensorflow.python.platform import gfile
sess=tf.InteractiveSession()
f = gfile.FastGFile("model.pb", 'rb')
graph_def = tf.GraphDef()
# Parses a serialized binary message into the current message.
graph_def.ParseFromString(f.read()); f.close()
sess.graph.as_default()
# Import a serialized TensorFlow `GraphDef` protocol buffer
# and place into the current default `Graph`.
tf.import_graph_def(graph_def)

'''=====================Locate the output Tensor by name===================='''


MyOutput_tensor = sess.graph.get_tensor_by_name('import/dense_10/Sigmoid:0') #output


'''=====================Prepare datasets for inference==================='''
#Loading Data:
import pandas as pd; import numpy as np;


import pandas as pd
dataset = pd.read_csv("dataset.csv")
X_val = [[-73,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100], [-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-71,-73,-100,-100,-100,-100,-100],
              [-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-68,-100,-100,-100,-100,-100,-100,-73,-100,-100,-100], [-100,-100,-100,-100,-100,-100,-100,-100,-68,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100,-100]]

'''dataset.iloc[0:3, 0:24].values'''


'''========================Make prediction===================='''
#using input

predictions = sess.run(MyOutput_tensor, {'import/dense_9_input:0': X_val})
print("predictions="+str(predictions))



'''ToGet All Node Names from TF'''
[n.name for n in tf.get_default_graph().as_graph_def().node]

pip install tensorflow=='1.14'

pip install keras=='2.3.0'
