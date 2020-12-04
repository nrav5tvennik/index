package com.server.clients;

import com.server.IOConnection;
import com.server.Server;

public abstract class Client implements Runnable {
    protected IOConnection ioConnection;
    protected Server server;
    protected String id;

    public Client(IOConnection ioConnection, String id, Server server) {
        this.ioConnection = ioConnection;
        this.server = server;
        this.id = id;
    }

    protected abstract void close();
}