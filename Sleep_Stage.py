import copy
import random
import matplotlib.pyplot as plt
import math
from scipy.interpolate import splrep, splev
import timeit
import sys
import numpy as np
from scipy.signal import find_peaks
sys.setrecursionlimit(10**6)

def stage(RRs,State):
    State2 = []
    start = 0
    end = 0
    Post = 0
    for i in range(len(RRs)-1):
        if(i > 1 and RRs[i] > RRs[i+1] + 3  and RRs[i-1] + 3 < RRs[i]):
            State2.append(2);
        elif(i > 1 and RRs[i] < RRs[i+1] - 3  and RRs[i-1] - 3 > RRs[i]):
            State2.append(3);
        elif(RRs[i] > RRs[i+1]+3):
            State2.append(3);
        elif(RRs[i] > RRs[i+1]):
            State2.append(0);
        elif(RRs[i] < RRs[i+1]-3):
            State2.append(2);
        elif(RRs[i]+0.1  < RRs[i+1]):
            State2.append(1);
        elif(i > 1):
            State2.append(4);
        else:
            State2.append(1);
    Stack = 1;
    Stack2 = 0;
    for i in range(len(RRs)-1):
        if(Stack  == 1):
            State.append(90)
        else:
            State.append(0);
        if(State2[i] == 0 and Stack == 0):
            if(Stack2 != 0 ):
                Stack2 -= 1 ;
        elif(State2[i] == 0 and Stack == 1):
            if(Stack2 >= 1):
                Stack = 0
                Stack2 = 0
            else:
                Stack2+=1
        elif(State2[i] == 1 and Stack == 0):
            if(Stack2 >= 1):
                Stack = 1  
                Stack2 = 0
            else:
                Stack2+=1
        elif(State2[i] == 2):
            Stack2 = 0
            Stack = 1
        elif(State2[i] == 3):
            Stack2 = 0
            Stack = 0
        elif(State2[i] != 4):
            if(Stack2 != 0 ):
                Stack2 -= 1 ;

def BPM_Sleep_Stage(file_name):
	RRs = []
	State = []
	print("박우혁")
	r = open(file_name, mode='rt', encoding='utf-8')
	print("박우혁2")
	lines = r.readlines()
	r.close()
	L = []
	B = []
	S = []
	
	for i in range(len(lines)):
			if(len(lines[i]) != 1 and i > 5000):
				L.append((i/160,float(lines[i][15:])))
			else:
				L.append((i/160,0))
	r.close()
	L = np.asarray(L)
	x = range(0,len(L))
	y = [L[v][1] for v in x]
	peaks, _ =find_peaks(y,distance=40,height = 390,width=1)#h = 1.5
	np.diff(peaks)
	z = np.asarray(y)
	RRs.append((0,0))
	for i in range(len(peaks)):
		RRs.append((peaks[i]/160,(peaks[i]/160 - RRs[len(RRs)-1][0])))
	sum = 0
		
	
	RRs = np.asarray(RRs)
	fs = 160
	dt = 1/fs
	spl = splrep(RRs[:,0],RRs[:,1])
	sumsum = 0
	
	RRIy = []
	newt = np.arange(0,len(L)/160,dt)
	newx = splev(newt,spl)
	
	
	RRI = 0
	RRI_aver = 0
	RRI_sum = 0
	RRI_sum2 = []
	RRI_aver2 = 0
	RRIy = []
	
	for z in range((int)(len(newx)/48000) - 1):#46
		RRI_aver2 = 0
		for i in range(300):
			i += z * 300
			RRI = 0
			RRI_aver = 0
			RRI_sum = 0
			for j in range(4800):#3000
				RRI_aver += newx[(i*160+j)]
			RRI_aver /= 4800
			RRI_aver2 += RRI_aver
		RRI_sum2.append(int(60/(RRI_aver2/300)))
		print("LF/HF's Summary : %f\n" %(RRI_sum2[z]))
	RRI_sum2[0] = RRI_sum2[1]
	xlim = []
	for f in range(len(RRI_sum2)):
		xlim.append(f*5)
	plt.bar(xlim,RRI_sum2,width = 3)
	
	stage(RRI_sum2,State)
	RRI_sum2.append(len(State))
	RRI_sum2.append(len(RRI_sum2)-1)
	print("%f\n",RRI_sum2[len(RRI_sum2)-1]);
	return State, RRI_sum2