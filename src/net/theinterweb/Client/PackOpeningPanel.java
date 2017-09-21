package net.theinterweb.Client;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class PackOpeningPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9221783011375337839L;
	private JButton btnBack;
	private JButton btnOpenAPack;
	
	private ArrayList<JButton> card_places = new ArrayList<JButton>();
	private JLabel lblPacks;
	private JLabel lblGold;
	private JButton btnNewButton;

	/**
	 * Create the panel.
	 */
	public PackOpeningPanel() {
		setLayout(null);
		
		btnNewButton = new JButton("Buy a Pack");
		btnNewButton.setBounds(26, 102, 117, 29);
		add(btnNewButton);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(26, 174, 117, 29);
		add(btnBack);
		
		btnOpenAPack = new JButton("Open a Pack");
		btnOpenAPack.setBounds(26, 139, 117, 29);
		add(btnOpenAPack);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setBounds(350, 20, 125, 68);
		add(btnNewButton_1);
		card_places.add(btnNewButton_1);
		
		JButton button = new JButton("");
		button.setEnabled(false);
		button.setBounds(217, 102, 125, 68);
		add(button);
		card_places.add(button);

		
		JButton button_1 = new JButton("");
		button_1.setEnabled(false);
		button_1.setBounds(486, 102, 125, 68);
		add(button_1);
		card_places.add(button_1);
		
		JButton button_2 = new JButton("");
		button_2.setEnabled(false);
		button_2.setBounds(281, 182, 125, 68);
		add(button_2);
		card_places.add(button_2);
		
		JButton button_3 = new JButton("");
		button_3.setEnabled(false);
		button_3.setBounds(418, 182, 125, 68);
		add(button_3);
		card_places.add(button_3);
		
		lblPacks = new JLabel("Packs: ");
		lblPacks.setBounds(36, 215, 125, 16);
		add(lblPacks);
		
		lblGold = new JLabel("Gold: ");
		lblGold.setBounds(35, 234, 125, 16);
		add(lblGold);

	}

	public JButton getBackButton() {
		return btnBack;
	}
	public JButton getOpenPackButton() {
		return btnOpenAPack;
	}
	
	public ArrayList<JButton> getCardPlaces() {
		return card_places;
	}
	public JLabel getPacksLabel() {
		return lblPacks;
	}
	public JLabel getGoldLabel() {
		return lblGold;
	}
	public JButton getBuyButton() {
		return btnNewButton;
	}
}
