package com.ies.schoolos.ui.mobile;

import com.ies.schoolos.ui.mobile.teaching.PersonalTeachingView;

public class SchoolOSView extends PersonalTeachingView{
	private static final long serialVersionUID = 1L;
	/* Component */
	private PersonalTeachingView personalTeachingView;
	
	
	public SchoolOSView() {		
		buildMainLayout();
	}
	
	private void buildMainLayout(){
		personalTeachingView = new PersonalTeachingView();
		setCurrentComponent(personalTeachingView);
	}
}
