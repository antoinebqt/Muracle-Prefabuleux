package ca.ulaval.glo2004.domaine.Accessoire;

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
public class RetourAir extends Accessoire implements Serializable{
    
    public static final Imperial LONGUEUR_DEFAUT = new Imperial(20);
    public RetourAir(Mur mur) {
        this(mur, LONGUEUR_DEFAUT);
    }
    public RetourAir(Mur mur, Imperial longueur) {
        super(mur.getCote(),calculateCoor(mur, longueur), longueur, mur.getSalle().getHauteurRetourAir());
        type = TypeAccessoire.RETOUR_AIR;
    }
    
    public Mur getMur(){
        return this.getCote().getMurAtX(this.getPosX());
    }
    
    public boolean recenter(){
        Mur mur = this.getMur();
        if (mur != null) {
            // ceci ne cree pas de boucle infini car on appel super.setCoor au lieu de this.setCoor super etant la classe parent
            super.setCoor(calculateCoor(mur, this.getLongueur()));
        } 
        return mur != null;
    }
            
    private static PointImperial calculateCoor(Mur mur, Imperial longueur){
        Salle salle = mur.getSalle();
        Imperial posX = mur.getPosX().add(mur.getLongueur().divide(2)).substract(longueur.divide(2));
        Imperial posY = mur.getHauteur().substract(salle.getHauteurRetourAir().add(salle.getDistanceSolRetourAir()));
        return new PointImperial(posX, posY);
    }
    
    @Override
    public void setPosX(Imperial posX) {
        super.setPosX(posX);
        this.recenter();
    }
    @Override
    public void setCoor(PointImperial coor) {
        super.setCoor(coor);
        this.recenter(); 
    }
    
    @Override
    public void setLongueur(Imperial longueur) {
        super.setLongueur(longueur);
        this.recenter();
    }
}
