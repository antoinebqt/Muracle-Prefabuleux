package ca.ulaval.glo2004.domaine;

import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import ca.ulaval.glo2004.gui.Enum.TypePolygone;
import ca.ulaval.glo2004.domaine.MesureImperial.PointImperial;
import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Polygon;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Cette classe permet d'obtenir la liste des points du polygone, 
 * contrairement a la classe Java Polygon qui ne permet que de les set.
 * @author jeamm
 */
public class Polygone extends Polygon implements Serializable{
    
    private ArrayList<Point> pointsPixel;
    private ArrayList<PointImperial> pointsImperial;
    private Color color;
    private TypePolygone typePolygone;
    
    public Polygone() {
        super();
        pointsPixel = new ArrayList<Point>();
        typePolygone = TypePolygone.MUR;
    }
    public Polygone(ArrayList<PointImperial> pointsImperial, TypePolygone type) {
        setPoints(pointsImperial);
        typePolygone = type;
        color = Color.GRAY;
    }
    public Polygone(ArrayList<PointImperial> pointsImperial, TypePolygone type, boolean isSelected) {
        setPoints(pointsImperial);
        typePolygone = type;
        color = Color.GRAY;
    }
    
    public ArrayList<Point> getPoints() {
        return pointsPixel;
    }
    
    @Override
    public void addPoint(int x, int y) {
        super.addPoint(x,y);
        pointsPixel.add(new Point(x, y));
    }
    
    public void setPoints(ArrayList<PointImperial> pointsImperial) {
        this.pointsImperial = pointsImperial;
        this.pointsPixel = new ArrayList<>();
        reset();
        for (PointImperial point : this.pointsImperial) {
            int x = (int) point.getX().getDoublePixelValue();
            int y = (int) point.getY().getDoublePixelValue();

            addPoint(x, y);
        }
    }
    
    public void resetPolygon() {
        super.reset();
        for (Point point : pointsPixel) {
             super.addPoint(point.x,point.y);
        }
    }
    
    public ArrayList<PointImperial> getImperialPoints() {
        return pointsImperial;
    }

    public TypePolygone getTypePolygone() {
        return typePolygone;
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public void addMargePolygone(int margeDebut, int margeFin) {
        super.reset();
        super.addPoint(pointsPixel.get(0).x - margeDebut,pointsPixel.get(0).y);
        super.addPoint(pointsPixel.get(1).x + margeFin,pointsPixel.get(1).y);
        super.addPoint(pointsPixel.get(2).x + margeFin,pointsPixel.get(2).y);
        super.addPoint(pointsPixel.get(3).x - margeDebut,pointsPixel.get(3).y);        
    }
    
    public Imperial getLowerBoundX(){
        Imperial lowerBound = this.getImperialPoints().get(0).getX();
        for(PointImperial point: pointsImperial){
            if(!point.getX().isGreater(lowerBound)){
                lowerBound = point.getX();
            }
        }
        return lowerBound;
    }
    public Imperial getUpperBoundX(){
        Imperial upperBound = this.getImperialPoints().get(0).getX();
        for(PointImperial point: pointsImperial){
            if(point.getX().isGreater(upperBound)){
                upperBound = point.getX();
            }
        }
        return upperBound;
    }
    public Imperial getLenghtX(){
        return getUpperBoundX().substract(getLowerBoundX());
    }
    public Imperial getLowerBoundY(){
        Imperial lowerBound = this.getImperialPoints().get(0).getY();
        for(PointImperial point: pointsImperial){
            if(!point.getY().isGreater(lowerBound)){
                lowerBound = point.getY();
            }
        }
        return lowerBound;
    }
    public Imperial getUpperBoundY(){
        Imperial upperBound = this.getImperialPoints().get(0).getY();
        for(PointImperial point: pointsImperial){
            if(point.getY().isGreater(upperBound)){
                upperBound = point.getY();
            }
        }
        return upperBound;
    }
    public Imperial getLenghtY(){
        return getUpperBoundY().substract(getLowerBoundY());
    }
    
    public boolean contains(PointImperial p){
        return Imperial.isBetween(p.getX(), getLowerBoundX(), getUpperBoundX()) && Imperial.isBetween(p.getY(), getLowerBoundY(), getUpperBoundY());
    }
    
    public boolean touches(Polygone p){
        boolean touch = false;
        ArrayList<PointImperial> poi = new ArrayList<>(Arrays.asList(new PointImperial(90,12),new PointImperial(128,12),new PointImperial(128,100),new PointImperial(90,100)));
        Polygone p1 = new Polygone(poi, TypePolygone.FENETRE);
                for (PointImperial point : p.getImperialPoints()){
                    if (this.contains(point)){
                        touch = true;
                    }
                }
                for (PointImperial point : this.getImperialPoints()){
                    if (p.contains(point)){
                        touch = true;
                    }
                }
        return touch;
    }
    
    public Polygone getReflection(boolean horizontal){
        ArrayList<PointImperial> newPoints = new ArrayList<PointImperial>();
        if(horizontal){
            Imperial decalageX = this.getUpperBoundX().add(this.getLowerBoundX());
            Imperial decalageY = this.getUpperBoundY().add(this.getLowerBoundY());
            for(PointImperial point : this.getImperialPoints()){
                newPoints.add(new PointImperial(decalageX.substract(point.getX()), decalageY.substract(point.getY())));
            }
        }else{
            Imperial decalageY = this.getUpperBoundY().add(this.getLowerBoundY());
            for(PointImperial point : this.getImperialPoints()){
                newPoints.add(new PointImperial(point.getX(), decalageY.substract(point.getY())));
            }
        }
        //pour garder les points dans le sens horaire
        /*ArrayList<PointImperial> newPointsInverse = new ArrayList<PointImperial>();
        for(int i = newPoints.size()-1; i >= 0; i--){
            newPointsInverse.add(newPoints.get(i));
        }*/
        return new Polygone(newPoints, this.getTypePolygone());
    }
    public Polygone getTranslation(PointImperial translation){
        ArrayList<PointImperial> newPoints = new ArrayList<PointImperial>();
        for(PointImperial point : this.getImperialPoints()){
            newPoints.add(new PointImperial(point.getX().add(translation.getX()), point.getY().add(translation.getY())));
        }
        return new Polygone(newPoints, this.getTypePolygone());
    }
    public Polygone getTranslation(Imperial translation, boolean horizontal){
        if(horizontal){
            return this.getTranslation(new PointImperial(translation, new Imperial()));
        } else {
            return this.getTranslation(new PointImperial(new Imperial(), translation));
        }
    }
    
    public double getAreaPixel(){
        double sum = 0;
        ArrayList<Point> points = this.getPoints();
        for (int i = 0; i < points.size(); i++)
        {
            Point currentPoint = points.get(i);
            int nextPointIndex = i+1;
            if(nextPointIndex >= points.size())
                nextPointIndex = 0;
            Point nextPoint = points.get(nextPointIndex);
            sum += (double)currentPoint.x*(double)nextPoint.y - (double)currentPoint.y*(double)nextPoint.x;
        }
        return Math.abs(sum / 2);
    }
    public Imperial getAreaImperial(){
        //pouce carre donc on divise par le ratio avant de convertir (dans la convertion on redivise par le ratio)
        return Imperial.getImperialValue(getAreaPixel()/Math.pow(Imperial.ratioPixelPouce,2));
    }
    
    @Override
    public String toString(){
        return this.getImperialPoints().toString();
    }
}
