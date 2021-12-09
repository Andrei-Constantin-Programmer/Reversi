import java.awt.*;

/**
 * A color option contains a set of two colors and color names.
 */
public class Theme
{
    private String themeName;
    private Color color1, color2;
    private String name1, name2;

    /**
     * Create a color option object. The name will be automatically generated.
     * @param color1 The first color
     * @param name1 The name of the first color
     * @param color2 The second color
     * @param name2 The name of the second color
     */
    public Theme(Color color1, String name1, Color color2, String name2)
    {
        this.color1 = color1;
        this.name1 = name1;
        this.color2 = color2;
        this.name2 = name2;
        this.themeName = name1 + "-" + name2;
    }

    /**
     * Create a color option object with a given name.
     * @param themeName The name of the theme
     * @param color1 The first color
     * @param name1 The name of the first color
     * @param color2 The second color
     * @param name2 The name of the second color
     */
    public Theme(String themeName, Color color1, String name1, Color color2, String name2)
    {
        this(color1, name1, color2, name2);
        this.themeName = themeName;
    }

    /**
     * Get color 1.
     * @return Color 1
     */
    public Color getColor1()
    {
        return color1;
    }

    /**
     * Get color 2.
     * @return Color 2
     */
    public Color getColor2()
    {
        return color2;
    }

    /**
     * Get the name of color 1.
     * @return The name of color 1
     */
    public String getName1()
    {
        return name1;
    }

    /**
     * Get the name of color 2.
     * @return The name of color 2
     */
    public String getName2()
    {
        return name2;
    }

    /**
     * Get the name of the theme.
     * @return The theme name
     */
    public String getThemeName()
    {
        return themeName;
    }

    @Override
    public String toString()
    {
        return themeName + " ("+name1 + " - " + name2+")";
    }
}