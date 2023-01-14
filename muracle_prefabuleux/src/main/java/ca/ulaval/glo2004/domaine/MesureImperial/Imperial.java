package ca.ulaval.glo2004.domaine.MesureImperial;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author anto-
 * Mesure en pouce
 */
public class Imperial  implements Serializable {
    private int entier;
    private int numerateur;
    private int denominateur;
    public static final int ratioPixelPouce = 128;
    private static final int maxNombreVirgule = 10;
    
    public Imperial(int entier, int numerateur, int denominateur){
        this.entier = entier;
        if (numerateur >= denominateur) {
            this.entier += Math.floor(numerateur / denominateur);
            this.numerateur = numerateur % denominateur;
        }
        else {
            this.numerateur = numerateur;
        }
        this.denominateur = denominateur;
    }
    public Imperial(int entier){
        this(entier,0,1);
    }
    public Imperial(){
        this(0, 0, 1);
    }
    
    public int getEntier(){
        return entier;
    }
    public int getNumerateur(){
        return numerateur;
    }
    public int getDenominateur(){
        return denominateur;
    }
    
    public Imperial add(Imperial imp){
        return Imperial.add(this, imp);
    }
    public Imperial add(double imp){
        return Imperial.add(this, new Imperial((int)imp));
    }
    public Imperial substract(Imperial imp){
         return Imperial.substract(this, imp);
    }
    public Imperial substract(double imp){
         return Imperial.substract(this, new Imperial((int)imp));
    }
    public Imperial multiply(Imperial imp){
         return Imperial.multiply(this, imp);
    }
    public Imperial multiply(double value){
         return Imperial.multiply(this, Imperial.getImperialValue(value));
    }
    public Imperial divide(Imperial imp){
        return Imperial.divide(this, imp);
    }
    public Imperial divide(double value){
        return Imperial.divide(this, Imperial.getImperialValue(value));
    }
    
    public static Imperial add(Imperial imp1, Imperial imp2){
        return calculAdditionSubstraction(imp1, imp2,true);
    }
    
    public static Imperial substract(Imperial imp1, Imperial imp2){
        return calculAdditionSubstraction(imp1, imp2,false);
    }
    
    private static Imperial calculAdditionSubstraction(Imperial imp1, Imperial imp2,boolean isAddition) {
        int entier1 = imp1.entier;
        int entier2 = imp2.entier;
        int signeDebut = 1;
        while (entier1 < 0 || entier2 < 0) {
            if(entier2 < 0 && !isAddition) { // si imp1 - -imp2 -> imp1 + imp2
                isAddition = true;
                entier2 = Math.abs(imp2.entier);
            }
            else if (entier2 < 0 && isAddition) {
                isAddition = false;
                entier2 = Math.abs(imp2.entier);
            }
            else if (entier1 < 0 && isAddition) { // si -imp1 + imp2 -> imp2 - imp1
                isAddition = false;
                entier1 = entier2;
                Imperial tmp = imp1;
                imp1 = imp2;
                imp2 = tmp;
                entier2 = Math.abs(imp2.entier);
            }
            else if (entier1 < 0 && entier2 >= 0 && !isAddition) { // si -imp1 - imp2 -> -(imp1 + imp2)
                isAddition = true;
                signeDebut = -1;
                entier1 = Math.abs(entier1);
            }
        }
        int numerateurTotal1 = entier1 * imp1.denominateur + imp1.numerateur;
        int numerateurTotal2 = entier2 * imp2.denominateur + imp2.numerateur;
        int denominateurCommun = imp1.denominateur;
        if (imp1.denominateur !=  imp2.denominateur) {
            numerateurTotal1 *= imp2.denominateur;
            numerateurTotal2 *= imp1.denominateur;
            denominateurCommun = imp1.denominateur * imp2.denominateur; 
        }
        int operationNumerateur = 0;
        if(isAddition) {
            operationNumerateur = numerateurTotal1 + numerateurTotal2;
        }
        else {
            operationNumerateur = numerateurTotal1 - numerateurTotal2;
        }
        int signe = operationNumerateur<0 ? -1 : 1;
        operationNumerateur = Math.abs(operationNumerateur);
        int gcd = gcd(operationNumerateur, denominateurCommun);
        operationNumerateur /= gcd;
        denominateurCommun /= gcd;
        int resultatEntier = 0;
        int resultatNumerateur = operationNumerateur;
        if (operationNumerateur >= denominateurCommun) {
            resultatEntier = (int) Math.floor(operationNumerateur / denominateurCommun) * signe * signeDebut;
            resultatNumerateur = operationNumerateur % denominateurCommun;
        }
        return new Imperial(resultatEntier,resultatNumerateur,denominateurCommun);
    }
    
