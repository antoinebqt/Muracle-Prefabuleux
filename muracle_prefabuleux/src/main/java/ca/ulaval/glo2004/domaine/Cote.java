package ca.ulaval.glo2004.domaine;

import ca.ulaval.glo2004.domaine.MesureImperial.PointImperial;
import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import ca.ulaval.glo2004.domaine.Accessoire.Accessoire;
import ca.ulaval.glo2004.domaine.Accessoire.RetourAir;
import ca.ulaval.glo2004.gui.Enum.Orientation;
import ca.ulaval.glo2004.gui.Enum.TypeAccessoire;
import ca.ulaval.glo2004.gui.Enum.TypePolygone;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author anto-
 */
public class Cote extends ElementSalle implements Serializable{

    final private Salle SALLE;
    final private Orientation ORIENTATION;
    private ArrayList<Mur> murs;
    private ArrayList<Separateur> separateurs;
    private ArrayList<Accessoire> accessoires;
    private Imperial oldHauteur;

    public Cote(Salle salle, Orientation orientation, Imperial longueur) {
        super(); //Faire en sorte d'affecter de vrai coordonnée X et Y pour la vue en plan ?
        switch (orientation) {
            case EST:
                setPosX(Imperial.add(salle.getLongueur(), salle.getEpaisseurMur()));
                setPosY(salle.getEpaisseurMur());
                break;
            case SUD:
                setPosX(salle.getEpaisseurMur());
                setPosY(Imperial.add(salle.getLargeur(), salle.getEpaisseurMur()));
                break;
            case NORD:
                setPosX(salle.getEpaisseurMur());
                break;                
            case OUEST:
                setPosY(salle.getEpaisseurMur());
                break;
            default:
                throw new AssertionError();
        }
        
        this.SALLE = salle;
        this.ORIENTATION = orientation;
        this.murs = new ArrayList<Mur>();
        this.separateurs = new ArrayList<Separateur>();
        this.accessoires = new ArrayList<>();
        
        this.setLongueur(longueur);
        this.setHauteur(SALLE.getHauteur());
        this.setLargeur(SALLE.getEpaisseurMur());
        
        calculeDisposition();
    }

    public Orientation getOrientation() {
        return ORIENTATION;
    }

    public ArrayList<Mur> getMurs() {
        return murs;
    }

    public ArrayList<Separateur> getSeparateurs() {
        return separateurs;
    }

    public ArrayList<Accessoire> getAccessoires() {
        return accessoires;
    }
    
    public ArrayList<RetourAir> getRetourAirs() {
        ArrayList<RetourAir> retourAirs = new ArrayList<RetourAir>();
        for (Accessoire accessoire : accessoires) {
            if (accessoire.getType() == TypeAccessoire.RETOUR_AIR) {
                retourAirs.add((RetourAir)accessoire);
            }
        }
        return retourAirs;
    }
    
    public ArrayList<Accessoire> getAccessoires(TypeAccessoire type) {
        ArrayList<Accessoire> list = new ArrayList<Accessoire>();
        for (Accessoire accessoire : this.accessoires) {
            if (accessoire.getType() == type) {
                list.add(accessoire);
            }
        }
        return list;
    }

    //this will also create a new Mur
    public Separateur addSeparateur(Imperial posX) {
        Separateur separateur = new Separateur(this, getMurAtX(posX), posX);
        //l'usager peut couper un accesoire d'un separateur, mais les accessoires apparaitront de couleur differente
        /*for (Accessoire accessoire : accessoires) {
            if (accessoire.containsX(posX)) {
                //On throw pas une exception on trouvera un moyen de gérer l'erreur sans faire arreter l'appli
                System.out.println("Un separateur ne peut pas couper un accessoire.");
            }
        }*/
        separateurs.add(separateur);
        sortSeparateurs();
        calculeDisposition();
        return separateur;
    }

    public boolean removeSeparateur(Separateur separateur) {
        boolean result = separateurs.remove(separateur);
        if (result) {
            sortSeparateurs();
            calculeDisposition();
        }
        return result;
    }
    public void sortSeparateurs(){
        Collections.sort(separateurs);
    }

