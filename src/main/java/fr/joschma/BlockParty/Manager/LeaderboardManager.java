package fr.joschma.BlockParty.Manager;

import org.bukkit.Bukkit;

import java.util.*;

public class LeaderboardManager {

    Map<String, Integer> gameMap = new HashMap<String, Integer>();
    Map<String, Integer> winMap = new HashMap<String, Integer>();
    Map<String, Integer> looseMap = new HashMap<String, Integer>();

    public int getNumber(String pName, Map<String, Integer> map) {
        return map.get(pName);
    }

    public int getNumber(int place, Map<String, Integer> map) {
        if (map.keySet().size() <= place) {
            return -1;
        }
        map = sortByValue(map);
        return (int) map.values().toArray()[place];
    }

    public String getName(int place, Map<String, Integer> map) {
        if (map.keySet().size() <= place) {
            return "No value at place: " + String.valueOf(place);
        }

        map = sortByValue(map);
        return (String) map.keySet().toArray()[place];
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public Map<String, Integer> getLooseMap() {
        return looseMap;
    }

    public Map<String, Integer> getGameMap() {
        return gameMap;
    }

    public Map<String, Integer> getWinMap() {
        return winMap;
    }
}