    public static Imperial multiply(Imperial imp1, Imperial imp2){
        return calculMultiplyDivide(imp1,imp2,true);
    }
    public static Imperial divide(Imperial imp1, Imperial imp2){
        if (imp2.getDoublePixelValue() != 0) {
            return calculMultiplyDivide(imp1,imp2,false);
        }
        return new Imperial(0);
    }
    
    private static Imperial calculMultiplyDivide(Imperial imp1, Imperial imp2,boolean isMultiplication) {
        int signe = imp1.entier<0 ^ imp2.entier<0 ? -1 : 1; 
        int entier1 =  Math.abs(imp1.entier);
        int entier2 =  Math.abs(imp2.entier);
        int numerateurTotal1 = entier1 * imp1.denominateur + imp1.numerateur;
        int numerateurTotal2 = entier2 * imp2.denominateur + imp2.numerateur;
        int resultNumerateur;
        int resultatDenominateur;
        if (isMultiplication) {
            resultNumerateur = numerateurTotal1 * numerateurTotal2;
            resultatDenominateur = imp1.denominateur * imp2.denominateur;
        }
        else {
            resultNumerateur = numerateurTotal1 * imp2.denominateur;
            resultatDenominateur = imp1.denominateur * numerateurTotal2;
        }
        int gcd = gcd(resultNumerateur, resultatDenominateur);
        resultNumerateur /= gcd;
        resultatDenominateur /= gcd;
        int entier = 0;
        if (resultNumerateur >= resultatDenominateur) {
            entier = (int) Math.floor(resultNumerateur / resultatDenominateur) * signe;
            resultNumerateur = resultNumerateur % resultatDenominateur;
        }
        return new Imperial(entier, resultNumerateur, resultatDenominateur);
    }
    
    public double getDoublePixelValue(){
        int signe = this.entier<0 ? -1 : 1; 
        int resultat = Math.abs(entier) * denominateur + numerateur;
        resultat *=  ratioPixelPouce;
        resultat /= denominateur;
        resultat *= signe;
        return resultat;
    }
    
    public double getDoubleValue(){
        int signe = this.entier<0 ? -1 : 1; 
        int resultat = Math.abs(entier) * denominateur + numerateur;
        resultat /= denominateur;
        resultat *= signe;
        return resultat;
    }
    
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    public static Imperial getImperialFromPixelValue(double x){
        int signe = x<0 ? -1 : 1; 
        x = Math.abs(x);
        double numerateur = x;
        double denominateur = ratioPixelPouce;
        int compteur = 0;
        while (numerateur-(int)Math.floor(numerateur) != 0 && compteur<maxNombreVirgule) {
            numerateur *= 10;
            denominateur *= 10;
            compteur++;
        }
        if (numerateur-(int)Math.floor(numerateur) != 0) { numerateur = Math.round(numerateur); }
        int gcd = gcd((int)numerateur, (int)denominateur);
        numerateur /= gcd;
        denominateur /= gcd;
        int entier = 0;
        if (numerateur >= denominateur) {
            entier = (int) Math.floor(numerateur / denominateur) * signe;
            numerateur = numerateur % denominateur;
        }
        return new Imperial(entier, (int)numerateur, (int)denominateur);
    }
    
    public static Imperial getImperialValue(double x){
        int signe = x<0 ? -1 : 1; 
        x = Math.abs(x);
        double numerateur = x;
        double denominateur = 1;
        int compteur = 0;
        while (numerateur-(int)Math.floor(numerateur) != 0  && compteur<maxNombreVirgule) {
            numerateur *= 10;
            denominateur *= 10;
            compteur++;
        }
        int gcd = gcd((int)numerateur, (int)denominateur);
        numerateur /= gcd;
        denominateur /= gcd;
        int entier = 0;
        if (numerateur >= denominateur) {
            entier = (int) Math.floor(numerateur / denominateur) * signe;
            numerateur = numerateur % denominateur;
        }
        return new Imperial(entier, (int)numerateur, (int)denominateur);
    }
    
    public boolean equals(Imperial imp){
        return equal(this,imp);
    }
    public static boolean equal(Imperial imp1, Imperial imp2){
        return imp1.getDoublePixelValue() == imp2.getDoublePixelValue();
    }
    public boolean isGreater(Imperial lowerBound){
        return Imperial.isGreater(this, lowerBound);
    }
     public static boolean isGreater(Imperial value, Imperial lowerBound){
        return value.getDoublePixelValue() >= lowerBound.getDoublePixelValue();
    }
    public boolean isBetween(Imperial lowerBound, Imperial upperBound){
        return Imperial.isBetween(this, lowerBound, upperBound);
    }
    public static boolean isBetween(Imperial value, Imperial lowerBound, Imperial upperBound) {
        return value.isGreater(lowerBound) && upperBound.isGreater(value);
    }
    
