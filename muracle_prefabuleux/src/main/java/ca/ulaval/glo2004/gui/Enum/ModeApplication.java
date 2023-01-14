package ca.ulaval.glo2004.gui.Enum;

/**
 *
 * @author anto-
 */
public enum ModeApplication {
    SELECTION("Sélection"),
    DEPLACEMENT("Déplacement"),
    AJOUT("Ajout");

    private final String value;

    ModeApplication(String value) {
        this.value = value;
    }

    public static ModeApplication fromString(String value) {
        for (ModeApplication mode : ModeApplication.values()) {
            if (mode.getValue().equals(value)) {
                return mode;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
