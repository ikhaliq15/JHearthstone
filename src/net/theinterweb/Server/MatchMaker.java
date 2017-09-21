package net.theinterweb.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

public class MatchMaker extends Thread{
	
	public static ArrayList<Socket> line;
	private ArrayList<String> names;
	private ArrayList<Game> games = new ArrayList<Game>();
	
	public MatchMaker (ArrayList<Socket> line) {
		MatchMaker.line = line;
		names = new ArrayList<String>();
	}
	
	public void run() {
		boolean game_over = false;
		while (!game_over) {
			if (line.size() >= 2) {
				System.out.println("GOT 2 PLAYERS!");
	    		Game g = null;
				try {
					g = new Game();
				} catch (IOException | ParseException | InterruptedException e) {
					e.printStackTrace();
				}
				games.add(g);
		    	for (int i = 0; i < 2; i++){
		    		try {
		    			String name = names.remove(0);
		    			ArrayList<Card> d = null;
						try {
							d = Main.udm.getDeck(name);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						g.players.add(new Player(line.remove(0), d, g, name));
						//g.players.add(new Player(line.remove(0), Main.udm.getDeck(name), g, name));
						//System.out.println(name + "'S DECK: " + Main.udm.getDeck(name));
					} catch (IOException e) {
						e.printStackTrace();
					}
		    		//g.sendClientInfo();
		    	}
		    	System.out.println("STARTING GAME!");
		    	g.start();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Game addToLine(Socket s, String u){
		line.add(s);
		names.add(u);
		boolean in_game = false;
		while (!in_game) {
			if (!line.contains(s)) {
				in_game = true;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return games.get(games.size()-1);
	}
}