package ca.ulaval.glo2004.domaine;

import ca.ulaval.glo2004.domaine.Accessoire.Accessoire;
import ca.ulaval.glo2004.domaine.Accessoire.Fenetre;
import ca.ulaval.glo2004.domaine.Accessoire.RetourAir;
import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import ca.ulaval.glo2004.domaine.MesureImperial.PointImperial;
import ca.ulaval.glo2004.gui.Enum.TypeAccessoire;
import ca.ulaval.glo2004.gui.Enum.TypePolygone;
import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author anto-
 */
public class Panneau extends ElementSalle implements Serializable{

    private boolean isExterieur;
    private Mur mur;
    private ArrayList<Polygone> polygones = new ArrayList<Polygone>();
    private boolean isTropLourd = false;
    private static final double MASSE_SURFACIQUE_MATERIAU = 6.3;
    private double poids;
    
    public Panneau (Mur mur, boolean isExterieur, Imperial longueur, Imperial hauteur){
        super(new PointImperial(), longueur, hauteur, new Imperial());
        this.mur = mur;
        this.isExterieur = isExterieur;
        
    }
    public ArrayList<Polygone> getPolygones(){
        if(polygones.isEmpty())
            this.calculeDisposition();
        return polygones;
    }
    public Mur getMur(){
        return mur;
    }
    @Override
    public Salle getSalle() {
        return this.getMur().getSalle();
    }

    private Polygone generateCorp(){
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Imperial decalageCoins = new Imperial();
        if(isExterieur){
            if(mur.isCoinDebut())
                decalageCoins = decalageCoins.add(mur.getSalle().getEpaisseurMur());
            if(mur.isCoinFin())
                decalageCoins = decalageCoins.add(mur.getSalle().getEpaisseurMur());
        }
        points.add(new PointImperial());
        points.add(new PointImperial(mur.getLongueur().add(decalageCoins), new Imperial()));
        points.add(new PointImperial(mur.getLongueur().add(decalageCoins), mur.getHauteur()));
        points.add(new PointImperial(new Imperial(), mur.getHauteur()));
        return new Polygone(points, TypePolygone.PANNEAU);
    }
    private Polygone generateEpaisseurExterieur(boolean Debut){
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Salle salle = mur.getSalle();
        
        Imperial longueurEpaisseur = salle.getEpaisseurMur();
        if((Debut && mur.isCoinDebut()) || (!Debut && mur.isCoinFin()))
           longueurEpaisseur = salle.getLongueurHypothenuseCote();
        
        points.add(new PointImperial());
        points.add(new PointImperial(longueurEpaisseur, new Imperial()));
        points.add(new PointImperial(longueurEpaisseur, mur.getHauteur().substract(salle.getMargeRepliEpaisseur().multiply(2))));
        points.add(new PointImperial(new Imperial(), mur.getHauteur().substract(salle.getMargeRepliEpaisseur().multiply(2))));
        return new Polygone(points, TypePolygone.PANNEAU);
    }
    private Polygone generateEpaisseurInterieur(){
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Salle salle = mur.getSalle();

        Imperial point1PosX = new Imperial();
        Imperial point2PosX = mur.getLongueur().substract(salle.getMargeRepliEpaisseur().multiply(2));
        Imperial point3PosX = mur.getLongueur().substract(salle.getMargeRepliEpaisseur().multiply(2));
        Imperial point4PosX = new Imperial();
        if(mur.isCoinDebut()){
            point2PosX = point2PosX.add(salle.getEpaisseurMur());
            point3PosX = point3PosX.add(salle.getEpaisseurMur());
            point4PosX = point4PosX.add(salle.getEpaisseurMur());
        }
        if(mur.isCoinFin()){
            point2PosX = point2PosX.add(salle.getEpaisseurMur());
        }
        points.add(new PointImperial(point1PosX, new Imperial())); 
        points.add(new PointImperial(point2PosX, new Imperial()));
        points.add(new PointImperial(point3PosX, salle.getEpaisseurMur()));
        points.add(new PointImperial(point4PosX, salle.getEpaisseurMur()));
        
        return new Polygone(points, TypePolygone.PANNEAU);
    }
    private Polygone generateMargePliageExterieur(){
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Salle salle = mur.getSalle();
        points.add(new PointImperial());
        points.add(new PointImperial(salle.getMargePliage(), new Imperial()));
        points.add(new PointImperial(salle.getMargePliage(), mur.getHauteur().substract(salle.getMargeRepliEpaisseur().multiply(2))));
        points.add(new PointImperial(new Imperial(), mur.getHauteur().substract(salle.getMargeRepliEpaisseur().multiply(2))));
        return new Polygone(points, TypePolygone.PANNEAU);
    }
    private Polygone generateMargePliageInterieur(boolean eloigne){
        ArrayList<PointImperial> Points = new ArrayList<PointImperial>();
        Salle salle = mur.getSalle();
        Imperial decalageCoins = new Imperial();
        if(eloigne){
            if(mur.isCoinDebut())
                decalageCoins = decalageCoins.add(mur.getSalle().getEpaisseurMur());
            if(mur.isCoinFin())
                decalageCoins = decalageCoins.add(mur.getSalle().getEpaisseurMur());
        }
        Points.add(new PointImperial());
        Points.add(new PointImperial(mur.getLongueur().add(decalageCoins).substract(salle.getMargeRepliEpaisseur().multiply(2)), new Imperial()));
        Points.add(new PointImperial(mur.getLongueur().add(decalageCoins).substract(salle.getMargeRepliEpaisseur().multiply(2)), salle.getMargePliage()));
        Points.add(new PointImperial(new Imperial(), salle.getMargePliage()));
        return new Polygone(Points, TypePolygone.PANNEAU);
    }
    private Polygone generatePliSoudureExterieur(){
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Salle salle = mur.getSalle();
        
        Imperial inclinaisonPliSoudure = salle.getInclinaisonPliSoudure();
        points.add(new PointImperial(new Imperial(), inclinaisonPliSoudure));
        points.add(new PointImperial(salle.getLongueurPliSoudure(), new Imperial()));
        points.add(new PointImperial(salle.getLongueurPliSoudure(), mur.getHauteur().substract(salle.getMargeRepliEpaisseur().multiply(2))));
        points.add(new PointImperial(new Imperial(), mur.getHauteur().substract(salle.getMargeRepliEpaisseur().multiply(2).add(inclinaisonPliSoudure))));
        return new Polygone(points, TypePolygone.PANNEAU);
    }
    private Polygone generatePliSoudureInterieur(){
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Salle salle = mur.getSalle();
        
        Imperial inclinaisonPliSoudure = salle.getInclinaisonPliSoudure();
        Imperial point1PosX = inclinaisonPliSoudure;
        Imperial point2PosX = mur.getLongueur().substract(salle.getMargeRepliEpaisseur().multiply(2)).substract(inclinaisonPliSoudure);
        Imperial point3PosX = mur.getLongueur().substract(salle.getMargeRepliEpaisseur().multiply(2));
        Imperial point4PosX = new Imperial();
        if(mur.isCoinDebut()){
            point2PosX = point2PosX.add(salle.getEpaisseurMur());
            point3PosX = point3PosX.add(salle.getEpaisseurMur());
        }
        if(mur.isCoinFin()){
            point2PosX = point2PosX.add(salle.getEpaisseurMur());
            point3PosX = point3PosX.add(salle.getEpaisseurMur());
        }
            
        points.add(new PointImperial(point1PosX, new Imperial()));
        points.add(new PointImperial(point2PosX, new Imperial()));
        points.add(new PointImperial(point3PosX, salle.getLongueurPliSoudure()));
        points.add(new PointImperial(point4PosX, salle.getLongueurPliSoudure()));
        
        return new Polygone(points, TypePolygone.PANNEAU);
    }
    
