package com.ies.schoolos.ui.mobile.info;

import com.ies.schoolos.container.Container;
import com.ies.schoolos.schema.info.PersonnelGraduatedHistorySchema;
import com.ies.schoolos.type.GraduatedLevel;
import com.ies.schoolos.type.dynamic.Province;
import com.ies.schoolos.ui.mobile.component.NumberField;
import com.ies.schoolos.utility.Notification;
import com.vaadin.data.Item;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

public class EditPersonnelGraduatedHistoryView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private Container container = new Container();
	private SQLContainer pgContainer = container.getPersonnelGraduatedHistoryContainer();
	
	private Item item;

	private FieldGroup graduatedHistoryBinder;
	private VerticalComponentGroup graduatedGroup;
	private TextField institute;
	private NativeSelect graduatedLevel;
	private TextField degree;
	private TextField major;
	private TextField minor;
	private NumberField year;
	private TextField location;
	private NativeSelect provinceId;
	private TextArea description;
	private Button save;	
	
	public EditPersonnelGraduatedHistoryView(Object id) {		
		item = pgContainer.getItem(new RowId(id));
		
		setSizeFull();
		setSpacing(true);
		setMargin(true);
		buildMainLayout();
	}
	
	private void buildMainLayout(){
		//Form		
		graduatedGroup = new VerticalComponentGroup();
		addComponent(graduatedGroup);
		
		institute = new TextField("สถาบัน");
		institute.setInputPrompt("สถาบัน");
		institute.setNullRepresentation("");
		institute.setImmediate(false);
		institute.setRequired(true);
		institute.setWidth("-1px");
		institute.setHeight("-1px");
		graduatedGroup.addComponent(institute);
		
		graduatedLevel = new NativeSelect("ระดับการศึกษา",new GraduatedLevel());
		graduatedLevel.setItemCaptionPropertyId("name");
		graduatedLevel.setImmediate(true);
		graduatedLevel.setNullSelectionAllowed(false);
		graduatedLevel.setRequired(true);
		graduatedLevel.setWidth("-1px");
		graduatedLevel.setHeight("-1px");
		graduatedGroup.addComponent(graduatedLevel);
		
		degree = new TextField("วุฒิการศึกษา");
		degree.setInputPrompt("วุฒิการศึกษา");
		degree.setNullRepresentation("");
		degree.setImmediate(false);
		degree.setWidth("-1px");
		degree.setHeight("-1px");
		graduatedGroup.addComponent(degree);
		
		major = new TextField("วิชาเอก");
		major.setInputPrompt("วิชาเอก");
		major.setNullRepresentation("");
		major.setImmediate(false);
		major.setWidth("-1px");
		major.setHeight("-1px");
		graduatedGroup.addComponent(major);
		
		minor = new TextField("วิชาโท");
		minor.setInputPrompt("วิชาโท");
		minor.setNullRepresentation("");
		minor.setImmediate(false);
		minor.setWidth("-1px");
		minor.setHeight("-1px");
		graduatedGroup.addComponent(minor);
		
		year = new NumberField("ปีที่จบ");
		year.setInputPrompt("ปีที่จบ");
		year.setNullRepresentation("");
		year.setImmediate(false);
		year.setWidth("-1px");
		year.setHeight("-1px");
		graduatedGroup.addComponent(year);
		
		location = new TextField("ประเทศ");
		location.setInputPrompt("ประเทศ");
		location.setNullRepresentation("");
		location.setImmediate(false);
		location.setWidth("-1px");
		location.setHeight("-1px");
		graduatedGroup.addComponent(location);
		
		provinceId = new NativeSelect("จังหวัดสถาบัน",new Province());
		provinceId.setItemCaptionPropertyId("name");
		provinceId.setImmediate(true);
		provinceId.setNullSelectionAllowed(false);
		provinceId.setWidth("-1px");
		provinceId.setHeight("-1px");
		graduatedGroup.addComponent(provinceId);
		
		description = new TextArea("รายละเอียด");
		description.setInputPrompt("รายละเอียดเพิ่มเติม");
		description.setImmediate(false);
		description.setWidth("-1px");
		description.setHeight("-1px");
		description.setNullRepresentation("");
		graduatedGroup.addComponent(description);
		
		save = new Button("แก้ไข", FontAwesome.SAVE);
		save.setSizeFull();
		save.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					graduatedHistoryBinder.commit();
					pgContainer.commit();

					Notification.show("บันทึกสำเร็จ", Type.HUMANIZED_MESSAGE);
					
					initFieldGroup();

					Notification.show("บันทึกสำเร็จ", Type.HUMANIZED_MESSAGE);
				} catch (Exception e) {
					e.printStackTrace();
					Notification.show("บันทึกไม่สำเร็จ", Type.WARNING_MESSAGE);
				}
			}
		});
		graduatedGroup.addComponent(save);
		
		initFieldGroup();
	}
	
	/* จัดกลุ่มของ ฟอร์มในการแก้ไข - เพิ่ม ข้อมูล */
	private void initFieldGroup(){		
		graduatedHistoryBinder = new FieldGroup(item);
		graduatedHistoryBinder.setBuffered(true);
		graduatedHistoryBinder.bind(institute, PersonnelGraduatedHistorySchema.INSTITUTE);
		graduatedHistoryBinder.bind(graduatedLevel, PersonnelGraduatedHistorySchema.GRADUATED_LEVEL);
		graduatedHistoryBinder.bind(degree, PersonnelGraduatedHistorySchema.DEGREE);
		graduatedHistoryBinder.bind(major, PersonnelGraduatedHistorySchema.MAJOR);
		graduatedHistoryBinder.bind(minor, PersonnelGraduatedHistorySchema.MINOR);
		graduatedHistoryBinder.bind(year, PersonnelGraduatedHistorySchema.YEAR);
		graduatedHistoryBinder.bind(location, PersonnelGraduatedHistorySchema.LOCATION);
		graduatedHistoryBinder.bind(provinceId, PersonnelGraduatedHistorySchema.PROVINCE_ID);
		graduatedHistoryBinder.bind(description, PersonnelGraduatedHistorySchema.DESCRIPTION);
	}
}
