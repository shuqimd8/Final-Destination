package Controller;

import com.learneria.utils.Database;
import com.learneria.utils.SceneManager;
import com.learneria.utils.UserAware;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class SettingsController implements UserAware {

    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;

    private String username;

    @Override
    public void setUsername(String username) {
        this.username = username;
        loadUserData();
    }

    /**
     * Load user info from the database
     */
    private void loadUserData() {
        try {
            Connection conn = Database.getInstance().getConnection();

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT username, password, COALESCE(name, username) AS displayName FROM users WHERE username = ?"
            );
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usernameLabel.setText(rs.getString("username"));
                passwordLabel.setText("********"); // mask password
                nameLabel.setText(rs.getString("displayName"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 🔙 Handle Back button
     */
    @FXML
    private void handleBack() {
        // Return to dashboard depending on role
        SceneManager.goBackToDashboard();
    }

    /**
     * ✏️ Edit display name
     */
    @FXML
    private void editName() {
        editField("Enter new name:", "name", nameLabel);
    }

    /**
     * ✏️ Edit username
     */
    @FXML
    private void editUsername() {
        editField("Enter new username:", "username", usernameLabel);
        this.username = usernameLabel.getText(); // update reference
    }

    /**
     * ✏️ Edit password
     */
    @FXML
    private void editPassword() {
        editField("Enter new password:", "password", passwordLabel);
        passwordLabel.setText("********"); // always masked
    }

    /**
     * Generic field editor for user info
     */
    private void editField(String prompt, String column, Label targetLabel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(prompt);
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(newValue -> {
            if (!newValue.trim().isEmpty()) {
                updateDatabaseField(column, newValue.trim());
                if (!column.equals("password")) {
                    targetLabel.setText(newValue.trim());
                }
            }
        });
    }

    /**
     * Update database value safely
     */
    private void updateDatabaseField(String column, String newValue) {
        try {
            Connection conn = Database.getInstance().getConnection();
            String sql = "UPDATE users SET " + column + " = ? WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newValue);
            stmt.setString(2, username);
            stmt.executeUpdate();
            stmt.close();

            // If username changed, update local and global references
            if (column.equals("username")) {
                username = newValue;
                SceneManager.setCurrentUser(newValue, SceneManager.getCurrentRole());
            }

            System.out.println("✅ Updated " + column + " for user: " + username);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🎨 Theme buttons (placeholders)
    @FXML private void setDefaultTheme() { System.out.println("✅ Default theme set"); }
    @FXML private void setRGTheme() { System.out.println("✅ Red/Green theme set"); }
    @FXML private void setBYTheme() { System.out.println("✅ Blue/Yellow theme set"); }
}


