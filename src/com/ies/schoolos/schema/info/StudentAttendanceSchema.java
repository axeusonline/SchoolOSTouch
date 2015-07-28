package com.ies.schoolos.schema.info;

public class StudentAttendanceSchema implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final String TABLE_NAME = "student_attendance";
	
	public static final String STUDENT_ATTENDANCE_ID = "student_attendance_id";
	public static final String SCHOOL_ID = "school_id";
	public static final String TIMETABLE_ID = "timetable_id";
	public static final String STUDENT_STUDY_ID = "student_study_id";
	public static final String CHECK_DATE = "check_date";
	public static final String ATTENDANCE_STATUS = "attendance_status";
}
