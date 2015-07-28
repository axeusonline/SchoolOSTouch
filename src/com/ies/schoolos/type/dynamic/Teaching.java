package com.ies.schoolos.type.dynamic;

import com.ies.schoolos.container.Container;
import com.ies.schoolos.schema.SessionSchema;
import com.ies.schoolos.schema.academic.LessonPlanSubjectSchema;
import com.ies.schoolos.schema.academic.TeachingSchema;
import com.ies.schoolos.schema.fundamental.SubjectSchema;
import com.ies.schoolos.schema.info.PersonnelSchema;
import com.ies.schoolos.utility.DateTimeUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.sqlcontainer.SQLContainer;

public class Teaching extends IndexedContainer{

	private Container container = new Container();
	private static final long serialVersionUID = 1L;
	
	private SQLContainer freeContainer;

	public Teaching() {
		addContainerProperty("name", String.class,null);
		initfreeContainer();
	}
	
	public Teaching(Object weekend){
		addContainerProperty("name", String.class,null);
		initfreeContainer(weekend);
	}
	
	public Teaching(int semester){
		addContainerProperty("name", String.class,null);
		initfreeContainer(semester);
	}
	
	public Teaching(Object classYear, Object semester){
		addContainerProperty("name", String.class,null);
		initfreeContainer(classYear,semester);
	}
	
	public Teaching(Object classYear, Object semester, Object weekend){
		addContainerProperty("name", String.class,null);
		initfreeContainer(classYear,semester,weekend);
	}
 
