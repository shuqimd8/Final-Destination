package Controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Map;

public class TeacherReportsController {

    @FXML private TableView<Map<String, Object>> classTable;
    @FXML private TableColumn<Map<String, Object>, String> colClassName;
    @FXML private TableColumn<Map<String, Object>, String> colClassCode;
    @FXML private TableColumn<Map<String, Object>, String> colAvgScore;
    @FXML private TableColumn<Map<String, Object>, String> colStudentCount;

    @FXML private ListView<String> topStudentsList;
    @FXML private ListView<String> gameStatsList;

    private String teacherUsername;

    @FXML
    public void initialize() {
        teacherUsername = SceneManager.getCurrentUser();

        loadClassOverview();
        loadTopStudents();
        loadGameStats();
    }

    private void loadClassOverview() {
        List<Map<String, Object>> data = Database.getClassOverview(teacherUsername);

        colClassName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get("class_name").toString()));
        colClassCode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get("class_code").toString()));
        colAvgScore.setCellValueFactory(cell -> {
            Object val = cell.getValue().get("avg_score");
            String text = (val instanceof Double)
                    ? String.format("%.1f", (Double) val)
                    : String.valueOf(val);
            return new SimpleStringProperty(text);
        });
        colStudentCount.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().get("student_count"))));

        classTable.setItems(FXCollections.observableArrayList(data));
    }

    private void loadTopStudents() {
        List<Map<String, Object>> top = Database.getTopStudents(teacherUsername);
        for (Map<String, Object> s : top) {
            topStudentsList.getItems().add("⭐ " + s.get("username") + " — " + s.get("avg_score"));
        }
    }

    private void loadGameStats() {
        Map<String, Double> stats = Database.getAverageByGame(teacherUsername);
        stats.forEach((game, score) -> {
            gameStatsList.getItems().add(game + ": " + String.format("%.1f", score));
        });
    }

    @FXML
    private void handleBack() {
        SceneManager.switchScene("/com/learneria/fxml/teacher_main.fxml", "Teacher Menu");
    }
}
