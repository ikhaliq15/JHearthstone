package net.theinterweb.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

public class Game {
	 
	private final int PORT = 8787;
	private ArrayList<Player> players = new ArrayList<Player>();
	
	private int currentTurn = 0;
	
	private ArrayList<Card> t_deck = new ArrayList<Card>();
	
	public Game() throws IOException, ParseException, InterruptedException{
		this.fillDeck();
		System.out.println("SERVER IS BOOTING UP!");
		startUpServer();
		System.out.println("SERVER SET UP!");
		while (true){
			players.get(currentTurn).startTurn();
			sendClientInfo();
			players.get(currentTurn).waitUntilEndTurn();
			if (currentTurn == 0){
				 currentTurn = 1;
			}else if (currentTurn == 1){
				currentTurn = 0;
			}
		}
	}
	
	public void sendClientInfo() {
		for (int i=0; i < players.size(); i++){
			String str_TotalMessage = "";
			if(i == currentTurn){
				str_TotalMessage = "ENABLECONTROLS";
			}else{
				str_TotalMessage = "DISABLECONTROLS";
			}
			if(i == 0){
				str_TotalMessage += players.get(1).getHealth() + players.get(0).getHealth();
				str_TotalMessage += players.get(0).getMana() + players.get(0).getManaCrystals();
				str_TotalMessage += players.get(1).getMana() + players.get(1).getManaCrystals();
				str_TotalMessage += players.get(0).getHand().substring(0, players.get(0).getHand().length()-1) + "^";
			}else if (i==1){
				str_TotalMessage += players.get(0).getHealth() + players.get(1).getHealth();
				str_TotalMessage += players.get(1).getMana() + players.get(1).getManaCrystals();
				str_TotalMessage += players.get(0).getMana() + players.get(0).getManaCrystals();
				str_TotalMessage += players.get(1).getHand().substring(0, players.get(1).getHand().length()-1) + "^";
			}
			if(i == currentTurn){
				str_TotalMessage += "";
			}else{
				str_TotalMessage += "";
			}
			players.get(i).sendMessage("BASICINFO:" + str_TotalMessage);
			if (i == 0){
				players.get(0).sendMessage("FBOARD:" + players.get(0).getBoard());
				players.get(0).sendMessage("EBOARD:" + players.get(1).getBoard());
			}else if (i == 1){
				players.get(1).sendMessage("FBOARD:" + players.get(1).getBoard());
				players.get(1).sendMessage("EBOARD:" + players.get(0).getBoard());
			}
		}
	}

	private void startUpServer() throws IOException{
        ServerSocket listener = new ServerSocket(PORT);
        for (int i = 0; i < 2; i++){
        	System.out.println("SERVER IS WAITING FOR CONNECTIONS!");
        	players.add(new Player(listener.accept(), t_deck, this));
        }
	}
	
	private void fillDeck() throws ParseException{
//		String[] deck = {"Knife Juggler", "Knife Juggler", "Azure Drake", "Azure Drake", "Tunnel Trogg", "Tunnel Trogg", 
//						"Totem Golem", "Totem Golem", "Argent Squire", "Argent Squire", "Dire Wolf Alpha", "Dire Wolf Alpha", 
//						"Tuskarr Totemic", "Tuskarr Totemic", "Flamewreathed Faceless", "Flamewreathed Faceless",
//						"Thing from Below", "Thing from Below", "Al'Akir the Windlord", "Mana Tide Totem", "Mana Tide Totem",
//						"Deathwing", "Chillwind Yeti", "Chillwind Yeti", "Polluted Hoarder", "Polluted Hoarder", "Wisp", "Wisp", 
//						"Voodoo Doctor", "Voodoo Doctor"};
		
		String[] deck = {"Tunnel Trogg", "Tunnel Trogg", "Siltfin Spiritwalker", "Siltfin Spiritwalker",
						"Totem Golem", "Totem Golem", "Flamewreathed Faceless", "Flamewreathed Faceless",
						"Unbound Elemental", "Unbound Elemental"};
		for (int i = 0; i < deck.length; i++){
			//System.out.println(deck[i]);
			t_deck.add(new Card(deck[i]));
		}
	}
	
	public Minion attack(Minion m, int t){
		Minion r = m;
		if(currentTurn == 1){
			Minion tm = players.get(0).getBoardArray().get(t);
			r.takeDamage(tm.getAttack());
			tm.takeDamage(r.getAttack());
		}else if(currentTurn == 0){
			Minion tm = players.get(1).getBoardArray().get(t);
			r.takeDamage(tm.getAttack());
			tm.takeDamage(r.getAttack());
		}
		players.get(1).removeDead();
		players.get(0).removeDead();
		sendClientInfo();
		return r;
	}
	
	public void attackHero(Minion m){
		if (currentTurn == 0){
			players.get(1).dealDamage(m.getAttack());
		}else if(currentTurn == 1){
			players.get(0).dealDamage(m.getAttack());
		}
		sendClientInfo();
		checkDeadHeroes();
	}
	
	public void checkDeadHeroes(){
		//System.out.println("PLAYER 0: " + players.get(0).isDead() + ", HEALTH: " + players.get(0).getHealth());
		//System.out.println("PLAYER 1: " + players.get(1).isDead() + ", HEALTH: " + players.get(1).getHealth());
		if (players.get(0).isDead() && players.get(1).isDead()){
			this.sendGlobalMessage("RESULT:TIE");
		}else if (players.get(0).isDead()){
			players.get(1).sendMessage("RESULT:WIN");
			players.get(0).sendMessage("RESULT:LOSE");
		}else if(players.get(1).isDead()){
			players.get(1).sendMessage("RESULT:LOSE");
			players.get(0).sendMessage("RESULT:WIN");
		}
	}
	
	public void sendGlobalMessage(String m){
		for (int i = 0; i < players.size(); i++){
			players.get(i).sendMessage(m);
		}
	}
}