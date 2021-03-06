package com.ies.schoolos.schema.info;

import java.util.HashMap;

public class PersonnelSchema implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final String TABLE_NAME = "personnel";
	
	public static final String PERSONNEL_ID = "personnel_id";
	public static final String SCHOOL_ID = "school_id";
	public static final String PEOPLE_ID = "people_id";
	public static final String PEOPLE_ID_TYPE = "people_id_type";
	public static final String PERSONNEL_CODE = "personnel_code";
	public static final String PRENAME = "prename";
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String FIRSTNAME_ND = "firstname_nd";
	public static final String LASTNAME_ND = "lastname_nd";
	public static final String FIRSTNAME_RD = "firstname_rd";
	public static final String LASTNAME_RD = "lastname_rd";
	public static final String NICKNAME = "nickname";
	public static final String GENDER = "gender";
	public static final String RELIGION = "religion";
	public static final String RACE = "race";
	public static final String NATIONALITY = "nationality";
	public static final String MARITAL_STATUS = "marital_status";
	public static final String BIRTH_DATE = "birth_date";
	public static final String BLOOD = "blood";
	public static final String HEIGHT = "height";
	public static final String WEIGHT = "weight";
	public static final String CONGENITAL_DISEASE = "congenital_disease";
	public static final String PERSONNEL_STATUS = "personnel_status";
	public static final String ALIVE_STATUS = "alive_status";
	public static final String JOB_POSITION_ID = "job_position_id";
	public static final String DEPARTMENT_ID = "department_id";
	public static final String LICENSE_LECTURER_NUMBER = "license_lecturer_number";
	public static final String LICENSE_LECTURER_TYPE = "license_lecturer_type";
	public static final String LICENSE_LECTURER_ISSUED_DATE = "license_lecturer_issued_date";
	public static final String LICENSE_LECTURER_EXPIRED_DATE = "license_lecturer_expired_date";
	public static final String LICENSE_11_NUMBER = "license_11_number";
	public static final String LICENSE_ISSUE_AREA = "license_issue_area";
	public static final String LICENSE_ISSUE_PROVINCE_ID = "license_issue_province_id";
	public static final String LICENSE_17_NUMBER = "license_17_number";
	public static final String LICENSE_18_NUMBER = "license_18_number";
	public static final String LICENSE_19_NUMBER = "license_19_number";
	public static final String FILL_DEGREE_POST = "fill_degree_post";
	public static final String FILL_DEGREE_POST_DATE = "fill_degree_post_date";
	public static final String TEL = "tel";
	public static final String MOBILE = "mobile";
	public static final String EMAIL = "email";
	public static final String CENSUS_ADDRESS = "census_address";
	public static final String CENSUS_CITY_ID = "census_city_id";
	public static final String CENSUS_DISTRICT_ID = "census_district_id";
	public static final String CENSUS_PROVINCE_ID = "census_province_id";
	public static final String CENSUS_POSTCODE_ID = "census_postcode_id";
	public static final String CURRENT_ADDRESS = "current_address";
	public static final String CURRENT_CITY_ID = "current_city_id";
	public static final String CURRENT_DISTRICT_ID = "current_district_id";
	public static final String CURRENT_PROVINCE_ID = "current_province_id";
	public static final String CURRENT_POSTCODE_ID = "current_postcode_id";
	public static final String EMPLOYMENT_TYPE = "employment_type";
	public static final String START_WORK_DATE = "start_work_date";
	public static final String RECRUIT_BY_ID = "recruit_by_id";
	public static final String RECRUIT_DATE = "recruit_date";
	public static final String RESIGN_BY_ID = "resign_by_id";
	public static final String RESIGN_DATE = "resign_date";
	public static final String RESIGN_TYPE = "resign_type";
	public static final String RESIGN_DESCRIPTION = "resign_description";
	public static final String BANK_NAME = "bank_name";
	public static final String BANK_ACCOUNT_NUMBER = "bank_account_number";
	public static final String BANK_ACCOUNT_TYPE = "bank_account_type";
	public static final String BANK_ACCOUNT_NAME = "bank_account_name";
	public static final String BANK_ACCOUNT_BRANCH = "bank_account_branch";
	public static final String BANK_ACCOUNT_PROVINCE_ID = "bank_account_province_id";	
	public static final String FATHER_ID = "father_id";
	public static final String MOTHER_ID = "mother_id";
	public static final String SPOUSE_ID = "spouse_id";
	
	public static String getTitle(String propertyId){
		HashMap<String, String> mapTitle = new HashMap<String, String>();
		mapTitle.put(PERSONNEL_ID,"ลำดับ");
		mapTitle.put(SCHOOL_ID,"โรงเรียน");
		mapTitle.put(PEOPLE_ID,"เลขประจำตัวประชาชน");
		mapTitle.put(PEOPLE_ID_TYPE,"ประเภทเลขประจำตัวประชาชน");
		mapTitle.put(PERSONNEL_CODE,"เลขประจำตัว");
		mapTitle.put(PRENAME,"ชื่อต้น");
		mapTitle.put(FIRSTNAME,"ชื่อ");
		mapTitle.put(LASTNAME,"สกุล");
		mapTitle.put(FIRSTNAME_ND,"ชื่อภาษาที่สอง");
		mapTitle.put(LASTNAME_ND,"สกุลภาษาที่สอง");
		mapTitle.put(FIRSTNAME_RD,"ชื่อภาษาที่สาม");
		mapTitle.put(LASTNAME_RD,"ชื่อภาษาที่สาม");
		mapTitle.put(NICKNAME,"ชื่อเล่น");
		mapTitle.put(GENDER,"เพศ");
		mapTitle.put(RELIGION,"ศาสนา");
		mapTitle.put(RACE,"เชื้อชาติ");
		mapTitle.put(NATIONALITY,"สัญชาติ");
		mapTitle.put(MARITAL_STATUS,"สถานภาพ");
		mapTitle.put(BIRTH_DATE,"วัน เดือน ปี เกิด");
		mapTitle.put(BLOOD,"หมู่เลือด");
		mapTitle.put(HEIGHT,"ส่วนสูง");
		mapTitle.put(WEIGHT,"น้ำหนัก");
		mapTitle.put(CONGENITAL_DISEASE,"โรคประจำตัว");
		mapTitle.put(PERSONNEL_STATUS,"สถานะ");
		mapTitle.put(JOB_POSITION_ID,"ตำแหน่ง");
		mapTitle.put(DEPARTMENT_ID,"แผนก");
		mapTitle.put(LICENSE_LECTURER_NUMBER,"เลขที่ใบประกอบวิชาชีพครู");
		mapTitle.put(LICENSE_LECTURER_TYPE,"ประเภทใบประกอบวิชาชีพ");
		mapTitle.put(LICENSE_LECTURER_ISSUED_DATE,"วัน เดือน ปี ที่ได้หมายเลขครู");
		mapTitle.put(LICENSE_LECTURER_EXPIRED_DATE,"วัน เดือน ปี ที่หมดอายุหมายเลขครู");
		mapTitle.put(LICENSE_11_NUMBER,"เลขที่ใบอนุญาติ สช 11");
		mapTitle.put(LICENSE_ISSUE_AREA,"เขตพื้นที่ ที่ออก");
		mapTitle.put(LICENSE_ISSUE_PROVINCE_ID,"ออกโดย(จังหวัด)");
		mapTitle.put(LICENSE_17_NUMBER,"เลขที่ใบอนุญาติ สช 17");
		mapTitle.put(LICENSE_18_NUMBER,"เลขที่ใบอนุญาติ สช 18");
		mapTitle.put(LICENSE_19_NUMBER,"เลขที่ใบอนุญาติ สช 19");
		mapTitle.put(FILL_DEGREE_POST,"วุฒิที่ได้รับการบรรจุ");
		mapTitle.put(FILL_DEGREE_POST_DATE,"วัน เดือน ปีที่ได้รับการบรรจุ");
		mapTitle.put(TEL,"โทร");
		mapTitle.put(MOBILE,"มือถือ");
		mapTitle.put(EMAIL,"อีเมล์");
		mapTitle.put(CENSUS_ADDRESS,"ที่อยู่ตามทะเบียนบ้าน");
		mapTitle.put(CENSUS_CITY_ID,"ตำบลตามทะเบียนบ้าน");
		mapTitle.put(CENSUS_DISTRICT_ID,"อำเภอตามทะเบียนบ้าน");
		mapTitle.put(CENSUS_PROVINCE_ID,"จังหวัดตามทะเบียนบ้าน");
		mapTitle.put(CENSUS_POSTCODE_ID,"ไปรษณีย์");
		mapTitle.put(CURRENT_ADDRESS,"ที่อยู่ปัจจุบัน");
		mapTitle.put(CURRENT_CITY_ID,"ตำบลปัจจุบัน");
		mapTitle.put(CURRENT_DISTRICT_ID,"อำเภอปัจจุบัน");
		mapTitle.put(CURRENT_PROVINCE_ID,"จังหวัดปัจจุบัน");
		mapTitle.put(CURRENT_POSTCODE_ID,"ไปรษณีย์ปัจจุบัน");
		mapTitle.put(EMPLOYMENT_TYPE,"การว่าจ้าง");
		mapTitle.put(START_WORK_DATE,"วันเริ่มทำงาน");
		mapTitle.put(RECRUIT_BY_ID,"ผู้รับเข้าทำงาน");
		mapTitle.put(RECRUIT_DATE,"วันที่รับเข้าทำงาน");
		mapTitle.put(RESIGN_BY_ID,"ผู้รับเรื่องจำหน่ายออก");
		mapTitle.put(RESIGN_DATE,"วัน เดือน ปี ที่ออก");
		mapTitle.put(RESIGN_TYPE,"ประเภทการออก");
		mapTitle.put(RESIGN_DESCRIPTION,"รายละเอียดการออก");
		mapTitle.put(BANK_NAME,"ธนาคาร");
		mapTitle.put(BANK_ACCOUNT_NUMBER,"หมายเลขบัญชี");
		mapTitle.put(BANK_ACCOUNT_TYPE,"ประเภทบัญชี");
		mapTitle.put(BANK_ACCOUNT_NAME,"ชื่อบัญชี");
		mapTitle.put(BANK_ACCOUNT_BRANCH,"สาขา");
		mapTitle.put(BANK_ACCOUNT_PROVINCE_ID,"จังหวัดธนาคาร");	
		mapTitle.put(FATHER_ID,"บิดา");
		mapTitle.put(MOTHER_ID,"มารดา");
		mapTitle.put(SPOUSE_ID,"คู่สมรส");
		return mapTitle.get(propertyId);
	}
	
}
