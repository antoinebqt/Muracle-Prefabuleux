package ca.ulaval.glo2004.domaine.Accessoire;

import ca.ulaval.glo2004.domaine.Accessoire.Accessoire;
import ca.ulaval.glo2004.domaine.Cote;
import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import ca.ulaval.glo2004.domaine.MesureImperial.PointImperial;
import ca.ulaval.glo2004.domaine.Mur;
import ca.ulaval.glo2004.domaine.Salle;
import ca.ulaval.glo2004.gui.Enum.TypeAccessoire;
import java.io.Serializable;

/**
 *
 * @author anto-
 */
public class Porte extends Accessoire implements Serializable{

    
    public static final Imperial MARGE_DEFAUT = new Imperial(0, 0, 1);
    public static final Imperial LONGUEUR_DEFAUT = new Imperial(38, 0, 1);
    public static final Imperial HAUTEUR_DEFAUT = new Imperial(88, 0, 1);
    private Imperial margeMarche;

    public Porte(Cote cote, Imperial x) {
        this(cote, x, LONGUEUR_DEFAUT, HAUTEUR_DEFAUT, MARGE_DEFAUT);
    }

    public Porte(Cote cote, Imperial x, Imperial longueur, Imperial hauteur) {
        this(cote, x, longueur, hauteur, MARGE_DEFAUT);
    }

    public Porte(Cote cote, Imperial x, Imperial longueur, Imperial hauteur, Imperial margeMarche) {
        super(cote, new PointImperial(x,margeMarche), longueur, hauteur);
        this.margeMarche = margeMarche;
        this.adjustPosY();
        type = TypeAccessoire.PORTE;
    }
    
    public final Imperial getMargeMarche() {
        return margeMarche;
    }

    public final void setMargeMarche(Imperial marge) {
        margeMarche = marge;
    }
    
    @Override
    public void setPosY(Imperial posY) {
        super.setPosY(Imperial.substract(cote.getHauteur(), Imperial.add(margeMarche, getHauteur())));
        //this.adjustPosY();
    }
    @Override
    public void setCoor(PointImperial coor) {
        super.setCoor(coor);
        this.adjustPosY();
    }
    
    public void adjustPosY() {
        Imperial posY = Imperial.substract(cote.getHauteur(), Imperial.add(margeMarche, getHauteur()));
        super.setPosY(posY);
    }
}
