package interfaces;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;

import util.JTextAreaHint;
import connection.Connector;
import connection.SQLCommander;

public class TagsManager extends JFrame implements ActionListener,DocumentListener{

	private JTextAreaHint tagArea;
	private JTextAreaHint inuseArea;
	private JButton saveButton;
	
	public TagsManager(){
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    	
    	this.setSize(new Dimension(510,100));
    	this.setResizable(false);
    	
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    	int taskBarHeight = dim.height - winSize.height;
    	this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		
		saveButton = new JButton("Save tag");
		saveButton.setEnabled(false);
		saveButton.setPreferredSize(new Dimension(100,0));
		saveButton.addActionListener(this);
		
		JPanel inputPanel = new JPanel(new BorderLayout(1,1));
		inputPanel.setBackground(GUI.color);
		tagArea = new JTextAreaHint("tag");
		inuseArea = new JTextAreaHint("inuse");
		tagArea.setPreferredSize(new Dimension(199,0));
		inuseArea.setPreferredSize(new Dimension(199,0));
		tagArea.getDocument().addDocumentListener(this);
		inuseArea.getDocument().addDocumentListener(this);
		
		inputPanel.add(tagArea,BorderLayout.WEST);
		inputPanel.add(inuseArea,BorderLayout.EAST);
		
		
		
		this.add(saveButton,BorderLayout.EAST);
		this.add(inputPanel,BorderLayout.CENTER);
		this.setVisible(true);
		inputPanel.requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			SQLCommander.insertTag(tagArea.getText(),Integer.parseInt(inuseArea.getText()));
		}catch (SQLException e1) {
			System.err.println("SQL error");
		}
		
		saveButton.setEnabled(false);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		saveButton.setEnabled(allowSave());
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		saveButton.setEnabled(allowSave());		
	}
	
	private boolean isInUseInputValid(){
		try{
			Integer.parseInt(inuseArea.getText());
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	public boolean allowSave(){
		return tagArea.hasInput() && inuseArea.hasInput() && isInUseInputValid();
	}
}
