package com.server.clients;

import com.server.IOConnection;
import com.server.Server;

import java.io.IOException;
import java.sql.SQLException;

public class Doctor extends Client
{
    private String idStack;
    private StatusDoctor status = StatusDoctor.NONE;

    public Doctor(IOConnection ioConnection, Server server, String id, String idStack) throws SQLException {
        super(ioConnection, id, server);
        this.idStack = idStack;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String[] data = ioConnection.read().split(";");
                // data: NONE, NEXT, WORK, END

                if (data[0].equals(StatusDoctor.NEXT.toString()))
                    status = StatusDoctor.NEXT;
                else if (data[0].equals(StatusDoctor.WORK.toString()))
                    status = StatusDoctor.WORK;
                else if (data[0].equals(StatusDoctor.END.toString()))
                    status = StatusDoctor.NONE;
                else
                    continue;
                server.getStack(idStack).changeStatusDoctor();
            }
        }
        catch (IOException exp) {
            exp.printStackTrace();
            close();
        }
    }

    public void sendCountPatients(int count) {
        System.out.println("class Doctor(id: " + id + ") sendCountPatients(" + count + ")");

        try {
            ioConnection.write(String.valueOf(count));
        }
        catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    public StatusDoctor getStatus() {
        return status;
    }

    @Override
    protected void close() {
        server.getStack(idStack).disconnectDoctor();
        ioConnection.close();
    }
}