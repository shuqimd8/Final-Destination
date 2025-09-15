package learneria;

import javafx.scene.Scene;

/**
 * Settings manager for colour-blind modes.
 * Supports:
 *   - "default" (normal colours)
 *   - "rg" (Red-Green colourblind mode)
 *   - "by" (Blue-Yellow colourblind mode)
 */
public class Settings {

    private static String colourBlindMode = "default"; // current mode

    /**
     * Apply the selected colour-blind mode to a given scene.
     *
     * @param scene the JavaFX scene where styles will be applied
     */
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
        // "default" → do nothing
    }

    /**
     * Set the colour-blind mode.
     *
     * @param mode "default", "rg", or "by"
     */
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

    /**
     * Get the current colour-blind mode.
     *
     * @return the mode string
     */
    public static String getColourBlindMode() {
        return colourBlindMode;
    }
}
