package net.theinterweb.Client;

import net.theinterweb.Shared.ServerCommands;
import net.theinterweb.Shared.UIStrings;

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
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Client {
	
	private static BufferedReader in;
	private static PrintWriter out;
	
	private static int selectedAttacker = -1;
	private static int selectedTarget = -1;

	private static JFrame frm_Main;
	private static JFrame frm_Loading;
	
	private static String name = "";
	private static String rank = "";

	private static boolean inSelectingMode = false;

	private static Socket socket;
	
	public static void main(String args[]) throws IOException{
        String serverAddress = "67.188.200.101";
		//String serverAddress = "localhost";
		//String serverAddress = "10.51.27.28";
		System.out.println("TRYING TO CONNECT TO SERVER: " + serverAddress);
		try{
        	socket = new Socket(serverAddress, 8787);
        	in = new BufferedReader(new InputStreamReader(
        			socket.getInputStream()));
        	out = new PrintWriter(socket.getOutputStream(), true);
        	System.out.println("WELCOME TO HEARTHSTONE!");
        }catch (ConnectException e){
        	System.out.println("There seems to be some server issues!");
        }
        
        frm_Loading = new JFrame("Hearthstone");
        frm_Loading.setSize(725, 325);
        frm_Loading.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm_Loading.setLocationRelativeTo(null);
                
        // Set up frm_Loading
        frm_Loading.add(createLoginPage(frm_Loading));
        frm_Loading.setVisible(true);
        
        
        frm_Main = new JFrame("Hearthstone");
        frm_Main.setSize(730, 350);
        frm_Main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frm_Main.setResizable(false);
        frm_Main.setLocationRelativeTo(null);
        
        frm_Main.setLayout(new GridBagLayout());
        GridBagConstraints cn = new GridBagConstraints();
        
        
        JPanel pnl_FriendlyControls = new JPanel();
        final JPanel pnl_EnemyControls = new JPanel();
        
        
        cn.fill = GridBagConstraints.HORIZONTAL;
        cn.weightx = 0.5;
        cn.gridx = 2;
        cn.gridy = 0;  
        frm_Main.add(pnl_EnemyControls, cn); //, BorderLayout.NORTH);
        
        JLabel lbl_ManaInfo = new JLabel("Mana: 0/0");
        pnl_FriendlyControls.add(lbl_ManaInfo, BorderLayout.WEST);
        final JButton btn_FriendlyHero = new JButton("30");
        btn_FriendlyHero.setEnabled(false);
        pnl_FriendlyControls.add(btn_FriendlyHero, BorderLayout.CENTER);        
        String[] hand = {};//{ "Coin", "Wisp", "Reno Jackson", "Leeroy Jenkins", "Jade Claws" };
        final JComboBox<String> jcb_Hand = new JComboBox<>(hand);
        pnl_FriendlyControls.add(jcb_Hand, BorderLayout.EAST);     
        final JButton btn_PlayCard = new JButton("Play!");
        pnl_FriendlyControls.add(btn_PlayCard);
        btn_PlayCard.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		sendMessage(ServerCommands.GAME_PLAY_PREFIX + jcb_Hand.getSelectedIndex());
        		if (jcb_Hand.getSelectedItem().equals("")){
        			jcb_Hand.setEnabled(false);
        			btn_PlayCard.setEnabled(false);
        		}
        	}
        });
        
        final JButton btn_EndTurn = new JButton("End Turn!");
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

        
        final JLabel lbl_EnemyManaInfo = new JLabel("Mana: 0/0");
        final JButton btn_EnemyHero = new JButton("30");
        btn_EnemyHero.setEnabled(false);
        JLabel lbl_NumberOfCardsEnemy = new JLabel("Number Of Cards: x");
        pnl_EnemyControls.add(lbl_NumberOfCardsEnemy, BorderLayout.WEST);
        pnl_EnemyControls.add(btn_EnemyHero, BorderLayout.CENTER); 
        pnl_EnemyControls.add(lbl_EnemyManaInfo, BorderLayout.EAST);
       
        final JPanel pnl_FriendlyMinions = new JPanel();
        final JPanel pnl_EnemyMinions = new JPanel();
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
        		if (inSelectingMode) {
        			selectedAttacker = -1;
					for (Component c : pnl_FriendlyMinions.getComponents()) {
						if (c instanceof JButton) {
							((JButton)c).setBackground(new JButton().getBackground());
							((JButton)c).setBorder(new JButton().getBorder());
						}
					}
				}
				//btn_FriendlyHero.setBackground(new JButton().getBackground());
				//btn_FriendlyHero.setBorder(new JButton().getBorder());
        	}
        });

		btn_FriendlyHero.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				selectedAttacker = 9999;
				for (Component c : pnl_FriendlyMinions.getComponents()) {
					if (c instanceof JButton) {
						((JButton)c).setBackground(new JButton().getBackground());
						((JButton)c).setBorder(new JButton().getBorder());
					}
				}
				btn_FriendlyHero.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
				if (inSelectingMode) {
					selectedTarget = -1;
					for (Component c : pnl_EnemyMinions.getComponents()) {
						if (c instanceof JButton) {
							((JButton)c).setBackground(new JButton().getBackground());
							((JButton)c).setBorder(new JButton().getBorder());
						}
					}
				}
				//btn_EnemyHero.setBackground(new JButton().getBackground());
				//btn_EnemyHero.setBorder(new JButton().getBorder());
			}
		});
        
        btn_EndTurn.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		sendMessage(ServerCommands.GAME_END_TURN);
        		btn_EnemyHero.setBackground(new JButton().getBackground());
			    btn_EnemyHero.setBorder(new JButton().getBorder());
				btn_FriendlyHero.setBackground(new JButton().getBackground());
				btn_FriendlyHero.setBorder(new JButton().getBorder());
        		selectedTarget = -1;
        		selectedAttacker = -1;
        	}
        });
        
        btn_Attack.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if (((JButton) e.getSource()).getText().equals("Attack!")) {
					sendMessage(ServerCommands.GAME_ATTACK_PREFIX + selectedAttacker + ":" + selectedTarget);
					btn_EnemyHero.setBackground(new JButton().getBackground());
					btn_EnemyHero.setBorder(new JButton().getBorder());
					btn_FriendlyHero.setBackground(new JButton().getBackground());
					btn_FriendlyHero.setBorder(new JButton().getBorder());
					selectedTarget = -1;
					selectedAttacker = -1;
				} else if (((JButton) e.getSource()).getText().equals("Confirm.")) {
					((JButton) e.getSource()).setText("Attack!");
					((JButton) e.getSource()).setEnabled(false);
					if (selectedAttacker == -1) {
						sendMessage("E" + selectedTarget);
					} else if (selectedTarget == -1) {
						sendMessage("F" + selectedAttacker);
					}

					inSelectingMode = false;

					for (Component c : pnl_EnemyMinions.getComponents()) {
						if (c instanceof JButton) {
							((JButton)c).setBackground(new JButton().getBackground());
							((JButton)c).setBorder(new JButton().getBorder());
						}
					}
					for (Component c : pnl_FriendlyMinions.getComponents()) {
						if (c instanceof JButton) {
							((JButton)c).setBackground(new JButton().getBackground());
							((JButton)c).setBorder(new JButton().getBorder());

						}
					}

					btn_EnemyHero.setBackground(new JButton().getBackground());
					btn_EnemyHero.setBorder(new JButton().getBorder());
					btn_EnemyHero.setEnabled(true);

					btn_FriendlyHero.setBackground(new JButton().getBackground());
					btn_FriendlyHero.setBorder(new JButton().getBorder());
					btn_FriendlyHero.setEnabled(false);

					btn_PlayCard.setEnabled(true);
					btn_EndTurn.setEnabled(true);
					selectedTarget = -1;
					selectedAttacker = -1;
				}
        	}
        });
        
        //frm_Main.setVisible(true);
        
        while (true){
        	//System.out.println(frm_Main.getSize());
        	if ((selectedTarget == -1 || selectedAttacker == -1) && !inSelectingMode){
        		btn_Attack.setEnabled(false);
        	}else{
        		btn_Attack.setEnabled(true);
        	}
        	if(in.ready()){
        		String msg = in.readLine();
        		System.out.println("LINE: " + msg);
        		if (msg.startsWith(ServerCommands.GAME_BASIC_INFO_PREFIX)){
	        		msg = msg.substring(ServerCommands.GAME_BASIC_INFO_PREFIX.length());
	        		if(msg.startsWith(ServerCommands.GAME_DISABLE_CONTROLS) && !inSelectingMode){
	        			jcb_Hand.setEnabled(false);
	        			btn_PlayCard.setEnabled(false);
	        			btn_Attack.setEnabled(false);
	        			btn_EndTurn.setEnabled(false);
	        			btn_EnemyHero.setEnabled(false);
	        			msg = msg.substring(ServerCommands.GAME_DISABLE_CONTROLS.length());
	        		}else if(msg.startsWith(ServerCommands.GAME_ENABLE_CONTROLS) && !inSelectingMode){
	        			jcb_Hand.setEnabled(true);
	        			btn_PlayCard.setEnabled(true);
	        			btn_EnemyHero.setEnabled(true);
	        			if (selectedTarget == -1 || selectedAttacker == -1){
	                		btn_Attack.setEnabled(false);
	                	}else{
	                		btn_Attack.setEnabled(true);
	                	}
	        			btn_EndTurn.setEnabled(true);
	        			msg = msg.substring(ServerCommands.GAME_ENABLE_CONTROLS.length());
	        		}
	        		btn_EnemyHero.setText(msg.substring(0, 2));
	        		msg = msg.substring(2);
	        		btn_FriendlyHero.setText(msg.substring(0, 2));
	        		msg = msg.substring(2);
	        		int enemy_armor = Integer.valueOf(msg.substring(0, msg.indexOf(":EAR")));
	        		if (enemy_armor != 0) {
	        			btn_EnemyHero.setText(btn_EnemyHero.getText() + " + " + enemy_armor);
					}
					msg = msg.substring(msg.indexOf(":EAR") + ":EAR".length());
					int friendly_armor = Integer.valueOf(msg.substring(0, msg.indexOf(":FAR")));
					if (friendly_armor != 0) {
						btn_FriendlyHero.setText(btn_FriendlyHero.getText() + " + " + friendly_armor);
					}
	        		msg = msg.substring(msg.indexOf(":FAR") + ":FAR".length());
	        		lbl_ManaInfo.setText(msg.substring(0,2) + "/" + msg.substring(2, 4));
	        		msg = msg.substring(4);
	        		lbl_EnemyManaInfo.setText(msg.substring(0,2) + "/" + msg.substring(2, 4));
	        		msg = msg.substring(4);
	        		System.out.println(msg);
					int friendly_weapon_attack = Integer.valueOf(msg.substring(0, msg.indexOf(":FWA")));
					msg = msg.substring(msg.indexOf(":FWA") + ":FWA".length());
					int friendly_weapon_durability = Integer.valueOf(msg.substring(0, msg.indexOf(":FWD")));
					msg = msg.substring(msg.indexOf(":FWD") + ":FWD".length());
					if (friendly_weapon_durability > 0) {
						btn_FriendlyHero.setText(friendly_weapon_attack + "/" + friendly_weapon_durability + " - " + btn_FriendlyHero.getText());
					}
					int enemy_weapon_attack = Integer.valueOf(msg.substring(0, msg.indexOf(":EWA")));
					msg = msg.substring(msg.indexOf(":EWA") + ":EWA".length());
					int enemy_weapon_durability = Integer.valueOf(msg.substring(0, msg.indexOf(":EWD")));
					msg = msg.substring(msg.indexOf(":EWD") + ":EWD".length());
					if (enemy_weapon_durability > 0) {
						btn_EnemyHero.setText(enemy_weapon_attack + "/" + enemy_weapon_durability + " - " + btn_EnemyHero.getText());
					}
					boolean heroCanAttack = Boolean.valueOf(msg.substring(0, msg.indexOf(":FCA")));
					btn_FriendlyHero.setEnabled(heroCanAttack);
					msg = msg.substring(msg.indexOf(":FCA") + ":FCA".length());
					msg = msg.substring(1);
	        		char c = msg.charAt(0);
	        		String t = "";
	        		while (c != '^'){
	        			t += c;
	        			msg = msg.substring(1);
	        			c = msg.charAt(0);
	        		}
	        		ArrayList<String> givenhand = new ArrayList<>(Arrays.asList(t.split(",")));
	        		String[] handarray = new String[givenhand.size()];
	        		for(int i = 0; i < givenhand.size(); i++){
	        			handarray[i] = givenhand.get(i);
	        		}
	        		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<> ( handarray );
	        		jcb_Hand.setModel( model );
        		}else if(msg.startsWith(ServerCommands.GAME_FRIEND_BOARD_PREFIX)){
        			msg = msg.substring(ServerCommands.GAME_FRIEND_BOARD_PREFIX.length());
        			String [][] minions = getMinions(msg);
        			pnl_FriendlyMinions.removeAll();
        			if (minions.length >= 7){
        				btn_PlayCard.setEnabled(false);
        			}
        			for(int i = 0; i < minions.length; i++){
        				final int minionCurrent = i;
        				final JButton b = new JButton();
        				b.setEnabled(minions[i][3].equals("true"));
        				if (i == selectedAttacker){
    						b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        				}

						String button_text = "<html><center>" + minions[i][0] + "<br/>" + minions[i][1] + " - " + minions[i][2] + "</center>";
						if (minions[i][4].equals("true")) {
							button_text += "<center><b>(TAUNT)</b></center>";
//							b.setBackground(Color.DARK_GRAY);
//							b.setOpaque(true);
						}
						button_text += "</html>";
						b.setText(button_text);

        				b.addActionListener(new ActionListener(){
        					public void actionPerformed(ActionEvent e){
        						if (inSelectingMode) {
        							selectedTarget = -1;
								}
        						selectedAttacker = minionCurrent;
        						for (Component c : pnl_FriendlyMinions.getComponents()) {
        						    if (c instanceof JButton) {
        						    	((JButton)c).setBackground(new JButton().getBackground());
										((JButton)c).setBorder(new JButton().getBorder());
        						    }
        						}
        						b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
								btn_FriendlyHero.setBackground(new JButton().getBackground());
								btn_FriendlyHero.setBorder(new JButton().getBorder());        					}
        				});
        				if(!btn_EndTurn.isEnabled() || minions[i][1].equals("0")){
        					b.setEnabled(false);
        				}
        				pnl_FriendlyMinions.add(b);
        			}
        			pnl_FriendlyMinions.revalidate();
        			pnl_FriendlyMinions.repaint(); 
        		}else if(msg.startsWith(ServerCommands.GAME_ENEMY_BOARD_PREFIX)){
        			msg = msg.substring(ServerCommands.GAME_ENEMY_BOARD_PREFIX.length());
        			String[][] minions = getMinions(msg);
        			pnl_EnemyMinions.removeAll();
        			for(int i = 0; i < minions.length; i++){
        				final int minionCurrent = i;
        				final JButton b = new JButton();
        				String button_text = "<html><center>" + minions[i][0] + "<br/>" + minions[i][1] + " - " + minions[i][2] + "</center>";
        				if (minions[i][4].equals("true")) {
        					button_text += "<center><b>(TAUNT)</b></center>";
							//b.setBackground(Color.DARK_GRAY);
							//b.setOpaque(true);
						}
						button_text += "</html>";
        				b.setText(button_text);
        				if (i == selectedTarget){
    						b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        				}
        				b.addActionListener(new ActionListener(){
        					public void actionPerformed(ActionEvent e){
        						if (inSelectingMode) {
        							selectedAttacker = -1;
								}
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
        		} else if (msg.equals(ServerCommands.START_PAGE_WAITING)) {
        			//for (Component c: frm_Main.getContentPane().getComponents()){
        			//	c.setVisible(false);
        			//}
        			//frm_Main.add(new JLabel("Waiting for players..."));
        		} else if (msg.equals(ServerCommands.GAME_READY_TO_START)) {
        			//for (Component c: frm_Main.getContentPane().getComponents()){
        			//	c.setVisible(!c.isVisible());
        			//}
        		} else if(msg.startsWith(ServerCommands.GAME_ERROR_PREFIX)){
        			msg = msg.substring(ServerCommands.GAME_ERROR_PREFIX.length());
        			if(msg.equals(ServerCommands.GAME_ERROR_MANA_LOW)){
        		        JOptionPane.showMessageDialog(frm_Main, "You don't have enough mana.", "Error!", JOptionPane.INFORMATION_MESSAGE);
        			}else if (msg.startsWith(ServerCommands.GAME_ERROR_FATIGUE)){
        				msg = msg.substring(ServerCommands.GAME_ERROR_FATIGUE.length());
        		        JOptionPane.showMessageDialog(frm_Main, "You have run out of cards causing " + msg + " damage.", "Fatigue!", JOptionPane.INFORMATION_MESSAGE);
        			}else if (msg.startsWith(ServerCommands.GAME_ERROR_BURNED)){
        				msg = msg.substring(ServerCommands.GAME_ERROR_BURNED.length());
        		        JOptionPane.showMessageDialog(frm_Main, "Your hand is full. You burned " + msg + "!", "Hand is full!", JOptionPane.INFORMATION_MESSAGE);
        			}else if (msg.startsWith(ServerCommands.GAME_ERROR_TAUNT)) {
						JOptionPane.showMessageDialog(frm_Main, "A minion with taunt is in the way.", "Taunt", JOptionPane.INFORMATION_MESSAGE);
					}
        		}else if(msg.startsWith(ServerCommands.GAME_RESULT_PREFIX)){
        			msg = msg.substring(ServerCommands.GAME_RESULT_PREFIX.length());
        			jcb_Hand.setEnabled(false);
        			btn_PlayCard.setEnabled(false);
        			btn_Attack.setEnabled(false);
        			btn_EndTurn.setEnabled(false);
        			btn_EnemyHero.setEnabled(false);
        			if(msg.equals(ServerCommands.GAME_RESULT_WIN)){
        		        JOptionPane.showMessageDialog(frm_Main, "You won!", "Congrats!", JOptionPane.INFORMATION_MESSAGE);
        			}else if(msg.equals(ServerCommands.GAME_RESULT_LOSE)){
        		        JOptionPane.showMessageDialog(frm_Main, "You lost!", "Sorry!", JOptionPane.INFORMATION_MESSAGE);
        			}else if(msg.equals(ServerCommands.GAME_RESULT_TIE)){
        		        JOptionPane.showMessageDialog(frm_Main, "You tied!", "Tie!", JOptionPane.INFORMATION_MESSAGE);
        			}
        			frm_Main.setVisible(false);
        			frm_Loading.setLocation(frm_Main.getLocation());
        			frm_Loading.getContentPane().removeAll();
        			frm_Loading.add(createStartPage(name, rank));
        			frm_Loading.setVisible(true);
        			sendMessage(ServerCommands.GAME_END_TURN);
        		} else if (msg.startsWith(ServerCommands.GAME_GET_TARGET_PREFIX)) {
        			String valid_targets = msg.substring(ServerCommands.GAME_GET_TARGET_PREFIX.length());
        			System.out.println(valid_targets);
					btn_Attack.setText("Confirm.");
					btn_Attack.setEnabled(true);
					btn_PlayCard.setEnabled(false);
					btn_EndTurn.setEnabled(false);
					inSelectingMode = true;
        			if (valid_targets.equals("ANY")) {
						btn_FriendlyHero.setEnabled(true);
						btn_EnemyHero.setEnabled(true);
						for (Component c : pnl_EnemyMinions.getComponents()) {
							if (c instanceof JButton) {
								((JButton)c).setBackground(new JButton().getBackground());
								((JButton)c).setBorder(new JButton().getBorder());
								c.setEnabled(true);
							}
						}
						for (Component c : pnl_FriendlyMinions.getComponents()) {
							if (c instanceof JButton) {
								((JButton)c).setBackground(new JButton().getBackground());
								((JButton)c).setBorder(new JButton().getBorder());
									c.setEnabled(true);
							}
						}
						btn_EnemyHero.setBackground(new JButton().getBackground());
						btn_EnemyHero.setBorder(new JButton().getBorder());
						btn_FriendlyHero.setBackground(new JButton().getBackground());
						btn_FriendlyHero.setBorder(new JButton().getBorder());
					} else if (valid_targets.equals("MINIONS")) {
						btn_FriendlyHero.setEnabled(false);
						btn_EnemyHero.setEnabled(false);
						for (Component c : pnl_EnemyMinions.getComponents()) {
							if (c instanceof JButton) {
								((JButton)c).setBackground(new JButton().getBackground());
								((JButton)c).setBorder(new JButton().getBorder());
								c.setEnabled(true);
							}
						}
						for (Component c : pnl_FriendlyMinions.getComponents()) {
							if (c instanceof JButton) {
								((JButton)c).setBackground(new JButton().getBackground());
								((JButton)c).setBorder(new JButton().getBorder());
								c.setEnabled(true);
							}
						}
					} else if (valid_targets.equals("ENEMY_CHARACTERS")) {

					} else if (valid_targets.equals("FRIENDLY_MINIONS")) {
						btn_FriendlyHero.setEnabled(false);
						btn_EnemyHero.setEnabled(false);
						for (Component c : pnl_EnemyMinions.getComponents()) {
							if (c instanceof JButton) {
								((JButton)c).setBackground(new JButton().getBackground());
								((JButton)c).setBorder(new JButton().getBorder());
								c.setEnabled(false);
							}
						}
						for (Component c : pnl_FriendlyMinions.getComponents()) {
							if (c instanceof JButton) {
								((JButton)c).setBackground(new JButton().getBackground());
								((JButton)c).setBorder(new JButton().getBorder());
								c.setEnabled(true);
							}
						}
					} else if (valid_targets.equals("ENEMY_MINIONS")) {
						btn_FriendlyHero.setEnabled(false);
						btn_EnemyHero.setEnabled(false);
						for (Component c : pnl_EnemyMinions.getComponents()) {
							if (c instanceof JButton) {
								((JButton)c).setBackground(new JButton().getBackground());
								((JButton)c).setBorder(new JButton().getBorder());
								c.setEnabled(true);
							}
						}
						for (Component c : pnl_FriendlyMinions.getComponents()) {
							if (c instanceof JButton) {
								((JButton)c).setBackground(new JButton().getBackground());
								((JButton)c).setBorder(new JButton().getBorder());
								c.setEnabled(false);
							}
						}
					} else if (valid_targets.equals("FRIENDLY_CHARACTERS")) {

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
        ArrayList<ArrayList<String>> a = new ArrayList<>();
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
        String [][] b = new String[a.size()][5];
        for (int i = 0; i < a.size(); i++){
            for (int j = 0; j < a.get(i).size(); j++){
                b[i][j] = a.get(i).get(j);
            }
        }
        return b;
     }

    
	private static void sendMessage(String msg){
		out.println(msg);
	}
	
	private static SignUpPanel createSignUpPage(final JFrame f){
		final SignUpPanel tmp = new SignUpPanel();
		tmp.getBtn_Login().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				f.getContentPane().removeAll();
				f.add(createLoginPage(f));
			}
		});
		tmp.getBtn_SignUp().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if (String.valueOf(tmp.psf_Password.getPassword()).equals(String.valueOf(tmp.pwf_Retype.getPassword()))) {
					out.println(ServerCommands.SIGN_UP_PREFIX  + ServerCommands.SIGN_UP_USERNAME_PREFIX + tmp.txt_Username.getText() + ServerCommands.SIGN_UP_PASSWORD_PREFIX + String.valueOf(tmp.psf_Password.getPassword()));
				}
				try {
					String input = in.readLine();
					if (input.startsWith(ServerCommands.SIGN_UP_SUCCESS)) {
						f.getContentPane().removeAll();
						frm_Loading.add(createLoginPage(f));
					} else if (input.equals(ServerCommands.SIGN_UP_FAIL)) {
        		        JOptionPane.showMessageDialog(f, UIStrings.SIGN_UP_FAIL, UIStrings.SIGN_UP_FAIL_TITLE, JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		return tmp;
	}
	
	private static LoginPanel createLoginPage(final JFrame f){
		final LoginPanel tmp = new LoginPanel();
		tmp.btn_SignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.getContentPane().removeAll();
				f.add(createSignUpPage(f));
			}
		});
		tmp.btn_Submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				out.println(ServerCommands.LOGIN_PREFIX + tmp.jtf_Username.getText() + ServerCommands.LOGIN_PASSWORD_PREFIX + String.valueOf(tmp.jpf_Password.getPassword()));
				try {
					String input = in.readLine();
					if (input.startsWith(ServerCommands.LOGIN_VALID_PASSWORD + ServerCommands.LOGIN_USERNAME_PREFIX)) {
						input = input.substring(ServerCommands.LOGIN_VALID_PASSWORD.length() + ServerCommands.LOGIN_USERNAME_PREFIX.length());
						for (Component c: frm_Loading.getContentPane().getComponents()){
							c.setVisible(false);
						}
						String name = "";
						if (input.contains(ServerCommands.LOGIN_RANK_PREFIX))	{
							name = input.substring(0, input.indexOf(ServerCommands.LOGIN_RANK_PREFIX));
							name = String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1); 
						}
						input = input.substring(name.length() + ServerCommands.LOGIN_RANK_PREFIX.length());
						String rank = input;
						frm_Main.setTitle(frm_Main.getTitle() + " - " + name);
						frm_Loading.setTitle(frm_Loading.getTitle() + " - " + name);
						frm_Loading.add(createStartPage(name, rank));
					} else if (input.equals(ServerCommands.LOGIN_INVALID_PASSWORD)) {
        		        JOptionPane.showMessageDialog(f, UIStrings.LOGIN_FAIL, UIStrings.LOGIN_FAIL_TITLE, JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		return tmp;
	}
	
	private static StartPagePanel createStartPage(String n, String r) {
		name = n;
		rank = r;
		StartPagePanel spp = new StartPagePanel();
		spp.getPlayButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				out.println(ServerCommands.START_PAGE_JOIN_LINE);
				frm_Main.setLocation(frm_Loading.getLocation());
				frm_Main.setVisible(true);
				frm_Loading.setVisible(false);
			}
		});
		spp.getLogoutButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(ServerCommands.START_PAGE_LOGOUT);
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		spp.getMakeDeckButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Component c: frm_Loading.getContentPane().getComponents()) {
					c.setVisible(false);
				}
				frm_Loading.add(createMakeDeckPage());
			}
		});
		spp.getOpenPackButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Component c: frm_Loading.getContentPane().getComponents()) {
					c.setVisible(false);
				}
				frm_Loading.add(createPackOpeningPage());
			}
		});
		spp.getCollectionButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Component c: frm_Loading.getContentPane().getComponents()) {
					c.setVisible(false);
				}
				frm_Loading.add(createCollectionPage());
			}
		});
		spp.getLblWelcome().setText(spp.getLblWelcome().getText() + "" + n + ".");
		spp.getRankLabel().setText(spp.getRankLabel().getText() + "" + r + ".");
		return spp;
	}
	
	private static MakeDeckPanel createMakeDeckPage() {
		final MakeDeckPanel mdp = new MakeDeckPanel();
		out.println(ServerCommands.MAKE_DECK_GET_DECK);
		try {
			String input = "";
			while (!input.equals(ServerCommands.MAKE_DECK_DECK_FINISHED)) {
				if (!input.equals("")) {
					mdp.addCardToDeck(input);
					System.out.println(input);
				}
				if(in.ready()) {
					input = in.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println(ServerCommands.MAKE_DECK_COLLECTION);
		try {
			String input = "";
			while (!input.equals(ServerCommands.MAKE_DECK_COLLECTION_DONE)) {
				if (!input.equals("")) {
					mdp.addCardToCollection(input);
					//System.out.println(input);
				}
				while (!in.ready()) {
					
				}
				if(in.ready()) {
					input = in.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		mdp.getSearchButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mdp.resetCollection();
				JList<String> l = mdp.getCollectionList();
				ArrayList<String> remove_list = new ArrayList<>();
				for (int i = 0; i < l.getModel().getSize(); i++) {
					if (!l.getModel().getElementAt(i).contains(mdp.getSearchField().getText())) {
						remove_list.add(l.getModel().getElementAt(i));
					}
				}
				for (String str: remove_list) {
					mdp.removeCardFromCollection(str);
				}
			}
		});
		mdp.getBackButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Component c: frm_Loading.getContentPane().getComponents()) {
					c.setVisible(false);
				}
				frm_Loading.add(createStartPage(name, rank));
			}
		});
		mdp.getSaveButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(ServerCommands.MAKE_DECK_SAVE);
				for (int i = 0; i < mdp.deck_list.getModel().getSize(); i++){
					String n = mdp.deck_list.getModel().getElementAt(i);
					System.out.println(n);
					out.println(n);
				}
				out.println(ServerCommands.MAKE_DECK_SAVE_DONE);
			}
		});
		mdp.getAddButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mdp.collection.getSelectedIndex() != -1) {
					mdp.addCardToDeck(mdp.collection.getModel().getElementAt(mdp.collection.getSelectedIndex()));
				}
			}
		});
		mdp.getDeleteButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mdp.deck_list.getSelectedIndex() != -1) {
					mdp.removeCardFromDeck(mdp.deck_list.getSelectedIndex());
				}
			}
		});
		return mdp;
	}
	
	private static PackOpeningPanel createPackOpeningPage () {
		final PackOpeningPanel pop = new PackOpeningPanel();
		out.println(ServerCommands.OPEN_PACK_GET_INFO);
		try {
			String input = in.readLine();
			String packs = input.substring(input.indexOf(ServerCommands.OPEN_PACK_PACK_PREFIX) + ServerCommands.OPEN_PACK_PACK_PREFIX.length(), input.indexOf(ServerCommands.OPEN_PACK_GOLD_PREFIX));
			String gold = input.substring(input.indexOf(ServerCommands.OPEN_PACK_GOLD_PREFIX) + ServerCommands.OPEN_PACK_GOLD_PREFIX.length());
			pop.getPacksLabel().setText(UIStrings.OPEN_PACK_PACK_PREFIX + packs);
			pop.getGoldLabel().setText(UIStrings.OPEN_PACK_GOLD_PREFIX + gold);
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		pop.getBackButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Component c: frm_Loading.getContentPane().getComponents()) {
					c.setVisible(false);
				}
				frm_Loading.add(createStartPage(name, rank));
			}
		});
		pop.getBuyButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(ServerCommands.OPEN_PACK_BUY_PACK);
				String response = "";
				try {
					response = in.readLine();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (response.equals(ServerCommands.OPEN_PACK_NO_GOLD)) {
					JOptionPane.showMessageDialog(frm_Loading, UIStrings.OPEN_PACK_NO_GOLD, UIStrings.OPEN_PACK_NO_GOLD_TITLE, JOptionPane.INFORMATION_MESSAGE);
				}
				out.println(ServerCommands.OPEN_PACK_GET_INFO);
				try {
					String input = in.readLine();
					String packs = input.substring(input.indexOf(ServerCommands.OPEN_PACK_PACK_PREFIX) + ServerCommands.OPEN_PACK_PACK_PREFIX.length(), input.indexOf(ServerCommands.OPEN_PACK_GOLD_PREFIX));
					String gold = input.substring(input.indexOf(ServerCommands.OPEN_PACK_GOLD_PREFIX) + ServerCommands.OPEN_PACK_GOLD_PREFIX.length());
					pop.getPacksLabel().setText(UIStrings.OPEN_PACK_PACK_PREFIX + packs);
					pop.getGoldLabel().setText(UIStrings.OPEN_PACK_GOLD_PREFIX + gold);
				} catch (IOException e3) {
					e3.printStackTrace();
				}
			}
		});
		pop.getOpenPackButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pop.getOpenPackButton().setEnabled(false);
				ArrayList<String> pack = new ArrayList<>();
				ArrayList<String> rarities = new ArrayList<>();
				out.println(ServerCommands.OPEN_PACK_OPEN);
				String cardname = "";
				try {
					cardname = in.readLine();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				boolean hasPack = true;
				while (!cardname.equals(ServerCommands.OPEN_PACK_OPEN_DONE)) {
					if (cardname.equals(ServerCommands.OPEN_PACK_NO_PACKS)) {
						hasPack = false;
						break;
					}
					if (!cardname.equals("")) {
						String rarity = cardname.substring(cardname.indexOf("RARITY:::") + "RARITY:::".length());
						cardname = cardname.substring(0, cardname.indexOf("RARITY:::"));
						pack.add(cardname);
						rarities.add(rarity);
						//System.out.println("CARD FOR PACK: " + cardname + " OF RARITY: " + rarity);
					}
					try {
						while (!in.ready()) {
							
						}
						if (in.ready()) {
							cardname = in.readLine();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (hasPack) {
					for (int i = 0; i < pop.getCardPlaces().size(); i++) {
						pop.getCardPlaces().get(i).setText(pack.get(i));
						String rarity = rarities.get(i);
						pop.getCardPlaces().get(i).setBackground(Color.DARK_GRAY);
						pop.getCardPlaces().get(i).setOpaque(false);
						pop.getCardPlaces().get(i).setBorderPainted(true);
						if (rarity.equals("legendary")) {
							pop.getCardPlaces().get(i).setBackground(Color.yellow);
							pop.getCardPlaces().get(i).setOpaque(true);
							pop.getCardPlaces().get(i).setBorderPainted(false);
						} else if (rarity.equals("epic")) {
							pop.getCardPlaces().get(i).setBackground(Color.MAGENTA);
							pop.getCardPlaces().get(i).setOpaque(true);
							pop.getCardPlaces().get(i).setBorderPainted(false);
						} else if (rarity.equals("rare")) {
							pop.getCardPlaces().get(i).setBackground(Color.cyan);
							pop.getCardPlaces().get(i).setOpaque(true);
							pop.getCardPlaces().get(i).setBorderPainted(false);
						}
					}
				} else {
					JOptionPane.showMessageDialog(frm_Loading, UIStrings.OPEN_PACK_NO_PACKS, UIStrings.OPEN_PACK_NO_PACKS_TITLE, JOptionPane.INFORMATION_MESSAGE);
				}
				out.println(ServerCommands.OPEN_PACK_GET_INFO);
				try {
					String input = in.readLine();
					String packs = input.substring(input.indexOf(ServerCommands.OPEN_PACK_PACK_PREFIX) + ServerCommands.OPEN_PACK_PACK_PREFIX.length(), input.indexOf(ServerCommands.OPEN_PACK_GOLD_PREFIX));
					String gold = input.substring(input.indexOf(ServerCommands.OPEN_PACK_GOLD_PREFIX) + ServerCommands.OPEN_PACK_GOLD_PREFIX.length());
					pop.getPacksLabel().setText(UIStrings.OPEN_PACK_PACK_PREFIX + packs);
					pop.getGoldLabel().setText(UIStrings.OPEN_PACK_GOLD_PREFIX + gold);
				} catch (IOException e3) {
					e3.printStackTrace();
				}
				pop.getOpenPackButton().setEnabled(true);
			}
		});
		return pop;
	}
	
	private static CollectionPanel createCollectionPage() {
		final CollectionPanel cp = new CollectionPanel();
		out.println(ServerCommands.COLLECTION_GET_WHOLE);
		try {
			String input = "";
			while (!input.equals(ServerCommands.COLLECTION_GET_WHOLE_DONE)) {
				if (!input.equals("")) {
					cp.addCardToCollection(input);
				}
				while(!in.ready()){
				}
				if(in.ready()) {
					input = in.readLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println(ServerCommands.COLLECTION_GET_DUST);
		try {
			while (!in.ready()) {
			}
			String dust = in.readLine();
			cp.getDustLabel().setText(UIStrings.COLLECTION_DUST_PREFIX + dust);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		cp.getBackButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Component c: frm_Loading.getContentPane().getComponents()) {
					c.setVisible(false);
				}
				frm_Loading.add(createStartPage(name, rank));
			}
		});
		cp.getSearchButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cp.resetCollection();
				JList<String> l = cp.getCollectionList();
				ArrayList<String> remove_list = new ArrayList<>();
				for (int i = 0; i < l.getModel().getSize(); i++) {
					if (!l.getModel().getElementAt(i).contains(cp.getSearchField().getText())) {
						remove_list.add(l.getModel().getElementAt(i));
					}
				}
				for (String str: remove_list) {
					cp.removeCardFromCollection(str);
				}
			}
		});
		cp.getDustButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(ServerCommands.COLLECTION_DUST_PREFIX + cp.getCollectionList().getSelectedValue());
				try{
					while (!in.ready()) {
						
					}
					String response = in.readLine();
					if (response.equals(ServerCommands.COLLECTION_DUST_NOT_HAVE)) {
						JOptionPane.showMessageDialog(frm_Loading, UIStrings.COLLECTION_DONT_OWN, UIStrings.COLLECTION_DONT_OWN_TITLE, JOptionPane.INFORMATION_MESSAGE);
					} else if (response.equals(ServerCommands.COLLECTION_DUST_BASIC_ERR)) {
						JOptionPane.showMessageDialog(frm_Loading, UIStrings.COLLECTION_BASIC_D, UIStrings.COLLECTION_BASIC_D_TITLE, JOptionPane.INFORMATION_MESSAGE);
					} else if (response.equals(ServerCommands.COLLECTION_DUST_NON_SELC)) {

					} else if (response.equals(ServerCommands.COLLECTION_DUST_SUCCESS)) {
						out.println(ServerCommands.COLLECTION_GET_DUST);
						try {
							while (!in.ready()) {
							}
							String dust = in.readLine();
							cp.getDustLabel().setText(UIStrings.COLLECTION_DUST_PREFIX + dust);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						cp.removeAllCards();
						out.println(ServerCommands.COLLECTION_GET_WHOLE);
						try {
							String input = "";
							while (!input.equals(ServerCommands.COLLECTION_GET_WHOLE_DONE)) {
								if (!input.equals("")) {
									cp.addCardToCollection(input);
								}
								while(!in.ready()){
								}
								if(in.ready()) {
									input = in.readLine();
								}
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						};
						cp.getSearchButton().doClick();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		cp.getCraftButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println(ServerCommands.COLLECTION_CRAFT_PREFIX + cp.getCollectionList().getSelectedValue());
				try{
					while (!in.ready()) {
						
					}
					String response = in.readLine();
					if (response.equals(ServerCommands.COLLECTION_CRAFT_BASIC_ERR)) {
						JOptionPane.showMessageDialog(frm_Loading, UIStrings.COLLECTION_BASIC_C, UIStrings.COLLECTION_BASIC_C_TITLE, JOptionPane.INFORMATION_MESSAGE);
					} else if (response.equals(ServerCommands.COLLECTION_CRAFT_FAIL_DUST)) {
						JOptionPane.showMessageDialog(frm_Loading, UIStrings.COLLECTION_NO_DUST, UIStrings.COLLECTION_NO_DUST_TITLE, JOptionPane.INFORMATION_MESSAGE);
					} else if (response.equals(ServerCommands.COLLECTION_CRAFT_NON_SELEC)) {
						
					} else if (response.equals(ServerCommands.COLLECTION_CRAFT_SUCCESS)) {
						out.println(ServerCommands.COLLECTION_GET_DUST);
						try {
							while (!in.ready()) {
							}
							String dust = in.readLine();
							cp.getDustLabel().setText(UIStrings.COLLECTION_DUST_PREFIX + dust);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						cp.removeAllCards();
						out.println(ServerCommands.COLLECTION_GET_WHOLE);
						try {
							String input = "";
							while (!input.equals(ServerCommands.COLLECTION_GET_WHOLE_DONE)) {
								if (!input.equals("")) {
									cp.addCardToCollection(input);
								}
								while(!in.ready()){
								}
								if(in.ready()) {
									input = in.readLine();
								}
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						};
						// Reset search bar
						JList<String> l = cp.getCollectionList();
						ArrayList<String> remove_list = new ArrayList<>();
						for (int i = 0; i < l.getModel().getSize(); i++) {
							if (!l.getModel().getElementAt(i).contains(cp.getSearchField().getText())) {
								remove_list.add(l.getModel().getElementAt(i));
							}
						}
						for (String str: remove_list) {
							cp.removeCardFromCollection(str);
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		return cp;
	}
}