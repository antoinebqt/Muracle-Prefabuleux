package ca.ulaval.glo2004.domaine;
import ca.ulaval.glo2004.domaine.Accessoire.Accessoire;
import ca.ulaval.glo2004.domaine.Accessoire.RetourAir;
import ca.ulaval.glo2004.domaine.MesureImperial.PointImperial;
import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import ca.ulaval.glo2004.gui.Enum.Orientation;
import ca.ulaval.glo2004.gui.Enum.TypeAccessoire;
import ca.ulaval.glo2004.gui.Enum.TypePolygone;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author anto-
 */
public class Salle extends ElementSalle implements Serializable{

    public static final Imperial LONGEUR_DEFAUT = new Imperial(200);
    public static final Imperial HAUTEUR_DEFAUT = new Imperial(100);
    public static final Imperial LARGEUR_DEFAUT = new Imperial(200);
    
    public static final Imperial EPAISSEUR_MUR_DEFAUT = new Imperial(5);
    public static final Imperial MARGEPLIAGE_DEFAUT = new Imperial(0, 1,4);
    public static final Imperial MARGE_REPLIEPAISSEUR_DEFAUT = new Imperial(0, 1,2);
    public static final Imperial HAUTEUR_RETOURAIR_DEFAUT = new Imperial(10);
    public static final Imperial DISTANCESOL_RETOURAIR_DEFAUT = new Imperial(3);
    public static final Imperial HAUTEURTROU_RETOURAIR_DEFAUT = new Imperial(1);
    public static final Imperial LARGEUR_PLISOUDURE_DEFAUT = new Imperial(1);
    public static final double ANGLE_PLISOUDURE_DEFAUT = Math.PI/4;
    public static final double MASSE_SURFACIQUE_MATERIAU = 6.3;
    
    private Imperial epaisseurMur;
    private Imperial margePliage;
    private Imperial margeRepliEpaisseur;
    
    private Imperial hauteurRetourAir;
    private Imperial distanceSolRetourAir;
    private Imperial hauteurTrouRetourAir;
    
    private Imperial longueurPliSoudure;
    
    private double anglePliSoudure; //En radian
   
    /**
     * L'array de cotés est comme suit: {Nord, Est, Sud, Ouest}
     * La largeur de la salle représente les côté Ouest et est.
     * La longueur de la salle représente les coté nord et sud.
     */
    public HashMap<Orientation, Cote> cotes;
    
    public Salle(){
        this(LONGEUR_DEFAUT, HAUTEUR_DEFAUT, LARGEUR_DEFAUT, EPAISSEUR_MUR_DEFAUT, MARGEPLIAGE_DEFAUT, MARGE_REPLIEPAISSEUR_DEFAUT,
                HAUTEUR_RETOURAIR_DEFAUT, DISTANCESOL_RETOURAIR_DEFAUT, HAUTEURTROU_RETOURAIR_DEFAUT, 
                LARGEUR_PLISOUDURE_DEFAUT, ANGLE_PLISOUDURE_DEFAUT);
    }
    public Salle(Imperial longueur, Imperial hauteur, Imperial largeur, Imperial epaisseurMur, Imperial margePliage, Imperial margeRepliEpaisseur,
        Imperial hauteurRetourAir, Imperial distanceSolRetourAir, Imperial hauteurTrouRetourAir, Imperial largeurPliSoudure,
        double anglePliSoudure){
        super(new PointImperial(), longueur, hauteur, largeur);
        setEpaisseurMur(epaisseurMur);
        setMargePliage(margePliage);
        setMargeRepliEpaisseur(margeRepliEpaisseur);
        setHauteurRetourAir(hauteurRetourAir);
        setDistanceSolRetourAir(distanceSolRetourAir);
        setHauteurTrouRetourAir(hauteurTrouRetourAir);
        setLargeurPliSoudure(largeurPliSoudure);
        setAnglePliSoudure(anglePliSoudure);
        cotes = new HashMap<>();
        cotes.put(Orientation.NORD, new Cote(this, Orientation.NORD, longueur));
        cotes.put(Orientation.EST, new Cote(this, Orientation.EST, largeur));
        cotes.put(Orientation.SUD, new Cote(this, Orientation.SUD, longueur));
        cotes.put(Orientation.OUEST, new Cote(this, Orientation.OUEST, largeur));        
    }

    public final void setEpaisseurMur(Imperial epaisseurMur) {
        this.epaisseurMur = epaisseurMur;
    }

    public final void setMargePliage(Imperial margePliage) {
        this.margePliage = margePliage;
    }
    
