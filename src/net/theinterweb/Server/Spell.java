package net.theinterweb.Server;

import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.util.ArrayList;

public class Spell {

    private String name;
    private int cost;
    private String type;
    private String rarity;
    private String set;

    private ArrayList<ArrayList<String>> effects = new ArrayList<>();
    private ArrayList<String> attributes = new ArrayList<>();

    private static JSonReader reader;

    private String GetExecutionPath(){
        String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
        absolutePath = absolutePath.replaceAll("%20"," "); // Surely need to do this here
        return absolutePath;
    }

    public Spell(String n) throws ParseException{
        reader = new JSonReader(new File(GetExecutionPath()).getParent() + "/all-cards.json");
        name = n;
        cost =  (int)(reader.getCost(n));
        rarity = (JSonReader.getRarity(n));
        set = JSonReader.getSet(n);
        type = reader.getType(n);
    }

    public JSONObject getEffect() {
        try {
            return new org.json.JSONObject(reader.getEffect(name).toJSONString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new org.json.JSONObject();
    }

    public String getName() {
        return this.name;
    }

    public int getCost() {
        return this.cost;
    }

    public void setCost(int c) {
        this.cost = c;
    }

    public String getType() {
        return this.type;
    }

    public String getRarity() {
        return rarity;
    }

    public String getSet() {
        return set;
    }
}
