package ca.ulaval.glo2004.domaine.dessin;

import ca.ulaval.glo2004.domaine.ControleurSalle;
import ca.ulaval.glo2004.gui.Enum.Face;
import ca.ulaval.glo2004.gui.Enum.Orientation;
import ca.ulaval.glo2004.domaine.Polygone;
import ca.ulaval.glo2004.gui.ElementCouleur;
import ca.ulaval.glo2004.gui.Enum.TypePolygone;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Antoine
 */
public class AfficheurElevationCote extends Afficheur {
    
    private static TypePolygone[] POLYGONES_INTERIEUR = {TypePolygone.PRISE, TypePolygone.RETOUR_AIR};
    Orientation orientation;
    Face face;
    

    public AfficheurElevationCote(ControleurSalle controleur, ElementCouleur elementCouleur, Dimension initialDimension, Orientation orientation, Face face) {
        super(controleur, elementCouleur, initialDimension);
        this.orientation = orientation;
        this.face = face;
    }

    @Override
    public void draw(Graphics2D g2) {
        drawGrille(g2);

        ArrayList<Polygone> polygones = controleur.getPolygonesCote(orientation);
        ArrayList<Polygone> polygonesAccessoires = controleur.getPolygonesAccessoires(orientation);
        ArrayList<Polygone> polygonesAccessoireDrawable = new ArrayList<>(polygonesAccessoires);
               
        for (Polygone polygone : polygones) {
            polygone.resetPolygon();
        }
        // Regle l'epaisseur du materiel du mur.
        g2.setStroke(new BasicStroke((float)controleur.getEpaisseurMateriaux()));
//        g2.setStroke(new BasicStroke(1));

        // Si mur exterieur inverse l'affichage et supprime les polygones qui doivent apparaitre qu'a l'intérieur
        if (face == Face.EXTERIEUR) {
            // Ajout de l'epaisseur du mur pour l'exterieur
            Polygone lastMurPolygone = getLastMur(polygones);
            if (lastMurPolygone == polygones.get(0)) {
                lastMurPolygone.addMargePolygone((int)controleur.getEpaisseur(),(int)controleur.getEpaisseur());
            }
            else {
                polygones.get(0).addMargePolygone((int)controleur.getEpaisseur(),0);
                lastMurPolygone.addMargePolygone(0,(int)controleur.getEpaisseur());
            }
            g2 = transformGraphicsForExterieur(g2);
            
            polygonesAccessoireDrawable = removePolyInterieur(polygonesAccessoires);
        }
        else {
            // Ajout de l'epaisseur du mur pour l'interieur
            polygones = ajoutPolygoneEpaisseurMurInterieur(polygones, (int)controleur.getEpaisseur());
        }
        
        // Chaque polygone de la salle en vue de cote
        for (Polygone polygone : polygones) {
            drawAvecCouleur(g2, polygone);
        }
        
        // Pour que le polygone selectionne soit par dessus les autres polygones
        if (controleur.getMurSelectedPolygone() != null) {
            Polygone selectedPolygone = controleur.getMurSelectedPolygone();
            g2.setColor(selectedPolygone.getColor());
            g2.draw(selectedPolygone);    
        }
        
        // Affiche tous les polygones des accessoires par dessus ceux du mur
        for (Polygone polygone : polygonesAccessoireDrawable){
            drawAvecCouleur(g2, polygone);
        }
    }

    private boolean isInterieur(Polygone polygone) {
        TypePolygone polygoneType = polygone.getTypePolygone();
        for (TypePolygone type : POLYGONES_INTERIEUR) {
            if (polygoneType == type) {
                return true;
            }
        }
        return false;
    }
    
    private Polygone getLastMur(ArrayList<Polygone> polygones) {
        Polygone lastMurPolygone = polygones.get(0);
        int i = 1;
        while (lastMurPolygone == polygones.get(0) && i < polygones.size()) {
            if(polygones.get(polygones.size() - i).getTypePolygone() == TypePolygone.MUR) {
                lastMurPolygone = polygones.get(polygones.size() - i);
            }
            i++;
        }
        return lastMurPolygone;
    }
    
    private ArrayList<Polygone> ajoutPolygoneEpaisseurMurInterieur(ArrayList<Polygone> oldList, int margeEpaisseur) {
        Polygone polygoneMargeDebut = new Polygone();
        ArrayList<Point> pointsPremierMur = oldList.get(0).getPoints();
        Polygone polygoneMargeFin = new Polygone();
        ArrayList<Point> pointsDernierMur = getLastMur(oldList).getPoints();
        
        polygoneMargeDebut.addPoint(pointsPremierMur.get(0).x - margeEpaisseur, pointsPremierMur.get(0).y);
        polygoneMargeDebut.addPoint(pointsPremierMur.get(0).x, pointsPremierMur.get(0).y);
        polygoneMargeDebut.addPoint(pointsPremierMur.get(3).x, pointsPremierMur.get(3).y);
        polygoneMargeDebut.addPoint(pointsPremierMur.get(3).x - margeEpaisseur, pointsPremierMur.get(3).y);
        polygoneMargeDebut.setColor(Color.DARK_GRAY);
        
        polygoneMargeFin.addPoint(pointsDernierMur.get(1).x, pointsDernierMur.get(1).y);
        polygoneMargeFin.addPoint(pointsDernierMur.get(1).x + margeEpaisseur, pointsDernierMur.get(1).y);
        polygoneMargeFin.addPoint(pointsDernierMur.get(2).x + margeEpaisseur, pointsDernierMur.get(2).y);
        polygoneMargeFin.addPoint(pointsDernierMur.get(2).x, pointsDernierMur.get(2).y);
        polygoneMargeFin.setColor(Color.DARK_GRAY);
        oldList.add(0,polygoneMargeDebut);
        oldList.add(0,polygoneMargeFin);
        return oldList;
    }

    private Graphics2D transformGraphicsForExterieur(Graphics2D g2) {
        g2.rotate(Math.toRadians(180));
        g2.scale(1, -1);
        g2.translate(-(controleur.getLongueurCote(orientation)),0);
        return g2;
    }

    private ArrayList<Polygone> removePolyInterieur(ArrayList<Polygone> polygonesAccessoires) {
        ArrayList<Polygone> polygonesDrawable = new ArrayList<>(polygonesAccessoires);
        for (Polygone polygone : polygonesAccessoires){
                if(isInterieur(polygone)) {
                    polygonesDrawable.remove(polygone);
                }
        }
        return polygonesDrawable;
    }
}
