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
public class Prise extends Accessoire implements Serializable{

    public static final Imperial LONGUEUR_DEFAUT = new Imperial(2, 0, 1);
    public static final Imperial HAUTEUR_DEFAUT = new Imperial(4, 0, 1);

    public Prise(PointImperial coor) {
        this(coor, LONGUEUR_DEFAUT, HAUTEUR_DEFAUT);
    }

    public Prise(PointImperial coor, Imperial longueur, Imperial hauteur) {
        super(coor, longueur, hauteur);
        type = TypeAccessoire.PRISE;
    }
}
