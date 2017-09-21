package net.theinterweb.Server;

import java.io.File;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

public class Card {
	
	private String name;
	private int cost;
	private int attack;
	private int health;
	private String type;
	private String rarity;
	private String set;
	private boolean canAttack;
	private boolean hasTaunt;
	private boolean hasDivineShield;
	private boolean hasWindfury;
	private boolean containsAura;

	private String race;
	
	private ArrayList<ArrayList<String>> effects = new ArrayList<>();
	private ArrayList<String> attributes = new ArrayList<>();
	private ArrayList<Object> attribute_values = new ArrayList<>();

	private ArrayList<String> triggers = new ArrayList<>();
	private JSONObject trigger_object;

	private static JSonReader reader;
	
	private String GetExecutionPath(){
	    String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    	absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
	    absolutePath = absolutePath.replaceAll("%20"," "); // Surely need to do this here
	    return absolutePath;
	}
	
	public Card(String n) throws ParseException{
		reader = new JSonReader(new File(GetExecutionPath()).getParent() + "/all-cards.json");
		name = n;
		cost =  (int)(reader.getCost(n));
		attack = (int)(reader.getAttack(n));
		health = (int)(reader.getHealth(n));
		setRarity(JSonReader.getRarity(n));
		set = JSonReader.getSet(n);
		canAttack = false;
		//effects = reader.getEffects(n);
		attributes = reader.getAttributes(n);
		type = reader.getType(n);
		attribute_values = reader.getAttributeValues(n, type);
		triggers = reader.getTriggers(n, type);
		trigger_object = reader.getTriggerObject(n, type);
		hasTaunt = attributes.contains("TAUNT");
		hasDivineShield = attributes.contains("DIVINE_SHIELD");
		hasWindfury = attributes.contains("WINDFURY");
		containsAura = reader.hasAura(n);
		race = reader.getRace(n, type);
	}


	public Card(String n, String t) throws ParseException{
		reader = new JSonReader(new File(GetExecutionPath()).getParent() + "/all-cards.json");
		name = n;
		cost =  (int)(reader.getCost(n, t));
		attack = (int)(reader.getAttack(n, t));
		health = (int)(reader.getHealth(n, t));
		setRarity(JSonReader.getRarity(n, t));
		set = JSonReader.getSet(n, t);
		canAttack = false;
		//effects = reader.getEffects(n);
		attributes = reader.getAttributes(n, t);
		attribute_values = reader.getAttributeValues(n, t);
		triggers = reader.getTriggers(n, t);
		trigger_object = reader.getTriggerObject(n, t);
		type = t;
		hasTaunt = attributes.contains("TAUNT");
		hasDivineShield = attributes.contains("DIVINE_SHIELD");
		hasWindfury = attributes.contains("WINDFURY");
		containsAura = reader.hasAura(n, t);
		race = reader.getRace(n, t);
	}
	
	public Card(String n, int c, int a, int h, String t){
		name = n;
		cost = c;
		attack = a;
		health = h;
		type = t;
	}
	
	public String getName(){
		return name;
	}
	
	public void setCost(int nc){
		cost = nc;
	}
	
	public int getCost(){
		return cost;
	}
	
	public void setAttack(int na){
		attack = na;
	}
	
	public int getAttack(){
		return attack;
	}
	
	public void setHealth(int nh){
		health = nh;
	}
	
	public int getHealth(){
		return health;
	}
	
	public String getType(){
		return type;
	}
	
	public boolean canAttack() {
		return canAttack;
	}

	public void setCanAttack(boolean cA) {
		canAttack = cA;
	}
	
	public ArrayList<ArrayList<String>> getEffects(){
		return effects;
	}
	
	public org.json.JSONObject getBattleCry() {
		try {
			return new org.json.JSONObject(reader.getBattlecry(name).toJSONString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new org.json.JSONObject();
	}

	public org.json.JSONObject getAura() {
		try {
			return new org.json.JSONObject(reader.getAura(name).toJSONString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new org.json.JSONObject();
	}

	public String getDeathRattle(){
		for (int i = 0; i < this.getEffects().size(); i++){
			if (this.getEffects().get(i).get(0).equals("deathrattle")){
				return this.getEffects().get(i).get(1).toLowerCase();
			}
		}
		return "";
	}
	
	public String toString(){
		return this.getName();
		//return "Name: " + this.getName() + ", Type: " + this.getType() + 
		//", Attack: " + this.getAttack() + ", Health: " + this.getHealth() +
		//", Cost: " + this.getCost();
	}

	public boolean hasTaunt() {
		return hasTaunt;
	}

	public boolean hasAura() {
		return this.containsAura;
	}
	
	public void setTaunt(boolean t){
		hasTaunt = t;
	}

	public void setDivineShield (boolean ds) { this.hasDivineShield = ds; }

	public boolean getDivineShield () { return this.hasDivineShield; }

	public boolean getWindfury () { return this.hasWindfury; }

	public String getOverload() {
		for (int i = 0; i < this.getEffects().size(); i++){
			if (this.getEffects().get(i).get(0).equals("overload")){
				return this.getEffects().get(i).get(1).toLowerCase();
			}
		}
		return "";
	}
	
	public boolean hasCharge() {
		return this.attributes.contains("CHARGE");
	}

	public String getSet() {
		return set;
	}

	public String getRarity() {
		return rarity;
	}

	public void setRarity(String rarity) {
		this.rarity = rarity;
	}

	public int getSpellDamage() {
		if (this.attributes.contains("SPELL_DAMAGE")) {
			return (int)(((long)(this.attribute_values.get(attributes.indexOf("SPELL_DAMAGE")))));
		}
		return 0;
	}

	public ArrayList<String> getTriggers() { return triggers; }

	public JSONObject getTriggerObject() { return trigger_object; }

	public String getRace () {
		return this.race;
	}
}