    //manque l'epaisseur des materieaux
    public ArrayList<Polygone> generatePolygonesPanneauExterieur(){
        ArrayList<Polygone> polygones = new ArrayList<Polygone>();
        Imperial translationY = mur.getSalle().getMargeRepliEpaisseur();
        
        //le debut est a la fin car c'est pour le panneau exterieur
        polygones.add(generatePliSoudureExterieur().getTranslation(new PointImperial(new Imperial(), translationY)));
        polygones.add(generateMargePliageExterieur().getTranslation(new PointImperial(polygones.get(polygones.size() - 1).getUpperBoundX(), translationY)));
        polygones.add(generateEpaisseurExterieur(false).getTranslation(new PointImperial(polygones.get(polygones.size() - 1).getUpperBoundX(), translationY)));
        polygones.add(generateMargePliageExterieur().getTranslation(new PointImperial(polygones.get(polygones.size() - 1).getUpperBoundX(), translationY)));
        polygones.add(generateCorp().getTranslation(new PointImperial(polygones.get(polygones.size() - 1).getUpperBoundX(), new Imperial())));
        polygones.add(generateMargePliageExterieur().getTranslation(new PointImperial(polygones.get(polygones.size() - 1).getUpperBoundX(), translationY)));
        polygones.add(generateEpaisseurExterieur(true).getReflection(true).getTranslation(new PointImperial(polygones.get(polygones.size() - 1).getUpperBoundX(), translationY)));
        polygones.add(generateMargePliageExterieur().getTranslation(new PointImperial(polygones.get(polygones.size() - 1).getUpperBoundX(), translationY)));
        polygones.add(generatePliSoudureExterieur().getReflection(true).getTranslation(new PointImperial(polygones.get(polygones.size() - 1).getUpperBoundX(), translationY)));
        
        return polygones;
    }    
    //manque l'epaisseur des materieaux
    public ArrayList<Polygone> generatePolygonesPanneauInterieur(){
        ArrayList<Polygone> polygones = new ArrayList<Polygone>();
        Salle salle = mur.getSalle();
        Imperial translationEpaisseur = salle.getMargeRepliEpaisseur();
        Imperial translationCorp = new Imperial();
        Imperial translationMargeCorp = salle.getMargeRepliEpaisseur();
        if(mur.isCoinDebut()){
            translationEpaisseur = new Imperial();
            translationCorp = salle.getEpaisseurMur().substract(salle.getMargeRepliEpaisseur());
            translationMargeCorp = salle.getEpaisseurMur();
        }

        polygones.add(generatePliSoudureInterieur().getTranslation(new PointImperial(translationEpaisseur, new Imperial())));
        polygones.add(generateMargePliageInterieur(true).getTranslation(new PointImperial(translationEpaisseur, polygones.get(polygones.size()-1).getUpperBoundY())));
        polygones.add(generateEpaisseurInterieur().getTranslation(new PointImperial(translationEpaisseur, polygones.get(polygones.size()-1).getUpperBoundY())));
        polygones.add(generateMargePliageInterieur(false).getTranslation(new PointImperial(translationMargeCorp, polygones.get(polygones.size()-1).getUpperBoundY())));
        polygones.add(generateCorp().getTranslation(new PointImperial(translationCorp, polygones.get(polygones.size()-1).getUpperBoundY())));
        polygones.add(generateMargePliageInterieur(false).getTranslation(new PointImperial(translationMargeCorp, polygones.get(polygones.size()-1).getUpperBoundY())));
        polygones.add(generateEpaisseurInterieur().getReflection(false).getTranslation(new PointImperial(translationEpaisseur, polygones.get(polygones.size()-1).getUpperBoundY())));
        polygones.add(generateMargePliageInterieur(true).getTranslation(new PointImperial(translationEpaisseur, polygones.get(polygones.size()-1).getUpperBoundY())));
        polygones.add(generatePliSoudureInterieur().getReflection(false).getTranslation(new PointImperial(translationEpaisseur, polygones.get(polygones.size()-1).getUpperBoundY())));

        return polygones;
    }
    
