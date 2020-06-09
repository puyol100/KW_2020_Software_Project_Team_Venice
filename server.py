# server.py : 연결해 1 보낼 수 있음.
import socket
import Manager #무호흡 계산을 위한 놈
import pcm2wav
import mysqlprac as msp
import mysql4login as m4log
import Apnea_dB
import os
from datetime import datetime
#import Sleep_Stage

def get_email(message):
   new_buf = message[2:]
   data_arr = []
   for i in range(0, len(new_buf)):
      if new_buf[i] =="'":
         data_arr.append(i+1)
   email = new_buf[data_arr[0]:data_arr[1]-1]
   return str(email)
   

def decode_rcv_message(v):
   temp = v.decode()
   val = ""
   if len(temp) >= 2:
      for i in range(2,len(temp)):
         val += temp[i]
   return val


def make_apnea_info_send_message(v):
   print("한성민")
   print(v)
   print(type(v))
   temp = v.replace("]","")
   temp = temp.replace("[","")
   temp = temp.replace("\\n","+")
   temp= temp.replace(" ","+")
   temp = temp.replace("\'","")
   print(temp)
   val = temp.encode()
   print(val)
   
   return val

def make_Login_info_send_message(v):
   if v >= 1:
      val = bytearray(b'\x00\x011')
   else:
      val = (b'\x00\x010')
   return val
   

host = '192.168.0.52' # 호스트 ip를 적어주세요
port = 9998

#소켓 생성, IPv4, TCP
server_sock = socket.socket(socket.AF_INET,socket.SOCK_STREAM)

#WinError 10048해결을위한(포트 사용중이라 연결할수없음) -->더 알아보기
#server_sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR,1)

server_sock.bind((host, port))
server_sock.listen()

print("기다리는 중")


now = datetime.now()
now_path = str(now.year)+'-'+str(now.month)+'-'+str(now.day)

count = 1
data = b''
email = ""
apnea_result = [0,0,0,0,1]
while True:
   client_sock, addr = server_sock.accept()
   newbuf = client_sock.recv(17620)
   print("연결은 됐고")
   if not newbuf:
      break
   if newbuf[2] == 83:
      #val = Manager.Manager()
      message = newbuf
      date = message.decode('utf-8')[3:]
      print("Search")
      #val = Apnea_dB.Find_dB_value(email,date);
      send1, send2, send3 = Apnea_dB.Find_dB_value(email,date)
      val = str(send1) + str(send2) + str(send3)
      print("왜")
      if send1 == 999:
        print("왜2")
        send=make_Login_info_send_message(1)
        client_sock.sendall(send)
      else:
        print("왜3")
        print(val)
        send = make_apnea_info_send_message(val)
        length_send = len(send)
        client_sock.sendall(length_send.to_bytes(4,byteorder="big"))
        client_sock.sendall(send)
    
      client_sock.close()
      data = b''
      continue
   elif newbuf[2] == 65:
      message = newbuf
      date = message.decode('utf-8')[3:]
      
      send1, send2, send3 = Apnea_dB.Find_dB_value(email,date)
      val = str(send1) + str(send2) + str(send3)
      print("왜")
      if send1 == 999:
        print("왜2")
        send=make_Login_info_send_message(1)
        client_sock.sendall(send)
      else:
        print("왜3")
        print(val)
        send = make_apnea_info_send_message(val)
        print('print send', send)
        length_send = len(send)
        client_sock.sendall(length_send.to_bytes(4,byteorder="big"))
        client_sock.sendall(send)
    
      client_sock.close()
      data = b''
      continue

   elif newbuf[2] == 82:
      print("Register")
      message = newbuf
      if msp.mysqlcon(message.decode('utf-8')) == 1:
         send = make_Login_info_send_message(1)
         client_sock.sendall(send)
      else:
         send = make_Login_info_send_message(0)
         client_sock.sendall(send)
      client_sock.close()
      data = b''
      continue
   elif newbuf[2] == 76:
      print("login!")
      message = newbuf
      if m4log.mysql4login(message.decode('utf-8')) == 1:
         send = make_Login_info_send_message(1)
         client_sock.sendall(send)
         print("login success!!")
         email = get_email(message.decode('utf-8'))
      else:
         send = make_Login_info_send_message(0)
         client_sock.sendall(send)
      client_sock.close()
      
      #---------------------------------------
      make_dir = './' + email +'/'+ now_path
      if os.path.isdir(make_dir)== False:
         os.mkdir(str(make_dir))
      #---------------------------------------
      
      data = b''
      continue
   elif newbuf[2] == 71:#G -->graph
      message = newbuf 
      date = message.decode('utf-8')[3:]
      csv_name = email+'/'+date+'.csv'
      try:
        file = open(csv_name,'rb')
        file_size = os.path.getsize(csv_name)
        csv_file = file.read(file_size)
        print('file tpye:', type(csv_file))
        print(csv_file)
        client_sock.sendall(csv_file)
        file.close()
      except OSError:
        send = make_Login_info_send_message(1)
        client_sock.sendall(send)
        print('no such file', csv_name)

      client_sock.close()
      print("send csv!")
      data = b''
   else: #Max_dB = apnea_result[2]
      while True:
         print("Recording")
         newbuf = client_sock.recv(17620)
         if not newbuf:
             client_sock.close()
             print("Insert_Apnea_Info")
             if count > 1:
                apnea_result[3] = apnea_result[3]/(count-1)
             Apnea_dB.Insert_dB_value(apnea_result,email)
             data = b''
             #apnea_result = [0,0,0,0]
             break
         elif newbuf[2] == 78:
            print("여기 왔고")
            f_name = './' + email +'/'+ now_path +'/'+ str(count) +'.wav' #로그인 한 유저의 디렉토리 밑에 모든 결과 있음
            pcm2wav.pcm2wav(data,f_name,1,16,44100)
            apnea_result = Manager.Manager(f_name,apnea_result,email,count)
            count = count + 1
            data = b''            
         else :
            data += newbuf      

      file_temp = './'+email+'/'+now_path+'/'+str(apnea_result[4])+'.wav'
      Manager.Max_period(file_temp, apnea_result[2], email)
      apnea_result = [0,0,0,0,0]  
    # file_name = max_dB가 갖고 있는 file name   ==>apnea_result[4]  ==> file_temp
    # Max_dB = 수면 시간 중 최고 코골이 소리          ==>apnea_result[2]
    # email = 사용자 id / 사용자 폴더를 찾기 위해 
    # date = 사용자 id 폴더 아래 날짜 폴더 안에 코골이 데시벨 csv 파일을 위치 시킬꺼
   count = 1
   
      
client_sock.close()
server_sock.close()