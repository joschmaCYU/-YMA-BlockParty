package fr.joschma.BlockParty.Manager;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class TextToAddToCommandManager {

    HashMap<Player, String> textToAddToCommand = new HashMap<Player, String>();

    public HashMap<Player, String> getTextToAddToCommand() {
        return textToAddToCommand;
    }
}
