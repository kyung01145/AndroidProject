import json
import requests
import socket
import collections
import threading
import time

host = '192.168.0.2'  # 호스트 ip를 적어주세요
port = 10000  # 포트번호를 임의로 설정해주세요
api_key='RGAPI-64a0cfd9-ece7-429e-a3ff-247088c22265'
region='kr'
id=''

"""유저정보"""
users_nickname=[]
locations=collections.defaultdict(list)
users_accountid=collections.defaultdict(str)
users_gametimes=collections.defaultdict(list)
users_summonerid=collections.defaultdict(str)
users_watching=collections.defaultdict(int)
"""유저정보"""

server_sock = socket.socket(socket.AF_INET)
server_sock.bind((host, port))
server_sock.listen()

def update():
    while(True):
        current_time = time.localtime(time.time())
        today=current_time.tm_wday
        user_set=set(users_nickname)
        for nickname in user_set:
            accountid = users_accountid[nickname]
            URL = "https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/"+accountid+"?endIndex=30&beginIndex=0"
            res = requests.get(URL, headers={"X-Riot-Token": api_key})
            if res.status_code == 200:
                matches = json.loads(res.text)
                gameIds=[]
                for match in matches['matches']:
                    sec=match['timestamp']/1000
                    millisec=match['timestamp']%1000
                    millisec/=1000.0
                    matchtime=time.localtime(sec+millisec)
                    if(matchtime.tm_wday==today):
                       gameIds.append(match['gameId'])
                    else:
                        break
                sum=0
                for gameid in gameIds:
                    URL = "https://kr.api.riotgames.com/lol/match/v4/matches/"+str(gameid)
                    res = requests.get(URL, headers={"X-Riot-Token": api_key})
                    if res.status_code == 200:
                        match = json.loads(res.text)
                        sum+=match['gameDuration']

                print(nickname)
                users_gametimes[nickname][today]=sum//60
                print(users_gametimes[nickname])
        time.sleep(10)

def main_process(client_sock):
    data = client_sock.recv(1024)[2:]
    data = data.decode("utf-8")
    data = data.split(',')
    print(data)
    if (data[0] == "register"):
        nickname = data[1]
        URL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nickname
        res = requests.get(URL, headers={"X-Riot-Token": api_key})
        if res.status_code == 200:
            user_json = json.loads(res.text)
            users_accountid[nickname] = user_json['accountId']
            if(len(users_gametimes[nickname])==0):
                users_gametimes[nickname] = [0] * 7
            users_nickname.append(nickname)
            users_summonerid[nickname] = user_json['id']
    elif (data[0] == 'gettime'):
        nickname = data[1]
        times = ''
        for i in users_gametimes[nickname]:
            times += str(i)
            times += ","
        n = 0
        times = times.rstrip()
        head = n.to_bytes(1, byteorder='little') + len(times).to_bytes(1, byteorder='little')
        times = head.decode('utf-8') + times
        client_sock.send(times.encode('utf-8'))
    elif (data[0] == "watch"):
        print("감시중")
        nickname=data[1]
        id = users_summonerid[nickname]
        URL = "https://kr.api.riotgames.com/lol/spectator/v4/active-games/by-summoner/" + id
        res = requests.get(URL, headers={"X-Riot-Token": api_key})
        if(users_watching[nickname]):
            users_watching[nickname]=0
            num = 2
            client_sock.send(num.to_bytes(4, byteorder='little'))
        elif (res.status_code == 200):
            num = 1
            client_sock.send(num.to_bytes(4, byteorder='little'))
        else:
            num = 0
            client_sock.send(num.to_bytes(4, byteorder='little'))
    elif (data[0] == "watch_end"):
        nickname=data[1]
        users_watching[nickname]=1

    elif (data[0] == "delete"):
        nickname=data[1]
        users_nickname.remove(nickname)
    elif (data[0] == "baby"):
        nickname=data[1]
        latitude=data[2]
        longitude=data[3]
        if(latitude!='0.0'):
            locations[nickname]=[latitude,longitude]
        print(locations[nickname])

    elif (data[0] == "getgps"):
        nickname=data[1]
        print(locations[nickname])
        location=locations[nickname][0]+","+locations[nickname][1]
        n = 0
        head = n.to_bytes(1, byteorder='little') + len(location).to_bytes(1, byteorder='little')
        location = head.decode('utf-8') + location
        client_sock.send(location.encode('utf-8'))
    client_sock.close()


update_thread=threading.Thread(target=update)
update_thread.start()

while (True):
    print("기다리는 중")
    client_sock, addr = server_sock.accept()
    main_thread = threading.Thread(target=main_process, args=(client_sock,))
    main_thread.start()




server_sock.close()




