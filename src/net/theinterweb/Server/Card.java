package net.theinterweb.Server;

import java.io.File;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

public class Card {
	
	private String name;
	private int cost;
	private int attack;
	private int health;
	private String type;
	private boolean canAttack;
	private boolean hasTaunt;
	
	private ArrayList<ArrayList<String>> effects = new ArrayList<ArrayList<String>>();
	
	private JSonReader reader;
	
	private String GetExecutionPath(){
	    String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    	absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
	    absolutePath = absolutePath.replaceAll("%20"," "); // Surely need to do this here
	    return absolutePath;
	}
	
	public Card(String n) throws ParseException{
		reader = new JSonReader(new File(GetExecutionPath()).getParent() + "/all-cards.json");
		name = n;
		cost = Math.toIntExact(reader.getCost(n));
		attack = Math.toIntExact(reader.getAttack(n));
		health = Math.toIntExact(reader.getHealth(n));
		canAttack = false;
		effects = reader.getEffects(n);
		type = reader.getType(n);
		if(reader.getEffects(n).contains("Taunt")){
			hasTaunt = true;
		}
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
	
	public String getBattleCry(){
		for (int i = 0; i < this.getEffects().size(); i++){
			if (this.getEffects().get(i).get(0).equals("battlecry")){
				return this.getEffects().get(i).get(1).toLowerCase();
			}
		}
		return "";
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
	
	public void setTaunt(boolean t){
		hasTaunt = t;
	}

	public String getOverload() {
		for (int i = 0; i < this.getEffects().size(); i++){
			if (this.getEffects().get(i).get(0).equals("overload")){
				return this.getEffects().get(i).get(1).toLowerCase();
			}
		}
		return "";
	}
	
	public boolean hasCharge() {
		for (int i = 0; i < this.getEffects().size(); i++){
			if (this.getEffects().get(i).get(0).equals("charge")){
				return true;
			}
		}
		return false;
	}
}