    public ArrayList<Polygone> generatePolygonesPanneau(){
        ArrayList<Polygone> polygones = new ArrayList<Polygone>();
        if(isExterieur){
            polygones.addAll(generatePolygonesPanneauExterieur());
        } else {
            polygones.addAll(generatePolygonesPanneauInterieur());
        }
        return polygones;
    }
    
    private ArrayList<PointImperial> generatePerimetreFinPanneauExterieur(boolean coin){
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Salle salle = mur.getSalle();
        Imperial margePliage = salle.getMargePliage(), margeRepliEpaisseur = salle.getMargeRepliEpaisseur(),
                inclinaisonPlisSoudure = salle.getInclinaisonPliSoudure(), longueurPliSoudure = salle.getLongueurPliSoudure(),
                epaisseurMur = salle.getEpaisseurMur(), hauteur = salle.getHauteur();
        if(coin){
            epaisseurMur = salle.getLongueurHypothenuseCote();
        }
        points.add(new PointImperial(longueurPliSoudure.add( 
            epaisseurMur).add(margePliage.multiply(2)), 
            new Imperial())
        );
        points.add(new PointImperial(longueurPliSoudure.add(
            epaisseurMur).add(margePliage.multiply(2)), 
            points.get(points.size()-1).getY().add(margeRepliEpaisseur))
        );
        points.add(new PointImperial(longueurPliSoudure.add(
            epaisseurMur).add(margePliage), 
            points.get(points.size()-1).getY())
        );
        points.add(new PointImperial(
            longueurPliSoudure.add(margePliage), 
            points.get(points.size()-1).getY())
        );
        points.add(new PointImperial(
            longueurPliSoudure, 
            points.get(points.size()-1).getY())
        );
        points.add(new PointImperial(
            new Imperial(), 
            points.get(points.size()-1).getY().add(inclinaisonPlisSoudure))
        );
        points.add(new PointImperial(
            new Imperial(), 
            points.get(points.size()-1).getY().add(hauteur.substract(margeRepliEpaisseur.multiply(2).add(inclinaisonPlisSoudure.multiply(2)))))
        );
        points.add(new PointImperial(
            longueurPliSoudure, 
            points.get(points.size()-1).getY().add(inclinaisonPlisSoudure))
        );
        points.add(new PointImperial(
            longueurPliSoudure.add(margePliage), 
            points.get(points.size()-1).getY())
        );
        points.add(new PointImperial(
            longueurPliSoudure.add(epaisseurMur).add(margePliage), 
            points.get(points.size()-1).getY())
        );
        points.add(new PointImperial(
            longueurPliSoudure.add(epaisseurMur).add(margePliage.multiply(2)), 
            points.get(points.size()-1).getY())
        );
        points.add(new PointImperial(
            longueurPliSoudure.add(epaisseurMur).add(margePliage.multiply(2)), 
            points.get(points.size()-1).getY().add(margeRepliEpaisseur))
        );
        return points;
    }
    private ArrayList<PointImperial> generatePerimetreDebutPanneauExterieur(Imperial decalageX){
        ArrayList<PointImperial> points = generatePerimetreFinPanneauExterieur(mur.isCoinFin());
        Polygone polygone = new Polygone(points, null);
        return polygone.getReflection(true).getTranslation(decalageX, true).getImperialPoints();
    }

