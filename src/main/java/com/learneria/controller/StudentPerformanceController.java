package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;
import java.util.Map;

public class StudentPerformanceController {

    @FXML private Label studentNameLabel;
    @FXML private Label classCodeLabel;
    @FXML private Label lastPlayedLabel;

    @FXML private TableView<Map<String, Object>> scoreTable;
    @FXML private TableColumn<Map<String, Object>, String> colGame;
    @FXML private TableColumn<Map<String, Object>, String> colScore;
    @FXML private TableColumn<Map<String, Object>, String> colCorrect;
    @FXML private TableColumn<Map<String, Object>, String> colIncorrect;
    @FXML private TableColumn<Map<String, Object>, String> colAvgSpeed;

    private String studentUsername;

    /** Called by the opener (Manage Classes) */
    public void setStudent(String username) {
        this.studentUsername = username;
        loadStudentInfo();
        loadScores();
    }

    private void loadStudentInfo() {
        Map<String, String> info = Database.getStudentInfo(studentUsername);
        if (info != null) {
            studentNameLabel.setText("👩‍🎓 " + info.getOrDefault("name", studentUsername));
            classCodeLabel.setText("🏫 Class: " + info.getOrDefault("class_code", "-"));
            lastPlayedLabel.setText("🕒 Last Played: " + info.getOrDefault("last_played", "-"));
        }
    }

    private void loadScores() {
        List<Map<String, Object>> scores = Database.getScoresByStudent(studentUsername);

        colGame.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().get("game"))));
        colScore.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().get("score"))));
        colCorrect.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().get("correct"))));
        colIncorrect.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().get("incorrect"))));
        colAvgSpeed.setCellValueFactory(cell ->
                new SimpleStringProperty(String.format("%.1f",
                        Double.parseDouble(String.valueOf(cell.getValue().get("avgSpeed"))))));

        scoreTable.setItems(FXCollections.observableArrayList(scores));
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_class_list.fxml", "Manage Classes");
    }
}
