package ca.ulaval.glo2004.gui.Enum;

/**
 *
 * @author anto-
 */
public enum Orientation {
    NORD ("Nord"),
    SUD ("Sud"),
    EST ("Est"),
    OUEST ("Ouest");
    
    private final String value;

    Orientation(String value) {
        this.value = value;
    }

    public static Orientation fromString(String value) {
        for (Orientation orientation : Orientation.values()) {
            if (orientation.getValue().equals(value)) {
                return orientation;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
