package com.example.demo.learneria;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Settings extends Application {

    private BorderPane root;
    private VBox sideMenu;
    private StackPane contentPane;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();

        // === Left Sidebar Menu ===
        sideMenu = new VBox(10);
        sideMenu.setPadding(new Insets(15));

        Button btnGeneral = new Button("General");
        Button btnAudio = new Button("Audio");
        Button btnDisplay = new Button("Display");
        Button btnTutorial = new Button("Tutorial");

        btnGeneral.getStyleClass().add("side-button");
        btnAudio.getStyleClass().add("side-button");
        btnDisplay.getStyleClass().add("side-button");
        btnTutorial.getStyleClass().add("side-button");

        btnGeneral.setMaxWidth(Double.MAX_VALUE);
        btnAudio.setMaxWidth(Double.MAX_VALUE);
        btnDisplay.setMaxWidth(Double.MAX_VALUE);
        btnTutorial.setMaxWidth(Double.MAX_VALUE);

        sideMenu.getChildren().addAll(btnGeneral, btnAudio, btnDisplay, btnTutorial);

        // === Right Content Area ===
        contentPane = new StackPane();
        contentPane.setPadding(new Insets(20));
        showGeneralPage();

        // === Layout ===
        root.setLeft(sideMenu);
        root.setCenter(contentPane);

        // === Page Switching Actions ===
        btnGeneral.setOnAction(e -> showGeneralPage());
        btnAudio.setOnAction(e -> showAudioPage());
        btnDisplay.setOnAction(e -> showDisplayPage());
        btnTutorial.setOnAction(e -> showTutorialPage());

        // === Scene ===
        Scene scene = new Scene(root, 700, 450);

        // Load CSS
        scene.getStylesheets().add(getClass().getResource("/css/defaultstyle.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/colourblind.css").toExternalForm());

        // Start with light mode by default
        root.getStyleClass().add("light");

        primaryStage.setTitle("Papa Learneria - Settings");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // === General Page ===
    private void showGeneralPage() {
        VBox page = new VBox(15);

        Label title = new Label("General Settings");

        // Profile Section
        TextField nameField = new TextField("Student Name");
        TextField usernameField = new TextField("Username123");
        Button logoutBtn = new Button("Log out");

        VBox profileBox = new VBox(10,
                new Label("Name:"), nameField,
                new Label("Username:"), usernameField,
                logoutBtn
        );

        // Teacher Section
        TextField searchTeacher = new TextField();
        searchTeacher.setPromptText("Search teacher");
        ComboBox<String> teacherList = new ComboBox<>();
        teacherList.getItems().addAll("Mr Smith", "Ms Johnson", "Mrs Brown");
        teacherList.setPromptText("Select a teacher");

        VBox teacherBox = new VBox(10, new Label("Add teacher:"), searchTeacher, teacherList);

        // Hints Section
        CheckBox hintsCheck = new CheckBox("Enable hints");
        hintsCheck.setSelected(true);

        // About Section
        VBox aboutBox = new VBox(5,
                new Label("Papa Learneria v1.0"),
                new Label("Created by Team Final Destination")
        );

        page.getChildren().addAll(title, profileBox, teacherBox, hintsCheck, aboutBox);
        page.setPadding(new Insets(20));

        contentPane.getChildren().setAll(page);
    }

    // === Audio Page (includes Notifications) ===
    private void showAudioPage() {
        VBox page = new VBox(15);

        Label title = new Label("Audio Settings");

        Slider musicSlider = new Slider(0, 100, 50);
        Slider sfxSlider = new Slider(0, 100, 70);
        CheckBox notifToggle = new CheckBox("Enable notifications");
        notifToggle.setSelected(true);

        page.getChildren().addAll(
                title,
                new Label("Music volume"), musicSlider,
                new Label("Sound effects"), sfxSlider,
                notifToggle
        );
        page.setPadding(new Insets(20));

        contentPane.getChildren().setAll(page);
    }

    // === Display Page (Theme & Font) ===
    private void showDisplayPage() {
        VBox page = new VBox(15);

        Label title = new Label("Display Settings");

        // Theme
        ToggleGroup themeGroup = new ToggleGroup();
        RadioButton light = new RadioButton("Light mode");
        RadioButton dark = new RadioButton("Dark mode");
        light.setToggleGroup(themeGroup);
        dark.setToggleGroup(themeGroup);
        light.setSelected(true);

        // ✅ Switch between stylesheets instead of inline styles
        themeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            root.getStyleClass().removeAll("light", "dark"); // clear both first
            if (newVal == dark) {
                root.getStyleClass().add("dark");
            } else {
                root.getStyleClass().add("light");
            }
        });

        // Colour-blind
        ComboBox<String> cbColourBlind = new ComboBox<>();
        cbColourBlind.getItems().addAll("Default", "R/G Colour-blind", "B/Y Colour-blind");
        cbColourBlind.setValue("Default");

        // Add event listener for colour-blind mode switching
        cbColourBlind.setOnAction(e -> {
            root.getStyleClass().removeAll("colourblind-rg", "colourblind-by");

            String choice = cbColourBlind.getValue();
            if (choice.equals("R/G Colour-blind")) {
                root.getStyleClass().add("colourblind-rg");
            } else if (choice.equals("B/Y Colour-blind")) {
                root.getStyleClass().add("colourblind-by");
            }
        });

        // Font Options
        ComboBox<String> fontOption = new ComboBox<>();
        fontOption.getItems().addAll("Jost", "Arial", "Comic Sans MS");
        fontOption.setValue("Jost");

        // ✅ Apply font dynamically
        fontOption.setOnAction(e -> {
            String font = fontOption.getValue();
            root.setStyle("-fx-font-family: '" + font + "';");
        });

        page.getChildren().addAll(
                title,
                new Label("Theme:"), light, dark,
                new Label("Colour-blind mode:"), cbColourBlind,
                new Label("Font option:"), fontOption
        );
        page.setPadding(new Insets(20));

        contentPane.getChildren().setAll(page);

        Button resetBtn = new Button("Reset to default");
        resetBtn.getStyleClass().add("reset-button");
        resetBtn.setOnAction(e -> {
            root.getStyleClass().removeAll("dark", "colourblind-rg", "colourblind-by");
            if (!root.getStyleClass().contains("light")) {
                root.getStyleClass().add("light"); // force back to light theme
            }
            cbColourBlind.setValue("Default");
            fontOption.setValue("Jost");
            root.setStyle("-fx-font-family: 'Jost';");
            light.setSelected(true);
        });

        page.getChildren().add(resetBtn);
    }

    // === Tutorial Page ===
    private void showTutorialPage() {
        VBox page = new VBox(10, new Label("Tutorial page (coming soon)"));
        page.setPadding(new Insets(20));
        contentPane.getChildren().setAll(page);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
