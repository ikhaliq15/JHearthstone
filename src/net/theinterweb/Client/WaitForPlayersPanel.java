package net.theinterweb.Client;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class WaitForPlayersPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6898554041111765758L;

	/**
	 * Create the panel.
	 */
	public WaitForPlayersPanel() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Waiting for an opponent...");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblNewLabel.setBounds(289, 143, 249, 25);
		add(lblNewLabel);

	}

}