    public static Imperial getImperialFromString(String value) throws Exception {
        String trimmedValue = value.trim().replaceAll(" +", " ");
        String replacedValue = trimmedValue.replace(' ','/');
        String data[] = replacedValue.split("/", 3);
        
        try {
            int entier = Integer.parseInt(data[0]);
            if(entier < 0)
                throw new Exception("Une mesure en pouce dois etre positive.");
            if (data.length == 1) {
                return new Imperial(entier);
            }
            int numerateur = Integer.parseInt(data[1]);
            int denominateur = Integer.parseInt(data[2]);
            if(numerateur < 0 || denominateur < 0)
                throw new Exception("Une mesure en pouce dois etre positive.");
            if(denominateur == 0){
                throw new Exception("Le denominateur ne peut pas etre 0.");
            }
            return new Imperial(entier,numerateur,denominateur);
        } catch (NumberFormatException e) {
            throw new Exception("La chaine de charactere " + value + " ne respecte pas la forme: a b/c ou a, b et c sont des entiers positif.");
        }
    }
    
    public static ArrayList<PointImperial> rotation (ArrayList<PointImperial> listePointsImperial, PointImperial centreSalle, int degreRotation) {
       ArrayList<PointImperial> newList = new ArrayList<PointImperial>();
       double[][] matriceRotation = genereMatriceRotation(degreRotation);
       listePointsImperial = translationSoustraction(listePointsImperial, centreSalle);
       for(PointImperial point : listePointsImperial){
            Imperial x = point.getX();
            Imperial y = point.getY();
            PointImperial newPoint = new PointImperial(
                    x.multiply((int)matriceRotation[0][0]).add(y.multiply((int)matriceRotation[0][1])),
                    x.multiply((int)matriceRotation[1][0]).add(y.multiply((int)matriceRotation[1][1]))
                   );
            newList.add(newPoint);
       }
       newList = translationAddition(newList, centreSalle);
       return newList;
    }
    
    public static ArrayList<PointImperial> translationSoustraction (ArrayList<PointImperial> listePointsImperial, PointImperial pointCentre) {
        ArrayList<PointImperial> matriceTransforme = new ArrayList<PointImperial>();
        for(PointImperial point : listePointsImperial) {
            matriceTransforme.add(new PointImperial(
                    substract(point.getX(), pointCentre.getX()),
                    substract(point.getY(), pointCentre.getY())
            ));
        }
        return  matriceTransforme;
    }
    
    public static ArrayList<PointImperial> translationAddition (ArrayList<PointImperial> listePointsImperial, PointImperial pointCentre) {
        ArrayList<PointImperial> matriceTransforme = new ArrayList<PointImperial>();
        for(PointImperial point : listePointsImperial) {
            matriceTransforme.add(new PointImperial(
                    add(point.getX(), pointCentre.getX()),
                    add(point.getY(), pointCentre.getY())
            ));
        }
        return  matriceTransforme;
    }

    private static double[][] genereMatriceRotation (int degreRotation) {
        double[][] matrice = new double[2][2];
        double degreRotationToRad = Math.toRadians(degreRotation);
        matrice[0][0] = Math.round(Math.cos(degreRotationToRad)) ;
        matrice[0][1] = -Math.round(Math.sin(degreRotationToRad));
        matrice[1][0] = Math.round(Math.sin(degreRotationToRad));
        matrice[1][1] = Math.round(Math.cos(degreRotationToRad));
        return matrice;
    }
    
    public static Imperial calculDistanceCoin (Imperial epaisseurMur, Imperial epaisseurMateriaux) {
        double epaisseurMurPixel = epaisseurMur.getDoublePixelValue();
        double epaisseurMateriauxPixel = epaisseurMateriaux.getDoublePixelValue();

        double hypothenuse =  Math.sqrt(Math.pow(epaisseurMurPixel, 2) + Math.pow(epaisseurMurPixel, 2));
        double angle = Math.asin(epaisseurMurPixel / hypothenuse);
        double sousAngle = angle / 2;
        double distanceCoin = Math.tan(sousAngle) * epaisseurMateriauxPixel * 2;
        return Imperial.getImperialFromPixelValue(distanceCoin);
    }
    
    public static double pouceCarreToPiedCarre(double pouceCarre){
        return pouceCarre/144.0;
    }
    
    @Override
    public String toString(){
        String s;
        if (numerateur!=0){
            s = this.entier + " " + this.numerateur + "/" + this.denominateur;
        } else{
            s = this.entier + "";
        }
        return s;
    }
    
    public String toStringEntier() {
        return this.entier + "";
    }
    
}
