#!/usr/bin/python3

import socket as Socket
import os as Os
import threading as Threading
import datetime as Datetime
import json as Json
import time as Time

from typing import Dict, Set, TextIO



SOCKET_PORT = int(Os.getenv('PORT', 1234))
SOCKET_BIND = Os.getenv('BIND', "0.0.0.0")



def log (level: str, line: str, prefix: str = ""):
    colors = {
        "info": u"\u001b[34m",
        "success": u"\u001b[32m",
        "warn": u"\u001b[33m",
        "error": u"\u001b[31m",
        "log": u"\u001b[37m"
    }
    print(u"%s%-12s [%s%s\u001b[0m] %s" % (
        prefix,
        Datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S'),
        colors[level],
        level.upper().center(7),
        line
    ), flush=True)



class Client():

    socket: Socket.socket = None
    rfile: TextIO = None
    address = None
    name: str = None
    thread: Threading.Thread = None


    def __init__(self, socket: Socket.socket, address) -> None:
        self.socket = socket
        self.address = address
        self.rfile = socket.makefile()
        self.name = address[0] + ":" + str(address[1])
        self.thread = Threading.Thread(target=self.entrypoint)
        self.thread.daemon = True
        self.thread.start()


    def entrypoint (self):
        """
        The client thread entrypoint
        """
        log("log", "[client] %s connected " % self.name)
        emptyCount = 0
        while True:
            try:
                content = self.rfile.readline()
                if not content:
                    emptyCount += 1
                    if emptyCount > 10: break
                    continue
                emptyCount = 0
                data: Dict = Json.loads(content)
                
                if (data.get('action') == 'setname'):
                    self.name = data.get("data")

                if (data.get('action') == 'message'):
                    for client in clients:
                        if client.name != self.name:
                            client.send({ "action": "message", "from": self.name, "data": data.get('data') })

                if (data.get('action') == 'dm'):
                    print(data.get('to'))
                    print(data.get('data'))
                    for client in clients:
                        if client.name == data.get('to'):
                            client.send({ "action": "dm", "from": self.name, "data": data.get('data') })


            except:
                pass
        self.close()


    def send(self, data: Dict):
        """
        Send data to client
        """
        bdata = bytes(Json.dumps(data) + "\n", "UTF-8")
        self.socket.send(bdata)


    def close(self):
        """
        Gracefully close the client socket
        """
        with server_lock:
            log("log", "[client] %s closing ..." % self.name)
            self.socket.close()
            clients.remove(self)
            log("warn", "[client] %s closed" % self.name)



log("info", "[system] Starting server on port %d ..." % SOCKET_PORT)
clients: Set[Client] = set()
server_lock = Threading.Lock()

server_socket = Socket.socket(Socket.AF_INET, Socket.SOCK_STREAM)
server_socket.bind((SOCKET_BIND, SOCKET_PORT))
server_socket.listen(10)
log("success", "[system] Started server on port %d ..." % SOCKET_PORT)



log("info", "[system] Starting healthchecker")
def healthchecker():
    while True:
        for client in list(clients):
            try:
                client.send({"action": "healthcheck"})
                # client.send({"action": "message", "sender": "server", "content": "healthcheck!"})
            except: pass
        Time.sleep(5)

healthchecker_thread = Threading.Thread(target=healthchecker)
healthchecker_thread.daemon = True
healthchecker_thread.start()
log("success", "[system] Started healthchecker")



while True:
    try:
        socket, address = server_socket.accept()

        with server_lock:
            client = Client(socket, address)
            clients.add(client)

    except KeyboardInterrupt:
        break
    except:
        pass



log("warn", "[system] Closing server ...", "\n")
for client in list(clients):
    client.close()

server_socket.close()
log("info", "[system] Server closed")

