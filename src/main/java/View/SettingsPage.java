package View;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SettingsPage implements SceneProvider {

    private final Scene scene;
    private final BorderPane root;
    private final VBox sideMenu;
    private final StackPane contentPane;

    public SettingsPage(Stage stage) {
        root = new BorderPane();

        // === Left Sidebar Menu ===
        sideMenu = new VBox(10);
        sideMenu.setPadding(new Insets(15));

        Button btnGeneral = new Button("General");
        Button btnAudio = new Button("Audio");
        Button btnDisplay = new Button("Display");
        Button btnTutorial = new Button("Tutorial");
        Button btnExit = new Button("Exit");

        // Style buttons
        for (Button btn : new Button[]{btnGeneral, btnAudio, btnDisplay, btnTutorial, btnExit}) {
            btn.getStyleClass().add("side-button");
            btn.setMaxWidth(Double.MAX_VALUE);
        }

        sideMenu.getChildren().addAll(btnGeneral, btnAudio, btnDisplay, btnTutorial, btnExit);

        // === Right Content Area ===
        contentPane = new StackPane();
        contentPane.setPadding(new Insets(20));
        showGeneralPage(); // default page

        // === Layout ===
        root.setLeft(sideMenu);
        root.setCenter(contentPane);

        // === Actions ===
        btnGeneral.setOnAction(e -> showGeneralPage());
        btnAudio.setOnAction(e -> showAudioPage());
        btnDisplay.setOnAction(e -> showDisplayPage());
        btnTutorial.setOnAction(e -> showTutorialPage());
        btnExit.setOnAction(e -> stage.setScene(new MainMenu(stage).getScene()));

        // === Scene ===
        scene = new Scene(root, 700, 450);

        // CSS (safe load)
        loadCss("/css/style.css");        // ✅ fixed from defaultstyle.css
        loadCss("/css/colourblind.css");  // still here
    }

    private void loadCss(String path) {
        try {
            var css = getClass().getResource(path);
            if (css != null) {
                scene.getStylesheets().add(css.toExternalForm());
                System.out.println("✅ Loaded CSS: " + path);
            } else {
                System.out.println("⚠️ CSS not found: " + path);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Failed to load CSS: " + path + " → " + e.getMessage());
        }
    }

    // === PAGE CONTENTS ===
    private void showGeneralPage() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));

        Label lbl = new Label("General Settings");
        CheckBox chkAutosave = new CheckBox("Enable Autosave");
        CheckBox chkNotifications = new CheckBox("Show Notifications");

        box.getChildren().addAll(lbl, chkAutosave, chkNotifications);
        contentPane.getChildren().setAll(box);
    }

    private void showAudioPage() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));

        Label lbl = new Label("Audio Settings");
        Slider volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        CheckBox muteBox = new CheckBox("Mute");

        box.getChildren().addAll(lbl, new Label("Volume:"), volumeSlider, muteBox);
        contentPane.getChildren().setAll(box);
    }

    private void showDisplayPage() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));

        Label lbl = new Label("Display Settings");
        ComboBox<String> themeBox = new ComboBox<>();
        themeBox.getItems().addAll("Default", "Dark Mode", "Colourblind");
        themeBox.setValue("Default");

        CheckBox fullscreen = new CheckBox("Enable Fullscreen");

        box.getChildren().addAll(lbl, new Label("Theme:"), themeBox, fullscreen);
        contentPane.getChildren().setAll(box);
    }

    private void showTutorialPage() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));

        Label lbl = new Label("Tutorial Settings");
        Button btnStartTutorial = new Button("Start Tutorial");
        Button btnResetTutorial = new Button("Reset Tutorial Progress");

        box.getChildren().addAll(lbl, btnStartTutorial, btnResetTutorial);
        contentPane.getChildren().setAll(box);
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