    public void addAccessoire(Accessoire newAccessoire){
        newAccessoire.setCote(this);
        accessoires.add(newAccessoire);
        calculeDisposition();
    }

    public boolean removeAccessoire(Accessoire accessoire) {
        boolean result = accessoires.remove(accessoire);
        if (result) {
            calculeDisposition();
        }
        return result;
    }

    public Mur getMurAtX(Imperial posX) {
        for (Mur mur : murs) {
            if (mur.containsVueCoteX(posX)) {
                return mur;
            }
        }
        return null;
    }
    
    public Mur getMurAt(PointImperial coor) {
        for (Mur mur : murs) {
            if (mur.contains(coor)) {
                return mur;
            }
        }
        return null;
    }

    public Accessoire getAccessoireAt(PointImperial coor) {
        for (Accessoire accessoire : accessoires) {
            if (accessoire.contains(coor)) {
                return accessoire;
            }
        }
        return null;
    }
    
    public Separateur getSeparateurAt(Imperial posX) {
        Imperial marge = new Imperial(5);
        for (Separateur separateur : separateurs) {
            Imperial separateurPosX = separateur.getPosX();
            if (posX.isBetween(separateurPosX.substract(marge), separateurPosX.add(marge))) {
                return separateur;
            }
        }
        return null;
    }
    
    public final void generateMurs() {
        ArrayList<Mur> oldMurs = new ArrayList<Mur>(murs);
        ArrayList<Mur> newMurs = new ArrayList<Mur>();
        Imperial longueurPremierMur;
        
        if (separateurs.isEmpty()) {
            longueurPremierMur = this.getLongueur();
        } else {
            longueurPremierMur = separateurs.get(0).getPosX();
        }
        newMurs.add(new Mur(SALLE,this, new Imperial(), longueurPremierMur));
        
        for (int i = 0; i < separateurs.size(); i++) {
            
            Imperial longueurMur;
            if (i < separateurs.size()-1) {
                longueurMur = Imperial.substract(separateurs.get(i + 1).getPosX(), separateurs.get(i).getPosX());
            } else {
                longueurMur = Imperial.substract(this.getLongueur(), separateurs.get(i).getPosX());
            }
            
            Mur mur = new Mur(SALLE, this, separateurs.get(i).getPosX(), longueurMur);
            separateurs.get(i).setMur(mur);
            if (separateurs.get(i).isSelected()) {
                mur.setSeparateurGaucheSelected();
            }
            newMurs.add(mur);
        }
        
        newMurs.get(0).isCoinDebut(true);
        newMurs.get(newMurs.size()-1).isCoinFin(true);
        murs = newMurs;
        
        removeInvalidRetourAir();
    }
    
    private void removeInvalidRetourAir(){
        ArrayList<RetourAir> retourAirs = this.getRetourAirs();
        ArrayList<RetourAir> invalidRetourAir = new ArrayList<RetourAir>();
        for (RetourAir retourAir : retourAirs) {
            retourAir.recenter();
            if(retourAir.getMur() == null) {
                invalidRetourAir.add(retourAir);
            } else {
                Mur mur = retourAir.getMur();
                for (RetourAir retourAir2 : retourAirs) {
                    if(retourAir != retourAir2 && mur == retourAir2.getMur() && invalidRetourAir.indexOf(retourAir2) == -1) {
                        invalidRetourAir.add(retourAir);
                    }
                }
            }
        }
        this.accessoires.removeAll(invalidRetourAir);
    }
    
    // Verifie si le separateur est dans le cote que ce soit la vue de dessus ou de cote.
    private void verificationSeparateurHorsLimite() {
        for (Separateur separateur : separateurs) {
            if (!contains(separateur.getCoor())) {
                removeSeparateur(separateur);
            }
        }
    }
    
    private void verificationAccessoireInterieurMur() {
        for (Accessoire accessoire : accessoires) {
            boolean murFound = false;
            int index = 0;
            while (!murFound && index < murs.size()) {                
                if (murs.get(index).fullyContains(accessoire.getPolygone())) {
                    murFound = true;
                    accessoire.setNoErrorDetected();
                }
                else {
                    accessoire.setCollisionMur(true);
                    accessoire.setErrorDetected();
                }
                index++;
            }
            boolean accessoireColissionDetected = false;
            for (Accessoire autreAccessoire : accessoires) {
                 if (autreAccessoire != accessoire && autreAccessoire.touches(accessoire)) {
                     accessoireColissionDetected = true;
                     accessoire.setErrorDetected();
                     autreAccessoire.setErrorDetected();
                 }
            }
            if(!accessoireColissionDetected && murFound) {
                accessoire.setNoErrorDetected();
            }
        }
    }
    