    private ArrayList<PointImperial> generatePerimetreDebutPanneauInterieur(){
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Salle salle = mur.getSalle();
        Imperial margePliage = salle.getMargePliage(), margeRepliEpaisseur = salle.getMargeRepliEpaisseur(),
                inclinaisonPlisSoudure = salle.getInclinaisonPliSoudure(), longueurPliSoudure = salle.getLongueurPliSoudure(),
                epaisseurMur = salle.getEpaisseurMur(), hauteur = salle.getHauteur();
        points.add(new PointImperial(
            margeRepliEpaisseur.add(inclinaisonPlisSoudure),
            new Imperial())
        );
        points.add(new PointImperial(
            margeRepliEpaisseur, 
            points.get(points.size()-1).getY().add(longueurPliSoudure))
        );
        points.add(new PointImperial(
            margeRepliEpaisseur, 
            points.get(points.size()-1).getY().add(margePliage))
        );
        points.add(new PointImperial(
            margeRepliEpaisseur, 
            points.get(points.size()-1).getY().add(epaisseurMur))
        );
        points.add(new PointImperial(
            margeRepliEpaisseur, 
            points.get(points.size()-1).getY().add(margePliage))
        );
        points.add(new PointImperial(
            new Imperial(), 
            points.get(points.size()-1).getY())
        );
        points.add(new PointImperial(
            new Imperial(), 
            points.get(points.size()-1).getY().add(hauteur))
        );
        points.add(new PointImperial(
            margeRepliEpaisseur, 
            points.get(points.size()-1).getY())
        );
        points.add(new PointImperial( 
            margeRepliEpaisseur, 
            points.get(points.size()-1).getY().add(margePliage))
        );
        points.add(new PointImperial(
            margeRepliEpaisseur, 
            points.get(points.size()-1).getY().add(epaisseurMur))
        );
        points.add(new PointImperial(
            margeRepliEpaisseur, 
            points.get(points.size()-1).getY().add(margePliage))
        );
        points.add(new PointImperial(
            margeRepliEpaisseur.add(inclinaisonPlisSoudure), 
            points.get(points.size()-1).getY().add(longueurPliSoudure))
        );
        return points;
    }
    private ArrayList<PointImperial> generatePerimetreCoinDebutPanneauInterieur(){
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Salle salle = mur.getSalle();
        Imperial margePliage = salle.getMargePliage(), margeRepliEpaisseur = salle.getMargeRepliEpaisseur(),
                inclinaisonPlisSoudure = salle.getInclinaisonPliSoudure(), longueurPliSoudure = salle.getLongueurPliSoudure(),
                epaisseurMur = salle.getEpaisseurMur(), hauteur = salle.getHauteur();
        points.add(new PointImperial(
            inclinaisonPlisSoudure, 
            new Imperial())
        );
        points.add(new PointImperial(
            new Imperial(), 
            points.get(points.size()-1).getY().add(longueurPliSoudure))
        );
        points.add(new PointImperial(
            new Imperial(), 
            points.get(points.size()-1).getY().add(margePliage))
        );
        points.add(new PointImperial(
            epaisseurMur, 
            points.get(points.size()-1).getY().add(epaisseurMur))
        );
        points.add(new PointImperial(
            epaisseurMur, 
            points.get(points.size()-1).getY().add(margePliage))
        );
        points.add(new PointImperial(
            epaisseurMur.substract(margeRepliEpaisseur), 
            points.get(points.size()-1).getY())
        );
        points.add(new PointImperial(
            epaisseurMur.substract(margeRepliEpaisseur), 
            points.get(points.size()-1).getY().add(hauteur))
        );
        points.add(new PointImperial(
            epaisseurMur, 
            points.get(points.size()-1).getY())
        );
        points.add(new PointImperial( 
            epaisseurMur, 
            points.get(points.size()-1).getY().add(margePliage))
        );
        points.add(new PointImperial(
            new Imperial(), 
            points.get(points.size()-1).getY().add(epaisseurMur))
        );
        points.add(new PointImperial(
            new Imperial(), 
            points.get(points.size()-1).getY().add(margePliage))
        );
        points.add(new PointImperial(
            inclinaisonPlisSoudure, 
            points.get(points.size()-1).getY().add(longueurPliSoudure))
        );
        return points;
    }
    private ArrayList<PointImperial> generatePerimetreFinPanneauInterieur(Imperial decalageX){
        ArrayList<PointImperial> points = generatePerimetreDebutPanneauInterieur();
        Polygone polygone = new Polygone(points, null);
        return polygone.getReflection(true).getTranslation(decalageX, true).getImperialPoints();
    }
    private ArrayList<PointImperial> generatePerimetreCoinFinPanneauInterieur(Imperial decalageX){
        ArrayList<PointImperial> points = generatePerimetreCoinDebutPanneauInterieur();
        Polygone polygone = new Polygone(points, null);
        return polygone.getReflection(true).getTranslation(decalageX, true).getImperialPoints();
    }
    
