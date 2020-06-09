import pymysql
import os
import matplotlib.pyplot as plt
import matplotlib.image as img
import numpy as np
import Apnea_dB 
import pcm2wav
from datetime import datetime
import Sleep_Stage

'''
now = datetime.now()
print("now date and time : " + str(now))
print("now year : " + str(now.year))
print("now month : " + str(now.month))
print("now day : " + str(now.day))
print("now hour : " + str(now.hour))
print("now min : " + str(now.minute))
print("now second : " + str(now.second))
print("now date : {}-{}-{}".format(now.year, now.month, now.day))
'''



def Find_dB_value(email,date):
	#f_path = './' + email + '/Apnea_2020_02_10.txt'
	print('email: ',email)
	print('date: ',date)
	#value = []
	try:
		f = open(email+'/'+date+'/'+'Apnea_'+date+'.txt',mode='rt',encoding='utf-8')
		
		temp = f.readline()
		temp = temp.replace(",","")
		temp = temp[0:len(temp)-1]
		print('ses')
		print(type(temp))
		temp2 = f.readline()
		temp2 = temp2.replace(",","")
		print('ses1')
		print(type(temp2))
		temp3 = f.readline()
		temp3 = temp3.replace(",","")
		print('ses2')
		print(type(temp3))
		
		f.close()
		
		value1 = temp.split(', ')
		value2 = temp2.split(', ')
		value3 = temp3.split(', ')
		
		return value1, value2, value3
	except OSError:
		return 999, 999, 999
def Insert_dB_value(value,email):
	'''
	value[0] = count_apnea
	value[1] = count
	value[2] = Max_dB
	value[3] = Mean_dB
	'''
	'''
	conn = pymysql.connect(host='localhost', user='root', password='abc1234!@#$', db='sleep', charset='utf8')
	curs = conn.cursor()
	sql = "select audio_path from sleep_status where id = 'test1@kw.ac.kr'";
	curs.execute(sql)
	conn.commit()
	row = curs.fetchall()
	path = row.index('test1@kw.ac.kr')
	conn.close()
	'''
	now = datetime.now()
	now_path = str(now.year)+'-'+str(now.month)+'-'+str(now.day)

	f_path = './' + email + '/' +now_path +'/'+'Apnea_'+ now_path +'.txt'
	bpm, stage = Sleep_Stage.BPM_Sleep_Stage('ECG2.txt')#data
	
	f = open(f_path,mode='wt',encoding='utf-8')
	for i in range(0,4):
		temp = str(int(value[i]))+' '
		f.write(temp)
	f.write('\n')
	f.write(str(bpm))
	f.write('\n')
	f.write(str(stage))
	f.close()
	