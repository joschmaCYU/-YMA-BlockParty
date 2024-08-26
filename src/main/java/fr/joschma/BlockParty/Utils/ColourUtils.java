package fr.joschma.BlockParty.Utils;

import fr.joschma.BlockParty.Arena.Arena;
import org.bukkit.Color;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class ColourUtils {

    public String getBlockColourName(Material ma) {
        String blockName = ma.name();
        String colour = "";
        if (blockName.contains("_")) {
            colour = blockName.split("_")[0];
            if (colour.equals("LIGHT") || colour.equals("DARK"))
                colour = blockName.split("_")[1];
        }
        return colour;
    }

    public boolean isAColour(String colour, Arena a) {
        List<String> colours = new ArrayList<>();
        colours.addAll(a.getPl().getColourName());
        if (colours.contains(colour.toLowerCase()))
            return true;

        return false;
    }

    public Color getBlockColour(Arena a, Material ma) {
        String colour = a.getPl().getColourUtils().getBlockColourName(ma).toLowerCase();
        if (colour.equals("red")) {
            return Color.RED;
        } else if (colour.equals("gold")) {
            return Color.fromRGB(255, 215, 0);
        } else if (colour.equals("yellow")) {
            return Color.YELLOW;
        } else if (colour.equals("green")) {
            return Color.GREEN;
        } else if (colour.equals("aqua")) {
            return Color.AQUA;
        } else if (colour.equals("blue")) {
            return Color.BLUE;
        } else if (colour.equals("purple")) {
            return Color.PURPLE;
        } else if (colour.equals("white")) {
            return Color.WHITE;
        } else if (colour.equals("gray")) {
            return Color.GRAY;
        } else if (colour.equals("black")) {
            return Color.BLACK;
        } else {
            return a.getDefaultParticleColour();
        }
    }
}