    public Polygone generatePerimetrePanneauExterieur(){
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Salle salle = mur.getSalle();
        Imperial decalageDebut = mur.getLongueur().add(salle.getLongueurPliSoudure()).add(salle.getEpaisseurMur()).add(salle.getMargePliage().multiply(2));
        points.addAll(generatePerimetreFinPanneauExterieur(mur.isCoinFin()));
        if(mur.isCoinFin()){
            decalageDebut = decalageDebut.add(salle.getLongueurHypothenuseCote());
        }
        if(mur.isCoinDebut()){
            decalageDebut = decalageDebut.add(salle.getEpaisseurMur());
        }
        points.addAll(generatePerimetreDebutPanneauExterieur(decalageDebut));
        return new Polygone(points, TypePolygone.PANNEAU);
    }
    public Polygone generatePerimetrePanneauInterieur(){
        ArrayList<PointImperial> points = new ArrayList<PointImperial>();
        Salle salle = mur.getSalle();
        Imperial decalageFin = mur.getLongueur().substract(salle.getMargeRepliEpaisseur().add(salle.getInclinaisonPliSoudure()));
        if(mur.isCoinDebut()){
            points.addAll(generatePerimetreCoinDebutPanneauInterieur());
            decalageFin = decalageFin.add(salle.getEpaisseurMur().substract(salle.getMargeRepliEpaisseur()));

        } else {
            points.addAll(generatePerimetreDebutPanneauInterieur());
        }
        if (mur.isCoinFin()){
            decalageFin = decalageFin.add(salle.getInclinaisonPliSoudure());
            points.addAll(generatePerimetreCoinFinPanneauInterieur(decalageFin));
        } else {
            points.addAll(generatePerimetreFinPanneauInterieur(decalageFin));
        }
        return new Polygone(points, TypePolygone.PANNEAU);
    }
    
    private PointImperial getPanneauRelativeMesureFromCoteRelativeMesure(PointImperial pointRelatifCote, boolean exterieur){
        Salle salle = mur.getSalle();
        PointImperial pointRelatifPanneau = pointRelatifCote.substract(mur.getPosX(), new Imperial());
        if(exterieur){
            //inverse les coordonnees du point
            pointRelatifPanneau = new PointImperial(mur.getLongueur().substract(pointRelatifPanneau.getX()), pointRelatifPanneau.getY());
            //ajoute du padding pour les bandes horizontals du panneau
            pointRelatifPanneau = pointRelatifPanneau.add(salle.getLongueurPliSoudure().add(salle.getEpaisseurMur()).add(salle.getMargePliage().multiply(2)), new Imperial());
            if(mur.isCoinFin()){
                pointRelatifPanneau = pointRelatifPanneau.add(salle.getLongueurHypothenuseCote(), new Imperial());
            }
        } else{
            pointRelatifPanneau = pointRelatifPanneau.add(new Imperial(), salle.getEpaisseurMur().add(salle.getLongueurPliSoudure()).add(salle.getMargePliage().multiply(2)));
            if(mur.isCoinDebut()){
                pointRelatifPanneau = pointRelatifPanneau.add(salle.getEpaisseurMur().substract(salle.getMargeRepliEpaisseur()), new Imperial());
            }
        }
        return pointRelatifPanneau;
    }
    public ArrayList<Polygone> generatePolygonesAccessoires(){
        ArrayList<Polygone> polygones = new ArrayList<Polygone>();
        for(Accessoire accessoire : mur.getAccessoires()){
            Salle salle = mur.getSalle();
            PointImperial coorAccessoire = getPanneauRelativeMesureFromCoteRelativeMesure(accessoire.getCoor(), isExterieur);
            Imperial longueurAccessoire = accessoire.getLongueur();
            Imperial hauteurAccessoire = accessoire.getHauteur();
            if(isExterieur){
                coorAccessoire = coorAccessoire.substract(longueurAccessoire, new Imperial());
            }
            
            switch (accessoire.getType()){
                case FENETRE:
                    Fenetre fenetre = (Fenetre)accessoire;
                    coorAccessoire = coorAccessoire.substract(fenetre.getMargeMoulure(), fenetre.getMargeMoulure());
                    longueurAccessoire = fenetre.getLongueur().add(fenetre.getMargeMoulure().multiply(2));
                    hauteurAccessoire = fenetre.getHauteur().add(fenetre.getMargeMoulure().multiply(2));
                    break;
                    
                case PRISE:
                    if(isExterieur){
                        continue;
                    }
                    break;
                    
                case RETOUR_AIR:
                    if(isExterieur){           
                        continue;
                    } else {
                        RetourAir retourAir = (RetourAir)accessoire;
                        Imperial hauteurDessusRetourAir = salle.getHauteurTrouRetourAir();
                        Imperial posYDessusRetourAir = salle.getEpaisseurMur().substract(salle.getHauteurTrouRetourAir()).divide(2).add(salle.getLongueurPliSoudure()).add(salle.getMargePliage());
                        
                        ArrayList<PointImperial> pointsDessusRetourAir = new ArrayList<PointImperial>();
                        pointsDessusRetourAir.add(new PointImperial(coorAccessoire.getX(), posYDessusRetourAir));
                        pointsDessusRetourAir.add(new PointImperial(coorAccessoire.getX().add(longueurAccessoire), posYDessusRetourAir));
                        pointsDessusRetourAir.add(new PointImperial(coorAccessoire.getX().add(longueurAccessoire), posYDessusRetourAir.add(hauteurDessusRetourAir)));
                        pointsDessusRetourAir.add(new PointImperial(coorAccessoire.getX(), posYDessusRetourAir.add(hauteurDessusRetourAir)));
                        polygones.add(new Polygone(pointsDessusRetourAir, TypePolygone.RETOUR_AIR));
                    }
                    break;
            }
            ArrayList<PointImperial> pointsAccessoire = calculAccessoireAvecPli(coorAccessoire,accessoire, longueurAccessoire, hauteurAccessoire);
            polygones.add(new Polygone(pointsAccessoire, TypePolygone.fromString(accessoire.getType().getValue())));
        }
        return polygones;
    }
    
