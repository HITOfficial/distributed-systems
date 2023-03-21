import socket
import threading

SERVER_ADDRESS = 'localhost'
SERVER_PORT_TCP = 5000
SERVER_PORT_UDP = 5000
BUFFER_SIZE = 1024



def handle_tcp_receive(client_socket):
    while True:
        try:
            data = client_socket.recv(BUFFER_SIZE)
            if not data:
                break
            print(data.decode('utf-8'))
        except:
            break
    client_socket.close()


def handle_udp_receive(client_socket):
    while True:
        try:
            data, addr = client_socket.recvfrom(BUFFER_SIZE)
            if not data:
                break
            print(data.decode('utf-8'))
        except:
            break


def tcp_client():
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect((SERVER_ADDRESS, SERVER_PORT_TCP))

    threading.Thread(target=handle_tcp_receive, args=(client_socket,)).start()

    while True:
        message = input('> ')
        if message == 'U':
            udp_client()
        elif message == 'M':
            multicast_client()
        elif message == 'A':
            f = open("art.txt")
            client_socket.send(bytes(f.read(), "utf-8"))
            f.close()
        else:
            client_socket.sendall(message.encode('utf-8'))


def udp_client():
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    client_socket.sendto('[UDP]'.encode('utf-8'), (SERVER_ADDRESS, SERVER_PORT_UDP))

    threading.Thread(target=handle_udp_receive, args=(client_socket,)).start()


def multicast_client():
    multicast_group = '224.1.1.1'
    multicast_port = 5000

    client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
    client_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    client_socket.bind((SERVER_ADDRESS, multicast_port))
    client_socket.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, socket.inet_aton(multicast_group) + socket.inet_aton(SERVER_ADDRESS))

    threading.Thread(target=handle_udp_receive, args=(client_socket,)).start()


if __name__ == '__main__':
    tcp_client()