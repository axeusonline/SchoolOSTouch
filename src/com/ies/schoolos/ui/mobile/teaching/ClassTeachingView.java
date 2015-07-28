package com.ies.schoolos.ui.mobile.teaching;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.ies.schoolos.container.Container;
import com.ies.schoolos.schema.CreateModifiedSchema;
import com.ies.schoolos.schema.SessionSchema;
import com.ies.schoolos.schema.academic.TeachingSchema;
import com.ies.schoolos.schema.academic.TimetableSchema;
import com.ies.schoolos.schema.fundamental.ClassRoomSchema;
import com.ies.schoolos.schema.info.StudentAttendanceSchema;
import com.ies.schoolos.schema.info.StudentClassRoomSchema;
import com.ies.schoolos.schema.info.StudentSchema;
import com.ies.schoolos.schema.info.StudentStudySchema;
import com.ies.schoolos.type.Gender;
import com.ies.schoolos.type.Prename;
import com.ies.schoolos.ui.mobile.SearchView;
import com.ies.schoolos.ui.mobile.component.ManagerView;
import com.ies.schoolos.ui.mobile.info.StudentReadOnlyView;
import com.ies.schoolos.utility.DateTimeUtil;
import com.ies.schoolos.utility.Images;
import com.ies.schoolos.utility.Notification;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;

public class ClassTeachingView extends ManagerView {
	private static final long serialVersionUID = 1L;

	private final int STUDENT_ATTENDANCE_ID_INDEX = 0;
	private final int STUDENT_STUDY_ID_INDEX = 1;
	
	private Object id;
	private Object studentAttendanceId;
	private String className;
	private int maleQty;
	private int femaleQty;
	private Component component;
	
	private Container container;
	private SQLContainer studentAttendanceContainer;
	private SQLContainer freeFormContainer;
	private SQLContainer freeForm2Container;
	private IndexedContainer timetableContainer;
	
	/* ข้อมูลส่วนตัว */
	private Panel panel;
	private VerticalLayout roomLayout;
	private Image classImage;
	private Label classroomDetailLabel;
	
	private Button studentButton;
	private Button attendanceButton;
	private Button chartButton;
	
	private VerticalComponentGroup studentGroup;
    private VerticalComponentGroup attendanceGroup;
    private VerticalComponentGroup chartGroup;
    
    private DateField teachingDate;
    private NativeSelect timetableSelect;
	    
	public ClassTeachingView(Object id, Component component) {
		this.id = id;
		this.component = component;
		
		container = new Container();
		studentAttendanceContainer = container.getStudentAttendanceContainer();
		studentAttendanceContainer.addRowIdChangeListener(new RowIdChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void rowIdChange(RowIdChangeEvent event) {
				studentAttendanceId = event.getNewRowId();
			}
		});
		
		buildMainLayout();
		setPreviousComponent(component);
		
