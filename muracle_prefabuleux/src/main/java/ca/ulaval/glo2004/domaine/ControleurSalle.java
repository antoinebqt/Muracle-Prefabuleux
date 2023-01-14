package ca.ulaval.glo2004.domaine;

import ca.ulaval.glo2004.domaine.Accessoire.Accessoire;
import ca.ulaval.glo2004.domaine.Accessoire.Fenetre;
import ca.ulaval.glo2004.domaine.Accessoire.Porte;
import ca.ulaval.glo2004.domaine.Accessoire.Prise;
import ca.ulaval.glo2004.domaine.Accessoire.RetourAir;
import ca.ulaval.glo2004.domaine.MesureImperial.Imperial;
import ca.ulaval.glo2004.domaine.MesureImperial.PointImperial;
import ca.ulaval.glo2004.domaine.dtos.AccessoireParamDTO;
import ca.ulaval.glo2004.domaine.dtos.ImperialPointDTO;
import ca.ulaval.glo2004.domaine.dtos.MurParamDTO;
import ca.ulaval.glo2004.gui.Enum.Orientation;
import ca.ulaval.glo2004.gui.Enum.Face;
import ca.ulaval.glo2004.gui.Enum.TypeAccessoire;
import ca.ulaval.glo2004.gui.Enum.ModeVue;
import ca.ulaval.glo2004.domaine.dtos.SalleParamDTO;
import ca.ulaval.glo2004.domaine.dtos.SeparateurParamDTO;
import ca.ulaval.glo2004.domaine.svg.SVGHelper;
import ca.ulaval.glo2004.gui.Enum.TypePolygone;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author anto-
 */
public class ControleurSalle {
    
    //Attributs
    private Salle salle;
    private Accessoire selectedAccessoire;
    private Separateur selectedSeparateur;
    private Mur selectedMur;
    private ModeVue modeVue;
    private Orientation currentCote;
    private File currentFile;
    private final ArrayList<ByteArrayOutputStream> listeVer;
    private int ptrVer;
    private int espacementGrille;
    
    //Constructeur
    public ControleurSalle () {
        salle = new Salle();
        modeVue = ModeVue.DESSUS;
        currentCote = Orientation.NORD;
        listeVer = new ArrayList<>();
        ptrVer = 0;
        espacementGrille = 12 * Imperial.ratioPixelPouce;
        
        //Pour tester le code et l'affichage
        Cote coteNord = salle.getCote(Orientation.NORD);
        Cote coteSud = salle.getCote(Orientation.SUD);

        coteNord.addSeparateur(new Imperial(30));
        coteNord.addSeparateur(new Imperial(80));
        coteNord.addSeparateur(new Imperial(150));
        
        Fenetre fenetre = new Fenetre(new PointImperial(160,20));
        Porte porte = new Porte(coteNord, new Imperial(90));
        Prise prise = new Prise(new PointImperial(15,80));
        RetourAir retourAir = new RetourAir(coteSud.getMurs().get(0));
        
        coteNord.addAccessoire(fenetre);
        coteNord.addAccessoire(porte);
        coteNord.addAccessoire(prise);
        coteSud.addAccessoire(retourAir);
        
        
        saveCurrentState();
        updateCurrentView();
    }
    
    public void createNouvelleSalle() {
        salle = new Salle();
        currentFile = null;
        resetListeModif();
        refreshCotes();
        refreshSalle();
        removeAllSelected();
    }
    
    //GETTERS
    public boolean isMurSelected(){
        return selectedMur != null;
    }
    public boolean isAccessoirSelected(){
        return selectedAccessoire != null;
    }
    public boolean isSeparateurSelected(){
        return selectedSeparateur != null;
    }
    private ArrayList<Polygone> getPolygonesPanneauMur(Mur mur, boolean exterieur){
        if(exterieur){
            return mur.getPanneauExterieur().getPolygones();
        } else {
            return mur.getPanneauInterieur().getPolygones();
        }
    }
    public ArrayList<Polygone> getPolygonesPanneauSelectedMur(boolean exterieur){
        return getPolygonesPanneauMur(selectedMur, exterieur);
    }
    private ArrayList<Polygone> getPolygonesSVGPanneauMur(Mur mur, boolean exterieur){
        if(exterieur){
            return mur.getPanneauExterieur().generateSVGPolygones();
        } else {
            return mur.getPanneauInterieur().generateSVGPolygones();
        }
    }
    public ArrayList<Polygone> getPolygonesSVGPanneauSelectedMur(boolean exterieur){
        return getPolygonesSVGPanneauMur(selectedMur, exterieur);
    }
    
