package net.theinterweb.Server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSonReader {
	
	private static ArrayList<String> collections = new ArrayList<String>();
	private static ArrayList<String> legendaries = new ArrayList<String>();
	private static ArrayList<String> epics = new ArrayList<String>();
	private static ArrayList<String> rares = new ArrayList<String>();
	private static ArrayList<String> commons = new ArrayList<String>();

	private static String file;
	public JSonReader(String inputStream){
		file = inputStream;
	}

	public ArrayList<String> getAttributes (String n) throws org.json.simple.parser.ParseException {
		ArrayList<String> attr = new ArrayList<>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n)){
					if (card.containsKey("attributes")) {
						JSONObject attributes = (JSONObject) card.get("attributes");
						for (int j = 0; j < attributes.keySet().size(); j++) {
							attr.add((String) (attributes.keySet().toArray()[j]));
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return attr;
	}

	public boolean hasAura (String n) throws org.json.simple.parser.ParseException {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n)){
					return card.containsKey("aura");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean hasAura (String n, String t) throws org.json.simple.parser.ParseException{
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n) && card.get("type").equals(t)){
					return card.containsKey("aura");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public JSONObject getAura(String n) throws org.json.simple.parser.ParseException{
		JSONObject temp = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n)){
					if (card.containsKey("aura")) {
						return (JSONObject)(card.get("aura"));
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public ArrayList<String> getAttributes (String n, String t) throws org.json.simple.parser.ParseException{
		ArrayList<String> attr = new ArrayList<>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n) && card.get("type").equals(t)){
					if (card.containsKey("attributes")) {
						JSONObject attributes = (JSONObject) card.get("attributes");
						for (int j = 0; j < attributes.keySet().size(); j++) {
							attr.add((String) (attributes.keySet().toArray()[j]));
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return attr;
	}

	public ArrayList<Object> getAttributeValues (String n, String t) throws org.json.simple.parser.ParseException{
		t = t.toUpperCase();
		ArrayList<Object> attr = new ArrayList<>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n) && card.get("type").equals(t)){
					if (card.containsKey("attributes")) {
						JSONObject attributes = (JSONObject) card.get("attributes");
						for (int j = 0; j < attributes.keySet().size(); j++) {
							attr.add(attributes.get(attributes.keySet().toArray()[j]));
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("ATTRIBUTE VALUES OF " + n.toUpperCase() + ": " + attr);
		return attr;
	}

	public ArrayList<String> getTriggers (String n, String t) throws org.json.simple.parser.ParseException{
		t = t.toUpperCase();
		ArrayList<String> triggers = new ArrayList<>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n) && card.get("type").equals(t)){
					if (card.containsKey("trigger")) {
						JSONObject trigger = (JSONObject) card.get("trigger");
						if (trigger.containsKey("eventTrigger")) {
							JSONObject eventTrigger = (JSONObject) trigger.get("eventTrigger");
							if (eventTrigger.containsKey("class")) {
								triggers.add((String)eventTrigger.get("class"));
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
		return triggers;
	}

	public String getRace (String n, String t) throws org.json.simple.parser.ParseException{
		t = t.toUpperCase();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n) && card.get("type").equals(t)){
					if (card.containsKey("race")) {
						return (String) card.get("race");
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public JSONObject getTriggerObject (String n, String t) throws org.json.simple.parser.ParseException{
		t = t.toUpperCase();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n) && card.get("type").equals(t)){
					if (card.containsKey("trigger")) {
						JSONObject trigger = (JSONObject) card.get("trigger");
						return trigger;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject getBattlecry(String n) throws org.json.simple.parser.ParseException{
		JSONObject temp = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n)){
					if (card.containsKey("battlecry")) {
						return (JSONObject)(card.get("battlecry"));
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public JSONObject getEffect(String n) throws org.json.simple.parser.ParseException{
		JSONObject temp = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n)){
					return card;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
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
					if(card.get("type") != null && card.get("type").equals("MINION")){
						t = 1;
					}else if(card.get("type") != null && card.get("type").equals("SPELL")){
						t = 2;
					}else if(card.get("type") != null && card.get("type").equals("WEAPON")){
						t = 3;
					}
					if(t == 1){
						long attack = 0;
						if (card.get("baseAttack") == null){
							attack = 0;
							//System.out.println("CARD IS ZERO ATTACK!");
						}else{
							attack = (long) card.get("baseAttack");
						}
						long [] info = {
							t,
							(long) card.get("baseManaCost"),
							attack,
							(long) card.get("baseHp")};
						return info;
					}else if(t == 2){
						long[] info = {
							t,
							(long) card.get("baseManaCost")};
						return info;
					}else if (t == 3) {
						long [] info = {
							t,
							(long) card.get("baseManaCost"),
							(long) card.get("damage"),
							(long) card.get("durability")};
						return info;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Card not found: " + n);
		return new long[0];
	}

	private long [] findCard(String n, String ty) throws org.json.simple.parser.ParseException{
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if(card.get("name").equals(n) && card.get("type").equals(ty)){
					long t = -1;
					if(card.get("type") != null && card.get("type").equals("MINION")){
						t = 1;
					}else if(card.get("type") != null && card.get("type").equals("SPELL")){
						t = 2;
					}else if(card.get("type") != null && card.get("type").equals("WEAPON")){
						t = 3;
					}
					if(t == 1){
						long attack = 0;
						if (card.get("baseAttack") == null){
							attack = 0;
							//System.out.println("CARD IS ZERO ATTACK!");
						}else{
							attack = (long) card.get("baseAttack");
						}
						long [] info = {
								t,
								(long) card.get("baseManaCost"),
								attack,
								(long) card.get("baseHp")};
						return info;
					}else if(t == 2){
						long[] info = {
								t,
								(long) card.get("baseManaCost")};
						return info;
					}else if (t == 3) {
						long [] info = {
								t,
								(long) card.get("baseManaCost"),
								(long) card.get("damage"),
								(long) card.get("durability")};
						return info;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Card not found: " + n);
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
	public long getAttack(String n, String t) throws org.json.simple.parser.ParseException{
		long[] info = findCard(n, t);
		if(info[0] != 2){
			return info[2];
		}
		return -1;
	}
	public long getHealth(String n, String t) throws org.json.simple.parser.ParseException{
		long[] info = findCard(n, t);
		if(info[0] != 2){
			return info[3];
		}
		return -1;
	}
	public long getCost(String n) throws org.json.simple.parser.ParseException{
		//return findCard(n)[1];
		file = "all-cards.json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if (card.get("name").equals(n)) {
					return (long) card.get("baseManaCost");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("COULD NOT FIND CARD: " + n);
		return -1;
	}

	public long getCost(String n, String t) throws org.json.simple.parser.ParseException{
		//return findCard(n)[1];
		file = "all-cards.json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if (card.get("name").equals(n) && card.get("type").equals(t)) {
					return (long) card.get("baseManaCost");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("COULD NOT FIND CARD: " + n);
		return -1;
	}

	public String getType(String n) throws org.json.simple.parser.ParseException{
		long type = findCard(n)[0];
		if (type == 1){
			return "minion";
		}else if(type == 2){
			return "spell";
		}else if(type == 3){
			return "weapon";
		}
		return "null";
	}

	public static ArrayList<String> getCollection() {
		if (collections.size() > 0){
			return collections;
		}
		file = "all-cards.json";
		ArrayList<String> c = new ArrayList<String>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				System.out.println(card.get("name"));
				if ((boolean)(card.get("collectible"))) {
					c.add((String) card.get("name"));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		collections = c;
		return c;
	}

	public static String getRarity(String card_name) {
		file = "all-cards.json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if (card.get("name").equals(card_name)) {
					return (String) card.get("rarity");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getRarity(String card_name, String type) {
		file = "all-cards.json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if (card.get("name").equals(card_name) && card.get("type").equals(type)) {
					return (String) card.get("rarity");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static ArrayList<String> getARandomPack() {
		boolean gotBetterThanCommon = false;
		ArrayList<String> p = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			double f = Math.random();
			if (f >= .9890) {
				p.add(getRandomLegendary());
				gotBetterThanCommon = true;
			} else if (f >= .9558) {
				gotBetterThanCommon = true;
				p.add(getRandomEpic());
			} else if (f >= .7716){
				gotBetterThanCommon = true;
				p.add(getRandomRare());
			} else {
				p.add(getRandomCommon());
			}
			if (i == 4 && !gotBetterThanCommon) {
				p.remove(0);
				p.add(getRandomRare());
			}
		}
		return p;
	}

	private static String getRandomLegendary() {
		if (legendaries.size() > 0) {
			return legendaries.get((int) (Math.random() * legendaries.size()));
		}
		file = "all-cards.json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if (card.get("rarity").equals("LEGENDARY") && (boolean)(card.get("collectible"))) {
					legendaries.add((String) card.get("name"));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return legendaries.get((int) (Math.random() * legendaries.size()));
	}

	private static String getRandomEpic() {
		if (epics.size() > 0) {
			return epics.get((int) (Math.random() * epics.size()));
		}
		file = "all-cards.json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if (card.get("rarity").equals("EPIC") && (boolean)(card.get("collectible"))) {
					epics.add((String) card.get("name"));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return epics.get((int) (Math.random() * epics.size()));
	}


	private static String getRandomRare() {
		if (rares.size() > 0) {
			return rares.get((int) (Math.random() * rares.size()));
		}
		file = "all-cards.json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if (card.get("rarity").equals("RARE") && (boolean)(card.get("collectible"))) {
					rares.add((String) card.get("name"));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rares.get((int) (Math.random() * rares.size()));
	}

	private static String getRandomCommon() {
		if (commons.size() > 0) {
			return commons.get((int) (Math.random() * commons.size()));
		}
		file = "all-cards.json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if (card.get("rarity").equals("COMMON") && (boolean)(card.get("collectible"))) {
					commons.add((String) card.get("name"));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return commons.get((int) (Math.random() * commons.size()));
	}

	public static String getSet(String n) {
		file = "all-cards.json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if (card.get("name").equals(n)) {
					return (String) card.get("set");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getSet(String n, String t) {
		file = "all-cards.json";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray cards = (JSONArray) jsonObject.get("cards");
			for (int i = 0; i < cards.size(); i++){
				JSONObject card = (JSONObject) cards.get(i);
				if (card.get("name").equals(n) && card.get("type").equals(t)) {
					return (String) card.get("set");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
}
