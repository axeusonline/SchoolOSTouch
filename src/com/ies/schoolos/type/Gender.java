package com.ies.schoolos.type;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class Gender extends IndexedContainer{

	private static final long serialVersionUID = 1L;
	
	public static int MALE = 0;
	public static int FEMALE = 1;
	
	private static String[] genders = {"ชาย","หญิง"};

	public Gender() {
		initContainer();
	}
 
	@SuppressWarnings("unchecked")
	private void initContainer(){
	   addContainerProperty("name", String.class,null);
	   for (int i = 0; i < genders.length; i++) {
	        Item item = addItem(i);
	        item.getItemProperty("name").setValue(genders[i]);
	   }
	}
	
	public static String getNameTh(int index){
		return genders[index];
	}
	
	/*public static String getNameEn(int index){
		return genders[index];
	}*/
}
