package com.example.finaldestinationgroupproject;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        Databasing.create_db();
        Databasing.create_tables();
    }
}
