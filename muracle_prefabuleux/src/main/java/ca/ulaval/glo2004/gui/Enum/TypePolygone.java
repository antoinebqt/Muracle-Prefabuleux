package ca.ulaval.glo2004.gui.Enum;


/**
 *
 * @author Antoine
 */
public enum TypePolygone {
    COTE_NORD("Nord"),
    COTE_SUD("Sud"),
    COTE_EST("Est"),
    COTE_OUEST("Ouest"),
    PRISE("Prise"),
    PORTE("Porte"),
    FENETRE("Fenêtre"),
    RETOUR_AIR("Retour Air"),
    MUR("Mur"),
    PANNEAU("Panneau"),
    SEPARATEUR("Séparateur"),
    GRILLE("Grille");


    private final String value;

    TypePolygone(String value) {
        this.value = value;
    }

    public static TypePolygone fromString(String value) {
        for (TypePolygone type : TypePolygone.values()) {
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
