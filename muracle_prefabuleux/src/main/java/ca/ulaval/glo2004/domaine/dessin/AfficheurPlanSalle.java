/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.domaine.dessin;

import ca.ulaval.glo2004.domaine.ControleurSalle;
import ca.ulaval.glo2004.domaine.Polygone;
import ca.ulaval.glo2004.gui.ElementCouleur;
import ca.ulaval.glo2004.gui.Enum.TypePolygone;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Antoine
 */
public class AfficheurPlanSalle extends Afficheur {
    

    public AfficheurPlanSalle(ControleurSalle controleur, ElementCouleur elementCouleur, Dimension initialDimension) {
        super(controleur, elementCouleur, initialDimension);
    }

    @Override
    public void draw(Graphics2D g2) {
        drawGrille(g2);
        // Regle l'epaisseur du materiel du mur.
        g2.setStroke(new BasicStroke((float)controleur.getEpaisseurMateriaux()));
        
        // Chaque polygone de la salle en vue de plan
        ArrayList<Polygone> polygones = controleur.getPolygonesSalle();
        ArrayList<Polygone> backupDisplayLast = new ArrayList<>();
        for (Polygone polygone : polygones){
            drawAvecCouleur(g2, polygone);
            if(polygone.getTypePolygone() == TypePolygone.RETOUR_AIR) {
                backupDisplayLast.add(polygone);
            }
        }
        if (controleur.getMurSelectedPolygone() != null) {
            Polygone murSelectedPolygone = controleur.getMurSelectedPolygone();
            drawAvecCouleur(g2, murSelectedPolygone);
        }
        if (!backupDisplayLast.isEmpty()) {
            for (Polygone backupPolygone : backupDisplayLast) {
                drawAvecCouleur(g2, backupPolygone);
            }
        }
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.RED);
        g2.drawLine(0, 0,0, 0);
    }
}
