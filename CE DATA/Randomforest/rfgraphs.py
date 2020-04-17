
import matplotlib.pyplot as plt
#Batch sizes

Accuracy=[0.91153,0.90923, 0.91307]
batch_sizes=[512, 824, 1024]
plt.plot(batch_sizes, Accuracy)
plt.ylabel('Accuracy')
plt.xlabel('Batch size')
plt.title("Impact of different batch sizes")
plt.show()


#Trees
Accuracy=[0.9136, 0.9115, 0.9123, 0.9161, 0.9153]
trees=[10, 20, 30, 50, 100]


plt.plot(trees, Accuracy)
plt.ylabel('Accuracy')
plt.xlabel('No. of trees')
plt.title("Impact of different number of trees on testing dataset")
plt.show()
 
#Random states

Accuracy=[0.9533, 0.9546,0.9528,0.9522]
Random_states=[0,2,6,10]


plt.plot(Random_states, Accuracy)
plt.ylabel('Accuracy')
plt.xlabel('Random states')
plt.title("Impact of different number of random states")
plt.show()