    public ArrayList<Polygone> getPolygonesCote(Orientation orientation){
        return salle.getCote(orientation).getPolygonesMur();
    }
    
    public ArrayList<Polygone> getPolygonesAccessoires(Orientation orientation){
        return salle.getCote(orientation).getPolygonesAccessoires();
    }
    
    public ArrayList<Polygone> getPolygonesSalle(){
        return salle.getPolygones();
    }
    
    public int getLongueurCote(Orientation orientation) {
        return (int)getCote(orientation).getLongueur().getDoublePixelValue();
    }

    private Cote getCote(Orientation orientation) {
        return salle.getCote(orientation);
    }
    
    public double getEpaisseurMateriaux() {
        return salle.epaisseurMateriaux.getDoublePixelValue();
    }
    
    public SalleParamDTO getSalleParamDTO() {
        return new SalleParamDTO(
                salle.getLongueur().toString(),
                salle.getLargeur().toString(),
                salle.getHauteur().toString(),
                salle.getEpaisseurMur().toString(),
                salle.getMargePliage().toString(),
                salle.getMargeRepliEpaisseur().toString(),
                salle.getHauteurRetourAir().toString(),
                salle.getDistanceSolRetourAir().toString(),
                salle.getHauteurTrouRetourAir().toString(),
                salle.getLongueurPliSoudure().toString(),
                Math.toDegrees(salle.getAnglePliSoudure()) + ""
        );
    }
    
    public SeparateurParamDTO getSelectedSeparateurParamDTO() {
        if (selectedSeparateur != null) {
            return new SeparateurParamDTO(selectedSeparateur.getPosX().toString());
        }
        return null;
    }
    
     public AccessoireParamDTO getSelectedAccessoireParamDTO() {
        if (selectedAccessoire != null) {
            return new AccessoireParamDTO(
                    selectedAccessoire.getPosX().toString(),
                    selectedAccessoire.getPosY().toString(),
                    selectedAccessoire.getLongueur().toString(),
                    selectedAccessoire.getHauteur().toString(),
                    selectedAccessoire.getMargeMarche().toString()
            );
        }
        return null;
     }
     
     public MurParamDTO getSelectedMurParamDTO() {
        return new MurParamDTO(
                selectedMur.getPosX().toString(),
                selectedMur.getLongueur().toString(),
                selectedMur.getHauteur().toString(),
                String.valueOf(selectedMur.getPanneauInterieur().calculePoids()) + (selectedMur.getPanneauInterieur().getIsTropLourd()? "(!)" : ""),
                String.valueOf(selectedMur.getPanneauExterieur().calculePoids()) + (selectedMur.getPanneauExterieur().getIsTropLourd()? "(!)" : "")
        );
     }
    
    //SETTERS
    public void setEpaisseurMateriaux(double epaisseur) {
        salle.epaisseurMateriaux = (Imperial.getImperialFromPixelValue(epaisseur));
    }
    
    private PointImperial convertPointVueCote(Orientation orientation, Face face, Point coor){
        PointImperial impCoor = new PointImperial(coor);
        switch (face){
            case EXTERIEUR:
                Cote cote = salle.getCote(orientation);
                return new PointImperial(cote.getLongueur().substract(impCoor.getX()), impCoor.getY());
            case INTERIEUR:
            default:
                return impCoor;
        }
    }
    
    private Imperial convertVueDessusPointToVueCotePosX(Cote cote, PointImperial impCoor) {
        switch (cote.getOrientation()){
                case NORD:
                    //X - salle.epaisseurMur - epaisseurMat
                    return impCoor.getX().substract(salle.getEpaisseurMur()).substract(salle.getEpaisseurMateriaux());
                case EST:
                    //Y - salle.epaisseurMur - epaisseurMat
                    return impCoor.getY().substract(salle.getEpaisseurMur()).substract(salle.getEpaisseurMateriaux());
                case SUD:
                    //cote.longueur - (X - salle.epaisseurMur - salle.epaisseurMat)
                    return cote.getLongueur().substract(impCoor.getX().substract(salle.getEpaisseurMur()).substract(salle.getEpaisseurMateriaux()));
                case OUEST:
                    //cote.longueur - (Y - salle.epaisseurMur - salle.epaisseurMat)
                    return cote.getLongueur().substract(impCoor.getY().substract(salle.getEpaisseurMur()).substract(salle.getEpaisseurMateriaux()));
                default:
                    return null;
        }
    }
    
