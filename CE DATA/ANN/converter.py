# write and compile this code in google collaborator




import tensorflow as tf
model=tf.keras.models.load_model("model.h5")
converter= tf.lite.TFLiteConverter.from_keras_model(model)
converter.experimental_new_converter = True
tfmodel = converter.convert()
open("converted_model.tflite", "wb").write(tfmodel)


# how to use google collaborator
# firstly initialize resources and then upload model.h5 file
# then write code and compile