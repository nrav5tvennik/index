package com.server;

import com.server.IOConnection;
import com.server.Server;
import com.server.TypeClient;
import com.server.clients.Doctor;
import com.server.clients.Patient;
import com.server.clients.Reception;

import java.io.IOException;
import java.net.Socket;

public class AuthorizationClient implements Runnable
{
    private IOConnection ioConnection;
    private Server server;

    public AuthorizationClient(Socket socket, Server server) throws IOException {
        ioConnection = new IOConnection(socket);
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // if doctor then data = type;id
            // if patient then data = type;id;id_stack
            // if reception then data = type;id

            String[] data = ioConnection.read().split(";");

            if (TypeClient.DOCTOR.toString().equals(data[0])) {
                String idStack = server.getDatabase().getIdStack(data[1]);
                if (idStack.equals("")) {
                    ioConnection.close();
                    return;
                }
                Doctor doctor = new Doctor(ioConnection, server, data[1], idStack);
                new Thread(doctor).start();
                server.getStack(idStack).setDoctor(doctor);
                System.out.println("Doctor connect!");
            }
            else if (TypeClient.RECEPTION.toString().equals(data[0])) {
                if (server.getDatabase().existsReception(data[1])) {
                    ioConnection.write("OK");
                    Reception reception = new Reception(ioConnection, data[1], server);
                    new Thread(reception).start();
                    server.addReception(data[1], reception);
                    System.out.println("Reception connect!");
                }
                else {
                    ioConnection.write("NO");
                    ioConnection.close();
                }
            }
            else if (TypeClient.PATIENT.toString().equals(data[0])) {
                if (!server.getDatabase().existsPatientInStack(data[1], data[2]))
                    return;
                Patient patient = new Patient(ioConnection, data[1], server);
                new Thread(patient).start();
                server.getStack(data[2]).addPatient(data[1], patient);
                System.out.println("Patient connect!");
            }
            else
                ioConnection.close();
        }
        catch (Exception exp) {
            //System.out.println("Disconnect on authorize");
            ioConnection.close();
        }
    }
}