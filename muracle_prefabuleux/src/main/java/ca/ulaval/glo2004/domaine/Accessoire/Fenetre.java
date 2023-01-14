package ca.ulaval.glo2004.domaine.Accessoire;

import ca.ulaval.glo2004.domaine.Accessoire.Accessoire;
import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import ca.ulaval.glo2004.domaine.MesureImperial.PointImperial;
import ca.ulaval.glo2004.gui.Enum.TypeAccessoire;
import java.io.Serializable;

/**
 *
 * @author anto-
 */
public class Fenetre extends Accessoire implements Serializable{

    public static final Imperial MARGE_DEFAUT = new Imperial(0, 1, 8);
    public static final Imperial LONGUEUR_DEFAUT = new Imperial(30, 0, 1);
    public static final Imperial HAUTEUR_DEFAUT = new Imperial(30, 0, 1);
    private Imperial margeMoulure;

    public Fenetre(PointImperial coor) {
        this(coor, LONGUEUR_DEFAUT, HAUTEUR_DEFAUT, MARGE_DEFAUT);
    }

    public Fenetre(PointImperial coor, Imperial longueur, Imperial hauteur) {
        this(coor, longueur, hauteur, MARGE_DEFAUT);
    }

    public Fenetre(PointImperial coor, Imperial longueur, Imperial hauteur, Imperial margeMoulure) {
        super(coor, longueur, hauteur);
        this.margeMoulure = margeMoulure;
        type = TypeAccessoire.FENETRE;
    }

    public Imperial getMargeMoulure() {
        return margeMoulure;
    }

    public void setMargeMoulure(Imperial marge) {
        margeMoulure = marge;
    }
}
