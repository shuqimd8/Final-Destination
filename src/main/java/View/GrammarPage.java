package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GrammarPage implements SceneProvider {

    private final Scene scene;

    public GrammarPage(Stage stage) {
        // Root container
        StackPane root = new StackPane();

        // Background with soft gradient
        Rectangle bgRect = new Rectangle(1024, 768);
        LinearGradient bgGradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#FFF5E6")),
                new Stop(0.5, Color.web("#FFE4CC")),
                new Stop(1, Color.web("#FFD4B3"))
        );
        bgRect.setFill(bgGradient);

        // Main content container
        BorderPane mainContainer = new BorderPane();

        // ================== TOP SECTION ==================
        StackPane topSection = new StackPane();

        // Background image with overlay
        try {
            ImageView bgImage = new ImageView(new Image(getClass().getResource("/Images/background.jpg").toExternalForm()));
            bgImage.setPreserveRatio(false);
            bgImage.setFitHeight(150);
            bgImage.setFitWidth(1024);

            // Semi-transparent white overlay
            Rectangle whiteOverlay = new Rectangle(1024, 150);
            whiteOverlay.setFill(Color.rgb(255, 255, 255, 0.85));

            topSection.getChildren().addAll(bgImage, whiteOverlay);
        } catch (Exception e) {
            // Fallback if image not found
            Rectangle topBg = new Rectangle(1024, 150);
            topBg.setFill(Color.web("#E6F3FF"));
            topSection.getChildren().add(topBg);
        }

        // Top content container
        HBox topContent = new HBox();
        topContent.setPadding(new Insets(20, 30, 20, 30));
        topContent.setAlignment(Pos.CENTER);

        // Back button
        Button backButton = new Button();
        try {
            ImageView arrowIcon = new ImageView(new Image(getClass().getResource("/Images/Arrow.png").toExternalForm()));
            arrowIcon.setFitWidth(35);
            arrowIcon.setFitHeight(35);
            backButton.setGraphic(arrowIcon);
        } catch (Exception e) {
            backButton.setText("←");
            backButton.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        }
        backButton.setStyle(
                "-fx-background-color: #87CEEB;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 2, 2);"
        );
        backButton.setOnAction(e -> stage.setScene(new MainMenu(stage).getScene()));
        backButton.setOnMouseEntered(e -> backButton.setStyle(
                "-fx-background-color: #6BB6D6;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 3, 3);"
        ));
        backButton.setOnMouseExited(e -> backButton.setStyle(
                "-fx-background-color: #87CEEB;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 2, 2);"
        ));

        // High Score
        VBox highScoreBox = new VBox(2);
        highScoreBox.setAlignment(Pos.CENTER);
        Label highScoreLabel = new Label("HIGH SCORE:");
        highScoreLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        highScoreLabel.setTextFill(Color.web("#4A90E2"));
        Label highScoreValue = new Label("230");
        highScoreValue.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
        highScoreValue.setTextFill(Color.web("#4A90E2"));
        highScoreBox.getChildren().addAll(highScoreLabel, highScoreValue);

        // Title
        Label title = new Label("Papa's Learnaria");
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#2C5AA0"));
        title.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.2)));

        // Current Score
        VBox scoreBox = new VBox(2);
        scoreBox.setAlignment(Pos.CENTER);
        Label scoreLabel = new Label("Score:");
        scoreLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 18));
        scoreLabel.setTextFill(Color.web("#333333"));
        Label scoreValue = new Label("50");
        scoreValue.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 24));
        scoreValue.setTextFill(Color.web("#333333"));
        scoreBox.getChildren().addAll(scoreLabel, scoreValue);

        // User display card
        StackPane userCard = new StackPane();
        try {
            ImageView userDisplay = new ImageView(new Image(getClass().getResource("/Images/UserDisplay.png").toExternalForm()));
            userDisplay.setFitWidth(180);
            userDisplay.setFitHeight(70);
            userCard.getChildren().add(userDisplay);
        } catch (Exception e) {
            // Fallback user card
            Rectangle userBg = new Rectangle(180, 70);
            userBg.setFill(Color.WHITE);
            userBg.setArcWidth(20);
            userBg.setArcHeight(20);
            userBg.setStroke(Color.web("#4A90E2"));
            userBg.setStrokeWidth(2);

            HBox userContent = new HBox(10);
            userContent.setAlignment(Pos.CENTER);

            Circle beeIcon = new Circle(20);
            beeIcon.setFill(Color.web("#FFD700"));
            beeIcon.setStroke(Color.BLACK);
            beeIcon.setStrokeWidth(2);

            VBox userInfo = new VBox(2);
            Label userName = new Label("Jake");
            userName.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
            Label userTitle = new Label("Busy Bee");
            userTitle.setFont(Font.font("Comic Sans MS", 12));
            userInfo.getChildren().addAll(userName, userTitle);

            userContent.getChildren().addAll(beeIcon, userInfo);
            userCard.getChildren().addAll(userBg, userContent);
        }

        // Arrange top content
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        topContent.getChildren().addAll(
                backButton,
                highScoreBox,
                spacer1,
                title,
                spacer2,
                scoreBox,
                userCard
        );

        topSection.getChildren().add(topContent);

        // ================== CENTER SECTION ==================
        VBox centerSection = new VBox(30);
        centerSection.setAlignment(Pos.CENTER);
        centerSection.setPadding(new Insets(30));

        // Timer
        Label timer = new Label("2:39s");
        timer.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 48));
        timer.setTextFill(Color.web("#FF4444"));
        timer.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.3)));

        // Word display card
        StackPane wordCard = new StackPane();
        wordCard.setMaxWidth(500);
        wordCard.setPrefHeight(200);

        // Card background
        Rectangle cardBg = new Rectangle(500, 200);
        cardBg.setFill(Color.WHITE);
        cardBg.setArcWidth(30);
        cardBg.setArcHeight(30);
        cardBg.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.15)));

        // Lines container
        VBox linesContainer = new VBox(0);
        linesContainer.setAlignment(Pos.CENTER);
        linesContainer.setPadding(new Insets(20));

        // Top line (red)
        Line topLine = new Line(0, 0, 400, 0);
        topLine.setStroke(Color.web("#FF6B6B"));
        topLine.setStrokeWidth(2);

        // Word
        Label word = new Label("Jump");
        word.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, 64));
        word.setTextFill(Color.web("#333333"));
        word.setPadding(new Insets(10, 0, 10, 0));

        // Middle lines (blue)
        Line middleLine = new Line(0, 0, 400, 0);
        middleLine.setStroke(Color.web("#4ECDC4"));
        middleLine.setStrokeWidth(2);

        // Bottom line (blue)
        Line bottomLine = new Line(0, 0, 400, 0);
        bottomLine.setStroke(Color.web("#4A90E2"));
        bottomLine.setStrokeWidth(2);

        linesContainer.getChildren().addAll(topLine, word, middleLine, bottomLine);
        wordCard.getChildren().addAll(cardBg, linesContainer);

        centerSection.getChildren().addAll(timer, wordCard);

        // ================== BOTTOM SECTION (BUCKETS) ==================
        HBox bucketsSection = new HBox(80);
        bucketsSection.setAlignment(Pos.CENTER);
        bucketsSection.setPadding(new Insets(30));

        // Create buckets
        bucketsSection.getChildren().addAll(
                createBucket("Noun", Color.BLACK, Color.web("#333333")),
                createBucket("Verb", Color.web("#FF6B6B"), Color.web("#FF6B6B")),
                createBucket("Adjective", Color.web("#4A90E2"), Color.web("#4A90E2"))
        );

        // Assemble main container
        mainContainer.setTop(topSection);
        mainContainer.setCenter(centerSection);
        mainContainer.setBottom(bucketsSection);

        root.getChildren().addAll(bgRect, mainContainer);

        scene = new Scene(root, 1024, 768);
    }

    private VBox createBucket(String label, Color symbolColor, Color labelColor) {
        VBox bucketContainer = new VBox(10);
        bucketContainer.setAlignment(Pos.CENTER);

        // Bucket stack pane for layering
        StackPane bucketStack = new StackPane();

        try {
            // Try to load bucket image
            ImageView bucketImage = new ImageView(new Image(getClass().getResource("/Images/bucket.png").toExternalForm()));
            bucketImage.setFitWidth(120);
            bucketImage.setFitHeight(120);
            bucketStack.getChildren().add(bucketImage);
        } catch (Exception e) {
            // Fallback bucket drawing
            VBox bucket = new VBox(0);
            bucket.setAlignment(Pos.CENTER);

            // Bucket top
            Rectangle bucketTop = new Rectangle(100, 10);
            bucketTop.setFill(Color.web("#8B8B8B"));
            bucketTop.setArcWidth(5);
            bucketTop.setArcHeight(5);

            // Bucket body (trapezoid effect using polygon would be better, using rectangle for simplicity)
            Rectangle bucketBody = new Rectangle(90, 80);
            bucketBody.setFill(Color.web("#C0C0C0"));
            bucketBody.setStroke(Color.web("#707070"));
            bucketBody.setStrokeWidth(2);
            bucketBody.setArcWidth(10);
            bucketBody.setArcHeight(10);

            bucket.getChildren().addAll(bucketTop, bucketBody);
            bucketStack.getChildren().add(bucket);
        }

        // Add symbol on bucket
        if (label.equals("Noun")) {
            Label triangle = new Label("▲");
            triangle.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            triangle.setTextFill(symbolColor);
            bucketStack.getChildren().add(triangle);
        } else if (label.equals("Verb")) {
            Circle circle = new Circle(20);
            circle.setFill(symbolColor);
            bucketStack.getChildren().add(circle);
        } else if (label.equals("Adjective")) {
            Label triangle = new Label("△");
            triangle.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            triangle.setTextFill(symbolColor);
            bucketStack.getChildren().add(triangle);
        }

        // Bucket label
        Label bucketLabel = new Label(label);
        bucketLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        bucketLabel.setTextFill(labelColor);

        bucketContainer.getChildren().addAll(bucketStack, bucketLabel);

        // Add hover effect
        bucketContainer.setOnMouseEntered(e -> {
            bucketContainer.setScaleX(1.1);
            bucketContainer.setScaleY(1.1);
        });
        bucketContainer.setOnMouseExited(e -> {
            bucketContainer.setScaleX(1.0);
            bucketContainer.setScaleY(1.0);
        });

        return bucketContainer;
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}