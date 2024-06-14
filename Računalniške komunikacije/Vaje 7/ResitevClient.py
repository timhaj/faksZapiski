#!/usr/bin/python3

import socket as Socket
import os as Os
import threading as Threading
import datetime as Datetime
import json as Json
import time as Time
import sys as Sys
import re as Re
from typing import Dict



SOCKET_PORT = int(Os.getenv('PORT', 1234))
SOCKET_HOST = Os.getenv('HOST', "127.0.0.1")
TERM_COLS, TERM_ROWS = Os.get_terminal_size()

socket: Socket.socket = None
displayname: str = None



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



def receiver(socket: Socket.socket):
    rfile = socket.makefile()
    while True:
        try:
            raw = rfile.readline()
            if not raw:
                continue

            data: Dict = Json.loads(raw)
            if data.get("action") == "message" or data.get("action") == "dm":
                print("\033[%d;%dH[%12s%s %s" % (TERM_ROWS - 1, 0, data.get("from"), ">" if data.get("action") == "dm" else "]", data.get("data")), flush=True)
                print("[%12s] " % displayname, flush=True)
                print("\033[%d;%dH" % (TERM_ROWS - 1, 16), end='', flush=True)

        except Exception as e:
            print("Error")
            print(e)
            pass



def send(data: Dict):
    bdata = bytes(Json.dumps(data) + "\n", "UTF-8")
    socket.send(bdata)



def command(msg: str):
    cmd, *args = msg[1:].split(" ")
    if cmd == "help":
        print("""
\u001b[33mCommands\u001b[0m:
    \u001b[34m/help\u001b[0m
        Display this help menu

    \u001b[34m/msg\u001b[0m <\u001b[34msession\u001b[0m> <\u001b[34mcontent\u001b[0m>
        Send a direct message to a specific session
        <\u001b[34msession\u001b[0m> is the session identificator. You can get list of them with /list
        <\u001b[34mcontent\u001b[0m> is your message content
""")

    elif cmd == "msg":
        send({ "action": "dm", "to": args[0], "data": " ".join(args[1:]) })
    
    else:
        print("\u001b[31mERROR!\u001b[0m Invalid command. Try \u001b[34m/help\u001b[0m for a list of commands")

def defineDisplayname():
    global displayname
    try:
        while not displayname:
            print(chr(27) + "[2J")
            print("\033[%d;%dH" % (TERM_ROWS / 2, TERM_COLS / 2 - 30), flush=True, end="")
            vpis = input("Displayname ^[a-zA-Z0-9]{3,8}$: ")
            if Re.match("^[a-zA-Z0-9]{3,8}$", vpis):
                displayname = vpis
            print(chr(27) + "[2J")
    except KeyboardInterrupt:
        print()
        Sys.exit()


defineDisplayname()

while True:
    try:
        print("\033[%d;%dH" % (TERM_ROWS - 1, 0), flush=True)
        print("[      \u001b[34msystem\u001b[0m] Use \u001b[34m/help\u001b[0m for a list of commands")
        print("[      \u001b[34msystem\u001b[0m] connecting to chat server ... ", end="", flush=True)
        socket = Socket.socket(Socket.AF_INET, Socket.SOCK_STREAM)
        connected = False
        while not connected:
            try:
                socket.connect((SOCKET_HOST, SOCKET_PORT))
                connected = True
                send({ "action": "setname", "data": displayname })
            except Exception:
                False
            Time.sleep(1.0)
        print("\u001b[32mCONNECTED\u001b[0m!")

        thread = Threading.Thread(target=receiver, args=(socket,))
        thread.daemon = True
        thread.start()

        while True:
            try:
                print("[%12s] " % displayname)
                print("\033[%d;%dH" % (TERM_ROWS - 1, 16), end='', flush=True)
                vpis = input("")
                
                if not vpis:
                    print ("\033[A" + " " * TERM_COLS + "\033[A", flush=True)
                    continue
                
                if vpis.startswith("/"):
                    command(vpis)
                else:
                    send({ "action": "message", "data": vpis })
            except Exception as e:
                log("error", e)
                print("Something went wrong! Restarting...")
                break
    except KeyboardInterrupt:
        print()
        Sys.exit()
