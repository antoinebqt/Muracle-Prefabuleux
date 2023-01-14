package ca.ulaval.glo2004.gui;


import ca.ulaval.glo2004.gui.Enum.ModeVue;
import ca.ulaval.glo2004.domaine.ControleurSalle;
import ca.ulaval.glo2004.domaine.dessin.Afficheur;
import ca.ulaval.glo2004.domaine.dessin.AfficheurElevationCote;
import ca.ulaval.glo2004.domaine.dessin.AfficheurPlanSalle;
import ca.ulaval.glo2004.domaine.dessin.AfficheurProfilDecoupagePanneau;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Antoine
 */
public class Canvas extends JPanel implements Serializable {
    

    public Dimension initialDimension;
    private MainWindow mainWindow;
    private ControleurSalle controleur;
    private ElementCouleur elementCouleur;
    
    private static final double pixelParPouce = 1; 
    private double facteurZoom;
    private double ancienFacteurZoom;
    private boolean changementZoom = false;
    
    private Point debutDeplacement;
    private double decalageX = 100;
    private double decalageY = 100;
    
    public Canvas(){
        
    }
    
    public Canvas(MainWindow mainWindow) {
        setMainWindow(mainWindow);
        setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED));
        int width = (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().width*0.85);
        setPreferredSize(new Dimension(width,1));
        setVisible(true);
        int height = (int)(width*0.5);
        initialDimension = new Dimension(width,height);
        controleur = mainWindow.getSalleControleur();
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        if (mainWindow != null){
            super.paintComponent(g); 
            
            Graphics2D g2 = (Graphics2D) g;
            
            g2 = updateGraphics(g2);
            
            elementCouleur = mainWindow.getElementCouleur();
            
            Afficheur afficheur = createAfficheur();
            
            afficheur.draw(g2);
            refreshBoutonsUndoRedo();
        }
    }
   
    public MainWindow getMainWindow(){
        return mainWindow;
    }
    
    public void setMainWindow(MainWindow mainWindow){
        this.mainWindow = mainWindow;
    }
    
    public Dimension getInitialDimension(){
        return initialDimension;
    }

    private Graphics2D updateGraphics(Graphics2D g2) {
        if(changementZoom) {
                
            Point relativeMouseLocation = this.getMouseRelativePosition(); 
            
            double zoom = facteurZoom / ancienFacteurZoom;
            
            decalageX = (zoom) * (decalageX) + (1 - zoom) * relativeMouseLocation.getX();
            decalageY = (zoom) * (decalageY) + (1 - zoom) * relativeMouseLocation.getY();
            
            this.ancienFacteurZoom = this.facteurZoom;
            
            changementZoom = false;
        }
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(decalageX, decalageY);
        affineTransform.scale(facteurZoom * pixelParPouce, facteurZoom * pixelParPouce);
        g2.transform(affineTransform);
  
        return g2;
    }

    private Afficheur createAfficheur() {
        
        ModeVue vue = mainWindow.getVue();
        
        if (vue == ModeVue.DESSUS) {
            return new AfficheurPlanSalle(controleur, elementCouleur, initialDimension);
        } else if (vue == ModeVue.COTE) {
            return new AfficheurElevationCote(controleur, elementCouleur, initialDimension,mainWindow.getOrientation(),mainWindow.getFace());
        /*} else if (vue == ModeVue.MUR) {
            return new AfficheurElevationMur(controleur, elementCouleur, initialDimension,mainWindow.getOrientation(),mainWindow.getFace());*/
        } else {
            return new AfficheurProfilDecoupagePanneau(controleur, elementCouleur, initialDimension,mainWindow.getOrientation(),mainWindow.getFace());
        }
    }

    public void resetDecalage() {
        decalageX = 100;
        decalageY = 100;
    }
    
    public double getFacteurZoom(){
        return facteurZoom;
    }
    public void setFacteurZoom(double facteurZoom){
        this.facteurZoom = facteurZoom;
        updateZoom();
    }
    
    public void setAncienFacteurZoom(double facteurZoom) {
        this.ancienFacteurZoom = facteurZoom;
        updateZoom();
    }
    
    public void updateZoom(){
        this.changementZoom = true;
        repaint();
    }
    
    public void deplacerVue(Point destination) {
        if(debutDeplacement != null) {
            decalageX += destination.x - debutDeplacement.x;
            decalageY += destination.y - debutDeplacement.y;
        }
        debutDeplacement = destination;
        repaint();
    }
    public void commencerDeplacement() {
        debutDeplacement = getMouseRelativePosition();
    }
    
    public Point getMouseRelativePosition(){
        Point mouseLocation =  MouseInfo.getPointerInfo().getLocation();
        Point canvasLocation = getLocationOnScreen();
        double x = mouseLocation.getX() - canvasLocation.getX();
        double y = mouseLocation.getY() - canvasLocation.getY();
        Point relativePoint = new Point();
        relativePoint.setLocation(x, y);
        return relativePoint;
    }
    public Point getUnzoomedPoint(Point point){
        long x = Math.round((point.getX() - decalageX) / (facteurZoom));
        long y = Math.round((point.getY() - decalageY) / (facteurZoom));
        return  new Point((int)x, (int)y);
    }

    private void refreshBoutonsUndoRedo() {
        mainWindow.getBoutonUndo().setEnabled(controleur.canUndo());
        mainWindow.getBoutonRedo().setEnabled(controleur.canRedo());
    }
}