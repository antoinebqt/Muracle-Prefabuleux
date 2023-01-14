package ca.ulaval.glo2004.domaine.dessin;

import ca.ulaval.glo2004.domaine.ControleurSalle;
import ca.ulaval.glo2004.domaine.Polygone;
import ca.ulaval.glo2004.gui.ElementCouleur;
import ca.ulaval.glo2004.gui.Enum.TypePolygone;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author Antoine
 */
public abstract class Afficheur {
    
    protected final ControleurSalle controleur;
    protected ElementCouleur elementCouleur;
    private Dimension initialDimension;
    
    public Afficheur (ControleurSalle controleur, ElementCouleur elementCouleur, Dimension initialDimension) {
        this.controleur = controleur;
        this.elementCouleur = elementCouleur;
        this.initialDimension = initialDimension;
    }
    
    protected void drawAvecCouleur(Graphics2D g2, Polygone polygone) {
        TypePolygone type = polygone.getTypePolygone();
        Color couleur = elementCouleur.getCouleur(type);
        g2.setColor(couleur.darker());
        g2.fill(polygone);
        g2.setColor(polygone.getColor());
        g2.draw(polygone);
    }
    
    protected void drawGrille(Graphics2D g2) {
        Color couleur = elementCouleur.getCouleur(TypePolygone.GRILLE);
        g2.setColor(couleur.darker());
        for (int i = -10000; i < 10001; i++) {
            int pos = i * controleur.getEspacementGrille();
            g2.drawLine(pos,Integer.MIN_VALUE,pos,Integer.MAX_VALUE);
            g2.drawLine(Integer.MIN_VALUE, pos,Integer.MAX_VALUE,pos);
        }
    }
    
    public abstract void draw(Graphics2D g2);
    
}
