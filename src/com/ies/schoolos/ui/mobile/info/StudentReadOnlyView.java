package com.ies.schoolos.ui.mobile.info;

import com.ies.schoolos.container.Container;
import com.ies.schoolos.schema.info.StudentSchema;
import com.ies.schoolos.schema.info.StudentStudySchema;
import com.ies.schoolos.ui.mobile.component.ManagerView;
import com.ies.schoolos.ui.mobile.info.layout.StudentLayout;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.Component;

public class StudentReadOnlyView extends ManagerView {
	private static final long serialVersionUID = 1L;

	private Object studyId;
	public Object pkStore[] = new Object[5];
	
	private Container container = new Container();
	public SQLContainer sSqlContainer = container.getStudentContainer();
	public SQLContainer ssSqlContainer = container.getStudentStudyContainer();
	public SQLContainer fSqlContainer = container.getFamilyContainer();
	public SQLContainer userfSqlContainer = container.getUserContainer();
	
	private StudentLayout studentLayout;
	
	public FieldGroup studentBinder;
	public FieldGroup studentStudyBinder;
	public FieldGroup fatherBinder;
	public FieldGroup motherBinder;
	public FieldGroup guardianBinder;
		
	public StudentReadOnlyView(Object studyId,Component component) {
		this.studyId = studyId;

		studentLayout = new StudentLayout(true);
		navView.setContent(studentLayout);
		navView.setToolbar(null);
		setPreviousComponent(component);
		
		studentBinder = studentLayout.studentBinder;
		studentStudyBinder = studentLayout.studentStudyBinder;
		fatherBinder = studentLayout.fatherBinder;
		motherBinder = studentLayout.motherBinder;
		guardianBinder = studentLayout.guardianBinder;
		
		initEditData();
	}
	
	/* นำข้อมูลจาก studyId มาทำการกรอกในฟอร์มทั้งหมด */
	private void initEditData(){
		Item studyItem = ssSqlContainer.getItem(studyId);
		Item studentItem = sSqlContainer.getItem(new RowId(studyItem.getItemProperty(StudentStudySchema.STUDENT_ID).getValue()));

		Object fatherId = studentItem.getItemProperty(StudentSchema.FATHER_ID).getValue();
		Object motherId = studentItem.getItemProperty(StudentSchema.MOTHER_ID).getValue();
		Object guardianId = studyItem.getItemProperty(StudentStudySchema.GUARDIAN_ID).getValue();
		
		Item fatherItem = null;
		Item motherItem = null;
		Item guardianItem = null;
		if(fatherId != null){
			fatherItem = fSqlContainer.getItem(new RowId(fatherId));
			pkStore[0] = fatherId;
		}
		if(motherId != null){
			motherItem = fSqlContainer.getItem(new RowId(motherId));
			pkStore[1] = motherId;
		}
		if(guardianId != null){
			guardianItem = fSqlContainer.getItem(new RowId(guardianId));
			pkStore[2] = guardianId;
		}

		fatherBinder.setItemDataSource(fatherItem);
		motherBinder.setItemDataSource(motherItem);
		guardianBinder.setItemDataSource(guardianItem);
		studentBinder.setItemDataSource(studentItem);
		studentStudyBinder.setItemDataSource(studyItem);
		
		fatherBinder.setReadOnly(true);
		motherBinder.setReadOnly(true);
		guardianBinder.setReadOnly(true);
		studentBinder.setReadOnly(true);
		studentStudyBinder.setReadOnly(true);
	}
}
