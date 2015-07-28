package com.ies.schoolos.ui.mobile.info;

import org.vaadin.activelink.ActiveLink;

import com.ies.schoolos.container.Container;
import com.ies.schoolos.schema.SessionSchema;
import com.ies.schoolos.schema.UserSchema;
import com.ies.schoolos.schema.info.PersonnelGraduatedHistorySchema;
import com.ies.schoolos.schema.info.PersonnelSchema;
import com.ies.schoolos.schema.info.StudentStudySchema;
import com.ies.schoolos.type.GraduatedLevel;
import com.ies.schoolos.type.UserType;
import com.ies.schoolos.ui.mobile.SearchView;
import com.ies.schoolos.ui.mobile.component.ManagerView;
import com.ies.schoolos.utility.BCrypt;
import com.ies.schoolos.utility.Notification;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class EditUserView extends ManagerView {
	private static final long serialVersionUID = 1L;
	
	private boolean isPassEdited;
    private Boolean isFirstTimeStudent;
    private Object userId;
    private String passwordFieldHashed;
	private FieldGroup userBinder;
	private Item userItem;
	private Component component;
	
	private Container container;
	private SQLContainer userContainer;
	private SQLContainer studyContainer;
	private SQLContainer studentContainer; 
	private SQLContainer personnelContainer;  
	private SQLContainer pgContainer;
			
	/* แก้ไขข้อมูลส่วนตัว */
	private Panel panel;
	private VerticalLayout userLayout;
	private Button userButton;
	private Button graduateButton;
	
	private VerticalComponentGroup userDataGroup;
	private TextField firstnameField;
	private TextField lastnameField;
	private TextField emailField;
	private PasswordField  passwordField;
	private PasswordField passwordAgainField;
	
	private VerticalComponentGroup graduatedGroup;
	
	public EditUserView(Component component) {
		this.component = component;
		
		initialData();
		buildMainLayout();
		setPreviousComponent(component);
		initialUserBinding();
		Responsive.makeResponsive(userDataGroup);
		//Responsive.makeResponsive(userDataGroup);
	}
	
	public EditUserView(Component component, boolean isShowUserData) {
		this.component = component;
		
		initialData();
		buildMainLayout();
		setPreviousComponent(component);
		initialUserBinding();
		
		userDataGroup.setVisible(isShowUserData);
		userDataGroup.setVisible(!isShowUserData);
		if(isShowUserData)
			userButton.click();
		else
			graduateButton.click();
		
		Responsive.makeResponsive(userDataGroup);
		//Responsive.makeResponsive(userDataGroup);
	}
	
	/* เรียก หน้าตา แก้ไขข้อมูล*/
	private void buildMainLayout(){
		panel = new Panel();
		navView.setContent(panel);
		
		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setMargin(true);
		panel.setContent(mainLayout);
		
		userLayout = new VerticalLayout();
		userLayout.setSpacing(true);
		mainLayout.addComponent(userLayout);
		mainLayout.setComponentAlignment(userLayout, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout submenuLayout = new HorizontalLayout();
		submenuLayout.setWidth("100%");
		userLayout.addComponent(submenuLayout);
		
		userButton = new Button(FontAwesome.USER);
		userButton.setSizeFull();
		userButton.setStyleName("submenu-button-focus");
		userButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				userButton.setStyleName("submenu-button-focus");
				
				userDataGroup.setVisible(true);
				
				if(graduateButton != null && 
						graduatedGroup != null){
					graduateButton.setStyleName("submenu-button");
					graduatedGroup.setVisible(false);
				}
			}
		});
		submenuLayout.addComponent(userButton);
		
		if((int)SessionSchema.getRefUserType() == UserType.EMPLOYEE){
			graduateButton = new Button(FontAwesome.MORTAR_BOARD);
			graduateButton.setSizeFull();
			graduateButton.setStyleName("submenu-button");
			graduateButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					userButton.setStyleName("submenu-button");
					graduateButton.setStyleName("submenu-button-focus");
					
					userDataGroup.setVisible(false);
					graduatedGroup.setVisible(true);
				}
			});
			submenuLayout.addComponent(graduateButton);
		}
		
		userDataGroup = new VerticalComponentGroup();
		userDataGroup.setStyleName("user-info");
		userDataGroup.setVisible(false);
		userLayout.addComponent(userDataGroup);
		fetchUserData();
		
		if((int)SessionSchema.getRefUserType() == UserType.EMPLOYEE){
			graduatedGroup = new VerticalComponentGroup();
			graduatedGroup.setStyleName("user-info");
			graduatedGroup.setVisible(false);
			userLayout.addComponent(graduatedGroup);
			fetchGraduatedData();
		}
	    userButton.click();
	 }
	 
	/* ดึงข้อมูลบัญชีผู้ใช้ */
	private void fetchUserData(){
		Button infoButton = new Button("ข้อมูลเพิ่มเติม",FontAwesome.INFO_CIRCLE);
		infoButton.setSizeFull();
		infoButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if((int)SessionSchema.getRefUserType() == UserType.STUDENT){
			    	navigateTo(new EditStudentView(SessionSchema.getRefID(),false));
			    }else if((int)SessionSchema.getRefUserType() == UserType.EMPLOYEE){
			    	Window window = new Window("แก้ไขข้อมูลส่วนตัว");
			    	window.setSizeFull();
			    	window.center();
			    	UI.getCurrent().addWindow(window);

			    	if((int)SessionSchema.getRefUserType() == UserType.STUDENT){
				    	window.setContent(new EditStudentView(SessionSchema.getRefID(),false));
				    }else if((int)SessionSchema.getRefUserType() == UserType.EMPLOYEE){
				    	window.setContent(new EditPersonnelView(SessionSchema.getRefID()));
				    }
			    }
			}
		});
		userDataGroup.addComponent(infoButton);
	    
	    firstnameField = new TextField("ชื่อ");
	    firstnameField.setRequired(true);
	    firstnameField.setInputPrompt("ชื่อ");
	    userDataGroup.addComponent(firstnameField);
	   
	    lastnameField = new TextField("สกุล");
	    lastnameField.setRequired(true);
	    lastnameField.setInputPrompt("สกุล");
	    userDataGroup.addComponent(lastnameField);
	    
	    emailField = new TextField("อีเมลล์");
	    emailField.setRequired(true);
	    emailField.setInputPrompt("อีเมลล์");
	    emailField.setNullRepresentation("");
	    emailField.addValidator(new EmailValidator("อีเมลล์ไม่ถูกต้อง"));
	    userDataGroup.addComponent(emailField);
	    
	    HorizontalLayout linkLayout = new HorizontalLayout();
	    userDataGroup.addComponent(linkLayout);
	    linkLayout.addLayoutClickListener(new com.vaadin.event.LayoutEvents.LayoutClickListener() {
	    	private static final long serialVersionUID = 1L;
	        public void layoutClick(com.vaadin.event.LayoutEvents.LayoutClickEvent event)
	        {
	            if(!isPassEdited)
	                setShowPassword();
	            else
	                setHidePassword();
	        }
	    });
	    
	    ActiveLink passwordFieldChange = new ActiveLink();
	    passwordFieldChange.setCaption("เปลี่ยนรหัสผ่าน");
	    linkLayout.addComponent(passwordFieldChange);
	    
	    passwordField = new PasswordField("รหัสผ่าน");
	    passwordField.setInputPrompt("passwordField");
	    passwordField.setVisible(false);
	    passwordField.setNullRepresentation("");
	    passwordField.addValueChangeListener(new com.vaadin.data.Property.ValueChangeListener() {
	    	private static final long serialVersionUID = 1L;
	        public void valueChange(com.vaadin.data.Property.ValueChangeEvent event)
	        {
	            if(event.getProperty().getValue() != null)
	                passwordFieldHashed = BCrypt.hashpw(event.getProperty().getValue().toString(), BCrypt.gensalt());
	        }
	    });
	    
	    userDataGroup.addComponent(passwordField);
	    
	    passwordAgainField = new PasswordField("รหัสผ่านอีกครั้ง");
	    passwordAgainField.setInputPrompt("passwordField");
	    passwordAgainField.setVisible(false);
	    passwordAgainField.setNullRepresentation("");
	    userDataGroup.addComponent(passwordAgainField);
	    
	    Button userSave = new Button("บันทึก", FontAwesome.SAVE);
	    userSave.setSizeFull();
	    userSave.addClickListener(new com.vaadin.ui.Button.ClickListener() {
	    	private static final long serialVersionUID = 1L;
	        @SuppressWarnings("unchecked")
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event){
	            if(!userBinder.isValid())
	                
	            if(isFirstTimeStudent != null && isFirstTimeStudent.booleanValue()){
	                userItem.getItemProperty(UserSchema.IS_EDITED).setValue(Boolean.valueOf(true));
	                isFirstTimeStudent = false;
	            }
	            if(isPassEdited)
	                if(passwordField.getValue().equals(passwordAgainField.getValue())) {
	                    passwordField.setValue(passwordFieldHashed);
	                } else{
	                    Notification.show("รหัสผ่านไม่ตรงกัน กรุณาระบุใหม่อีกครั้ง", Type.WARNING_MESSAGE);
	                    return;
	                }
	                try{
						userBinder.commit();
						userContainer.commit(); 
						
						emailField.setReadOnly(true);
		                setHidePassword();
		                Notification.show("บันทึกสำเร็จ", Type.HUMANIZED_MESSAGE);
	
					} catch (Exception e) {
						Notification.show("บันทึกไม่สำเร็จ", Type.WARNING_MESSAGE);
						e.printStackTrace();
					}
	        }
	    });
	    userDataGroup.addComponent(userSave);
	}
	
	/* ดึงข้อมูลการศึกษา */
	private void fetchGraduatedData(){
		graduatedGroup.removeAllComponents();
		for (final Object itemId:pgContainer.getItemIds()) {
			Item item = pgContainer.getItem(itemId);
			HorizontalLayout graduatedLayout = new HorizontalLayout();
			graduatedLayout.setWidth("100%");
			graduatedLayout.addLayoutClickListener(new LayoutClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void layoutClick(LayoutClickEvent event) {
					Window window = new Window("แก้ไขประวัติการศึกษา");
			    	window.setSizeFull();
			    	window.center();
			    	window.addCloseListener(new CloseListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void windowClose(CloseEvent e) {
							pgContainer.addContainerFilter(new Equal(PersonnelGraduatedHistorySchema.PERSONNEL_ID, userId));
							fetchGraduatedData();
						}
					});
			    	UI.getCurrent().addWindow(window);
			    	
			    	window.setContent(new EditPersonnelGraduatedHistoryView(Integer.parseInt(itemId.toString())));
				}
			});
			graduatedGroup.addComponent(graduatedLayout);
			
			String yearString = "ไม่ระบุ";
			if(item.getItemProperty(PersonnelGraduatedHistorySchema.YEAR).getValue() != null)
				yearString = item.getItemProperty(PersonnelGraduatedHistorySchema.YEAR).getValue().toString();
			
			Label yearLabel = new Label(yearString);
			yearLabel.setContentMode(ContentMode.HTML);
			graduatedLayout.addComponent(yearLabel);
			graduatedLayout.setExpandRatio(yearLabel, 1);
			
			StringBuilder detail = new StringBuilder();
			detail.append(GraduatedLevel.getNameTh(Integer.parseInt(item.getItemProperty(PersonnelGraduatedHistorySchema.GRADUATED_LEVEL).getValue().toString())) + " ");
			detail.append(item.getItemProperty(PersonnelGraduatedHistorySchema.INSTITUTE).getValue().toString() + " ");
			detail.append("</br>");
			if(item.getItemProperty(PersonnelGraduatedHistorySchema.DEGREE).getValue() != null)
				detail.append(item.getItemProperty(PersonnelGraduatedHistorySchema.DEGREE).getValue().toString() + "</br>");
			if(item.getItemProperty(PersonnelGraduatedHistorySchema.MAJOR).getValue() != null)
				detail.append(item.getItemProperty(PersonnelGraduatedHistorySchema.MAJOR).getValue().toString() + "</br>");
			if(item.getItemProperty(PersonnelGraduatedHistorySchema.MINOR).getValue() != null)
				detail.append(item.getItemProperty(PersonnelGraduatedHistorySchema.MINOR).getValue().toString() + "</br>");
			
			Label detailLabel = new Label(detail.toString());
			detailLabel.setContentMode(ContentMode.HTML);
			graduatedLayout.addComponent(detailLabel);
			graduatedLayout.setExpandRatio(detailLabel,3);
		}
	}
	
	private void initialUserBinding(){
	    userBinder = new FieldGroup(userItem);
	    userBinder.setBuffered(true);
	    userBinder.bind(firstnameField, UserSchema.FIRSTNAME);
	    userBinder.bind(lastnameField, UserSchema.LASTNAME);
	    userBinder.bind(emailField, UserSchema.EMAIL);
	    userBinder.bind(passwordField, UserSchema.PASSWORD);
	    if(isFirstTimeStudent == null)
	        emailField.setReadOnly(true);
	    else{
	        if(isFirstTimeStudent){
	            emailField.setValue(null);
	            emailField.setReadOnly(false);
	        }else{
	            emailField.setReadOnly(true);
	        }
	    }
	}
 
	private void initialData(){
		isPassEdited = false;
        passwordFieldHashed = "";
        userItem = null;
        
		container = new Container();
		userContainer = container.getUserContainer();
        userItem = userContainer.getItem(new RowId(SessionSchema.getUserID()));
        if(userItem.getItemProperty(UserSchema.REF_USER_TYPE).getValue().toString().equals(Integer.toString(UserType.EMPLOYEE))){
            userId = userItem.getItemProperty(UserSchema.REF_USER_ID).getValue();
            personnelContainer = container.getPersonnelContainer();
            personnelContainer.addContainerFilter(new Equal(PersonnelSchema.PERSONNEL_ID, userId));
            pgContainer = container.getPersonnelGraduatedHistoryContainer();
            pgContainer.addContainerFilter(new Equal(PersonnelGraduatedHistorySchema.PERSONNEL_ID, userId));

        }else if(userItem.getItemProperty(UserSchema.REF_USER_TYPE).getValue().toString().equals(Integer.toString(UserType.STUDENT))){
        		studentContainer = container.getStudentContainer();
	            studentContainer.addContainerFilter(new Equal(StudentStudySchema.STUDENT_ID, userItem.getItemProperty(UserSchema.REF_USER_ID).getValue()));
	            
	            studyContainer = container.getStudentStudyContainer();
	            studyContainer.addContainerFilter(new Equal(StudentStudySchema.STUDENT_ID, userItem.getItemProperty(UserSchema.REF_USER_ID).getValue()));
	            
	            userId = Integer.parseInt(studyContainer.getIdByIndex(studyContainer.size()-1).toString());
	            if(!((Boolean)userItem.getItemProperty(UserSchema.IS_EDITED).getValue()).booleanValue())
	                isFirstTimeStudent = true;
	            else
	                isFirstTimeStudent = false;
        }
	 }
	
	/* แสดงการแก้รหัสผ่าน */
	private void setShowPassword(){
        isPassEdited = true;
        passwordField.setValue(null);
        passwordAgainField.setValue(null);
        passwordField.setVisible(true);
        passwordAgainField.setVisible(true);
    }

	/* ซ่อนการแก้รหัสผ่าน */
    private void setHidePassword(){
        isPassEdited = false;
        passwordField.setValue(passwordFieldHashed);
        passwordAgainField.setValue(passwordFieldHashed);
        passwordField.setVisible(false);
        passwordAgainField.setVisible(false);
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
		        navigateTo(new SearchView(new EditUserView(component,userDataGroup.isVisible())));
			}
		});

        addButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
		        addButton.addStyleName("toolbar-button-focus");
		        searchButton.setStyleName("");
		        timetableButton.setStyleName("");
		        
		        if(graduatedGroup.isVisible()){
		        	Window window = new Window("เพิ่มประวัติการศึกษา");
			    	window.setSizeFull();
			    	window.center();
			    	window.addCloseListener(new CloseListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void windowClose(CloseEvent e) {
							pgContainer.addContainerFilter(new Equal(PersonnelGraduatedHistorySchema.PERSONNEL_ID, userId));
							fetchGraduatedData();
						}
					});
			    	UI.getCurrent().addWindow(window);
			    	
			    	window.setContent(new AddPersonnelGraduatedHistoryView());
		        }
			}
		});
	}
}
