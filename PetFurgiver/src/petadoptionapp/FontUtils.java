package petadoptionapp;
import java.awt.*;
import java.io.InputStream;

// Abstraction - provides simple interface for complex font operations
public class FontUtils {
    
    // Encapsulation - public method controls access to font loading logic
    // Abstraction - hides complex font loading details from caller
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
    
    // Abstraction - simplifies Arial font creation to single method call
    // Encapsulation - public method provides controlled access to font creation
    public static Font Arial(float size) {
        Font font = loadCustomFont("Arial Nova Cond.ttf", size, Font.PLAIN);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);
        return font;
    }
}