# Graph plotting for kNN

import numpy as np
import matplotlib.pyplot as plt
a=1
b=2
c=(a,b)
print(c)
# data to plot
n_groups = 3
score_training = ( 0.911742, 0.877927, 0.855227)
score_testing = (0.909440, 0.860630, 0.845343)

# create plot
fig, ax = plt.subplots()
index = np.arange(n_groups)
bar_width = 0.15
opacity = 0.8

rects1 = plt.bar(index, score_training, bar_width,
alpha=opacity,
color='b',
label='Training Scores')

rects2 = plt.bar(index + bar_width, score_testing, bar_width,
alpha=opacity,
color='g',
label='Testing Scores')



plt.xlabel('Performance Metrics')
plt.ylabel('Scores')
plt.title('Performance Measure Graph')
plt.xticks(index + bar_width/2, ('Accuracy', 'Precision', 'Recall'))
plt.legend()

plt.tight_layout()
plt.show()