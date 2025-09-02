package com.example.finaldestinationgroupproject;

public class Student {
    private String StudentUsername;
    private String StudentName;
    private String StudentPassword;
    private String Teacher;
    private String Level;

    public Student(String user, String pass, String name, String teacher) {
        StudentUsername = user;
        StudentPassword = pass;
        StudentName = name;
        Teacher = teacher;
    }
}
