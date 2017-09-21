package net.theinterweb.Client;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class MakeDeckPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6446130412187871879L;
	
	public JList<String> deck_list;
	public JList<String> collection;
	
	public ArrayList<String> whole_collection = new ArrayList<String>();
	
	private DefaultListModel<String> model = new DefaultListModel<String>();
	
	private DefaultListModel<String> collection_model = new DefaultListModel<String>();
	
	private JButton btnDelete;
	private JButton btnAdd;
	private JButton btnSave;
	private JButton btnBack;
	private JTextField textField;
	private JButton btnSearch;

	/**
	 * Create the panel.
	 */
	public MakeDeckPanel() {
		setLayout(null);
		
		JLabel lblDecklist = new JLabel("Decklist: ");
		lblDecklist.setBounds(434, 6, 60, 16);
		add(lblDecklist);
		
		JLabel lblCollection = new JLabel("Collection:");
		lblCollection.setBounds(30, 22, 68, 16);
		add(lblCollection);
		
		btnDelete = new JButton("Delete");
		btnDelete.setBounds(434, 271, 117, 29);
		add(btnDelete);
		
		btnAdd = new JButton("Add");
		btnAdd.setBounds(213, 271, 117, 29);
		add(btnAdd);
		
		btnSave = new JButton("Save");
		btnSave.setBounds(547, 271, 117, 29);
		add(btnSave);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(444, 34, 220, 235);
		add(scrollPane);
		
		deck_list = new JList<String>(model);
		deck_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(deck_list);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(30, 56, 377, 214);
		add(scrollPane_1);
		
		collection = new JList<String>(collection_model);
		collection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(collection);
		
		btnBack = new JButton("Back");
		btnBack.setBounds(81, 271, 117, 29);
		add(btnBack);
		
		textField = new JTextField();
		textField.setBounds(110, 16, 180, 28);
		add(textField);
		textField.setColumns(10);
		
		btnSearch = new JButton("Search");
		btnSearch.setBounds(290, 17, 117, 29);
		add(btnSearch);

	}

	public void addCardToDeck(String input) {
		model.addElement(input);
	}
	public JButton getDeleteButton() {
		return btnDelete;
	}

	public void removeCardFromDeck(int selectedIndex) {
		model.remove(selectedIndex);
	}

	public void addCardToCollection(String input) {
		collection_model.addElement(input);
		whole_collection.add(input);
	}
	public JButton getAddButton() {
		return btnAdd;
	}
	public JButton getSaveButton() {
		return btnSave;
	}
	public JButton getBackButton() {
		return btnBack;
	}
	public JTextField getSearchField() {
		return textField;
	}
	public JButton getSearchButton() {
		return btnSearch;
	}
	public JList<String> getCollectionList() {
		return collection;
	}

	public void resetCollection() {
		collection_model.removeAllElements();
		System.out.println("COLLECTION MODEL: " + collection_model.getSize());
		for (String s: whole_collection) {
			collection_model.addElement(s);
		}
	}
	
	public void removeCardFromCollection(String str) {
		collection_model.removeElement((String)(str));
	}
}
