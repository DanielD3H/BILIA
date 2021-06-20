import modelBuilder
import socket
from _thread import *
import threading


# thread function
def threaded(soc):
    while True:

        # data received from client
        data = (soc.recv(1024)).decode()
        if not data:
            print('Bye')
            break
        else:
            print(data)
            result = modelBuilder.translate(data).replace(" <end>", "")
            soc.send((result+'\n').encode())
    # connection closed
    soc.close()


def main():
    host = '0.0.0.0'

    port = 1234
    listen_soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    listen_soc.bind((host, port))
    print("socket binded to port", port)

    # put the socket into listening mode
    listen_soc.listen(5)
    print("socket is listening")

    # a forever loop until client wants to exit
    while True:
        # establish connection with client
        soc, addr = listen_soc.accept()


        print('Connected to :', addr[0], ':', addr[1])

        # Start a new thread and return its identifier
        start_new_thread(threaded, (soc,))
    listen_soc.close()


if __name__ == '__main__':
    main()