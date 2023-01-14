package ca.ulaval.glo2004.domaine.dtos;

/**
 *
 * @author Antoine
 */
public class SeparateurParamDTO {
    
    private String position;
    
    public SeparateurParamDTO(String position) {
        this.position = position;
    }
    
    public String position() {
        return position;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Position : " + position());
        
        return sb.toString();
    }
}
