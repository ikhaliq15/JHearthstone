package net.theinterweb.Client;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class SignUpPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5748232366762694012L;
	public JTextField txt_Username;
	public JPasswordField psf_Password;
	public JPasswordField pwf_Retype;
	private JButton btn_Login;
	private JButton btn_SignUp;

	/**
	 * Create the panel.
	 */
	public SignUpPanel() {
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Hearthstone");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 35));
		lblNewLabel.setBounds(150, 35, 211, 61);
		add(lblNewLabel);
		
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
		
		JLabel label = new JLabel("Username:");
		label.setBounds(58, 103, 70, 16);
		add(label);
		
		txt_Username = new JTextField();
		txt_Username.setColumns(20);
		txt_Username.setBounds(134, 97, 254, 28);
		add(txt_Username);
		
		JLabel label_1 = new JLabel("Password:");
		label_1.setBounds(65, 151, 63, 16);
		add(label_1);
		
		psf_Password = new JPasswordField();
		psf_Password.setColumns(20);
		psf_Password.setBounds(134, 145, 254, 28);
		add(psf_Password);
		
		pwf_Retype = new JPasswordField();
		pwf_Retype.setColumns(20);
		pwf_Retype.setBounds(134, 191, 254, 28);
		add(pwf_Retype);
		
		JLabel lblRetypePassword = new JLabel("Retype Password:");
		lblRetypePassword.setBounds(19, 197, 109, 16);
		add(lblRetypePassword);
		
		btn_Login = new JButton("Login");
		btn_Login.setBounds(134, 240, 117, 29);
		add(btn_Login);
		
		btn_SignUp = new JButton("Sign Up");
		btn_SignUp.setBounds(271, 240, 117, 29);
		add(btn_SignUp);

	}
	public JButton getBtn_Login() {
		return btn_Login;
	}
	public JButton getBtn_SignUp() {
		return btn_SignUp;
	}
}
