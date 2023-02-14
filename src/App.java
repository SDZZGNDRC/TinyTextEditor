import java.awt.Color;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import com.formdev.flatlaf.intellijthemes.*;

public class App {
    public static void main(String[] args) throws Exception {
        // UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        // defaults.put("TextPane.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        // try {
        //     UIManager.setLookAndFeel( new FlatDarkFlatIJTheme() );
        // } catch( Exception ex ) {
        //     System.err.println( "Failed to initialize LaF" );
        // }
        new Editor();
    }
}
