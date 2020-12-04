package com.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Database
{
    private Connection connection;
    private Statement statement;

    public Database() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:DataBaze.db");
        statement = connection.createStatement();
    }

    public synchronized boolean existsDoctor(String id) throws SQLException {
        ResultSet answer = statement.executeQuery("SELECT * FROM doctors");

        while(answer.next())
            if (answer.getString("id").equals(id))
                return true;

        return false;
    }

    public synchronized boolean existsPatientInStack(String idPatient, String idStack) throws SQLException {
        ResultSet answer = statement.executeQuery("SELECT id FROM " + idStack + " WHERE id='" + idPatient +"'");

        if (answer.next())
            return true;
        return false;
    }

    public synchronized boolean existsReception(String id) throws SQLException {
        ResultSet answer = statement.executeQuery("SELECT * FROM reception");

        while(answer.next())
            if (answer.getString("id").equals(id))
                return true;

        return false;
    }

//    public synchronized boolean existsPatient(String stack) {
//
//        return false;
//    }

    public synchronized String getIdStack(String idDoctor) throws SQLException {
        ResultSet answer = statement.executeQuery("SELECT stack FROM doctors WHERE id='" + idDoctor + "'");
        if (answer.next())
            return answer.getString("stack");
        return "";
    }

    public synchronized void addPatientToStack(String idStack, String idPatient) throws SQLException {
//        String nameStack = getNameStack(idDoctor);
        statement.execute("INSERT INTO " + idStack + " (id) VALUES ('" + idPatient + "')");
    }

    public synchronized void removePatientFromStack(String idStack, String idPatient) throws SQLException {
        statement.execute("DELETE from " + idStack + " WHERE id='" + idPatient + "'");
    }

    public synchronized String getFirstPatientFromStack(String stack) throws SQLException {
        ResultSet answer = statement.executeQuery("SELECT id FROM " + stack);
        if (answer.next()) {
            String text = answer.getString("id");
            if (text != null)
                return text;
        }
        return "";
    }

    public synchronized String getPatientsFromStack(String stack) throws SQLException {
        ResultSet answer = statement.executeQuery("SELECT id FROM " + stack);
        StringJoiner stringJoiner = new StringJoiner(";");

        while (answer.next())
            stringJoiner.add(answer.getString("id"));

        return stringJoiner.toString();
    }

    public synchronized String getStacks() throws SQLException {
        ResultSet answer = statement.executeQuery("SELECT stack FROM doctors");
        StringJoiner stringJoiner = new StringJoiner(";");

        while(answer.next())
            stringJoiner.add(answer.getString("stack"));
        return stringJoiner.toString();
    }

    public void close() {
        try {
            connection.close();
        }
        catch (SQLException e) {}
    }
}