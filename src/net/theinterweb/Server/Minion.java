package net.theinterweb.Server;

public class Minion {
	
	private String name;
	private int attack;
	private int health;
	private boolean ready = false;
	private String deathrattle = "";
	
	public Minion(String n, int a, int h, String d){
		this.name = n;
		this.setAttack(a);
		this.setHealth(h);
		this.deathrattle = d;
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

	public String getName() {
		return name;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public void takeDamage(int d) {
		health -= d;
	}
	
	public String getDeathRattle(){
		return deathrattle;
	}
	
}