package com.client.clients.events;

public interface PatientEvent {
    void setTimeOfReceipt(String time);
    void notification(String text);
}