import os
import matplotlib.pyplot as plt
import matplotlib.image as img
import numpy as np
import pymysql

def mysql4login(message):
	conn = pymysql.connect(host='localhost', user='root', password='jimmy0', db='sleep', charset='utf8')

	curs = conn.cursor()

	new_buf = message[2:]

	data_arr = []

	for i in range(0, len(new_buf)):
		if new_buf[i] =="'":
			data_arr.append(i+1)

	email = new_buf[data_arr[0]:data_arr[1]-1]
	pw = new_buf[data_arr[2]:data_arr[3]-1]
	print(email)
	print(pw)
	sql = "select * from user where user_id = '"+email+"'"
	curs.execute(sql)
	row = curs.fetchall()

	if len(row) > 0:
		sql = "select * from user where password = '"+pw+"'"
		curs.execute(sql)
		row = curs.fetchall()

		if len(row)>0:
			conn.close()
			return 1
		else:
			conn.close()
			return 0
	else:
		conn.close()
		return 0