	@SuppressWarnings("unchecked")
	private void initfreeContainer(){
		StringBuilder teachingBuilder = new StringBuilder();
		teachingBuilder.append(" SELECT * FROM "+ TeachingSchema.TABLE_NAME + " tc");
		teachingBuilder.append(" INNER JOIN "+ SubjectSchema.TABLE_NAME + " s ON s." + SubjectSchema.SUBJECT_ID + " = tc." + TeachingSchema.SUBJECT_ID);
		teachingBuilder.append(" WHERE tc."+ TeachingSchema.SCHOOL_ID + "=" + SessionSchema.getSchoolID());
		teachingBuilder.append(" AND tc." + TeachingSchema.ACADEMIC_YEAR + "='" + DateTimeUtil.getBuddishYear()+"'");

		freeContainer = container.getFreeFormContainer(teachingBuilder.toString(), TeachingSchema.TEACHING_ID);
		for (int i = 0; i < freeContainer.size(); i++) {
			Object teachingItemId = freeContainer.getIdByIndex(i);
			
			Item teachingItem = freeContainer.getItem(teachingItemId);
			String firstname = "";
			String lastname = "";
			/* ตรวจสอบว่า เป็นอาจารย์พิเศษไหม ถ้าใช่ แสดงว่า personnel_id = null จึงต้องดึงจากชื่อ Tmp มาแสดงแทน */
			if(teachingItem.getItemProperty(TeachingSchema.PERSONNEL_ID).getValue() == null){
				String[] nameTmp = teachingItem.getItemProperty(TeachingSchema.PERSONNEL_NAME_TMP).getValue().toString().split(" ");
				firstname = nameTmp[0];
				lastname = nameTmp[1];
			}else{
				try {
					StringBuilder builder = new StringBuilder();
					builder.append(" SELECT " + PersonnelSchema.PERSONNEL_ID + "," + PersonnelSchema.PERSONNEL_CODE + "," + PersonnelSchema.FIRSTNAME + "," + PersonnelSchema.LASTNAME);
					builder.append(" FROM " + PersonnelSchema.TABLE_NAME);
					builder.append(" WHERE " + PersonnelSchema.PERSONNEL_ID + "=" + teachingItem.getItemProperty(TeachingSchema.PERSONNEL_ID).getValue());
					
					SQLContainer freeContainer = container.getFreeFormContainer(builder.toString(), PersonnelSchema.PERSONNEL_ID);

					Item personnelItem = freeContainer.getItem(freeContainer.getIdByIndex(0));
					firstname = personnelItem.getItemProperty(PersonnelSchema.FIRSTNAME).getValue().toString();
					lastname = personnelItem.getItemProperty(PersonnelSchema.LASTNAME).getValue().toString();
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
			
			String subject = "";
			if(teachingItem.getItemProperty(SubjectSchema.CODE).getValue() != null)
				subject = teachingItem.getItemProperty(SubjectSchema.CODE).getValue().toString() + ":";
			subject += teachingItem.getItemProperty(SubjectSchema.NAME).getValue().toString();
			
			Item item = addItem(teachingItemId);
	        item.getItemProperty("name").setValue(subject + "(อ."+firstname + " " + lastname + ")");
		}
		
		/* ลบ WHERE ออกจาก Query เพื่อป้องกันการค้างของคำสั่่งจากการทำงานอื่นที่เรียกตัวแปรไปใช้ */
		freeContainer.removeAllContainerFilters();
	}
	
	@SuppressWarnings("unchecked")
	private void initfreeContainer(Object weekend){
		StringBuilder teachingBuilder = new StringBuilder();
		teachingBuilder.append(" SELECT * FROM "+ TeachingSchema.TABLE_NAME + " tc");
		teachingBuilder.append(" INNER JOIN "+ SubjectSchema.TABLE_NAME + " s ON s." + SubjectSchema.SUBJECT_ID + " = tc." + TeachingSchema.SUBJECT_ID);
		teachingBuilder.append(" WHERE tc."+ TeachingSchema.SCHOOL_ID + "='" + SessionSchema.getSchoolID() + "'");
		if(weekend != null)
			teachingBuilder.append(" AND tc."+ TeachingSchema.WEEKEND + "='" + weekend.toString() + "'");
		else
			teachingBuilder.append(" AND tc."+ TeachingSchema.WEEKEND + " IS NULL");
		teachingBuilder.append(" AND tc." + TeachingSchema.ACADEMIC_YEAR + "='" + DateTimeUtil.getBuddishYear()+"'");

		freeContainer = container.getFreeFormContainer(teachingBuilder.toString(), TeachingSchema.TEACHING_ID);
		for (int i = 0; i < freeContainer.size(); i++) {
			Object teachingItemId = freeContainer.getIdByIndex(i);
			
			Item teachingItem = freeContainer.getItem(teachingItemId);
			String firstname = "";
			String lastname = "";
			/* ตรวจสอบว่า เป็นอาจารย์พิเศษไหม ถ้าใช่ แสดงว่า personnel_id = null จึงต้องดึงจากชื่อ Tmp มาแสดงแทน */
			if(teachingItem.getItemProperty(TeachingSchema.PERSONNEL_ID).getValue() == null){
				String[] nameTmp = teachingItem.getItemProperty(TeachingSchema.PERSONNEL_NAME_TMP).getValue().toString().split(" ");
				firstname = nameTmp[0];
				lastname = nameTmp[1];
			}else{
				try {
					StringBuilder builder = new StringBuilder();
					builder.append(" SELECT " + PersonnelSchema.PERSONNEL_ID + "," + PersonnelSchema.PERSONNEL_CODE + "," + PersonnelSchema.FIRSTNAME + "," + PersonnelSchema.LASTNAME);
					builder.append(" FROM " + PersonnelSchema.TABLE_NAME);
					builder.append(" WHERE " + PersonnelSchema.PERSONNEL_ID + "=" + teachingItem.getItemProperty(TeachingSchema.PERSONNEL_ID).getValue());
					
					SQLContainer freeContainer = container.getFreeFormContainer(builder.toString(), PersonnelSchema.PERSONNEL_ID);
					
					Item personnelItem = freeContainer.getItem(freeContainer.getIdByIndex(0));
					firstname = personnelItem.getItemProperty(PersonnelSchema.FIRSTNAME).getValue().toString();
					lastname = personnelItem.getItemProperty(PersonnelSchema.LASTNAME).getValue().toString();
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
			
			String subject = "";
			if(teachingItem.getItemProperty(SubjectSchema.CODE).getValue() != null)
				subject = teachingItem.getItemProperty(SubjectSchema.CODE).getValue().toString() + ":";
			subject += teachingItem.getItemProperty(SubjectSchema.NAME).getValue().toString();
			
			Item item = addItem(teachingItemId);
	        item.getItemProperty("name").setValue(subject + "(อ."+firstname + " " + lastname + ")");
		}
		
		/* ลบ WHERE ออกจาก Query เพื่อป้องกันการค้างของคำสั่่งจากการทำงานอื่นที่เรียกตัวแปรไปใช้ */
		freeContainer.removeAllContainerFilters();
	}
	
	@SuppressWarnings("unchecked")
	private void initfreeContainer(int semester){
		/*SELECT * FROM teaching tc 
		INNER JOIN subject s ON s.subject_id = tc.subject_id 
		WHERE tc.school_id=9 
		AND tc.academic_year=2558 
		AND s.subject_id IN 
		(SELECT subject_id FROM lesson_plan_subject 
		WHERE class_year=9 
		AND semester=0)*/

		StringBuilder teachingBuilder = new StringBuilder();
		teachingBuilder.append(" SELECT * FROM "+ TeachingSchema.TABLE_NAME + " tc");
		teachingBuilder.append(" INNER JOIN "+ SubjectSchema.TABLE_NAME + " s ON s." + SubjectSchema.SUBJECT_ID + " = tc." + TeachingSchema.SUBJECT_ID);
		teachingBuilder.append(" WHERE tc."+ TeachingSchema.SCHOOL_ID + "=" + SessionSchema.getSchoolID());
		teachingBuilder.append(" AND tc." + TeachingSchema.ACADEMIC_YEAR + "='" + DateTimeUtil.getBuddishYear()+"'");
		teachingBuilder.append(" AND s." + SubjectSchema.SUBJECT_ID + " IN");
		teachingBuilder.append(" (SELECT " + LessonPlanSubjectSchema.SUBJECT_ID + " FROM " + LessonPlanSubjectSchema.TABLE_NAME);
		teachingBuilder.append(" WHERE " + LessonPlanSubjectSchema.SEMESTER + "=" + semester +")");
		
		freeContainer = container.getFreeFormContainer(teachingBuilder.toString(), TeachingSchema.TEACHING_ID);
		for (int i = 0; i < freeContainer.size(); i++) {
			Object teachingItemId = freeContainer.getIdByIndex(i);
			
			Item teachingItem = freeContainer.getItem(teachingItemId);
			String firstname = "";
			String lastname = "";
			/* ตรวจสอบว่า เป็นอาจารย์พิเศษไหม ถ้าใช่ แสดงว่า personnel_id = null จึงต้องดึงจากชื่อ Tmp มาแสดงแทน */
			if(teachingItem.getItemProperty(TeachingSchema.PERSONNEL_ID).getValue() == null){
				String[] nameTmp = teachingItem.getItemProperty(TeachingSchema.PERSONNEL_NAME_TMP).getValue().toString().split(" ");
				firstname = nameTmp[0];
				lastname = nameTmp[1];
			}else{
				try {
					StringBuilder builder = new StringBuilder();
					builder.append(" SELECT " + PersonnelSchema.PERSONNEL_ID + "," + PersonnelSchema.PERSONNEL_CODE + "," + PersonnelSchema.FIRSTNAME + "," + PersonnelSchema.LASTNAME);
					builder.append(" FROM " + PersonnelSchema.TABLE_NAME);
					builder.append(" WHERE " + PersonnelSchema.PERSONNEL_ID + "=" + teachingItem.getItemProperty(TeachingSchema.PERSONNEL_ID).getValue());

					SQLContainer freeContainer = container.getFreeFormContainer(builder.toString(), PersonnelSchema.PERSONNEL_ID);

					Item personnelItem = freeContainer.getItem(freeContainer.getIdByIndex(0));
					firstname = personnelItem.getItemProperty(PersonnelSchema.FIRSTNAME).getValue().toString();
					lastname = personnelItem.getItemProperty(PersonnelSchema.LASTNAME).getValue().toString();
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
			
			String subject = "";
			if(teachingItem.getItemProperty(SubjectSchema.CODE).getValue() != null)
				subject = teachingItem.getItemProperty(SubjectSchema.CODE).getValue().toString() + ":";
			subject += teachingItem.getItemProperty(SubjectSchema.NAME).getValue().toString();
			
			Item item = addItem(teachingItemId);
	        item.getItemProperty("name").setValue(subject + "(อ."+firstname + " " + lastname + ")");
		}
		
		/* ลบ WHERE ออกจาก Query เพื่อป้องกันการค้างของคำสั่่งจากการทำงานอื่นที่เรียกตัวแปรไปใช้ */
		freeContainer.removeAllContainerFilters();
	}
	
	@SuppressWarnings("unchecked")
	private void initfreeContainer(Object classYear, Object semester){
		/*SELECT * FROM teaching tc 
		INNER JOIN subject s ON s.subject_id = tc.subject_id 
		WHERE tc.school_id=9 
		AND tc.academic_year=2558 
		AND s.subject_id IN 
		(SELECT subject_id FROM lesson_plan_subject 
		WHERE class_year=9 
		AND semester=0)*/

		StringBuilder teachingBuilder = new StringBuilder();
		teachingBuilder.append(" SELECT * FROM "+ TeachingSchema.TABLE_NAME + " tc");
		teachingBuilder.append(" INNER JOIN "+ SubjectSchema.TABLE_NAME + " s ON s." + SubjectSchema.SUBJECT_ID + " = tc." + TeachingSchema.SUBJECT_ID);
		teachingBuilder.append(" WHERE tc."+ TeachingSchema.SCHOOL_ID + "=" + SessionSchema.getSchoolID());
		teachingBuilder.append(" AND tc." + TeachingSchema.ACADEMIC_YEAR + "='" + DateTimeUtil.getBuddishYear()+"'");
		teachingBuilder.append(" AND s." + SubjectSchema.SUBJECT_ID + " IN");
		teachingBuilder.append(" (SELECT " + LessonPlanSubjectSchema.SUBJECT_ID + " FROM " + LessonPlanSubjectSchema.TABLE_NAME);
		teachingBuilder.append(" WHERE " + LessonPlanSubjectSchema.CLASS_YEAR + "=" + classYear);
		teachingBuilder.append(" AND " + LessonPlanSubjectSchema.SEMESTER + "=" + semester +")");
		
		freeContainer = container.getFreeFormContainer(teachingBuilder.toString(), TeachingSchema.TEACHING_ID);
		for (int i = 0; i < freeContainer.size(); i++) {
			Object teachingItemId = freeContainer.getIdByIndex(i);
			
			Item teachingItem = freeContainer.getItem(teachingItemId);
			String firstname = "";
			String lastname = "";
			/* ตรวจสอบว่า เป็นอาจารย์พิเศษไหม ถ้าใช่ แสดงว่า personnel_id = null จึงต้องดึงจากชื่อ Tmp มาแสดงแทน */
			if(teachingItem.getItemProperty(TeachingSchema.PERSONNEL_ID).getValue() == null){
				String[] nameTmp = teachingItem.getItemProperty(TeachingSchema.PERSONNEL_NAME_TMP).getValue().toString().split(" ");
				firstname = nameTmp[0];
				lastname = nameTmp[1];
			}else{
				try {
					StringBuilder builder = new StringBuilder();
					builder.append(" SELECT " + PersonnelSchema.PERSONNEL_ID + "," + PersonnelSchema.PERSONNEL_CODE + "," + PersonnelSchema.FIRSTNAME + "," + PersonnelSchema.LASTNAME);
					builder.append(" FROM " + PersonnelSchema.TABLE_NAME);
					builder.append(" WHERE " + PersonnelSchema.PERSONNEL_ID + "=" + teachingItem.getItemProperty(TeachingSchema.PERSONNEL_ID).getValue());

					SQLContainer freeContainer = container.getFreeFormContainer(builder.toString(), PersonnelSchema.PERSONNEL_ID);

					Item personnelItem = freeContainer.getItem(freeContainer.getIdByIndex(0));
					firstname = personnelItem.getItemProperty(PersonnelSchema.FIRSTNAME).getValue().toString();
					lastname = personnelItem.getItemProperty(PersonnelSchema.LASTNAME).getValue().toString();
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
			
			String subject = "";
			if(teachingItem.getItemProperty(SubjectSchema.CODE).getValue() != null)
				subject = teachingItem.getItemProperty(SubjectSchema.CODE).getValue().toString() + ":";
			subject += teachingItem.getItemProperty(SubjectSchema.NAME).getValue().toString();
			
			Item item = addItem(teachingItemId);
	        item.getItemProperty("name").setValue(subject + "(อ."+firstname + " " + lastname + ")");
		}
		
		/* ลบ WHERE ออกจาก Query เพื่อป้องกันการค้างของคำสั่่งจากการทำงานอื่นที่เรียกตัวแปรไปใช้ */
		freeContainer.removeAllContainerFilters();
	}
	
	@SuppressWarnings("unchecked")
	private void initfreeContainer(Object classYear, Object semester,Object weekend){
		
		/*SELECT * FROM teaching tc 
		INNER JOIN subject s ON s.subject_id = tc.subject_id 
		WHERE tc.school_id=9 
		AND tc.academic_year=2558 
		AND tc.weekend=[ 5, 6 ] 
		AND s.subject_id IN 
		(SELECT subject_id FROM lesson_plan_subject 
		WHERE class_year=9 
		AND semester=0)*/

		StringBuilder teachingBuilder = new StringBuilder();
		teachingBuilder.append(" SELECT * FROM "+ TeachingSchema.TABLE_NAME + " tc");
		teachingBuilder.append(" INNER JOIN "+ SubjectSchema.TABLE_NAME + " s ON s." + SubjectSchema.SUBJECT_ID + " = tc." + TeachingSchema.SUBJECT_ID);
		teachingBuilder.append(" WHERE tc."+ TeachingSchema.SCHOOL_ID + "=" + SessionSchema.getSchoolID());
		teachingBuilder.append(" AND tc." + TeachingSchema.ACADEMIC_YEAR + "='" + DateTimeUtil.getBuddishYear()+"'");
		teachingBuilder.append(" AND tc." + TeachingSchema.WEEKEND + "='" + weekend.toString() +"'");
		teachingBuilder.append(" AND s." + SubjectSchema.SUBJECT_ID + " IN");
		teachingBuilder.append(" (SELECT " + LessonPlanSubjectSchema.SUBJECT_ID + " FROM " + LessonPlanSubjectSchema.TABLE_NAME);
		teachingBuilder.append(" WHERE " + LessonPlanSubjectSchema.CLASS_YEAR + "=" + classYear);
		teachingBuilder.append(" AND " + LessonPlanSubjectSchema.SEMESTER + "=" + semester +")");

		freeContainer = container.getFreeFormContainer(teachingBuilder.toString(), TeachingSchema.TEACHING_ID);
		
		if(freeContainer.size() > 0){
			for (int i = 0; i < freeContainer.size(); i++) {
				Object teachingItemId = freeContainer.getIdByIndex(i);
				
				Item teachingItem = freeContainer.getItem(teachingItemId);
				String firstname = "";
				String lastname = "";
				/* ตรวจสอบว่า เป็นอาจารย์พิเศษไหม ถ้าใช่ แสดงว่า personnel_id = null จึงต้องดึงจากชื่อ Tmp มาแสดงแทน */
				if(teachingItem.getItemProperty(TeachingSchema.PERSONNEL_ID).getValue() == null){
					String[] nameTmp = teachingItem.getItemProperty(TeachingSchema.PERSONNEL_NAME_TMP).getValue().toString().split(" ");
					firstname = nameTmp[0];
					lastname = nameTmp[1];
				}else{
					try {
						StringBuilder builder = new StringBuilder();
						builder.append(" SELECT " + PersonnelSchema.PERSONNEL_ID + "," + PersonnelSchema.PERSONNEL_CODE + "," + PersonnelSchema.FIRSTNAME + "," + PersonnelSchema.LASTNAME);
						builder.append(" FROM " + PersonnelSchema.TABLE_NAME);
						builder.append(" WHERE " + PersonnelSchema.PERSONNEL_ID + "=" + teachingItem.getItemProperty(TeachingSchema.PERSONNEL_ID).getValue());

						SQLContainer freeContainer = container.getFreeFormContainer(builder.toString(), PersonnelSchema.PERSONNEL_ID);

						Item personnelItem = freeContainer.getItem(freeContainer.getIdByIndex(0));
						firstname = personnelItem.getItemProperty(PersonnelSchema.FIRSTNAME).getValue().toString();
						lastname = personnelItem.getItemProperty(PersonnelSchema.LASTNAME).getValue().toString();
					} catch (Exception e) {
						e.printStackTrace();
					}		
				}
				
				String subject = "";
				if(teachingItem.getItemProperty(SubjectSchema.CODE).getValue() != null)
					subject = teachingItem.getItemProperty(SubjectSchema.CODE).getValue().toString() + ":";
				subject += teachingItem.getItemProperty(SubjectSchema.NAME).getValue().toString();
				
				Item item = addItem(teachingItemId);
		        item.getItemProperty("name").setValue(subject + "(อ."+firstname + " " + lastname + ")");
			}
		}else{
			initfreeContainer(classYear, semester);
		}
		
		
		/* ลบ WHERE ออกจาก Query เพื่อป้องกันการค้างของคำสั่่งจากการทำงานอื่นที่เรียกตัวแปรไปใช้ */
		freeContainer.removeAllContainerFilters();
	}
}
