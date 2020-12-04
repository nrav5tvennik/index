
class IOConnection:
    def __init__(self, client_socket):
        self.client_socket = client_socket
        self.BUFFER_SIZE = 1024

    def write_msg(self, message):
        self.client_socket.send(bytes(message, "utf-8"))

    def read_message(self):
        all_data = list()

        while True:
            if len(all_data) != 0 and len(all_data[-1]) < self.BUFFER_SIZE:
                break
            data = self.client_socket.recv(self.BUFFER_SIZE)
            if not data:
                break
            all_data.append(data)

        return "".join([i.decode("utf-8") for i in all_data])