    //Méthodes de SELECTION
    public TypePolygone selectVueCote(Orientation orientation, Face face, Point coor) {
        removeAllSelected();
        PointImperial impCoor = convertPointVueCote(orientation, face, coor);
        if (selectAccessoireVueCote(orientation, impCoor)) {
            return TypePolygone.fromString(selectedAccessoire.getType().getValue());
        } else if (selectSeparateurVueCote(orientation, impCoor)) {
            return TypePolygone.SEPARATEUR;
        } else if (selectMurVueCote(orientation, impCoor)){
            return TypePolygone.MUR;
        }
        return null;
    }
    
    public TypePolygone selectVueDessus(Point coor) {
        removeAllSelected();
        PointImperial impCoor = new PointImperial(coor);
        Cote cote = salle.getCoteAt(impCoor);
        if (cote != null) {
            if (selectSeparateurVueDessus(cote, impCoor)) {
                return TypePolygone.SEPARATEUR;
            } else if (salle.getMurAt(impCoor) != null) {
                selectedMur = salle.getMurAt(impCoor);
                selectedMur.setSelected();
                return TypePolygone.MUR;
            }
        }
        return null;
    }
    
    public boolean selectAccessoireVueCote(Orientation orientation, PointImperial coor){
        removeAllSelected();
        selectedAccessoire = salle.getCote(orientation).getAccessoireAt(coor);
        if (selectedAccessoire != null) { selectedAccessoire.setSelected(); }
        return selectedAccessoire != null;
    }
        
    private boolean selectSeparateurVueCote(Orientation orientation, PointImperial coor){
        removeAllSelected();
        Cote cote = salle.getCote(orientation);
        selectedSeparateur = cote.getSeparateurAt(coor.getX());
        if (selectedSeparateur != null) {
            selectedSeparateur.setSelected();
            updateCurrentView();
            return true;
        }
        return false;
    }
    
    public Polygone getMurSelectedPolygone () {
        if (selectedMur != null) {
            return selectedMur.getPolygone();
        }
        return null;
    }
    
    public boolean selectMurVueCote(Orientation orientation, PointImperial coor){
        removeAllSelected();
        if (selectedMur != null) { selectedMur.setUnselected(); }
        selectedMur = salle.getCote(orientation).getMurAt(coor);
        if (selectedMur != null) { selectedMur.setSelected(); }
        return selectedMur != null;
    }

    private boolean selectSeparateurVueDessus(Cote cote, PointImperial impCoor){
        removeAllSelected();
        Imperial separateurPosX = convertVueDessusPointToVueCotePosX(cote, impCoor);
        if(separateurPosX != null) {
            selectedSeparateur = cote.getSeparateurAt(separateurPosX);
            if (selectedSeparateur != null) {
                selectedSeparateur.setSelected();
                updateCurrentView();
                return true;
            }
        }
        return false;
    }
    
    public void deplacerSelectionVueCote(Orientation orientation, Face face, Point coor){
        PointImperial impCoor = convertPointVueCote(orientation, face, coor);
        Cote cote = salle.getCote(orientation);
        if(cote != null && cote.containsVueCote(impCoor)){
            if(selectedAccessoire != null){
                Imperial distToCenterX = selectedAccessoire.getLongueur().divide(2);
                Imperial distToCenterY = selectedAccessoire.getHauteur().divide(2);
                PointImperial centeredCoor = impCoor.substract(distToCenterX, distToCenterY);
                selectedAccessoire.setCoor(centeredCoor);
                updateCurrentView();
            } else if(selectedSeparateur != null){
                selectedSeparateur.setCoor(impCoor);
                updateCurrentView();
            }
        }
    }
    
    public void deplacerSelectionVueDessus(Point coor){
        PointImperial impCoor = new PointImperial(coor);
        Cote cote = salle.getCoteAt(impCoor);
        if (cote != null && selectedSeparateur != null && cote.getOrientation() == selectedSeparateur.getOrientation()) {
            Imperial newPosX = convertVueDessusPointToVueCotePosX(cote, impCoor);
            if(newPosX != null && cote.containsVueCoteX(newPosX)) {
                selectedSeparateur.setPosX(newPosX);
                updateCurrentView();
            }
        }
    }
    
