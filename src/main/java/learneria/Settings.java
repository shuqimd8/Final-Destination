package learneria;

import javafx.scene.Scene;


public class Settings {

    private static String colourBlindMode = "default"; // current mode


    public static void applyColourBlind(Scene scene) {
        if (scene == null || scene.getRoot() == null) return;

        // Remove all possible modes first
        scene.getRoot().getStyleClass().removeAll("colourblind-rg", "colourblind-by");

        // Apply based on selected mode
        if ("rg".equals(colourBlindMode)) {
            scene.getRoot().getStyleClass().add("colourblind-rg");
        } else if ("by".equals(colourBlindMode)) {
            scene.getRoot().getStyleClass().add("colourblind-by");
        }
    }


    public static void setColourBlindMode(String mode) {
        if (mode == null) return;
        switch (mode) {
            case "rg":
            case "by":
            case "default":
                colourBlindMode = mode;
                break;
            default:
                System.out.println("⚠ Unknown mode: " + mode);
        }
    }


    public static String getColourBlindMode() {
        return colourBlindMode;
    }
}
