package ca.ulaval.glo2004.domaine.dessin;

import ca.ulaval.glo2004.domaine.ControleurSalle;
import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import ca.ulaval.glo2004.domaine.Polygone;
import ca.ulaval.glo2004.domaine.dtos.MurParamDTO;
import ca.ulaval.glo2004.domaine.dtos.SalleParamDTO;
import ca.ulaval.glo2004.gui.ElementCouleur;
import ca.ulaval.glo2004.gui.Enum.Face;
import ca.ulaval.glo2004.gui.Enum.ModeVue;
import ca.ulaval.glo2004.gui.Enum.Orientation;
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
public class AfficheurProfilDecoupagePanneau extends Afficheur {
    
    Orientation orientation;
    Face face;
    

    public AfficheurProfilDecoupagePanneau(ControleurSalle controleur, ElementCouleur elementCouleur, Dimension initialDimension, Orientation orientation, Face face) {
        super(controleur, elementCouleur, initialDimension);
        this.orientation = orientation;
        this.face = face;
    }

    @Override
    public void draw(Graphics2D g2) {
        drawGrille(g2);

        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.RED);
        g2.drawLine(0, 0,0, 0);
        
// TODO : Gerer getMur avec y et x de la classe Cote avant d'implementer ceci
//        ArrayList<Polygone> polygones = controleur.getPolygonesPanneau(ModeVue.PLAN_DECOUPE_INTERIEURE , orientation, 0)
        
//        for (Polygone polygone : polygones){
//            g2.draw(polygone);
//        }
        ArrayList<Polygone> polygones = new ArrayList<Polygone>();
        //polygones.addAll(controleur.getPolygonesSVGPanneauSelectedMur(face == Face.EXTERIEUR)); 
        polygones.addAll(controleur.getPolygonesPanneauSelectedMur(face == Face.EXTERIEUR)); 
        for (Polygone polygone : polygones){
            drawAvecCouleur(g2, polygone);
        }
        //drawDebug(g2);
    }
    
    private void drawDebug(Graphics2D g2){
        try{
            MurParamDTO dto = controleur.getSelectedMurParamDTO();
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.RED);
            g2.drawLine(0, 0, 0, (int)Imperial.getImperialFromString(dto.hauteur()).getDoublePixelValue());
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.RED);
            g2.drawLine(0, 0, (int)Imperial.getImperialFromString(dto.longueur()).getDoublePixelValue(), 0);
        } catch (Exception e){
            
        }
        ArrayList<Polygone> polygonesPanneau = new ArrayList<Polygone>();
        polygonesPanneau.addAll(controleur.getPolygonesPanneauSelectedMur(face == Face.EXTERIEUR)); 
        for (Polygone polygone : polygonesPanneau){
            g2.setColor(Color.GREEN);
            g2.draw(polygone);
        }
        ArrayList<Polygone> polygonesSVG = new ArrayList<Polygone>();
        polygonesSVG.addAll(controleur.getPolygonesSVGPanneauSelectedMur(face == Face.EXTERIEUR)); 
        for (Polygone polygone : polygonesSVG){
            g2.setColor(Color.BLUE);
            g2.draw(polygone);
        }
    }
}
