package com.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class IOConnection {
    private Socket socket;
    private InputStreamReader reader;
    private OutputStreamWriter writer;

    public IOConnection(Socket socket) throws IOException {
        this.socket = socket;
        reader = new InputStreamReader(socket.getInputStream());
        writer = new OutputStreamWriter(socket.getOutputStream());
    }

    public String read() throws IOException {
        Read read = new Read();
        Thread th = new Thread(read);
        th.start();

        while (true) {
            if (!th.isAlive()) {
                if (read.getException() != null)
                    throw read.getException();
                return read.getRead();
            }
        }
    }

    public void write(String data) throws IOException {
        Write write = new Write(data);
        Thread th = new Thread(write);
        th.start();

        while (true) {
            if (!th.isAlive()) {
                if (write.getException() != null)
                    throw write.getException();
                break;
            }
        }
    }

    private class Read implements Runnable {
        private IOException exception;
        private String strRead = "";

        @Override
        public void run() {
            try {
                int data;
                StringBuilder strData = new StringBuilder();

                while ((data = reader.read()) != -1) {
                    strData.append((char) data);

                    if (!reader.ready())
                        break;
                }

                if (strData.toString().equals("_")) {
                    strRead = "";
                    return;
                }

                strRead = strData.toString();
            }
            catch (IOException exp) {
                exception = exp;
            }
        }

        public String getRead() {
            return strRead;
        }

        public IOException getException() {
            return exception;
        }
    }

    private class Write implements Runnable {
        private IOException exception;
        private String strWrite = "";

        public Write(String strWrite) {
            this.strWrite = strWrite;
        }

        @Override
        public void run() {
            try {
                if (strWrite == null || strWrite.equals(""))
                    strWrite = "_";
                for (char i : strWrite.toCharArray())
                    writer.write(i);
                writer.flush();
            }
            catch (IOException exp) {
                exception = exp;
            }
        }

        public IOException getException() {
            return exception;
        }
    }

    public void close() {
        try {
            socket.close();
        }
        catch (IOException exp) {}
    }
}