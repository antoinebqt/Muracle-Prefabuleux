package ca.ulaval.glo2004.gui.Enum;

/**
 *
 * @author anto-
 */
public enum Face {
    INTERIEUR("Intérieur"),
    EXTERIEUR("Extérieur");

    private final String value;

    Face(String value) {
        this.value = value;
    }

    public static Face fromString(String value) {
        for (Face face : Face.values()) {
            if (face.getValue().equals(value)) {
                return face;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
