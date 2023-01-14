package ca.ulaval.glo2004.domaine.Accessoire;

import ca.ulaval.glo2004.domaine.Cote;
import ca.ulaval.glo2004.domaine.ElementSalle;
import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import ca.ulaval.glo2004.domaine.MesureImperial.PointImperial;
import ca.ulaval.glo2004.domaine.Salle;
import ca.ulaval.glo2004.domaine.Separateur;
import ca.ulaval.glo2004.gui.Enum.TypeAccessoire;

import java.util.ArrayList;

/**
 *
 * @author anto-
 */
public abstract class Accessoire extends ElementSalle{

    protected Cote cote;
    protected TypeAccessoire type;
    
    public Accessoire(Cote cote, PointImperial coor, Imperial longueur, Imperial hauteur){
        super(coor, longueur, hauteur, new Imperial());
        this.cote = cote;
    }
    
    public Accessoire(PointImperial coor, Imperial longueur, Imperial hauteur){
        this(null, coor, longueur, hauteur);
    }
    
    public Cote getCote(){
        return cote;
    }
    
    public void setCote(Cote cote){
        this.cote = cote;
    }
    
    @Override
    public Salle getSalle() {
        return cote.getSalle();
    }
    @Override
    public void calculeDisposition() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public String toString(){
        return "      Type: " + this.getClass().getSimpleName() + "\n      Position: " + getCoor() + "\n      Longueur: " + getLongueur() + "\n      Hauteur: " + getHauteur(); 
    }

    public TypeAccessoire getType() {
        return type;
    }

    public Imperial getMargeMarche() {
        return new Imperial(0);
    }

    public void adjustPosY() {}

    public void setMargeMarche(Imperial imperialFromString) {}

}
