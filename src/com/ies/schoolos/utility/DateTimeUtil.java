package com.ies.schoolos.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
	
	public static String getDDMMYYYY(Date date){
		String dateStr = new SimpleDateFormat("dd/MM/yyyy").format(date);
    	String year = dateStr.substring(6);
    	if(Integer.parseInt(year) > 2500)
    		dateStr = dateStr.replace(year, Integer.parseInt(year)-543+"");
		return dateStr;
	}
	
	public static String getDDMMYYYYBD(Date date){
		String dateStr = new SimpleDateFormat("dd/MM/yyyy").format(date);
    	String year = dateStr.substring(6);
    	if(Integer.parseInt(year) < 2500)
    		dateStr = dateStr.replace(year, Integer.parseInt(year)+543+"");
		return dateStr;
	}
	
	/* กำหนดวันที่ ในรูปแบบ SQL*/
	public static String getyyyyMMddSringSQL(Date date){
		String dateStr = new SimpleDateFormat("yyyy-MM-dd",new Locale("en")).format(new Date());
		return dateStr;
	}
	
	/* กำหนดวันที่ ในรูปแบบ SQL*/
	public static Date getyyyyMMddDateSQL(Date date){
		String dateStr = new SimpleDateFormat("yyyy-MM-dd",new Locale("en")).format(new Date());
		//Date date = new Date();
		try{
			date = new SimpleDateFormat("yyyy-MM-dd",new Locale("en")).parse(dateStr.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return date;
	}
	
	/* กำหนดวัน เป็น dd/MM/YYYY จาก dd/MM/YYYY */
	public static String getDDMMYYYYBD(String date){
		String[] dates = date.split("-");
    	String dateStr = dates[2] + "/" + dates[1] + "/";
    	if(Integer.parseInt(dates[0]) < 2500)
    		dateStr += (Integer.parseInt(dates[0])+543) + "";
    	else
    		dateStr = dates[0];
		return dateStr;
	}
	
	/* กำหนดวันที่ จาก วันที่กำหนด */
	public static String getDateFromDayOfWeek(int dowTarget){
		Calendar now = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
		
		int DAY_OF_WEEK = now.get(Calendar.DAY_OF_WEEK)-1;
		if(dowTarget >= DAY_OF_WEEK){
			now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), (now.get(Calendar.DATE)+dowTarget-DAY_OF_WEEK));
		}else if(dowTarget <= DAY_OF_WEEK){
			now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), (now.get(Calendar.DATE)+6-dowTarget));				
		}
		return format1.format(now.getTime());
	}
	
	/* แสดงวันแรกของปี 1 มค ปี */
	@SuppressWarnings("deprecation")
	public static Date getFirstDateOfYear(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, new Date().getYear()+1900);
		cal.set(Calendar.DAY_OF_YEAR, 1);    
		return cal.getTime();
	}
	
	/* แสดงวันสุดท้ายของปี  31 ธค ปี */
	@SuppressWarnings("deprecation")
	public static Date getLastDateOfYear(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, new Date().getYear()+1900);
		cal.set(Calendar.DAY_OF_YEAR, 11);    
		cal.set(Calendar.DAY_OF_MONTH, 31);
		return cal.getTime();
	}
	
	@SuppressWarnings("deprecation")
	public static String getChristianYear(){
		return Integer.toString(1900+new Date().getYear());
	}
	
	@SuppressWarnings("deprecation")
	public static String getBuddishYear(){
		return Integer.toString(2443+new Date().getYear());
	}
	
	public static boolean compareBetweenDate(Date beginDate, Date endDate){
		boolean isBetween = false;
		Date todayDate = new Date();
		if((todayDate.equals(beginDate) || todayDate.equals(endDate)) ||
				(todayDate.after(beginDate) && todayDate.before(endDate))) {
			isBetween = true;
		}
		return isBetween;
	}

	public static int getWeekOfday()
    {
        Calendar now = Calendar.getInstance();
        return now.get(7) - 1;
    }
}
