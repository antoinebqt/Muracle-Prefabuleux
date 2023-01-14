/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.domaine.svg;

import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import ca.ulaval.glo2004.domaine.Polygone;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 *
 * @author anto-
 */
public class SVGHelper {
    
    public static final String STARTING_TAGS = "<?xml version=\"1.0\" standalone=\"no\"?>\n"
            + "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \n \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n"
            + "<svg width=\"100%\" height=\"100%\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink= \"http://www.w3.org/1999/xlink\">\n";
    public static final String ENDING_TAGS = "</svg>";
    
    private ArrayList<Polygone> polygones = new ArrayList<Polygone>();
    public SVGHelper(){
        
    }
    public void clear(){
        polygones.clear();
    }
    public void addPolygon(Polygone polygone){
        polygones.add(polygone);
    }
    
    public static String convertPolygonToTag(Polygone polygone){
        //Je divise par le ratioPixelPouce pour obtenir des coordonnées visible dans un browser web, sinon j'avais des valeurs plus grande que 10000 qui sont trop grande pour être affiché.
        String tag = "<polygon points=\"";
        int ratio = (Imperial.ratioPixelPouce / 4);
        for(Point point : polygone.getPoints()){
            tag += point.x/ratio + "," + point.y/ratio + " ";
        }
        tag += "\" style=\"fill:none;stroke:black;stroke-width:1\" />\n";
        return tag;
    }
    
    public String toSVG(){
        String svg = STARTING_TAGS;
        for(Polygone polygone : polygones){
            svg += convertPolygonToTag(polygone);
        }
        svg += ENDING_TAGS;
        return svg;
    }
}
