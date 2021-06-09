package Vue;

import Global.Configuration;

import javax.swing.*;
import java.awt.*;

public class BackgroundBox extends Box {

    Graphics2D drawable;
    Image background;
    /**
     * Creates a <code>Box</code> that displays its components
     * along the specified axis.
     *
     * @param axis can be {@link BoxLayout#X_AXIS},
     *             {@link BoxLayout#Y_AXIS},
     *             {@link BoxLayout#LINE_AXIS} or
     *             {@link BoxLayout#PAGE_AXIS}.
     * @throws AWTError if the <code>axis</code> is invalid
     * @see #createHorizontalBox
     * @see #createVerticalBox
     */
    public BackgroundBox(int axis, Image background) {
        super(axis);
        this.background =background;
    }

    public static Box createVerticalBackgroundBox(){
        Image enqueteur = Configuration.chargeImage("fond");
        Box lineBox = new BackgroundBox(BoxLayout.PAGE_AXIS,enqueteur);
        return lineBox;
    }

    //Pre-Condition : Vide
    //Post-Condition : Renvoie une box avec l'image res/fond.png en fond
    @Override
    public void paintComponent(Graphics g) {
        drawable = (Graphics2D) g;

        drawable.drawImage(background, 0, 0, getWidth(), getHeight(), null);
    }
}