    public final void setMargeRepliEpaisseur(Imperial margeRepliEpaisseur) {
        this.margeRepliEpaisseur = margeRepliEpaisseur;
    }

    public final void setHauteurRetourAir(Imperial hauteurRetourAir) {
        this.hauteurRetourAir = hauteurRetourAir;
        this.updateRetourAir();
    }

    public final void setDistanceSolRetourAir(Imperial distanceSolRetourAir) {
        this.distanceSolRetourAir = distanceSolRetourAir;
        this.updateRetourAir();
    }

    public final void setHauteurTrouRetourAir(Imperial hauteurTrouRetourAir) {
        this.hauteurTrouRetourAir = hauteurTrouRetourAir;
    }

    public final void setLargeurPliSoudure(Imperial largeurPliSoudure) {
        this.longueurPliSoudure = largeurPliSoudure;
    }

    public final void setAnglePliSoudure(double anglePliSoudure) {
        this.anglePliSoudure = anglePliSoudure;
    }

    public void setCotes(HashMap<Orientation, Cote> cotes) {
        this.cotes = cotes;
    }

    public Imperial getEpaisseurMur() {
        return epaisseurMur;
    }

    public Imperial getMargePliage() {
        return margePliage;
    }
    
    public Imperial getMargeRepliEpaisseur() {
        return margeRepliEpaisseur;
    }

    public Imperial getHauteurRetourAir() {
        return hauteurRetourAir;
    }

    public Imperial getDistanceSolRetourAir() {
        return distanceSolRetourAir;
    }

    public Imperial getHauteurTrouRetourAir() {
        return hauteurTrouRetourAir;
    }

    public Imperial getLongueurPliSoudure() {
        return longueurPliSoudure;
    }

    public double getAnglePliSoudure() {
        return anglePliSoudure;
    }

    public double getMasseSurfaciqueMateriau() {
        return MASSE_SURFACIQUE_MATERIAU;
    }

    public Imperial getInclinaisonPliSoudure(){
        return longueurPliSoudure.divide(Math.round(Math.tan(anglePliSoudure) * (double)1000) / (double)1000);
    }
    
    public Imperial getLongueurHypothenuseCote(){
        return epaisseurMur.divide(Math.round(Math.sin(anglePliSoudure) * (double)1000) / (double)1000);
    }
    
    public HashMap<Orientation, Cote> getCotes() {
        return cotes;
    }
    
    public Cote getCote(Orientation orientation){
        return cotes.get(orientation);
    }
    public ArrayList<Mur> getAllMur(){
        ArrayList<Mur> murs = new ArrayList<Mur>();
        murs.addAll(cotes.get(Orientation.NORD).getMurs());
        murs.addAll(cotes.get(Orientation.SUD).getMurs());
        murs.addAll(cotes.get(Orientation.EST).getMurs());
        murs.addAll(cotes.get(Orientation.OUEST).getMurs());
        return murs;
    }
    
    public Cote getCoteAt(PointImperial coor){
        for (Polygone poly : getPolygones()) {
            Point point = new Point((int) coor.getX().getDoublePixelValue(), (int) coor.getY().getDoublePixelValue());
            if (poly.contains(point)) {
                switch (poly.getTypePolygone()) {
                    case COTE_NORD:
                        return cotes.get(Orientation.NORD);
                    case COTE_SUD:
                        return cotes.get(Orientation.SUD);
                    case COTE_EST:
                        return cotes.get(Orientation.EST);
                    case COTE_OUEST:
                        return cotes.get(Orientation.OUEST);
                }
            }
        }
        return null;
    }
    
