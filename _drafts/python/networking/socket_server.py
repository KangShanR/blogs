import socket


def socket_server(parameter_list):
    """
    docstring
    """
    print(parameter_list)
    s = socket.socket()
    host = "127.0.0.1"
    port = 8889
    s.bind((host, port))
    s.listen(5)

    while(True):
        c, addr = s.accept()
        print("Connect address: ", addr)
        c.send(b'Welcome, this is socket server')
        c.close()

if __name__ == '__main__':
    socket_server("socket server start...")