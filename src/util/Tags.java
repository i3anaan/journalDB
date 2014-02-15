package util;

import java.util.ArrayList;

public class Tags extends ArrayList<String>{
	
	public Tags(){
		
	}
	
	public void addTag(String tag){
		this.add(tag);
	}
	
	public String[] getArray(){
		String[] arr = new String[this.size()];
		for(int i=0;i<this.size();i++){
			arr[i] = this.get(i);
		}
		
		return arr;
	}
}
