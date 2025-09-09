package com.example.finaldestinationgroupproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

import java.io.IOException;

public class Program {

    public void signIn() {
        User.getLogin();
    }

    public void signUp() {
        String type = User.getUserType();
        if (type.equals("student")) {
            User.CreateNewStudent();
        }
        else {
            User.CreateNewTeacher();
        }
    }
}
