import java.awt.Color;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;

public class App {
    public static void main(String[] args) throws Exception {
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.put("TextPane.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        try {
            UIManager.setLookAndFeel( new FlatLightFlatIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        new Editor();
    }
}
