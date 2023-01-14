package ca.ulaval.glo2004.domaine.dtos;

/**
 *
 * @author Antoine
 */
public class MurParamDTO {
    
    private String positionX;
    private String longueur;
    private String hauteur;
    private String poidsPanneauInterieur;
    private String poidsPanneauExterieur;
    
    public MurParamDTO(String positionX, String longueur, String hauteur, String poidsPanneauInterieur, String poidsPanneauExterieur) {   
        this.positionX = positionX;
        this.longueur = longueur;
        this.hauteur = hauteur;
        this.poidsPanneauExterieur = poidsPanneauExterieur;
        this.poidsPanneauInterieur = poidsPanneauInterieur;
        
    }

    public String poidsPanneauInterieur() {
        return poidsPanneauInterieur;
    }

    public String poidsPanneauExterieur() {
        return poidsPanneauExterieur;
    }
    
    public String positionX() {
        return positionX;
    }
    
    
    public String longueur() {
        return longueur;
    }
    
    public String hauteur() {
        return hauteur;
    }
    
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Position X " + positionX());
        sb.append("\nLongueur : " + longueur());
        sb.append("\nHauteur : " + hauteur());
        sb.append("\n Poids du panneau interieur "  + poidsPanneauInterieur());
        sb.append("\n Poids du panneau exterieur "  + poidsPanneauExterieur());
        
        return sb.toString();
    }
}
