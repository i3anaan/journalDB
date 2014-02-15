package util;

import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextArea;

public class JTextAreaHint extends JTextArea implements FocusListener{
	private boolean showingHint = true;
	
	public JTextAreaHint(String startText){
		super(startText);
		this.setFont(new Font("Dialog.plain", Font.ITALIC, 11));
		this.addFocusListener(this);
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
		if(showingHint){
			this.setText("");
			showingHint = false;
			this.setFont(new Font("Dialog.plain", Font.PLAIN, 12));
		}
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
