package com.client.clients;

import com.client.IOConnection;
import com.client.clients.events.DoctorEvents;

import java.io.IOException;

public class Doctor extends Client implements Runnable {
    private DoctorEvents doctorEvents;

    public Doctor(IOConnection ioConnection, DoctorEvents doctorEvents) {
        super(ioConnection);
        this.doctorEvents = doctorEvents;
        new Thread(this).start();
//        new Thread(new AcceptMessage()).start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String strRead = ioConnection.read();

                if (strRead != null && !strRead.equals("")) {
                    int count = Integer.parseInt(strRead);
                    doctorEvents.setCountPatients(count);
                }
            }
        }
        catch (IOException exp) {
            System.out.println("Error");
            exp.printStackTrace();
        }
    }

//    private class AcceptMessage implements Runnable {
//        @Override
//        public void run() {
//
//        }
//    }

    public void setStatus(StatusDoctor status) throws IOException {
        ioConnection.write(status.toString());
    }
}