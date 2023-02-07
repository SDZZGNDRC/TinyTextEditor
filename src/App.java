import javax.swing.*;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel( new FlatLightFlatIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        new Editor();
    }
}
