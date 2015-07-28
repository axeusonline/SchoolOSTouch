package com.ies.schoolos.ui.mobile;

import java.util.Date;

import javax.servlet.http.Cookie;

import org.vaadin.dialogs.ConfirmDialog;

import com.ies.schoolos.container.Container;
import com.ies.schoolos.schema.CookieSchema;
import com.ies.schoolos.schema.CreateModifiedSchema;
import com.ies.schoolos.schema.SchoolSchema;
import com.ies.schoolos.schema.SessionSchema;
import com.ies.schoolos.schema.UserSchema;
import com.ies.schoolos.type.Feature;
import com.ies.schoolos.type.UserType;
import com.ies.schoolos.type.dynamic.Province;
import com.ies.schoolos.type.dynamic.School;
import com.ies.schoolos.ui.mobile.info.AddPersonnelView;
import com.ies.schoolos.ui.mobile.info.AddStudentView;
import com.ies.schoolos.utility.BCrypt;
import com.ies.schoolos.utility.DateTimeUtil;
import com.ies.schoolos.utility.Images;
import com.ies.schoolos.utility.Notification;
import com.vaadin.addon.touchkit.ui.EmailField;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeListener;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;

public class LoginView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private Object schoolId;
	
	private Container container = new Container();
	private SQLContainer schoolContainer;
	private SQLContainer userContainer;
	
	private Item schoolItem;
	private Item userItem;
	
	private CssLayout boxLayout;
	private Image logoImg;
	private EmailField emailField;
	private PasswordField passwordField;
	private Button signonButton;
	private Button forgetPassButton;
	private Button studentRecruitButton;
	
	private HorizontalLayout schoolRegisLayout;
	private Button schoolRegisButton;
	
	private HorizontalLayout localRegisLayout;
	private Button studentSignup;
	private Button personnelSignup;
	
	/* สมัครนามโรงเรียน */
	private FieldGroup registrationBinder;
	private FieldGroup userBinder;
	
	private VerticalComponentGroup schoolRecruitGroup;
	private Label freeNoticeField;
	private TextField schoolNameField;
	private NativeSelect provinceIdField;
	private TextField firstnameField;
	private TextField lastnameField;
	private EmailField emailRecruitField;
	private PasswordField passwordSignupField;
	private PasswordField passwordSignupFieldAgainField;
	private Button signupButton;
	
	public LoginView() {
		schoolContainer = container.getSchoolContainer();
		userContainer = container.getUserContainer();
		
		setSizeFull();
		setStyleName("login");
		
		buildMainLayout();
		initLoginPage();
		
		Responsive.makeResponsive(this);
	}
	
	private void buildMainLayout(){
		initLoginBox();
	}
	
	private void initLoginBox(){		
		boxLayout = new CssLayout();
		boxLayout.setStyleName("login-box");
		addComponent(boxLayout);
		setComponentAlignment(boxLayout, Alignment.MIDDLE_CENTER);
		
		/* LOGO */
		HorizontalLayout imageLayout = new HorizontalLayout();
        imageLayout.setWidth("100%");
        boxLayout.addComponent(imageLayout);
        
		logoImg = new Image();
        logoImg.setImmediate(false);
        logoImg.setWidth("-1px");
        logoImg.setHeight("-1px");
        logoImg.setSource(new ThemeResource(Images.LOGO));
        imageLayout.addComponent(logoImg);
        imageLayout.setComponentAlignment(logoImg, Alignment.MIDDLE_CENTER);
		
        /* FORM */
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setSpacing(true);
        formLayout.setMargin(true);
        formLayout.setStyleName("login-text");
        boxLayout.addComponent(formLayout);
        
        HorizontalLayout emailLayout = new HorizontalLayout();
		emailLayout.setSizeFull();
		emailLayout.setMargin(true);
		formLayout.addComponent(emailLayout);
		
		emailField = new EmailField();
		emailField.setStyleName("textfield");
		emailField.setInputPrompt("อีเมลล์");
		emailField.setSizeFull();
		emailLayout.addComponent(emailField);
		emailLayout.setComponentAlignment(emailField, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout schoolLayout = new HorizontalLayout();
		schoolLayout.setSizeFull();
		schoolLayout.setMargin(true);
		formLayout.addComponent(schoolLayout);
		
		passwordField = new PasswordField();
		passwordField.setStyleName("textfield");
		passwordField.setInputPrompt("รหัสผ่าน");
		passwordField.setSizeFull();
		passwordField.addShortcutListener(new ShortcutListener("Shortcut Name", ShortcutAction.KeyCode.ENTER, null) {
				private static final long serialVersionUID = 1L;

				@Override
	        	public void handleAction(Object sender, Object target) {
					signonButton.click();
	        	}
	    	});
		schoolLayout.addComponent(passwordField);
		schoolLayout.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
		
		/* SIGON BUTTON */
		HorizontalLayout signOnLayout = new HorizontalLayout();
		signOnLayout.setWidth("100%");
		formLayout.addComponent(signOnLayout);
		
		signonButton = new Button("เข้าสู่ระบบ");
		signonButton.setSizeFull();
		signonButton.setStyleName("blue-button");
		signonButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(emailField.getValue().length() > 0 &&
						passwordField.getValue().length() > 0)
					login(emailField.getValue(), passwordField.getValue());
				else
					Notification.show("กรุณาพิมพ์ อีเมลล์ และ รหัสผ่าน", Type.WARNING_MESSAGE);
			}
		});
		
		signOnLayout.addComponent(signonButton);
		signOnLayout.setComponentAlignment(signonButton, Alignment.MIDDLE_CENTER);		
		
		/* LINK */
		HorizontalLayout linkLayout = new HorizontalLayout();
		linkLayout.setVisible(false);
		linkLayout.setWidth("100%");
		formLayout.addComponent(linkLayout);
		
		forgetPassButton = new Button();
		forgetPassButton.setCaption("ลืมรหัสผ่าน");
		forgetPassButton.setStyleName("forgetpass-button");
		linkLayout.addComponent(forgetPassButton);
		linkLayout.setComponentAlignment(forgetPassButton, Alignment.MIDDLE_CENTER);
		
		studentRecruitButton = new Button();
		studentRecruitButton.setVisible(false);
		studentRecruitButton.setCaption("สมัครเรียน");
		linkLayout.addComponent(studentRecruitButton);
		linkLayout.setComponentAlignment(studentRecruitButton, Alignment.MIDDLE_CENTER);
		
		/* LOCAL REGISTER */
		localRegisLayout = new HorizontalLayout();
		localRegisLayout.setWidth("100%");
		localRegisLayout.setSpacing(true);
		formLayout.addComponent(localRegisLayout);
		
		studentSignup = new Button("สำหรับนักเรียน");
		studentSignup.setStyleName("local-button");
		studentSignup.setSizeFull();
		studentSignup.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				invokeAskStudentPassword();
			}
		});
		localRegisLayout.addComponent(studentSignup);
		localRegisLayout.setComponentAlignment(studentSignup, Alignment.MIDDLE_CENTER);
		
		personnelSignup = new Button("สำหรับเจ้าหน้าที่");
		personnelSignup.setStyleName("local-button");
		personnelSignup.setSizeFull();
		personnelSignup.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				invokeAskEmployeePassword();
			}
		});
		localRegisLayout.addComponent(personnelSignup);
		localRegisLayout.setComponentAlignment(personnelSignup, Alignment.MIDDLE_CENTER);	
		
		/* SCHOOL REGISTER */
		schoolRegisLayout = new HorizontalLayout();
		schoolRegisLayout.setWidth("100%");
		formLayout.addComponent(schoolRegisLayout);
		
		schoolRegisButton = new Button("สร้างบัญชีใหม่ฟรี (สำหรับโรงเรียน)");
		schoolRegisButton.setStyleName("newuser-button");
		schoolRegisButton.setSizeFull();
		schoolRegisButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				invokeSchoolRegis();
				initSchoolFieldGroup();
				initUserFieldGroup();
			}
		});
		schoolRegisLayout.addComponent(schoolRegisButton);
		schoolRegisLayout.setComponentAlignment(schoolRegisButton, Alignment.MIDDLE_CENTER);
	}
	/* ================ SET DATA =================*/
	/* กำหนดค่า PK Auto Increment หลังการบันทึก */
	private void initSqlContainerRowIdChange(){
		/* นักเรียน */
		schoolContainer.addRowIdChangeListener(new RowIdChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void rowIdChange(RowIdChangeEvent arg0) {
				schoolId = arg0.getNewRowId();
			}
		});
		
	}
	/*จัดกลุ่มสำหรับ Field สมัครสมาชิก*/
	private void initSchoolFieldGroup(){			
		registrationBinder = new FieldGroup(schoolItem);
		registrationBinder.setBuffered(true);
		registrationBinder.bind(schoolNameField, SchoolSchema.NAME);
		registrationBinder.bind(provinceIdField, SchoolSchema.PROVINCE_ID);
	}	
	/*จัดกลุ่มสำหรับ Field ผุ้ใช้*/
	private void initUserFieldGroup(){
		userBinder = new FieldGroup(userItem);
		userBinder.setBuffered(true);
		userBinder.bind(firstnameField, UserSchema.FIRSTNAME);
		userBinder.bind(lastnameField, UserSchema.LASTNAME);
		userBinder.bind(emailRecruitField, UserSchema.EMAIL);
		userBinder.bind(passwordSignupField, UserSchema.PASSWORD);
	}
	/* ตั้งค่าหน้าต่างล้อกอิน */
	private void initLoginPage(){
		/* ถ้าเข้ากับ Url โรงเรียนให้ทำการปิดรับสมัคร แล้วขึ้นชื่อโรงเรียนแทน */
		if(SessionSchema.getSchoolID() != null){
			Item item = schoolContainer.getItem(new RowId(SessionSchema.getSchoolID()));
			
			/* ตรวจสอบว่า อยู่ช่วงรับสมัครใหม*/ 
			if(item.getItemProperty(SchoolSchema.RECRUIT_START_DATE).getValue() != null &&
					item.getItemProperty(SchoolSchema.RECRUIT_END_DATE).getValue() != null){
				if(DateTimeUtil.compareBetweenDate((Date)item.getItemProperty(SchoolSchema.RECRUIT_START_DATE).getValue(), 
						(Date)item.getItemProperty(SchoolSchema.RECRUIT_END_DATE).getValue())){
					studentRecruitButton.setVisible(true);
				}
			}
		}
	}
	/* ================ INVOKE LAYOUT =================*/
	/* ตั้งค่าหน้าสมัครโรงเรียน */
	private void invokeSchoolRegis(){
		final Window window = new Window("สมัครนามตัวแทนโรงเรียน");
		window.setWidth("308px");
		window.setHeight("420px");
		window.setResizable(false);
		window.setModal(true);
		window.center();
		UI.getCurrent().addWindow(window);
		
		schoolRecruitGroup = new VerticalComponentGroup();
		schoolRecruitGroup.setSizeFull();
		window.setContent(schoolRecruitGroup);
		
		freeNoticeField = new Label("สมัครใช้งานฟรี");
		schoolRecruitGroup.addComponent(freeNoticeField);
		
		schoolNameField = new TextField("ชื่อโรงเรียน");
		schoolNameField.setInputPrompt("ชื่อโรงเรียน");
		schoolNameField.setNullRepresentation("");
		schoolRecruitGroup.addComponent(schoolNameField);

		provinceIdField = new NativeSelect("จังหวัด");
		provinceIdField.setContainerDataSource(new Province());
		provinceIdField.setItemCaptionPropertyId("name");
		provinceIdField.setImmediate(true);
		provinceIdField.setNullSelectionAllowed(false);
		provinceIdField.setValue(1);
		schoolRecruitGroup.addComponent(provinceIdField);
		
		firstnameField = new TextField("ชื่อจริง");
		firstnameField.setInputPrompt("ชื่อจริง");
		firstnameField.setNullRepresentation("");
		schoolRecruitGroup.addComponent(firstnameField);
		
		lastnameField = new TextField("นามสกุล");
		lastnameField.setInputPrompt("นามสกุล");
		lastnameField.setNullRepresentation("");
		schoolRecruitGroup.addComponent(lastnameField);
		
		emailRecruitField = new EmailField("อีเมล์");
		emailRecruitField.setInputPrompt("อีเมล์");
		emailRecruitField.setNullRepresentation("");
		emailRecruitField.addValidator(new EmailValidator("ข้อมูลไม่ถูกต้อง"));
		schoolRecruitGroup.addComponent(emailRecruitField);
		
		passwordSignupField = new PasswordField("รหัสผ่าน");
		passwordSignupField.setInputPrompt("xxxx");
		passwordSignupField.setNullRepresentation("");
		schoolRecruitGroup.addComponent(passwordSignupField);
		
		passwordSignupFieldAgainField = new PasswordField("รหัสผ่านอีกครั้ง");
		passwordSignupFieldAgainField.setInputPrompt("xxxx");
		passwordSignupFieldAgainField.setNullRepresentation("");
		schoolRecruitGroup.addComponent(passwordSignupFieldAgainField);
		
		signupButton = new Button("สมัครใช้งาน");
		signupButton.setSizeFull();
		signupButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				/* ตรวจสอบ Email */
				if(!emailRecruitField.isValid()){
					Notification.show("อีเมล์ไม่ถูกต้อง", Type.WARNING_MESSAGE);
					return ;
				}
				
				//สมัครสมาชิก
				if(registrationBinder.getField(SchoolSchema.NAME).getValue() != null &&
					registrationBinder.getField(SchoolSchema.PROVINCE_ID).getValue() != null &&
					userBinder.getField(UserSchema.FIRSTNAME).getValue() != null &&
					userBinder.getField(UserSchema.LASTNAME).getValue() != null &&
					userBinder.getField(UserSchema.EMAIL).getValue() != null &&
					userBinder.getField(UserSchema.PASSWORD).getValue() != null){
					
					userContainer.addContainerFilter(new Equal(UserSchema.EMAIL, userBinder.getField(UserSchema.EMAIL).getValue().toString()));
					/* ตรวจสอบ Email ซ้ำ */
					if(userContainer.size() > 0){
						Notification.show("ไม่สามารถใช้อีเมล์ดังกล่าว เนื่องจากมีผู้ใช้ในการสมัครแล้ว", Type.WARNING_MESSAGE);
						return;
					}					
					/* ลบ WHERE ออกจาก Query เพื่อป้องกันการค้างของคำสั่่งจากการทำงานอื่นที่เรียกตัวแปรไปใช้ */
					userContainer.removeAllContainerFilters();
					
					if(passwordSignupField.getValue().equals(passwordSignupFieldAgainField.getValue())){
							try {								
								/* เพิ่มข้อมูลโรงเรียน */
								Object schoolTmpId = schoolContainer.addItem();
								schoolItem = schoolContainer.getItem(schoolTmpId);
								schoolItem.getItemProperty(SchoolSchema.NAME).setValue(registrationBinder.getField(SchoolSchema.NAME).getValue());
								schoolItem.getItemProperty(SchoolSchema.PROVINCE_ID).setValue(registrationBinder.getField(SchoolSchema.PROVINCE_ID).getValue());
								schoolItem.getItemProperty(SchoolSchema.CONTACT_EMAIL).setValue(userBinder.getField(UserSchema.EMAIL).getValue());
								initSchoolFieldGroup();
								registrationBinder.commit();
								schoolContainer.commit();
								
								/* เพิ่มข้อมูลโรงเรียน */
								Object userTmpId = userContainer.addItem();
								userItem = userContainer.getItem(userTmpId);
								userItem.getItemProperty(UserSchema.SCHOOL_ID).setValue(Integer.parseInt(schoolId.toString()));
								userItem.getItemProperty(UserSchema.FIRSTNAME).setValue(userBinder.getField(UserSchema.FIRSTNAME).getValue());
								userItem.getItemProperty(UserSchema.LASTNAME).setValue(userBinder.getField(UserSchema.LASTNAME).getValue());
								userItem.getItemProperty(UserSchema.EMAIL).setValue(userBinder.getField(UserSchema.EMAIL).getValue());
								userItem.getItemProperty(UserSchema.PASSWORD).setValue(BCrypt.hashpw(userBinder.getField(UserSchema.PASSWORD).getValue().toString(), BCrypt.gensalt()));
								userItem.getItemProperty(UserSchema.STATUS).setValue(0);
								userItem.getItemProperty(UserSchema.REF_USER_ID).setValue(Integer.parseInt(schoolId.toString()));
								userItem.getItemProperty(UserSchema.REF_USER_TYPE).setValue(UserType.ADMIN);
								userItem.getItemProperty(CreateModifiedSchema.CREATED_BY_ID).setValue(Integer.parseInt(schoolId.toString()));
								userItem.getItemProperty(CreateModifiedSchema.CREATED_DATE).setValue(new Date());
								
								Feature.setPermission(userItem, true);
								initUserFieldGroup();
								userBinder.commit();
								userContainer.commit();
								window.close();
								ConfirmDialog.show(UI.getCurrent(),"สมัครสมาชิก", "การสมัครเสร็จสิ้น พร้อมใช้งาน SchoolOS. คุณต้องการเข้าใช้งานขณะนี้ใช่หรือไม่?", "ตกลง", "ยกเลิก", new ConfirmDialog.Listener() {
									private static final long serialVersionUID = 1L;
									public void onClose(ConfirmDialog dialog) {
						                if (dialog.isConfirmed()) {
						                	login(emailRecruitField.getValue(), passwordSignupFieldAgainField.getValue());
						                }
						            }
						        });
								
							} catch (Exception e) {
								Notification.show("สมัครไม่สำเร็จ กรุณาลองใหม่อีกครั้ง", Type.WARNING_MESSAGE);
								e.printStackTrace();
							}
					}else{
						Notification.show("รหัสผ่านไม่ตรงกัน กรุณาระบุใหม่อีกครั้ง", Type.WARNING_MESSAGE);
					}			
				}else{
					Notification.show("กรุณากรอกข้อมูลให้ครบถ้วน", Type.WARNING_MESSAGE);
				}	
			}
		});
		schoolRecruitGroup.addComponent(signupButton);
		initSqlContainerRowIdChange();
	}
	/* เรียกหน้าต่าง ถามรหัสผ่าน (สำหรับเจ้าหน้าที่) */
	private void invokeAskEmployeePassword(){
		final Window window = new Window("เลือกโรงเรียน");
		window.setWidth("400px");
		window.setHeight("250px");
		window.center();
		UI.getCurrent().addWindow(window);
		
		VerticalComponentGroup schoolLayout = new VerticalComponentGroup();
		schoolLayout.setSizeFull();
		window.setContent(schoolLayout);
	
		final NativeSelect schoolProvinceIdField = new NativeSelect("จังหวัด");
		schoolProvinceIdField.setContainerDataSource(new Province());
		schoolProvinceIdField.setItemCaptionPropertyId("name");
		schoolProvinceIdField.setImmediate(true);
		schoolProvinceIdField.setNullSelectionAllowed(false);
		schoolProvinceIdField.setValue(1);
		schoolProvinceIdField.setRequired(true);
		schoolLayout.addComponent(schoolProvinceIdField);
		
		final NativeSelect schoolField = new NativeSelect("โรงเรียน");
		schoolField.setItemCaptionPropertyId("name");
		schoolField.setImmediate(true);
		schoolField.setNullSelectionAllowed(false);
		schoolField.setValue(1);
		schoolField.setRequired(true);
		schoolProvinceIdField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null)
					schoolField.setContainerDataSource(new School(Integer.parseInt(event.getProperty().getValue().toString())));
			}
		});
		schoolField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null)
					SessionSchema.setSchoolId(Integer.parseInt(event.getProperty().getValue().toString()));
			}
		});
		schoolLayout.addComponent(schoolField);
		
		final TextField passwordPersonnel = new TextField("รหัสสมัครสมาชิก");
		passwordPersonnel.setInputPrompt("พิมพ์รหัสผ่าน");
		passwordPersonnel.setRequired(true);
		schoolLayout.addComponent(passwordPersonnel);
		
		Button accepButton = new Button("ตกลง", FontAwesome.SEND);
		accepButton.setSizeFull();
		accepButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(schoolProvinceIdField.isValid() &&
						schoolField.isValid() &&
						passwordPersonnel.isValid()){
					Item item = schoolContainer.getItem(new RowId(SessionSchema.getSchoolID()));
					if(item.getItemProperty(SchoolSchema.PERSONNEL_SIGNUP_PASS).getValue().equals(passwordPersonnel.getValue())){
						ConfirmDialog.show(UI.getCurrent(), "อีเมล์จะถูกใช้เป็นบัญชีเข้าใช้งาน","คุณมีอีเมล์ใช่แล้วหรือไม่?","มีอีเมล์","ไม่มีอีเมล์",
					        new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 1L;
								public void onClose(ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										Item item = schoolContainer.getItem(new RowId(SessionSchema.getSchoolID()));
										if(item.getItemProperty(SchoolSchema.PERSONNEL_SIGNUP_PASS).getValue().equals(passwordPersonnel.getValue())){
											window.close();
											invokeEmployeeSignup();				
										}else{
											Notification.show("รหัสไม่ถูกต้อง", Type.WARNING_MESSAGE);
										}
					                }
					            }
				         });
					}else{
						Notification.show("รหัสไม่ถูกต้อง", Type.WARNING_MESSAGE);
					}
				}else{
					Notification.show("กรุณากรอกข้อมูลให้ครบถ้วน", Type.WARNING_MESSAGE);
				}		
			}
		});
		schoolLayout.addComponent(accepButton);
	}
	/* เรียกหน้าต่าง ถามรหัสผ่าน (สำหรับนักเรียน) */
	private void invokeAskStudentPassword(){
		final Window window = new Window("กรุณาพิมพ์รหัสผ่าน");
		window.setWidth("400px");
		window.setHeight("250px");
		window.center();
		UI.getCurrent().addWindow(window);
		
		VerticalComponentGroup schoolLayout = new VerticalComponentGroup();
		window.setContent(schoolLayout);
		
		final NativeSelect schoolProvinceIdField = new NativeSelect("จังหวัด");
		schoolProvinceIdField.setContainerDataSource(new Province());
		schoolProvinceIdField.setItemCaptionPropertyId("name");
		schoolProvinceIdField.setImmediate(true);
		schoolProvinceIdField.setNullSelectionAllowed(false);
		schoolProvinceIdField.setRequired(true);
		schoolProvinceIdField.setValue(1);
		schoolLayout.addComponent(schoolProvinceIdField);
		
		final NativeSelect schoolField = new NativeSelect("โรงเรียน");
		schoolField.setItemCaptionPropertyId("name");
		schoolField.setImmediate(true);
		schoolField.setNullSelectionAllowed(false);
		schoolField.setValue(1);
		schoolField.setRequired(true);
		schoolProvinceIdField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null)
					schoolField.setContainerDataSource(new School(Integer.parseInt(event.getProperty().getValue().toString())));
			}
		});
		schoolField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null)
					SessionSchema.setSchoolId(Integer.parseInt(event.getProperty().getValue().toString()));
			}
		});
		schoolLayout.addComponent(schoolField);
		
		final TextField passwordStudent = new TextField("รหัสสมัครสมาชิก");
		passwordStudent.setInputPrompt("พิมพ์รหัสผ่าน");
		passwordStudent.setRequired(true);
		schoolLayout.addComponent(passwordStudent);
		
		Button accepButton = new Button("ตกลง", FontAwesome.SEND);
		accepButton.setSizeFull();
		accepButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if(schoolProvinceIdField.isValid() &&
						schoolField.isValid() &&
						passwordStudent.isValid()){
					Item item = schoolContainer.getItem(new RowId(SessionSchema.getSchoolID()));
					if(item.getItemProperty(SchoolSchema.STUDENT_SIGNUP_PASS).getValue().equals(passwordStudent.getValue())){
						ConfirmDialog.show(UI.getCurrent(), "อีเมล์จะถูกใช้เป็นบัญชีเข้าใช้งาน","คุณมีอีเมล์ใช่แล้วหรือไม่?","มีอีเมล์","ไม่มีอีเมล์",
					        new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 1L;
								public void onClose(ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										Item item = schoolContainer.getItem(new RowId(SessionSchema.getSchoolID()));
										if(item.getItemProperty(SchoolSchema.PERSONNEL_SIGNUP_PASS).getValue().equals(passwordStudent.getValue())){
											window.close();
											invokeStudentSignup();				
										}else{
											Notification.show("รหัสไม่ถูกต้อง", Type.WARNING_MESSAGE);
										}
					                }
					            }
				         });
					}else{
						Notification.show("รหัสไม่ถูกต้อง", Type.WARNING_MESSAGE);
					}
				}else{
					Notification.show("กรุณากรอกข้อมูลให้ครบถ้วน", Type.WARNING_MESSAGE);
				}
			}
		});
		schoolLayout.addComponent(accepButton);
	}
	/* เรียกหน้าลงทะเบียนนักเรียน */
	private void invokeStudentSignup(){
		Window window = new Window();
		window.setSizeFull();
		window.setContent(new AddStudentView());
		window.center();
		UI.getCurrent().addWindow(window);
	}
	/* เรียกหน้าลงทะเบียนเจ้าหน้าที่*/
	private void invokeEmployeeSignup(){
		Window window = new Window();
		window.setSizeFull();
		window.setContent(new AddPersonnelView());
		window.center();
		UI.getCurrent().addWindow(window);
	}
	/*เข้าสู่ระบบ*/
	private void login(String username, String password){	
		userContainer.removeAllContainerFilters();
		userContainer.addContainerFilter(new Equal(UserSchema.EMAIL,username));

		if(userContainer.size() != 0){
			Item userItem = userContainer.getItem(userContainer.getIdByIndex(0));
			Item schoolItem = schoolContainer.getItem(new RowId(userItem.getItemProperty(SchoolSchema.SCHOOL_ID).getValue()));

			String passwordHash = userItem.getItemProperty(UserSchema.PASSWORD).getValue().toString();
			if(BCrypt.checkpw(password, passwordHash)){
				UI ui = UI.getCurrent();
				SessionSchema.setSession(
						Integer.parseInt(userItem.getItemProperty(UserSchema.SCHOOL_ID).getValue().toString()),
						Integer.parseInt(userItem.getItemProperty(UserSchema.USER_ID).getValue().toString()),
						Integer.parseInt(userItem.getItemProperty(UserSchema.REF_USER_TYPE).getValue().toString()),
						Integer.parseInt(userItem.getItemProperty(UserSchema.REF_USER_ID).getValue().toString()),
						schoolItem.getItemProperty(SchoolSchema.NAME).getValue(),
						userItem.getItemProperty(UserSchema.FIRSTNAME).getValue(),
						userItem.getItemProperty(UserSchema.LASTNAME).getValue(),
						schoolItem.getItemProperty(SchoolSchema.CONTACT_EMAIL).getValue());
				
				ui.setContent(new SchoolOSView());	
				/* จำบัญชีผู้ใช้และรหัสผ่าน */
				Cookie emailCookie = new Cookie(CookieSchema.EMAIL, username);
				emailCookie.setMaxAge(12000);
				emailCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
				VaadinService.getCurrentResponse().addCookie(emailCookie);
				
				Cookie passwordCookie = new Cookie(CookieSchema.PASSWORD, passwordHash);
				passwordCookie.setMaxAge(12000);
				passwordCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
				VaadinService.getCurrentResponse().addCookie(passwordCookie);
			}else{
				Notification.show("บัญชีผู้ใช้ หรือ รหัสผิดพลาด กรุณาลองใหม่อีกครั้ง", Type.WARNING_MESSAGE);
			}
		}else{
			Notification.show("ไม่พบบัญชีผู้ใช้", Type.WARNING_MESSAGE);
		}

		/* ลบ WHERE ออกจาก Query เพื่อป้องกันการค้างของคำสั่่งจากการทำงานอื่นที่เรียกตัวแปรไปใช้ */
		schoolContainer.removeAllContainerFilters();
	}
}
