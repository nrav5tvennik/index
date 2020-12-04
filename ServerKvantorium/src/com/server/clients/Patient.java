package com.server.clients;

import com.server.IOConnection;
import com.server.Server;
import com.server.Stack;

import java.io.IOException;

public class Patient extends Client
{
    //private Stack stack;

    public Patient(IOConnection ioConnection, String id, Server server) {
        super(ioConnection, id, server);
        //this.stack = stack;
        //stack.addPatient(id, this);
    }

    /*public String getId() {
        return id;
    }*/

    @Override
    public void run() {
        try {
            ioConnection.read();
            // TODO: на будущее
        }
        catch (IOException exp) {
            close();
        }
    }

    public void notification() {
        System.out.println("class Patient(id: " + id + ") notification()");
        try {
            ioConnection.write(TypeMessage.NOTIFICATION.toString() + ";Ваш приём!");
        }
        catch (Exception exp) {
            close();
        }
    }

    public void sendTime(String time) {
        System.out.println("class Patient(id: " + id + ") sendTime(" + time + ")");
        try {
            ioConnection.write(TypeMessage.TIME.toString() + ";" + time);
        }
        catch (Exception exp) {
            close();
        }
    }

    @Override
    public void close() {
        ioConnection.close();
    }
}

enum TypeMessage {
    TIME, NOTIFICATION
}