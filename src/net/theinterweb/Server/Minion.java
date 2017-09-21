package net.theinterweb.Server;

import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;

public class Minion {

	private Game game;

	private String name;
	private String race;
	private int attack;
	private int health;
	private int maxHealth;
	private boolean ready = false;
	private boolean hasTaunt = false;
	private boolean hasDivineShield = false;
	private boolean isFrozen = false;
	private boolean missedAttack = false;
	private String deathrattle = "";
	private boolean hasCharge = false;
	private boolean hasWindfury = false;
	private int spellDamage = 0;
	private int numberOfAttacksThisTurn = 0;
	private ArrayList<String> triggers = new ArrayList<>();
	private org.json.simple.JSONObject trigger_object;
	
//	public Minion(String n, int a, int h, String d, boolean taunt, boolean ds){
//		this.name = n;
//		this.setAttack(a);
//		this.setHealth(h);
//		this.setMaxHealth(h);
//		this.setTaunt(taunt);
//		this.setDivineShield(ds);
//		this.deathrattle = d;
//	}

	public Minion (Card c, Game g) {
		this.name = c.getName();
		this.setAttack(c.getAttack());
		this.setHealth(c.getHealth());
		this.setMaxHealth(c.getHealth());
		this.setTaunt(c.hasTaunt());
		this.setDivineShield(c.getDivineShield());
		this.hasCharge = c.hasCharge();
		this.setSpellDamage(c.getSpellDamage());
		this.triggers = c.getTriggers();
		this.trigger_object = c.getTriggerObject();
		this.hasWindfury = c.getWindfury();
		this.game = g;
		this.race = c.getRace().toUpperCase();
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth () {return this.maxHealth;}

	public void setMaxHealth(int h) { this.maxHealth = h; }

	public String getName() {
		return name;
	}

	public boolean isReady() {
		return ready;
	}

	public void setFrozen (boolean f) {
		this.isFrozen = f;
	}

	public boolean isFrozen () { return this.isFrozen; }

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public void takeDamage(int d) {
		if (!hasDivineShield) {
			health -= d;
			try {
				HashMap<String, Object> arguments = new HashMap<>();
				arguments.put("source_minion", this);
				//System.out.println(this.name.toUpperCase() + " IS TAKING DAMAGE.");
				game.trigger("DamageReceivedTrigger", arguments);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			hasDivineShield = false;
		}
	}

	public void setTaunt (boolean t) {this.hasTaunt = t;}

	public boolean getTaunt () { return this.hasTaunt; }

	public void setDivineShield (boolean ds) { this.hasDivineShield = ds; }

	public boolean getMissedAttack () { return this.missedAttack; }

	public void setMissedAttack (boolean ma) { this.missedAttack = ma; }

	public boolean getDivineShield() { return this.hasDivineShield; }

	public boolean hasCharge () { return this.hasCharge; }

	public boolean hasWindfury () { return this.hasWindfury; }

	public void heal (int h) throws org.json.simple.parser.ParseException {
		if (this.health < this.maxHealth && h != 0) {
			HashMap<String, Object> arguments = new HashMap<>();
			arguments.put("targetEntityType", "MINION");
			try {
				game.trigger("HealingTrigger", arguments);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
 		this.health += h;
		if (this.health > this.getMaxHealth()) {
			this.health = this.getMaxHealth();
		}
	}

	public void addAttribute(String attr) {
		if (attr.equals("TAUNT")) {
			this.setTaunt(true);
		} else if (attr.equals("DIVINE_SHIELD")) {
			this.setDivineShield(true);
		} else if (attr.equals("FROZEN")) {
			System.out.println(this.name + " IS FROZEN!");
			this.setFrozen(true);
		} else if (attr.equals("WINDFURY")) {
			this.hasWindfury = true;
			if (this.getNumberOfAttacksThisTurn() == 1) {
				this.setReady(true);
			}
			game.sendClientInfo();
		}
	}

	public String getDeathRattle(){
		return deathrattle;
	}

	public boolean meetsFilterRequirements (JSONObject filter) {
		String filterClass = "";
		if (filter.has("class")) {
			filterClass = filter.getString("class");
		}
		if (filterClass.equals("AttributeFilter")) {
			String attribute = "";
			String operation = "";
			if (filter.has("attribute")) {
				attribute = filter.getString("attribute");
			}
			if (filter.has("operation")) {
				operation = filter.getString("operation");
			}
			if (operation.equals("HAS")) {
				if (attribute.equals("CHARGE")) {
					return this.hasCharge();
				}
			}
		}
		return false;
	}

	public void setSpellDamage(int spellDamage) {
		this.spellDamage = spellDamage;
	}

	public int getSpellDamage () {
		return this.spellDamage;
	}

	public boolean hasTriggerEvent (String triggerEvent) {
		return triggers.contains(triggerEvent);
	}

	public org.json.simple.JSONObject getTriggerObject () {
		return trigger_object;
	}

	public void addAttackThisTurn () {
		this.numberOfAttacksThisTurn += 1;
	}

	public void resetNumberOfAttacksThisTurn () {
		this.numberOfAttacksThisTurn = 0;
	}

	public int getNumberOfAttacksThisTurn () {
		return this.numberOfAttacksThisTurn;
	}

	public String getRace () {
		return this.race;
	}
}