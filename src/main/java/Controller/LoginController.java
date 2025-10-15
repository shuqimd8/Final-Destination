package Controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    /**
     * Handle Sign In button
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("⚠️ Please enter username and password");
            return;
        }

        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("✅ Login successful: " + username + " (" + role + ")");

                // ✅ Remember the logged-in user & role globally
                SceneManager.setCurrentUser(username, role);

                // 🚀 Navigate based on role
                if ("student".equalsIgnoreCase(role)) {
                    SceneManager.switchScene(
                            "/com/learneria/fxml/student_main.fxml",
                            "Student Main Menu"
                    );
                } else if ("teacher".equalsIgnoreCase(role)) {
                    SceneManager.switchScene(
                            "/com/learneria/fxml/teacher_main.fxml",
                            "Teacher Main Menu"
                    );
                } else {
                    System.out.println("⚠️ Unknown role: " + role);
                }

            } else {
                System.out.println("❌ Invalid credentials!");
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle Create Account button
     */
    @FXML
    private void handleCreateAccount() {
        SceneManager.switchScene(
                "/com/learneria/fxml/createAccount_Select.fxml",
                "Create Account - Select Type"
        );
    }
}