    //Méthodes d'AJOUT
    public void addAccessoireVueCote(Orientation orientation, Face face, Point coor, TypeAccessoire type){
        PointImperial impCoor = convertPointVueCote(orientation, face, coor);
        Cote cote = salle.getCote(orientation);
        if (cote.containsVueCote(impCoor)) {
            Accessoire accessoire;
            removeAllSelected();
            switch (type) {
                case RETOUR_AIR:
                    Mur mur = cote.getMurAt(impCoor);
                    accessoire = new RetourAir(mur);
                    break;
                case PORTE:
                    accessoire = new Porte(cote, impCoor.getX());
                    break;
                case FENETRE:
                    accessoire = new Fenetre(impCoor);
                    break;
                case PRISE:
                default:
                    accessoire = new Prise(impCoor);
                    break;
            }
            cote.addAccessoire(accessoire);
            removeAllSelected();
            selectedAccessoire = accessoire;
            selectedAccessoire.setSelected();
            saveCurrentState();
        }
    }
    
    public void addSeparateurVueCote(Orientation orientation, Face face, Point coor){
        removeAllSelected();
        PointImperial impCoor = convertPointVueCote(orientation, face, coor);
        Cote cote = salle.getCote(orientation);
        if (cote.containsVueCoteX(impCoor.getX())) {
            cote.addSeparateur(impCoor.getX());
            selectSeparateurVueCote(orientation, impCoor);
            saveCurrentState();
        }
    }

    public void addSeparateurVueDessus(Point coor) {
        PointImperial impCoor = new PointImperial(coor);
        selectedMur = null;
        selectedAccessoire = null;
        Cote cote = salle.getCoteAt(impCoor);
        if (cote != null) {
            Imperial newPosX = convertVueDessusPointToVueCotePosX(cote, impCoor);
            if(newPosX != null && cote.containsVueCoteX(newPosX)) {
                cote.addSeparateur(newPosX);
                selectSeparateurVueDessus(cote,impCoor);
                saveCurrentState();
            }
        }
        updateCurrentView();
    }
    
    //Méthodes d'UPDATE
    public void updateSelectedAccessoire(AccessoireParamDTO accessoireParam) throws Exception{
        selectedAccessoire.setPosX(Imperial.getImperialFromString(accessoireParam.positionX()));
        selectedAccessoire.setPosY(Imperial.getImperialFromString(accessoireParam.positionY()));
        selectedAccessoire.setLongueur(Imperial.getImperialFromString(accessoireParam.longueur()));
        selectedAccessoire.setHauteur(Imperial.getImperialFromString(accessoireParam.hauteur()));
        
        if(selectedAccessoire.getType() == TypeAccessoire.PORTE) {
            selectedAccessoire.setMargeMarche(Imperial.getImperialFromString(accessoireParam.margeMarche()));
            selectedAccessoire.adjustPosY();
        }
        saveCurrentState();
        updateCurrentView();
    }
    
    public void updateSalle(SalleParamDTO salleParamDTO) throws Exception {
        salle.setLongueur(Imperial.getImperialFromString(salleParamDTO.longueur()));
        salle.setLargeur(Imperial.getImperialFromString(salleParamDTO.largeur()));
        salle.setHauteur(Imperial.getImperialFromString(salleParamDTO.hauteur()));
        salle.setEpaisseurMur(Imperial.getImperialFromString(salleParamDTO.epaisseur()));
        salle.setMargeRepliEpaisseur(Imperial.getImperialFromString(salleParamDTO.margeRepli()));
        salle.setMargePliage(Imperial.getImperialFromString(salleParamDTO.margePliage()));
        salle.setHauteurRetourAir(Imperial.getImperialFromString(salleParamDTO.hauteurRetourAir()));
        salle.setDistanceSolRetourAir(Imperial.getImperialFromString(salleParamDTO.distanceSolRetourAir()));
        salle.setHauteurTrouRetourAir(Imperial.getImperialFromString(salleParamDTO.hauteurTrouRetourAir()));
        salle.setLargeurPliSoudure(Imperial.getImperialFromString(salleParamDTO.longueurPliSoudure()));
        salle.setAnglePliSoudure(Math.toRadians(Double.parseDouble(salleParamDTO.anglePliSoudure())));
        
        saveCurrentState();
        updateCurrentView();
    }

    
    public void updateSelectedSeparateur(SeparateurParamDTO separateurParam) throws Exception{
        selectedSeparateur.setPosX(Imperial.getImperialFromString(separateurParam.position()));
        saveCurrentState();
        updateCurrentView();
    }
    
