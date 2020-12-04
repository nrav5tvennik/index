package com.server;

import com.server.clients.Doctor;
import com.server.clients.Patient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Stack implements Runnable
{
    private String name;
    private HashMap<String, Patient> patients = new HashMap<>();
    private Doctor doctor;
    private ArrayList<Long> times = new ArrayList<>();
    private long averageTime = 25 * 60 * 1000;  // 25 minutes

    private Database database;

    private Date startTime;
    private String idPatientWithWork = "";

    public Stack(Database database, String name) {
        this.database = database;
        this.name = name;
    }

    @Override
    public void run() {
        while (true)
        {
            if (isDoctor())
                updateTime();

            try {
                Thread.sleep(60000);  // minute 60000 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isDoctor() {
        if (doctor != null)
            return true;
        return false;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        doctor.sendCountPatients(getCountPatients());
    }

    public void disconnectDoctor() {
        doctor = null;
    }

    public void changeStatusDoctor() {
        try {
            Patient patient = patients.get(database.getFirstPatientFromStack(name));

            switch (doctor.getStatus()) {
                case NEXT:
                    if (patient != null)
                        patient.notification();
                    break;
                case WORK:
                    idPatientWithWork = database.getFirstPatientFromStack(name);
                    patient = patients.get(idPatientWithWork);
                    if (patient != null)
                        patient.close();
                    database.removePatientFromStack(name, idPatientWithWork);
                    startTime = new Date();
                    break;
                case NONE:
                    System.out.println(startTime + " " + times);
                    times.add(startTime.getTime() - new Date().getTime());
                    calculateAverageTime();
                    break;
            }
        }
        catch (SQLException exp){
            exp.printStackTrace();
        }
    }

    public void updateTime() {
        try {
            switch (doctor.getStatus()) {
                case NONE:
                    sendTimeToPatients(new Date());
                    break;
                case NEXT:
                    sendTimeToPatients(new Date());
                    Patient patient = patients.get(database.getFirstPatientFromStack(name));
                    if (patient != null)
                        patient.notification();
                    break;
                case WORK:
                    long appointmentTime = new Date().getTime() - startTime.getTime();
                    if (appointmentTime < averageTime)
                        sendTimeToPatients(new Date(startTime.getTime() + averageTime));
                    else
                        sendTimeToPatients(new Date());
                    break;
                case END:
                    times.add(startTime.getTime() - new Date().getTime());
                    calculateAverageTime();
                    sendTimeToPatients(new Date());
                    break;
            }
        }
        catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    public void sendTimeToPatients(Date date) throws SQLException
    {
        String[] idPatients = database.getPatientsFromStack(name).split(";");
        for (int i = 0; i < idPatients.length; i++) {
            Patient patient = patients.get(idPatients[i]);
            if (patient != null) {
                patient.sendTime(new Date(date.getTime() + i * averageTime).toString().substring(11, 16));
            }
        }
    }

    private void calculateAverageTime() {
        if (times.size() <= 5)
            return;

        long sumTimes = 0;
        for (Long time : times)
            sumTimes += time;

        averageTime = sumTimes / times.size();
    }

    public void addPatient(String id, Patient patient) {
        synchronized (patients) {
            patients.put(id, patient);
            doctor.sendCountPatients(getCountPatients());
        }

        updateTime();
    }

    public int getCountPatients() {
        try {
            String[] patients = database.getPatientsFromStack(name).split(";");
            System.out.println(patients.length + patients[0]);
            if (patients.length == 1 && patients[0].equals(""))
                return 0;
            else
                return patients.length;
        }
        catch (SQLException exp) {
            exp.printStackTrace();
        }
        return 0;
    }

    public Patient getPatient(String id) {
        synchronized (patients) {
            return patients.get(id);
        }
    }

    public void removePatient(String id) {
        synchronized (patients) {
            patients.remove(id);
        }
    }
}