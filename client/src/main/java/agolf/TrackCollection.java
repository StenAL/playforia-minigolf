package agolf;

import java.util.HashMap;
import java.util.Map;

public class TrackCollection {

    private Map<String, String> tracks = new HashMap<>();

    public void addTrack(String author, String name, String data) {
        String key = author + "\t" + name;
        this.tracks.put(key, data);
    }

    public String getTrack(String author, String name) {
        System.out.println("--------------- GET TRACK");
        String key = author + "\t" + name;
        return this.tracks.get(key);
    }
}
