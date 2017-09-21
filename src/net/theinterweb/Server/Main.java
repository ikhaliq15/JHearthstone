package net.theinterweb.Server;

import net.theinterweb.Shared.ServerCommands;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {
	
	private static ArrayList<Socket> line = new ArrayList<>();

	static UserDataManager udm = new UserDataManager(new File("user_data.json"));
	static MatchMaker m = new MatchMaker(line);

	//private static JFrame frm_Main = new JFrame("Hearthstone Server");
	
	public static void main(String args[]) throws IOException, ParseException, InterruptedException{
		//udm.setUpCollection();
//		JPanel pnl_Main = new JPanel();
//
//		JScrollPane scrollPane = new JScrollPane();
//		pnl_Main.add(scrollPane);
//
//		JTextArea jta_Output = new JTextArea(22, 50);
//		jta_Output.setEnabled(false);
//
//		PrintStream std_output = new PrintStream(new JTextAreaStream(jta_Output));
//		System.setOut(std_output);
//		System.setErr(std_output);
//		scrollPane.add(jta_Output);
//		scrollPane.setViewportView(jta_Output);
//
//		frm_Main.add(pnl_Main);
//
//		frm_Main.setSize(650, 400);
//		frm_Main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frm_Main.setLocationRelativeTo(null);
//		frm_Main.setVisible(true);


		System.out.println("Starting server!");
		ServerSocket listener = new ServerSocket(Game.PORT);
		m.start();

	    while (true) {
	    	Socket s = listener.accept();
	    	UserConnection u = new UserConnection(s, line);
	    	u.start();
	    	if (!u.isAlive()){
	    		break;
	    	}
	    }
	    listener.close();
	}

}

class UserConnection extends Thread {
	
	private Socket s;
	private ArrayList<Socket> line;
	private String user = "";
	
	UserConnection(Socket soc, ArrayList<Socket> line){
		this.s = soc;
		this.line = line;
	}
	