    //Méthode de SUPPRESSION
    private void removeAllSelected() {
        if(selectedAccessoire != null) {
            selectedAccessoire.setUnselected();
            selectedAccessoire = null;
        }
        
        if(selectedMur != null) {
            selectedMur.setUnselected();
            selectedMur = null;
        }
        
        if(selectedSeparateur != null) {
            selectedSeparateur.setUnselected();
            selectedSeparateur = null;
        }
        
        salle.removeAllSelected();
    }

    
    public void refreshSalle() {
        removeAllSelected();
        updateCurrentView();
    }
    
    public void refreshCotes() {
        removeAllSelected();
        salle.refreshCotes();
    }
    
    
    public void removeSelectedAccessoire(){
        if(selectedAccessoire != null){
            selectedAccessoire.getCote().removeAccessoire(selectedAccessoire);
            saveCurrentState();
        }
        else{
            System.out.println("Il faut avoir selectionner un accessoire pour pouvoir le supprimer.");
        }
    }
    
    public void removeSelectedSeparateur(){
        if(selectedSeparateur != null){
            selectedSeparateur.getCote().removeSeparateur(selectedSeparateur);
            saveCurrentState();
        }
        else{
            System.out.println("Il faut avoir selectionner un separateur pour pouvoir le supprimer.");
        }
    }

    public TypePolygone getSelectedType() {
        if (selectedSeparateur != null) return TypePolygone.SEPARATEUR;
        if (selectedAccessoire != null) return TypePolygone.fromString(selectedAccessoire.getType().getValue());
        if (selectedMur != null) return TypePolygone.MUR;
        return null;
    }

    public double getEpaisseur() {
        return salle.getEpaisseurMur().getDoublePixelValue();
    }


    public ImperialPointDTO convertPointToImperialPointDTO(Point point, Face face, Orientation orientation) {
        PointImperial pointImperial = convertPointVueCote(orientation, face, point);
        return new ImperialPointDTO(
                pointImperial.getX().toStringEntier(),
                pointImperial.getY().toStringEntier()
        );
    }
    
    // Met a jour les polygones et la disposition selon le mode de vue.
    // Idealement serait une classe a part appele par le controleur.
    public void setModeVue(ModeVue modeVue) {
        this.modeVue = modeVue;
    }
    
     public void setCurrentCote(Orientation orientation) {
        this.currentCote = orientation;
    }
    
    public void updateCurrentView() {
        switch (this.modeVue) {
            case DESSUS:
                salle.updateCotes();
                salle.calculeDisposition();
                break;
            case COTE:
                Cote cote = salle.getCote(currentCote);
                cote.updateCote();
                cote.calculeDisposition();
                break;
            case PLAN_DECOUPE_INTERIEUR:
                if(selectedMur != null){
                    Panneau interieur = selectedMur.getPanneauInterieur();
                    interieur.calculeDisposition();
                }
                break;
            case PLAN_DECOUPE_EXTERIEUR:
                if(selectedMur != null){
                    Panneau exterieur = selectedMur.getPanneauExterieur();
                    exterieur.calculeDisposition();
                }
                break;
            default:
                throw new AssertionError();
        }
    }

