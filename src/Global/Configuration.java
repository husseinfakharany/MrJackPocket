package Global;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;

public class Configuration {

    public static Image chargeImage(String nom) {
        Image img = null;
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream( "PNG/" + nom + ".png" );
        try {
            // Chargement d'une image
            img = ImageIO.read (in);
        } catch ( Exception e ) {
            System.err.println("Pb image : " + nom);
            System .exit(1);
        }
        return img;
    }

}
