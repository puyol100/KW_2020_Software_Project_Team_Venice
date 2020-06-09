import librosa 
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
from sklearn.cluster import KMeans
from scipy.signal import find_peaks

def band_pass(signal,low_freq,high_freq):#D는 transpose하기 전 값이 들어감
	signal[:low_freq] = 0
	signal[high_freq:] = 0
	return signal

def find_pass_range(signal): #signal은 transpose한 D값이 들어가게 됨
	length = len(signal)
	temp_sig = np.copy(abs(signal))
	temp_max_arg = np.zeros(length)
	for i in range(0,length):
		temp_max_arg[i] = np.argmax(temp_sig[i])
	
	#-----clustring으로 잡음을 학습 시켜 찾아 줌  1번째 clustering한겨
	model = KMeans(n_clusters=2)
	df = pd.DataFrame(temp_max_arg)
	model.fit(df)
	y_predict = model.fit_predict(df)
	#----------------------------------
	
	idx = np.where(y_predict==0)[0]
	idx1 = np.where(y_predict==1)[0]	
	
	if len(idx) <= len(idx1):
		idx, idx1 = idx1, idx
	
	mean_freq = 0
	for i in range(0,len(idx)):
		mean_freq = mean_freq + temp_max_arg[idx[i]]
	
	mean_freq = mean_freq/len(idx)
	
	return int(mean_freq)


def a2b(file_name):
	y, sr = librosa.load(file_name)
	
	D = librosa.stft(y)#1025, 5168
	D = np.transpose(D) #5168 1025
	
	delete_d_idx = []
	for i in range(0,len(D)):
		if np.all(D[i] ==0):
			delete_d_idx.append(i) #1의 값을 가지고 있는 index는 퍽 소리 부분인겨 (문제가 있는 부분)
	
	if len(delete_d_idx)!=0:
		new_D = np.delete(D,delete_d_idx,axis=0)
		D = np.copy(new_D)
	
	D2 = np.copy(D)#5168 1025 D2 for real signal info\
	D = np.transpose(D)
	
	min_frq = find_pass_range(D2)
	D = band_pass(D,min_frq,50)
	D = np.transpose(D)#5168 1025  -->푸리에 변환 한 본 신호
	abs_D = np.copy(abs(D))
	length = len(abs_D)
	
	temp_arr = np.zeros(length)
	for i in range(0,length):
		temp_arr[i] = np.sum(abs_D[i],axis=0)#해당 시간의 모든 주파수 영역 값을 더한겨
	
	
	#-----clustring으로 잡음을 학습 시켜 찾아 줌  1번째 clustering한겨
	model = KMeans(n_clusters=2)
	df = pd.DataFrame(temp_arr)
	model.fit(df)
	y_predict = model.fit_predict(df)
	#----------------------------------
	
	
	idx = np.where(y_predict==0)[0] #얘는 그러면 잡음이라고 추정되는 frame의 index 값을 가지게 됨
	idx2 = np.where(y_predict==1)[0]
	
	temp_sum = 0
	for i in range(0,len(idx)):
		temp_sum = temp_arr[idx[i]]
	idx_mean = temp_sum/len(idx)
	temp_sum = 0
	for i in range(0,len(idx2)):
		temp_sum = temp_arr[idx2[i]]
	idx2_mean = temp_sum/len(idx2)
	
	if idx_mean >= idx2_mean: #두 clustring 군집화 중 작은 값들의 모임의 군집을 선택
		idx, idx2 = idx2, idx
	
	#----------------cepstrum for noise check
	log_temp = np.log(abs(D2))
	cepstrum = np.fft.ifft(log_temp)
	for i in range(0, len(cepstrum)):
		cepstrum[i]=np.where(cepstrum[i]!=np.min(cepstrum[i]),0, cepstrum[i])
	
	
	cepstral_signal = np.transpose(cepstrum)[0]
	cepstral_signal = abs(cepstral_signal + abs(np.min(cepstral_signal)))
	cepstral_signal.astype('float')
	
	
	new_ceps = np.zeros(len(idx))
	cepstral_max_arg = np.argmax(cepstral_signal)
	
	dict_new_temp_arr = {}
	for i in range(0,len(idx)):
		new_ceps[i] = cepstral_signal[idx[i]]#/np.max(cepstral_signal) 
		dict_new_temp_arr[i] = idx[i]
	
	
	
	'''
	잡음 추정 구간을 조사하게 되었고 
	그래서 캡스트럼과 해당 소리가 차지하는 비율을 비교하게 된거야.
	cepstrum이 말해주는 실제비율과 실제 값이 차지하는 비율을 뺏으니
	값이 작을수록 잡음에 가까운게 되겠지
	'''
	
	
	model2 = KMeans(n_clusters=2)
	df2 = pd.DataFrame(new_ceps)
	model2.fit(df2)
	y_predict2 = model2.fit_predict(df2)
	#-----model2는 쓰레스 홀드를 잡아 주기 위해 돌림
	
	idx_temp_temp1 = np.where(y_predict2==0)[0]
	
	idx_temp_temp2 = np.where(y_predict2==1)[0]
	
	idx_temp1_mean = 0
	idx_temp1_high = 0
	for i in range(0,len(idx_temp_temp1)):
		if idx_temp1_high < new_ceps[idx_temp_temp1[i]]:
			idx_temp1_high = new_ceps[idx_temp_temp1[i]]
		idx_temp1_mean = idx_temp1_mean + new_ceps[idx_temp_temp1[i]]
	
	idx_temp1_mean = idx_temp1_mean/len(idx_temp_temp1)
	
	idx_temp2_mean = 0
	idx_temp2_high = 0
	for i in range(0,len(idx_temp_temp2)):
		if idx_temp2_high < new_ceps[idx_temp_temp2[i]]:
			idx_temp2_high = new_ceps[idx_temp_temp2[i]]
		idx_temp2_mean = idx_temp2_mean + new_ceps[idx_temp_temp2[i]]
	
	idx_temp2_mean = idx_temp2_mean/len(idx_temp_temp2)
	
	
	if idx_temp1_mean > idx_temp2_mean:
		mean = (idx_temp2_mean + idx_temp2_high)/2 
	else:
		mean = (idx_temp1_mean + idx_temp1_high)/2
	
	
	arg = np.where(new_ceps < mean)[0] #여기에 진정한 잡음이 들어가게 됨.
	
	noise_arr = np.zeros(len(temp_arr))
	for i in range(0, len(arg)):
		nta_index = dict_new_temp_arr[arg[i]]
		temp_arr[nta_index] = 0
	
	#--------------------------------------------------
	
	snr_arr = np.where(temp_arr != 0, 1, temp_arr)
	
	
	smooth = []
	addi = 0
	
	for i in range(0, len(snr_arr)):
		if i % 43 == 0:
			smooth.append(addi)
			addi = 0
		addi = addi + snr_arr[i]
	
	smooth = np.array(smooth)
	
	
	smooth = np.where(smooth < 2, 0 , smooth)
	
	peaks, _ = find_peaks(smooth)
	
	return peaks, smooth

