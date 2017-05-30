package net.theinterweb.Server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSonReader {
	private String file;
	public JSonReader(String inputStream){
		file = inputStream;
	}
	
	public ArrayList<ArrayList<String>> getEffects(String n) throws org.json.simple.parser.ParseException{
		ArrayList<ArrayList<String>> effects = new ArrayList<ArrayList<String>>();	
		
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file)); 
			JSONObject jsonObject = (JSONObject) obj; 
			JSONArray cards = (JSONArray) jsonObject.get("cards"); 
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n)){
					JSONArray effect = (JSONArray) card.get("effect_list");
					for(int j = 0; j < effect.size(); j++){
						effects.add(new ArrayList<String>());
						JSONObject e = (JSONObject) effect.get(j);
						effects.get(effects.size()-1).add(e.get("effect").toString());
						effects.get(effects.size()-1).add(e.get("extra").toString());
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		
		return effects;
	}
	
	private long [] findCard(String n) throws org.json.simple.parser.ParseException{
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file)); 
			JSONObject jsonObject = (JSONObject) obj; 
			JSONArray cards = (JSONArray) jsonObject.get("cards"); 
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n)){
					long t = -1;
					if(card.get("type") != null && card.get("type").equals("minion")){
						t = 1;
					}else if(card.get("category") != null && card.get("category").equals("minion")){
						t = 1;
					}else if(card.get("type") != null && card.get("type").equals("spell")){
						t = 2;
					}else if(card.get("category") != null && card.get("category").equals("spell")){
						t = 2;
					}
					if(t == 1){
						long attack = 0;
						if (card.get("attack") == null){
							attack = 0;
							//System.out.println("CARD IS ZERO ATTACK!");
						}else{
							attack = (long) card.get("attack");
						}
						long [] info = {
							t,
							(long) card.get("mana"),
							(long) attack,
							(long) card.get("health")};
						return info;
					}else if(t == 2){
						long[] info = {
							t,
							(long) card.get("mana")};
						return info;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		return new long[0];
	}
	public long getAttack(String n) throws org.json.simple.parser.ParseException{
		long[] info = findCard(n);
		if(info[0] != 2){
			return info[2];
		}
		return -1;
	}
	public long getHealth(String n) throws org.json.simple.parser.ParseException{
		long[] info = findCard(n);
		if(info[0] != 2){
			return info[3];
		}
		return -1;
	}
	public long getCost(String n) throws org.json.simple.parser.ParseException{
		return findCard(n)[1];
	}
	public String getType(String n) throws org.json.simple.parser.ParseException{
		long type = findCard(n)[0];
		if (type == 1){
			return "minion";
		}else if(type == 2){
			return "spell";
		}
		return "null";
	}
}
