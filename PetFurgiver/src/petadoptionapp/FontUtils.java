package petadoptionapp;

import java.awt.*;
import java.io.InputStream;

public class FontUtils {

    public static Font loadCustomFont(String fontFileName, float size, int style) {
        try {
            InputStream is = FontUtils.class.getResourceAsStream("/" + fontFileName);
            if (is == null) {
                System.err.println("Font file not found: " + fontFileName);
                return new Font("SansSerif", style, (int) size);
            }
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is);
            return baseFont.deriveFont(style, size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", style, (int) size);
        }
    }

    public static Font Arial(float size) {
        Font font = loadCustomFont("Arial Nova Cond.ttf", size, Font.PLAIN);
        // Register the font with the graphics environment
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
        return font;
    }
}
