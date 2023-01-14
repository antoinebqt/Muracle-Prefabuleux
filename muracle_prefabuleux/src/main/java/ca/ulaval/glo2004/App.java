package ca.ulaval.glo2004;

import ca.ulaval.glo2004.gui.MainWindow;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

public class App {
    
    public static void main(String[] args) {
         
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	final int screen_Width = dim.width;
	final int screen_Height = dim.height;
        mainWindow.setSize(screen_Width , screen_Height);
        mainWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}

