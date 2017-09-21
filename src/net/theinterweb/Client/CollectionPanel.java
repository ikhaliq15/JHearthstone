package net.theinterweb.Client;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ListSelectionModel;

public class CollectionPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2769401046622798606L;
	private JTextField txtSearch;
	private JButton btnBack;

	private DefaultListModel<String> collection_model = new DefaultListModel<String>();
	private JButton btnSearch;
	private JButton btnDust;
	private JButton btnCraft;
	private JList<String> list;
	
	private ArrayList<String> collection;
	private JLabel lblDust;
	
	/**
	 * Create the panel.
	 */
	public CollectionPanel() {
		setLayout(null);
		
		collection = new ArrayList<String>();
		
		txtSearch = new JTextField();
		txtSearch.setToolTipText("Search...");
		txtSearch.setBounds(208, 29, 278, 28);
		add(txtSearch);
		txtSearch.setColumns(100);
		
		btnSearch = new JButton("Search");
		btnSearch.setBounds(506, 30, 117, 29);
		add(btnSearch);
		
		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		lblSearch.setBounds(108, 23, 88, 31);
		add(lblSearch);
		
		btnCraft = new JButton("Craft");
		btnCraft.setBounds(245, 266, 117, 29);
		add(btnCraft);
		
		btnDust = new JButton("Dust");
		btnDust.setBounds(374, 266, 117, 29);
		add(btnDust);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 73, 646, 181);
		add(scrollPane);
		
		list = new JList<String>(collection_model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(22, 266, 117, 29);
		add(btnBack);
		
		lblDust = new JLabel("Dust:");
		lblDust.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDust.setBounds(563, 271, 106, 16);
		add(lblDust);
		
	}
	public JButton getBackButton() {
		return btnBack;
	}
	public void addCardToCollection(String name) {
		collection_model.addElement(name);
		collection.add(name);
	}
	public void removeCardFromCollection(String name) {
		collection_model.removeElement((String)(name));
	}
	public JTextField getSearchField() {
		return txtSearch;
	}
	public JButton getSearchButton() {
		return btnSearch;
	}
	public JButton getDustButton() {
		return btnDust;
	}
	public JButton getCraftButton() {
		return btnCraft;
	}
	public JList<String> getCollectionList() {
		return list;
	}
	public void resetCollection() {
		collection_model.removeAllElements();
		//System.out.println("COLLECTION MODEL: " + collection_model.getSize());
		for (String s: collection) {
			collection_model.addElement(s);
		}
	}
	public JLabel getDustLabel() {
		return lblDust;
	}
	public void removeAllCards() {
		collection_model.removeAllElements();
		collection = new ArrayList<String>();
		//System.out.println("SIZE: " + collection.size());
	}
}
