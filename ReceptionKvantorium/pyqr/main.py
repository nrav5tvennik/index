import pyqrcode
import png
from pyqrcode import QRCode
import socket
import os
from ioconnection import IOConnection

NAME_PNG = "qr.png"

server_socket = socket.socket()
server_socket.bind(("", 5001))
server_socket.listen()
client_socket, _ = server_socket.accept()

io_connection = IOConnection(client_socket)
data = io_connection.read_message()
url = pyqrcode.create(data)
url.png(NAME_PNG, scale=6)
io_connection.write_msg(os.getcwd() + "\\" + NAME_PNG)
client_socket.close()
server_socket.close()
