package ca.ulaval.glo2004.domaine.dtos;

/**
 *
 * @author Antoine
 */
public class ImperialPointDTO {
    
    private String positionX;
    private String positionY;
    
    public ImperialPointDTO(String positionX, String positionY) {   
        this.positionX = positionX;
        this.positionY = positionY;
    }
    
    public String positionX() {
        return positionX;
    }
    
    
    public String positionY() {
        return positionY;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("X : " + positionX());
        sb.append(" | Y : " + positionY());
        
        return sb.toString();
    }
}