	public void run () {
		System.out.println("GOT A CONNECTION!");
		try {
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    	boolean logged_in = false;
	    	while (!logged_in){
	    		String input = in.readLine(); 
	    		System.out.println(input);
	    		if (input.startsWith(ServerCommands.LOGIN_PREFIX)) {
	    			input = input.substring(ServerCommands.LOGIN_PREFIX.length());
	    			String username = "";
	    			String password = "";
	    			if (input.contains(ServerCommands.LOGIN_PASSWORD_PREFIX)){
	    				username = input.substring(0, input.indexOf(ServerCommands.LOGIN_PASSWORD_PREFIX));
	    				user = username;
	    				password = input.substring(username.length() + ServerCommands.LOGIN_PASSWORD_PREFIX.length());
	    			}
	    			if (Main.udm.isValidLogin(username, password)) {
	    				out.println(ServerCommands.LOGIN_VALID_PASSWORD + ServerCommands.LOGIN_USERNAME_PREFIX + username + ServerCommands.LOGIN_RANK_PREFIX + "0");
	    				logged_in = true;
	    			} else {
	    				out.println(ServerCommands.LOGIN_INVALID_PASSWORD);
	    			}
	    		} else if (input.startsWith(ServerCommands.SIGN_UP_PREFIX + ServerCommands.SIGN_UP_USERNAME_PREFIX)) {
	    			input = input.substring(ServerCommands.SIGN_UP_PREFIX.length() + ServerCommands.SIGN_UP_USERNAME_PREFIX.length());
	    			String password = "";
	    			String username = "";
	    			if (input.contains(ServerCommands.SIGN_UP_PASSWORD_PREFIX)){
	    				username = input.substring(0, input.indexOf(ServerCommands.SIGN_UP_PASSWORD_PREFIX));
	    				password = input.substring(username.length() + ServerCommands.SIGN_UP_PASSWORD_PREFIX.length());
	    			}
	    			if (!Main.udm.hasUser(username)) {
	    				Main.udm.createUser(username, password);
	    				out.println(ServerCommands.SIGN_UP_SUCCESS);
	    			} else {
	    				out.println(ServerCommands.SIGN_UP_FAIL);
	    			}
	    		}
	    	}
	    	boolean in_game = false;
	    	Game g = null;
	    	while (true) {
	    		//System.out.println(this.user.toUpperCase() + " IS STILL RUNNING!");
	    		if (!in_game){
		    		String input = in.readLine();
		    		if (input.equals(ServerCommands.START_PAGE_JOIN_LINE)) {
		    			if (line.size() == 0) {
		    				out.println(ServerCommands.START_PAGE_WAITING);
		    			}
		    			g = Main.m.addToLine(s, user);
		    			Thread.sleep(5000);
		    			System.out.println(this.user.toUpperCase() + " IS BEING ADDED TO THE LINE.");
		    			in_game = true;
		    		} else if (input.equals(ServerCommands.MAKE_DECK_GET_DECK)) {
		    			try {
							for (Card c: Main.udm.getDeck(user)) {
								out.println(c.getName());
							}
							out.println(ServerCommands.MAKE_DECK_DECK_FINISHED);
						} catch (ParseException e) {
							e.printStackTrace();
						}
		    		} else if (input.equals(ServerCommands.MAKE_DECK_COLLECTION)) {
						ArrayList<String> collections = Main.udm.getCollection(user);
		    			for (String n: collections) {
							out.println(n);
						}
		    			out.println(ServerCommands.MAKE_DECK_COLLECTION_DONE);
		    		} else if (input.equals(ServerCommands.MAKE_DECK_SAVE)) {
		    			ArrayList<String> d = new ArrayList<>();
		    			input = "";
		    			while (!input.equals(ServerCommands.MAKE_DECK_SAVE_DONE)) {
		    				System.out.println(input);
		    				if (!input.equals("")){// && !input.equals("SAVEDECKSTART")) {
		    					d.add(input);
		    					//System.out.println(input);
		    				}
		    				input = in.readLine();
		    			}
		    			System.out.println(d);
		    			Main.udm.saveDeck(d, user);
		    		} else if (input.equals(ServerCommands.COLLECTION_GET_WHOLE)){
		    			ArrayList<String> collection_data = Main.udm.getWholeCollection(user);
		    			for (String n: collection_data) {
							out.println(n);
						}
		    			out.println(ServerCommands.COLLECTION_GET_WHOLE_DONE);
		    		} else if (input.equals(ServerCommands.START_PAGE_LOGOUT)) {
		    			break;
		    		} else if (input.equals(ServerCommands.OPEN_PACK_OPEN)) {
		    			if (Main.udm.getPacks(user) > 0) {
		    				ArrayList<String> pack = JSonReader.getARandomPack();
		    				for (String card: pack) {
		    					out.println(card + "RARITY:::" + JSonReader.getRarity(card));
		    				}
		    				for (String card: pack) {
		    					Main.udm.addCard(card, user);
		    				}
		    				out.println(ServerCommands.OPEN_PACK_OPEN_DONE);
		    				Main.udm.removePack(user);
		    			} else {
		    				out.println(ServerCommands.OPEN_PACK_NO_PACKS);
		    			}
		    		} else if (input.equals(ServerCommands.OPEN_PACK_GET_INFO)) {
		    			out.println(ServerCommands.OPEN_PACK_PACK_PREFIX + Main.udm.getPacks(user) + ServerCommands.OPEN_PACK_GOLD_PREFIX + Main.udm.getGold(user));
		    		} else if (input.equals(ServerCommands.OPEN_PACK_BUY_PACK)) {
		    			if (Main.udm.getGold(user) < 100) {
		    				out.println(ServerCommands.OPEN_PACK_NO_GOLD);
		    			} else {
		    				Main.udm.buyPack(user);
		    				out.println(ServerCommands.OPEN_PACK_BUY_SUCCESS);
		    			}
		    		} else if (input.equals(ServerCommands.COLLECTION_GET_DUST)) {
		    			out.println(Main.udm.getDust(user));
		    		} else if (input.startsWith(ServerCommands.COLLECTION_DUST_PREFIX)) {
		    			if (input.contains(" x ")) {
			    			String card_name = input.substring(ServerCommands.COLLECTION_DUST_PREFIX.length(), input.indexOf(" x "));
			    			if (!Main.udm.hasCard(card_name, user)) {
			    				out.println(ServerCommands.COLLECTION_DUST_NOT_HAVE);
			    			} else if (new Card(card_name).getSet().equals("BASIC")) {
			    				out.println(ServerCommands.COLLECTION_DUST_BASIC_ERR);
			    			} else {
				    			System.out.println(card_name);
			    				Main.udm.dustCard(card_name, user);
			    				out.println(ServerCommands.COLLECTION_DUST_SUCCESS);
			    			}
		    			} else {
		    				out.println(ServerCommands.COLLECTION_DUST_NON_SELC);
		    			}
		    		} else if (input.startsWith(ServerCommands.COLLECTION_CRAFT_PREFIX)) {
		    			if (input.contains(" x ")) {
			    			String card_name = input.substring(ServerCommands.COLLECTION_CRAFT_PREFIX.length(), input.indexOf(" x "));
			    			Card c = new Card(card_name);
			    			int dust = Integer.valueOf(Main.udm.getDust(user));
			    			if (c.getSet().equals("BASIC")) {
			    				out.println(ServerCommands.COLLECTION_CRAFT_BASIC_ERR);
			    			} else if ( (c.getRarity().equals("LEGENDARY") && dust < 1600) ||
			    						(c.getRarity().equals("EPIC") && dust < 400) ||
			    						(c.getRarity().equals("RARE") && dust < 100) ||
			    						(c.getRarity().equals("COMMON") && dust < 40) ){
			    				out.println(ServerCommands.COLLECTION_CRAFT_FAIL_DUST);
			    			} else {
				    			System.out.println(card_name);
			    				Main.udm.craftCard(card_name, user);
			    				out.println(ServerCommands.COLLECTION_CRAFT_SUCCESS);
			    			}
		    			} else {
		    				out.println(ServerCommands.COLLECTION_CRAFT_NON_SELEC);
		    			}
		    		}
	    		} else {
	    			if (!g.isAlive() && !MatchMaker.line.contains(s)) {
	    				in_game = false;
	    				System.out.println(this.user.toUpperCase() + " IS OUT OF GAME!");
	    				Iterator<Player> iter = g.players.iterator();
		    			while (iter.hasNext()) {
		    				Player p = iter.next();
		    				if (p.isDead() || g.gameIsOver) {
		    					if (!p.getUsername().equals(user)) {
		    						Main.udm.addWin(user);
		    					}
		    				}
		    			}
	    			}
	    			try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    		}
			}
		} catch (IOException | InterruptedException | ParseException e) {
			e.printStackTrace();
		}
		System.out.println(this.user.toUpperCase() + " HAS ENDED THE CONNECTION!");
	}
}