    private ArrayList<PointImperial> calculAccessoireAvecPli(PointImperial coorAccessoire, Accessoire accessoire, Imperial longueurAccessoire, Imperial hauteurAccessoire) {
        ArrayList<PointImperial> pointsAccessoire = new ArrayList<PointImperial>();
        Salle salle = getSalle();
        Imperial accessoirePosX = coorAccessoire.getX();
        Imperial accessoirePosY = coorAccessoire.getY();

        boolean accessoireAccepte = (accessoire.getType() == TypeAccessoire.FENETRE || accessoire.getType() == TypeAccessoire.PORTE);
        pointsAccessoire.add(new PointImperial(accessoirePosX,accessoirePosY));
        if (accessoireAccepte && !isExterieur && mur.getMinY().getDoublePixelValue() < accessoire.getMinY().substract(salle.getMargeRepliEpaisseur().multiply(2)).getDoublePixelValue()) { 
            // pli haut
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(salle.getEpaisseurMateriaux()), 
                    accessoirePosY)); 
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(salle.getEpaisseurMateriaux()), 
                    accessoirePosY.add(salle.getEpaisseurMur())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(salle.getEpaisseurMateriaux()).add(salle.getInclinaisonPliSoudure()), 
                    accessoirePosY.add(salle.getEpaisseurMur()).add(salle.getMargeRepliEpaisseur())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.substract(salle.getEpaisseurMateriaux()).substract(salle.getInclinaisonPliSoudure()).add(longueurAccessoire), 
                    accessoirePosY.add(salle.getEpaisseurMur()).add(salle.getMargeRepliEpaisseur())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.substract(salle.getEpaisseurMateriaux()).add(longueurAccessoire), 
                    accessoirePosY.add(salle.getEpaisseurMur())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.substract(salle.getEpaisseurMateriaux()).add(longueurAccessoire), 
                    accessoirePosY));
        }
        pointsAccessoire.add(new PointImperial(accessoirePosX.add(longueurAccessoire), accessoirePosY));
        if (accessoireAccepte && isExterieur && mur.getMinX().getDoublePixelValue() < accessoire.getMinX().substract(salle.getMargeRepliEpaisseur().multiply(2)).getDoublePixelValue()) { 
            // pli gauche
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(longueurAccessoire), 
                    accessoirePosY.add(salle.getEpaisseurMateriaux())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(longueurAccessoire).substract(salle.getEpaisseurMur()), 
                    accessoirePosY.add(salle.getEpaisseurMateriaux())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(longueurAccessoire).substract(salle.getEpaisseurMur()).substract(salle.getMargeRepliEpaisseur()), 
                    accessoirePosY.add(salle.getEpaisseurMateriaux()).add(salle.getInclinaisonPliSoudure())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(longueurAccessoire).substract(salle.getEpaisseurMur()).substract(salle.getMargeRepliEpaisseur()), 
                    accessoirePosY.add(hauteurAccessoire).substract(salle.getEpaisseurMateriaux()).substract(salle.getInclinaisonPliSoudure())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(longueurAccessoire).substract(salle.getEpaisseurMur()), 
                    accessoirePosY.add(hauteurAccessoire).substract(salle.getEpaisseurMateriaux())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(longueurAccessoire), 
                    accessoirePosY.add(hauteurAccessoire).substract(salle.getEpaisseurMateriaux()))); 
        }
        pointsAccessoire.add(new PointImperial(accessoirePosX.add(longueurAccessoire), accessoirePosY.add(hauteurAccessoire)));
        if (accessoireAccepte && !isExterieur && mur.getMaxY().getDoublePixelValue() > accessoire.getMaxY().add(salle.getMargeRepliEpaisseur().multiply(2)).getDoublePixelValue()) { 
            // pli bas
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.substract(salle.getEpaisseurMateriaux()).add(longueurAccessoire), 
                    accessoirePosY.add(hauteurAccessoire)));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.substract(salle.getEpaisseurMateriaux()).add(longueurAccessoire), 
                    accessoirePosY.add(hauteurAccessoire).substract(salle.getEpaisseurMur())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.substract(salle.getEpaisseurMateriaux()).substract(salle.getInclinaisonPliSoudure()).add(longueurAccessoire), 
                    accessoirePosY.add(hauteurAccessoire).substract(salle.getEpaisseurMur()).substract(salle.getMargeRepliEpaisseur())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(salle.getEpaisseurMateriaux()).add(salle.getInclinaisonPliSoudure()), 
                    accessoirePosY.add(hauteurAccessoire).substract(salle.getEpaisseurMur()).substract(salle.getMargeRepliEpaisseur())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(salle.getEpaisseurMateriaux()), 
                    accessoirePosY.add(hauteurAccessoire).substract(salle.getEpaisseurMur())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(salle.getEpaisseurMateriaux()), 
                    accessoirePosY.add(hauteurAccessoire))); 
        }
        pointsAccessoire.add(new PointImperial(accessoirePosX, accessoirePosY.add(hauteurAccessoire)));
        if (accessoireAccepte && isExterieur && mur.getMaxX().getDoublePixelValue() > accessoire.getMaxX().add(salle.getMargeRepliEpaisseur().multiply(2)).getDoublePixelValue()) { 
            // pli droit
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX, 
                    accessoirePosY.add(hauteurAccessoire).substract(salle.getEpaisseurMateriaux()))); 
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(salle.getEpaisseurMur()), 
                    accessoirePosY.add(hauteurAccessoire).substract(salle.getEpaisseurMateriaux())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(salle.getEpaisseurMur()).add(salle.getMargeRepliEpaisseur()), 
                    accessoirePosY.add(hauteurAccessoire).substract(salle.getEpaisseurMateriaux()).substract(salle.getInclinaisonPliSoudure())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(salle.getEpaisseurMur()).add(salle.getMargeRepliEpaisseur()), 
                    accessoirePosY.add(salle.getEpaisseurMateriaux()).add(salle.getInclinaisonPliSoudure())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX.add(salle.getEpaisseurMur()), 
                    accessoirePosY.add(salle.getEpaisseurMateriaux())));
            pointsAccessoire.add(new PointImperial(
                    accessoirePosX, 
                    accessoirePosY.add(salle.getEpaisseurMateriaux())));
        }
        return pointsAccessoire;
    }
    
    public ArrayList<Polygone> generateSVGPolygones(){
        ArrayList<Polygone> polygones = new ArrayList<Polygone>();
        if(isExterieur){
            polygones.add(generatePerimetrePanneauExterieur());
            polygones.addAll(generatePolygonesAccessoires());
        } else {
            polygones.add(generatePerimetrePanneauInterieur());
            polygones.addAll(generatePolygonesAccessoires());
        }
        return polygones;
    }
    
    @Override
    public void calculeDisposition() {
        polygones.clear();
        polygones.addAll(this.generatePolygonesPanneau());
        polygones.addAll(this.generatePolygonesAccessoires());
        calculePoids();
    }
    
    public Imperial getAirePanneau() {
        ArrayList<Polygone> panneau = generatePolygonesPanneau();
        Imperial airePanneauImperial = new Imperial();
        for(Polygone polygone : panneau){
            airePanneauImperial = airePanneauImperial.add(polygone.getAreaImperial());
        }
        ArrayList<Polygone> accessoires = generatePolygonesAccessoires();
        Imperial aireAccessoirImperial = new Imperial();
        for(Polygone polygone : accessoires){
            aireAccessoirImperial = aireAccessoirImperial.add(polygone.getAreaImperial());
        }
        Imperial aireImperial = airePanneauImperial.substract(aireAccessoirImperial);
        return aireImperial;
    }
    
    @Override
    public double getAire(){
        return getAirePanneau().getDoubleValue();
    }
    
    public boolean getIsTropLourd() {
        return isTropLourd;
    }

    public void setIsTropLourd(boolean isTropLourd) {
        if (isTropLourd){
            for(Polygone p : polygones){
                p.setColor(Color.RED);
            }
        } else {
            for(Polygone p : polygones){
                p.setColor(Color.GRAY);
            }
        }
        this.isTropLourd = isTropLourd;
    }
    
    public double calculePoids(){
        poids  = Math.round(Imperial.pouceCarreToPiedCarre(getAire())*MASSE_SURFACIQUE_MATERIAU*100d)/100d;
        setIsTropLourd(poids>250);
        return poids;
    }

    
    /*// TODO : A optimiser, il devrait y avoir une fonction calculDisposition dans la classe parent ElementSalle 
    // qui fait le calcul ci dessous dans une boucle. La fonction calculeDisposition de la classe enfant ne devrait que  
    // lui donner les parametres du calcul.
    // TODO : pour l'instant n'affiche que le cote interieur du panneau, faire en sorte qu'il affiche aussi l'exterieur. (Utiliser la variable isExterieur)
    @Override
    public void calculeDisposition() {
//        this.listePolygones.clear();
        int xMaxPrecedent = 0;

        int longeur = (int) getLongueur().getDoublePixelValue();
        int hauteur = (int) getHauteur().getDoublePixelValue();
        int largeur = (int) getLargeur().getDoublePixelValue();
        int pixelLongueurPliSoudure = (int) longueurPliSoudure.getDoublePixelValue();
        int pixelInclinaisonPliSoudure = (int) (longueurPliSoudure.getDoublePixelValue()*Math.tan(anglePliSoudure));

        // Calcul polygone du pli de soudure
        ArrayList<Point> newPoints = new ArrayList<Point>();
        newPoints.add(new Point(xMaxPrecedent, 0 + pixelInclinaisonPliSoudure));		
        newPoints.add(new Point(xMaxPrecedent+pixelLongueurPliSoudure, 0));
        newPoints.add(new Point(xMaxPrecedent+pixelLongueurPliSoudure, 0 + hauteur));
        newPoints.add(new Point(xMaxPrecedent, 0 - pixelInclinaisonPliSoudure + hauteur));
       // listePolygones.add(new Polygone(newPoints));
        xMaxPrecedent += pixelLongueurPliSoudure;
        
        // TODO : faire polygone du pli entre les parties du panneau.
        
        // Calcul polygone de la largeur gauche du mur.
        newPoints = new ArrayList<Point>();
        newPoints.add(new Point(xMaxPrecedent               , 0            ));		
        newPoints.add(new Point(xMaxPrecedent + largeur    , 0            ));
        newPoints.add(new Point(xMaxPrecedent + largeur    , 0 + hauteur));
        newPoints.add(new Point(xMaxPrecedent               , 0 + hauteur));
       // listePolygones.add(new Polygone(newPoints));
        xMaxPrecedent += largeur;
        
        // Calcul polygone du mur
        newPoints = new ArrayList<Point>();
        newPoints.add(new Point(xMaxPrecedent                , 0            ));		
        newPoints.add(new Point(xMaxPrecedent + longeur    , 0            ));
        newPoints.add(new Point(xMaxPrecedent + longeur    , 0 + hauteur));
        newPoints.add(new Point(xMaxPrecedent                , 0 + hauteur));
      //  listePolygones.add(new Polygone(newPoints));
        xMaxPrecedent += longeur;
        
        // Calcul polygone de la largeur droit du mur.
        newPoints = new ArrayList<Point>();
        newPoints.add(new Point(xMaxPrecedent               , 0            ));		
        newPoints.add(new Point(xMaxPrecedent + largeur    , 0            ));
        newPoints.add(new Point(xMaxPrecedent + largeur    , 0 + hauteur));
        newPoints.add(new Point(xMaxPrecedent               , 0 + hauteur));
       // listePolygones.add(new Polygone(newPoints));
        xMaxPrecedent += largeur;
        
        // Calcul polygone du pli de soudure
        newPoints = new ArrayList<Point>();
        newPoints.add(new Point(xMaxPrecedent                                           , 0                                                         ));		
        newPoints.add(new Point(xMaxPrecedent + pixelLongueurPliSoudure    , 0 + pixelInclinaisonPliSoudure                ));
        newPoints.add(new Point(xMaxPrecedent + pixelLongueurPliSoudure    , 0 - pixelInclinaisonPliSoudure + hauteur));
        newPoints.add(new Point(xMaxPrecedent                                           , 0 + hauteur                                             ));
       // listePolygones.add(new Polygone(newPoints));
        xMaxPrecedent += pixelLongueurPliSoudure;
    }    */
}
