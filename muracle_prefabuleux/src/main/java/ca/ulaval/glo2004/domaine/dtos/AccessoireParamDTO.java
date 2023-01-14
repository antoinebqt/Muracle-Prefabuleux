package ca.ulaval.glo2004.domaine.dtos;

/**
 *
 * @author Antoine
 */
public class AccessoireParamDTO {
    
    private String positionX;
    private String positionY;
    private String longueur;
    private String hauteur;
    private String margeMarche;
    
    public AccessoireParamDTO(String positionX, String positionY, String longueur, String hauteur, String margeMarche) {   
        this.positionX = positionX;
        this.positionY = positionY;
        this.longueur = longueur;
        this.hauteur = hauteur;
        this.margeMarche = margeMarche;
    }
    
    public String positionX() {
        return positionX;
    }
    
    public String positionY() {
        return positionY;
    }
    
    public String longueur() {
        return longueur;
    }
    
    public String hauteur() {
        return hauteur;
    }
    
    public String margeMarche() {
        return margeMarche;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Position X " + positionX());
        sb.append("\nPosition Y : " + positionY());
        sb.append("\nLongueur : " + longueur());
        sb.append("\nHauteur : " + hauteur());
        
        return sb.toString();
    }
}