    public void enregistrerFichier(File selectedFile) {
        try {
            saveCurrentPath(selectedFile);
            if(!currentFile.createNewFile()){
                currentFile.delete();
                currentFile.createNewFile();
            }
            writeSalleToFile(currentFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void enregistrerFichier() {
        enregistrerFichier(currentFile);
    }

    private void writeSalleToFile(File file) {
        ObjectOutputStream objectOutputStream = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(salle);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean canAutoSave() {
        return currentFile != null;
    }
    
    public void loadSalleFromFile(File file) {
        FileInputStream fileInputStream = null;
        try {
            saveCurrentPath(file);
            fileInputStream = new FileInputStream(file.getAbsolutePath());
            ObjectInputStream objectInputStream = null;
            objectInputStream = new ObjectInputStream(fileInputStream);
            salle = (Salle) objectInputStream.readObject();
            resetListeModif();
            refreshCotes();
            refreshSalle();
            removeAllSelected();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveCurrentPath(File selectedFile) {
        currentFile = new File(selectedFile.getAbsolutePath());
        if (currentFile.getAbsolutePath().contains(".mpf")) {
            currentFile = new File(currentFile.getAbsolutePath());
        } else {
            currentFile = new File(currentFile.getAbsolutePath() + ".mpf");
        }
    }

    public void undo() {
        ptrVer--;
        applySavedModification(listeVer.get(ptrVer));
        refreshCotes();
        refreshSalle();
    }

    public void redo() {
        ptrVer++;
        applySavedModification(listeVer.get(ptrVer));
        refreshCotes();
        refreshSalle();
    }
    
   private void applySavedModification(ByteArrayOutputStream baos){
        
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            salle = (Salle) ois.readObject();
            ois.close();
            removeAllSelected();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
   }

    public void saveCurrentState(){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject( salle );
            oos.close(); 
            
            if(ptrVer != listeVer.size()-1 && listeVer.size() != 0) {
                int listeModifSize = listeVer.size();
                for(int i = ptrVer+1; i < listeModifSize; i++) {
                    listeVer.remove(ptrVer+1);
                }
            }
            
            listeVer.add(baos);
            ptrVer = listeVer.size()-1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean canUndo() {
        return listeVer.size() != 0 && ptrVer > 0;
    }

    public boolean canRedo() {
        return listeVer.size() != 0 && ptrVer != listeVer.size()-1;
    }

    public void removeLastModif() {
        if (listeVer.size() != 0) {
            listeVer.remove(ptrVer);
            ptrVer--;
        }
    }

    private void resetListeModif() {
        listeVer.clear();
        ptrVer = 0;
        saveCurrentState();
    }
    
    
    public String getMessageErreurVueCote(){
        String erreur = "";
        int ext;
        Integer x;
        HashMap<TypeAccessoire, Integer> qteAccessoiresInvalides = new HashMap<>();
        int count = 0;
        for(Accessoire accessoire : salle.getCote(currentCote).getAccessoires()){
            if (!accessoire.isValid()){
                count++;
                x = qteAccessoiresInvalides.get(accessoire.getType());
                if(x==null){
                    qteAccessoiresInvalides.put(accessoire.getType(), 1);
                } else {
                    
                    qteAccessoiresInvalides.put(accessoire.getType(), x+1);
                }
            }
        }
        if (count>0){
            erreur+=count==1? ("Il y a " + count + " accessoire invalide sur ce côté :\n") : ("Il y a " + count + " accessoires invalides sur ce côté :\n");
            for(TypeAccessoire typeAccessoire : qteAccessoiresInvalides.keySet()){
                switch (typeAccessoire) {
                    case RETOUR_AIR:
                        erreur+="       •" + qteAccessoiresInvalides.get(TypeAccessoire.RETOUR_AIR) + " retour(s) d'air\n";
                    break;
                    case PORTE:
                        erreur+="       •" + qteAccessoiresInvalides.get(TypeAccessoire.PORTE) + " porte(s)\n";
                    break;
                    case FENETRE:
                        erreur+="       •" + qteAccessoiresInvalides.get(TypeAccessoire.FENETRE) + " fenêtre(s)\n";
                    break;
                    case PRISE:
                        erreur+="       •" + qteAccessoiresInvalides.get(TypeAccessoire.PRISE) + " prise(s)\n";
                    break;
                }
            }
        }
        count = 0;
        ext=0;
        for(Mur mur : salle.getCote(currentCote).getMurs()){
            if (mur.getPanneauInterieur().getIsTropLourd()){
                count++;
                
            }
            if (mur.getPanneauExterieur().getIsTropLourd()){
                count++;
                ext++;
            }
        }
        if (count>0)
            erreur+=count==1? ("Il y a " + count + " panneau de plus de 250 livres sur ce côté :\n") : ("Il y a " + count + " panneaux de plus de 250 livres sur ce côté :\n");
        if (count-ext>0){
            erreur+="       •" + (count-ext) + " panneau(x) intérieur(s)\n";
        }
        if (ext>0){
            erreur+="       •" + (ext) + " panneau(x) extérieur(s)\n";
        }
        
        return erreur;
    }
    
    public String getMessageErreurVueDessus(){
        String erreur = "";
        int ext;
        Integer x;
        HashMap<TypeAccessoire, Integer> qteAccessoiresInvalides = new HashMap<>();
        int count = 0;
        for(Accessoire accessoire : salle.getAllAccessoires()){
            if (!accessoire.isValid()){
                count++;
                x = qteAccessoiresInvalides.get(accessoire.getType());
                if(x==null){
                    qteAccessoiresInvalides.put(accessoire.getType(), 1);
                } else {
                    
                    qteAccessoiresInvalides.put(accessoire.getType(), x+1);
                }
            }
        }
        if (count>0){
            erreur+=count==1? ("Il y a " + count + " accessoire invalide dans cette salle :\n") : ("Il y a " + count + " accessoires invalides dans cette salle :\n");
            for(TypeAccessoire typeAccessoire : qteAccessoiresInvalides.keySet()){
                switch (typeAccessoire) {
                    case RETOUR_AIR:
                        erreur+="       •" + qteAccessoiresInvalides.get(TypeAccessoire.RETOUR_AIR) + " retour(s) d'air\n";
                    break;
                    case PORTE:
                        erreur+="       •" + qteAccessoiresInvalides.get(TypeAccessoire.PORTE) + " porte(s)\n";
                    break;
                    case FENETRE:
                        erreur+="       •" + qteAccessoiresInvalides.get(TypeAccessoire.FENETRE) + " fenêtre(s)\n";
                    break;
                    case PRISE:
                        erreur+="       •" + qteAccessoiresInvalides.get(TypeAccessoire.PRISE) + " prise(s)\n";
                    break;
                }
            }
        }
        /*count = 0;
        ext=0;
        for(Mur mur : salle.getAllMur()){
            if (mur.getPanneauInterieur().getIsTropLourd()){
                count++;
                ext++;
            }
            if (mur.getPanneauExterieur().getIsTropLourd()){
                count++;
            }
        }
        if (count>0)
            erreur+=count==1? ("Il y a " + count + " panneau de plus de 250 livres dans cette salle :\n") : ("Il y a " + count + " panneaux de plus de 250 livres dans cette salle :\n");
        if (count-ext>0){
            erreur+="       •" + (count-ext) + " panneau(x) intérieur(s)\n";
        }
        if (ext>0){
            erreur+="       •" + (ext) + " panneau(x) extérieur(s)\n";
        }
        */
        return erreur;
    }
    
    private String generatePanneauSVGMur(Mur mur, boolean exterieur) {
        SVGHelper helper = new SVGHelper();
        ArrayList<Polygone> polygones = getPolygonesSVGPanneauMur(mur, exterieur);
        for(Polygone polygone : polygones){
            helper.addPolygon(polygone);
        }
        return helper.toSVG();
    }
    public String generatePanneauSVGSelectedMur(boolean exterieur) throws Exception{
        if(!isMurSelected())
            throw new Exception("Generer un svg necessite d'avoir un mur selectionne.");
        return generatePanneauSVGMur(selectedMur, exterieur);
    }
    
    private void savePlanMur(Mur mur, String folderPath) throws IOException{
        Cote cote = mur.getCote();
        int murIndex = cote.getMurs().indexOf(mur);
        Path pathExterieur = Paths.get(folderPath, cote.getOrientation().toString() + murIndex + "_Exterieur.svg");
        String exterieur = generatePanneauSVGMur(mur, true);
        Files.writeString(pathExterieur, exterieur);
        Path pathInterieur = Paths.get(folderPath, cote.getOrientation().toString() + murIndex + "_Interieur.svg");
        String interieur = generatePanneauSVGMur(mur, false);
        Files.writeString(pathInterieur, interieur);
    }
    public void savePlansSelectedMur(String folderPath) throws IOException{
        savePlanMur(selectedMur, folderPath);
    }
    public void savePlansSalle(String folderPath) throws IOException{
        for(Mur mur : salle.getAllMur()){
            savePlanMur(mur, folderPath);
        }
    }
    
    private Imperial getAirPanneauMur(Mur mur, boolean exterieur) {
        if(exterieur){
            return mur.getPanneauExterieur().getAirePanneau();
        } else {
            return mur.getPanneauInterieur().getAirePanneau();
        }
    }
    
    public void setEspacementGrille(String newEspacement) throws Exception {
        espacementGrille = (int)(Imperial.getImperialFromString(newEspacement).getDoublePixelValue());
    }
    
    public int getEspacementGrille() {
        return espacementGrille;
    }
    
}
