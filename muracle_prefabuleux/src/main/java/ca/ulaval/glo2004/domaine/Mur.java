package ca.ulaval.glo2004.domaine;

import ca.ulaval.glo2004.domaine.Accessoire.Accessoire;
import ca.ulaval.glo2004.domaine.Accessoire.RetourAir;
import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import java.util.ArrayList;

/**
 *
 * @author anto-
 */
public class Mur extends ElementSalle{

    final private Salle SALLE;
    final private Cote COTE;
    
    private Panneau panneauInterieur;
    private Panneau panneauExterieur;
    private boolean separateurGaucheSelected = false;


    
    public Mur(Salle salle, Cote cote, Imperial posX, Imperial longueur){
        super(posX, new Imperial(), longueur, cote.getHauteur(),salle.getLargeur());
        this.SALLE = salle;
        this.COTE = cote;
        this.panneauExterieur = new Panneau(this, true,longueur, salle.getHauteur());
        this.panneauInterieur = new Panneau(this, false,longueur,salle.getHauteur());
    }
    
    public Cote getCote(){
        return COTE;
    }
    public Panneau getPanneauInterieur(){
        return panneauInterieur;
    }
    public Panneau getPanneauExterieur(){
        return panneauExterieur;
    }
    public RetourAir getRetourAir(){
        ArrayList<RetourAir> retourAirs =  this.getCote().getRetourAirs();
        for(RetourAir retourAir : retourAirs){
            if(retourAir.getMur() == this){
                return retourAir;
            }
        }
        return null;
    }
    public ArrayList<Accessoire> getAccessoires(){
        ArrayList<Accessoire> accessoires = new ArrayList<Accessoire>();
        for(Accessoire accessoire : this.getCote().getAccessoires()){
            if(containsVueCoteX(accessoire.getPosX())){
                accessoires.add(accessoire);
            }
        }
        return accessoires;
    }
    
    
    @Override
    public void setLongueur(Imperial longueur){
        panneauInterieur.setLongueur(longueur);
        panneauExterieur.setLongueur(longueur);
        calculeDisposition();
    }
    
    @Override
    public void setHauteur(Imperial longueur){
        panneauInterieur.setHauteur(longueur);
        panneauExterieur.setHauteur(longueur);
        calculeDisposition();
    }
    
    @Override
    public Salle getSalle() {
        return COTE.getSalle();
    }
    
    // TODO : Affichage des panneaux
    @Override
    public void calculeDisposition() {
        panneauInterieur.calculeDisposition();
        panneauExterieur.calculeDisposition();
        calculatePoidsPanneaux();
    }
    @Override
    public String toString(){
        return "\t Position en X: " + getPosX() + "\t\tLongueur: " + getLongueur() + "\n";
    }

    void setSeparateurGaucheSelected() {
        separateurGaucheSelected = true;
    }
    
    boolean isSeparateurGaucheSelected(){
        return separateurGaucheSelected;
    }
    
    boolean containsVueCoteX(Imperial posX) {
        return posX.isBetween(this.getPosX(),this.getPosX().add(this.getLongueur()));
    }
    
    @Override
    public double getAire(){
        return panneauInterieur.getAire()+panneauInterieur.getAire();
    }
    
    public void calculatePoidsPanneaux(){
        panneauExterieur.calculePoids();
        panneauInterieur.calculePoids();
    }
}
