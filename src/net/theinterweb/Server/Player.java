package net.theinterweb.Server;

import net.theinterweb.Shared.ServerCommands;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Player {

	private BufferedReader in;
    private PrintWriter out;
    
    private ArrayList<Card> deck = new ArrayList<>();
    private ArrayList<Card> hand = new ArrayList<>();
    private ArrayList<Minion> board = new ArrayList<>();
    
    private int fatigue = 0;
    
    private int health = 30;
    private int maxHealth = health;
    private int armor = 0;

    private int turnMana = 0;
    private int manacrystals = 0;
    private int overloaded_crystals = 0;

    private ArrayList<JSONObject> auras = new ArrayList<>();
    private ArrayList<Minion> aura_sources = new ArrayList<>();

    private String username;
    
    private Game game;

    private Weapon weapon;

    private boolean hasWindfury = false;
    private int numberOfAttacksThisTurn = 0;

    private boolean canAttack = false;
    
	public Player(Socket accept, ArrayList<Card> d, Game g, String u) throws IOException {
		this.setUsername(u);
		this.weapon = new Weapon();
		game = g;
		in = new BufferedReader(new InputStreamReader(
                accept.getInputStream()));
		out = new PrintWriter(accept.getOutputStream(), true);
		for (int i = 0; i < d.size(); i ++){
			deck.add(d.get(i));
		}
		this.drawCard(3);
	}
	
    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
	
    public static ArrayList<String> split(String s, char r){
		ArrayList<String> t = new ArrayList<String>();
		String ts = "";
		for (int i = 0; i < s.length(); i++){
			if(s.charAt(i) == r){
				t.add(ts);
				ts = "";
			}else{
				ts += s.charAt(i);
			}
		}
		t.add(ts);
		return t;
	}

	public static String replaceCommas(String s){
        String t = "";
        for (int i = 0; i < s.length(); i++){
            if(s.charAt(i) == ','){
                t += "^";
            }else{
                t += String.valueOf(s.charAt(i));
            }
        }
        return t;
    }
	
	public static int inNumerals(String inwords)
	{
	    int wordnum = 0;
	    String[] arrinwords = inwords.split(" ");
	    int arrinwordsLength = arrinwords.length;
	    if(inwords.equals("zero"))
	    {
	        return 0;
	    }
	    if(inwords.contains("thousand"))
	    {
	        int indexofthousand = inwords.indexOf("thousand");
	        //System.out.println(indexofthousand);
	        String beforethousand = inwords.substring(0,indexofthousand);
	        //System.out.println(beforethousand);
	        String[] arrbeforethousand = beforethousand.split(" ");
	        int arrbeforethousandLength = arrbeforethousand.length;
	        //System.out.println(arrbeforethousandLength);
	        if(arrbeforethousandLength==2)
	        {
	            wordnum = wordnum + 1000*(wordtonum(arrbeforethousand[0]) + wordtonum(arrbeforethousand[1]));
	            //System.out.println(wordnum);
	        }
	        if(arrbeforethousandLength==1)
	        {
	            wordnum = wordnum + 1000*(wordtonum(arrbeforethousand[0]));
	            //System.out.println(wordnum);
	        }

	    }
	    if(inwords.contains("hundred"))
	    {
	        int indexofhundred = inwords.indexOf("hundred");
	        //System.out.println(indexofhundred);
	        String beforehundred = inwords.substring(0,indexofhundred);

	        //System.out.println(beforehundred);
	        String[] arrbeforehundred = beforehundred.split(" ");
	        int arrbeforehundredLength = arrbeforehundred.length;
	        wordnum = wordnum + 100*(wordtonum(arrbeforehundred[arrbeforehundredLength-1]));
	        String afterhundred = inwords.substring(indexofhundred+8);//7 for 7 char of hundred and 1 space
	        //System.out.println(afterhundred);
	        String[] arrafterhundred = afterhundred.split(" ");
	        int arrafterhundredLength = arrafterhundred.length;
	        if(arrafterhundredLength==1)
	        {
	            wordnum = wordnum + (wordtonum(arrafterhundred[0]));
	        }
	        if(arrafterhundredLength==2)
	        {
	            wordnum = wordnum + (wordtonum(arrafterhundred[1]) + wordtonum(arrafterhundred[0]));
	        }
	        //System.out.println(wordnum);

	    }
	    if(!inwords.contains("thousand") && !inwords.contains("hundred"))
	    {
	        if(arrinwordsLength==1)
	        {
	            wordnum = wordnum + (wordtonum(arrinwords[0]));
	        }
	        if(arrinwordsLength==2)
	        {
	            wordnum = wordnum + (wordtonum(arrinwords[1]) + wordtonum(arrinwords[0]));
	        }
	        //System.out.println(wordnum);
	    }


	    return wordnum;
	}
	
	public static int wordtonum(String word)
	{
	        int num = 0;
	        switch (word) {
	            case "one":  num = 1;
	                     break;
	            case "two":  num = 2;
	                     break;
	            case "three":  num = 3;
	                     break;
	            case "four":  num = 4;
	                     break;
	            case "five":  num = 5;
	                     break;
	            case "six":  num = 6;
	                     break;
	            case "seven":  num = 7;
	                     break;
	            case "eight":  num = 8;
	                     break;
	            case "nine":  num = 9;
	                     break;
	            case "ten": num = 10;
	                     break;
	            case "eleven": num = 11;
	                     break;
	            case "twelve": num = 12;
	                     break;
	            case "thirteen": num = 13;
	                     break;
	            case "fourteen": num = 14;
	                     break;
	            case "fifteen": num = 15;
	                     break;
	            case "sixteen": num = 16;
	                     break;
	            case "seventeen": num = 17;
	                     break;
	            case "eighteen": num = 18;
	                     break;
	            case "nineteen": num = 19;
	                     break;
	            case "twenty":  num = 20;
	                     break;
	            case "thirty":  num = 30;
	                     break;
	            case "forty":  num = 40;
	                     break;
	            case "fifty":  num = 50;
	                     break;
	            case "sixty":  num = 60;
	                     break;
	            case "seventy":  num = 70;
	                     break;
	            case"eighty":  num = 80;
	                     break;
	            case "ninety":  num = 90;
	                     break;
	            case "hundred": num = 100;
	                        break;
	            case "thousand": num = 1000;
	                        break;
	           /*default: num = "Invalid month";
	                             break;*/
	        }
	        return num;
	    }
	
	public void giveManaCrystal(boolean full, int numOfCrystals){
		manacrystals += numOfCrystals;
		if(full){
			turnMana += numOfCrystals;
		}
		if (manacrystals > 10){
			manacrystals = 10;
		}
		if (turnMana > 10){
			turnMana = 10;
		}
	}
	
	public void sendMessage(String msg){
		out.println(msg);
	}
	
	public void startTurn() {
		this.giveManaCrystal(true, 1);
		this.turnMana = this.manacrystals-overloaded_crystals;
		overloaded_crystals = 0;
		this.drawCard(1);
		this.setReady(true);
		this.resetNumberOfAttacksThisTurn();
		for (int i = 0; i < board.size(); i++){
			if (board.get(i).getMissedAttack()) {
				board.get(i).setFrozen(false);
				System.out.println(board.get(i).getName() + " IS UNFROZEN!");
				board.get(i).setMissedAttack(false);
			}
			if (!board.get(i).isFrozen()) {
				board.get(i).setReady(true);
				board.get(i).resetNumberOfAttacksThisTurn();
			} else {
				board.get(i).setReady(false);
				board.get(i).setMissedAttack(true);
			}
		}
	}
	
	public String getMana(){
		if (turnMana < 10){
			return "0" + turnMana;
		}
		return "" + turnMana;
	}
	
	public String getManaCrystals(){
		if (manacrystals < 10){
			return "0" + manacrystals;
		}
		return "" + manacrystals;
	}
	
	public String getHealth() {
		if(health <= 0){
			return "00";
		}
		if (health < 10){
			return "0" + health;
		}
		return "" + health;
	}
	
	private void drawCard(int num){
		for(int i = 0; i < num; i++){
			if (deck.size() > 0){
				if (hand.size() >= 10){
					this.sendMessage(ServerCommands.GAME_ERROR_PREFIX + ServerCommands.GAME_ERROR_BURNED + deck.remove((int) (Math.random() * (deck.size()))).getName());
				}else{
					hand.add(deck.remove((int) (Math.random() * (deck.size()))));
				}
			}else{
				fatigue++;
				health -= fatigue;
				this.sendMessage(ServerCommands.GAME_ERROR_PREFIX + ServerCommands.GAME_ERROR_FATIGUE + fatigue);
				game.checkDeadHeroes();
			}
		}
	}
    
	public String getHand(){
		return hand.toString();
	}
    
	public String getBoard(){
		ArrayList<ArrayList<String>> b = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < board.size(); i++){
			b.add(new ArrayList<String>());
			b.get(b.size()-1).add(board.get(i).getName());
			b.get(b.size()-1).add(Integer.toString(board.get(i).getAttack()));
			b.get(b.size()-1).add(Integer.toString(board.get(i).getHealth()));
			b.get(b.size()-1).add("" + (board.get(i).isReady()));
			b.get(b.size()-1).add("" + board.get(i).getTaunt());
		}
		String r = replaceCommas(b.toString());
		return r;
	}

	public ArrayList<Minion> getBoardArray(){
		return board;
	}

	public void waitUntilEndTurn() throws IOException{
        while (true){
        	if(in.ready()){
        		String msg = in.readLine();
        		//System.out.println("LINE: " + msg);
        		if (msg.equals(ServerCommands.GAME_END_TURN)){
        			break;
        		}else if(msg.startsWith(ServerCommands.GAME_PLAY_PREFIX) && Integer.valueOf(msg.substring(ServerCommands.GAME_PLAY_PREFIX.length())) < hand.size()){
        			int minion = Integer.valueOf(msg.substring(ServerCommands.GAME_PLAY_PREFIX.length()));
        			Card t = hand.get(minion);
        			if (t.getCost() <= turnMana){
        				String type = t.getType().toLowerCase();
        				if (type.equals("minion")) {
							//Minion m = new Minion(t.getName(), t.getAttack(), t.getHealth(), t.getDeathRattle(), t.hasTaunt(), t.getDivineShield());
							Minion m = new Minion(t, this.game);
							board.add(m);
							hand.remove(minion);
							turnMana -= t.getCost();
							try {
								this.executeCommands(t.getBattleCry(), board.indexOf(m));
							} catch (ParseException e) {
								e.printStackTrace();
							}
							if (m.hasCharge()) {
								m.setReady(true);
							}
							for (Player p: game.players) {
								for (int i = 0; i < p.getAuras().size(); i++) {
									if (p.isValidTargetOfAura(p.getAuras().get(i), this, board.indexOf(m), p.getAuraMinions().get(i))) {
										System.out.println("WORKING WITH " + p.getAuras().get(i).getString("class"));
										applyAura(p.getAuras().get(i), this, board.indexOf(m));
									}
								}
							}
							if (t.hasAura()) {
								auras.add(t.getAura());
								aura_sources.add(m);
								for (Player p: game.players) {
									for (int i = 0; i < p.board.size(); i++) {
										if (isValidTargetOfAura(t.getAura(), p, i, m)) {
											applyAura(t.getAura(), p, i);
										}
									}
								}
							}
						} else if (type.equals("spell")) {
							hand.remove(minion);
							turnMana -= t.getCost();
        					try {
								Spell s = new Spell(t.getName());
								this.executeCommands(s.getEffect(), -1);
							} catch (ParseException e) {
        						e.printStackTrace();
							}
						} else if (type.equals("weapon")) {
							hand.remove(minion);
							turnMana -= t.getCost();
							Weapon w = new Weapon(t, game);
							weapon = w;
							if (this.getNumberOfAttacksThisTurn() == 0) {
								this.setReady(true);
							}
						}
        				game.sendClientInfo();
        			}else{
        				sendMessage(ServerCommands.GAME_ERROR_PREFIX + ServerCommands.GAME_ERROR_MANA_LOW);
        			}
        		}else if(msg.startsWith(ServerCommands.GAME_ATTACK_PREFIX)){
        			msg = msg.substring(ServerCommands.GAME_ATTACK_PREFIX.length());
        			int attacker = Integer.valueOf(msg.substring(0, msg.indexOf(":")));
        			msg = msg.substring(msg.indexOf(":") + 1);
        			int target = Integer.valueOf(msg);
        			boolean opponentHasTaunt = false;
        			boolean attackingTauntMinion = false;
        			for (Player p: game.players) {
        				if (!p.username.equals(this.username)) {
        					for (int i = 0; i < p.getBoardArray().size(); i++) {
        						if (p.getBoardArray().get(i).getTaunt()) {
        							opponentHasTaunt = true;
        							if (i == target) {
        								attackingTauntMinion = true;
									}
								}
							}
						}
					}
					if (opponentHasTaunt && !attackingTauntMinion) {
						sendMessage(ServerCommands.GAME_ERROR_PREFIX + ServerCommands.GAME_ERROR_TAUNT);
						game.sendClientInfo();
					} else {
        				if (attacker != 9999) {
							if ((board.get(attacker).hasWindfury() && board.get(attacker).getNumberOfAttacksThisTurn() == 1) || (!board.get(attacker).hasWindfury() && board.get(attacker).getNumberOfAttacksThisTurn() == 0)) {
								board.get(attacker).setReady(false);
							}
							if (target == 9999) {
								game.attackHero(board.get(attacker));
							} else {
								game.attack(board.get(attacker), target);
							}
						} else {
							if ((this.hasWindfury() && this.getNumberOfAttacksThisTurn() == 1) || (!this.hasWindfury() && this.getNumberOfAttacksThisTurn() == 0)) {
								this.setReady(false);
							}
							if (target == 9999) {
								game.attackHeroWithHero(this);
							} else {
								game.attackWithHero(this, target);
							}
						}
					}
        		}
        	}
        }
	}

	private int getValue (JSONObject spell, String key, Minion source_minion) {
		if (spell.has(key)) {
			String valueObjectType = spell.get(key).getClass().getSimpleName();
			if (valueObjectType.equals("Integer")) {
				return spell.getInt(key);
			} else if (valueObjectType.equals("JSONObject")){
				JSONObject value = spell.getJSONObject(key);
				String valueClass = value.getString("class");
				if (valueClass.equals("ConditionalValueProvider")) {
					if (analyzeCondition(value.getJSONObject("condition"))) {
						return value.getInt("ifTrue");
					} else {
						return value.getInt("ifFalse");
					}
				} else if (valueClass.equals("AttributeValueProvider")) {
					String attribute = "";
					if (value.has("attribute")) {
						attribute = value.getString("attribute");
					}
					if (attribute.equals("HP")) {
						return source_minion.getHealth();
					} else if (attribute.equals("MAX_HP")) {
						return source_minion.getMaxHealth();
					}
				}
			}
		}
		return 0;
	}

	private boolean analyzeCondition (JSONObject condition) {
		String conditionClass = "";
		if (condition.has("class")) {
			conditionClass = condition.getString("class");
		}
		if (conditionClass.equals("MinionOnBoardCondition")) {
			if (condition.has("cardFilter")) {
				JSONObject cardFilter = condition.getJSONObject("cardFilter");
				String cardFilterClass = "";
				if (cardFilter.has("class")) {
					cardFilterClass = cardFilter.getString("class");
				}
				if (cardFilterClass.equals("RaceFilter")) {
					String race = "";
					if (cardFilter.has("race")) {
						race = cardFilter.getString("race");
					}
					for (int i = 0; i < this.board.size(); i++) {
						if (board.get(i).getRace().equals(race)) {
							return true;
						}
					}
					return false;
				}
			} else {
				return this.board.size() > 0;
			}
		}
		return false;
	}

    private String executeCommands(JSONObject cmd, int pos) throws ParseException {
		String selectedTarget = "";
		if (cmd.has("spell")) {
			JSONObject spell = cmd.getJSONObject("spell");
			if (spell.has("class")) {
				String spellClass = spell.getString("class");
				if (spellClass.equals("DrawCardSpell")) {
					int value = 1;
					if (spell.has("value")) {
						value = spell.getInt("value");
					}
					this.drawCard(value);
				} else if (spellClass.equals("SummonSpell")) {
					ArrayList<String> cards = new ArrayList<>();
					String position = "RIGHT";
					if (spell.has("card")) {
						cards.add(toTitleCase(spell.getString("card").substring("token_".length()).replace("_", " ")));
					}
					if (spell.has("cards")) {
						for (Object o: spell.getJSONArray("cards")) {
							cards.add(toTitleCase(((String)(o)).substring("token_".length()).replace("_", " ")));
						}
					}
					if (spell.has("boardPositionRelative")) {
						position = spell.getString("boardPositionRelative");
					}
					for (String card_name: cards) {
						Card c = new Card(card_name, "MINION");
						//Minion m = new Minion(c.getName(), c.getAttack(), c.getHealth(), c.getDeathRattle(), c.hasTaunt(), c.getDivineShield());
						Minion m = new Minion (c, this.game);
						if (c.hasCharge()) {
							m.setReady(true);
						}
						if (position.equals("LEFT")) {
							this.board.add(pos, m);
						} else if (position.equals("RIGHT")) {
							if (pos + 1 > this.board.size() || pos == -1) {
								this.board.add(m);
							} else {
								this.board.add(pos + 1, m);
							}
						}
						for (Player p: game.players) {
							for (int i = 0; i < p.getAuras().size(); i++) {
								if (p.isValidTargetOfAura(p.getAuras().get(i), this, board.indexOf(m), p.getAuraMinions().get(i))) {
									System.out.println("WORKING WITH " + p.getAuras().get(i).getString("class"));
									applyAura(p.getAuras().get(i), this, board.indexOf(m));
								}
							}
						}
						if (c.hasAura()) {
							auras.add(c.getAura());
							aura_sources.add(m);
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
									if (isValidTargetOfAura(c.getAura(), p, i, m)) {
										applyAura(c.getAura(), p, i);
									}
								}
							}
						}
					}
				} else if (spellClass.equals("DamageSpell")) {
					int value = 0;
					if (pos == -1) {
						value = this.getSpellDamage();
					}
					String targetSelection = "";
					String target = "";
					if (spell.has("value")) {
						value += getValue(spell, "value", null);
					}
					if (cmd.has("targetSelection")) {
						targetSelection = cmd.getString("targetSelection");
					}
					if (spell.has("target")) {
						target = spell.getString("target");
					}
					if ((!targetSelection.equals("NONE") && !targetSelection.equals("")) || targetSelection.startsWith("CUSTOM:")) {
						if (!targetSelection.startsWith("CUSTOM:")) {
							sendMessage(ServerCommands.GAME_GET_TARGET_PREFIX + targetSelection);
						}
						try {
							String response = "";
							if (!targetSelection.startsWith("CUSTOM:")) {
								response = in.readLine();
								selectedTarget = response;
							} else {
								response = targetSelection.substring("CUSTOM:".length());
								selectedTarget = response;
							}
							boolean isFriendly = response.startsWith("F");
							int selected_target = Integer.valueOf(response.substring(1));
							if (selected_target == 9999) {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										p.dealDamage(value);
									}
								}
							} else {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										for (int i = 0; i < p.board.size(); i++) {
											if (i == selected_target) {
												p.board.get(i).takeDamage(value);
											}
										}
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (!target.equals("")) {
						System.out.println("NON TARGETING DAMAGE SPELL");
						if (target.equals("ALL_OTHER_CHARACTERS")) {
							for (Player p : game.players) {
								p.dealDamage(value);
								for (int i = 0; i < p.board.size(); i++) {
									if (!(i == pos && p.username.equals(this.username))) {
										p.board.get(i).takeDamage(value);
									}
								}
							}
						} else if (target.equals("FRIENDLY_HERO")) {
							this.dealDamage(value);
						} else if (target.equals("ALL_CHARACTERS")) {
							for (Player p : game.players) {
								p.dealDamage(value);
								for (int i = 0; i < p.board.size(); i++) {
									p.board.get(i).takeDamage(value);
								}
							}
						} else if (target.equals("ENEMY_MINIONS")) {
							for (Player p : game.players) {
								if (!p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
										p.board.get(i).takeDamage(value);
									}
								}
							}
						} else if (target.equals("ALL_MINIONS")) {
							for (Player p : game.players) {
								for (int i = 0; i < p.board.size(); i++) {
									p.board.get(i).takeDamage(value);
								}
							}
						} else if (target.equals("FRIENDLY_CHARACTERS")) {
							for (Player p : game.players) {
								if (p.username.equals(this.username)) {
									p.dealDamage(value);
									for (int i = 0; i < p.board.size(); i++) {
										p.board.get(i).takeDamage(value);
									}
								}
							}
						} else if (target.equals("ENEMY_CHARACTERS")) {
							for (Player p : game.players) {
								if (!p.username.equals(this.username)) {
									p.dealDamage(value);
									for (int i = 0; i < p.board.size(); i++) {
										p.board.get(i).takeDamage(value);
									}
									p.removeDead();
								}
							}
						} else if (target.equals("ENEMY_HERO")) {
							for (Player p : game.players) {
								if (!p.username.equals(this.username)) {
									p.dealDamage(value);
								}
							}
						}
					}
					for (Player p : game.players) {
						p.removeDead();
					}
					game.sendClientInfo();
					game.checkDeadHeroes();
				} else if (spellClass.equals("MissilesSpell")) {
					String target = "";
					int value = 0;
					int howMany = getSpellDamage();
					if (spell.has("howMany")) {
						howMany += spell.getInt("howMany");
					}
					if (spell.has("value")) {
						value = getValue(spell, "value", null);
					}
					if (spell.has("target")) {
						target = spell.getString("target");
					}
					if (target.equals("ALL_OTHER_CHARACTERS")) {

					} else if (target.equals("FRIENDLY_HERO")) {
					} else if (target.equals("ALL_CHARACTERS")) {

					} else if (target.equals("ENEMY_MINIONS")) {

					} else if (target.equals("ALL_MINIONS")) {

					} else if (target.equals("FRIENDLY_CHARACTERS")) {

					} else if (target.equals("ENEMY_CHARACTERS")) {
						for (Player p: game.players) {
							if (!p.username.equals(this.username)) {
								for (int i = 0; i < howMany; i++) {
									int targetMissle = (int)(Math.random() * (p.board.size() + 1));
									System.out.println(targetMissle);
									if (targetMissle == p.board.size()) {
										p.dealDamage(value);
										System.out.println("MISSLE SHOT AT FACE");
									} else {
										if (!(p.board.get(targetMissle).getHealth() <= 0)) {
											p.board.get(targetMissle).takeDamage(value);
											System.out.println("MISSILE SHOT AT " + p.board.get(targetMissle).getName());
										} else {
											howMany ++;
											System.out.println("MISSLE ALMOST SHOT AT ZERO HEALTH MINION. + " + p.board.size());
										}
									}
								}
							}
						}
					} else if (target.equals("ENEMY_HERO")) {

					}
					for (Player p: game.players) {
						p.removeDead();
					}
					game.sendClientInfo();
					game.checkDeadHeroes();
				} else if (spellClass.equals("DiscardSpell")) {
					int value = 0;
					if (spell.has("value")) {
						value = spell.getInt("value");
					}
					for (int i = 0; i < value; i++) {
						int s = (int)(Math.random() * hand.size());
						hand.remove(s);
					}
					game.sendClientInfo();
				} else if (spellClass.equals("HealSpell")) {
					int value = 0;
					String targetSelection = "";
					String target = "";
					if (spell.has("value")) {
						if (pos >= 0) {
							value = getValue(spell, "value", board.get(pos));
						}
					}
					if (cmd.has("targetSelection")) {
						targetSelection = cmd.getString("targetSelection");
					}
					if (spell.has("target")) {
						target = spell.getString("target");
					}
					if ((!targetSelection.equals("NONE") && !targetSelection.equals("")) || targetSelection.startsWith("CUSTOM:")) {
						if (!targetSelection.startsWith("CUSTOM:")) {
							sendMessage(ServerCommands.GAME_GET_TARGET_PREFIX + targetSelection);
						}
						try {
							String response = "";
							if (!targetSelection.startsWith("CUSTOM:")) {
								response = in.readLine();
								selectedTarget = response;
							} else {
								response = targetSelection.substring("CUSTOM:".length());
								selectedTarget = response;
							}
							boolean isFriendly = response.startsWith("F");
							int selected_target = Integer.valueOf(response.substring(1));
							if (selected_target == 9999) {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										p.heal(value);
									}
								}
							} else {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										for (int i = 0; i < p.board.size(); i++) {
											if (i == selected_target) {
												value = getValue(spell, "value", p.board.get(i));
												p.board.get(i).heal(value);
											}
										}
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (!target.equals("")) {
						System.out.println("NON TARGETING DAMAGE SPELL");
						if (target.equals("ALL_OTHER_CHARACTERS")) {
							for (Player p: game.players) {
								p.heal(value);
								for (int i = 0; i < p.board.size(); i++) {
									if ( !(i == pos && p.username.equals(this.username)) ) {
										value = getValue(spell, "value", p.board.get(i));
										p.board.get(i).heal(value);
									}
								}
							}
						} else if (target.equals("FRIENDLY_HERO")) {
							this.heal(value);
						} else if (target.equals("ALL_CHARACTERS")) {
							for (Player p: game.players) {
								p.heal(value);
								for (int i = 0; i < p.board.size(); i++) {
									value = getValue(spell, "value", p.board.get(i));
									p.board.get(i).heal(value);
								}
							}
						} else if (target.equals("ENEMY_MINIONS")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
										value = getValue(spell, "value", p.board.get(i));
										p.board.get(i).heal(value);
									}
								}
							}
						} else if (target.equals("ALL_MINIONS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
									value = getValue(spell, "value", p.board.get(i));
									p.board.get(i).heal(value);
								}
							}
						} else if (target.equals("FRIENDLY_CHARACTERS")) {
							for (Player p: game.players) {
								if (p.username.equals(this.username)) {
									p.heal(value);
									for (int i = 0; i < p.board.size(); i++) {
										value = getValue(spell, "value", p.board.get(i));
										p.board.get(i).heal(value);
									}
								}
							}
						} else if (target.equals("ENEMY_CHARACTERS")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									p.heal(value);
									for (int i = 0; i < p.board.size(); i++) {
										value = getValue(spell, "value", p.board.get(i));
										p.board.get(i).heal(value);
									}
								}
							}
						} else if (target.equals("ENEMY_HERO")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									p.heal(value);
								}
							}
						}
					}
					for (Player p: game.players) {
						p.removeDead();
					}
					game.sendClientInfo();
					game.checkDeadHeroes();
				} else if (spellClass.equals("GainManaSpell")) {
					int value = 1;
					if (spell.has("value")) {
						value = spell.getInt("value");
					}
					this.turnMana += value;
					if (this.turnMana > 10) {
						this.turnMana = 10;
					}
				} else if (spellClass.equals("TransformMinionSpell")) {
					String card_name = "";
					String targetSelection = "";
					if (spell.has("card")) {
						card_name = toTitleCase(spell.getString("card").substring("token_".length()).replace("_", " "));
					}
					if (cmd.has("targetSelection")) {
						targetSelection = cmd.getString("targetSelection");
					}
					if ((!targetSelection.equals("NONE")) || targetSelection.startsWith("CUSTOM:")) {
						if (!targetSelection.startsWith("CUSTOM:")) {
							sendMessage(ServerCommands.GAME_GET_TARGET_PREFIX + targetSelection);
						}
						try {
							String response = "";
							if (!targetSelection.startsWith("CUSTOM:")) {
								response = in.readLine();
								selectedTarget = response;
							} else {
								response = targetSelection.substring("CUSTOM:".length());
								selectedTarget = response;
							}
							boolean isFriendly = response.startsWith("F");
							int selected_target = Integer.valueOf(response.substring(1));
							if (selected_target == 9999) {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {

									}
								}
							} else {
								//TODO: Work on when minion with aura transforms, remove aura effects.
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										for (int i = 0; i < p.board.size(); i++) {
											if (i == selected_target) {
												Card c = new Card(card_name, "MINION");
												//p.board.set(i, new Minion(c.getName(), c.getAttack(), c.getHealth(), c.getDeathRattle(), c.hasTaunt(), c.getDivineShield()));
												Minion o_m = p.board.get(i);
												Minion n_m = new Minion(c, this.game);
//												if (this.getAuraMinions().contains(o_m)) {
//													System.out.println("Killed an aura minion.");
//													int index = this.getAuraMinions().indexOf(o_m);
//													for (Player pl: game.players) {
//														for (int j = 0; j < p.board.size(); j++) {
//															if (isValidTargetOfAura(auras.get(index), pl, j, o_m)) {
//																deapplyAura(auras.get(index), pl, j);
//															}
//														}
//													}
//													this.aura_sources.remove(index);
//													this.auras.remove(index);
//												}
												p.board.get(i).setHealth(0);
												for (Player pl: game.players) {
													pl.removeDead();
												}
												p.board.add(i, n_m);
												game.sendClientInfo();
												game.checkDeadHeroes();
											}
										}
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else if (spellClass.equals("BuffSpell")) {
					int attackBonus = 0;
					int hpBonus = 0;
					String target = "";
					String targetSelection = "";
					if (spell.has("attackBonus")) {
						attackBonus = spell.getInt("attackBonus");
					}
					if (spell.has("hpBonus")) {
						if (spell.get("hpBonus").getClass().getSimpleName().equals("Integer")) {
							hpBonus = spell.getInt("hpBonus");
						}
					}
					if (cmd.has("targetSelection")) {
						targetSelection = cmd.getString("targetSelection");
					}
					if (spell.has("target")) {
						target = spell.getString("target");
					}
					if ((!targetSelection.equals("NONE") && !targetSelection.equals("")) || targetSelection.startsWith("CUSTOM:")) {
						if (!targetSelection.startsWith("CUSTOM:")) {
							sendMessage(ServerCommands.GAME_GET_TARGET_PREFIX + targetSelection);
						}
						try {
							String response = "";
							if (!targetSelection.startsWith("CUSTOM:")) {
								response = in.readLine();
								selectedTarget = response;
							} else {
								response = targetSelection.substring("CUSTOM:".length());
								selectedTarget = response;
							}
							boolean isFriendly = response.startsWith("F");
							int selected_target = Integer.valueOf(response.substring(1));
							if (selected_target == 9999) {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {

									}
								}
							} else {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										for (int i = 0; i < p.board.size(); i++) {
											if (i == selected_target) {
												hpBonus = getValue(spell, "hpBonus", p.board.get(i));
												System.out.println(hpBonus);
												p.board.get(i).setMaxHealth(p.board.get(i).getMaxHealth() + hpBonus);
												p.board.get(i).setHealth(p.board.get(i).getHealth() + hpBonus);
												p.board.get(i).setAttack(p.board.get(i).getAttack() + attackBonus);
											}
										}
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (!target.equals("")) {
						//System.out.println("NON TARGETING DAMAGE SPELL");
						if (target.equals("ALL_OTHER_CHARACTERS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
									if ( !(i == pos && p.username.equals(this.username)) ) {
										p.board.get(i).setMaxHealth(p.board.get(i).getMaxHealth() + hpBonus);
										p.board.get(i).setHealth(p.board.get(i).getHealth() + hpBonus);
										p.board.get(i).setAttack(p.board.get(i).getAttack() + attackBonus);
									}
								}
							}
						} else if (target.equals("FRIENDLY_HERO")) {
						} else if (target.equals("ALL_CHARACTERS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
									p.board.get(i).setMaxHealth(p.board.get(i).getMaxHealth() + hpBonus);
									p.board.get(i).setHealth(p.board.get(i).getHealth() + hpBonus);
									p.board.get(i).setAttack(p.board.get(i).getAttack() + attackBonus);
								}
							}
						} else if (target.equals("ENEMY_MINIONS")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
										p.board.get(i).setMaxHealth(p.board.get(i).getMaxHealth() + hpBonus);
										p.board.get(i).setHealth(p.board.get(i).getHealth() + hpBonus);
										p.board.get(i).setAttack(p.board.get(i).getAttack() + attackBonus);
									}
								}
							}
						} else if (target.equals("ALL_MINIONS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
									p.board.get(i).setMaxHealth(p.board.get(i).getMaxHealth() + hpBonus);
									p.board.get(i).setHealth(p.board.get(i).getHealth() + hpBonus);
									p.board.get(i).setAttack(p.board.get(i).getAttack() + attackBonus);
								}
							}
						} else if (target.equals("FRIENDLY_CHARACTERS")) {
							for (Player p: game.players) {
								if (p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
										p.board.get(i).setMaxHealth(p.board.get(i).getMaxHealth() + hpBonus);
										p.board.get(i).setHealth(p.board.get(i).getHealth() + hpBonus);
										p.board.get(i).setAttack(p.board.get(i).getAttack() + attackBonus);
									}
								}
							}
						} else if (target.equals("ENEMY_CHARACTERS")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
										p.board.get(i).setMaxHealth(p.board.get(i).getMaxHealth() + hpBonus);
										p.board.get(i).setHealth(p.board.get(i).getHealth() + hpBonus);
										p.board.get(i).setAttack(p.board.get(i).getAttack() + attackBonus);
									}
								}
							}
						} else if (target.equals("ENEMY_HERO")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
								}
							}
						} else if (target.equals("SELF")) {
							this.board.get(pos).setMaxHealth(this.board.get(pos).getMaxHealth() + hpBonus);
							this.board.get(pos).setHealth(this.board.get(pos).getHealth() + hpBonus);
							this.board.get(pos).setAttack(this.board.get(pos).getAttack() + attackBonus);
						}
					}
					for (Player p: game.players) {
						p.removeDead();
					}
					game.sendClientInfo();
					game.checkDeadHeroes();
				} else if (spellClass.equals("MetaSpell")) {
					ArrayList<JSONObject> spells = new ArrayList<>();
					if (spell.has("spells")) {
						for (Object o: spell.getJSONArray("spells")) {
							spells.add(((JSONObject)(o)));
						}
					}
					String targetSelectior = "";
					for (JSONObject s: spells) {
						JSONObject obj = new JSONObject();
						obj.put("spell", s);
						if (cmd.has("targetSelection") && targetSelectior.equals("")) {
							obj.put("targetSelection", cmd.getString("targetSelection"));
						} else {
							obj.put("targetSelection", "CUSTOM:" + targetSelectior);
						}
						targetSelectior = executeCommands(obj, pos);
					}
				} else if (spellClass.equals("SetAttackSpell")) {
					String targetSelection = "";
					if (cmd.has("targetSelection")) {
						targetSelection = cmd.getString("targetSelection");
					}
					int value = 0;
					if (spell.has("value")) {
						value = spell.getInt("value");
					}
					if (!targetSelection.startsWith("CUSTOM:")) {
						sendMessage(ServerCommands.GAME_GET_TARGET_PREFIX + targetSelection);
					}
					try {
						String response = "";
						if (!targetSelection.startsWith("CUSTOM:")) {
							response = in.readLine();
							selectedTarget = response;
						} else {
							response = targetSelection.substring("CUSTOM:".length());
							selectedTarget = response;
							}
						boolean isFriendly = response.startsWith("F");
						int selected_target = Integer.valueOf(response.substring(1));
						if (selected_target == 9999) {
							for (Player p : game.players) {
								if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {

								}
							}
						} else {
							for (Player p : game.players) {
								if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
									for (int i = 0; i < p.board.size(); i++) {
										if (i == selected_target) {
											p.board.get(i).setAttack(value);
										}
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					for (Player p: game.players) {
						p.removeDead();
					}
					game.sendClientInfo();
					game.checkDeadHeroes();
				} else if (spellClass.equals("SetHpSpell")) {
					String targetSelection = "";
					if (cmd.has("targetSelection")) {
						targetSelection = cmd.getString("targetSelection");
					}
					int value = 0;
					if (spell.has("value")) {
						value = spell.getInt("value");
					}
					if (!targetSelection.startsWith("CUSTOM:")) {
						sendMessage(ServerCommands.GAME_GET_TARGET_PREFIX + targetSelection);
					}
					try {
						String response = "";
						if (!targetSelection.startsWith("CUSTOM:")) {
							response = in.readLine();
							selectedTarget = response;
						} else {
							response = targetSelection.substring("CUSTOM:".length());
							selectedTarget = response;
						}
						boolean isFriendly = response.startsWith("F");
						int selected_target = Integer.valueOf(response.substring(1));
						if (selected_target == 9999) {
							for (Player p : game.players) {
								if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {

								}
							}
						} else {
							for (Player p : game.players) {
								if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
									for (int i = 0; i < p.board.size(); i++) {
										if (i == selected_target) {
											p.board.get(i).setHealth(value);
											p.board.get(i).setMaxHealth(value);
										}
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					for (Player p: game.players) {
						p.removeDead();
					}
					game.sendClientInfo();
					game.checkDeadHeroes();
				} else if (spellClass.equals("DestroySpell")) {
					String targetSelection = "";
					String target = "";
					if (cmd.has("targetSelection")) {
						targetSelection = cmd.getString("targetSelection");
					}
					if (spell.has("target")) {
						target = spell.getString("target");
					}
					if ((!targetSelection.equals("NONE") && !targetSelection.equals("")) || targetSelection.startsWith("CUSTOM:")) {
						if (!targetSelection.startsWith("CUSTOM:")) {
							sendMessage(ServerCommands.GAME_GET_TARGET_PREFIX + targetSelection);
						}
						try {
							String response = "";
							if (!targetSelection.startsWith("CUSTOM:")) {
								response = in.readLine();
								selectedTarget = response;
							} else {
								response = targetSelection.substring("CUSTOM:".length());
								selectedTarget = response;
							}
							boolean isFriendly = response.startsWith("F");
							int selected_target = Integer.valueOf(response.substring(1));
							if (selected_target == 9999) {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
									}
								}
							} else {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										for (int i = 0; i < p.board.size(); i++) {
											if (i == selected_target) {
												p.board.get(i).setHealth(0);
											}
										}
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (!target.equals("")) {
						System.out.println("NON TARGETING DAMAGE SPELL");
						if (target.equals("ALL_OTHER_CHARACTERS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
									if ( !(i == pos && p.username.equals(this.username)) ) {
									}
								}
							}
						} else if (target.equals("FRIENDLY_HERO")) {
						} else if (target.equals("ALL_CHARACTERS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
								}
							}
						} else if (target.equals("ENEMY_MINIONS")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
									}
								}
							}
						} else if (target.equals("ALL_MINIONS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
								}
							}
						} else if (target.equals("FRIENDLY_CHARACTERS")) {
							for (Player p: game.players) {
								if (p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
									}
								}
							}
						} else if (target.equals("ENEMY_CHARACTERS")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
									}
								}
							}
						} else if (target.equals("ENEMY_HERO")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
								}
							}
						} else if (target.equals("ENEMY_WEAPON")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									p.destroyWeapon();
								}
							}
						}
					}
					for (Player p: game.players) {
						p.removeDead();
					}
					game.sendClientInfo();
					game.checkDeadHeroes();
				} else if (spellClass.equals("AddAttributeSpell")) {
					String attribute = "";
					if (spell.has("attribute")) {
						attribute = spell.getString("attribute");
					}
					String targetSelection = "";
					String target = "";
					if (cmd.has("targetSelection")) {
						targetSelection = cmd.getString("targetSelection");
					}
					if (spell.has("target")) {
						target = spell.getString("target");
					}
					if ((!targetSelection.equals("NONE") && !targetSelection.equals("")) || targetSelection.startsWith("CUSTOM:")) {
						if (!targetSelection.startsWith("CUSTOM:")) {
							sendMessage(ServerCommands.GAME_GET_TARGET_PREFIX + targetSelection);
						}
						try {
							String response = "";
							if (!targetSelection.startsWith("CUSTOM:")) {
								response = in.readLine();
								selectedTarget = response;
							} else {
								response = targetSelection.substring("CUSTOM:".length());
								selectedTarget = response;
							}
							boolean isFriendly = response.startsWith("F");
							int selected_target = Integer.valueOf(response.substring(1));
							if (selected_target == 9999) {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
									}
								}
							} else {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										for (int i = 0; i < p.board.size(); i++) {
											if (i == selected_target) {
												p.board.get(i).addAttribute(attribute);
											}
										}
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (!target.equals("")) {
						System.out.println("NON TARGETING DAMAGE SPELL");
						if (target.equals("ALL_OTHER_CHARACTERS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
									if ( !(i == pos && p.username.equals(this.username)) ) {
									}
								}
							}
						} else if (target.equals("FRIENDLY_HERO")) {
						} else if (target.equals("ALL_CHARACTERS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
								}
							}
						} else if (target.equals("ENEMY_MINIONS")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
										p.board.get(i).addAttribute(attribute);
									}
								}
							}
						} else if (target.equals("ALL_MINIONS")) {
							for (Player p : game.players) {
								for (int i = 0; i < p.board.size(); i++) {
									p.board.get(i).addAttribute(attribute);
								}
							}
						} else if (target.equals("FRIENDLY_MINIONS")){
							for (Player p: game.players) {
								if (p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
										p.board.get(i).addAttribute(attribute);
									}
								}
							}
						} else if (target.equals("FRIENDLY_CHARACTERS")) {
							for (Player p: game.players) {
								if (p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
									}
								}
							}
						} else if (target.equals("ENEMY_CHARACTERS")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
									}
								}
							}
						} else if (target.equals("ENEMY_HERO")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
								}
							}
						}
					}
					for (Player p: game.players) {
						p.removeDead();
					}
					game.sendClientInfo();
					game.checkDeadHeroes();
				} else if (spellClass.equals("SummonRandomSpell")) {
					String card = "";
					if (spell.has("cards")) {
						int rand = (int)(Math.random() * spell.getJSONArray("cards").length());
						for (int i = 0; i < spell.getJSONArray("cards").length(); i++) {
							if (i == rand) {
								card = spell.getJSONArray("cards").getString(i);
							}
						}
					}
					System.out.println("CARD: " + card);
					if (!card.equals("")) {
						if (card.startsWith("token_")) {
							card = toTitleCase(card.substring("token_".length()).replace("_", " "));
						} else if (card.startsWith("minion_")) {
							card = toTitleCase(card.substring("minion_".length()).replace("_", " "));
						}
						Card c = new Card(card, "MINION");
						//Minion m = new Minion(c.getName(), c.getAttack(), c.getHealth(), c.getDeathRattle(), c.hasTaunt(), c.getDivineShield());
						Minion m = new Minion(c, this.game);
						if (board.size() < 7) {
							board.add(m);
							if (c.hasCharge()) {
								m.setReady(true);
							}
							for (Player p: game.players) {
								for (int i = 0; i < p.getAuras().size(); i++) {
									if (p.isValidTargetOfAura(p.getAuras().get(i), this, board.indexOf(m), p.getAuraMinions().get(i))) {
										System.out.println("WORKING WITH " + p.getAuras().get(i).getString("class"));
										applyAura(p.getAuras().get(i), this, board.indexOf(m));
									}
								}
							}
							if (c.hasAura()) {
								auras.add(c.getAura());
								aura_sources.add(m);
								for (Player p: game.players) {
									for (int i = 0; i < p.board.size(); i++) {
										if (isValidTargetOfAura(c.getAura(), p, i, m)) {
											applyAura(c.getAura(), p, i);
										}
									}
								}
							}
						}
					}
				} else if (spellClass.equals("MindControlSpell")) {
					String targetSelection = "";
					if (cmd.has("targetSelection")) {
						targetSelection = cmd.getString("targetSelection");
					}
					if ((!targetSelection.equals("NONE") && !targetSelection.equals("")) || targetSelection.startsWith("CUSTOM:")) {
						if (!targetSelection.startsWith("CUSTOM:")) {
							sendMessage(ServerCommands.GAME_GET_TARGET_PREFIX + targetSelection);
						}
						try {
							String response = "";
							if (!targetSelection.startsWith("CUSTOM:")) {
								response = in.readLine();
								selectedTarget = response;
							} else {
								response = targetSelection.substring("CUSTOM:".length());
								selectedTarget = response;
							}
							boolean isFriendly = response.startsWith("F");
							int selected_target = Integer.valueOf(response.substring(1));
							if (selected_target == 9999) {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
									}
								}
							} else {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										for (int i = 0; i < p.board.size(); i++) {
											if (i == selected_target) {
												Minion m = p.board.remove(i);
												if (this.board.size() < 7) {
													this.board.add(m);
												}
											}
										}
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else if (spellClass.equals("BuffHeroSpell")) {
					String target = "";
					int attack_bonus = 0;
					int armor_bonus = 0;
					if (spell.has("target")) {
						target = spell.getString("target");
					}
					if (spell.has("attackBonus")) {
						attack_bonus = spell.getInt("attackBonus");
					}
					if (spell.has("armorBonus")) {
						armor_bonus = spell.getInt("armorBonus");
					}
					this.addArmor(armor_bonus);
				} else if (spellClass.equals("BuffWeaponSpell")) {
					String target = "";
					int attackBonus = 0;
					if (spell.has("target")) {
						target = spell.getString("target");
					}
					if (spell.has("attackBonus")) {
						attackBonus = spell.getInt("attackBonus");
					}
					if (target.equals("FRIENDLY_WEAPON")) {
						if (this.hasWeapon()) {
							this.weapon.setAttack(this.weapon.getAttack() + attackBonus);
						}
					}
				} else if (spellClass.equals("ReturnMinionToHandSpell")) {
					String targetSelection = "";
					String target = "";
					if (cmd.has("targetSelection")) {
						targetSelection = cmd.getString("targetSelection");
					}
					if (spell.has("target")) {
						target = spell.getString("target");
					}
					if ((!targetSelection.equals("NONE") && !targetSelection.equals("")) || targetSelection.startsWith("CUSTOM:")) {
						if (!targetSelection.startsWith("CUSTOM:")) {
							sendMessage(ServerCommands.GAME_GET_TARGET_PREFIX + targetSelection);
						}
						try {
							String response = "";
							if (!targetSelection.startsWith("CUSTOM:")) {
								response = in.readLine();
								selectedTarget = response;
							} else {
								response = targetSelection.substring("CUSTOM:".length());
								selectedTarget = response;
							}
							boolean isFriendly = response.startsWith("F");
							int selected_target = Integer.valueOf(response.substring(1));
							if (selected_target == 9999) {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
									}
								}
							} else {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										for (int i = 0; i < p.board.size(); i++) {
											if (i == selected_target) {
												Minion m = p.board.remove(i);
												if (p.hand.size() < 10) {
													p.hand.add(new Card(m.getName(), "MINION"));
												}
											}
										}
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (!target.equals("")) {
						if (target.equals("ALL_OTHER_CHARACTERS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
									if ( !(i == pos && p.username.equals(this.username)) ) {
									}
								}
							}
						} else if (target.equals("FRIENDLY_HERO")) {
						} else if (target.equals("ALL_CHARACTERS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
								}
							}
						} else if (target.equals("ENEMY_MINIONS")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									Iterator<Minion> minions = p.board.iterator();
									while (minions.hasNext()) {
										Minion m = minions.next();
										minions.remove();
										if (p.hand.size() < 10) {
											p.hand.add(new Card(m.getName(), "MINION"));
										}
									}
								}
							}
						} else  if (target.equals("ALL_MINIONS")) {
							for (Player p: game.players) {
								for (int i = 0; i < p.board.size(); i++) {
									Iterator<Minion> minions = p.board.iterator();
									while (minions.hasNext()) {
										Minion m = minions.next();
										minions.remove();
										if (p.hand.size() < 10) {
											p.hand.add(new Card(m.getName(), "MINION"));
										}
									}
								}
							}
						} else if (target.equals("FRIENDLY_CHARACTERS")) {
							for (Player p: game.players) {
								if (p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
									}
								}
							}
						} else if (target.equals("ENEMY_CHARACTERS")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
									for (int i = 0; i < p.board.size(); i++) {
									}
								}
							}
						} else if (target.equals("ENEMY_HERO")) {
							for (Player p: game.players) {
								if (!p.username.equals(this.username)) {
								}
							}
						}
					}
					for (Player p: game.players) {
						p.removeDead();
					}
					game.sendClientInfo();
					game.checkDeadHeroes();
				} else if (spellClass.equals("CopyCardSpell")) {
					int value = 1;
					String cardLocation = "";
					if (spell.has("value")) {
						if (pos >= 0) {
							value = getValue(spell, "value", board.get(pos));
						} else {
							value = getValue(spell, "value", null);
						}
					}
					if (spell.has("cardLocation")) {
						cardLocation = spell.getString("cardLocation");
					}
					for (Player p : game.players) {
						if (!p.username.equals(this.username)) {
							for (int i = 0; i < value; i++) {
								if (cardLocation.equals("HAND")) {
									if (this.hand.size() < 10) {
										int random = (int) (Math.random() * p.hand.size());
										this.hand.add(p.hand.get(random));
									}
								}
							}
						}
					}
					for (Player p : game.players) {
						p.removeDead();
					}
					game.sendClientInfo();
					game.checkDeadHeroes();
				} else if (spellClass.equals("SwipeSpell")) {
					String targetSelection = "";
					String target = "";
					if (cmd.has("targetSelection")) {
						targetSelection = cmd.getString("targetSelection");
					}
					if ((!targetSelection.equals("NONE") && !targetSelection.equals("")) || targetSelection.startsWith("CUSTOM:")) {
						if (!targetSelection.startsWith("CUSTOM:")) {
							sendMessage(ServerCommands.GAME_GET_TARGET_PREFIX + targetSelection);
						}
						try {
							String response = "";
							String secondaryTarget = "";
							int value = 0;
							int secondaryValue = 0;
							if (!targetSelection.startsWith("CUSTOM:")) {
								response = in.readLine();
								selectedTarget = response;
							} else {
								response = targetSelection.substring("CUSTOM:".length());
								selectedTarget = response;
							}
							if (spell.has("secondaryTarget")) {
								secondaryTarget = spell.getString("secondaryTarget");
							}
							if (spell.has("value")) {
								value = spell.getInt("value");
							}
							if (spell.has("secondaryValue")) {
								secondaryValue = spell.getInt("secondaryValue");
							}
							boolean isFriendly = response.startsWith("F");
							int selected_target = Integer.valueOf(response.substring(1));
							if (selected_target == 9999) {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										p.dealDamage(value);
										for (int i = 0; i < p.board.size(); i++) {
											p.board.get(i).takeDamage(secondaryValue);
										}
									}
								}
							} else {
								for (Player p : game.players) {
									if ((p.username.equals(this.username) && isFriendly) || ((!p.username.equals(this.username) && !isFriendly))) {
										p.dealDamage(secondaryValue);
										for (int i = 0; i < p.board.size(); i++) {
											if (i == selected_target) {

											} else {

											}
										}
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						for (Player p: game.players) {
							p.removeDead();
						}
						game.checkDeadHeroes();
						game.sendClientInfo();
					}
				}
			}
		}
		return selectedTarget;
	}

	public void removeDead() {
		Iterator<Minion> iter = board.iterator();
		if (this.weapon.getDurability() == 0 && this.weapon.getAttack() > 0) {
			this.weapon = new Weapon();
		}
		while (iter.hasNext()) {
			Minion m = iter.next();
			if (m.getHealth() <= 0){
				if (this.getAuraMinions().contains(m)) {
					System.out.println("Killed an aura minion.");
					int index = this.getAuraMinions().indexOf(m);
					for (Player p: game.players) {
						for (int i = 0; i < p.board.size(); i++) {
							if (isValidTargetOfAura(auras.get(index), p, i, m)) {
								deapplyAura(auras.get(index), p, i);
							}
						}
					}
					this.aura_sources.remove(index);
					this.auras.remove(index);
				}
				iter.remove();
				//System.out.println(m.getName() + " IS DEAD");
			}
		}
//		for (Minion m: board){
//			System.out.println("CHECKING IF " + m.getName() + " IS DEAD");
//			if (m.getHealth() <= 0){
////				try {
////					this.executeCommands(getCommands(board.remove(i).getDeathRattle()));
////				} catch (ParseException e) {
////					e.printStackTrace();
////				}
//				board.remove(m);
//				System.out.println(m.getName() + " IS DEAD");
//			}
//		}
	}
	
	public void dealDamage(int a){
		if (this.getArmor() > 0) {
			if (a > this.getArmor()) {
				this.health -= a - this.getArmor();
				this.setArmor(0);
			} else {
				this.removeArmor(a);
			}
		} else {
			this.health -= a;
		}
	}

	public void heal (int value){
		if (this.health < this.maxHealth && value != 0) {
			HashMap<String, Object> arguments = new HashMap<>();
			arguments.put("targetEntityType", "HERO");
			try {
				game.trigger("HealingTrigger", arguments);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		this.health += value;
		if (this.health > this.maxHealth) {
			this.health = this.maxHealth;
		}
	}

	public boolean isDead(){
		return this.health <= 0;
	}

	public void trigger(String triggerClass, HashMap<String, Object> arguments) throws org.json.simple.parser.ParseException{
		System.out.println("TRIGGER CLASS: " + triggerClass);
		for (int i = 0; i < this.board.size(); i++) {
			Minion m = this.board.get(i);
			if (m.hasTriggerEvent(triggerClass)) {
				// TODO: check all arguments then execute the spell.
				JSONObject trigger = new JSONObject(m.getTriggerObject().toJSONString());
				JSONObject event_trigger = null;
				JSONObject spell = null;
				if (trigger.has("eventTrigger")) {
					event_trigger = trigger.getJSONObject("eventTrigger");
				}
				if (trigger.has("spell")) {
					spell = trigger.getJSONObject("spell");
				}
				boolean stillContinue = true;
				if (event_trigger.has("targetEntityType")) {
					if (stillContinue) {
						System.out.println(event_trigger.getString("targetEntityType") + ", " + arguments.get("targetEntityType"));
						stillContinue = event_trigger.getString("targetEntityType").equals(arguments.get("targetEntityType"));
					}
				}
				if (triggerClass.equals("DamageReceivedTrigger")) {
					if (stillContinue) {
						if (event_trigger.has("hostTargetType")) {
							if (event_trigger.getString("hostTargetType").equals("IGNORE_OTHER_TARGETS")) {
								System.out.println(((Minion)(arguments.get("source_minion"))).getName() + ", " + m.getName());
								stillContinue = arguments.get("source_minion") == m;
							}
						}
					}
				}
				if (stillContinue) {
					this.executeCommands(trigger, this.board.indexOf(m));
				}
			}
		}
	}

	public boolean isValidTargetOfAura (JSONObject aura, Player player, int min, Minion auraSource) {
		String target = "";
		if (aura.has("target")) {
			target = aura.getString("target");
		}
		//System.out.println("TARGET OF AURA: " + target);
		if (target.equals("FRIENDLY_MINIONS")) {
			if (aura.has("filter")) {
				return player.username.equals(this.username) && player.board.get(min).meetsFilterRequirements(aura.getJSONObject("filter"));
			}
			return player.username.equals(this.username);
		}else if (target.equals("OTHER_FRIENDLY_MINIONS")) {
			if (aura.has("filter")) {
				return player.username.equals(this.username) && player.board.get(min).meetsFilterRequirements(aura.getJSONObject("filter")) && (player.board.get(min) != auraSource);
			}
			return player.username.equals(this.username) && (player.board.get(min) != auraSource);
		}
		return false;
	}

	public void applyAura(JSONObject aura, Player player, int min) {
		String auraClass = "";
		if (aura.has("class")) {
			auraClass = aura.getString("class");
		}
		if (auraClass.equals("BuffAura")) {
			int attackBonus = 0;
			int hpBonus = 0;
			if (aura.has("attackBonus")) {
				attackBonus = aura.getInt("attackBonus");
			}
			if (aura.has("hpBonus")) {
				hpBonus = aura.getInt("hpBonus");
			}
			System.out.println("ATTACK BONUS:" + attackBonus + ", HEALTH BONUS: " + hpBonus);
			game.players.get(game.players.indexOf(player)).board.get(min).setAttack(player.board.get(min).getAttack() + attackBonus);
			game.players.get(game.players.indexOf(player)).board.get(min).setMaxHealth(game.players.get(game.players.indexOf(player)).board.get(min).getMaxHealth() + hpBonus);
			game.players.get(game.players.indexOf(player)).board.get(min).setHealth(game.players.get(game.players.indexOf(player)).board.get(min).getHealth() + hpBonus);
		}
	}

	public void deapplyAura(JSONObject aura, Player player, int min) {
		String auraClass = "";
		if (aura.has("class")) {
			auraClass = aura.getString("class");
		}
		if (auraClass.equals("BuffAura")) {
			int attackBonus = 0;
			int hpBonus = 0;
			if (aura.has("attackBonus")) {
				attackBonus = aura.getInt("attackBonus");
			}
			if (aura.has("hpBonus")) {
				hpBonus = aura.getInt("hpBonus");
			}
			System.out.println("ATTACK BONUS:" + attackBonus + ", HEALTH BONUS: " + hpBonus);
			game.players.get(game.players.indexOf(player)).board.get(min).setAttack(player.board.get(min).getAttack() - attackBonus);
			game.players.get(game.players.indexOf(player)).board.get(min).setMaxHealth(game.players.get(game.players.indexOf(player)).board.get(min).getMaxHealth() - hpBonus);
			if (game.players.get(game.players.indexOf(player)).board.get(min).getHealth() <= 0) {
				game.players.get(game.players.indexOf(player)).board.get(min).setHealth(1);
			}
			game.players.get(game.players.indexOf(player)).board.get(min).setHealth(game.players.get(game.players.indexOf(player)).board.get(min).getHealth() - hpBonus);
		}
	}

	public ArrayList<JSONObject> getAuras() {
		return this.auras;
	}

	public ArrayList<Minion> getAuraMinions() {
		return this.aura_sources;
	}

	public int getSpellDamage() {
		int sd = 0;
		for (Minion m: this.board) {
			sd += m.getSpellDamage();
		}
		return sd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getArmor () {
		return armor;
	}

	public void setArmor (int ar) {
		this.armor = ar;
	}

	public void addArmor(int a) {
		this.armor += a;
	}

	public void removeArmor(int a) {
		this.armor -= a;
	}

	public boolean hasWeapon () { return !(this.weapon.getAttack() == 0 && this.weapon.getDurability() == 0); }

	public Weapon getWeapon () { return this.weapon; }

	public void destroyWeapon () { this.weapon = new Weapon(); }

	public boolean hasWindfury () { // TODO: give heroes windfury
		return this.hasWindfury;
	}

	public int getNumberOfAttacksThisTurn () {
		return this.numberOfAttacksThisTurn;
	}

	public void addAttackThisTurn () {
		this.numberOfAttacksThisTurn += 1;
	}

	public void resetNumberOfAttacksThisTurn () {
		this.numberOfAttacksThisTurn = 0;
	}

	public boolean isReady() { return this.canAttack; }
	public void setReady (boolean sr) { this.canAttack = sr; }
}