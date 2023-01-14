package ca.ulaval.glo2004.domaine.dessin;

import ca.ulaval.glo2004.domaine.ControleurSalle;
import ca.ulaval.glo2004.gui.ElementCouleur;
import ca.ulaval.glo2004.gui.Enum.Face;
import ca.ulaval.glo2004.gui.Enum.Orientation;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author Antoine
 */
public class AfficheurElevationMur extends Afficheur {
    
    Orientation orientation;
    Face face;
    

    public AfficheurElevationMur(ControleurSalle controleur, ElementCouleur elementCouleur, Dimension initialDimension, Orientation orientation, Face face) {
        super(controleur, elementCouleur, initialDimension);
        this.orientation = orientation;
        this.face = face;
    }

    @Override
    public void draw(Graphics2D g2) {
        drawGrille(g2);
        g2.setStroke(new BasicStroke(5));
        g2.setColor(Color.DARK_GRAY);
        g2.drawOval(60, 5, 50, 50);
    }
    
}
