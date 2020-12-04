package com.server;

import com.server.clients.Reception;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server
{
    private int port;
    private Thread thread;
    private Database database;

    private HashMap<String, Reception> receptions = new HashMap<>();

    private HashMap<String, Stack> stacks = new HashMap<>();

    public Server(int port) throws IOException, SQLException, ClassNotFoundException {
        this.port = port;
        database = new Database();
        thread = new Thread(new AcceptorClients(new ServerSocket(port), this));
    }

    public Iterator<Map.Entry<String, Stack>> getStacks() {
        return stacks.entrySet().iterator();
    }

    public Database getDatabase() {
        return database;
    }

    public void addReception(String id, Reception reception) {
        synchronized (receptions) {
            receptions.put(id, reception);
        }
    }

    public void removeReception(String id) {
        synchronized (receptions) {
            receptions.remove(id);
        }
    }

    public void addStack(String name, Stack stack) {
        synchronized (stacks) {
            stacks.put(name, stack);
        }
    }

    public Stack getStack(String name) {
        synchronized (stacks) {
            return stacks.get(name);
        }
    }

    public void start() throws SQLException {
        if (!thread.isAlive()) { //если сервер не запущен, то запускаем
            for (String name : database.getStacks().split(";")) {
                Stack stack = new Stack(database, name);
                new Thread(stack).start();
                addStack(name, stack);
            }
            thread.start();
        }
    }

//    public void close() {
//        acceptorClients.close();
//    }
}