    private void verificationAccessoiresEnColision() {
        for (Accessoire accessoire : accessoires) {
            boolean accessoireColissionDetected = false;
            for (Accessoire AutreAccessoire : accessoires) {
                 if (AutreAccessoire != accessoire && AutreAccessoire.contains(accessoire.getPolygone())) {
                     accessoireColissionDetected = true;
                     accessoire.setErrorDetected();
                 }
            }
            if(!accessoireColissionDetected) {
                accessoire.setNoErrorDetected();
            }
        }
    }

    @Override
    public Salle getSalle() {
        return SALLE;
    }
    
    public ArrayList<Polygone> getPolygonesMur() {
        ArrayList<Polygone> totalPolygones = new ArrayList<Polygone>();
        for (Mur mur : murs) {
            totalPolygones.add(mur.getPolygone());
        }
        totalPolygones.addAll(getPolygonesSeparateurs());
        return totalPolygones;
    }
    
    public ArrayList<Polygone> getPolygonesAccessoires() {
        ArrayList<Polygone> totalPolygones = new ArrayList<Polygone>();
        for (Accessoire accessoire : accessoires) {
            totalPolygones.add(accessoire.getPolygone());
        }
        return totalPolygones;
    }
    
    public ArrayList<Polygone> getPolygonesSeparateurs() {
        ArrayList<Polygone> totalPolygones = new ArrayList<Polygone>();
        for (Separateur separateur : separateurs) {
            totalPolygones.add(separateur.getPolygone());
        }
        return totalPolygones;
    }
    
    @Override
    public void calculeDisposition() {
        generateMurs();
        calculMurDisposition();
        calculAccessoireDisposition();
        verificationAccessoireInterieurMur();
//        verificationAccessoiresEnColision();
        updateSeparateurs();
        calculatePoidsMurs();
    }
    
    private void calculMurDisposition() {
        Imperial xMaxPrecedent = new Imperial(0);
        for (Mur mur: this.murs) {
            Imperial longeur = mur.getLongueur();
            Imperial hauteur = mur.getHauteur();
            ArrayList<PointImperial> newImperialPoints = new ArrayList<PointImperial>();
            newImperialPoints.add(new PointImperial(
                    xMaxPrecedent, 
                    new Imperial(0)
            ));
            newImperialPoints.add(new PointImperial(
                    Imperial.add(xMaxPrecedent, longeur), 
                    new Imperial(0)
            ));
            newImperialPoints.add(new PointImperial(
                    Imperial.add(xMaxPrecedent, longeur), 
                    hauteur
            ));
            newImperialPoints.add(new PointImperial(
                    xMaxPrecedent, 
                    hauteur
            ));
            Polygone polygone = new Polygone(newImperialPoints, TypePolygone.MUR);
            mur.setPolygone(polygone);
            xMaxPrecedent = Imperial.add(xMaxPrecedent, longeur);
        }
    }
    
    private void calculAccessoireDisposition() {
        for (Accessoire accessoire : this.accessoires) {
            Imperial x = accessoire.getPosX();
            Imperial y = accessoire.getPosY();
            Imperial longueur = accessoire.getLongueur();
            Imperial hauteur = accessoire.getHauteur();
            ArrayList<PointImperial> newImperialPoints = new ArrayList<PointImperial>();
            newImperialPoints.add(new PointImperial(
                    x, 
                    y
            ));
            newImperialPoints.add(new PointImperial(
                    Imperial.add(x, longueur), 
                    y
            ));
            newImperialPoints.add(new PointImperial(
                    Imperial.add(x, longueur), 
                    Imperial.add(y, hauteur)
            ));
            newImperialPoints.add(new PointImperial(
                    x, 
                    Imperial.add(y, hauteur)
            ));
            TypePolygone type = TypePolygone.fromString(accessoire.getType().getValue());
            Polygone polygone = new Polygone(newImperialPoints, type);
            if (accessoire.isSelected()) {
                polygone.setColor(Color.GREEN);
            }
            accessoire.setPolygone(polygone);
        }
    }
    
