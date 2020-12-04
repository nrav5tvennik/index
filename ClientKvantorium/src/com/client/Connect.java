package com.client;

import com.client.clients.Doctor;
import com.client.clients.Patient;
import com.client.clients.Reception;
import com.client.clients.events.DoctorEvents;
import com.client.clients.events.PatientEvent;

import java.io.IOException;
import java.net.Socket;

public class Connect {
    private Socket socket;
    private ConnectInfo connectInfo;

    private IOException exception;
    private IOConnection ioConnection;

    private Connect(ConnectInfo connectInfo) throws IOException {
        this.connectInfo = connectInfo;

        Thread connection = new Thread(new ConnectSocket());
        connection.start();
        while (true) {
            if (socket != null && socket.isConnected())
                break;
            else if (!connection.isAlive())
                throw exception;
        }
        ioConnection = new IOConnection(socket);
    }

    public static Doctor authorizeDoctor(ConnectInfo connectInfo, String id, DoctorEvents doctorEvents) throws IOException {
        Connect connect = new Connect(connectInfo);
        connect.ioConnection.write(TypeClient.DOCTOR.toString() + ";" + id);
        return new Doctor(connect.ioConnection, doctorEvents);
    }

    public static Patient authorizePatient(ConnectInfo connectInfo, String id, String stack, PatientEvent patientEvent) throws IOException {
        Connect connect = new Connect(connectInfo);
        connect.ioConnection.write(TypeClient.PATIENT.toString() + ";" + id + ";" + stack);
        return new Patient(connect.ioConnection, patientEvent);
    }

    public static Reception authorizeReception(ConnectInfo connectInfo, String id) throws IOException {
        Connect connect = new Connect(connectInfo);
        connect.ioConnection.write(TypeClient.RECEPTION.toString() + ";" + id);
        if (connect.ioConnection.read().equals("OK"))
            return new Reception(connect.ioConnection);
        throw new IOException();
    }

    private class ConnectSocket implements Runnable {
        @Override
        public void run() {
            try {
                socket = new Socket(connectInfo.getIp(), connectInfo.getPort());
            }
            catch (IOException exp) {
                exception = exp;
            }
        }
    }
}