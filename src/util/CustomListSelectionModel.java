package util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListSelectionModel;
import javax.swing.Timer;

public class CustomListSelectionModel extends DefaultListSelectionModel implements ActionListener {
	private int lastSelectedIndex = -1;
	Timer timer;
	
	@Override
    public void setSelectionInterval(int index0, int index1) {
		if(index0!=lastSelectedIndex){
			lastSelectedIndex = index0;
	        if(super.isSelectedIndex(index0)) {
	            super.removeSelectionInterval(index0, index1);
	        }
	        else {
	            super.addSelectionInterval(index0, index1);
	        }
	        timer = new Timer(100, this);
			timer.setRepeats(false);
			timer.start();
		}
    }
	@Override
	public void actionPerformed(ActionEvent arg0) {
		lastSelectedIndex = -1;		
	}
}
