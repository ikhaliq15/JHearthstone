package net.theinterweb.Client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

public class LoginPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2844664515768867717L;
	public JPasswordField jpf_Password;
	public JTextField jtf_Username;
	public JButton btn_SignUp;
	public JButton btn_Submit;
	
	/**
	 * Create the panel.
	 */
	public LoginPanel() {
		setLayout(null);
		
		jpf_Password = new JPasswordField();
		jpf_Password.setBounds(123, 188, 254, 28);
		jpf_Password.setColumns(20);
		add(jpf_Password);
		
		btn_Submit = new JButton("Login");
		btn_Submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn_Submit.setBounds(123, 228, 117, 29);
		add(btn_Submit);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(54, 194, 63, 16);
		add(lblPassword);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(47, 127, 70, 16);
		add(lblUsername);
		
		jtf_Username = new JTextField();
		jtf_Username.setBounds(123, 121, 254, 28);
		add(jtf_Username);
		jtf_Username.setColumns(20);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(434, 20, 245, 260);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblUpdateNotes = new JLabel("Update Notes:");
		lblUpdateNotes.setBounds(6, 6, 89, 16);
		panel.add(lblUpdateNotes);
		
		JTextPane txtpn_UpdateNotes = new JTextPane();
		txtpn_UpdateNotes.setEditable(false);
		txtpn_UpdateNotes.setBackground(this.getBackground());
		txtpn_UpdateNotes.setText("* Added Support for Multiplayer\n\n\n* Support for login\n\n\n* Server can hold multiple games at once");
		txtpn_UpdateNotes.setBounds(16, 34, 211, 207);
		panel.add(txtpn_UpdateNotes);
		
		JLabel lblNewLabel = new JLabel("Hearthstone");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 35));
		lblNewLabel.setBounds(150, 35, 211, 61);
		add(lblNewLabel);
				
		btn_SignUp = new JButton("Sign Up");
		btn_SignUp.setBounds(260, 228, 117, 29);
		add(btn_SignUp);

	}
}
