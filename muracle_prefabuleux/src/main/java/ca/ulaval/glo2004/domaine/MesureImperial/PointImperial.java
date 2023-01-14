package ca.ulaval.glo2004.domaine.MesureImperial;

import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author anto-
 */
public class PointImperial implements Serializable{
    private Imperial posX;
    private Imperial posY;
    
    public PointImperial(){
        this.posX = new Imperial();
        this.posY = new Imperial();
    }
    
    public PointImperial(Imperial posX, Imperial posY){
        this.posX = posX;
        this.posY = posY;
    }
    public PointImperial(int posX, int posY){
        this(new Imperial(posX), new Imperial(posY));
    }
    public PointImperial(Point point){
        this(Imperial.getImperialFromPixelValue(point.getX()), Imperial.getImperialFromPixelValue(point.getY()));
    }
    
    public Imperial getX(){
        return posX;
    }
    public Imperial getY(){
        return posY;
    }
    
    public PointImperial add(Imperial X, Imperial Y){
        return PointImperial.add(this, new PointImperial(X, Y));
    }
    public PointImperial add(PointImperial imp){
        return PointImperial.add(this, imp);
    }
    public PointImperial substract(Imperial X, Imperial Y){
        return PointImperial.substract(this, new PointImperial(X, Y));
    }
    public PointImperial substract(PointImperial imp){
        return PointImperial.substract(this, imp);
    }
    
    public static PointImperial add(PointImperial imp1, PointImperial imp2){
        return new PointImperial(imp1.getX().add(imp2.getX()), imp1.getY().add(imp2.getY()));
    }
    public static PointImperial substract(PointImperial imp1, PointImperial imp2){
        return new PointImperial(imp1.getX().substract(imp2.getX()), imp1.getY().substract(imp2.getY()));
    }
    
    public static double getDist(PointImperial imp1, PointImperial imp2){
        return Math.sqrt(Math.pow(imp1.getX().getDoublePixelValue()-imp2.getX().getDoublePixelValue(),2) + Math.pow(imp1.getY().getDoublePixelValue()-imp2.getY().getDoublePixelValue(),2));
    }
    
    public boolean equals(PointImperial point){
        return this.getX().equals(point.getX()) && this.getY().equals(point.getY());
    }
    
    @Override
    public String toString(){
        return "(" + getX() + ", " + getY() + ")";
    }
}