    public Separateur getSeparateurAt(PointImperial coor){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ArrayList<Polygone> getPolygones() {
        ArrayList<Polygone> totalPolygones = new ArrayList<Polygone>();
        for (Cote cote : cotes.values()) {
            totalPolygones.addAll(cote.getPolygonesMur());
            ArrayList<RetourAir> retourAirs = cote.getRetourAirs();
            for(RetourAir retourAir : retourAirs){
                totalPolygones.add(retourAir.getPolygone());
            }
            totalPolygones.addAll(cote.getPolygonesSeparateurs());
        }
        return totalPolygones;
    }
    
    public Mur getMurAt(PointImperial coor) {
        Cote cote = getCoteAt(coor);
        if (cote != null) {
            return cote.getMurAt(coor);
        }
        return null;
    }
    
    private void updateRetourAir(){
        if (this.cotes != null) {
            for (Cote cote: this.cotes.values()) {
                for (Accessoire accessoire: cote.getAccessoires(TypeAccessoire.RETOUR_AIR)) {
                    RetourAir retourAir = (RetourAir)accessoire;
                    retourAir.setHauteur(this.hauteurRetourAir);
                    retourAir.recenter();
                }
                cote.calculeDisposition();
            }
        }
    }

    @Override
    public Salle getSalle() {
        return this;
    }
    
    private void generateAllMur() {
        for (Cote cote : this.cotes.values()) {
            cote.generateMurs();
        }
    }
    
    @Override
    public void calculeDisposition() {
        generateAllMur();
        Imperial xMaxPrecedent = new Imperial(0);
        for (Cote cote: this.cotes.values()) {
            setCoteHitbox(cote);
            
            for (Mur mur : cote.getMurs()) {
                RetourAir retourAir = mur.getRetourAir();
                Imperial distanceX = mur.getLongueur();
                Imperial distanceY = epaisseurMur;
                Imperial margeCoinDebut = new Imperial(0);
                Imperial margeCoinFin = new Imperial(0);
                ArrayList<PointImperial> newImperialPoints = new ArrayList<PointImperial>();
                if (mur.isCoinDebut()) {
                    margeCoinDebut = epaisseurMur;
                }
                if (mur.isCoinFin()) {
                    margeCoinFin = epaisseurMur;
                }
                newImperialPoints.add(new PointImperial(
                        xMaxPrecedent, 
                        new Imperial(0)
                ));
                newImperialPoints.add(new PointImperial(
                        Imperial.add(Imperial.add(Imperial.add(xMaxPrecedent, distanceX),margeCoinFin), margeCoinDebut), 
                        new Imperial(0)
                ));
                newImperialPoints.add(new PointImperial(
                        Imperial.add(Imperial.add(xMaxPrecedent, distanceX), margeCoinDebut), 
                        distanceY
                ));
                newImperialPoints.add(new PointImperial(
                        Imperial.add(xMaxPrecedent, margeCoinDebut), 
                        distanceY
                ));
                if (retourAir != null && retourAir.getCote() != null && retourAir.getMur() == mur) {
                    calculeRetourAir(retourAir);
                }
                newImperialPoints = rotationCotes(newImperialPoints,cote);
                newImperialPoints = deplacementSelonEpaisseurMateriaux(newImperialPoints, cote.getOrientation());
                Polygone polygone = new Polygone(newImperialPoints, TypePolygone.fromString(cote.getOrientation().getValue()));

                mur.setPolygone(polygone);
                xMaxPrecedent = Imperial.add(Imperial.add(xMaxPrecedent, distanceX), margeCoinDebut);
            }
            xMaxPrecedent = new Imperial(0);
        }
        updateSeparateurs();
    }
    
    private ArrayList<PointImperial> rotationCotes (ArrayList<PointImperial> oldPoints, Cote cote) {
        Orientation orientation = cote.getOrientation();
        ArrayList<PointImperial> newImperialPoints = new ArrayList<PointImperial>();
        if (orientation != Orientation.NORD) {
            int angleRotation = 0;
            switch (orientation) {
                case SUD:
                    angleRotation = 180;
                    break;
                case EST:
                    angleRotation = 90;
                    break;
                case OUEST:
                    angleRotation = 270;
                    break;
            }
            PointImperial centreSalle = new PointImperial(
                Imperial.add(Imperial.divide(cote.getLongueur(), new Imperial(2)),epaisseurMur),
                Imperial.add( Imperial.divide(getLargeur(), new Imperial(2)),epaisseurMur)
            );
            newImperialPoints = Imperial.rotation(oldPoints, centreSalle, angleRotation);            
        }
        else {
            return oldPoints;
        }
        return newImperialPoints;
    }

    
    private ArrayList<PointImperial> deplacementSelonEpaisseurMateriaux (ArrayList<PointImperial> oldPoints, Orientation orientation) {
        ArrayList<PointImperial> newImperialPoints = new ArrayList<PointImperial>(); 
        switch (orientation) {
            case EST:
                newImperialPoints = Imperial.translationAddition(oldPoints, new PointImperial(
                    getLongueur().substract(getLargeur()).add(epaisseurMateriaux.multiply(2)),
                    epaisseurMateriaux.add(epaisseurMateriaux.divide(new Imperial(4)))
                ));
                break;
            case OUEST :
                newImperialPoints = Imperial.translationAddition(oldPoints, new PointImperial(
                    epaisseurMateriaux.divide(new Imperial(2)),
                    epaisseurMateriaux.add(epaisseurMateriaux.divide(new Imperial(4)))
                ));
                break;
            case SUD : 
                newImperialPoints = Imperial.translationAddition(oldPoints, new PointImperial(
                    epaisseurMateriaux.add(epaisseurMateriaux.divide(new Imperial(4))), 
                    epaisseurMateriaux.multiply(2)
                ));
                break;
            case NORD : 
                newImperialPoints = Imperial.translationAddition(oldPoints, new PointImperial(
                    epaisseurMateriaux.add(epaisseurMateriaux.divide(new Imperial(4))), 
                    epaisseurMateriaux.divide(new Imperial(2))
                ));
                break;
        }
        return newImperialPoints;
    }
    
    private void setCoteHitbox(Cote cote) {
        Imperial longueur;
        TypePolygone typePolygone;
        switch (cote.getOrientation()) {
            case NORD:
                typePolygone = TypePolygone.COTE_NORD;
                break;
            case SUD:
                typePolygone = TypePolygone.COTE_SUD;
                break;
            case EST:
                typePolygone = TypePolygone.COTE_EST;
                break;
            case OUEST:
                typePolygone = TypePolygone.COTE_OUEST;
                break;
            default:
                typePolygone = TypePolygone.COTE_NORD;
        }

        if (cote.getOrientation() == Orientation.NORD || cote.getOrientation() == Orientation.SUD) {
            longueur = getLongueur();
        }
        else {
            longueur = getLargeur();
        }
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        points.add(new PointImperial(
            epaisseurMur,
            new Imperial()
        ));
        points.add(new PointImperial(
            epaisseurMur.add(longueur),
            new Imperial()
        ));
        points.add(new PointImperial(
            epaisseurMur.add(longueur),
            epaisseurMur
        ));
        points.add(new PointImperial(
            epaisseurMur,
            epaisseurMur
        ));
        points = rotationCotes(points,cote);
        points = deplacementSelonEpaisseurMateriaux(points, cote.getOrientation());
        cote.setPolygone( new Polygone(points,typePolygone) );
    }
    
    private void calculeRetourAir(RetourAir retourAir) {
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Imperial paddingY = this.getEpaisseurMur().substract(this.getHauteurTrouRetourAir()).divide(2);
        
        points.add(new PointImperial(
            this.getEpaisseurMur().add(retourAir.getPosX()),
            paddingY
        ));
        points.add(new PointImperial(
            this.getEpaisseurMur().add(retourAir.getPosX()).add(retourAir.getLongueur()),
            paddingY
        ));
        points.add(new PointImperial(
            this.getEpaisseurMur().add(retourAir.getPosX()).add(retourAir.getLongueur()),
            paddingY.add(this.getHauteurTrouRetourAir())
        ));
        points.add(new PointImperial(
            this.getEpaisseurMur().add(retourAir.getPosX()),
            paddingY.add(this.getHauteurTrouRetourAir())
        ));
        points = rotationCotes(points,retourAir.getCote());
        points = deplacementSelonEpaisseurMateriaux(points, retourAir.getCote().getOrientation());
        retourAir.setPolygone(new Polygone(points,TypePolygone.RETOUR_AIR) );
    }
    
    @Override
    public String toString(){
        String s="";
        for (Cote cote : cotes.values()) {
            s+= cote + "\n\n";
        }
        return s;
    }

    public void updateCotes() {
        for (Cote cote: this.cotes.values()) {
            cote.updateCote();
        }
    }
    
    public void refreshCotes() {
        for (Cote cote : this.cotes.values()) {
            cote.calculeDisposition();
        }
    }
    
    public void updateSeparateurs() {
        for (Cote cote : this.cotes.values()) {
            cote.updateSeparateurs();
        }
    }

    public void removeAllSelected() {
        for (Cote cote: this.cotes.values()) {
            cote.removeAllSelected();
        }
    }
    
    @Override
    public double getAire(){
        double aire = 0;
        for (Cote cote : cotes.values()){
            aire += cote.getAire();
        }
        return aire;
    }
    
    public ArrayList<Accessoire> getAllAccessoires(){
        ArrayList<Accessoire> accessoires = new ArrayList<>();
        accessoires.addAll(cotes.get(Orientation.NORD).getAccessoires());
        accessoires.addAll(cotes.get(Orientation.SUD).getAccessoires());
        accessoires.addAll(cotes.get(Orientation.EST).getAccessoires());
        accessoires.addAll(cotes.get(Orientation.OUEST).getAccessoires());
        return accessoires;
    }
}
