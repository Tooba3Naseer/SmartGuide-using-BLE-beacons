# Graph plotting for ANN

import numpy as np
import matplotlib.pyplot as plt
import math


# data to plot
n_groups = 3
score_training = ( 0.989553, 0.91, 0.91)
score_testing = (0.988442, 0.90, 0.90)

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


low = min(score_testing)
high = max(score_training)
plt.ylim([math.ceil(low-0.5*(high-low)), math.ceil(high+0.5*(high-low))])

plt.xlabel('Performance Metrics')
plt.ylabel('Scores')
plt.title('Performance Measure Graph')
plt.xticks(index + bar_width/2, ('Accuracy', 'Precision', 'Recall'))
plt.legend()

plt.tight_layout()
plt.show()