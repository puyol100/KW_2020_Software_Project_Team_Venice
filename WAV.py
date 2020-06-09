import librosa 
import librosa
import librosa.display
import IPython.display #5161
import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import matplotlib.font_manager as fm
import pandas as pd
import wave
from scipy.signal import butter, lfilter
from sklearn.cluster import KMeans
import seaborn as sns
import soundfile as sf
from scipy.signal import find_peaks
import audio2binary

def find_fake_apnea_by_real_sound(apnea_idx,real_smooth):
	
	delete_count = 0
	flag = False
	for i in range(0,len(apnea_idx)):
		if apnea_idx[i] + 4 <= len(real_smooth):
			for j in range(apnea_idx[i]+1,apnea_idx[i] + 9):
				if real_smooth[j] == 0:
					flag = False
					break
				else:
					flag = True
			if flag == False:
				print('fake apnea: ',apnea_idx[i])
				delete_count += 1
		flag = False
	
	return delete_count	

def DeTect(n, file_name):
	y, sr = librosa.load(file_name)
	a2b_peaks, a2b_smooth = audio2binary.a2b(file_name)

	#sr = 22050
	print('y shape:',y.shape)
	print('S shape:',sr)	
	
	#컨셉은 이제 소리의 크기로는 힘드니까 주파수의 변동성을 보자.
	
	D = librosa.feature.melspectrogram(y) #128 5168
	new_D = D[20:40] #(hz,5168) #make band pass filter 하모닉 시그널을 이용할꺼야
	
	S_db = librosa.amplitude_to_db(new_D,ref=0.00002) #(128,5168) 지가 stft까지 된거고
			
	
	for i in range(0,len(S_db)):
		S_db[i] = np.where(S_db[i] <=25,0,S_db[i])#이건 우리도 안들린거니까 맞아. 
	
	
	sig = np.transpose(S_db)#5168 128
	sig_frq_count = []#각 frame별 0이 아닌 주파수를 가진 개수
	sig_db = []#각 frame 별 max db를 가지고 있음
	temp_smooth = []
	for i in range(0,len(sig)):
		temp_smooth.append(np.argmax(sig[i]))
		sig_frq_count.append(len(np.where(sig[i]!=0)[0]))
		sig_db.append(np.max(sig[i]))
	
	temp_smooth = np.array(temp_smooth)#주파수의 main대역을 가지고 있겠지
	
	sig_frq_count = np.array(sig_frq_count)
	sig_db = np.array(sig_db)
	
	#잡아줘야하는건 뭉탱이랑 주파수 대역이 그 순간에 다양하겠지 칵 푸등등 여러 소리로 내니까
	
		
	
	APNI = []#smoothing한 시그널 값
	APNI_db = []#1초당 max db
	for i in range(1,int(len(temp_smooth)/43.078)):
		if len(np.where(temp_smooth[(i-1)*43 :i*43]!=0)[0]) > 2: 
			APNI.append(np.max(temp_smooth[(i-1)*43 :i*43]))
			APNI_db.append(np.max(sig_db[(i-1)*43 :i*43]))
		else:
			APNI.append(0)
			APNI_db.append(0)
		
	

	#-----------------------------------------------괴랄한 패턴에 대한 apnea
	apnea_idx=[] #무호흡 구간을 저장하고 있는 array
	apnea_by_frq = [] #frq를 조졌을때 나오는 apnea index 저장
	count = 0
	apnea_count = 0 #apnea_count by frq
	for i in range(0,len(APNI)):
		if APNI[i] == 0:
			if count >= 9 and count <= 15:
				apnea_idx.append(i-count) #괴랄한 패턴에 대한 apnea index 저장
				apnea_by_frq.append(i-count) # 나중에 다시 조사를 위해 일단 리스트에 저장
				apnea_count += 1		
			count = 0	
		else:
			count = count + 1
	#-----------------------------------------------------------------------

	
	#------------------------------------------------기존 수면 협회에 의한 패턴 apnea
	peaks_db, _=find_peaks(APNI_db)
	apnea_count2 = 0 #apnea_count2
	for i in range(0,len(peaks_db)):
		if i ==0:
			term = peaks_db[i] - 0
		else:
			term = peaks_db[i] - peaks_db[i-1]
		if term >= 20 and term <= 60:
			if APNI[peaks_db[i-1]-1] != 0:
				if peaks_db[i] + 2 != len(APNI):#파일 끝일 경우 방지 해주고 
					if APNI[peaks_db[i] + 1] != 0 and APNI[peaks_db[i] + 2]!=0:#샤프한거 방지해줌
						flag = 0#0이면 겹치는거 없고, 1이면 겹침
						for j in range(0,len(apnea_idx)):#겹치는 판단 방지
							temp = peaks_db[i] - apnea_idx[j]
							if temp < 0 : 
								temp = temp * (-1)
							if temp < 15: 
								flag = 1 
								break
						if flag == 0:
							apnea_idx.append(APNI[peaks_db[i]]) # 20~ 60 패턴에 대한 Apnea 저장
							apnea_count2 = apnea_count2 +1
			#파일 끝일 경우는 모르지 그 다음 파일이랑 엮어 봐여해
		
	#--------------------------------------------------------------------------------
	
	
	print('apnea_count by db: ',apnea_count2)
	
	
	print('apnea_count by frq:',apnea_count)
	total = apnea_count + apnea_count2
	print('total apnea: ',total)
	
	print('print the final apnea idx:')
	print(apnea_idx)
	
	print('a2b_peaks:')
	print(a2b_peaks)
	
	
	#apnea_by_frq
	val = find_fake_apnea_by_real_sound(apnea_by_frq,a2b_smooth)
	print('real apnea: ',total - val)
	apnea_cnt = total - val

	
	db_smooth = []
	db_add = 0

	for i in range(0, len(y)):
		if i % sr == 0:
			db_smooth.append(db_add/sr)
			db_add = 0
		db_add = db_add + abs(y[i])

	db_smooth = np.array(db_smooth)
	db = librosa.amplitude_to_db(db_smooth, ref = 0.00002)

	max_db = round(np.max(db))
	mean_db = round(np.mean(db))

	result_arr = np.zeros(4)

	if apnea_cnt >= 5:
		result_arr[0] = 1
	else:
		result_arr[0] = 0

	result_arr[1] = max_db
	result_arr[2] = mean_db
	result_arr[3] = apnea_cnt

	return result_arr
	
	#------------------------

