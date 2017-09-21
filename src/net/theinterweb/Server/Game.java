package net.theinterweb.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.theinterweb.Shared.ServerCommands;
import org.json.simple.parser.ParseException;

public class Game extends Thread{
	 
	static final int PORT = 8787;
	ArrayList<Player> players = new ArrayList<>();
	private int currentTurn = 0;
	boolean gameIsOver = false;
	
	public Game() throws IOException, ParseException, InterruptedException{
	}
	
	public void run(){
		players.get(0).sendMessage(ServerCommands.GAME_READY_TO_START);
		while (!(players.get(0).isDead() || players.get(1).isDead())){
			players.get(currentTurn).startTurn();
			sendClientInfo();
			try {
				players.get(currentTurn).waitUntilEndTurn();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (currentTurn == 0){
				 currentTurn = 1;
			}else if (currentTurn == 1){
				currentTurn = 0;
			}
			if (players.get(0).isDead() || players.get(1).isDead()) {
				break;
			}
		}
	}
	
	public void sendClientInfo() {
		if (players.size() < 2) {
			players.get(0).sendMessage(ServerCommands.START_PAGE_WAITING);
		} else {
			for (int i = 0; i < players.size(); i++) {
				String str_TotalMessage = "";
				if (i == currentTurn) {
					str_TotalMessage = ServerCommands.GAME_ENABLE_CONTROLS;
				} else {
					str_TotalMessage = ServerCommands.GAME_DISABLE_CONTROLS;
				}
				if (i == 0) {
					str_TotalMessage += players.get(1).getHealth() + players.get(0).getHealth();
					str_TotalMessage += players.get(1).getArmor() + ":EAR" + players.get(0).getArmor() + ":FAR";
					str_TotalMessage += players.get(0).getMana() + players.get(0).getManaCrystals();
					str_TotalMessage += players.get(1).getMana() + players.get(1).getManaCrystals();
					str_TotalMessage += players.get(0).getWeapon().getAttack() + ":FWA" + players.get(0).getWeapon().getDurability() + ":FWD";
					str_TotalMessage += players.get(1).getWeapon().getAttack() + ":EWA" + players.get(1).getWeapon().getDurability() + ":EWD";
					str_TotalMessage += players.get(0).isReady() + ":FCA";
					str_TotalMessage += players.get(0).getHand().substring(0, players.get(0).getHand().length() - 1) + "^";
				} else if (i == 1) {
					str_TotalMessage += players.get(0).getHealth() + players.get(1).getHealth();
					str_TotalMessage += players.get(0).getArmor() + ":EAR" + players.get(1).getArmor() + ":FAR";
					str_TotalMessage += players.get(1).getMana() + players.get(1).getManaCrystals();
					str_TotalMessage += players.get(0).getMana() + players.get(0).getManaCrystals();
					str_TotalMessage += players.get(1).getWeapon().getAttack() + ":FWA" + players.get(1).getWeapon().getDurability() + ":FWD";
					str_TotalMessage += players.get(0).getWeapon().getAttack() + ":EWA" + players.get(0).getWeapon().getDurability() + ":EWD";
					str_TotalMessage += players.get(1).isReady() + ":FCA";
					str_TotalMessage += players.get(1).getHand().substring(0, players.get(1).getHand().length() - 1) + "^";
				}
				if (i == currentTurn) {
					str_TotalMessage += "";
				} else {
					str_TotalMessage += "";
				}
				players.get(i).sendMessage(ServerCommands.GAME_BASIC_INFO_PREFIX + str_TotalMessage);
				if (i == 0) {
					players.get(0).sendMessage(ServerCommands.GAME_FRIEND_BOARD_PREFIX + players.get(0).getBoard());
					players.get(0).sendMessage(ServerCommands.GAME_ENEMY_BOARD_PREFIX + players.get(1).getBoard());
				} else if (i == 1) {
					players.get(1).sendMessage(ServerCommands.GAME_FRIEND_BOARD_PREFIX + players.get(1).getBoard());
					players.get(1).sendMessage(ServerCommands.GAME_ENEMY_BOARD_PREFIX + players.get(0).getBoard());
				}
			}
		}
	}

	public void trigger(String trigger_name, HashMap<String, Object> arguments) throws org.json.simple.parser.ParseException {
		for (Player p: this.players) {
			p.trigger(trigger_name, arguments);
		}
	}
	
	public Minion attack(Minion m, int t){
		Minion r = m;
		if(currentTurn == 1){
			Minion tm = players.get(0).getBoardArray().get(t);
			tm.takeDamage(r.getAttack());
			r.takeDamage(tm.getAttack());
			r.addAttackThisTurn();
		}else if(currentTurn == 0){
			Minion tm = players.get(1).getBoardArray().get(t);
			tm.takeDamage(r.getAttack());
			r.takeDamage(tm.getAttack());
			r.addAttackThisTurn();
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
		m.addAttackThisTurn();
		sendClientInfo();
		checkDeadHeroes();
	}

	public void attackWithHero(Player p, int t){
		if(currentTurn == 1){
			Minion tm = players.get(0).getBoardArray().get(t);
			tm.takeDamage(p.getWeapon().getAttack());
			p.dealDamage(tm.getAttack());
			p.addAttackThisTurn();
		}else if(currentTurn == 0){
			Minion tm = players.get(1).getBoardArray().get(t);
			tm.takeDamage(p.getWeapon().getAttack());
			p.dealDamage(tm.getAttack());
			p.addAttackThisTurn();
		}
		if (p.hasWeapon()) {
			p.getWeapon().loseDurability();
		}
		players.get(1).removeDead();
		players.get(0).removeDead();
		sendClientInfo();
	}

	public void attackHeroWithHero(Player p){
		if (currentTurn == 0){
			players.get(1).dealDamage(p.getWeapon().getAttack());
		}else if(currentTurn == 1){
			players.get(0).dealDamage(p.getWeapon().getAttack());
		}
		if (p.hasWeapon()) {
			p.getWeapon().loseDurability();
		}
		p.addAttackThisTurn();
		sendClientInfo();
		checkDeadHeroes();
	}
	
	public boolean checkDeadHeroes(){
		if (players.get(0).isDead() && players.get(1).isDead()){
			this.sendGlobalMessage(ServerCommands.GAME_RESULT_PREFIX + ServerCommands.GAME_RESULT_TIE);
			return true;
		}else if (players.get(0).isDead()){
			players.get(1).sendMessage(ServerCommands.GAME_RESULT_PREFIX + ServerCommands.GAME_RESULT_WIN);
			players.get(0).sendMessage(ServerCommands.GAME_RESULT_PREFIX + ServerCommands.GAME_RESULT_LOSE);
			return true;
		}else if(players.get(1).isDead()){
			players.get(1).sendMessage(ServerCommands.GAME_RESULT_PREFIX + ServerCommands.GAME_RESULT_LOSE);
			players.get(0).sendMessage(ServerCommands.GAME_RESULT_PREFIX + ServerCommands.GAME_RESULT_WIN);
			return true;
		}
		return false;
	}
	
	private void sendGlobalMessage(String m){
		for (Player p: players){
			p.sendMessage(m);
		}
	}
}