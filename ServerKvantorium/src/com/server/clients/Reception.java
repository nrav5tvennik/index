package com.server.clients;

import com.server.IOConnection;
import com.server.Server;
import com.server.Stack;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

public class Reception extends Client {

    public Reception(IOConnection ioConnection, String id, Server server) {
        super(ioConnection, id, server);
    }

    @Override
    public void run() {
        try {
            try {
                while (true) {
                    // data = type_request;... :
                    // STACKS - ...
                    // ADD_TO_STACK - ...;id_stack;id_patient

                    String[] data = ioConnection.read().split(";");

                    if (data[0].equals("STACKS")) {
                        String str = getStacks();
                        ioConnection.write(str);
                    }
                    else if (data[0].equals("ADD_TO_STACK")) {
                        server.getDatabase().addPatientToStack(data[1], data[2]);
                        //server.getStack(data[1]).updateTime();
                    }
                }
            }
            catch(SQLException e) {}
        }
        catch (IOException exp) {
            exp.printStackTrace();
            close();
        }
    }

    public String getStacks() {
        StringJoiner stringJoiner = new StringJoiner(";");
        Iterator<Map.Entry<String, Stack>> iterator = server.getStacks();

        while (iterator.hasNext()) {
            Map.Entry<String, Stack> entry = iterator.next();
            if (entry.getValue().isDoctor())
                stringJoiner.add(entry.getKey());
        }
        return stringJoiner.toString();
    }

    @Override
    protected void close() {
        ioConnection.close();
        server.removeReception(id);
    }
}