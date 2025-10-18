package com.learneria.controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherClassListController {

    @FXML private Label teacherNameLabel;
    @FXML private ListView<String> classListView;
    @FXML private Label classNameLabel;
    @FXML private Label classCodeLabel;
    @FXML private Label studentCountLabel;
    @FXML private Label avgScoreLabel;
    @FXML private ListView<String> studentListView;
    @FXML private Button viewStudentButton;

    private String teacherUsername;
    // Map visible class title -> class code
    private final Map<String, String> classTitleToCode = new HashMap<>();

    @FXML
    public void initialize() {
        teacherUsername = SceneManager.getCurrentUser();
        teacherNameLabel.setText("Teacher " + teacherUsername);

        loadClasses();

        classListView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) showClass(n);
        });

        studentListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) openSelectedStudent();
        });

        viewStudentButton.setOnAction(e -> openSelectedStudent());
        viewStudentButton.setDisable(true);

        studentListView.getSelectionModel().selectedItemProperty().addListener((o1, o2, n) ->
                viewStudentButton.setDisable(n == null));
    }

    private void loadClasses() {
        List<Map<String, String>> classes = Database.getClassesByTeacher(teacherUsername);

        ObservableList<String> items = FXCollections.observableArrayList();
        classTitleToCode.clear();
        for (Map<String, String> c : classes) {
            String title = c.get("class_name") + " (" + c.get("class_code") + ")";
            items.add(title);
            classTitleToCode.put(title, c.get("class_code"));
        }
        classListView.setItems(items);

        if (!items.isEmpty()) {
            classListView.getSelectionModel().select(0);
            showClass(items.get(0));
        } else {
            // clear right pane
            classNameLabel.setText("-");
            classCodeLabel.setText("-");
            studentCountLabel.setText("-");
            avgScoreLabel.setText("-");
            studentListView.setItems(FXCollections.observableArrayList());
        }
    }

    private void showClass(String classTitle) {
        String code = classTitleToCode.get(classTitle);
        classNameLabel.setText(classTitle.substring(0, classTitle.indexOf(" (")));
        classCodeLabel.setText(code);

        List<String> students = Database.getStudentsByClass(code);
        studentListView.setItems(FXCollections.observableArrayList(students));
        studentCountLabel.setText(String.valueOf(students.size()));

        double avg = Database.getAverageClassScore(code);
        avgScoreLabel.setText(students.isEmpty() ? "-" : String.format("%.1f", avg));
    }

    private void openSelectedStudent() {
        String username = studentListView.getSelectionModel().getSelectedItem();
        if (username == null || username.isBlank()) return;

        // open student_performance.fxml and pass username
        SceneManager.switchSceneWithController(
                "/com/learneria/fxml/student_performance.fxml",
                "Student Performance",
                controller -> {
                    if (controller instanceof StudentPerformanceController spc) {
                        spc.setStudent(username);
                    }
                }
        );
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Main Menu");
    }

    @FXML
    private void handleLogout() {
        SceneManager.setCurrentUser(null, null);
        SceneManager.switchScene("/com/learneria/fxml/login.fxml", "Login");
    }
    @FXML
    private void openSettings() {
        SceneManager.switchScene("/com/learneria/fxml/settings.fxml", "Settings");
    }

}
