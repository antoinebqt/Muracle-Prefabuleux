package ca.ulaval.glo2004.gui.Enum;

/**
 *
 * @author anto-
 */
public enum TypeAccessoire {
    RETOUR_AIR("Retour Air"),
    PORTE("Porte"),
    PRISE("Prise"),
    FENETRE("Fenêtre"),
    SEPARATEUR("Séparateur");

    private final String value;

    TypeAccessoire(String value) {
        this.value = value;
    }

    public static TypeAccessoire fromString(String value) {
        for (TypeAccessoire type : TypeAccessoire.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
