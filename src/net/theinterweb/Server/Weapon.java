package net.theinterweb.Server;

import org.json.JSONObject;

import java.util.ArrayList;

public class Weapon {

    private Game game;

    private String name;
    private int attack;
    private int durability;
    private int maxDurability;
    private boolean ready = true;
    private boolean hasWindfury = false;
    private int spellDamage = 0;
    private int numberOfAttacksThisTurn = 0;
    private ArrayList<String> triggers = new ArrayList<>();
    private org.json.simple.JSONObject trigger_object;

    public Weapon () {
        this.name = "";
        this.setAttack(0);
        this.setDurability(0);
        this.setMaxDurability(0);
        this.setSpellDamage(0);
        this.triggers = new ArrayList<>();
        this.trigger_object = new org.json.simple.JSONObject();
        this.hasWindfury = false;
        this.game = null;
    }

    public Weapon (Card c, Game g) {
        this.name = c.getName();
        this.setAttack(c.getAttack());
        this.setDurability(c.getHealth());
        this.setMaxDurability(c.getHealth());
        this.setSpellDamage(c.getSpellDamage());
        this.triggers = c.getTriggers();
        this.trigger_object = c.getTriggerObject();
        this.hasWindfury = c.getWindfury();
        this.game = g;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDurability() {
        return this.durability;
    }

    public void setDurability(int health) {
        this.durability = health;
    }

    public void loseDurability () { this.durability--; }

    public int getMaxDurability () {return this.maxDurability;}

    public void setMaxDurability(int h) { this.maxDurability = h; }

    public String getName() {
        return name;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean hasWindfury () { return this.hasWindfury; }

    public void addAttribute(String attr) {
        if (attr.equals("WINDFURY")) {
            this.hasWindfury = true;
            if (this.getNumberOfAttacksThisTurn() == 1) {
                this.setReady(true);
            }
            game.sendClientInfo();
        }
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
}
