package com.client.clients;

import com.client.IOConnection;
import com.client.clients.events.PatientEvent;

import java.io.IOException;

public class Patient extends Client {
    private PatientEvent patientEvent;

    public Patient(IOConnection ioConnection, PatientEvent patientEvent) {
        super(ioConnection);
        this.patientEvent = patientEvent;
        new Thread(new Update()).start();
    }

    private class Update implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String[] data = ioConnection.read().split(";");

                    System.out.println("DATA");

                    if (data[0].equals(TypeMessage.TIME.toString()))
                        patientEvent.setTimeOfReceipt(data[1]);
                    else if (data[0].equals(TypeMessage.NOTIFICATION.toString()))
                        patientEvent.notification(data[1]);
                }
            }
            catch (IOException exp) {
                System.exit(0);
            }
        }
    }
}

enum TypeMessage {
    TIME, NOTIFICATION
}