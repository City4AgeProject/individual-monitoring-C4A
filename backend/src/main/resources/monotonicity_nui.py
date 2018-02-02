
# coding: utf-8

# In[288]:


import numpy as np
from matplotlib import cm, pyplot as plt
from scipy import stats


# In[289]:


# Plotting

def plot_single_series(data):
    colours = cm.rainbow(np.linspace(0, 1, 1))
    measures = data
    dates = np.array(range(1, 32))
    plt.figure(num=None, figsize=(12, 8), dpi=80, facecolor='w', edgecolor='k')
    plt.plot(dates, measures, '.-')
    plt.grid(b=None)
    plt.show()
    


# In[290]:


# spearman correlation coefficient computation

# nui computation


# In[291]:


# data = np.array()
# data = (np.random.rand(1,31))*10
data = np.array([1.4601329403661678, 3.9343618387275514, 3.5132181566343035, 3.0975598634818349, 4.9179544791004224, 4.0096633195994418, 5.011483058433269, 5.6200033290018947, 3.7573206362823752, 4.184359195562644, 9.1154788305773096, 7.8567885842127039, 3.77678118158567067, 6.2926034127910118, 5.8670148966115523, 7.5189650607478082, 8.282487780604189, 6.136904984545076, 3.025843808527898, 3.6350189874118897, 8.9456365092115995, 8.3665326156679249, 4.003397048296339, 3.5266061684359817, 6.0699179911010246, 8.7293603204586052, 6.0214022444499538, 7.1626150247297371, 7.829305976646463, 4.9607100229724939, 7.2413017636768835])*2
# plot_single_series(data)

# import csv
# ofile  = open('ttest.csv', "wb")
# ofile.write(data)
# np.savetxt("foo.csv", data, delimiter=",")

reversed_data = np.array([i for i in reversed(data)])
plot_single_series(reversed_data)

np.savetxt("foo_rev.csv", reversed_data, delimiter = ",")

print (data.mean())
print (reversed_data.mean())

print (data.std())
print (reversed_data.std())

print (np.percentile(data, 25))
print (np.percentile(reversed_data, 25))

print ((np.percentile(data, 25) - data.mean())/data.mean())
print ((np.percentile(reversed_data, 25) - reversed_data.mean())/reversed_data.mean())

dates = np.array(range(1, 32))

print ("spearman's coefficients")
print (stats.spearmanr(dates, data))
print (stats.spearmanr(dates, reversed_data))


# In[292]:


data_1 = np.array([0.10000100,0.10000200,0.10003000,0.10000400,0.10000500,0.10000600,0.10000700,0.10000800,0.10000900,0.10001000,0.10001100,0.10001200,0.10001300,0.10001400,0.10001500,0.10001600,0.10001700,0.10001800,0.10001900,0.10002000,0.10002100,0.10002200,0.10002300,0.10002400,0.10002500,0.10002600,10.00002700,10.00002800,10.00002900,10.00003000,0.10003100])

data_2 = np.array([0.10000100,0.10000200,0.10000300,0.10000400,0.10000500,0.10000600,10.00000700,0.10000800,0.10000900,0.10001000,0.10001100,0.10001200,0.10001300,10.00001400,0.10001500,0.10001600,0.10001700,0.10001800,0.10001900,0.10002000,10.00002100,0.10002200,0.10002300,0.10002400,0.10002500,0.10002600,0.10002700,10.00002800,0.10002900,0.10003000,0.10003100])

# plot_single_series(data_1)
# plot_single_series(data_2)

print (data_1.mean())
print (data_2.mean())

print (data_1.std())
print (data_2.std())

print (np.percentile(data_1, 25))
print (np.percentile(data_2, 25))

print ((np.percentile(data_1, 25) - data_1.mean())/data_1.mean())
print ((np.percentile(data_2, 25) - data_2.mean())/data_2.mean())


dates = np.array(range(1, 32))
print ("spearman's coefficients")
print (stats.spearmanr(dates, data_1))
print (stats.spearmanr(dates, data_2))

