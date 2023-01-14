/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.gui;

import ca.ulaval.glo2004.gui.Enum.TypePolygone;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author Antoine
 */
public class ElementCouleur {
    
    public HashMap<TypePolygone, Color> couleurs;
    
    ElementCouleur() {
        couleurs = new HashMap<>();
        
        couleurs.put(TypePolygone.MUR,Color.GRAY);
        couleurs.put(TypePolygone.PANNEAU,Color.GRAY);
        couleurs.put(TypePolygone.PORTE,Color.LIGHT_GRAY);
        couleurs.put(TypePolygone.PRISE,Color.LIGHT_GRAY);
        couleurs.put(TypePolygone.FENETRE,Color.LIGHT_GRAY);
        couleurs.put(TypePolygone.RETOUR_AIR,Color.LIGHT_GRAY);
        couleurs.put(TypePolygone.SEPARATEUR,Color.LIGHT_GRAY);
        couleurs.put(TypePolygone.GRILLE,Color.GRAY);
    }
    
    public Color getCouleur(TypePolygone type) {
        if (polygoneEstUnCote(type)) {
            return couleurs.get(TypePolygone.MUR);
        }
        return couleurs.get(type);
    }
    
    public void setCouleur(TypePolygone type, Color newColor) {
        couleurs.put(type, newColor);
    }

    public HashMap<TypePolygone, Color> getCouleurs() {
        return couleurs;
    }

    private boolean polygoneEstUnCote(TypePolygone type) {
        if (type == TypePolygone.COTE_EST || type == TypePolygone.COTE_OUEST || type == TypePolygone.COTE_NORD || type == TypePolygone.COTE_SUD) {
            return true;
        }
        return false;
    }
    
}
