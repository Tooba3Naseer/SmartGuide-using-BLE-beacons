# Grpahical representations of ANN hyperparameter tuning results

import matplotlib.pyplot as plt
import numpy as np

#------------------------------------------1-------------------------------------------------
# Grpahical representation of behaviour of accuracy scores on differnt batch sizes

scores=[0.915574, 0.900854, 0.901069, 0.828011 ]
batch_sizes=[40,60,80,100]

plt.plot(batch_sizes, scores)
plt.ylabel('Accuracy')
plt.xlabel('Batch size')
plt.title("Accuracy score on different batch sizes")
plt.show()

# Grpahical representation of behaviour of accuracy scores on differnt batch epochs

scores=[0.763859, 0.801551, 0.774967, 0.915574]
epochs=[50, 100, 200, 250]


plt.plot(epochs, scores)
plt.ylabel('Accuracy')
plt.xlabel('Epochs')
plt.title("Accuracy score on different epochs")
plt.show()

#------------------------------------------2-------------------------------------------------
# Grpahical representation of behaviour of accuracy scores on differnt optimization algorithms
objects = ('SGD', 'RMSprop', 'Adagrad', 'Adadelta', 'Adam', 'Adamax', 'Nadam')
x = np.arange(len(objects))
performance = [0.761127,0.901487 , 0.705623, 0.857959, 0.927529, 0.761997 ,0.920891]

plt.bar(x, performance, align='center', alpha=0.5)
plt.xticks(x, objects)
plt.ylabel('Accuracy')
plt.title('Accuracy score on different optimization algorithms')
plt.show()



#------------------------------------------3-------------------------------------------------
# Grpahical representation of behaviour of accuracy scores on differnt weight initialization methods
#ON EXCEL
#Best: 0.941176 using {'init_mode': 'zero'}
#0.917279 (0.009255) with: {'init_mode': 'uniform'}
#0.916500 (0.021615) with: {'init_mode': 'normal'}
#0.941176 (0.000000) with: {'init_mode': 'zero'}

#------------------------------------------4-------------------------------------------------
# Grpahical representation of behaviour of accuracy scores on differnt activation functions
#No need, all have same accuracy
#Best: 0.941176 using {'activation': 'relu'}
#0.941176 (0.000000) with: {'activation': 'relu'}
#0.941176 (0.000000) with: {'activation': 'tanh'}
#0.941176 (0.000000) with: {'activation': 'sigmoid'}
#0.941176 (0.000000) with: {'activation': 'linear'}

#------------------------------------------5-------------------------------------------------
# Grpahical representation of behaviour of accuracy scores on differnt no of neurons
scores=[0.906341, 0.910438, 0.920169, 0.930114 ]
neurons=[10,12,25,27]

plt.plot(neurons, scores)
plt.ylabel('Accuracy')
plt.xlabel('Neurons')
plt.title("Accuracy score on different number of neurons")
plt.show()


