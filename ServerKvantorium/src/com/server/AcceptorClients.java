package com.server;

import java.io.IOException;
import java.net.ServerSocket;

class AcceptorClients implements Runnable
{
    private ServerSocket serverSocket;
    private Server server;

    AcceptorClients(ServerSocket serverSocket, Server server) {
        this.serverSocket = serverSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            while (true)
                new Thread(new AuthorizationClient(serverSocket.accept(), server)).start();
        }
        catch (IOException exp) {
            System.out.println("Server close");
            close();
        }
    }

    void close() {
        try {
            serverSocket.close();
        }
        catch (IOException exp) {}
    }
}
