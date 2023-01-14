package ca.ulaval.glo2004.domaine;

import ca.ulaval.glo2004.domaine.MesureImperial.PointImperial;
import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author anto-
 */
public abstract class ElementSalle implements Serializable{

    /**
     * Les positions sont par rapport au cote
     */
    private boolean isCoinDebut;
    private boolean isCoinFin;

    private Imperial posX;
    private Imperial posY;
    protected Imperial[] dimensions; //lenght x, y, z (only if needed)
    protected Imperial epaisseurMateriaux;
    private Polygone polygone;
    private boolean selected = false;
    private boolean errorDetected = false;
    private boolean collisionMur = false;
    private boolean collisionAccessoire = false;
    
    public ElementSalle(){
        this(new Imperial(),new Imperial(),new Imperial(),new Imperial(),new Imperial());
    }

    public ElementSalle(PointImperial pos, Imperial longueur, Imperial hauteur, Imperial largeur) {
        this(pos.getX(), pos.getY(), longueur, hauteur, largeur);
    }

    public ElementSalle(Imperial posX, Imperial posY, Imperial longueur, Imperial hauteur, Imperial largeur) {
        this.dimensions = new Imperial[]{longueur, hauteur, largeur};
        this.posX = posX;
        this.posY = posY;
        this.epaisseurMateriaux = new Imperial(0, 1, 8);
    }
    
    public boolean isCoinDebut() {
        return isCoinDebut;
    }
    
    public void isCoinDebut (boolean value) {
        isCoinDebut = value;
    }
    
    public boolean isCoinFin() {
        return isCoinFin;
    }
    
    public void isCoinFin (boolean value) {
        isCoinFin = value;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected() {
        selected = true;
        if (polygone != null) { polygone.setColor(Color.GREEN); }
    }
    
    public void setUnselected() {
        selected = false;
         if (polygone != null) { polygone.setColor(Color.GRAY); }
    }
    
    public void setErrorDetected() {
        errorDetected = true;
        if (polygone != null) { polygone.setColor(Color.RED); }
    }
    
    public void setNoErrorDetected() {
        errorDetected = false;
        if (polygone != null) { 
            if (selected) {
                polygone.setColor(Color.GREEN);
            } else {
                polygone.setColor(Color.GRAY);
            }
        }
    }
    
    public boolean isValid(){
        return !this.errorDetected;
    }
    
    public void setCollisionMur(boolean enCollision) {
        this.collisionMur = enCollision;
    }
    
    public Imperial getEpaisseurMateriaux () {
        return epaisseurMateriaux;
    }

    public final void setEpaisseurMateriaux(Imperial epaisseur) {
        this.epaisseurMateriaux = epaisseur;
    }

    public Imperial getPosX() {
        return posX;
    }

    public void setPosX(Imperial posX) {
        this.posX = posX;
    }

    public Imperial getPosY() {
        return posY;
    }

    public void setPosY(Imperial posY) {
        this.posY = posY;
    }

    public PointImperial getCoor() {
        return new PointImperial(getPosX(), getPosY());
    }

    public void setCoor(PointImperial coor) {
        this.posX = coor.getX();
        this.posY = coor.getY();
    }

    public Imperial[] getDimensions() {
        return dimensions;
    }

    public Imperial getLongueur() {
        return dimensions[0];
    }

    public void setLongueur(Imperial longueur) {
        dimensions[0] = longueur;
        calculeDisposition();
    }

    public Imperial getHauteur() {
        return dimensions[1];
    }

    public void setHauteur(Imperial hauteur) {
        dimensions[1] = hauteur;
        calculeDisposition();
    }

    public Imperial getLargeur() {
        return dimensions[2];
    }

    public void setLargeur(Imperial largeur) {
        dimensions[2] = largeur;
        calculeDisposition();
    }

    public Polygone getPolygone() {
        return this.polygone;
    }
    
    public void setPolygone(Polygone polygone) {
        this.polygone = polygone;
    }
    
    // Contains seulement en X
    public boolean containsX(Imperial posX) {
        if (polygone == null) { return false; }
        
        double posXPixel = posX.getDoublePixelValue();
        double maxX = getMaxX().getDoublePixelValue();
        double minX = getMinX().getDoublePixelValue();
        return (minX <= posXPixel && posXPixel <= maxX);
    }
    
    public Imperial getMinX(){
        double minX = Double.MAX_VALUE;

        for (PointImperial imperialPoint : polygone.getImperialPoints()) {
            double currentX = imperialPoint.getX().getDoublePixelValue();
            if (currentX < minX){
                minX = currentX;
            }
        }
        return Imperial.getImperialFromPixelValue(minX);
    }
    
    public Imperial getMaxX(){
        double maxX = 0;

        for (PointImperial imperialPoint : polygone.getImperialPoints()) {
            double currentX = imperialPoint.getX().getDoublePixelValue();
            if (currentX > maxX) {
                maxX = currentX;
            }
        }
        return  Imperial.getImperialFromPixelValue(maxX);
    }
    
    // Contains seulement en Y
    public boolean containsY(Imperial posY) {
        if (polygone == null) { return false; }

        double posYPixel = posY.getDoublePixelValue();
        double maxY = getMaxY().getDoublePixelValue();
        double minY = getMinY().getDoublePixelValue();
        return (minY <= posYPixel && posYPixel <= maxY);
    }

    public Imperial getMinY(){
        double minY = Double.MAX_VALUE;

        for (PointImperial imperialPoint : polygone.getImperialPoints()) {
            double currentY = imperialPoint.getY().getDoublePixelValue();
            if (currentY < minY){
                minY = currentY;
            }
        }
        return Imperial.getImperialFromPixelValue(minY);
    }
    
    public Imperial getMaxY(){
        double maxY = 0;

        for (PointImperial imperialPoint : polygone.getImperialPoints()) {
            double currentY = imperialPoint.getY().getDoublePixelValue();
            if (currentY > maxY) {
                maxY = currentY;
            }
        }
        return  Imperial.getImperialFromPixelValue(maxY);
    }
    
    // Verifie si un point est contenue dans l'elementSalle
    public boolean contains(PointImperial p) {
        return (containsX(p.getX()) && containsY(p.getY()));
    }
    
    // Verifie si un polygone est contenue au partiellement dans l'elementSalle
    public boolean contains(Polygone p) {
        for (PointImperial imperialPoint : p.getImperialPoints()) {
            if (contains(imperialPoint)) {
                return true;
            }
        }
        return false;
    }
    
    //vérifie si les deux accessoire se touchent
    public boolean touches(ElementSalle e) {
        return e.getPolygone().touches(this.getPolygone());
    }
    
    // Verifie si un polygone est contenue au complet dans l'elementSalle
    public boolean fullyContains(Polygone p) {
        for (PointImperial imperialPoint : p.getImperialPoints()) {
            if (!contains(imperialPoint)) {
                return false;
            }
        }
        return true;
    }

    public double getAire() {
        return getLongueur().getDoubleValue() * getHauteur().getDoubleValue();
    }
    
    public abstract Salle getSalle();
    
    public abstract void calculeDisposition();
    
}
