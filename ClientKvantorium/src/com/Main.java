package com;

import com.client.Connect;
import com.client.ConnectInfo;
import com.client.clients.Doctor;
import com.client.clients.Reception;
import com.client.clients.StatusDoctor;
import com.client.clients.events.DoctorEvents;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException
    {
        Doctor doctor = Connect.authorizeDoctor(new ConnectInfo("localhost", 5000), "doc", new MyDoctor());
        doctor.setStatus(StatusDoctor.NEXT);
    }

    static class MyDoctor implements DoctorEvents {
        @Override
        public void setCountPatients(int count) {
            System.out.println("Кол-во пациентов: " + count);
        }
    }
}
