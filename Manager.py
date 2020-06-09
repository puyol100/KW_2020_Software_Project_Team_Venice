import WAV
import numpy as np
import matplotlib.pyplot as plt
import numpy as np
import wave
import sys
import librosa
from pydub import AudioSegment
import pandas as pd
from datetime import datetime
import os  

# result[0] = 무호흡 환자 진단 카운팅 (수면 중 무호흡이라고 판단 된 횟수)
# result[1] = 가장 큰 소음 dB
# result[2] = 평균 소음 dB
# result[3] = 수면중 무호흡 횟수

def Manage(file_name): #c => count, c_a = count_apnea
	count = 0
	count_apnea = 0
	#file = 'out_tests.wav'
	file = file_name
	print(file)
	#result = WAV.DeTect(n,str)
	temp = WAV.DeTect(2,file)
	result = [0,0,0,0]
	result[0] = temp[0] # 무호흡 환자 진단 카운트
	result[1] = temp[3] # 무호흡 횟수 카운트
	result[2] = temp[1] #가장 큰 소음 dB
	result[3] = temp[2] #평균 소음 dB
	return result
	
def Max_period(file_name, Max_dB, email):

	y, sr = librosa.load(file_name)
	framerate = 22050

	abs_y = abs(y)
	temp_Time = []
	temp = 0
	time = int(len(abs_y)/framerate) #음성 input 파일의 총 시간 구함
	
	for i in range(0,time): #44100으로 sampling된 파일 1초 단위로 만들기
		for j in range(0,framerate):
			temp = temp + abs_y[i*framerate + j]
		temp_Time.append(temp/framerate)
		temp = 0

	mag = librosa.amplitude_to_db(np.array(temp_Time), ref=0.00002) #진폭 값 데시벨로 만들어 줌
	mag = np.around(mag)
	
	
	max = np.where(mag==Max_dB)
	max_term = max[0][0]

	###### 여기 부분은 수정이 필요함 #####
	
	# 지금은 현재 시간만으로 갖고 오는데 시간을 input 받아 갖고 올 수 있게 해야함
	now = datetime.now()
	date = str(now.year)+'-'+str(now.month)+'-'+str(now.day)

	################################

	if max_term-60 < 0 and max_term+60 > len(mag):
		data = [range(0,len(mag)), mag]
	elif max_term-60 < 0 and max_term+60 < len(mag):
		data = [range(0,max_term+60), mag[0:max_term+60]]
	elif max_term+60>len(mag) and max_term-60 > 0:
		data = [range(max_term-60,len(mag)),mag[max_term-60:]]
	else:
		data = [range(max_term-60,max_term+60),mag[max_term-60:max_term+60]]
	
	dataframe = pd.DataFrame(data)

	######### Max_dB가 있는 구간의 파형 csv 파일 이름 ##########
	# {user_id}/{date}.csv
	
	csv_file_name = email+'/'+date+'.csv'

	#####################################################

	try:
		if not(os.path.isdir(email)):
			os.makedirs(os.path.join(email))
	except OSError as e:
		if e.errno != errno.EEXIST:
			print("Failed to create directory!!!!!")
			raise

	dataframe.to_csv(csv_file_name,header=False, index=False)

def Manager(file_name, val,email,file_num):	
	count = val[0]
	count_apnea = val[1]
	Max_dB = val[2]
	Mean_dB = val[3]
	Max_dB_Time = 0
	value = [0,0,0,0,0] 
	value[4] = val[4]
	'''
	value[0] = 수면중 무호흡 횟수 count_apnea
	value[1] = 현재 무호흡 상태 ==>무호흡 환자인지 아닌지 1>= 무호흡 환자, 그외 정상
	value[2] = Max dB
	value[3] = Mean_dB
	value[4] = Max dB file_name
	'''

	result = Manage(file_name)
	count = count + result[0]
	count_apnea = count_apnea + result[1]
	if Max_dB > result[2] or Max_dB == 0:
		Max_dB = result[2]
		value[4] = file_num 
	Mean_dB = Mean_dB + result[3]

	
	
	
	#Max_period(file_name, Max_dB, email)	#######
	# file_name = max_dB가 갖고 있는 file name
	# Max_dB = 수면 시간 중 최고 코골이 소리 
	# email = 사용자 id / 사용자 폴더를 찾기 위해 
	# date = 사용자 id 폴더 아래 날짜 폴더 안에 코골이 데시벨 csv 파일을 위치 시킬꺼


	value[0] = count_apnea
	value[1] = count
	value[2] = Max_dB
	value[3] = Mean_dB
	
	return value 

#Manager("out_tests.wav",[0,0,0,0,0], "jimmy3663", 2)