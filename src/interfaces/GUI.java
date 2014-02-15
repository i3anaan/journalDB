package interfaces;

import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import util.CustomListSelectionModel;
import util.JTextAreaHint;
import util.Tags;
import connection.Connector;
import connection.SQLCommander;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
public class GUI extends JFrame implements ActionListener, DocumentListener,ListSelectionListener{
    
	private JList<String> tagBoxes1;
	private JList<String> tagBoxes2;
	private JPanel textFields;
	private JScrollPane descriptionScroller;
	private JTextAreaHint	descriptionArea;
	private JScrollPane locationScroller;
	private JTextAreaHint	locationArea;
	private JButton saveButton;
	public static final String DEFAULT_DESC = "Description";
	public static final String DEFAULT_LOC = "Location";
	public static final Color color = new Color(255f/255f,100f/255f,0f/255f);
	public static final String DEFAULT_IP = "localhost";
	public static final int DEFAULT_PORT = 3306;
	
    public static void main(String[] args) {
    	String ip = DEFAULT_IP;
    	int port = DEFAULT_PORT;
    	if(args.length>=2){
    		if(args.length>=3){
    			ip = args[2];
    			if(args.length>=4){
    				try{
    					port = Integer.parseInt(args[3]);
    				}catch(NumberFormatException e){
    					System.out.println("Could not read portnumber.");
    					System.out.println("USAGE:\nGUI <username> <password> [ip] [portnumber]");
    		    		System.exit(0);
    				}
    			}
    		}
    		new GUI(ip,port,args[0],args[1]);
    	}else{
    		System.out.println("USAGE:\nGUI <username> <password> [ip] [port]");
    		System.exit(0);
    	}
    }

    public GUI(String ip,int port,String username,String password)
    {
    	System.out.println("MYJOURNAL DATABASE SERVER V1");
    	System.out.println("@author:  i3anaan");
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	Connector.makeConnector(ip, port, username, password);
    	
    	this.setSize(new Dimension(800,400));
    	
    	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    	Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    	int taskBarHeight = dim.height - winSize.height;
    	this.setLocation(dim.width-this.getSize().width, dim.height-this.getSize().height-taskBarHeight);
    	
    	this.setLayout(new BorderLayout(10,10));
    	this.setBackground(color);
    	this.getContentPane().setBackground(color);
    	
    	
        
        
        final JPanel tagPanel = new JPanel(new BorderLayout(0,0));
        tagPanel.setBackground(color);
        
        tagBoxes1 = new JList<String>();
        tagBoxes1.addListSelectionListener(this);
        tagBoxes1.setSelectionModel(new CustomListSelectionModel());
        tagBoxes2 = new JList<String>();
        tagBoxes2.addListSelectionListener(this);
        tagBoxes2.setSelectionModel(new CustomListSelectionModel());
        
        this.fillTagLists();
        
        JScrollPane tag1Scroller = new JScrollPane(tagBoxes1);
        JScrollPane tag2Scroller = new JScrollPane(tagBoxes2);
        
        tagPanel.add(tag1Scroller,BorderLayout.WEST);
        tagPanel.add(tag2Scroller,BorderLayout.EAST);
        
        descriptionArea = new JTextAreaHint(DEFAULT_DESC);
        descriptionArea.getDocument().addDocumentListener(this);
		descriptionScroller = new JScrollPane(descriptionArea);
        
        locationArea = new JTextAreaHint(DEFAULT_LOC);
        locationScroller = new JScrollPane(locationArea);
        locationScroller.setPreferredSize(new Dimension(0,50));
        
        
        textFields = new JPanel();
        textFields.setLayout(new BorderLayout());
        textFields.add(descriptionScroller,BorderLayout.CENTER);
        textFields.add(locationScroller,BorderLayout.NORTH);
        
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        
        JPanel menuPanel = new JPanel(new BorderLayout(1,1));
        JPanel menuButtons = new JPanel();
        BoxLayout boxLayout = new BoxLayout(menuButtons,BoxLayout.Y_AXIS);
        menuButtons.setLayout(boxLayout);
        menuPanel.setBackground(color);
        
        JButton manageTags = new JButton("manage tags");
        manageTags.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TagsManager();
				
			}
		}); //TODO Dit niet meerdere keren laten openen.
        manageTags.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        JButton updateTags = new JButton("update tags");
        updateTags.addActionListener(new TagUpdateListener(this));
        updateTags.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButtons.add(updateTags);
        menuButtons.add(manageTags);
        
        menuPanel.add(menuButtons,BorderLayout.SOUTH);
        menuPanel.add(tagPanel,BorderLayout.CENTER);
        
        this.add(saveButton,BorderLayout.EAST);
        this.add(textFields);
        this.add(menuPanel,BorderLayout.WEST);
        this.setVisible(true);
        saveButton.setEnabled(false);
        Terminal.acceptInput();
    }
    
    private void fillTagLists(){
    	Tags tags1 = null;
    	Tags tags2 = null;
        try {
        	tags1 = SQLCommander.getTags(1);
        	tags2 = SQLCommander.getTags(2);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Could not retrieve tags, SQLException.");
			System.exit(0);
		}
        tagBoxes1.setListData(tags1.getArray());
        tagBoxes2.setListData(tags2.getArray());
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Tags tags = new Tags();
		for(String s:tagBoxes1.getSelectedValuesList()){
			tags.add(s);
		}
		for(String s:tagBoxes2.getSelectedValuesList()){
			tags.add(s);
		}
		
		try {
			SQLCommander.insertRecord(tags, descriptionArea.getText(), locationArea.getText());
			saveButton.setEnabled(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		saveButton.setEnabled(allowSave());
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		saveButton.setEnabled(allowSave());		
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		saveButton.setEnabled(allowSave());		
	}    
	
	public boolean allowSave(){
		return descriptionArea.hasInput() && locationArea.hasInput() && tagBoxes1.getSelectedValuesList().size()!=0 && tagBoxes2.getSelectedValuesList().size()!=0;
	}
	private class TagUpdateListener implements ActionListener{
		private GUI gui;
		public TagUpdateListener(GUI gui){
			this.gui = gui;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			gui.fillTagLists();
		}
	}
	
}

