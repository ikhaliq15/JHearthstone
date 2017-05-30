package net.theinterweb.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Client {
	
	static BufferedReader in;
	static PrintWriter out;
	
	private static int selectedAttacker = -1;
	private static int selectedTarget = -1;
	
	public static void main(String args[]) throws UnknownHostException, IOException{
		
        String serverAddress = "localhost";
        Socket socket;
        try{
        	socket = new Socket(serverAddress, 8787);
        	in = new BufferedReader(new InputStreamReader(
        			socket.getInputStream()));
        	out = new PrintWriter(socket.getOutputStream(), true);
        	System.out.println("WELCOME TO HEARTHSTONE!");
        }catch (ConnectException e){
        	System.out.println("There seems to be some server issues!");
        }
        
        
        JFrame frm_Main = new JFrame("Hearthstone");
        frm_Main.setSize(700, 300);
        frm_Main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frm_Main.setResizable(false);
        frm_Main.setLocationRelativeTo(null);
        
        frm_Main.setLayout(new GridBagLayout());
        GridBagConstraints cn = new GridBagConstraints();
        
        
        JPanel pnl_FriendlyControls = new JPanel();;  
        JPanel pnl_EnemyControls = new JPanel();    
        
        
        cn.fill = GridBagConstraints.HORIZONTAL;
        cn.weightx = 0.5;
        cn.gridx = 2;
        cn.gridy = 0;  
        frm_Main.add(pnl_EnemyControls, cn); //, BorderLayout.NORTH);
        
        JLabel lbl_ManaInfo = new JLabel("Mana: 0/0");
        pnl_FriendlyControls.add(lbl_ManaInfo, BorderLayout.WEST);
        JButton btn_FriendlyHero = new JButton("30");
        btn_FriendlyHero.setEnabled(false);
        pnl_FriendlyControls.add(btn_FriendlyHero, BorderLayout.CENTER);        
        String[] hand = {};//{ "Coin", "Wisp", "Reno Jackson", "Leeroy Jenkins", "Jade Claws" };
        JComboBox<String> jcb_Hand = new JComboBox<String>(hand);
        pnl_FriendlyControls.add(jcb_Hand, BorderLayout.EAST);     
        JButton btn_PlayCard = new JButton("Play!");
        pnl_FriendlyControls.add(btn_PlayCard);
        btn_PlayCard.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		sendMessage("PLAY:" + jcb_Hand.getSelectedIndex());
        		if (jcb_Hand.getSelectedItem().equals("")){
        			jcb_Hand.setEnabled(false);
        			btn_PlayCard.setEnabled(false);
        		}
        	}
        });
        
        JButton btn_EndTurn = new JButton("End Turn!");
        cn.fill = GridBagConstraints.HORIZONTAL;
        cn.weightx = .4;
        cn.ipady = 40;
        cn.gridx = 3;
        cn.gridy = 2;
        cn.anchor = GridBagConstraints.PAGE_END;
        frm_Main.add(btn_EndTurn, cn);
        JButton btn_Attack = new JButton("Attack!");
        cn.fill = GridBagConstraints.HORIZONTAL;
        cn.ipady = 40;
        cn.anchor = GridBagConstraints.EAST;
        cn.gridx = 3;
        cn.gridy = 3; 
        frm_Main.add(btn_Attack, cn);
        
        cn.fill = GridBagConstraints.HORIZONTAL;
        cn.weightx = 0.5;
        cn.gridx = 2;
        cn.gridy = 4;  
        frm_Main.add(pnl_FriendlyControls, cn);

        
        JLabel lbl_EnemyManaInfo = new JLabel("Mana: 0/0");
        JButton btn_EnemyHero = new JButton("30");
        btn_EnemyHero.setEnabled(false);
        JLabel lbl_NumberOfCardsEnemy = new JLabel("Number Of Cards: x");
        pnl_EnemyControls.add(lbl_NumberOfCardsEnemy, BorderLayout.WEST);
        pnl_EnemyControls.add(btn_EnemyHero, BorderLayout.CENTER); 
        pnl_EnemyControls.add(lbl_EnemyManaInfo, BorderLayout.EAST);
       
        JPanel pnl_FriendlyMinions = new JPanel();
        JPanel pnl_EnemyMinions = new JPanel();
        cn.fill = GridBagConstraints.HORIZONTAL;
        cn.weightx = 1;
        cn.gridx = 2;
        cn.ipady = 40;
        cn.gridy = 3;
        pnl_FriendlyMinions.setLayout(new GridLayout(1, 7));
        frm_Main.add(pnl_FriendlyMinions, cn);
        
        cn.fill = GridBagConstraints.HORIZONTAL;
        cn.weightx = 1;
        cn.gridx = 2;
        cn.gridy = 2;
        cn.ipady = 40;
        pnl_EnemyMinions.setLayout(new GridLayout(1, 7));
        frm_Main.add(pnl_EnemyMinions, cn);
        
        btn_EnemyHero.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		selectedTarget = 9999;
        		for (Component c : pnl_EnemyMinions.getComponents()) {
				    if (c instanceof JButton) { 
				       ((JButton)c).setBackground(new JButton().getBackground());
				       ((JButton)c).setBorder(new JButton().getBorder());
				    }
				}
				btn_EnemyHero.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        	}
        });
        
        btn_EndTurn.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		sendMessage("END");
        		btn_EnemyHero.setBackground(new JButton().getBackground());
			    btn_EnemyHero.setBorder(new JButton().getBorder());
        		selectedTarget = -1;
        		selectedAttacker = -1;
        	}
        });
        
        btn_Attack.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		sendMessage("ATTACK:" + selectedAttacker + ":" + selectedTarget);
        		btn_EnemyHero.setBackground(new JButton().getBackground());
			    btn_EnemyHero.setBorder(new JButton().getBorder());
        		selectedTarget = -1;
        		selectedAttacker = -1;
        	}
        });
        
        frm_Main.setVisible(true);
        
        while (true){
        	if (selectedTarget == -1 || selectedAttacker == -1){
        		btn_Attack.setEnabled(false);
        	}else{
        		btn_Attack.setEnabled(true);
        	}
        	if(in.ready()){
        		String msg = in.readLine();
        		System.out.println("LINE: " + msg);
        		if (msg.startsWith("BASICINFO:")){
	        		msg = msg.substring("BASICINFO:".length());
	        		if(msg.startsWith("DISABLECONTROLS")){
	        			jcb_Hand.setEnabled(false);
	        			btn_PlayCard.setEnabled(false);
	        			btn_Attack.setEnabled(false);
	        			btn_EndTurn.setEnabled(false);
	        			btn_EnemyHero.setEnabled(false);
	        			msg = msg.substring("DISABLECONTROLS".length());
	        		}else if(msg.startsWith("ENABLECONTROLS")){
	        			jcb_Hand.setEnabled(true);
	        			btn_PlayCard.setEnabled(true);
	        			btn_EnemyHero.setEnabled(true);
	        			if (selectedTarget == -1 || selectedAttacker == -1){
	                		btn_Attack.setEnabled(false);
	                	}else{
	                		btn_Attack.setEnabled(true);
	                	}
	        			btn_EndTurn.setEnabled(true);
	        			msg = msg.substring("ENABLECONTROLS".length());
	        		}
	        		btn_EnemyHero.setText(msg.substring(0, 2));
	        		msg = msg.substring(2);
	        		btn_FriendlyHero.setText(msg.substring(0, 2));
	        		msg = msg.substring(2);
	        		lbl_ManaInfo.setText(msg.substring(0,2) + "/" + msg.substring(2, 4));
	        		msg = msg.substring(4);
	        		lbl_EnemyManaInfo.setText(msg.substring(0,2) + "/" + msg.substring(2, 4));
	        		msg = msg.substring(5);
	        		char c = msg.charAt(0);
	        		String t = "";
	        		while (c != '^'){
	        			t += c;
	        			msg = msg.substring(1);
	        			c = msg.charAt(0);
	        		}
	        		ArrayList<String> givenhand = new ArrayList<String>(Arrays.asList(t.split(",")));
	        		String[] handarray = new String[givenhand.size()];
	        		for(int i = 0; i < givenhand.size(); i++){
	        			handarray[i] = givenhand.get(i);
	        		}
	        		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String> ( handarray );
	        		jcb_Hand.setModel( model );
        		}else if(msg.startsWith("FBOARD:")){
        			msg = msg.substring("FBOARD".length() + 1);
        			String [][] minions = getMinions(msg);
        			pnl_FriendlyMinions.removeAll();
        			if (minions.length >= 7){
        				btn_PlayCard.setEnabled(false);
        			}
        			for(int i = 0; i < minions.length; i++){
        				final int minionCurrent = i;
        				JButton b = new JButton("<html>" + minions[i][0] + "<br/><center>" + minions[i][1] + " - " + minions[i][2] + "</center></html>");
        				b.setEnabled(minions[i][3].equals("true"));
        				if (i == selectedAttacker){
    						b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        				}
        				b.addActionListener(new ActionListener(){
        					public void actionPerformed(ActionEvent e){
        						selectedAttacker = minionCurrent;
        						for (Component c : pnl_FriendlyMinions.getComponents()) {
        						    if (c instanceof JButton) { 
        						       ((JButton)c).setBackground(new JButton().getBackground());
        						       ((JButton)c).setBorder(new JButton().getBorder());
        						    }
        						}
        						b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        					}
        				});
        				if(!btn_EndTurn.isEnabled() || minions[i][1].equals("0")){
        					b.setEnabled(false);
        				}
        				pnl_FriendlyMinions.add(b);
        			}
        			pnl_FriendlyMinions.revalidate();
        			pnl_FriendlyMinions.repaint(); 
        		}else if(msg.startsWith("EBOARD:")){
        			msg = msg.substring("EBOARD".length() + 1);
        			String[][] minions = getMinions(msg);
        			pnl_EnemyMinions.removeAll();
        			for(int i = 0; i < minions.length; i++){
        				final int minionCurrent = i;
        				JButton b = new JButton("<html>" + minions[i][0] + "<br/><center>" + minions[i][1] + " - " + minions[i][2] + "</center></html>");
        				if (i == selectedTarget){
    						b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        				}
        				b.addActionListener(new ActionListener(){
        					public void actionPerformed(ActionEvent e){
        						selectedTarget = minionCurrent;
        						for (Component c : pnl_EnemyMinions.getComponents()) {
        						    if (c instanceof JButton) { 
        						       ((JButton)c).setBackground(new JButton().getBackground());
        						       ((JButton)c).setBorder(new JButton().getBorder());
        						    }
        						}
        						btn_EnemyHero.setBackground(new JButton().getBackground());
        					    btn_EnemyHero.setBorder(new JButton().getBorder());
        						b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        					}
        				});
        				if(!btn_EndTurn.isEnabled()){
        					b.setEnabled(false);
        				}
        				pnl_EnemyMinions.add(b);
        			}
        			pnl_EnemyMinions.revalidate();
        			pnl_EnemyMinions.repaint();
        		}else if(msg.startsWith("ERROR:")){
        			msg = msg.substring("ERROR:".length());
        			if(msg.equals("MANALOW")){
        		        JOptionPane.showMessageDialog(frm_Main, "You don't have enough mana.", "Error!", JOptionPane.INFORMATION_MESSAGE);
        			}else if (msg.startsWith("FATIGUE")){
        				msg = msg.substring("FATIGUE".length());
        		        JOptionPane.showMessageDialog(frm_Main, "You have run out of cards causing " + msg + " damage.", "Fatigue!", JOptionPane.INFORMATION_MESSAGE);
        			}else if (msg.startsWith("BURNED")){
        				msg = msg.substring("BURNED".length());
        		        JOptionPane.showMessageDialog(frm_Main, "Your hand is full. You burned " + msg + "!", "Hand is full!", JOptionPane.INFORMATION_MESSAGE);
        			}
        		}else if(msg.startsWith("RESULT:")){
        			msg = msg.substring("RESULT:".length());
        			jcb_Hand.setEnabled(false);
        			btn_PlayCard.setEnabled(false);
        			btn_Attack.setEnabled(false);
        			btn_EndTurn.setEnabled(false);
        			btn_EnemyHero.setEnabled(false);
        			if(msg.equals("WIN")){
        		        JOptionPane.showMessageDialog(frm_Main, "You won!", "Congrats!", JOptionPane.INFORMATION_MESSAGE);
        			}else if(msg.equals("LOSE")){
        		        JOptionPane.showMessageDialog(frm_Main, "You lost!", "Sorry!", JOptionPane.INFORMATION_MESSAGE);
        			}else if(msg.equals("TIE")){
        		        JOptionPane.showMessageDialog(frm_Main, "You tied!", "Tie!", JOptionPane.INFORMATION_MESSAGE);
        			}
        		}
        	}else{
        	}
        	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
	}
	
	public static String[][] getMinions(String s){
        ArrayList<ArrayList<String>> a = new ArrayList<ArrayList<String>>();
        String t = "";
        if (s.length() > 4){
            a.add(new ArrayList<String>());
        }
        for(int i = 2; i < s.length()-1; i++){
            if (String.valueOf(s.charAt(i)).equals("^") || String.valueOf(s.charAt(i)).equals("]")){
                a.get(a.size()-1).add(t);
                t = "";
            }else if(s.charAt(i) == '['){
                a.add(new ArrayList<String>());
            }else{
                t += String.valueOf(s.charAt(i));
            }
            if (s.charAt(i) == '^'){
                i ++;
            }
            if (s.charAt(i) == ']'){
                i += 2;
            }
        }
        String [][] b = new String[a.size()][4];
        for (int i = 0; i < a.size(); i++){
            for (int j = 0; j < a.get(i).size(); j++){
                b[i][j] = a.get(i).get(j);
            }
        }
        return b;
     }
	
    public static void printArray(String[][] a){
        ArrayList<ArrayList<String>> t = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < a.length; i++){
            t.add(new ArrayList<String>());
            for (int j = 0; j < a[i].length; j++){
                t.get(t.size()-1).add(a[i][j]);
            }
        }
        System.out.println(t);
     }
    
	public static void sendMessage(String msg){
		out.println(msg);
	}
}