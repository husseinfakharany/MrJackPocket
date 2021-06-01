package Global;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.PrintWriter;


public class Configuration {

    private static Configuration instance = null;

    public static Image chargeImage(String nom) {
        Image img = null;
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream( nom+".png" );
        try {
            // Chargement d'une image
            img = ImageIO.read (in);
        } catch ( Exception e ) {
            System.err.println("Pb image : " + nom);
            System .exit(1);
        }
        return img;
    }

    public static Configuration instance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public Logger logger() {
        Logger log = Logger.getLogger("MrJackPocket.Logger");
        log.setLevel(Level.INFO);
        return log;
    }
}