    @Override
    public String toString(){
        
        String s = "";
        s+= "Coté " + getOrientation().toString() + ": {\n";
        s+= "Nombre de séparateurs: " + getSeparateurs().size() + "\n";
        s+= "Murs: \n";
        for (int i=0;i<murs.size();i++){
            s+= "   Mur " + (i+1) + ": \n" + murs.get(i) + "\n";
        }
        s+= "\nAccessoires: \n";
        for (int i=0;i<accessoires.size();i++){
            s+= "   Accessoire " + (i+1) + ": \n" + accessoires.get(i) + "\n";
        }
        s+= "\n}";

        return s;
    }

    void updateCote() {
        switch (ORIENTATION) {
            case EST:
                setPosX(Imperial.add(SALLE.getLongueur(), SALLE.getEpaisseurMur()));
                setPosY(SALLE.getEpaisseurMur());
                setLongueur(SALLE.getLargeur());
                break;
            case SUD:
                setPosX(SALLE.getEpaisseurMur());
                setPosY(Imperial.add(SALLE.getLargeur(), SALLE.getEpaisseurMur()));
                setLongueur(SALLE.getLongueur());
                break;
            case NORD:
                setPosX(SALLE.getEpaisseurMur());
                setLongueur(SALLE.getLongueur());
                break;                
            case OUEST:
                setPosY(SALLE.getEpaisseurMur());
                setLongueur(SALLE.getLargeur());
                break;
            default:
                throw new AssertionError();
        }
        
        this.oldHauteur = getHauteur();
        this.setHauteur(SALLE.getHauteur());
        this.setLargeur(SALLE.getEpaisseurMur());
        
        //retirer les separateurs qui flote
        ArrayList<Separateur> invalidSeparateurs = new ArrayList<Separateur>();
        for (Separateur separateur: this.separateurs){
            if (separateur.getPosX().isGreater(this.getLongueur())){
                invalidSeparateurs.add(separateur);
            }
        }
        this.separateurs.removeAll(invalidSeparateurs);
        
        calculeDisposition();
        updateAccessoire();
  }

    private void updateAccessoire() {
        for (Accessoire accessoire : accessoires) {
            if (accessoire.getType() == TypeAccessoire.PORTE) {
                accessoire.setPosY(Imperial.substract(getHauteur(), Imperial.add(accessoire.getMargeMarche(), accessoire.getHauteur())));
            } else {
                accessoire.setPosY(Imperial.add(accessoire.getPosY(), Imperial.substract(getHauteur(), oldHauteur)));
            }
        }
        calculeDisposition();
    }
    
    // contains seulement pour la vue de cote. Contenue a partir du point haut gauche du cote
    boolean containsVueCote(PointImperial coor) {
        return containsVueCoteX(coor.getX()) && containsVueCoteY(coor.getY());
    }

    boolean containsVueCoteX(Imperial posX) {
        return (posX.getDoublePixelValue() > 0 && posX.getDoublePixelValue() < getLongueur().getDoublePixelValue());
    }

    private boolean containsVueCoteY(Imperial posY) {
        return (posY.getDoublePixelValue() > 0 && posY.getDoublePixelValue() < getHauteur().getDoublePixelValue());
    }
    
    public void updateSeparateurs() {
        for (Separateur separateur : separateurs) {
            separateur.calculeDisposition();
        }
    }

    void removeAllSelected() {
        for (Separateur separateur : separateurs) {
            separateur.setUnselected();
        }
        for (Mur mur : murs) {
            mur.setUnselected();
        }
        for (Accessoire accessoire : accessoires) {
            accessoire.setUnselected();
        }
    }
    
    @Override
    public double getAire(){
        double aire = 0;
        for (Mur mur : murs){
            aire += mur.getAire();
        }
        return aire;
    }
    
    public void calculatePoidsMurs(){
        for(Mur mur : this.getMurs()){
            mur.calculatePoidsPanneaux();
        }
    }
}
