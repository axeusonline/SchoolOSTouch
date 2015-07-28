package com.ies.schoolos.ui.mobile.teaching;

import java.util.ArrayList;

import com.ies.schoolos.container.Container;
import com.ies.schoolos.schema.SessionSchema;
import com.ies.schoolos.schema.UserSchema;
import com.ies.schoolos.schema.academic.TeachingSchema;
import com.ies.schoolos.schema.academic.TimetableSchema;
import com.ies.schoolos.schema.fundamental.ClassRoomSchema;
import com.ies.schoolos.schema.fundamental.SubjectSchema;
import com.ies.schoolos.schema.info.PersonnelSchema;
import com.ies.schoolos.schema.info.StudentClassRoomSchema;
import com.ies.schoolos.schema.info.StudentSchema;
import com.ies.schoolos.schema.info.StudentStudySchema;
import com.ies.schoolos.type.Gender;
import com.ies.schoolos.type.Prename;
import com.ies.schoolos.type.UserType;
import com.ies.schoolos.ui.mobile.SearchView;
import com.ies.schoolos.ui.mobile.component.ManagerView;
import com.ies.schoolos.ui.mobile.info.EditUserView;
import com.ies.schoolos.ui.mobile.info.StudentReadOnlyView;
import com.ies.schoolos.utility.Adsense;
import com.ies.schoolos.utility.DateTimeUtil;
import com.ies.schoolos.utility.Images;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class PersonalTeachingView extends ManagerView {
	private static final long serialVersionUID = 1L;

	private ArrayList<String> timetableStores = new ArrayList<>();
	
    private Object userId;
	private Item userItem;
	private Item personItem;
	
	private Container container;
	private SQLContainer userContainer;
	private SQLContainer studyContainer;
	private SQLContainer studentContainer; 
	private SQLContainer personnelContainer;  
	private SQLContainer freeFormContainer;
	private SQLContainer freeForm2Container;
	
    private String image;
    private String email;
	
    private VerticalComponentGroup studentGroup;
    private VerticalComponentGroup classRoomGroup;
    
	/* ข้อมูลส่วนตัว */
	private Panel panel;
	private VerticalLayout profileLayout;
	private Image classImage;
	private Label nameLabel;
	private Label emailLabel;
	
	private Button studentButton;
	private Button classButton;
	
	public PersonalTeachingView() {
		container = new Container();
		
		fetchUserData();
		buildMainLayout();

		Responsive.makeResponsive(studentGroup);
		Responsive.makeResponsive(classRoomGroup);
	}
	
	public PersonalTeachingView(boolean isStudentVisible) {
		container = new Container();
		
		fetchUserData();
		buildMainLayout();
		
		studentGroup.setVisible(isStudentVisible);
		classRoomGroup.setVisible(!isStudentVisible);
		if(isStudentVisible)
			studentButton.click();
		else
			classButton.click();
		
		Responsive.makeResponsive(studentGroup);
		Responsive.makeResponsive(classRoomGroup);
	}
	
	/* สร้าง Content */
	private void buildMainLayout(){
		panel = new Panel();
		navView.setContent(panel);
		
		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setMargin(true);
		panel.setContent(mainLayout);
		
		profileLayout = new VerticalLayout();
		profileLayout.setSpacing(true);
		mainLayout.addComponent(profileLayout);
		mainLayout.setComponentAlignment(profileLayout, Alignment.MIDDLE_CENTER);
		
		classImage = new Image();
		classImage.setSource(new ThemeResource(image));
		profileLayout.addComponent(classImage);
		profileLayout.setComponentAlignment(classImage, Alignment.MIDDLE_CENTER);
	    
	    nameLabel = new Label(SessionSchema.getFirstname() + " " + SessionSchema.getLastname());
	    nameLabel.setSizeFull();
	    nameLabel.setStyleName("name-profile");
	    profileLayout.addComponent(nameLabel);
	    
	    emailLabel = new Label();
	    emailLabel.setSizeFull();
	    emailLabel.setStyleName("email-profile");
	    emailLabel.setValue(email);
	    profileLayout.addComponent(emailLabel);
	
	    Button editButton = new Button("แก้ไขข้อมูล");
	    editButton.setStyleName("edit-button");
	    editButton.addClickListener(new com.vaadin.ui.Button.ClickListener() {
            private static final long serialVersionUID = 1L;
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event)
            {
                navigateTo(new EditUserView(new PersonalTeachingView(studentGroup.isVisible())));
            }
        });
	    profileLayout.addComponent(editButton);
		profileLayout.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout submenuLayout = new HorizontalLayout();
		submenuLayout.setWidth("100%");
		profileLayout.addComponent(submenuLayout);
		
		studentButton = new Button(FontAwesome.USER);
		studentButton.setSizeFull();
		studentButton.setStyleName("submenu-button-focus");
		studentButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				studentButton.setStyleName("submenu-button-focus");
				classButton.setStyleName("submenu-button");
				
				studentGroup.setVisible(true);
				classRoomGroup.setVisible(false);
			}
		});
		submenuLayout.addComponent(studentButton);
		
		classButton = new Button(FontAwesome.SUITCASE);
		classButton.setSizeFull();
		classButton.setStyleName("submenu-button");
		classButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				studentButton.setStyleName("submenu-button");
				classButton.setStyleName("submenu-button-focus");
				
				studentGroup.setVisible(false);
				classRoomGroup.setVisible(true);
			}
		});
		submenuLayout.addComponent(classButton);
		
		studentGroup = new VerticalComponentGroup();
		studentGroup.setStyleName("student-list");
		studentGroup.setVisible(false);
		profileLayout.addComponent(studentGroup);
		
		//NativeJS ads = new NativeJS(new Adsense.getAdsenseUrl());
		Label adsLabel = new Label(Adsense.getAdsenseUrl(), ContentMode.HTML);
		adsLabel.setWidth("100%");
		studentGroup.addComponent(adsLabel);
		
		fetchStudentsData();
		
		classRoomGroup = new VerticalComponentGroup();
		classRoomGroup.setStyleName("class-list");
		classRoomGroup.setVisible(false);
		profileLayout.addComponent(classRoomGroup);
		fetchClassRoomData();
		
		studentButton.click();
	}

	/* ดึงข้อมูลส่วนตัว */
	private void fetchUserData(){
        userItem = null;
		userContainer = container.getUserContainer();
        userItem = userContainer.getItem(new RowId(SessionSchema.getUserID()));
        if(userItem.getItemProperty(UserSchema.REF_USER_TYPE).getValue().toString().equals(Integer.toString(UserType.EMPLOYEE))){
            userId = userItem.getItemProperty(UserSchema.REF_USER_ID).getValue();
            personnelContainer = container.getPersonnelContainer();
            personnelContainer.addContainerFilter(new Equal(PersonnelSchema.PERSONNEL_ID, userId));
            personItem = personnelContainer.getItem(new RowId(userId));

            email = userItem.getItemProperty(UserSchema.EMAIL).getValue().toString();
            if(personItem.getItemProperty(PersonnelSchema.GENDER).getValue().toString().equals(Integer.toString(Gender.MALE)))
				image = Images.MALE;
        	else
        		image = Images.FEMALE;
            
        }else if(userItem.getItemProperty(UserSchema.REF_USER_TYPE).getValue().toString().equals(Integer.toString(UserType.STUDENT))){
        		studentContainer = container.getStudentContainer();
	            studentContainer.addContainerFilter(new Equal(StudentStudySchema.STUDENT_ID, userItem.getItemProperty(UserSchema.REF_USER_ID).getValue()));
	            
	            studyContainer = container.getStudentStudyContainer();
	            studyContainer.addContainerFilter(new Equal(StudentStudySchema.STUDENT_ID, userItem.getItemProperty(UserSchema.REF_USER_ID).getValue()));
	            
	            userId = Integer.parseInt(studyContainer.getIdByIndex(studyContainer.size()-1).toString());
	            personItem = studyContainer.getItem(new RowId(userId));
	           
	            email = userItem.getItemProperty(UserSchema.EMAIL).getValue().toString();
	            Item studentItem = studentContainer.getItem(studentContainer.getIdByIndex(0));
	        	if(studentItem.getItemProperty(StudentSchema.GENDER).getValue().toString().equals(Integer.toString(Gender.MALE)))
	        		image = Images.MALE;
	        	else
	        		image = Images.FEMALE;
        }else{
        	image = Images.SCHOOL;
        }
	 }
	
	/* ดึงข้อมูลนักเรียน */
	private void fetchStudentsData(){
        StringBuilder classRoomSQL = new StringBuilder();
        classRoomSQL.append(" SELECT tt." + TimetableSchema.TIMETABLE_ID + ",cr." + ClassRoomSchema.CLASS_ROOM_ID +",tc." + TeachingSchema.ACADEMIC_YEAR);
        classRoomSQL.append(" FROM " + TimetableSchema.TABLE_NAME + " tt");
        classRoomSQL.append(" INNER JOIN " + ClassRoomSchema.TABLE_NAME + " cr ON tt." + TimetableSchema.CLASS_ROOM_ID + "= cr." + ClassRoomSchema.CLASS_ROOM_ID);
        classRoomSQL.append(" INNER JOIN " + TeachingSchema.TABLE_NAME + " tc ON tt." + TimetableSchema.TEACHING_ID + "= tc." +  TeachingSchema.TEACHING_ID);
        classRoomSQL.append(" WHERE tc." + TeachingSchema.ACADEMIC_YEAR + "=" + DateTimeUtil.getBuddishYear());
        classRoomSQL.append(" AND tc." + TeachingSchema.PERSONNEL_ID + "=" + SessionSchema.getRefID());
        classRoomSQL.append(" AND tt." + TimetableSchema.SCHOOL_ID + "=" + SessionSchema.getSchoolID());
        classRoomSQL.append(" GROUP BY cr." + ClassRoomSchema.CLASS_ROOM_ID);
        classRoomSQL.append(" ORDER BY tt." + TimetableSchema.SEMESTER + " DESC, cr."+ ClassRoomSchema.CLASS_ROOM_ID + " ASC");
                
        StringBuilder studentSQL = new StringBuilder();
        studentSQL.append(" SELECT s." + StudentSchema.PRENAME+ ",s." + StudentSchema.FIRSTNAME+ ",s." + StudentSchema.LASTNAME + ",s." + StudentSchema.GENDER);
        studentSQL.append(" ,ss." + StudentStudySchema.STUDENT_STUDY_ID + " ,ss." + StudentStudySchema.STUDENT_CODE);
        studentSQL.append(" ,cr." + ClassRoomSchema.NAME);
        studentSQL.append(" ,scr." + StudentClassRoomSchema.STUDENT_CLASS_ROOM_ID);
        studentSQL.append(" FROM " + StudentClassRoomSchema.TABLE_NAME + " scr");
        studentSQL.append(" INNER JOIN " + ClassRoomSchema.TABLE_NAME + " cr ON scr." + StudentClassRoomSchema.CLASS_ROOM_ID + "= cr." + ClassRoomSchema.CLASS_ROOM_ID);
        studentSQL.append(" INNER JOIN " + StudentStudySchema.TABLE_NAME + " ss ON ss." + StudentStudySchema.STUDENT_STUDY_ID + "= scr." + StudentClassRoomSchema.STUDENT_STUDY_ID);
        studentSQL.append(" INNER JOIN " + StudentSchema.TABLE_NAME + " s ON s." + StudentSchema.STUDENT_ID + "= ss." + StudentStudySchema.STUDENT_ID);
        studentSQL.append(" WHERE scr." + StudentClassRoomSchema.ACADEMIC_YEAR + "=" + DateTimeUtil.getBuddishYear());
        studentSQL.append(" AND scr." + StudentClassRoomSchema.CLASS_ROOM_ID + " IN (");

        freeFormContainer = container.getFreeFormContainer(classRoomSQL.toString(), TimetableSchema.TIMETABLE_ID);
        if(freeFormContainer.size() > 0){
            for (Object id:freeFormContainer.getItemIds()) {
            	Item item = freeFormContainer.getItem(id);
            	studentSQL.append(item.getItemProperty(ClassRoomSchema.CLASS_ROOM_ID).getValue()+",");
    		}
            studentSQL.append(")");
            studentSQL.append(" GROUP BY ss." + StudentStudySchema.STUDENT_CODE);
            studentSQL.append(" ORDER BY s." + StudentSchema.FIRSTNAME + " ASC");
            
            freeFormContainer = container.getFreeFormContainer(studentSQL.toString().replace(",)", ")"), StudentClassRoomSchema.STUDENT_CLASS_ROOM_ID);

            for (Object id:freeFormContainer.getItemIds()) {
            	final Item item = freeFormContainer.getItem(id);
            	
            	HorizontalLayout studentLayout = new HorizontalLayout();
    			studentLayout.setSizeFull();
    			studentLayout.addLayoutClickListener(new LayoutClickListener() {
    				private static final long serialVersionUID = 1L;

    				@Override
    				public void layoutClick(LayoutClickEvent event) {
    					navigateTo(new StudentReadOnlyView(new RowId(item.getItemProperty(StudentStudySchema.STUDENT_STUDY_ID).getValue()), new PersonalTeachingView(studentGroup.isVisible())));
    				}
    			});
    			studentGroup.addComponent(studentLayout);
    			
    			String imageGender = "";
    			String fontGender = "";
    			
    			if(item.getItemProperty(StudentSchema.GENDER).getValue().toString().equals(Integer.toString(Gender.MALE))){
    				imageGender = Images.MALE;
    				fontGender = FontAwesome.MALE.getHtml();
    			}else{
    				imageGender = Images.FEMALE;
    				fontGender = FontAwesome.FEMALE.getHtml();
    			}
    			
    			Image studentImage = new Image();
    			studentImage.setSource(new ThemeResource(imageGender));
    			studentLayout.addComponent(studentImage);
    			studentLayout.setComponentAlignment(studentImage, Alignment.MIDDLE_CENTER);
    			studentLayout.setExpandRatio(studentImage,(float) 1);
    			
    			StringBuilder detailBuilder = new StringBuilder();
    			detailBuilder.append("&nbsp;");
    			detailBuilder.append(Prename.getNameTh(Integer.parseInt(item.getItemProperty(StudentSchema.PRENAME).getValue().toString())) + " ");
    			detailBuilder.append(item.getItemProperty(StudentSchema.FIRSTNAME).getValue() + " ");
    			detailBuilder.append(item.getItemProperty(StudentSchema.LASTNAME).getValue());
    			detailBuilder.append("</br>");
    			detailBuilder.append("&nbsp;");
    			detailBuilder.append(fontGender);
    			detailBuilder.append("&nbsp;&nbsp;");
    			detailBuilder.append(item.getItemProperty(StudentStudySchema.STUDENT_CODE).getValue() + " ");
    			detailBuilder.append(item.getItemProperty(ClassRoomSchema.NAME).getValue());
    			
    			Label detailLabel = new Label(detailBuilder.toString());
    			detailLabel.setStyleName("student-detail");
    			detailLabel.setSizeFull();
    			detailLabel.setContentMode(ContentMode.HTML);
    			studentLayout.addComponent(detailLabel);
    			studentLayout.setComponentAlignment(detailLabel, Alignment.TOP_CENTER);
    			studentLayout.setExpandRatio(detailLabel, (float)3.5);
    			
    			Label forwardLabel = new Label(FontAwesome.ARROW_RIGHT.getHtml());
    			forwardLabel.setSizeFull();
    			forwardLabel.setStyleName("forward");
    			forwardLabel.setContentMode(ContentMode.HTML);
    			studentLayout.addComponent(forwardLabel);
    			studentLayout.setComponentAlignment(forwardLabel, Alignment.MIDDLE_CENTER);
    			studentLayout.setExpandRatio(forwardLabel, (float)0.5);
    		}
        }
    }
	
	/* ดึงข้อมูลห้องเรียน */
	private void fetchClassRoomData()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT tt.*, cr." + ClassRoomSchema.NAME +" AS class_name,");
        builder.append(" s." + SubjectSchema.NAME + " AS subject_name");
        builder.append(" FROM " + TimetableSchema.TABLE_NAME + " tt");
        builder.append(" INNER JOIN " + ClassRoomSchema.TABLE_NAME + " cr ON tt." + TimetableSchema.CLASS_ROOM_ID + "= cr." + ClassRoomSchema.CLASS_ROOM_ID);
        builder.append(" INNER JOIN " + TeachingSchema.TABLE_NAME + " tc ON tt." + TimetableSchema.TEACHING_ID+ "= tc." + TeachingSchema.TEACHING_ID);
        builder.append(" INNER JOIN " + PersonnelSchema.TABLE_NAME + " p ON tc." + TeachingSchema.PERSONNEL_ID + "= p." + PersonnelSchema.PERSONNEL_ID);
        builder.append(" INNER JOIN " + SubjectSchema.TABLE_NAME + " s ON tc." + TeachingSchema.SUBJECT_ID + "= s." + SubjectSchema.SUBJECT_ID);
        builder.append(" WHERE tc." + TeachingSchema.ACADEMIC_YEAR + "=" + DateTimeUtil.getBuddishYear());
        builder.append(" AND p." + PersonnelSchema.PERSONNEL_ID + "=" + SessionSchema.getRefID());
        builder.append(" AND tc." + TeachingSchema.SCHOOL_ID +"=" +SessionSchema.getSchoolID());
        builder.append(" GROUP BY cr." + ClassRoomSchema.CLASS_ROOM_ID);
        builder.append(" ORDER BY tt." + TimetableSchema.SEMESTER +" DESC, cr."+ ClassRoomSchema.CLASS_YEAR + " ASC, cr." + ClassRoomSchema.NUMBER);
        
        freeFormContainer = container.getFreeFormContainer(builder.toString(), TimetableSchema.TIMETABLE_ID);
        for(Object id:freeFormContainer.getItemIds())
        {
            final Item item = freeFormContainer.getItem(id);
            String object = item.getItemProperty(TimetableSchema.CLASS_ROOM_ID).getValue().toString() + "," +
            		item.getItemProperty(TimetableSchema.TEACHING_ID).getValue().toString();
            
            if(!timetableStores.contains(object)){
            	HorizontalLayout studentLayout = new HorizontalLayout();
            	studentLayout.setSpacing(true);
    			studentLayout.setSizeFull();
    			studentLayout.addLayoutClickListener(new LayoutClickListener() {
    				private static final long serialVersionUID = 1L;

    				@Override
    				public void layoutClick(LayoutClickEvent event) {
    					navigateTo(new ClassTeachingView(item.getItemProperty(TimetableSchema.CLASS_ROOM_ID).getValue(),new PersonalTeachingView(studentGroup.isVisible())));
    				}
    			});
    			classRoomGroup.addComponent(studentLayout);
    			
                Image classImage = new Image();
    	        classImage.setSource(new ThemeResource(Images.CLASS));
    	        studentLayout.addComponent(classImage);
    			studentLayout.setComponentAlignment(classImage, Alignment.MIDDLE_CENTER);
    	        studentLayout.setExpandRatio(classImage,(float) 1);

    			StringBuilder detailBuilder = new StringBuilder();
    			detailBuilder.append("&nbsp;");
    			detailBuilder.append(item.getItemProperty("class_name").getValue() + " ");
    			detailBuilder.append(item.getItemProperty("subject_name").getValue());
    			detailBuilder.append("<br/>");
    			detailBuilder.append("&nbsp;");
    			detailBuilder.append("คาบถัดไป: " + fetchNextClassTeaching(item));
    			detailBuilder.append("<br/>");
    			detailBuilder.append("&nbsp;");
    			detailBuilder.append("จำนวนครั้ง: 5/10");
    			
    			Label detailLabel = new Label(detailBuilder.toString());
    			detailLabel.setStyleName("class-detail");
    			detailLabel.setSizeFull();
    			detailLabel.setContentMode(ContentMode.HTML);
    			studentLayout.addComponent(detailLabel);
    			studentLayout.setComponentAlignment(detailLabel, Alignment.TOP_CENTER);
    			studentLayout.setExpandRatio(detailLabel, (float)3.5);
    			    			
    	        Label forwardLabel = new Label(FontAwesome.ARROW_RIGHT.getHtml());
    			forwardLabel.setSizeFull();
    			forwardLabel.setStyleName("forward");
    			forwardLabel.setContentMode(ContentMode.HTML);
    			studentLayout.addComponent(forwardLabel);
    			studentLayout.setComponentAlignment(forwardLabel, Alignment.MIDDLE_CENTER);
    			studentLayout.setExpandRatio(forwardLabel, (float)0.5);
            }else{
                timetableStores.add(object);
            }
        }
    }
	
	/* ดึงคาบสอนถัดไป */
	private String fetchNextClassTeaching(Item item)
    {
        StringBuilder nextClass = new StringBuilder();
        nextClass.append(" SELECT * FROM " + TimetableSchema.TABLE_NAME);
        nextClass.append(" WHERE " + TimetableSchema.TEACHING_ID + "=" + item.getItemProperty(TimetableSchema.TEACHING_ID).getValue());
        nextClass.append(" AND " + TimetableSchema.CLASS_ROOM_ID + "=" + item.getItemProperty(TimetableSchema.CLASS_ROOM_ID).getValue());
        nextClass.append(" ORDER BY " + TimetableSchema.WORKING_DAY + " DESC");
        freeForm2Container = container.getFreeFormContainer(nextClass.toString(), TimetableSchema.TIMETABLE_ID);
        Integer dayOfWeek = null;
        for(Object id:freeForm2Container.getItemIds())
        {
            Item nextClassItem = freeForm2Container.getItem(id);
            if(DateTimeUtil.getWeekOfday() <= Integer.parseInt(nextClassItem.getItemProperty(TimetableSchema.WORKING_DAY).getValue().toString()))
                dayOfWeek = Integer.valueOf(Integer.parseInt(nextClassItem.getItemProperty(TimetableSchema.WORKING_DAY).getValue().toString()));
            else
            if(DateTimeUtil.getWeekOfday() >= Integer.parseInt(nextClassItem.getItemProperty(TimetableSchema.WORKING_DAY).getValue().toString()) && dayOfWeek == null)
                dayOfWeek = Integer.valueOf(Integer.parseInt(nextClassItem.getItemProperty(TimetableSchema.WORKING_DAY).getValue().toString()));
        }

        return DateTimeUtil.getDateFromDayOfWeek(dayOfWeek.intValue());
    }
	
	@Override
	public void setToolbarListenner() {
		searchButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				addButton.setStyleName("");
		        searchButton.setStyleName("toolbar-button-focus");
		        timetableButton.setStyleName("");
		        navigateTo(new SearchView(new PersonalTeachingView(studentGroup.isVisible())));
			}
		});
		

        addButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
		        addButton.addStyleName("toolbar-button-focus");
		        searchButton.setStyleName("");
		        timetableButton.setStyleName("");
			}
		});
	}
}
