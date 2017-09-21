package net.theinterweb.Server;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.parser.ParseException;

public class Test {
	public static void main(String args[]) throws ParseException{
		ArrayList<String> hand = new ArrayList<>();
		hand.add("Northshire Cleric");
		hand.add("Claw");
		hand.add("Fiery War Axe");
		hand.add("Moonfire");
		for (int i = 0; i < 10; i++) {
			int random = (int) (Math.random() * hand.size());
			System.out.println(hand.get(random));
		}
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
	
	public static ArrayList<String> getCommands(String s){
		s = s.replace('.', ',').toLowerCase();
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
