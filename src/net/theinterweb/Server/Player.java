package net.theinterweb.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.parser.ParseException;

public class Player {

	private BufferedReader in;
    private PrintWriter out;
    
    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<Card> hand = new ArrayList<Card>();
    private ArrayList<Minion> board = new ArrayList<Minion>();
    
    private int fatigue = 0;
    
    private int health = 30;
    
    private int turnMana = 0;
    private int manacrystals = 0;
    private int overloaded_crystals = 0;
    
    private Game game;
    
	public Player(Socket accept, ArrayList<Card> d, Game g) throws IOException {
		game = g;
		in = new BufferedReader(new InputStreamReader(
                accept.getInputStream()));
		out = new PrintWriter(accept.getOutputStream(), true);
		for (int i = 0; i < d.size(); i ++){
			deck.add(d.get(i));
		}
		this.drawCard(3);
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
		for (int i = 0; i < board.size(); i++){
			board.get(i).setReady(true);
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
		if (health < 10 && health > 0){
			return "0" + health;
		}
		return "" + health;
	}
	
	private void drawCard(int num){
		for(int i = 0; i < num; i++){
			if (deck.size() > 0){
				if (hand.size() >= 10){
					this.sendMessage("ERROR:BURNED" + deck.remove((int) (Math.random() * (deck.size()))).getName());
				}else{
					hand.add(deck.remove((int) (Math.random() * (deck.size()))));
				}
			}else{
				fatigue++;
				health -= fatigue;
				this.sendMessage("ERROR:FATIGUE" + fatigue);
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
        		//System.out.println("Line: " + msg);
        		if (msg.equals("END")){
        			break;
        		}else if(msg.startsWith("PLAY:") && Integer.valueOf(msg.substring("PLAY:".length())) < hand.size()){
        			int minion = Integer.valueOf(msg.substring("PLAY:".length()));
        			Card t = hand.get(minion);
        			if (t.getCost() <= turnMana){
        				Minion m = new Minion(t.getName(), t.getAttack(), t.getHealth(), t.getDeathRattle());
        				try {
							this.executeCommands(getCommands(t.getBattleCry()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
        				try {
        					this.executeCommands(getCommands(t.getOverload()));
        				} catch (ParseException e){
        					e.printStackTrace();
        				}
        				if (t.hasCharge()){
        					m.setReady(true);
        				}
        				// TODO setup system for interpreting card text to commands.
        				// i.e: Draw 2 Card --> draw(2);
        				turnMana -= t.getCost();
        				board.add(m);
        				hand.remove(minion);
        				game.sendClientInfo();
        			}else{
        				sendMessage("ERROR:MANALOW");
        			}
        		}else if(msg.startsWith("ATTACK:")){
        			msg = msg.substring("ATTACK:".length());
        			int attacker = Integer.valueOf(msg.substring(0, 1));
        			msg = msg.substring(2);
        			int target = Integer.valueOf(msg);
        			board.get(attacker).setReady(false);
        			if(target == 9999){
        				game.attackHero(board.get(attacker));
        			}else{
            			game.attack(board.get(attacker), target);
        			}
        		}
        	}
        }
	}
	
    private void executeCommands(ArrayList<String> cmds) throws ParseException {
		for (int i = 0; i < cmds.size(); i++){
			String c = cmds.get(i);
			ArrayList<String> t = split(c, ':');
			//System.out.println(t);
			if (t.get(0).equals("draw")){
				if(t.get(2).equals("card")){
					this.drawCard(Integer.valueOf(t.get(1)));
				}
			}else if (t.get(0).equals("summon")){
				for (int j = 0; j < Integer.valueOf(t.get(1)); j++){
					if(board.size() < 7){
						Card card = new Card(toTitleCase(t.get(2).trim()));
						board.add(new Minion(card.getName().trim(), card.getAttack(), card.getHealth(), card.getDeathRattle()));
					}
				}
			}else if (t.get(0).equals("deal")){
				int damage = Integer.valueOf(t.get(1));
				if(t.get(2).equals("fhero")){
					this.health -= damage;
					System.out.println("FRIENDLY HERO DAMAGE");
				}else if(t.get(2).equals("ehero")){
					game.attackHero(new Minion("s", damage, 0, ""));
					System.out.println("ENEMY HERO DAMAGE");
				}else if(t.get(2).equals("target")){
					
				}else if(t.get(2).equals("minionsenemy")){
					//game.attackAllMinion(damage);
				}else if(t.get(2).equals("allother")){
					this.health -= damage;
					game.attackHero(new Minion("s", damage, 0, ""));
					//game.attackAllMinion(damage);
					for (int j = 0; j < board.size(); j++){
						board.get(i).takeDamage(damage);
					}
				}else if(t.get(2).equals("minionall")){
					
				}else if(t.get(2).equals("all")){
					
				}else if(t.get(2).equals("minionsallother")){
					
				}
				
			}else if (t.get(0).equals("overload")){
				System.out.println("OVERLOAD!");
				overloaded_crystals += Integer.valueOf(t.get(1));
				for (int j = 0; j < board.size(); j++){
					ArrayList<ArrayList<String>> efts = new Card(board.get(j).getName()).getEffects();
					for (ArrayList<String> e: efts){
						if(e.get(0).equals("onoverload")){
							if(e.get(1).startsWith("gain")){
								String g = e.get(1).substring(5);
								board.get(i).setAttack(board.get(i).getAttack() + (Integer.valueOf(g.substring(0, 2)) * Integer.valueOf(t.get(1))));
								board.get(i).setHealth(board.get(i).getHealth() + Integer.valueOf(g.substring(2)) * Integer.valueOf(t.get(1)));
							}
						}
					}
				}
			}
		}
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

	public void removeDead() {
		for (int i = 0; i < board.size(); i++){
			if (board.get(i).getHealth() <= 0){
				try {
					this.executeCommands(getCommands(board.remove(i).getDeathRattle()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void dealDamage(int a){
		this.health -= a;
	}
	
	public boolean isDead(){
		return this.health <= 0;
	}
	
	
	
	
	
	public static ArrayList<String> getCommands(String s){
		s = s.replace('.', ',');
		ArrayList<String> cmds = new ArrayList<String>(Arrays.asList(s.split(",")));
		ArrayList<String> outputs = new ArrayList<String>();
		for (int i = 0; i < cmds.size(); i++){
			String compiledOutput = "";
			String cmd = cmds.get(i);
			cmd = cmd.trim();
			ArrayList<String> toks = new ArrayList<String>(Arrays.asList(cmd.split(" ")));
			if (toks.get(0).equals("draw")){
				compiledOutput += "draw:";
				if (toks.get(1).equals("a")){
					compiledOutput += "1";
				}else{
					compiledOutput += toks.get(1);
				}
				compiledOutput += ":";
				if(toks.get(2).equals("cards") || toks.get(2).equals("card")){
					compiledOutput += "card";
				}
			} else if (toks.get(0).equals("summon")){
				compiledOutput += "summon:";
				if (toks.get(1).equals("a")){
					compiledOutput += "1";
				}else{
					int num = inNumerals(toks.get(1));
					compiledOutput += num;
				}
				String minionName = "";
				for (int j = 3; j < toks.size(); j++){
					if(toks.get(j).equals("for")){
						break;
					}
					minionName += toks.get(j) + " ";;
				}
				if(!toks.get(1).equals("a")){
					compiledOutput += ":" + minionName.substring(0, minionName.length() - 2);
				}else{
					compiledOutput += ":" + minionName;
				}
				if(toks.contains("for")){
					if (toks.get(toks.size()-1).equals("opponent")){
						compiledOutput += ":opp";
					} else {
						compiledOutput += ":friend";
					}
				}
			} else if (toks.get(0).equals("deal")) {
				if (toks.get(2).equals("damage")){
					compiledOutput += "deal:" + toks.get(1) + ":";
					if (toks.size() == 3){
						compiledOutput += "target";
					}else if(toks.get(toks.size()-1).equals("hero")){
						if (toks.get(toks.size()-2).equals("your")){
							compiledOutput += "fhero";
						}else if(toks.get(toks.size()-2).equals("enemy")){
							compiledOutput += "ehero";
						}
					}else if(toks.get(toks.size()-1).equals("characters")){
						if (toks.get(toks.size()-2).equals("all")){
							compiledOutput += "all";
						}else if(toks.get(toks.size()-2).equals("other")){
							compiledOutput += "allother";
						}
					}else if(toks.get(toks.size()-1).equals("minions")){
						if (toks.get(toks.size()-2).equals("all")){
							compiledOutput += "minionall";
						}else if(toks.get(toks.size()-2).equals("other")){
							compiledOutput += "minonsallother";
						}else if(toks.get(toks.size()-2).equals("enemy")){
							compiledOutput += "minionsenemy";
						}
					}
				}
			} else if (toks.get(0).equals("overload:")) {
				compiledOutput += "overload:" + toks.get(1).substring(1, toks.get(1).length()-1);
			}
			outputs.add(compiledOutput);
		}
		return outputs;
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
}