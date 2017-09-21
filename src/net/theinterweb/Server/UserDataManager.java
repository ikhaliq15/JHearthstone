package net.theinterweb.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

public class UserDataManager {
	private File user_data;
	
	public UserDataManager(File f){
		this.user_data = f;
	}
	
	public void createUser(String u, String p){
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data"); 
			JSONObject new_user = new JSONObject();
			new_user.put("username", u);
			new_user.put("password", p);
			new_user.put("numwins", 0);
			new_user.put("deck", new JSONArray());
			new_user.put("packs", 5);
			new_user.put("gold", 500);
			new_user.put("dust", 400);
			new_user.put("collection", createStartingCollection());
			users.put(new_user);
			try (FileWriter file = new FileWriter(this.user_data)) {
				file.write(jsonObject.toString(4));
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	}
	
	public ArrayList<Card> getDeck(String u) throws ParseException {
		ArrayList<Card> d = new ArrayList<Card>();
		System.out.println("GETTING DECK FOR " + u.toUpperCase());
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users) {
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					JSONArray deck = user.getJSONArray("deck");
					for (Object o: deck){
						String card_name = ((String)(o));
						d.add(new Card(card_name));
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	public int addWin(String u) {
		int num_wins = 0;
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users){
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					user.put("numwins", (Integer)(user.get("numwins")) + 1);
					num_wins = (Integer)(user.get("numwins"));
					user.put("gold", (Integer)(user.get("gold")) + 50);
				}
			}
			try (FileWriter file = new FileWriter(this.user_data)) {
				file.write(jsonObject.toString(4));
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		return num_wins;
	}
	
	public boolean isValidLogin(String u, String p){
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data); 
			JSONArray users = (JSONArray) jsonObject.get("user_data"); 
			for (int i = 0; i < users.length(); i++){
				JSONObject user = (JSONObject) users.get(i);
				if(user.get("username").equals(u) && user.get("password").equals(p)){
					return true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		return false;
	}
	
	public boolean hasUser(String u){
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data); 
			JSONArray users = (JSONArray) jsonObject.get("user_data"); 
			for (int i = 0; i < users.length(); i++){
				JSONObject user = (JSONObject) users.get(i);
				if(user.get("username").equals(u)){
					return true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		return false;
	}
	
	private JSONObject parseJSONFile(File f) throws JSONException, IOException {
		String content = new String(Files.readAllBytes(Paths.get(f.getPath())));
		return new JSONObject(content);
	}
	
	public int getPacks(String u) {
		int packs = 0;
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users) {
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					packs = user.getInt("packs");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return packs;
	}
	
	public int getGold(String u) {
		int gold = 0;
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users) {
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					gold = user.getInt("gold");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gold;
	}

	public void saveDeck(ArrayList<String> d, String u) {
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users){
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					JSONArray deck = new JSONArray();
					for (String card: d) {
						//System.out.println("SAVING: " + card);
						deck.put(card);
					}
					user.put("deck", deck);
				}
			}
			try (FileWriter file = new FileWriter(this.user_data)) {
				file.write(jsonObject.toString(4));
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	}

	public void removePack(String u) {
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users){
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					user.put("packs", (Integer)(user.get("packs")) - 1);
				}
			}
			try (FileWriter file = new FileWriter(this.user_data)) {
				file.write(jsonObject.toString(4));
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	}

	public void buyPack(String u) {
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users){
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					user.put("packs", (Integer)(user.get("packs")) + 1);
					user.put("gold", (Integer)(user.get("gold")) - 100);
				}
			}
			try (FileWriter file = new FileWriter(this.user_data)) {
				file.write(jsonObject.toString(4));
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
	}
	
	public void setUpCollection() {
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users){
				JSONObject user = ((JSONObject)(e));
				JSONObject collection = new JSONObject();
				for (String card: JSonReader.getCollection()) {
					System.out.println("ADDING CARD: " + card);
					Card c = new Card(card);
					if (!c.getType().equals("hero")) {
						if (c.getSet().equals("BASIC")) {
							collection.put(card, 2);
						} else {
							collection.put(card, 0);
						}
					}
				}
				user.put("collection", collection);
			}
			try (FileWriter file = new FileWriter(this.user_data)) {
				file.write(jsonObject.toString(4));
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}
	
	private JSONObject createStartingCollection() {
		try {
			JSONObject collection = new JSONObject();
			for (String card: JSonReader.getCollection()) {
				Card c = new Card(card);
				if (!c.getType().equals("hero")) {
					if (c.getSet().equals("BASIC")) {
						collection.put(card, 2);
					} else {
						collection.put(card, 0);
					}
				}
			}
			return collection;
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public ArrayList<String> getCollection(String u) {
		ArrayList<String> d = new ArrayList<String>();
		System.out.println("GETTING COLLECTION FOR " + u.toUpperCase());
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users) {
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					JSONObject collection = user.getJSONObject("collection");
					for (Object o: collection.keySet()){
						String card_name = ((String)(o));
						for (int i = 0; i < collection.getInt(card_name); i++){
							d.add(card_name);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return d;
	}

	public ArrayList<String> getWholeCollection(String u) {
		ArrayList<String> data = new ArrayList<String>();
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users) {
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					JSONObject collection = user.getJSONObject("collection");
					for (Object o: collection.keySet()){
						String card_name = ((String)(o));
						String r = JSonReader.getRarity(card_name);
						data.add(card_name + " x " + collection.getInt(card_name) + " - " + String.valueOf(r.charAt(0)).toUpperCase() + r.substring(1).toLowerCase());
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public String getDust(String u) {
		int dust = 0;
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users) {
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					dust = user.getInt("dust");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return String.valueOf(dust);
	}

	public boolean hasCard(String name, String u) {
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users) {
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					JSONObject collection = user.getJSONObject("collection");
					for (Object o: collection.keySet()){
						String card_name = ((String)(o));
						if (card_name.equals(name)) {
							if (collection.getInt(card_name) > 0) {
								return true;
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void dustCard(String name, String u) {
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users) {
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					JSONObject collection = user.getJSONObject("collection");
					for (Object o: collection.keySet()){
						String card_name = ((String)(o));
						if (card_name.equals(name)) {
							System.out.println("DUSTING: " + card_name);
							collection.put(card_name, collection.getInt(card_name) - 1);
							Card c = new Card(card_name);
							if (c.getRarity().equals("legendary")) {
								user.put("dust", user.getInt("dust") + 800);
							} else if (c.getRarity().equals("epic")) {
								user.put("dust", user.getInt("dust") + 200);
							} else if (c.getRarity().equals("rare")) {
								user.put("dust", user.getInt("dust") + 50);
							} else if (c.getRarity().equals("common")) {
								user.put("dust", user.getInt("dust") + 20);
							}
						}
					}
				}
			}
			try (FileWriter file = new FileWriter(this.user_data)) {
				file.write(jsonObject.toString(4));
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}
	
	public void craftCard(String name, String u) {
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users) {
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					JSONObject collection = user.getJSONObject("collection");
					for (Object o: collection.keySet()){
						String card_name = ((String)(o));
						if (card_name.equals(name)) {
							System.out.println("CRAFTING: " + card_name);
							collection.put(card_name, collection.getInt(card_name) + 1);
							Card c = new Card(card_name);
							if (c.getRarity().equals("legendary")) {
								user.put("dust", user.getInt("dust") - 1600);
							} else if (c.getRarity().equals("epic")) {
								user.put("dust", user.getInt("dust") - 400);
							} else if (c.getRarity().equals("rare")) {
								user.put("dust", user.getInt("dust") - 100);
							} else if (c.getRarity().equals("common")) {
								user.put("dust", user.getInt("dust") - 40);
							}
						}
					}
				}
			}
			try (FileWriter file = new FileWriter(this.user_data)) {
				file.write(jsonObject.toString(4));
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	public void addCard(String name, String u) {
		try {
			JSONObject jsonObject = parseJSONFile(this.user_data);
			JSONArray users = (JSONArray) jsonObject.get("user_data");
			for (Object e: users) {
				JSONObject user = ((JSONObject)(e));
				if (user.get("username").equals(u)) {
					JSONObject collection = user.getJSONObject("collection");
					for (Object o: collection.keySet()){
						String card_name = ((String)(o));
						if (card_name.equals(name)) {
							collection.put(card_name, collection.getInt(card_name) + 1);
						}
					}
				}
			}
			try (FileWriter file = new FileWriter(this.user_data)) {
				file.write(jsonObject.toString(4));
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}