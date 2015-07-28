package com.ies.schoolos.ui.mobile;

import java.util.ArrayList;

import com.ies.schoolos.container.Container;
import com.ies.schoolos.schema.SessionSchema;
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
import com.ies.schoolos.ui.mobile.info.StudentReadOnlyView;
import com.ies.schoolos.ui.mobile.teaching.ClassTeachingView;
import com.ies.schoolos.utility.DateTimeUtil;
import com.ies.schoolos.utility.Images;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SearchView extends NavigationManager{
	private static final long serialVersionUID = 1L;

	private ArrayList<String> timetableStores = new ArrayList<>();
	
	private Container container;
	private SQLContainer studentContainer;
	private SQLContainer classTeachingContainer;
	private SQLContainer freeFormContainer;
	private SQLContainer freeForm2Container;
	
	private Component component;
	
	private VerticalComponentGroup searchGroup;
	
	private TextField searchField;
	private Button cancelButton;
	
	private Button studentButton;
	private Button classButton;
	
	private VerticalComponentGroup studentGroup;
    private VerticalComponentGroup classRoomGroup;
	
	public SearchView(Component component) {
		this.component = component;
		container = new Container();
		
		setPreviousComponent(component);
		buildMainLayout();
		Responsive.makeResponsive(searchGroup);
		Responsive.makeResponsive(studentGroup);
		Responsive.makeResponsive(classRoomGroup);
	}
	
	public SearchView(Component component,boolean isStudentVisible, String text) {
		this.component = component;
		container = new Container();
		
		setPreviousComponent(component);
		buildMainLayout();
		searchField.setValue(text);
		
		studentGroup.setVisible(isStudentVisible);
		classRoomGroup.setVisible(!isStudentVisible);
		setResultData(text);
		
		Responsive.makeResponsive(searchGroup);
		Responsive.makeResponsive(studentGroup);
		Responsive.makeResponsive(classRoomGroup);
	}
	
	private void buildMainLayout(){		
		searchGroup = new VerticalComponentGroup();
		searchGroup.setStyleName("search-layout");
		setCurrentComponent(searchGroup);
		
		HorizontalLayout searchModeLayout = new HorizontalLayout();
		searchModeLayout.setWidth("100%");
		searchGroup.addComponent(searchModeLayout);
		
		searchField = new TextField();
		searchField.setWidth("100%");
        searchField.setInputPrompt("คำค้น");
        searchField.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event) {
				setResultData(event.getText());
			}
		});
        searchModeLayout.addComponent(searchField);
        searchModeLayout.setExpandRatio(searchField, 2);
		
		cancelButton = new Button("ยกเลิก");
		cancelButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				navigateBack();
			}
		});
		searchModeLayout.addComponent(cancelButton);
        searchModeLayout.setExpandRatio(cancelButton, 1);
        
        HorizontalLayout buttonGroup = new HorizontalLayout();
		buttonGroup.setWidth("100%");
		searchGroup.addComponent(buttonGroup);
		
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
		buttonGroup.addComponent(studentButton);
		
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
		buttonGroup.addComponent(classButton);
		
		studentGroup = new VerticalComponentGroup();
		studentGroup.setStyleName("student-list");
		studentGroup.setVisible(false);
		searchGroup.addComponent(studentGroup);
		fetchStudentsData();
		
		classRoomGroup = new VerticalComponentGroup();
		classRoomGroup.setStyleName("class-list");
		classRoomGroup.setVisible(false);
		searchGroup.addComponent(classRoomGroup);
		fetchClassRoomData();
		
		studentButton.click();
	}
	
	private void setResultData(final String text){
		if(studentGroup.isVisible()){
			studentGroup.removeAllComponents();
			if(text.length() > 0){
				for (Object id:studentContainer.getItemIds()) {
		        	final Item item = studentContainer.getItem(id);
		        	
		        	if(item.getItemProperty(StudentSchema.FIRSTNAME).getValue().toString().contains(text) ||
		        			item.getItemProperty(StudentSchema.LASTNAME).getValue().toString().contains(text) ||
		        			item.getItemProperty(StudentStudySchema.STUDENT_CODE).getValue().toString().contains(text) ||
		        			item.getItemProperty(ClassRoomSchema.NAME).getValue().toString().contains(text) ){
		        		HorizontalLayout studentLayout = new HorizontalLayout();
						studentLayout.setSizeFull();
						studentLayout.addLayoutClickListener(new LayoutClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void layoutClick(LayoutClickEvent event) {
								navigateTo(new StudentReadOnlyView(new RowId(item.getItemProperty(StudentStudySchema.STUDENT_STUDY_ID).getValue()), new SearchView(component,true,text)));
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
						detailBuilder.append("ม.1/1");
						
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
		}else{
			classRoomGroup.removeAllComponents();
			if(text.length() > 0){
				for(Object itemId:classTeachingContainer.getItemIds()) {
		            final Item item = classTeachingContainer.getItem(itemId);
		            if(item.getItemProperty("class_name").getValue().toString().contains(text) ||
		        			item.getItemProperty("subject_name").getValue().toString().contains(text)){
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
			    					navigateTo(new ClassTeachingView(item.getItemProperty(TimetableSchema.CLASS_ROOM_ID).getValue(),new SearchView(component,false,text)));
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
			    			
			    			HorizontalButtonGroup buttonGroup = new HorizontalButtonGroup();
			    			buttonGroup.setSizeFull();
			    			
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
			}
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
        for (Object id:freeFormContainer.getItemIds()) {
        	Item item = freeFormContainer.getItem(id);
        	studentSQL.append(item.getItemProperty(ClassRoomSchema.CLASS_ROOM_ID).getValue()+",");
		}
        studentSQL.append(")");
        studentSQL.append(" GROUP BY ss." + StudentStudySchema.STUDENT_CODE);
        studentSQL.append(" ORDER BY s." + StudentSchema.FIRSTNAME + " ASC");
                
        freeFormContainer = container.getFreeFormContainer(studentSQL.toString().replace(",)", ")"), StudentClassRoomSchema.STUDENT_CLASS_ROOM_ID);
        studentContainer = freeFormContainer;
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
        builder.append(" INNER JOIN " + PersonnelSchema.TABLE_NAME + " p ON tc.personnel_id= p." + PersonnelSchema.PERSONNEL_ID);
        builder.append(" INNER JOIN " + SubjectSchema.TABLE_NAME + " s ON tc.subject_id= s." + SubjectSchema.SUBJECT_ID);
        builder.append(" WHERE tc." + TeachingSchema.ACADEMIC_YEAR + "=" + DateTimeUtil.getBuddishYear());
        builder.append(" AND p." + PersonnelSchema.PERSONNEL_ID + "=" + SessionSchema.getRefID());
        builder.append(" AND tc." + TeachingSchema.SCHOOL_ID +"=" +SessionSchema.getSchoolID());
        builder.append(" ORDER BY tt." + TimetableSchema.SEMESTER +" DESC");
        
        freeFormContainer = container.getFreeFormContainer(builder.toString(), TimetableSchema.TIMETABLE_ID);
        classTeachingContainer = freeFormContainer;
    }
	
	/* ดึงคาบสอนถัดไป */
	private String fetchNextClassTeaching(Item item)
    {
        StringBuilder nextClass = new StringBuilder();
        nextClass.append(" SELECT * FROM " + TimetableSchema.TABLE_NAME);
        nextClass.append(" WHERE teaching_id=" + item.getItemProperty("teaching_id").getValue());
        nextClass.append(" AND class_room_id=" + item.getItemProperty("class_room_id").getValue());
        nextClass.append(" ORDER BY working_day DESC");
        freeForm2Container = container.getFreeFormContainer(nextClass.toString(), TimetableSchema.TIMETABLE_ID);
        Integer dayOfWeek = null;
        for(Object id:freeForm2Container.getItemIds())
        {
            Item nextClassItem = freeForm2Container.getItem(id);
            if(DateTimeUtil.getWeekOfday() <= Integer.parseInt(nextClassItem.getItemProperty("working_day").getValue().toString()))
                dayOfWeek = Integer.valueOf(Integer.parseInt(nextClassItem.getItemProperty("working_day").getValue().toString()));
            else
            if(DateTimeUtil.getWeekOfday() >= Integer.parseInt(nextClassItem.getItemProperty("working_day").getValue().toString()) && dayOfWeek == null)
                dayOfWeek = Integer.valueOf(Integer.parseInt(nextClassItem.getItemProperty("working_day").getValue().toString()));
        }

        return DateTimeUtil.getDateFromDayOfWeek(dayOfWeek.intValue());
    }
}
