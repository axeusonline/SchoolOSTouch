package com.ies.schoolos.ui.mobile.info.layout;

import java.util.Date;
import java.util.Locale;

import org.vaadin.dialogs.ConfirmDialog;

import com.ies.schoolos.container.Container;
import com.ies.schoolos.schema.SchoolSchema;
import com.ies.schoolos.schema.SessionSchema;
import com.ies.schoolos.schema.UserSchema;
import com.ies.schoolos.schema.info.FamilySchema;
import com.ies.schoolos.schema.info.StudentSchema;
import com.ies.schoolos.schema.info.StudentStudySchema;
import com.ies.schoolos.type.AliveStatus;
import com.ies.schoolos.type.Blood;
import com.ies.schoolos.type.ClassRange;
import com.ies.schoolos.type.FamilyStatus;
import com.ies.schoolos.type.Gender;
import com.ies.schoolos.type.GuardianRelation;
import com.ies.schoolos.type.Nationality;
import com.ies.schoolos.type.Occupation;
import com.ies.schoolos.type.Parents;
import com.ies.schoolos.type.PeopleIdType;
import com.ies.schoolos.type.StudentCodeGenerateType;
import com.ies.schoolos.type.StudentComeWith;
import com.ies.schoolos.type.StudentPayerCourse;
import com.ies.schoolos.type.StudentStatus;
import com.ies.schoolos.type.Prename;
import com.ies.schoolos.type.Race;
import com.ies.schoolos.type.Religion;
import com.ies.schoolos.type.StudentStayWith;
import com.ies.schoolos.type.dynamic.City;
import com.ies.schoolos.type.dynamic.District;
import com.ies.schoolos.type.dynamic.Postcode;
import com.ies.schoolos.type.dynamic.Province;
import com.ies.schoolos.ui.mobile.component.NumberField;
import com.ies.schoolos.utility.DateTimeUtil;
import com.vaadin.addon.touchkit.ui.EmailField;
import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class StudentLayout extends TabBarView {
private static final long serialVersionUID = 1L;
	
	public boolean isEdit = false;
	public boolean isInsertParents = true;
	public boolean isDuplicateFather = false;
	public boolean isDuplicateMother = false;
	public boolean isDuplicateGuardian = false;
	
	/* ที่เก็บ Id Auto Increment เมื่อมีการ Commit SQLContainer 
	 * 0 แทนถึง id บิดา
	 * 1 แทนถึง id มารดา
	 * 2 แทนถึง id ผู้ปกครอง
	 * 3 แทนถึง id นักเรียน
	 * 4 แทนถึง id ข้อมูลการเรียนนักเรียน
	 * */
	public Object pkStore[] = new Object[5];

	private String generatedType = "";
	private String maxStudentCode = "";

	private Container container = new Container();
	private SQLContainer schoolContainer = container.getSchoolContainer();
	public SQLContainer sSqlContainer = container.getStudentContainer();
	public SQLContainer ssSqlContainer = container.getStudentStudyContainer();
	public SQLContainer fSqlContainer = container.getFamilyContainer();
	public SQLContainer userfSqlContainer = container.getUserContainer();
	
	public FieldGroup studentBinder;
	public FieldGroup studentStudyBinder;
	public FieldGroup fatherBinder;
	public FieldGroup motherBinder;
	public FieldGroup guardianBinder;

	private VerticalComponentGroup generalGroup;
	private OptionGroup peopleIdType;
	private TextField peopleId;
	private NativeSelect prename;
	private TextField firstname;
	private TextField lastname;
	private TextField firstnameNd;
	private TextField lastnameNd;
	private TextField firstnameRd;
	private TextField lastnameRd;
	private TextField nickname;
	private OptionGroup gender;
	private NativeSelect religion;
	private NativeSelect race;
	private NativeSelect nationality;
	private PopupDateField birthDate;
	private NativeSelect blood;
	private NumberField height;
	private NumberField weight;
	private TextField congenitalDisease;
	private TextField interested;
	private NumberField siblingQty;
	private NumberField siblingSequence;
	private NumberField siblingInSchoolQty;
	private Button studyNext;
	
	private VerticalComponentGroup studyGroup;
	private NativeSelect classRange;
	private OptionGroup autoGenerate;
	private TextField studentCode;
	private NativeSelect studentStatus;
	private NativeSelect studentComeWith;
	private TextField studentComeDescription;
	private NativeSelect studentPayerCourse;
	private NativeSelect studentStayWith;
	private Button generalBack;
	private Button graduatedNext;
	
	private VerticalComponentGroup graduatedGroup;
	private TextField graduatedSchool;
	private NativeSelect graduatedSchoolProvinceId;
	private NumberField graduatedGpa;
	private TextField graduatedYear;
	private NativeSelect graduatedClassRange;
	private Button studyBack;
	private Button addressNext;
	
	private VerticalComponentGroup addressGroup;
	private TextField tel;
	private TextField mobile;
	private EmailField email;
	private TextArea currentAddress;
	private NativeSelect currentCity;
	private NativeSelect currentDistrict;
	private NativeSelect currentProvince;
	private NativeSelect currentPostcode;
	private CheckBox isSameCurrentAddress;
	private TextArea censusAddress;
	private NativeSelect censusCity;
	private NativeSelect censusDistrict;
	private NativeSelect censusProvince;
	private NativeSelect censusPostcode;
	private CheckBox isBirthSameCurrentAddress;
	private TextArea birthAddress;
	private NativeSelect birthCity;
	private NativeSelect birthDistrict;
	private NativeSelect birthProvince;
	private NativeSelect birthPostcode;
	private Button graduatedBack;
	private Button fatherNext;
	private Button saveStudent;
	
	private VerticalComponentGroup fatherGroup;
	private OptionGroup fPeopleIdType;
	private TextField fPeopleid;
	private NativeSelect fPrename;
	private TextField fFirstname;
	private TextField fLastname;
	private TextField fFirstnameNd;
	private TextField fLastnameNd;
	private OptionGroup fGender;
	private NativeSelect fReligion;
	private NativeSelect fRace;
	private NativeSelect fNationality;
	private PopupDateField fBirthDate;
	private TextField fTel;
	private TextField fMobile;
	private TextField fEmail;
	private NumberField fSalary;
	private NativeSelect fAliveStatus;
	private NativeSelect fOccupation;
	private TextArea fJobAddress;
	private TextArea fCurrentAddress;
	private NativeSelect fCurrentCity;
	private NativeSelect fCurrentDistrict;
	private NativeSelect fCurrentProvinceId;
	private NativeSelect fCurrentPostcode;
	private Button addressBack;
	private Button motherNext;
	
	private VerticalComponentGroup motherGroup;
	private OptionGroup mPeopleIdType;
	private TextField mPeopleid;
	private NativeSelect mPrename;
	private TextField mFirstname;
	private TextField mLastname;
	private TextField mFirstnameNd;
	private TextField mLastnameNd;
	private OptionGroup mGender;
	private NativeSelect mReligion;
	private NativeSelect mRace;
	private NativeSelect mNationality;
	private PopupDateField mBirthDate;	
	private TextField mTel;
	private TextField mMobile;
	private TextField mEmail;
	private NumberField mSalary;
	private NativeSelect mAliveStatus;
	private NativeSelect mOccupation;
	private TextArea mJobAddress;
	private TextArea mCurrentAddress;
	private NativeSelect mCurrentCity;
	private NativeSelect mCurrentDistrict;
	private NativeSelect mCurrentProvinceId;
	private NativeSelect mCurrentPostcode;
	private NativeSelect familyStatus;
	private Button fatherBack;
	private Button guardianNext;
	
	private VerticalComponentGroup guardianGroup;
	private NativeSelect gParents;
	private OptionGroup gPeopleIdType;
	private TextField gPeopleid;
	private NativeSelect gPrename;
	private TextField gFirstname;
	private TextField gLastname;
	private TextField gFirstnameNd;
	private TextField gLastnameNd;
	private OptionGroup gGender;
	private NativeSelect gReligion;
	private NativeSelect gRace;
	private NativeSelect gNationality;
	private PopupDateField gBirthDate;	
	private TextField gTel;
	private TextField gMobile;
	private TextField gEmail;
	private NumberField gSalary;
	private NativeSelect gAliveStatus;
	private NativeSelect gOccupation;
	private TextArea gJobAddress;
	private TextArea gCurrentAddress;
	private NativeSelect gCurrentCity;
	private NativeSelect gCurrentDistrict;
	private NativeSelect gCurrentProvinceId;
	private NativeSelect gCurrentPostcode;
	private NativeSelect guardianRelation;
	private Button motherBack;
	private Button finish;
	private Button print;
	
	public StudentLayout() {
		setSizeFull();
		buildMainLayout();
	}
	
	public StudentLayout(boolean viewMode) {
		setSizeFull();
		buildMainLayout();
		if(viewMode){
			fatherNext.setVisible(true);
			saveStudent.setVisible(false);
			finish.setVisible(false);
		}
			
	}
	
	private void buildMainLayout()  {
		Item schoolItem = schoolContainer.getItem(new RowId(SessionSchema.getSchoolID()));
		if(schoolItem.getItemProperty(SchoolSchema.STUDENT_CODE_GENERATE_TYPE).getValue() != null)
			generatedType = schoolItem.getItemProperty(SchoolSchema.STUDENT_CODE_GENERATE_TYPE).getValue().toString();
		else
			generatedType = "1";
				
		generalInfoLayout();
		studyForm();
		graduatedForm();
		addressForm();
		fatherForm();
		motherForm();
		guardianForm();
		initFieldGroup();

		studentStatus.setValue(0);
		studentStatus.setReadOnly(true);
		studentCode.setValue(maxStudentCode);
		if(generatedType.equals("0"))
			autoGenerate.setReadOnly(true);
	}
	
	/*สร้าง Layout สำหรับข้อมูลทั่วไปนักเรียน*/
	private void generalInfoLayout()  {
		generalGroup = new VerticalComponentGroup();
		addTab(generalGroup,"ข้อมูลทั่วไป", FontAwesome.CHILD);
		
		peopleIdType = new OptionGroup("ประเภทบัตร",new PeopleIdType());
		peopleIdType.setItemCaptionPropertyId("name");
		peopleIdType.setImmediate(true);
		peopleIdType.setNullSelectionAllowed(false);
		peopleIdType.setWidth("-1px");
		peopleIdType.setHeight("-1px");
		generalGroup.addComponent(peopleIdType);
		
		peopleId = new TextField("หมายเลขประชาชน");
		peopleId.setInputPrompt("หมายเลขประชาชน");
		peopleId.setImmediate(false);
		peopleId.setWidth("-1px");
		peopleId.setHeight("-1px");
		peopleId.setNullRepresentation("");
		peopleId.addValidator(new StringLengthValidator("ข้อมูลไม่ถูกต้อง", 13, 20, false));
		peopleId.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event) {
				if(event.getText() != null){
					if(event.getText().length() >= 13){
						sSqlContainer.addContainerFilter(new Equal(StudentSchema.PEOPLE_ID,event.getText()));
						if(sSqlContainer.size() > 0){
							disableDuplicatePeopleIdForm();
							Notification.show("หมายเลขประชาชนถูกใช้งานแล้ว กรุณาระบุใหม่อีกครั้ง", Type.WARNING_MESSAGE);
						}else{
							enableDuplicatePeopleIdForm();
						}
						sSqlContainer.removeAllContainerFilters();
					}
				}
			}
		});
		generalGroup.addComponent(peopleId);
		
		prename = new NativeSelect("ชื่อต้น",new Prename());
		prename.setItemCaptionPropertyId("name");
		prename.setImmediate(true);
        prename.setNullSelectionAllowed(false);
		prename.setWidth("-1px");
		prename.setHeight("-1px");
		generalGroup.addComponent(prename);
		
		firstname = new TextField("ชื่อ");
		firstname.setInputPrompt("ชื่อ");
		firstname.setImmediate(false);
		firstname.setWidth("-1px");
		firstname.setHeight("-1px");
		firstname.setNullRepresentation("");
		generalGroup.addComponent(firstname);
		
		lastname = new TextField("สกุล");
		lastname.setInputPrompt("สกุล");
		lastname.setImmediate(false);
		lastname.setWidth("-1px");
		lastname.setHeight("-1px");
		lastname.setNullRepresentation("");
		generalGroup.addComponent(lastname);

		firstnameNd = new TextField("ชื่ออังกฤษ");
		firstnameNd.setInputPrompt("ชื่ออังกฤษ");
		firstnameNd.setImmediate(false);
		firstnameNd.setWidth("-1px");
		firstnameNd.setHeight("-1px");
		firstnameNd.setNullRepresentation("");
		generalGroup.addComponent(firstnameNd);
		
		lastnameNd = new TextField("สกุลอังกฤษ");
		lastnameNd.setInputPrompt("สกุลอังกฤษ");
		lastnameNd.setImmediate(false);
		lastnameNd.setWidth("-1px");
		lastnameNd.setHeight("-1px");
		lastnameNd.setNullRepresentation("");
		generalGroup.addComponent(lastnameNd);
		
		firstnameRd = new TextField("ชื่อภาษาที่สาม");
		firstnameRd.setInputPrompt("ชื่อภาษาที่สาม");
		firstnameRd.setImmediate(false);
		firstnameRd.setWidth("-1px");
		firstnameRd.setHeight("-1px");
		firstnameRd.setNullRepresentation("");
		generalGroup.addComponent(firstnameRd);
		
		lastnameRd = new TextField("สกุลภาษาที่สาม");
		lastnameRd.setInputPrompt("สกุลภาษาที่สาม");
		lastnameRd.setImmediate(false);
		lastnameRd.setWidth("-1px");
		lastnameRd.setHeight("-1px");
		lastnameRd.setNullRepresentation("");
		generalGroup.addComponent(lastnameRd);
		
		nickname = new TextField("ชื่อเล่น");
		nickname.setInputPrompt("ชื่อเล่น");
		nickname.setImmediate(false);
		nickname.setWidth("-1px");
		nickname.setHeight("-1px");
		nickname.setNullRepresentation("");
		generalGroup.addComponent(nickname);
		
		gender = new OptionGroup("เพศ",new Gender());
		gender.setItemCaptionPropertyId("name");
		gender.setImmediate(true);
		gender.setNullSelectionAllowed(false);
		gender.setWidth("-1px");
		gender.setHeight("-1px");
		generalGroup.addComponent(gender);
		
		religion = new NativeSelect("ศาสนา",new Religion());
		religion.setItemCaptionPropertyId("name");
		religion.setImmediate(true);
		religion.setNullSelectionAllowed(false);
		religion.setWidth("-1px");
		religion.setHeight("-1px");
		generalGroup.addComponent(religion);
		
		race = new NativeSelect("เชื้อชาติ",new Race());
		race.setItemCaptionPropertyId("name");
		race.setImmediate(true);
		race.setNullSelectionAllowed(false);
		race.setWidth("-1px");
		race.setHeight("-1px");
		generalGroup.addComponent(race);
		
		nationality = new NativeSelect("สัญชาติ",new Nationality());
		nationality.setItemCaptionPropertyId("name");
		nationality.setImmediate(true);
		nationality.setNullSelectionAllowed(false);
		nationality.setWidth("-1px");
		nationality.setHeight("-1px");
		generalGroup.addComponent(nationality);
		
		birthDate = new PopupDateField("วัน เดือน ปี เกิด");
		birthDate.setInputPrompt("วว/ดด/ปปปป");
		birthDate.setImmediate(false);
		birthDate.setWidth("-1px");
		birthDate.setHeight("-1px");
		birthDate.setDateFormat("dd/MM/yyyy");
		birthDate.setLocale(new Locale("th", "TH"));
		generalGroup.addComponent(birthDate);
		
		blood = new NativeSelect("หมู่เลือด",new Blood());
		blood.setItemCaptionPropertyId("name");
		blood.setImmediate(true);
		blood.setNullSelectionAllowed(false);
		blood.setWidth("-1px");
		blood.setHeight("-1px");
		generalGroup.addComponent(blood);
		
		height = new NumberField("ส่วนสูง");
		height.setInputPrompt("ส่วนสูง");
		height.setImmediate(false);
		height.setWidth("-1px");
		height.setHeight("-1px");
		height.setNullRepresentation("");
		generalGroup.addComponent(height);
		
		weight = new NumberField("น้ำหนัก");
		weight.setInputPrompt("น้ำหนัก");
		weight.setImmediate(false);
		weight.setWidth("-1px");
		weight.setHeight("-1px");
		weight.setNullRepresentation("");
		generalGroup.addComponent(weight);
		
		congenitalDisease = new TextField("โรคประจำตัว");
		congenitalDisease.setInputPrompt("โรคประจำตัว");
		congenitalDisease.setImmediate(false);
		congenitalDisease.setWidth("-1px");
		congenitalDisease.setHeight("-1px");
		congenitalDisease.setNullRepresentation("");
		generalGroup.addComponent(congenitalDisease);
		
		interested = new TextField("งานอดิเรก");
		interested.setInputPrompt("งานอดิเรก");
		interested.setImmediate(false);
		interested.setWidth("-1px");
		interested.setHeight("-1px");
		interested.setNullRepresentation("");
		generalGroup.addComponent(interested);
		
		siblingQty = new NumberField("จำนวนพี่น้อง");
		siblingQty.setInputPrompt("จำนวน");
		siblingQty.setImmediate(false);
		siblingQty.setDecimalAllowed(false);
		siblingQty.setWidth("-1px");
		siblingQty.setHeight("-1px");
		siblingQty.setNullRepresentation("");
		generalGroup.addComponent(siblingQty);
		
		siblingSequence = new NumberField("ลำดับพี่น้อง");
		siblingSequence.setInputPrompt("ลำดับที่");
		siblingSequence.setImmediate(false);
		siblingSequence.setDecimalAllowed(false);
		siblingSequence.setWidth("-1px");
		siblingSequence.setHeight("-1px");
		siblingSequence.setNullRepresentation("");
		generalGroup.addComponent(siblingSequence);
		
		siblingInSchoolQty = new NumberField("จำนวนพี่น้องที่ศึกษา");
		siblingInSchoolQty.setInputPrompt("จำนวน");
		siblingInSchoolQty.setImmediate(false);
		siblingInSchoolQty.setDecimalAllowed(false);
		siblingInSchoolQty.setWidth("-1px");
		siblingInSchoolQty.setHeight("-1px");
		siblingInSchoolQty.setNullRepresentation("");
		generalGroup.addComponent(siblingInSchoolQty);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setWidth("100%");
		generalGroup.addComponent(buttonLayout);
		
		studyNext = new Button(FontAwesome.ARROW_RIGHT);
		studyNext.setWidth("100%");
		studyNext.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(studyGroup);
			}
		});
		buttonLayout.addComponent(studyNext);	
	}
	
	/* สร้าง Layout สำหรับข้อมูลการเรียน */
	private void studyForm(){
		studyGroup = new VerticalComponentGroup();
		addTab(studyGroup,"ข้อมูลการเรียน", FontAwesome.GRADUATION_CAP);
				
		if(generatedType.equals("0")){
			classRange = new NativeSelect("ช่วงชั้นปัจจุบัน",new ClassRange());
			classRange.setItemCaptionPropertyId("name");
			classRange.setImmediate(true);
			classRange.setNullSelectionAllowed(false);
			classRange.setWidth("-1px");
			classRange.setHeight("-1px");
			classRange.addValueChangeListener(new ValueChangeListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void valueChange(ValueChangeEvent event) {
					if(event.getProperty().getValue() != null){
						studentCode.setValue(getStudentCode(event.getProperty().getValue().toString()));
						studentCode.setEnabled(false);
					}
				}
			});
			studyGroup.addComponent(classRange);
		
			autoGenerate = new OptionGroup("กำหนดรหัสประจำตัว",new StudentCodeGenerateType());
			autoGenerate.setItemCaptionPropertyId("name");
			autoGenerate.setImmediate(true);
			autoGenerate.setNullSelectionAllowed(false);
			autoGenerate.setWidth("-1px");
			autoGenerate.setHeight("-1px");
			autoGenerate.setValue(0);
			studyGroup.addComponent(autoGenerate);
		}else{
			maxStudentCode = getManaulStudentCode() + "(ชั่วคราว)";
		}
			
		studentCode = new TextField("รหัสประจำตัว");
		studentCode.setInputPrompt("รหัสประจำตัว");
		studentCode.setImmediate(false);
		studentCode.setWidth("-1px");
		studentCode.setHeight("-1px");
		studentCode.setNullRepresentation("");
		studyGroup.addComponent(studentCode);
		
		studentStatus = new NativeSelect("สถานะนักเรียน",new StudentStatus());
		studentStatus.setItemCaptionPropertyId("name");
		studentStatus.setImmediate(true);
		studentStatus.setNullSelectionAllowed(false);
		studentStatus.setWidth("-1px");
		studentStatus.setHeight("-1px");
		studentStatus.setValue(0);
		studyGroup.addComponent(studentStatus);
		
		studentComeWith = new NativeSelect("การมาโรงเรียน",new StudentComeWith());
		studentComeWith.setItemCaptionPropertyId("name");
		studentComeWith.setImmediate(true);
		studentComeWith.setNullSelectionAllowed(false);
		studentComeWith.setWidth("-1px");
		studentComeWith.setHeight("-1px");
		studyGroup.addComponent(studentComeWith);
		
		studentComeDescription = new TextField("รายละเอียดการมา");
		studentComeDescription.setInputPrompt("รายละเอียดการมา");
		studentComeDescription.setImmediate(false);
		studentComeDescription.setWidth("-1px");
		studentComeDescription.setHeight("-1px");
		studentComeDescription.setNullRepresentation("");
		studyGroup.addComponent(studentComeDescription);

		studentPayerCourse = new NativeSelect("ผู้ดูแลค่าเล่าเรียน",new StudentPayerCourse());
		studentPayerCourse.setItemCaptionPropertyId("name");
		studentPayerCourse.setImmediate(true);
		studentPayerCourse.setNullSelectionAllowed(false);
		studentPayerCourse.setWidth("-1px");
		studentPayerCourse.setHeight("-1px");
		studyGroup.addComponent(studentPayerCourse);

		studentStayWith = new NativeSelect("การพักอาศัย",new StudentStayWith());
		studentStayWith.setItemCaptionPropertyId("name");
		studentStayWith.setImmediate(true);
		studentStayWith.setNullSelectionAllowed(false);
		studentStayWith.setWidth("-1px");
		studentStayWith.setHeight("-1px");
		studyGroup.addComponent(studentStayWith);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth("100%");
		buttonLayout.setSpacing(true);
		studyGroup.addComponent(buttonLayout);
		
		generalBack = new Button(FontAwesome.ARROW_LEFT);
		generalBack.setWidth("100%");
		generalBack.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(generalGroup);
			}
		});
		buttonLayout.addComponents(generalBack);
		
		graduatedNext = new Button(FontAwesome.ARROW_RIGHT);
		graduatedNext.setWidth("100%");
		graduatedNext.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(graduatedGroup);				
			}
		});
		buttonLayout.addComponents(graduatedNext);
	}
	
	/*สร้าง Layout สำหรับประวัติการศึกษาของนักเรียน*/
	private void graduatedForm(){
		graduatedGroup = new VerticalComponentGroup();
		addTab(graduatedGroup,"ข้อมูลการศึกษา", FontAwesome.GRADUATION_CAP);
		
		graduatedSchool = new TextField("โรงเรียนที่จบ");
		graduatedSchool.setInputPrompt("ชื่อโรงเรียน");
		graduatedSchool.setImmediate(false);
		graduatedSchool.setWidth("-1px");
		graduatedSchool.setHeight("-1px");
		graduatedSchool.setNullRepresentation("");
		graduatedGroup.addComponent(graduatedSchool);

		graduatedSchoolProvinceId = new NativeSelect("จังหวัด",new Province());
		graduatedSchoolProvinceId.setItemCaptionPropertyId("name");
		graduatedSchoolProvinceId.setImmediate(true);
		graduatedSchoolProvinceId.setNullSelectionAllowed(false);
		graduatedSchoolProvinceId.setWidth("-1px");
		graduatedSchoolProvinceId.setHeight("-1px");
		graduatedGroup.addComponent(graduatedSchoolProvinceId);

		graduatedGpa = new NumberField("ผลการเรียนเฉลี่ย");
		graduatedGpa.setInputPrompt("ผลการเรียน");
		graduatedGpa.setImmediate(false);
		graduatedGpa.setWidth("-1px");
		graduatedGpa.setHeight("-1px");
		graduatedGpa.setNullRepresentation("");
		graduatedGroup.addComponent(graduatedGpa);

		graduatedYear = new TextField("ปีที่จบ");
		graduatedYear.setInputPrompt("ปีที่จบ");
		graduatedYear.setImmediate(false);
		graduatedYear.setWidth("-1px");
		graduatedYear.setHeight("-1px");
		graduatedYear.setNullRepresentation("");
		graduatedGroup.addComponent(graduatedYear);
				
		graduatedClassRange = new NativeSelect("ช่วงชั้นที่จบ",new ClassRange());
		graduatedClassRange.setItemCaptionPropertyId("name");
		graduatedClassRange.setImmediate(true);
		graduatedClassRange.setNullSelectionAllowed(false);
		graduatedClassRange.setWidth("-1px");
		graduatedClassRange.setHeight("-1px");
		graduatedGroup.addComponent(graduatedClassRange);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth("100%");
		buttonLayout.setSpacing(true);
		graduatedGroup.addComponent(buttonLayout);

		studyBack = new Button(FontAwesome.ARROW_LEFT);
		studyBack.setWidth("100%");
		studyBack.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(studyGroup);
			}
		});
		buttonLayout.addComponents(studyBack);
		
		addressNext = new Button(FontAwesome.ARROW_RIGHT);
		addressNext.setWidth("100%");
		addressNext.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(addressGroup);
			}
		});
		buttonLayout.addComponent(addressNext);
		
	}
	
	/*สร้าง Layout สำหรับที่อยู่ปัจจุบันของนักเรียน*/
	private void addressForm(){
		addressGroup = new VerticalComponentGroup();
		addTab(addressGroup,"ข้อมูลติดต่อ", FontAwesome.BOOK);
		
		tel = new TextField("เบอร์โทร");
		tel.setInputPrompt("เบอร์โทร");
		tel.setImmediate(false);
		tel.setWidth("-1px");
		tel.setHeight("-1px");
		tel.setNullRepresentation("");
		addressGroup.addComponent(tel);
		
		mobile = new TextField("มือถือ");
		mobile.setInputPrompt("มือถือ");
		mobile.setImmediate(false);
		mobile.setWidth("-1px");
		mobile.setHeight("-1px");
		mobile.setNullRepresentation("");
		addressGroup.addComponent(mobile);
		
		email = new EmailField("อีเมล์");
		email.setInputPrompt("อีเมล์");
		email.setImmediate(false);
		email.setWidth("-1px");
		email.setHeight("-1px");
		email.setNullRepresentation("");
		email.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event) {
				if(!isEdit){
					if(event.getText() != null){
						if(event.getText().length() >= 13){
							
							userfSqlContainer.addContainerFilter(new Equal(UserSchema.EMAIL,event.getText()));
							if(userfSqlContainer.size() > 0){
								disableDuplicateEmailForm();
								Notification.show("อีเมล์ถูกใช้งานแล้ว กรุณาระบุใหม่อีกครั้ง", Type.WARNING_MESSAGE);
							}else{
								enableDuplicateEmailForm();
							}
							userfSqlContainer.removeAllContainerFilters();
						}
					}
				}
			}
		});
		addressGroup.addComponent(email);
		
		Label currentLabel = new Label("ที่อยู่ปัจจุบัน");
		addressGroup.addComponent(currentLabel);
		
		currentAddress = new TextArea("ที่อยู่ปัจจุบัน");
		currentAddress.setInputPrompt("บ้านเลขที่ ซอย ถนน");
		currentAddress.setImmediate(false);
		currentAddress.setWidth("-1px");
		currentAddress.setHeight("-1px");
		currentAddress.setNullRepresentation("");
		addressGroup.addComponent(currentAddress);
		
		currentProvince = new NativeSelect("จังหวัด",new Province());
		currentProvince.setItemCaptionPropertyId("name");
		currentProvince.setImmediate(true);
		currentProvince.setNullSelectionAllowed(false);
		currentProvince.setWidth("-1px");
		currentProvince.setHeight("-1px");
		currentProvince.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null)
					currentDistrict.setContainerDataSource(new District(Integer.parseInt(event.getProperty().getValue().toString())));
			}
		});
		addressGroup.addComponent(currentProvince);
		
		currentDistrict = new NativeSelect("อำเภอ");
		currentDistrict.setItemCaptionPropertyId("name");
		currentDistrict.setImmediate(true);
		currentDistrict.setNullSelectionAllowed(false);
		currentDistrict.setWidth("-1px");
		currentDistrict.setHeight("-1px");
		currentDistrict.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					currentCity.setContainerDataSource(new City(Integer.parseInt(event.getProperty().getValue().toString())));
					currentPostcode.setContainerDataSource(new Postcode(Integer.parseInt(event.getProperty().getValue().toString())));
				}
			}
		});
		addressGroup.addComponent(currentDistrict);
		
		currentCity = new NativeSelect("ตำบล");
		currentCity.setItemCaptionPropertyId("name");
		currentCity.setImmediate(true);
		currentCity.setNullSelectionAllowed(false);
		currentCity.setWidth("-1px");
		currentCity.setHeight("-1px");
		addressGroup.addComponent(currentCity);
		
		currentPostcode = new NativeSelect("รหัสไปรษณีย์");
		currentPostcode.setItemCaptionPropertyId("name");
		currentPostcode.setImmediate(true);
		currentPostcode.setNullSelectionAllowed(false);
		currentPostcode.setWidth("-1px");
		currentPostcode.setHeight("-1px");
		addressGroup.addComponent(currentPostcode);
		
		isSameCurrentAddress = new CheckBox("ข้อมูลเดียวกับที่อยู่ปัจจุบัน");
		isSameCurrentAddress.setImmediate(true);
		isSameCurrentAddress.setWidth("-1px");
		isSameCurrentAddress.setHeight("-1px");
		isSameCurrentAddress.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					if((boolean)event.getProperty().getValue()){
						censusAddress.setValue(currentAddress.getValue());
						censusProvince.setValue(currentProvince.getValue());
						censusDistrict.setValue(currentDistrict.getValue());
						censusCity.setValue(currentCity.getValue());
						censusPostcode.setValue(currentPostcode.getValue());
					}else{
						censusAddress.setValue(null);
						censusProvince.setValue(null);
						censusDistrict.setValue(null);
						censusCity.setValue(null);
						censusPostcode.setValue(null);
					}
				}
			}
		});
		addressGroup.addComponent(isSameCurrentAddress);
		
		censusAddress = new TextArea("ที่อยู่ตามทะเบียนบ้าน");
		censusAddress.setInputPrompt("บ้านเลขที่ ซอย ถนน");
		censusAddress.setImmediate(false);
		censusAddress.setWidth("-1px");
		censusAddress.setHeight("-1px");
		censusAddress.setNullRepresentation("");
		addressGroup.addComponent(censusAddress);
		
		censusProvince = new NativeSelect("จังหวัดตามทะเบียนบ้าน",new Province());
		censusProvince.setItemCaptionPropertyId("name");
		censusProvince.setImmediate(true);
		censusProvince.setNullSelectionAllowed(false);
		censusProvince.setWidth("-1px");
		censusProvince.setHeight("-1px");
		censusProvince.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null)
					censusDistrict.setContainerDataSource(new District(Integer.parseInt(event.getProperty().getValue().toString())));
			}
		});
		addressGroup.addComponent(censusProvince);
		
		censusDistrict = new NativeSelect("อำเภอตามทะเบียนบ้าน");
		censusDistrict.setItemCaptionPropertyId("name");
		censusDistrict.setImmediate(true);
		censusDistrict.setNullSelectionAllowed(false);
		censusDistrict.setWidth("-1px");
		censusDistrict.setHeight("-1px");
		censusDistrict.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					censusCity.setContainerDataSource(new City(Integer.parseInt(event.getProperty().getValue().toString())));
					censusPostcode.setContainerDataSource(new Postcode(Integer.parseInt(event.getProperty().getValue().toString())));
				}
			}
		});
		addressGroup.addComponent(censusDistrict);
		
		censusCity = new NativeSelect("ตำบลตามทะเบียนบ้าน");
		censusCity.setItemCaptionPropertyId("name");
		censusCity.setImmediate(true);
		censusCity.setNullSelectionAllowed(false);
		censusCity.setWidth("-1px");
		censusCity.setHeight("-1px");
		addressGroup.addComponent(censusCity);
		
		censusPostcode = new NativeSelect("รหัสไปรษณีย์ตามทะเบียนบ้าน");
		censusPostcode.setItemCaptionPropertyId("name");
		censusPostcode.setImmediate(true);
		censusPostcode.setNullSelectionAllowed(false);
		censusPostcode.setWidth("-1px");
		censusPostcode.setHeight("-1px");
		addressGroup.addComponent(censusPostcode);
		
		isBirthSameCurrentAddress = new CheckBox("ข้อมูลเดียวกับที่อยู่ปัจจุบัน");
		isBirthSameCurrentAddress.setImmediate(true);
		isBirthSameCurrentAddress.setWidth("-1px");
		isBirthSameCurrentAddress.setHeight("-1px");
		isBirthSameCurrentAddress.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					if((boolean)event.getProperty().getValue()){
						birthAddress.setValue(currentAddress.getValue());
						birthProvince.setValue(currentProvince.getValue());
						birthDistrict.setValue(currentDistrict.getValue());
						birthCity.setValue(currentCity.getValue());
						birthPostcode.setValue(currentPostcode.getValue());
					}else{
						birthAddress.setValue(null);
						birthProvince.setValue(null);
						birthDistrict.setValue(null);
						birthCity.setValue(null);
						birthPostcode.setValue(null);
					}
				}
			}
		});
		addressGroup.addComponent(isBirthSameCurrentAddress);
		
		birthAddress = new TextArea("ที่อยู่สถานที่เกิด");
		birthAddress.setInputPrompt("บ้านเลขที่ ซอย ถนน");
		birthAddress.setImmediate(false);
		birthAddress.setWidth("-1px");
		birthAddress.setHeight("-1px");
		birthAddress.setNullRepresentation("");
		addressGroup.addComponent(birthAddress);
		
		birthProvince = new NativeSelect("จังหวัดสถานที่เกิด",new Province());
		birthProvince.setItemCaptionPropertyId("name");
		birthProvince.setImmediate(true);
		birthProvince.setNullSelectionAllowed(false);
		birthProvince.setWidth("-1px");
		birthProvince.setHeight("-1px");
		birthProvince.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null)
					birthDistrict.setContainerDataSource(new District(Integer.parseInt(event.getProperty().getValue().toString())));
			}
		});
		addressGroup.addComponent(birthProvince);
		
		birthDistrict = new NativeSelect("อำเภอสถานที่เกิด");
		birthDistrict.setItemCaptionPropertyId("name");
		birthDistrict.setImmediate(true);
		birthDistrict.setNullSelectionAllowed(false);
		birthDistrict.setWidth("-1px");
		birthDistrict.setHeight("-1px");
		birthDistrict.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					birthCity.setContainerDataSource(new City(Integer.parseInt(event.getProperty().getValue().toString())));
					birthPostcode.setContainerDataSource(new Postcode(Integer.parseInt(event.getProperty().getValue().toString())));
				}
			}
		});
		addressGroup.addComponent(birthDistrict);
		
		birthCity = new NativeSelect("ตำบลสถานที่เกิด");
		birthCity.setItemCaptionPropertyId("name");
		birthCity.setImmediate(true);
		birthCity.setNullSelectionAllowed(false);
		birthCity.setWidth("-1px");
		birthCity.setHeight("-1px");
		addressGroup.addComponent(birthCity);
		
		birthPostcode = new NativeSelect("รหัสไปรษณีย์สถานที่เกิด");
		birthPostcode.setItemCaptionPropertyId("name");
		birthPostcode.setImmediate(true);
		birthPostcode.setNullSelectionAllowed(false);
		birthPostcode.setWidth("-1px");
		birthPostcode.setHeight("-1px");
		addressGroup.addComponent(birthPostcode);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setWidth("100%");
		addressGroup.addComponent(buttonLayout);
		
		graduatedBack = new Button(FontAwesome.ARROW_LEFT);
		graduatedBack.setWidth("100%");
		graduatedBack.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(graduatedGroup);
			}
		});
		buttonLayout.addComponents(graduatedBack);
		
		fatherNext = new Button(FontAwesome.ARROW_RIGHT);
		fatherNext.setWidth("100%");
		fatherNext.setVisible(false);
		fatherNext.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(fatherGroup);
			}
		});
		buttonLayout.addComponents(fatherNext);
		
		saveStudent = new Button(FontAwesome.SAVE);
		saveStudent.setWidth("100%");
		saveStudent.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				ConfirmDialog.show(UI.getCurrent(), "ความพร้อมข้อมูล", "คุณต้องการเพิ่มข้อมูล บิดา มารดา ผู้ปกครอง ใช่หรือไม่?", "ใช่", "ไม่", new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 1L;
					public void onClose(ConfirmDialog dialog) {
						/* ตรวจสอบว่ามีข้อมูลบิดา มารดา ผู้ปกครองหรือไม่?
						 *  กรณี มีก็จะเข้าไปหน้าเพิ่มข้อมูลเจ้าหน้าที่
						 *  กรณี ไม่มี ก็จะบันทึกข้อมูลเลย */
		                if (dialog.isConfirmed()) {
		                	isInsertParents = true;
		            		familyStatus.setRequired(true);
		                	setSelectedTab(fatherGroup);
		                }else{
		                	isInsertParents = false;
		            		familyStatus.setRequired(false);
		                	finish.click();
		                }
		            }
		        });
			}
		});
		buttonLayout.addComponents(saveStudent);
	}
	
	/*สร้าง Layout สำหรับบิดา*/
	private void fatherForm(){
		fatherGroup = new VerticalComponentGroup();
		addTab(fatherGroup,"ข้อมูลบิดา", FontAwesome.MALE);
		
		fPeopleIdType = new OptionGroup("ประเภทบัตร",new PeopleIdType());
		fPeopleIdType.setItemCaptionPropertyId("name");
		fPeopleIdType.setImmediate(true);
		fPeopleIdType.setNullSelectionAllowed(false);
		fPeopleIdType.setRequired(true);
		fPeopleIdType.setWidth("-1px");
		fPeopleIdType.setHeight("-1px");
		fatherGroup.addComponent(fPeopleIdType);
		
		fPeopleid = new TextField("หมายเลขประชาชน");
		fPeopleid.setInputPrompt("หมายเลขประชาชน");
		fPeopleid.setImmediate(false);
		fPeopleid.setRequired(true);
		fPeopleid.setWidth("-1px");
		fPeopleid.setHeight("-1px");
		fPeopleid.setNullRepresentation("");
		fPeopleid.addValidator(new StringLengthValidator("ข้อมูลไม่ถูกต้อง", 13, 20, false));
		fPeopleid.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event) {
				if(event.getText() != null){
					if(event.getText().length() >= 13){
						fSqlContainer.addContainerFilter(new Equal(FamilySchema.PEOPLE_ID,event.getText()));
						if(fSqlContainer.size() > 0){
							Item item = fSqlContainer.getItem(fSqlContainer.getIdByIndex(0));
							fatherBinder.setItemDataSource(item);
							pkStore[0] = item.getItemProperty(FamilySchema.FAMILY_ID).getValue();
							fatherBinder.setEnabled(false);
							isDuplicateFather = true;
						}
						fSqlContainer.removeAllContainerFilters();
					}
				}
			}
		});
		fatherGroup.addComponent(fPeopleid);
		
		fPrename = new NativeSelect("ชื่อต้น",new Prename());
		fPrename.setValue("ชาย");
		fPrename.setItemCaptionPropertyId("name");
		fPrename.setImmediate(true);
		fPrename.setNullSelectionAllowed(false);
		fPrename.setRequired(true);
		fPrename.setWidth("-1px");
		fPrename.setHeight("-1px");
		fatherGroup.addComponent(fPrename);
		
		fFirstname = new TextField("ชื่อ");
		fFirstname.setInputPrompt("ชื่อ");
		fFirstname.setImmediate(false);
		fFirstname.setRequired(true);
		fFirstname.setWidth("-1px");
		fFirstname.setHeight("-1px");
		fFirstname.setNullRepresentation("");
		fatherGroup.addComponent(fFirstname);
		
		fLastname = new TextField("สกุล");
		fLastname.setInputPrompt("สกุล");
		fLastname.setImmediate(false);
		fLastname.setRequired(true);
		fLastname.setWidth("-1px");
		fLastname.setHeight("-1px");
		fLastname.setNullRepresentation("");
		fatherGroup.addComponent(fLastname);

		fFirstnameNd = new TextField("ชื่ออังกฤษ");
		fFirstnameNd.setInputPrompt("ชื่ออังกฤษ");
		fFirstnameNd.setImmediate(false);
		fFirstnameNd.setWidth("-1px");
		fFirstnameNd.setHeight("-1px");
		fFirstnameNd.setNullRepresentation("");
		fatherGroup.addComponent(fFirstnameNd);
		
		fLastnameNd = new TextField("สกุลอังกฤษ");
		fLastnameNd.setInputPrompt("สกุลอังกฤษ");
		fLastnameNd.setImmediate(false);
		fLastnameNd.setWidth("-1px");
		fLastnameNd.setHeight("-1px");
		fLastnameNd.setNullRepresentation("");
		fatherGroup.addComponent(fLastnameNd);
			
		fGender = new OptionGroup("เพศ",new Gender());
		fGender.setItemCaptionPropertyId("name");
		fGender.setImmediate(true);
		fGender.setNullSelectionAllowed(false);
		fGender.setRequired(true);
		fGender.setWidth("-1px");
		fGender.setHeight("-1px");
		fatherGroup.addComponent(fGender);
		
		fReligion = new NativeSelect("ศาสนา",new Religion());
		fReligion.setItemCaptionPropertyId("name");
		fReligion.setImmediate(true);
		fReligion.setNullSelectionAllowed(false);
		fReligion.setRequired(true);
		fReligion.setWidth("-1px");
		fReligion.setHeight("-1px");
		fatherGroup.addComponent(fReligion);
		
		fRace = new NativeSelect("เชื้อชาติ",new Race());
		fRace.setItemCaptionPropertyId("name");
		fRace.setImmediate(true);
		fRace.setNullSelectionAllowed(false);
		fRace.setRequired(true);
		fRace.setWidth("-1px");
		fRace.setHeight("-1px");
		fatherGroup.addComponent(fRace);
		
		fNationality = new NativeSelect("สัญชาติ",new Nationality());
		fNationality.setItemCaptionPropertyId("name");
		fNationality.setImmediate(true);
		fNationality.setNullSelectionAllowed(false);
		fNationality.setRequired(true);
		fNationality.setWidth("-1px");
		fNationality.setHeight("-1px");
		fatherGroup.addComponent(fNationality);
		
		fBirthDate = new PopupDateField("วัน เดือน ปี เกิด");
		fBirthDate.setInputPrompt("วว/ดด/ปปปป");
		fBirthDate.setImmediate(false);
		fBirthDate.setWidth("-1px");
		fBirthDate.setHeight("-1px");
		fBirthDate.setDateFormat("dd/MM/yyyy");
		fBirthDate.setLocale(new Locale("th", "TH"));
		fatherGroup.addComponent(fBirthDate);
		
		fTel = new TextField("เบอร์โทร");
		fTel.setInputPrompt("เบอร์โทร");
		fTel.setImmediate(false);
		fTel.setWidth("-1px");
		fTel.setHeight("-1px");
		fTel.setNullRepresentation("");
		fatherGroup.addComponent(fTel);
		
		fMobile = new TextField("มือถือ");
		fMobile.setInputPrompt("มือถือ");
		fMobile.setImmediate(false);
		fMobile.setRequired(true);
		fMobile.setWidth("-1px");
		fMobile.setHeight("-1px");
		fMobile.setNullRepresentation("");
		fatherGroup.addComponent(fMobile);
		
		fEmail = new TextField("อีเมล์");
		fEmail.setInputPrompt("อีเมล์");
		fEmail.setImmediate(false);
		fEmail.setWidth("-1px");
		fEmail.setHeight("-1px");
		fEmail.setNullRepresentation("");
		fEmail.addValidator(new EmailValidator("ข้อมูลไม่ถูกต้อง"));
		fatherGroup.addComponent(fEmail);
		
		fSalary = new NumberField("รายได้");
		fSalary.setInputPrompt("รายได้");
		fSalary.setImmediate(false);
		fSalary.setWidth("-1px");
		fSalary.setHeight("-1px");
		fSalary.setNullRepresentation("");
		fatherGroup.addComponent(fSalary);
		
		fAliveStatus = new NativeSelect("สถานภาพ",new AliveStatus());
		fAliveStatus.setItemCaptionPropertyId("name");
		fAliveStatus.setImmediate(true);
		fAliveStatus.setNullSelectionAllowed(false);
		fAliveStatus.setRequired(true);
		fAliveStatus.setWidth("-1px");
		fAliveStatus.setHeight("-1px");
		fatherGroup.addComponent(fAliveStatus);
		
		fOccupation = new NativeSelect("อาชีพ",new Occupation());
		fOccupation.setItemCaptionPropertyId("name");
		fOccupation.setImmediate(true);
		fOccupation.setNullSelectionAllowed(false);
		fOccupation.setRequired(true);
		fOccupation.setWidth("-1px");
		fOccupation.setHeight("-1px");
		fatherGroup.addComponent(fOccupation);
		
		fJobAddress = new TextArea("สถานที่ทำงาน");
		fJobAddress.setInputPrompt("บ้านเลขที่ ซอย ถนน");
		fJobAddress.setImmediate(false);
		fJobAddress.setWidth("-1px");
		fJobAddress.setHeight("-1px");
		fJobAddress.setNullRepresentation("");
		fatherGroup.addComponent(fJobAddress);
		
		fCurrentAddress = new TextArea("ที่อยู่ปัจจุบัน");
		fCurrentAddress.setInputPrompt("บ้านเลขที่ ซอย ถนน");
		fCurrentAddress.setImmediate(false);
		fCurrentAddress.setWidth("-1px");
		fCurrentAddress.setHeight("-1px");
		fCurrentAddress.setNullRepresentation("");
		fatherGroup.addComponent(fCurrentAddress);
		
		fCurrentProvinceId = new NativeSelect("จังหวัด",new Province());
		fCurrentProvinceId.setItemCaptionPropertyId("name");
		fCurrentProvinceId.setImmediate(true);
		fCurrentProvinceId.setNullSelectionAllowed(false);
		fCurrentProvinceId.setWidth("-1px");
		fCurrentProvinceId.setHeight("-1px");
		fCurrentProvinceId.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null)
					fCurrentDistrict.setContainerDataSource(new District(Integer.parseInt(event.getProperty().getValue().toString())));
			}
		});
		fatherGroup.addComponent(fCurrentProvinceId);
		
		fCurrentDistrict = new NativeSelect("อำเภอ");
		fCurrentDistrict.setItemCaptionPropertyId("name");
		fCurrentDistrict.setImmediate(true);
		fCurrentDistrict.setNullSelectionAllowed(false);
		fCurrentDistrict.setWidth("-1px");
		fCurrentDistrict.setHeight("-1px");
		fCurrentDistrict.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					fCurrentCity.setContainerDataSource(new City(Integer.parseInt(event.getProperty().getValue().toString())));
					fCurrentPostcode.setContainerDataSource(new Postcode(Integer.parseInt(event.getProperty().getValue().toString())));
				}
			}
		});
		fatherGroup.addComponent(fCurrentDistrict);
		
		fCurrentCity = new NativeSelect("ตำบล");
		fCurrentCity.setItemCaptionPropertyId("name");
		fCurrentCity.setImmediate(true);
		fCurrentCity.setNullSelectionAllowed(false);
		fCurrentCity.setWidth("-1px");
		fCurrentCity.setHeight("-1px");
		fatherGroup.addComponent(fCurrentCity);
		
		fCurrentPostcode = new NativeSelect("รหัสไปรษณีย์");
		fCurrentPostcode.setItemCaptionPropertyId("name");
		fCurrentPostcode.setImmediate(true);
		fCurrentPostcode.setNullSelectionAllowed(false);
		fCurrentPostcode.setWidth("-1px");
		fCurrentPostcode.setHeight("-1px");
		fatherGroup.addComponent(fCurrentPostcode);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setWidth("100%");
		fatherGroup.addComponent(buttonLayout);
		
		addressBack = new Button(FontAwesome.ARROW_LEFT);
		addressBack.setWidth("100%");
		addressBack.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(addressGroup);
			}
		});
		buttonLayout.addComponents(addressBack);
		
		motherNext = new Button(FontAwesome.ARROW_RIGHT);
		motherNext.setWidth("100%");
		motherNext.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(motherGroup);
			}
		});
		buttonLayout.addComponents(motherNext);
	}
	
	/*สร้าง Layout สำหรับมารดา*/
	private void motherForm(){
		motherGroup = new VerticalComponentGroup();
		addTab(motherGroup,"ข้อมูลมารดา", FontAwesome.FEMALE);
		
		mPeopleIdType = new OptionGroup("ประเภทบัตร",new PeopleIdType());
		mPeopleIdType.setItemCaptionPropertyId("name");
		mPeopleIdType.setImmediate(true);
		mPeopleIdType.setNullSelectionAllowed(false);
		mPeopleIdType.setRequired(true);
		mPeopleIdType.setWidth("-1px");
		mPeopleIdType.setHeight("-1px");
		motherGroup.addComponent(mPeopleIdType);
		
		mPeopleid = new TextField("หมายเลขประชาชน");
		mPeopleid.setInputPrompt("หมายเลขประชาชน");
		mPeopleid.setImmediate(false);
		mPeopleid.setRequired(true);
		mPeopleid.setWidth("-1px");
		mPeopleid.setHeight("-1px");
		mPeopleid.setNullRepresentation("");
		mPeopleid.addValidator(new StringLengthValidator("ข้อมูลไม่ถูกต้อง", 13, 20, false));
		mPeopleid.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event) {
				if(event.getText() != null){
					if(event.getText().length() >= 13){
						fSqlContainer.addContainerFilter(new Equal(FamilySchema.PEOPLE_ID,event.getText()));
						if(fSqlContainer.size() > 0){
							Item item = fSqlContainer.getItem(fSqlContainer.getIdByIndex(0));
							motherBinder.setItemDataSource(item);
							pkStore[1] = item.getItemProperty(FamilySchema.FAMILY_ID).getValue();
							motherBinder.setEnabled(false);
							isDuplicateMother = true;
						}
						fSqlContainer.removeAllContainerFilters();
					}
				}
			}
		});
		motherGroup.addComponent(mPeopleid);
		
		mPrename = new NativeSelect("ชื่อต้น",new Prename());
		mPrename.setItemCaptionPropertyId("name");
		mPrename.setImmediate(true);
		mPrename.setNullSelectionAllowed(false);
		mPrename.setRequired(true);
		mPrename.setWidth("-1px");
		mPrename.setHeight("-1px");
		motherGroup.addComponent(mPrename);
		
		mFirstname = new TextField("ชื่อ");
		mFirstname.setInputPrompt("ชื่อ");
		mFirstname.setImmediate(false);
		mFirstname.setRequired(true);
		mFirstname.setWidth("-1px");
		mFirstname.setHeight("-1px");
		mFirstname.setNullRepresentation("");
		motherGroup.addComponent(mFirstname);
		
		mLastname = new TextField("สกุล");
		mLastname.setInputPrompt("สกุล");
		mLastname.setImmediate(false);
		mLastname.setRequired(true);
		mLastname.setWidth("-1px");
		mLastname.setHeight("-1px");
		mLastname.setNullRepresentation("");
		motherGroup.addComponent(mLastname);

		mFirstnameNd = new TextField("ชื่ออังกฤษ");
		mFirstnameNd.setInputPrompt("ชื่ออังกฤษ");
		mFirstnameNd.setImmediate(false);
		mFirstnameNd.setWidth("-1px");
		mFirstnameNd.setHeight("-1px");
		mFirstnameNd.setNullRepresentation("");
		motherGroup.addComponent(mFirstnameNd);
		
		mLastnameNd = new TextField("สกุลอังกฤษ");
		mLastnameNd.setInputPrompt("สกุลอังกฤษ");
		mLastnameNd.setImmediate(false);
		mLastnameNd.setWidth("-1px");
		mLastnameNd.setHeight("-1px");
		mLastnameNd.setNullRepresentation("");
		motherGroup.addComponent(mLastnameNd);
			
		mGender = new OptionGroup("เพศ",new Gender());
		mGender.setItemCaptionPropertyId("name");
		mGender.setImmediate(true);
		mGender.setNullSelectionAllowed(false);
		mGender.setRequired(true);
		mGender.setWidth("-1px");
		mGender.setHeight("-1px");
		motherGroup.addComponent(mGender);
		
		mReligion = new NativeSelect("ศาสนา",new Religion());
		mReligion.setItemCaptionPropertyId("name");
		mReligion.setImmediate(true);
		mReligion.setNullSelectionAllowed(false);
		mReligion.setRequired(true);
		mReligion.setWidth("-1px");
		mReligion.setHeight("-1px");
		motherGroup.addComponent(mReligion);
		
		mRace = new NativeSelect("เชื้อชาติ",new Race());
		mRace.setItemCaptionPropertyId("name");
		mRace.setImmediate(true);
		mRace.setNullSelectionAllowed(false);
		mRace.setRequired(true);
		mRace.setWidth("-1px");
		mRace.setHeight("-1px");
		motherGroup.addComponent(mRace);
		
		mNationality = new NativeSelect("สัญชาติ",new Nationality());
		mNationality.setItemCaptionPropertyId("name");
		mNationality.setImmediate(true);
		mNationality.setNullSelectionAllowed(false);
		mNationality.setRequired(true);
		mNationality.setWidth("-1px");
		mNationality.setHeight("-1px");
		motherGroup.addComponent(mNationality);

		mBirthDate = new PopupDateField("วัน เดือน ปี เกิด");
		mBirthDate.setInputPrompt("วว/ดด/ปปปป");
		mBirthDate.setImmediate(false);
		mBirthDate.setWidth("-1px");
		mBirthDate.setHeight("-1px");
		mBirthDate.setDateFormat("dd/MM/yyyy");
		mBirthDate.setLocale(new Locale("th", "TH"));
		motherGroup.addComponent(mBirthDate);
		
		mTel = new TextField("เบอร์โทร");
		mTel.setInputPrompt("เบอร์โทร");
		mTel.setImmediate(false);
		mTel.setWidth("-1px");
		mTel.setHeight("-1px");
		mTel.setNullRepresentation("");
		motherGroup.addComponent(mTel);
		
		mMobile = new TextField("มือถือ");
		mMobile.setInputPrompt("มือถือ");
		mMobile.setImmediate(false);
		mMobile.setRequired(true);
		mMobile.setWidth("-1px");
		mMobile.setHeight("-1px");
		mMobile.setNullRepresentation("");
		motherGroup.addComponent(mMobile);
		
		mEmail = new TextField("อีเมล์");
		mEmail.setInputPrompt("อีเมล์");
		mEmail.setImmediate(false);
		mEmail.setWidth("-1px");
		mEmail.setHeight("-1px");
		mEmail.setNullRepresentation("");
		mEmail.addValidator(new EmailValidator("ข้อมูลไม่ถูกต้อง"));
		motherGroup.addComponent(mEmail);
		
		mSalary = new NumberField("รายได้");
		mSalary.setInputPrompt("รายได้");
		mSalary.setImmediate(false);
		mSalary.setWidth("-1px");
		mSalary.setHeight("-1px");
		mSalary.setNullRepresentation("");
		motherGroup.addComponent(mSalary);
		
		mAliveStatus = new NativeSelect("สถานภาพ",new AliveStatus());
		mAliveStatus.setItemCaptionPropertyId("name");
		mAliveStatus.setImmediate(true);
		mAliveStatus.setNullSelectionAllowed(false);
		mAliveStatus.setRequired(true);
		mAliveStatus.setWidth("-1px");
		mAliveStatus.setHeight("-1px");
		motherGroup.addComponent(mAliveStatus);
		
		mOccupation = new NativeSelect("อาชีพ",new Occupation());
		mOccupation.setItemCaptionPropertyId("name");
		mOccupation.setImmediate(true);
		mOccupation.setNullSelectionAllowed(false);
		mOccupation.setRequired(true);
		mOccupation.setWidth("-1px");
		mOccupation.setHeight("-1px");
		motherGroup.addComponent(mOccupation);
		
		mJobAddress = new TextArea("สถานที่ทำงาน");
		mJobAddress.setInputPrompt("บ้านเลขที่ ซอย ถนน");
		mJobAddress.setImmediate(false);
		mJobAddress.setWidth("-1px");
		mJobAddress.setHeight("-1px");
		mJobAddress.setNullRepresentation("");
		motherGroup.addComponent(mJobAddress);
		
		mCurrentAddress = new TextArea("ที่อยู่ปัจจุบัน");
		mCurrentAddress.setInputPrompt("บ้านเลขที่ ซอย ถนน");
		mCurrentAddress.setImmediate(false);
		mCurrentAddress.setWidth("-1px");
		mCurrentAddress.setHeight("-1px");
		mCurrentAddress.setNullRepresentation("");
		motherGroup.addComponent(mCurrentAddress);
		
		mCurrentProvinceId = new NativeSelect("จังหวัด",new Province());
		mCurrentProvinceId.setItemCaptionPropertyId("name");
		mCurrentProvinceId.setImmediate(true);
		mCurrentProvinceId.setNullSelectionAllowed(false);
		mCurrentProvinceId.setWidth("-1px");
		mCurrentProvinceId.setHeight("-1px");
		mCurrentProvinceId.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null)
					mCurrentDistrict.setContainerDataSource(new District(Integer.parseInt(event.getProperty().getValue().toString())));
			}
		});
		motherGroup.addComponent(mCurrentProvinceId);
		
		mCurrentDistrict = new NativeSelect("อำเภอ");
		mCurrentDistrict.setItemCaptionPropertyId("name");
		mCurrentDistrict.setImmediate(true);
		mCurrentDistrict.setNullSelectionAllowed(false);
		mCurrentDistrict.setWidth("-1px");
		mCurrentDistrict.setHeight("-1px");
		mCurrentDistrict.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					mCurrentCity.setContainerDataSource(new City(Integer.parseInt(event.getProperty().getValue().toString())));
					mCurrentPostcode.setContainerDataSource(new Postcode(Integer.parseInt(event.getProperty().getValue().toString())));
				}
			}
		});
		motherGroup.addComponent(mCurrentDistrict);
		
		mCurrentCity = new NativeSelect("ตำบล");
		mCurrentCity.setItemCaptionPropertyId("name");
		mCurrentCity.setImmediate(true);
		mCurrentCity.setNullSelectionAllowed(false);
		mCurrentCity.setWidth("-1px");
		mCurrentCity.setHeight("-1px");
		motherGroup.addComponent(mCurrentCity);
		
		mCurrentPostcode = new NativeSelect("รหัสไปรษณีย์");
		mCurrentPostcode.setItemCaptionPropertyId("name");
		mCurrentPostcode.setImmediate(true);
		mCurrentPostcode.setNullSelectionAllowed(false);
		mCurrentPostcode.setWidth("-1px");
		mCurrentPostcode.setHeight("-1px");
		motherGroup.addComponent(mCurrentPostcode);
				
		familyStatus = new NativeSelect("สถานะครอบครัว",new FamilyStatus());
		familyStatus.setItemCaptionPropertyId("name");
		familyStatus.setImmediate(true);
		familyStatus.setNullSelectionAllowed(false);
		familyStatus.setWidth("-1px");
		familyStatus.setHeight("-1px");
		motherGroup.addComponent(familyStatus);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setWidth("100%");
		motherGroup.addComponent(buttonLayout);
		
		fatherBack = new Button(FontAwesome.ARROW_LEFT);
		fatherBack.setWidth("100%");
		fatherBack.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(fatherGroup);
			}
		});
		buttonLayout.addComponents(fatherBack);
		
		guardianNext = new Button(FontAwesome.ARROW_RIGHT);
		guardianNext.setWidth("100%");
		guardianNext.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(guardianGroup);
			}
		});
		buttonLayout.addComponents(guardianNext);
	}
	
	/*สร้าง Layout สำหรับผู้ปกครอง*/
	private void guardianForm(){
		guardianGroup = new VerticalComponentGroup();
		addTab(guardianGroup,"ข้อมูลผู้ปกครอง", FontAwesome.USER);
		
		gParents = new NativeSelect("ผู้ปกครอง",new Parents());
		gParents.setItemCaptionPropertyId("name");
		gParents.setImmediate(true);
		gParents.setNullSelectionAllowed(false);
		gParents.setRequired(true);
		gParents.setWidth("-1px");
		gParents.setHeight("-1px");
		guardianGroup.addComponent(gParents);
		
		gPeopleIdType = new OptionGroup("ประเภทบัตร",new PeopleIdType());
		gPeopleIdType.setItemCaptionPropertyId("name");
		gPeopleIdType.setImmediate(true);
		gPeopleIdType.setNullSelectionAllowed(false);
		gPeopleIdType.setRequired(true);
		gPeopleIdType.setWidth("-1px");
		gPeopleIdType.setHeight("-1px");
		guardianGroup.addComponent(gPeopleIdType);
		
		gPeopleid = new TextField("หมายเลขประชาชน");
		gPeopleid.setInputPrompt("หมายเลขประชาชน");
		gPeopleid.setImmediate(false);
		gPeopleid.setRequired(true);
		gPeopleid.setNullRepresentation("");
		gPeopleid.setWidth("-1px");
		gPeopleid.setHeight("-1px");
		gPeopleid.setNullRepresentation("");
		gPeopleid.addValidator(new StringLengthValidator("ข้อมูลไม่ถูกต้อง", 13, 20, false));
		gPeopleid.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void textChange(TextChangeEvent event) {
				if(event.getText() != null){
					if(event.getText().length() >= 13){
						fSqlContainer.addContainerFilter(new Equal(FamilySchema.PEOPLE_ID,event.getText()));
						if(fSqlContainer.size() > 0){
							Item item = fSqlContainer.getItem(fSqlContainer.getIdByIndex(0));
							guardianBinder.setItemDataSource(item);
							pkStore[2] = item.getItemProperty(FamilySchema.FAMILY_ID).getValue();
							guardianBinder.setEnabled(false);
							isDuplicateFather = true;
						}
						fSqlContainer.removeAllContainerFilters();
					}
				}
			}
		});
		guardianGroup.addComponent(gPeopleid);
		
		gPrename = new NativeSelect("ชื่อต้น",new Prename());
		gPrename.setItemCaptionPropertyId("name");
		gPrename.setImmediate(true);
		gPrename.setNullSelectionAllowed(false);
		gPrename.setRequired(true);
		gPrename.setWidth("-1px");
		gPrename.setHeight("-1px");
		guardianGroup.addComponent(gPrename);
		
		gFirstname = new TextField("ชื่อ");
		gFirstname.setInputPrompt("ชื่อ");
		gFirstname.setImmediate(false);
		gFirstname.setRequired(true);
		gFirstname.setWidth("-1px");
		gFirstname.setHeight("-1px");
		gFirstname.setNullRepresentation("");
		guardianGroup.addComponent(gFirstname);
		
		gLastname = new TextField("สกุล");
		gLastname.setInputPrompt("สกุล");
		gLastname.setImmediate(false);
		gLastname.setRequired(true);
		gLastname.setWidth("-1px");
		gLastname.setHeight("-1px");
		gLastname.setNullRepresentation("");
		guardianGroup.addComponent(gLastname);

		gFirstnameNd = new TextField("ชื่ออังกฤษ");
		gFirstnameNd.setInputPrompt("ชื่ออังกฤษ");
		gFirstnameNd.setImmediate(false);
		gFirstnameNd.setWidth("-1px");
		gFirstnameNd.setHeight("-1px");
		gFirstnameNd.setNullRepresentation("");
		guardianGroup.addComponent(gFirstnameNd);
		
		gLastnameNd = new TextField("สกุลอังกฤษ");
		gLastnameNd.setInputPrompt("สกุลอังกฤษ");
		gLastnameNd.setImmediate(false);
		gLastnameNd.setWidth("-1px");
		gLastnameNd.setHeight("-1px");
		gLastnameNd.setNullRepresentation("");
		guardianGroup.addComponent(gLastnameNd);
			
		gGender = new OptionGroup("เพศ",new Gender());
		gGender.setItemCaptionPropertyId("name");
		gGender.setImmediate(true);
		gGender.setNullSelectionAllowed(false);
		gGender.setRequired(true);
		gGender.setWidth("-1px");
		gGender.setHeight("-1px");
		guardianGroup.addComponent(gGender);
		
		gReligion = new NativeSelect("ศาสนา",new Religion());
		gReligion.setItemCaptionPropertyId("name");
		gReligion.setImmediate(true);
		gReligion.setNullSelectionAllowed(false);
		gReligion.setRequired(true);
		gReligion.setWidth("-1px");
		gReligion.setHeight("-1px");
		guardianGroup.addComponent(gReligion);
		
		gRace = new NativeSelect("เชื้อชาติ",new Race());
		gRace.setItemCaptionPropertyId("name");
		gRace.setImmediate(true);
		gRace.setNullSelectionAllowed(false);
		gRace.setRequired(true);
		gRace.setWidth("-1px");
		gRace.setHeight("-1px");
		guardianGroup.addComponent(gRace);
		
		gNationality = new NativeSelect("สัญชาติ",new Nationality());
		gNationality.setItemCaptionPropertyId("name");
		gNationality.setImmediate(true);
		gNationality.setNullSelectionAllowed(false);
		gNationality.setRequired(true);
		gNationality.setWidth("-1px");
		gNationality.setHeight("-1px");
		guardianGroup.addComponent(gNationality);
		
		gBirthDate = new PopupDateField("วัน เดือน ปี เกิด");
		gBirthDate.setInputPrompt("วว/ดด/ปปปป");
		gBirthDate.setImmediate(false);
		gBirthDate.setWidth("-1px");
		gBirthDate.setHeight("-1px");		
		gBirthDate.setDateFormat("dd/MM/yyyy");
		gBirthDate.setLocale(new Locale("th", "TH"));
		guardianGroup.addComponent(gBirthDate);
		
		gTel = new TextField("เบอร์โทร");
		gTel.setInputPrompt("เบอร์โทร");
		gTel.setImmediate(false);
		gTel.setWidth("-1px");
		gTel.setHeight("-1px");
		gTel.setNullRepresentation("");
		guardianGroup.addComponent(gTel);
		
		gMobile = new TextField("มือถือ");
		gMobile.setInputPrompt("มือถือ");
		gMobile.setImmediate(false);
		gMobile.setRequired(true);
		gMobile.setWidth("-1px");
		gMobile.setHeight("-1px");
		gMobile.setNullRepresentation("");
		guardianGroup.addComponent(gMobile);
		
		gEmail = new TextField("อีเมล์");
		gEmail.setInputPrompt("อีเมล์");
		gEmail.setImmediate(false);
		gEmail.setWidth("-1px");
		gEmail.setHeight("-1px");
		gEmail.setNullRepresentation("");
		gEmail.addValidator(new EmailValidator("ข้อมูลไม่ถูกต้อง"));
		guardianGroup.addComponent(gEmail);
		
		gSalary = new NumberField("รายได้");
		gSalary.setInputPrompt("รายได้");
		gSalary.setImmediate(false);
		gSalary.setWidth("-1px");
		gSalary.setHeight("-1px");
		gSalary.setNullRepresentation("");
		guardianGroup.addComponent(gSalary);
		
		gAliveStatus = new NativeSelect("สถานภาพ",new AliveStatus());
		gAliveStatus.setItemCaptionPropertyId("name");
		gAliveStatus.setImmediate(true);
		gAliveStatus.setNullSelectionAllowed(false);
		gAliveStatus.setRequired(true);
		gAliveStatus.setWidth("-1px");
		gAliveStatus.setHeight("-1px");
		guardianGroup.addComponent(gAliveStatus);
		
		gOccupation = new NativeSelect("อาชีพ",new Occupation());
		gOccupation.setItemCaptionPropertyId("name");
		gOccupation.setImmediate(true);
		gOccupation.setNullSelectionAllowed(false);
		gOccupation.setRequired(true);
		gOccupation.setWidth("-1px");
		gOccupation.setHeight("-1px");
		guardianGroup.addComponent(gOccupation);
		
		gJobAddress = new TextArea("สถานที่ทำงาน");
		gJobAddress.setInputPrompt("บ้านเลขที่ ซอย ถนน");
		gJobAddress.setImmediate(false);
		gJobAddress.setWidth("-1px");
		gJobAddress.setHeight("-1px");
		gJobAddress.setNullRepresentation("");
		guardianGroup.addComponent(gJobAddress);
		
		gCurrentAddress = new TextArea("ที่อยู่ปัจจุบัน");
		gCurrentAddress.setInputPrompt("บ้านเลขที่ ซอย ถนน");
		gCurrentAddress.setImmediate(false);
		gCurrentAddress.setRequired(true);
		gCurrentAddress.setWidth("-1px");
		gCurrentAddress.setHeight("-1px");
		gCurrentAddress.setNullRepresentation("");
		guardianGroup.addComponent(gCurrentAddress);
		
		gCurrentProvinceId = new NativeSelect("จังหวัด",new Province());
		gCurrentProvinceId.setItemCaptionPropertyId("name");
		gCurrentProvinceId.setImmediate(true);
		gCurrentProvinceId.setNullSelectionAllowed(false);
		gCurrentProvinceId.setRequired(true);
		gCurrentProvinceId.setWidth("-1px");
		gCurrentProvinceId.setHeight("-1px");
		gCurrentProvinceId.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null)
					gCurrentDistrict.setContainerDataSource(new District(Integer.parseInt(event.getProperty().getValue().toString())));
			}
		});
		guardianGroup.addComponent(gCurrentProvinceId);
		
		gCurrentDistrict = new NativeSelect("อำเภอ",new Blood());
		gCurrentDistrict.setItemCaptionPropertyId("name");
		gCurrentDistrict.setImmediate(true);
		gCurrentDistrict.setNullSelectionAllowed(false);
		gCurrentDistrict.setRequired(true);
		gCurrentDistrict.setWidth("-1px");
		gCurrentDistrict.setHeight("-1px");
		gCurrentDistrict.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null){
					gCurrentCity.setContainerDataSource(new City(Integer.parseInt(event.getProperty().getValue().toString())));
					gCurrentPostcode.setContainerDataSource(new Postcode(Integer.parseInt(event.getProperty().getValue().toString())));
				}
			}
		});
		guardianGroup.addComponent(gCurrentDistrict);
		
		gCurrentCity = new NativeSelect("ตำบล");
		gCurrentCity.setItemCaptionPropertyId("name");
		gCurrentCity.setImmediate(true);
		gCurrentCity.setNullSelectionAllowed(false);
		gCurrentCity.setRequired(true);
		gCurrentCity.setWidth("-1px");
		gCurrentCity.setHeight("-1px");
		guardianGroup.addComponent(gCurrentCity);
		
		gCurrentPostcode = new NativeSelect("รหัสไปรษณีย์");
		gCurrentPostcode.setItemCaptionPropertyId("name");
		gCurrentPostcode.setImmediate(true);
		gCurrentPostcode.setNullSelectionAllowed(false);
		gCurrentPostcode.setRequired(true);
		gCurrentPostcode.setWidth("-1px");
		gCurrentPostcode.setHeight("-1px");
		guardianGroup.addComponent(gCurrentPostcode);
		
		guardianRelation = new NativeSelect("ความสัมพันธ์ผู้ปกครอง",new GuardianRelation());
		guardianRelation.setItemCaptionPropertyId("name");
		guardianRelation.setImmediate(true);
		guardianRelation.setNullSelectionAllowed(false);
		guardianRelation.setRequired(true);
		guardianRelation.setWidth("-1px");
		guardianRelation.setHeight("-1px");
		guardianRelation.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void valueChange(ValueChangeEvent event) {
				if(event.getProperty().getValue() != null)
					guardianRelation.setValue(Integer.parseInt(event.getProperty().getValue().toString()));
			}
		});
		guardianGroup.addComponent(guardianRelation);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setWidth("100%");
		guardianGroup.addComponent(buttonLayout);
		
		motherBack = new Button(FontAwesome.ARROW_LEFT);
		motherBack.setWidth("100%");
		motherBack.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				setSelectedTab(motherGroup);
			}
		});
		buttonLayout.addComponents(motherBack);
		
		finish = new Button("ตกลง",FontAwesome.SAVE);
		finish.setWidth("100%");
		buttonLayout.addComponents(finish);
		
		print = new Button("พิมพ์ใบสมัคร",FontAwesome.PRINT);
		print.setVisible(false);
		print.setWidth("100%");
		buttonLayout.addComponents(print);
	}
	
	/*กำหนดค่าเริ่มต้นภายในฟอร์ม นักเรียน บิดา มารดา*/
	private void initFieldGroup(){		
		studentBinder = new FieldGroup();
		studentBinder.setBuffered(true);
		studentBinder.bind(peopleIdType, StudentSchema.PEOPLE_ID_TYPE);
		studentBinder.bind(peopleId, StudentSchema.PEOPLE_ID);
		studentBinder.bind(prename, StudentSchema.PRENAME);
		studentBinder.bind(firstname, StudentSchema.FIRSTNAME);
		studentBinder.bind(lastname, StudentSchema.LASTNAME);
		studentBinder.bind(firstnameNd, StudentSchema.FIRSTNAME_ND);
		studentBinder.bind(lastnameNd, StudentSchema.LASTNAME_ND);
		studentBinder.bind(firstnameRd, StudentSchema.FIRSTNAME_RD);
		studentBinder.bind(lastnameRd, StudentSchema.LASTNAME_RD);		
		studentBinder.bind(nickname, StudentSchema.NICKNAME);
		studentBinder.bind(gender, StudentSchema.GENDER);
		studentBinder.bind(religion, StudentSchema.RELIGION);
		studentBinder.bind(race, StudentSchema.RACE);
		studentBinder.bind(nationality, StudentSchema.NATIONALITY);
		studentBinder.bind(birthDate, StudentSchema.BIRTH_DATE);
		studentBinder.bind(blood, StudentSchema.BLOOD);
		studentBinder.bind(height, StudentSchema.HEIGHT);
		studentBinder.bind(weight, StudentSchema.WEIGHT);
		studentBinder.bind(congenitalDisease, StudentSchema.CONGENITAL_DISEASE);
		studentBinder.bind(interested, StudentSchema.INTERESTED);
		studentBinder.bind(siblingQty, StudentSchema.SIBLING_QTY);
		studentBinder.bind(siblingSequence, StudentSchema.SIBLING_SEQUENCE);
		studentBinder.bind(siblingInSchoolQty, StudentSchema.SIBLING_INSCHOOL_QTY);
		studentBinder.bind(familyStatus, StudentSchema.FAMILY_STATUS);
		
		studentStudyBinder = new FieldGroup();
		studentStudyBinder.setBuffered(true);
		studentStudyBinder.bind(studentCode, StudentStudySchema.STUDENT_CODE);
		studentStudyBinder.bind(studentStatus, StudentStudySchema.STUDENT_STATUS);
		studentStudyBinder.bind(studentComeWith, StudentStudySchema.STUDENT_COME_WITH);
		studentStudyBinder.bind(studentComeDescription, StudentStudySchema.STUDENT_COME_DESCRIPTION);
		studentStudyBinder.bind(studentPayerCourse, StudentStudySchema.STUDENT_PAYER_COURSE);
		studentStudyBinder.bind(studentStayWith, StudentStudySchema.STUDENT_STAY_WITH);
		studentStudyBinder.bind(graduatedSchool, StudentStudySchema.GRADUATED_SCHOOL);
		studentStudyBinder.bind(graduatedSchoolProvinceId, StudentStudySchema.GRADUATED_SCHOOL_PROVINCE_ID);
		studentStudyBinder.bind(graduatedGpa, StudentStudySchema.GRADUATED_GPA);
		studentStudyBinder.bind(graduatedYear, StudentStudySchema.GRADUATED_YEAR);
		studentStudyBinder.bind(graduatedClassRange, StudentStudySchema.GRADUATED_CLASS_RANGE);
		studentStudyBinder.bind(tel, StudentStudySchema.TEL);
		studentStudyBinder.bind(mobile, StudentStudySchema.MOBILE);
		studentStudyBinder.bind(email, StudentStudySchema.EMAIL);
		studentStudyBinder.bind(currentAddress, StudentStudySchema.CURRENT_ADDRESS);
		studentStudyBinder.bind(currentCity, StudentStudySchema.CURRENT_CITY_ID);
		studentStudyBinder.bind(currentDistrict, StudentStudySchema.CURRENT_DISTRICT_ID);
		studentStudyBinder.bind(currentProvince, StudentStudySchema.CURRENT_PROVINCE_ID);
		studentStudyBinder.bind(currentPostcode, StudentStudySchema.CURRENT_POSTCODE_ID);
		studentStudyBinder.bind(censusAddress, StudentStudySchema.CENSUS_ADDRESS);
		studentStudyBinder.bind(censusCity, StudentStudySchema.CENSUS_CITY_ID);
		studentStudyBinder.bind(censusDistrict, StudentStudySchema.CENSUS_DISTRICT_ID);
		studentStudyBinder.bind(censusProvince, StudentStudySchema.CENSUS_PROVINCE_ID);
		studentStudyBinder.bind(censusPostcode, StudentStudySchema.CENSUS_POSTCODE_ID);
		studentStudyBinder.bind(birthAddress, StudentStudySchema.BIRTH_ADDRESS);
		studentStudyBinder.bind(birthCity, StudentStudySchema.BIRTH_CITY_ID);
		studentStudyBinder.bind(birthDistrict, StudentStudySchema.BIRTH_DISTRICT_ID);
		studentStudyBinder.bind(birthProvince, StudentStudySchema.BIRTH_PROVINCE_ID);
		studentStudyBinder.bind(birthPostcode, StudentStudySchema.BIRTH_POSTCODE_ID);
		
		fatherBinder = new FieldGroup();
		fatherBinder.setBuffered(true);
		fatherBinder.bind(fPeopleIdType, FamilySchema.PEOPLE_ID_TYPE);
		fatherBinder.bind(fPeopleid, FamilySchema.PEOPLE_ID);
		fatherBinder.bind(fPrename, FamilySchema.PRENAME);
		fatherBinder.bind(fFirstname, FamilySchema.FIRSTNAME);
		fatherBinder.bind(fLastname, FamilySchema.LASTNAME);
		fatherBinder.bind(fFirstnameNd, FamilySchema.FIRSTNAME_ND);
		fatherBinder.bind(fLastnameNd, FamilySchema.LASTNAME_ND);
		fatherBinder.bind(fGender, FamilySchema.GENDER);
		fatherBinder.bind(fReligion, FamilySchema.RELIGION);
		fatherBinder.bind(fRace, FamilySchema.RACE);
		fatherBinder.bind(fNationality, FamilySchema.NATIONALITY);
		fatherBinder.bind(fBirthDate, FamilySchema.BIRTH_DATE);
		fatherBinder.bind(fTel, FamilySchema.TEL);
		fatherBinder.bind(fMobile, FamilySchema.MOBILE);
		fatherBinder.bind(fEmail, FamilySchema.EMAIL);
		fatherBinder.bind(fSalary, FamilySchema.SALARY);
		fatherBinder.bind(fAliveStatus, FamilySchema.ALIVE_STATUS);
		fatherBinder.bind(fOccupation, FamilySchema.OCCUPATION);
		fatherBinder.bind(fJobAddress, FamilySchema.JOB_ADDRESS);
		fatherBinder.bind(fCurrentAddress, FamilySchema.CURRENT_ADDRESS);
		fatherBinder.bind(fCurrentProvinceId, FamilySchema.CURRENT_PROVINCE_ID);
		fatherBinder.bind(fCurrentDistrict, FamilySchema.CURRENT_DISTRICT_ID);
		fatherBinder.bind(fCurrentCity, FamilySchema.CURRENT_CITY_ID);
		fatherBinder.bind(fCurrentPostcode, FamilySchema.CURRENT_POSTCODE_ID);
		
		motherBinder = new FieldGroup();
		motherBinder.setBuffered(true);
		motherBinder.bind(mPeopleIdType, FamilySchema.PEOPLE_ID_TYPE);
		motherBinder.bind(mPeopleid, FamilySchema.PEOPLE_ID);
		motherBinder.bind(mPrename, FamilySchema.PRENAME);
		motherBinder.bind(mFirstname, FamilySchema.FIRSTNAME);
		motherBinder.bind(mLastname, FamilySchema.LASTNAME);
		motherBinder.bind(mFirstnameNd, FamilySchema.FIRSTNAME_ND);
		motherBinder.bind(mLastnameNd, FamilySchema.LASTNAME_ND);
		motherBinder.bind(mGender, FamilySchema.GENDER);
		motherBinder.bind(mReligion, FamilySchema.RELIGION);
		motherBinder.bind(mRace, FamilySchema.RACE);
		motherBinder.bind(mNationality, FamilySchema.NATIONALITY);
		motherBinder.bind(mBirthDate, FamilySchema.BIRTH_DATE);
		motherBinder.bind(mTel, FamilySchema.TEL);
		motherBinder.bind(mMobile, FamilySchema.MOBILE);
		motherBinder.bind(mEmail, FamilySchema.EMAIL);
		motherBinder.bind(mSalary, FamilySchema.SALARY);
		motherBinder.bind(mAliveStatus, FamilySchema.ALIVE_STATUS);
		motherBinder.bind(mOccupation, FamilySchema.OCCUPATION);
		motherBinder.bind(mJobAddress, FamilySchema.JOB_ADDRESS);
		motherBinder.bind(mCurrentAddress, FamilySchema.CURRENT_ADDRESS);
		motherBinder.bind(mCurrentProvinceId, FamilySchema.CURRENT_PROVINCE_ID);
		motherBinder.bind(mCurrentDistrict, FamilySchema.CURRENT_DISTRICT_ID);
		motherBinder.bind(mCurrentCity, FamilySchema.CURRENT_CITY_ID);
		motherBinder.bind(mCurrentPostcode, FamilySchema.CURRENT_POSTCODE_ID);
		
		guardianBinder = new FieldGroup();
		guardianBinder.setBuffered(true);
		guardianBinder.bind(gPeopleIdType, FamilySchema.PEOPLE_ID_TYPE);
		guardianBinder.bind(gPeopleid, FamilySchema.PEOPLE_ID);
		guardianBinder.bind(gPrename, FamilySchema.PRENAME);
		guardianBinder.bind(gFirstname, FamilySchema.FIRSTNAME);
		guardianBinder.bind(gLastname, FamilySchema.LASTNAME);
		guardianBinder.bind(gFirstnameNd, FamilySchema.FIRSTNAME_ND);
		guardianBinder.bind(gLastnameNd, FamilySchema.LASTNAME_ND);
		guardianBinder.bind(gGender, FamilySchema.GENDER);
		guardianBinder.bind(gReligion, FamilySchema.RELIGION);
		guardianBinder.bind(gRace, FamilySchema.RACE);
		guardianBinder.bind(gNationality, FamilySchema.NATIONALITY);
		guardianBinder.bind(gBirthDate, FamilySchema.BIRTH_DATE);
		guardianBinder.bind(gTel, FamilySchema.TEL);
		guardianBinder.bind(gMobile, FamilySchema.MOBILE);
		guardianBinder.bind(gEmail, FamilySchema.EMAIL);
		guardianBinder.bind(gSalary, FamilySchema.SALARY);
		guardianBinder.bind(gAliveStatus, FamilySchema.ALIVE_STATUS);
		guardianBinder.bind(gOccupation, FamilySchema.OCCUPATION);
		guardianBinder.bind(gJobAddress, FamilySchema.JOB_ADDRESS);
		guardianBinder.bind(gCurrentAddress, FamilySchema.CURRENT_ADDRESS);
		guardianBinder.bind(gCurrentProvinceId, FamilySchema.CURRENT_PROVINCE_ID);
		guardianBinder.bind(gCurrentDistrict, FamilySchema.CURRENT_DISTRICT_ID);
		guardianBinder.bind(gCurrentCity, FamilySchema.CURRENT_CITY_ID);
		guardianBinder.bind(gCurrentPostcode, FamilySchema.CURRENT_POSTCODE_ID);
	}
			
	/* ปีดการกรอกข้อมูลหากข้อมูล ประชาชนซ้ำหรือยังไม่ได้ตรวจสอบ */
	private void disableDuplicatePeopleIdForm(){
		for(Field<?> field: studentBinder.getFields()){
			if(!studentBinder.getPropertyId(field).equals(StudentSchema.PEOPLE_ID) &&
					!studentBinder.getPropertyId(field).equals(StudentSchema.PEOPLE_ID_TYPE))
				field.setEnabled(false);
		}
		for(Field<?> field: studentStudyBinder.getFields()){
			field.setEnabled(false);
		}
		for(Field<?> field: fatherBinder.getFields()){
			field.setEnabled(false);
		}
		for(Field<?> field: motherBinder.getFields()){
			field.setEnabled(false);
		}
		for(Field<?> field: guardianBinder.getFields()){
			field.setEnabled(false);
		}
		studyNext.setEnabled(false);
		generalBack.setEnabled(false);
		graduatedNext.setEnabled(false);
		studyBack.setEnabled(false);
		addressNext.setEnabled(false);
		graduatedBack.setEnabled(false);
		saveStudent.setEnabled(false);
		addressBack.setEnabled(false);
		motherNext.setEnabled(false);
		fatherBack.setEnabled(false);
		guardianNext.setEnabled(false);
		motherBack.setEnabled(false);
		finish.setEnabled(false);
		print.setEnabled(false);
	}
	
	/* ปีดการกรอกข้อมูลหากข้อมูล ประชาชนซ้ำหรือยังไม่ได้ตรวจสอบ */
	private void disableDuplicateEmailForm(){
		for(Field<?> field: studentStudyBinder.getFields()){
			if(!studentStudyBinder.getPropertyId(field).equals(StudentStudySchema.EMAIL))
				field.setEnabled(false);
		}
		for(Field<?> field: fatherBinder.getFields()){
			field.setEnabled(false);
		}
		for(Field<?> field: motherBinder.getFields()){
			field.setEnabled(false);
		}
		for(Field<?> field: guardianBinder.getFields()){
			field.setEnabled(false);
		}
		saveStudent.setEnabled(false);
		addressBack.setEnabled(false);
		motherNext.setEnabled(false);
		fatherBack.setEnabled(false);
		guardianNext.setEnabled(false);
		motherBack.setEnabled(false);
		finish.setEnabled(false);
		print.setEnabled(false);
	}
	
	/* เปีดการกรอกข้อมูลหากข้อมูล ประชาชนยังไม่ได้ถูกใช้งาน */
	private void enableDuplicatePeopleIdForm(){
		for(Field<?> field: studentBinder.getFields()){
			field.setEnabled(true);
		}
		for(Field<?> field: studentStudyBinder.getFields()){
			field.setEnabled(true);
		}
		for(Field<?> field: fatherBinder.getFields()){
			field.setEnabled(true);
		}
		for(Field<?> field: motherBinder.getFields()){
			field.setEnabled(true);
		}
		for(Field<?> field: guardianBinder.getFields()){
			field.setEnabled(true);
		}
		studyNext.setEnabled(true);
		generalBack.setEnabled(true);
		graduatedNext.setEnabled(true);
		studyBack.setEnabled(true);
		addressNext.setEnabled(true);
		graduatedBack.setEnabled(true);
		saveStudent.setEnabled(true);
		addressBack.setEnabled(true);
		motherNext.setEnabled(true);
		fatherBack.setEnabled(true);
		guardianNext.setEnabled(true);
		motherBack.setEnabled(true);
		finish.setEnabled(true);
		print.setEnabled(true);
	}
	
	/* ปีดการกรอกข้อมูลหากข้อมูล ประชาชนซ้ำหรือยังไม่ได้ตรวจสอบ */
	private void enableDuplicateEmailForm(){
		for(Field<?> field: studentStudyBinder.getFields()){
			field.setEnabled(true);
		}
		for(Field<?> field: fatherBinder.getFields()){
			field.setEnabled(true);
		}
		for(Field<?> field: motherBinder.getFields()){
			field.setEnabled(true);
		}
		for(Field<?> field: guardianBinder.getFields()){
			field.setEnabled(true);
		}
		saveStudent.setEnabled(true);
		addressBack.setEnabled(true);
		motherNext.setEnabled(true);
		fatherBack.setEnabled(true);
		guardianNext.setEnabled(true);
		motherBack.setEnabled(true);
		finish.setEnabled(true);
		print.setEnabled(true);
	}

	/*กรณีทดสอบ ของการเพิ่มข้อมูล*/
	private void testData(){
		classRange.setValue(0);
		peopleIdType.setValue(0);
		peopleId.setValue("1959900163320");
		prename.setValue(0);
		firstname.setValue("sfasf");
		lastname.setValue("asdfdasf");
		firstnameNd.setValue("asdfdasf");
		lastnameNd.setValue("asdfdasf");
		nickname.setValue("asdfdasf");
		gender.setValue(0);
		religion.setValue(0);
		race.setValue(0);
		nationality.setValue(0);
		birthDate.setValue(new Date());
		blood.setValue(0);
		height.setValue("0");
		weight.setValue("0");
		congenitalDisease.setValue("");
		interested.setValue("");
		siblingQty.setValue("0");
		siblingSequence.setValue("0");
		siblingInSchoolQty.setValue("0");
		graduatedSchool.setValue("asdfdasf");
		graduatedSchoolProvinceId.setValue(1);
		graduatedGpa.setValue("2.5");
		graduatedYear.setValue("2554");
		tel.setValue("0897375348");
		mobile.setValue("0897375348");
		email.setValue("axeusonline@gmail.com");
		currentAddress.setValue("aasdfadsf");
		currentProvince.setValue(8);
		currentDistrict.setValue(109);
		currentCity.setValue(860);
		currentPostcode.setValue(119);
		
		classRange.setValue(2);
		autoGenerate.setValue(1);;
		studentCode.setValue("123456");;
		studentStatus.setValue(0);;
		studentComeWith.setValue(0);;
		studentComeDescription.setValue("");;
		studentPayerCourse.setValue(0);;
		studentStayWith.setValue(0);;
		graduatedClassRange.setValue(1);
		
		fPeopleIdType.setValue(0);
		fPeopleid.setValue("1959900163320");
		fPrename.setValue(0);
		fFirstname.setValue("asfadsf");
		fLastname.setValue("asdfdasf");
		fFirstnameNd.setValue("asdfadsf");
		fLastnameNd.setValue("asdfdasf");
		fGender.setValue(0);
		fReligion.setValue(0);
		fRace.setValue(0);
		fNationality.setValue(0);
		fBirthDate.setValue(new Date());
		fTel.setValue("0732174283");
		fMobile.setValue("0897375348");
		fEmail.setValue("asdfdas@asdf.com");
		fSalary.setValue("0");
		fAliveStatus.setValue(0);
		fOccupation.setValue(0);
		fJobAddress.setValue("asfdasf");
		fCurrentAddress.setValue("asfdasf");
		fCurrentProvinceId.setValue(1);
		fCurrentDistrict.setValue(1);
		fCurrentCity.setValue(1);
		fCurrentPostcode.setValue(1);

		mPeopleIdType.setValue(0);
		mPeopleid.setValue("1959900163320");
		mPrename.setValue(0);
		mFirstname.setValue("asfadsf");
		mLastname.setValue("asdfdasf");
		mFirstnameNd.setValue("asdfadsf");
		mLastnameNd.setValue("asdfdasf");
		mGender.setValue(0);
		mReligion.setValue(0);
		mRace.setValue(0);
		mNationality.setValue(0);
		mBirthDate.setValue(new Date());
		mTel.setValue("0732174283");
		mMobile.setValue("0897375348");
		mEmail.setValue("asdfdas@asdf.com");
		mSalary.setValue("0");
		mAliveStatus.setValue(0);
		mOccupation.setValue(0);
		mJobAddress.setValue("asfdasf");
		mCurrentAddress.setValue("asfdasf");
		mCurrentProvinceId.setValue(1);
		mCurrentDistrict.setValue(1);
		mCurrentCity.setValue(1);
		mCurrentPostcode.setValue(1);
		
		gPeopleIdType.setValue(0);
		gPeopleid.setValue("1959900163320");
		gPrename.setValue(0);
		gFirstname.setValue("asfadsf");
		gLastname.setValue("asdfdasf");
		gFirstnameNd.setValue("asdfadsf");
		gLastnameNd.setValue("asdfdasf");
		gGender.setValue(0);
		gReligion.setValue(0);
		gRace.setValue(0);
		gNationality.setValue(0);
		gBirthDate.setValue(new Date());
		gTel.setValue("0732174283");
		gMobile.setValue("0897375348");
		gEmail.setValue("asdfdas@asdf.com");
		gSalary.setValue("0");
		gAliveStatus.setValue(0);
		gOccupation.setValue(0);
		gJobAddress.setValue("asfdasf");
		gCurrentAddress.setValue("asfdasf");
		gCurrentProvinceId.setValue(1);
		gCurrentDistrict.setValue(1);
		gCurrentCity.setValue(1);
		gCurrentPostcode.setValue(1);
		
		gParents.setValue(0);
		familyStatus.setValue(0);
		guardianRelation.setValue(0);
	}

	/* ==================== PUBLIC ==================== */
	
	public void selectGuardianFormTab(){
		setSelectedTab(guardianGroup);
	}
	/* ตั้งค่า Mode ว่าต้องการให้กำหนดข้อมูลเริ่มต้นให้เลยไหม*/
	public void setDebugMode(boolean debugMode){
		if(debugMode)
			testData();
	}
	
	/* ตั้งค่า Event ของผู้ปกครอง */
	public void setGParentsValueChange(ValueChangeListener gParensValueChange){
		gParents.addValueChangeListener(gParensValueChange);
	}
	
	/* ตั้งค่า Event ของปุ่มบันทึก */
	public void setFinishhClick(ClickListener finishClick){
		finish.addClickListener(finishClick);
	}
	
	/*อนุญาติแก้ไขฟอร์ม ผู้ปกครอง
	 * กรณี เลือกผู้ปกครองเป็นอื่น ๆ 
	 * */
	public void enableGuardianBinder(){
		guardianBinder.setEnabled(true);
		guardianBinder.setReadOnly(false);
	}
	
	/*ปิดการแก้ไขฟอร์ม ผู้ปกครอง
	 * กรณี เลือกผู้ปกครองเป็น บิดา มารดา
	 * */
	public void disableGuardianBinder(){
		guardianBinder.setEnabled(false);
		guardianBinder.setReadOnly(true);
	}
	
	/* Reset ค่าภายในฟอร์ม ผู้ปกครอง กรณีเลือก เป็นอื่น ๆ */
	public void resetGuardian(){
		gPeopleIdType.setValue(null);
		gPeopleid.setValue(null);
		gPrename.setValue(null);
		gFirstname.setValue(null);
		gLastname.setValue(null);
		gFirstnameNd.setValue(null);
		gLastnameNd.setValue(null);
		gGender.setValue(null);
		gReligion.setValue(null);
		gRace.setValue(null);
		gNationality.setValue(null);
		gBirthDate.setValue(null);
		gTel.setValue(null);
		gMobile.setValue(null);
		gEmail.setValue(null);
		gSalary.setValue((Double)null);
		gAliveStatus.setValue(null);
		gOccupation.setValue(null);
		gJobAddress.setValue(null);
		gCurrentAddress.setValue(null);
		gCurrentProvinceId.setValue(null);
		gCurrentDistrict.setValue(null);
		gCurrentCity.setValue(null);
		gCurrentPostcode.setValue(null);
		guardianRelation.setValue(null);
	}

	/* ตั้งค่า บุคคลที่เป็นผู้ปกครอง */
	public void setGParentsValue(int value){
		gParents.setValue(value);
	}
	
	/* พิมพ์เอกสารการสมัคร*/
	public void visiblePrintButton(){
		print.setVisible(true);
	}
	
	/* ความสัมพันธ์ของผู้ปกครอง เช่น พ่อ/แม่ พี่ ป้า น้า อา */
	public void setGuardianRelationValue(int value){
		guardianRelation.setValue(value);
	}
	
	/* ตรวจสอบข้อมูลครบถ้วน */
	public boolean validateForms(){
		/* ตรวจสอบว่าต้องการใส่ข้อมูลบิดา มาร หรือไม่*/
		if(isInsertParents){
			/* ตรวจสอบว่าข้อมูลบิดา มารดา ผู้ปกครอง ครบถ้วนหรือไม่*/
			if(fatherBinder.isValid() && motherBinder.isValid() && guardianBinder.isValid())
				return true;
		}else{
			
			/* ตรวจสอบว่าข้อมูลนักเรียน ครบถ้วนหรือไม่*/
			if(studentBinder.isValid() && studentStudyBinder.isValid()){
				return true;
			}
		}

		return false;
	}
	
	private String getStudentCode(String classRange){
		studentCode.setEnabled(true);
		/* รหัสเริ่มต้น 5801*/
		String studentCodeStr = DateTimeUtil.getBuddishYear().substring(2) + classRange;
		
		/* ดึง รหัสที่มาทที่สุด SELECT MAX(student_code) FROM student WHERE student_code LIKE 'ตำแหน่ง%' */
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(" SELECT MAX(" + StudentStudySchema.STUDENT_CODE + ") AS " + StudentStudySchema.STUDENT_CODE);
		sqlBuilder.append(" FROM " + StudentStudySchema.TABLE_NAME);
		sqlBuilder.append(" WHERE " + StudentStudySchema.STUDENT_CODE + " LIKE '" + studentCodeStr + "%'");
		sqlBuilder.append(" AND " + StudentStudySchema.SCHOOL_ID + "=" + SessionSchema.getSchoolID());

		studentCodeStr += "001";
		
		SQLContainer freeContainer = container.getFreeFormContainer(sqlBuilder.toString(), StudentStudySchema.STUDENT_CODE);
		Item item = freeContainer.getItem(freeContainer.getIdByIndex(0));
		
		if(item.getItemProperty(StudentStudySchema.STUDENT_CODE).getValue() != null){
			studentCodeStr = (Integer.parseInt(item.getItemProperty(StudentStudySchema.STUDENT_CODE).getValue().toString()) + 1) + "";	
		}
		
		return studentCodeStr;
	}
	
	private String getManaulStudentCode(){
		String maxCode = "";
		int max = 0;
		StringBuilder builder = new StringBuilder();
		builder.append(" SELECT MAX("+StudentStudySchema.STUDENT_CODE +") AS " + StudentStudySchema.STUDENT_CODE + " FROM " + StudentStudySchema.TABLE_NAME);
		builder.append(" WHERE " + StudentStudySchema.SCHOOL_ID + "="+ SessionSchema.getSchoolID());

		SQLContainer freeContainer = container.getFreeFormContainer(builder.toString(), StudentStudySchema.STUDENT_CODE);
		if(freeContainer.getItem(freeContainer.getIdByIndex(0)).getItemProperty(StudentStudySchema.STUDENT_CODE).getValue() != null){
			max = Integer.parseInt(freeContainer.getItem(freeContainer.getIdByIndex(0)).getItemProperty(StudentStudySchema.STUDENT_CODE).getValue().toString());
			max++;
			maxCode = Integer.toString(max);
		}else{
		   Item schoolItem = schoolContainer.getItem(new RowId(SessionSchema.getSchoolID()));
		   maxCode = schoolItem.getItemProperty(SchoolSchema.STUDENT_CODE_FIRST).getValue().toString();
		}

		return maxCode;
	}
	
	public void setStudentMode(){
		peopleIdType.setRequired(true);
		peopleId.setRequired(true);
        prename.setRequired(true);
		firstname.setRequired(true);
		lastname.setRequired(true);
		gender.setRequired(true);
		religion.setRequired(true);
		race.setRequired(true);
		nationality.setRequired(true);
		birthDate.setRequired(true);
		blood.setRequired(true);
		siblingQty.setRequired(true);
		siblingSequence.setRequired(true);
		siblingInSchoolQty.setRequired(true);

		if(generatedType.equals("0")){
			classRange.setRequired(true);
			autoGenerate.setRequired(true);
		}
		
		studentCode.setRequired(true);
		studentStatus.setRequired(true);
		studentComeWith.setRequired(true);
		graduatedSchool.setRequired(true);
		graduatedSchoolProvinceId.setRequired(true);
		graduatedGpa.setRequired(true);
		graduatedYear.setRequired(true);
		graduatedClassRange.setRequired(true);
		email.setRequired(true);
		currentAddress.setRequired(true);
		currentProvince.setRequired(true);
		currentDistrict.setRequired(true);
		currentCity.setRequired(true);
		currentPostcode.setRequired(true);
	
		studentCode.setReadOnly(true);
		studentStatus.setReadOnly(true);
	}
	
	public void setStudentTempMode(){
		peopleIdType.setRequired(true);
		peopleId.setRequired(true);
        prename.setRequired(true);
		firstname.setRequired(true);
		lastname.setRequired(true);
		gender.setRequired(true);
		if(generatedType.equals("0"))
			autoGenerate.setRequired(true);
		studentCode.setRequired(true);
		studentStatus.setRequired(true);	
	}
	
	public String getActualStudentCode(){
		String studentCodeStr = "";
		if(generatedType.equals("0")){
			studentCodeStr = getStudentCode(classRange.getValue().toString());
		}else if(generatedType.equals("1")){
			studentCodeStr = getManaulStudentCode();
		}
		studentCode.setReadOnly(false);
		studentCode.setValue(studentCodeStr);
		studentCode.setReadOnly(true);
		return studentCodeStr;
	}
}
