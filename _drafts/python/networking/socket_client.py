import socket

def socket_client(parameter_list):
    """
    docstring
    """
    c = socket.socket()
    c.connect(('127.0.0.1', 8889))
    print('This is server, accept message:%s, from: %d' %(c.recv(1024), parameter_list))
    c.close()

if __name__ == '__main__':
    for i in range(10):
        socket_client(i)