		Responsive.makeResponsive(studentGroup);
		Responsive.makeResponsive(attendanceGroup);
	}
	
	public ClassTeachingView(Object id, Component component, boolean isStudentVisible, boolean isAttendanceVisible, boolean isChartVisible) {
		this.id = id;
		this.component = component;
		
		container = new Container();
		
		buildMainLayout();
		setPreviousComponent(component);
		
		studentGroup.setVisible(isStudentVisible);
		attendanceGroup.setVisible(isAttendanceVisible);
		chartGroup.setVisible(isChartVisible);
		
		if(isStudentVisible)
			studentButton.click();
		else if(isAttendanceVisible)
			attendanceButton.click();
		else
			chartButton.click();
		
		Responsive.makeResponsive(studentGroup);
		Responsive.makeResponsive(attendanceGroup);
	}
	
	/* สร้าง Content */
	private void buildMainLayout(){
		panel = new Panel();
		navView.setContent(panel);
		
		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setMargin(true);
		panel.setContent(mainLayout);
		
		roomLayout = new VerticalLayout();
		roomLayout.setWidth("100%");
		roomLayout.setSpacing(true);
		mainLayout.addComponent(roomLayout);
		mainLayout.setComponentAlignment(roomLayout, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout roomDetailLayout = new HorizontalLayout();
		roomDetailLayout.setSpacing(true);
		roomLayout.addComponent(roomDetailLayout);
		
		classImage = new Image();
		classImage.setSource(new ThemeResource(Images.CLASS));
		roomDetailLayout.addComponent(classImage);
		
		classroomDetailLabel = new Label();
	    classroomDetailLabel.setContentMode(ContentMode.HTML);
	    classroomDetailLabel.setStyleName("classroom-detail");
	    roomDetailLayout.addComponent(classroomDetailLabel);
	    roomDetailLayout.setComponentAlignment(classroomDetailLabel, Alignment.MIDDLE_CENTER);

	    HorizontalLayout submenuLayout = new HorizontalLayout();
	    submenuLayout.setWidth("100%");
	    submenuLayout.setSpacing(true);
		roomLayout.addComponent(submenuLayout);
	    
		studentButton = new Button(FontAwesome.USER);
		studentButton.setSizeFull();
		studentButton.setStyleName("submenu-button-focus");
		studentButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				studentButton.setStyleName("submenu-button-focus");
				attendanceButton.setStyleName("submenu-button");
				chartButton.setStyleName("submenu-button");
				
				studentGroup.setVisible(true);
				attendanceGroup.setVisible(false);
				chartGroup.setVisible(false);
			}
		});
		submenuLayout.addComponent(studentButton);
		
		attendanceButton = new Button(FontAwesome.CHECK_SQUARE_O);
		attendanceButton.setSizeFull();
		attendanceButton.setStyleName("submenu-button");
		attendanceButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				studentButton.setStyleName("submenu-button");
				attendanceButton.setStyleName("submenu-button-focus");
				chartButton.setStyleName("submenu-button");
				
				studentGroup.setVisible(false);
				attendanceGroup.setVisible(true);
				chartGroup.setVisible(false);
			}
		});
		submenuLayout.addComponent(attendanceButton);
		
		chartButton = new Button(FontAwesome.BAR_CHART_O);
		chartButton.setSizeFull();
		chartButton.setStyleName("submenu-button");
		chartButton.setVisible(false);
		chartButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				studentButton.setStyleName("submenu-button");
				attendanceButton.setStyleName("submenu-button");
				chartButton.setStyleName("submenu-button-focus");
				
				Notification.show("ไม่สามารถใช้งานได้ขณะนี้", Type.WARNING_MESSAGE);
				
				studentGroup.setVisible(false);
				attendanceGroup.setVisible(false);
				chartGroup.setVisible(true);
			}
		});
		submenuLayout.addComponent(chartButton);
		
		/*NativeJS nativeJScomponent = new NativeJS(Adsense.getAdsenseUrl());
        nativeJScomponent.execute();
		Label adsenseLabel = new Label(Adsense.getAdsenseUrl());
		adsenseLabel.setWidth("100%");
		adsenseLabel.setContentMode(ContentMode.HTML);
		roomLayout.addComponent(adsenseLabel);*/
		
		studentGroup = new VerticalComponentGroup();
		studentGroup.setStyleName("student-list");
		studentGroup.setVisible(false);
		roomLayout.addComponent(studentGroup);
		fetchStudentsData();
		
		attendanceGroup = new VerticalComponentGroup();
		attendanceGroup.setStyleName("attendance-list");
		attendanceGroup.setVisible(false);
		roomLayout.addComponent(attendanceGroup);
		
		teachingDate = new DateField("วันที่สอน");
		teachingDate.setRangeEnd(new Date());
        teachingDate.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings({ "deprecation", "unchecked" })
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					timetableSelect.removeAllItems();
					
					Iterator<Component> iterator = attendanceGroup.getComponentIterator();
					while(iterator.hasNext()){
						Object component = iterator.next();
						if(component.getClass() == HorizontalLayout.class){
							HorizontalLayout studentLayout = (HorizontalLayout) component;
							Image image = (Image) studentLayout.getComponent(STUDENT_ATTENDANCE_ID_INDEX);
							
							if(image.getSource().toString().contains("female")){
								image.setSource(new ThemeResource(Images.FEMALE));
							}else{
								image.setSource(new ThemeResource(Images.MALE));
							}
							studentLayout.replaceComponent(studentLayout.getComponent(STUDENT_ATTENDANCE_ID_INDEX), image);
						}
					}
					
					Date dateSelect = (Date)event.getProperty().getValue();
					timetableContainer.removeAllContainerFilters();
					timetableContainer.removeAllItems();
					
					StringBuilder timetableSQL = new StringBuilder();
					timetableSQL.append(" SELECT tt.*");
					timetableSQL.append(" FROM " + TimetableSchema.TABLE_NAME + " tt");
					timetableSQL.append(" INNER JOIN " + ClassRoomSchema.TABLE_NAME + " cr ON tt." + TimetableSchema.CLASS_ROOM_ID + "= cr." + ClassRoomSchema.CLASS_ROOM_ID);
					timetableSQL.append(" INNER JOIN " + TeachingSchema.TABLE_NAME + " tc ON tt." + TimetableSchema.TEACHING_ID + "= tc." +  TeachingSchema.TEACHING_ID);
					timetableSQL.append(" WHERE tc." + TeachingSchema.ACADEMIC_YEAR + "=" + DateTimeUtil.getBuddishYear());
					timetableSQL.append(" AND tc." + TeachingSchema.PERSONNEL_ID + "=" + SessionSchema.getRefID());
					timetableSQL.append(" AND tt." + TimetableSchema.SCHOOL_ID + "=" + SessionSchema.getSchoolID());
					timetableSQL.append(" AND tt." + TimetableSchema.CLASS_ROOM_ID + "=" + id.toString());
					timetableSQL.append(" AND tt." + TimetableSchema.WORKING_DAY + "=" + dateSelect.getDay());
					timetableSQL.append(" GROUP BY cr." + ClassRoomSchema.CLASS_ROOM_ID);
					timetableSQL.append(" ORDER BY tt." + TimetableSchema.SEMESTER + " DESC, cr."+ ClassRoomSchema.CLASS_ROOM_ID + " ASC");
			                
			        freeFormContainer = container.getFreeFormContainer(timetableSQL.toString(), TimetableSchema.TIMETABLE_ID);
			        if(freeFormContainer.size() > 0){
			        	timetableSelect.setVisible(true);
			      	   	for (Object itemId:freeFormContainer.getItemIds()) {
			      	   		Item timetableItem = freeFormContainer.getItem(itemId);

			      	   		StringBuilder timetableString = new StringBuilder();
			      	   		timetableString.append("ภาคเรียน:"+((int)timetableItem.getItemProperty(TimetableSchema.SEMESTER).getValue() + 1));
			      	   		timetableString.append(" คาบ "+timetableItem.getItemProperty(TimetableSchema.SECTION).getValue());

			      	        Item item = timetableContainer.addItem(itemId);
			      	        item.getItemProperty("name").setValue(timetableString.toString());
			      	   	}
			        }else{
			        	Notification.show("ไม่พบคาบสอน ในห้องเรียนดังกล่าว กรุณาระบุวันใหม่อีกครั้ง", Type.WARNING_MESSAGE);
			        	timetableSelect.setVisible(false);
			        	timetableSelect.clear();
			        }	
				}
			}
		});
		attendanceGroup.addComponent(teachingDate);
		
		timetableContainer = new IndexedContainer();
		timetableContainer.addContainerProperty("name", String.class,null);
		
		timetableSelect = new NativeSelect("ตารางสอน",timetableContainer);
    	timetableSelect.setItemCaptionPropertyId("name");
		timetableSelect.setImmediate(true);
        timetableSelect.setNullSelectionAllowed(false);
        timetableSelect.setVisible(false);
        timetableSelect.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					StringBuilder studentSQL = new StringBuilder();
					studentSQL.append(" SELECT sta.*,s."+StudentSchema.GENDER + ", ss."+StudentStudySchema.STUDENT_STUDY_ID);
					studentSQL.append(" FROM " + StudentAttendanceSchema.TABLE_NAME + " sta");
					studentSQL.append(" INNER JOIN " + StudentStudySchema.TABLE_NAME + " ss ON ss." + StudentStudySchema.STUDENT_STUDY_ID + "= sta." + StudentAttendanceSchema.STUDENT_STUDY_ID);
					studentSQL.append(" INNER JOIN " + StudentSchema.TABLE_NAME + " s ON s." + StudentSchema.STUDENT_ID + "= ss." + StudentStudySchema.STUDENT_ID);
					studentSQL.append(" WHERE sta." + StudentAttendanceSchema.TIMETABLE_ID + "=" + event.getProperty().getValue().toString());
					studentSQL.append(" AND sta." + StudentAttendanceSchema.CHECK_DATE + "='" + DateTimeUtil.getyyyyMMddSringSQL(teachingDate.getValue())+"'");
					studentSQL.append(" AND sta." + StudentAttendanceSchema.SCHOOL_ID + "=" + SessionSchema.getSchoolID());
		
					freeForm2Container = container.getFreeFormContainer(studentSQL.toString(), StudentAttendanceSchema.STUDENT_ATTENDANCE_ID);
					if(freeForm2Container.size() > 0){
						HashMap<Object, Object> attendanceMap = new HashMap<>();
						for(Object itemId:freeForm2Container.getItemIds()){
							Item attendanceItem = freeForm2Container.getItem(itemId);
							attendanceMap.put(attendanceItem.getItemProperty(StudentAttendanceSchema.STUDENT_STUDY_ID).getValue().toString(), itemId);
						}
						
						Iterator<Component> iter = attendanceGroup.getComponentIterator();
						while(iter.hasNext()){
							Object object = iter.next();
							if(object.getClass() == HorizontalLayout.class){
								HorizontalLayout studentLayout = (HorizontalLayout) object;
								Image image = (Image) studentLayout.getComponent(STUDENT_ATTENDANCE_ID_INDEX);
								Label studentDetail = (Label) studentLayout.getComponent(STUDENT_STUDY_ID_INDEX);
								
								String imagePath = image.getSource().toString();
								
								Item item = freeForm2Container.getItem(attendanceMap.get(studentDetail.getId()));
								int attentdanceStatus = (int) item.getItemProperty(StudentAttendanceSchema.ATTENDANCE_STATUS).getValue();
								if(item.getItemProperty(StudentSchema.GENDER).getValue().toString().equals(Integer.toString(Gender.MALE))){																	
									if(attentdanceStatus == 0){
										imagePath = Images.MALE_GREEN;
									}else if(attentdanceStatus == 1){
										imagePath = Images.MALE_RED;
									}else if(attentdanceStatus == 2){
										imagePath = Images.MALE_ORANGE;
									}
								}else{
									if(attentdanceStatus == 0){
										imagePath = Images.FEMALE_GREEN;
									}else if(attentdanceStatus == 1){
										imagePath = Images.FEMALE_RED;
									}else if(attentdanceStatus == 2){
										imagePath = Images.FEMALE_ORANGE;
									}
								}
								image.setSource(new ThemeResource(imagePath));
								image.setId(attendanceMap.get(studentDetail.getId()).toString());
							}
						}	
					}
				}
			}
		});
     	attendanceGroup.addComponent(timetableSelect);
		
		fetchAttendanceData();
		
		chartGroup = new VerticalComponentGroup();
		chartGroup.setStyleName("chart-list");
		chartGroup.setVisible(false);
		roomLayout.addComponent(chartGroup);
		//fetchChartData();
		
		studentButton.click();
		
		StringBuilder classDetail = new StringBuilder();
        classDetail.append("ห้อง "+ className);
        classDetail.append("</br>");
        classDetail.append(" " + FontAwesome.MALE.getHtml() + " " +/* " ชาย " +*/ maleQty);
        classDetail.append(" " + FontAwesome.FEMALE.getHtml() + " " +/* " หญิง " +*/ femaleQty);
        classDetail.append("</br>");
        classDetail.append("รวม " + freeFormContainer.size() + " คน");

        classroomDetailLabel.setValue(classDetail.toString());
	}
	
	/* ดึงข้อมูลนักเรียน */
	private void fetchStudentsData(){
		maleQty = 0;
		femaleQty = 0;
        StringBuilder studentSQL = new StringBuilder();
        studentSQL.append(" SELECT s." + StudentSchema.PRENAME+ ",s." + StudentSchema.FIRSTNAME+ ",s." + StudentSchema.LASTNAME + ",s." + StudentSchema.GENDER);
        studentSQL.append(" ,ss." + StudentStudySchema.STUDENT_STUDY_ID + " ,ss." + StudentStudySchema.STUDENT_CODE);
        studentSQL.append(" ,cr." + ClassRoomSchema.NAME);
        studentSQL.append(" ,scr." + StudentClassRoomSchema.STUDENT_CLASS_ROOM_ID);
        studentSQL.append(" FROM " + StudentClassRoomSchema.TABLE_NAME + " scr");
        studentSQL.append(" INNER JOIN " + ClassRoomSchema.TABLE_NAME + " cr ON scr." + StudentClassRoomSchema.CLASS_ROOM_ID + "= cr." + ClassRoomSchema.CLASS_ROOM_ID);
        studentSQL.append(" INNER JOIN " + StudentStudySchema.TABLE_NAME + " ss ON ss." + StudentStudySchema.STUDENT_STUDY_ID + "= scr." + StudentClassRoomSchema.STUDENT_STUDY_ID);
        studentSQL.append(" INNER JOIN " + StudentSchema.TABLE_NAME + " s ON s." + StudentSchema.STUDENT_ID + "= ss." + StudentStudySchema.STUDENT_ID);
        studentSQL.append(" WHERE scr." + StudentClassRoomSchema.CLASS_ROOM_ID + "=" + id.toString());
        studentSQL.append(" GROUP BY ss." + StudentStudySchema.STUDENT_CODE);
        studentSQL.append(" ORDER BY s." + StudentSchema.FIRSTNAME + " ASC");
        
        freeFormContainer = container.getFreeFormContainer(studentSQL.toString(), StudentClassRoomSchema.STUDENT_CLASS_ROOM_ID);

        for (Object itemId:freeFormContainer.getItemIds()) {
        	final Item item = freeFormContainer.getItem(itemId);
        	className = item.getItemProperty(ClassRoomSchema.NAME).getValue().toString();
        	
        	HorizontalLayout studentLayout = new HorizontalLayout();
			studentLayout.setSizeFull();
			studentLayout.addLayoutClickListener(new LayoutClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void layoutClick(LayoutClickEvent event) {
					navigateTo(new StudentReadOnlyView(new RowId(item.getItemProperty(StudentStudySchema.STUDENT_STUDY_ID).getValue()), 
							new ClassTeachingView(id,component,studentGroup.isVisible(), attendanceGroup.isVisible(), chartGroup.isVisible())));
				}
			});
			studentGroup.addComponent(studentLayout);
			
			String imageGender = "";
			String fontGender = "";

			if(item.getItemProperty(StudentSchema.GENDER).getValue().toString().equals(Integer.toString(Gender.MALE))){
				imageGender = Images.MALE;
				fontGender = FontAwesome.MALE.getHtml();
				maleQty++;
			}else{
				imageGender = Images.FEMALE;
				fontGender = FontAwesome.FEMALE.getHtml();
				femaleQty++;
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

	/* ดึงข้อมูลการเข้าเรียน */
	private void fetchAttendanceData(){
      
		/* ใส่ข้อมูลของนักเรียน */
		for (Object itemId:freeFormContainer.getItemIds()) {
        	final Item item = freeFormContainer.getItem(itemId);
        	
        	final HorizontalLayout studentLayout = new HorizontalLayout();
        	studentLayout.setId(itemId.toString());
			studentLayout.setSizeFull();
			attendanceGroup.addComponent(studentLayout);
			
			String imageGender = "";
			String fontGender = "";

			if(item.getItemProperty(StudentSchema.GENDER).getValue().toString().equals(Integer.toString(Gender.MALE))){
				imageGender = Images.MALE;
				fontGender = FontAwesome.MALE.getHtml();
			}else{
				imageGender = Images.FEMALE;
				fontGender = FontAwesome.FEMALE.getHtml();
			}
			
			final Image studentImage = new Image();
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
			
			final Label detailLabel = new Label(detailBuilder.toString());
			detailLabel.setStyleName("student-detail");
			detailLabel.setId(item.getItemProperty(StudentStudySchema.STUDENT_STUDY_ID).getValue().toString());
			detailLabel.setSizeFull();
			detailLabel.setContentMode(ContentMode.HTML);
			studentLayout.addComponent(detailLabel);
			studentLayout.setComponentAlignment(detailLabel, Alignment.TOP_CENTER);
			studentLayout.setExpandRatio(detailLabel,4);
			
			/* กรณี เลือก แถวของนักเรียนเพื่อเช็คชื่อ */
			studentLayout.addLayoutClickListener(new LayoutClickListener() {
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public void layoutClick(LayoutClickEvent event) {
					String imagePath= studentImage.getSource().toString();
					int attendanceStatus = 0;
					if(teachingDate.getValue() != null && 
							(timetableSelect.isVisible() && timetableSelect.getValue() != null)){
						/* ตรวจสอบสถานะของ การมา เพื่อเปลี่ยนสีของรูป */
						if(imagePath.equals(Images.MALE)){
							imagePath= Images.MALE_GREEN;
							attendanceStatus = 0;
						}else if(imagePath.equals(Images.MALE_GREEN)){
							imagePath= Images.MALE_RED;
							attendanceStatus = 1;
						}else if(imagePath.equals(Images.MALE_RED)){
							imagePath= Images.MALE_ORANGE;
							attendanceStatus = 2;
						}else if(imagePath.equals(Images.MALE_ORANGE)){
							imagePath= Images.MALE_GREEN;
							attendanceStatus = 0;
						}else if(imagePath.equals(Images.FEMALE)){
							imagePath= Images.FEMALE_GREEN;
							attendanceStatus = 0;
						}else if(imagePath.equals(Images.FEMALE_GREEN)){
							imagePath= Images.FEMALE_RED;
							attendanceStatus = 1;
						}else if(imagePath.equals(Images.FEMALE_RED)){
							imagePath= Images.FEMALE_ORANGE;
							attendanceStatus = 2;
						}else if(imagePath.equals(Images.FEMALE_ORANGE)){
							imagePath= Images.FEMALE_GREEN;
							attendanceStatus = 0;
						}
						studentImage.setSource(new ThemeResource(imagePath));
						
						try{
							/* ตรวจสอบว่ามีการใส่ข้อมูลแล้วหรือยัง 
							 *  ถ้ายังก็ทำการเพิ่มข้อมูล
							 *  ถ้ามีแล้วก็ อัพเดทข้อมูล
							 *  */
							if(studentImage.getId() == null){
								studentAttendanceContainer.removeAllContainerFilters();
								Object tempId = studentAttendanceContainer.addItem();
								Item item = studentAttendanceContainer.getItem(tempId);
								item.getItemProperty(StudentAttendanceSchema.SCHOOL_ID).setValue(SessionSchema.getSchoolID());
								item.getItemProperty(StudentAttendanceSchema.TIMETABLE_ID).setValue(Integer.parseInt(timetableSelect.getValue().toString()));
								item.getItemProperty(StudentAttendanceSchema.STUDENT_STUDY_ID).setValue(Integer.parseInt(detailLabel.getId()));
								item.getItemProperty(StudentAttendanceSchema.CHECK_DATE).setValue(DateTimeUtil.getyyyyMMddDateSQL(teachingDate.getValue()));
								item.getItemProperty(StudentAttendanceSchema.ATTENDANCE_STATUS).setValue(attendanceStatus);
								CreateModifiedSchema.setCreateAndModified(item);
								studentAttendanceContainer.commit();
								studentImage.setId(studentAttendanceId.toString());
							}else{
								
								Item item = studentAttendanceContainer.getItem(new RowId(Integer.parseInt(studentImage.getId())));
								item.getItemProperty(StudentAttendanceSchema.ATTENDANCE_STATUS).setValue(attendanceStatus);

								CreateModifiedSchema.setCreateAndModified(item);
								studentAttendanceContainer.commit();
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						Notification.show("กรุณาระบุวันที่สอน", Type.WARNING_MESSAGE);
					}
				}
			});
		}
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
		        navigateTo(new SearchView(new ClassTeachingView(id,component,studentGroup.isVisible(), attendanceGroup.isVisible(), chartGroup.isVisible())));
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
