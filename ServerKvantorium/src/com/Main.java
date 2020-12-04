package com;

import com.server.Database;
import com.server.Server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Server server = new Server(5000);
        server.start();

//        Database database = new Database();
//        database.removePatientFromStack("stack", "s");
//        System.out.println(database.existsPatientInStack("s", "stack"));
//        System.out.println(database.getStacks());
//        database.close();
//        Date date = new Date();
//        Date date_two = new Date(date.getTime() + 20 * 60 * 100000);
//        System.out.println(date.toString().substring(11, 19));
//        System.out.println(date.toString());
//        System.out.println(date_two.toString());
    }
}