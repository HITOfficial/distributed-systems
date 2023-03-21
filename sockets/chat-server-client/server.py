import socket
import threading

def handle_client_connection(client_socket, address):
    print(f"New connection from {address}")
    while True:
        message = client_socket.recv(1024).decode()
        if not message:
            break
        print(f"Received message from {address}: {message}")
        for c in clients:
            if c != client_socket:
                c.send(f"{address[0]}: {message}".encode())
    print(f"Connection closed with {address}")
    client_socket.close()

def handle_udp():
    udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    udp_socket.bind((HOST, PORT))
    while True:
        message, address = udp_socket.recvfrom(1024)
        print(f"Received UDP message from {address}")
        for c in clients:
            if c != udp_socket:
                c.send(f"{address[0]} [UDP]: {message.decode()}".encode())

HOST = "localhost"
PORT = 5000

tcp_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
tcp_socket.bind((HOST, PORT))
tcp_socket.listen(5)

clients = []

udp_thread = threading.Thread(target=handle_udp)
udp_thread.start()

print(f"Server listening on {HOST}:{PORT}")

while True:
    client_socket, address = tcp_socket.accept()
    clients.append(client_socket)
    client_thread = threading.Thread(target=handle_client_connection, args=(client_socket, address))
    client_thread.start()
