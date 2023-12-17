package ExpDevices.service;

import java.awt.Font;

import javax.swing.JComponent;

public class SetFont {
    public static void setFont(Font font, JComponent... components) {
        for (JComponent jComponent : components) {
            jComponent.setFont(font);
        }
    }
}
