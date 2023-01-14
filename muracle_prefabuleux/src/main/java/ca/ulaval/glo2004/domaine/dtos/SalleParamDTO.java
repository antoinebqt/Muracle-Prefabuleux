package ca.ulaval.glo2004.domaine.dtos;

/**
 *
 * @author Antoine
 */
public class SalleParamDTO {
    
    private String longueur;
    private String largeur;
    private String hauteur;
    private String epaisseur;
    private String margePliage;
    private String margeRepli;
    private String hauteurRetourAir;
    private String distanceSolRetourAir;
    private String hauteurTrouRetourAir;
    private String longueurPliSoudure;
    private String anglePliSoudure;
    
    
    public SalleParamDTO(String longueur, String largeur, String hauteur, String epaisseur,
            String margePliage, String margeRepli, String hauteurRetourAir,
            String distanceSolRetourAir, String hauteurTrouRetourAir, 
            String largeurPliSoudure, String anglePliSoudure) {
        
    this.longueur = longueur;
    this.largeur = largeur;
    this.hauteur = hauteur;
    this.epaisseur = epaisseur;
    this.margePliage = margePliage;
    this.margeRepli = margeRepli;
    this.hauteurRetourAir = hauteurRetourAir;
    this.distanceSolRetourAir = distanceSolRetourAir;
    this.hauteurTrouRetourAir = hauteurTrouRetourAir;
    this.longueurPliSoudure = largeurPliSoudure;
    this.anglePliSoudure = anglePliSoudure;
    
    }
    
    public String longueur() {
        return longueur;
    }
    
    public String largeur() {
        return largeur;
    }
    
    public String hauteur() {
        return hauteur;
    }
    
    public String epaisseur() {
        return epaisseur;
    }
    
    public String margeRepli() {
        return margeRepli;
    }
    
    public String margePliage() {
        return margePliage;
    }
    
    public String hauteurRetourAir() {
        return hauteurRetourAir;
    }
    
    
    public String distanceSolRetourAir() {
        return distanceSolRetourAir;
    }
    
    
    public String hauteurTrouRetourAir() {
        return hauteurTrouRetourAir;
    }
    
    
    public String longueurPliSoudure() {
        return longueurPliSoudure;
    }
    
    
    public String anglePliSoudure() {
        return anglePliSoudure;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Longueur : " + longueur());
        sb.append("\nLargeur : " + largeur());
        sb.append("\nHauteur : " + hauteur());
        sb.append("\nEpaisseur : " + epaisseur());
        sb.append("\nMarge repli : " + margeRepli());
        sb.append("\nMarge pliage : " + margePliage());
        sb.append("\nHauteur retour air : " + hauteurRetourAir());
        sb.append("\nDistance sol retour air : " + distanceSolRetourAir());
        sb.append("\nHauteur trou retour air : " + hauteurTrouRetourAir());
        sb.append("\nLongueur pli soudure : " + longueurPliSoudure());
        sb.append("\nAngle pli soudure : " + anglePliSoudure());
        
        return sb.toString();
    }
}
