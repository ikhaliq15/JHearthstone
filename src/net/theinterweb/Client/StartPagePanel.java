package net.theinterweb.Client;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class StartPagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5257896089467509368L;
	private JLabel lblWelcome;
	private JLabel lblYouAreRank;
	private JButton btnNewButton;
	private JButton btnMakeDeck;
	private JButton btnLogout;
	private JButton btnOpenPack;
	private JButton btnCollection;

	/**
	 * Create the panel.
	 */
	public StartPagePanel() {
		setLayout(null);
		
		btnNewButton = new JButton("Play");
		btnNewButton.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		btnNewButton.setBounds(141, 84, 149, 52);
		add(btnNewButton);
		
		btnMakeDeck = new JButton("Make Deck");
		btnMakeDeck.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		btnMakeDeck.setBounds(141, 144, 149, 52);
		add(btnMakeDeck);
		
		btnOpenPack = new JButton("Open Pack");
		btnOpenPack.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		btnOpenPack.setEnabled(false);
		btnOpenPack.setBounds(141, 208, 149, 52);
		add(btnOpenPack);
		
		JLabel lblHearthstone = new JLabel("Hearthstone");
		lblHearthstone.setFont(new Font("Lucida Grande", Font.PLAIN, 30));
		lblHearthstone.setBounds(131, 32, 178, 36);
		add(lblHearthstone);
		
		lblWelcome = new JLabel("Welcome, ");
		lblWelcome.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblWelcome.setBounds(391, 100, 224, 25);
		add(lblWelcome);
		
		lblYouAreRank = new JLabel("You are rank ");
		lblYouAreRank.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblYouAreRank.setBounds(391, 142, 265, 25);
		add(lblYouAreRank);
		
		JLabel lblYouHave = new JLabel("You have ");
		lblYouHave.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblYouHave.setBounds(391, 173, 224, 25);
		add(lblYouHave);
		
		JTextArea txtrAllPropertiesBelong = new JTextArea();
		txtrAllPropertiesBelong.setText("All properties belong to their respective owners.\nDeveloped by Imran Khaliq.");
		txtrAllPropertiesBelong.setBounds(391, 262, 303, 32);
		txtrAllPropertiesBelong.setBackground(this.getBackground());
		add(txtrAllPropertiesBelong);
		
		btnLogout = new JButton("Logout");
		btnLogout.setBounds(6, 265, 117, 29);
		add(btnLogout);
		
		btnCollection = new JButton("Collection");
		btnCollection.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		btnCollection.setBounds(466, 208, 149, 52);
		add(btnCollection);

	}
	public JLabel getLblWelcome() {
		return lblWelcome;
	}
	public JLabel getRankLabel() {
		return lblYouAreRank;
	}
	public JButton getPlayButton() {
		return btnNewButton;
	}
	public JButton getMakeDeckButton() {
		return btnMakeDeck;
	}
	public JButton getLogoutButton() {
		return btnLogout;
	}
	public JButton getOpenPackButton() {
		return btnOpenPack;
	}
	public JButton getCollectionButton() {
		return btnCollection;
	}
}
