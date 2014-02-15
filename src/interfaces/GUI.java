package interfaces;

import javax.swing.BoxLayout;
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

import util.JTextAreaHint;
import util.Tags;
import connection.Connector;
import connection.SQLCommander;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
public class GUI extends JFrame implements ActionListener,ComponentListener, DocumentListener,ListSelectionListener{
    
	private JList<String> tagBoxes1;
	private JList<String> tagBoxes2;
	private JPanel textFields;
	private JScrollPane descriptionScroller;
	private JTextArea	descriptionArea;
	private JScrollPane locationScroller;
	private JTextArea	locationArea;
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
    	
    	Tags tags1 = null;
    	Tags tags2 = null;
        try {
        	tags1 = SQLCommander.getTags(1);
        	tags2 = SQLCommander.getTags(2);
		} catch (SQLException e) {
			System.err.println("Could not retrive tags, SQLException.");
			System.exit(0);
		}
        
        
        final JPanel tagPanel = new JPanel(new BorderLayout(0,0));
        tagPanel.setBackground(color);
        
        tagBoxes1 = new JList<String>(tags1.getArray());
        tagBoxes1.addListSelectionListener(this);
        tagBoxes1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tagBoxes2 = new JList<String>(tags2.getArray());
        tagBoxes2.addListSelectionListener(this);
        tagBoxes2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane tag1Scroller = new JScrollPane(tagBoxes1);
        JScrollPane tag2Scroller = new JScrollPane(tagBoxes2);
        
        tagPanel.add(tag1Scroller,BorderLayout.EAST);
        tagPanel.add(tag2Scroller,BorderLayout.WEST);
        
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
        
        
        
        this.add(saveButton,BorderLayout.EAST);
        this.add(textFields);
        this.add(tagPanel,BorderLayout.WEST);
        this.setVisible(true);
        this.addComponentListener(this);
        saveButton.setEnabled(false);
        Terminal.acceptInput();
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
			//tagBoxes.clearSelection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		//descriptionScroller.setPreferredSize(new Dimension(textFields.getSize().width/2, textFields.getSize().height));
		//locationScroller.setPreferredSize(new Dimension(textFields.getSize().width/2, textFields.getSize().height));
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
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
		return !descriptionArea.getText().equals("") && !locationArea.getText().equals("") && tagBoxes1.getSelectedValuesList().size()!=0 && tagBoxes2.getSelectedValuesList().size()!=0;
	}
}