package com.example.finaldestinationgroupproject;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        MySQL.establishConnection();

        User.getStudents();
        User.getTeachers();
    }
}
