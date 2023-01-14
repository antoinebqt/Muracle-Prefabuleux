package ca.ulaval.glo2004.gui.Enum;

/**
 *
 * @author anto-
 */
public enum ModeVue {
    DESSUS ("Dessus"),
    COTE ("Côté"),
    PLAN_DECOUPE_INTERIEUR ("Plan découpe intérieur"),
    PLAN_DECOUPE_EXTERIEUR ("Plan découpe extérieur");
    
    private final String value;
    
    ModeVue(String value) {
        this.value = value;
    }
    
    public static ModeVue fromString(String value) {
        for (ModeVue vue : ModeVue.values()) {
            if (vue.getValue().equals(value)) {
                return vue;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
