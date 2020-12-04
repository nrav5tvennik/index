package com.client.clients;

import com.client.IOConnection;

import java.io.IOException;

public class Reception extends Client {
    public Reception(IOConnection ioConnection) {
        super(ioConnection);
    }

    public void addPatientToStack(String idStack, String idPatient) throws IOException {
        ioConnection.write("ADD_TO_STACK;" + idStack + ";" + idPatient);
    }

    public String[] getStacks() throws IOException {
        ioConnection.write("STACKS");
        String reading = ioConnection.read();
        return reading.split(";");
    }
}
