package com.client.clients;

import com.client.IOConnection;

public class Client {
    protected IOConnection ioConnection;

    protected Client(IOConnection ioConnection) {
        this.ioConnection = ioConnection;
    }
}
