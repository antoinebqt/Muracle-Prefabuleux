package ca.ulaval.glo2004.domaine;

import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import ca.ulaval.glo2004.domaine.MesureImperial.PointImperial;
import ca.ulaval.glo2004.gui.Enum.Orientation;
import ca.ulaval.glo2004.gui.Enum.TypePolygone;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author anto-
 */
public class Separateur extends ElementSalle implements Comparable<Separateur>, Serializable {

    private Cote cote;
    private Mur mur;

    public Separateur(Cote cote, Mur mur, Imperial posX){
        super();
        this.cote = cote;
        this.mur = mur;
        setPosX(posX);
    }
    
    public Cote getCote(){
        return cote;
    }
    public Orientation getOrientation(){
        return cote.getOrientation();
    }
    
    @Override
    public int compareTo(Separateur s) {
        double difference = this.getPosX().getDoublePixelValue() - s.getPosX().getDoublePixelValue();
        if (difference > 0) {
            return 1;
        } else if (difference == 0) {
            return 0;
        }
        else{
            return -1;
        }
    }
    
    public void setMur(Mur mur) {
        this.mur = mur;
    }

    @Override
    public void setPosX(Imperial posX) {
        super.setPosX(posX);
        this.getCote().sortSeparateurs();
        if(surAccessoire()){
            setErrorDetected();
        }
        else{
            setNoErrorDetected();
        }
    }
    
    public boolean surAccessoire(){
        boolean valide = true;
        for(ElementSalle acc : this.getCote().getAccessoires()){
            if(acc.containsX(this.getPosX())){
                valide = false;
            }
        }
        return !valide;
    }
    
    @Override
    public void setCoor(PointImperial coor) {
        super.setCoor(coor);
        this.getCote().sortSeparateurs();
    }
    
    @Override
    public Salle getSalle() {
        return cote.getSalle();
    }
    @Override
    public void calculeDisposition() {
        ArrayList<PointImperial> newPoints = new ArrayList<PointImperial>();
        newPoints.add( new PointImperial(
        this.mur.getPolygone().getImperialPoints().get(0).getX(),
        this.mur.getPolygone().getImperialPoints().get(0).getY()
        ));
        newPoints.add( new PointImperial(
        this.mur.getPolygone().getImperialPoints().get(3).getX(),
        this.mur.getPolygone().getImperialPoints().get(3).getY()
        ));
        Color colorBackup = Color.GRAY;
        if (getPolygone() != null) { colorBackup = getPolygone().getColor(); }
        setPolygone(new Polygone(newPoints,TypePolygone.SEPARATEUR));
        getPolygone().setColor(colorBackup);
    }
    
    @Override
    public String toString() {
        return this.getCote().getOrientation().name() + " posX: " + this.getPosX().toString();
    }
}
