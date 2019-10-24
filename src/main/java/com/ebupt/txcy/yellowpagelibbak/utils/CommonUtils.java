package com.ebupt.txcy.yellowpagelibbak.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtils {
	public static final String weekdayFormat = "EEE";
	public static final String timeFormat = "HHmmss";
	public static final String dateFormat = "yyyyMMdd";
	public static final String monthFormat = "yyyyMM";
	public static final String dateTimeFormat = dateFormat + timeFormat;

	/**
	 * 通过匹配字符串以及序列号的长度获取下一序列号的统一规则相关ID
	 * 
	 * @param charString
	 * @param unifyRuleID
	 * @param length
	 * @return
	 */
	public static String getUnifyRuleString(String charString, String unifyRuleID, int length) {
		String serial = unifyRuleID.replaceFirst(charString, "");
		int unifyRuleInt = Integer.parseInt(serial) + 1;
		String newValue = String.valueOf(unifyRuleInt);
		int len = length - newValue.length();
		if (len < 0) {
			len = length;
			newValue = "";
		}
		for (int i = 0; i < len; i++) {
			newValue = "0" + newValue;
		}
		return charString + newValue;

	}
    public static String formatDate(String time) {
	      String  newtime  = time ;
	      if(time!=null){
	    	  if(time.length() == 8){
		          newtime = time.substring(0,4) +'-'+ time.substring(4,6) +'-'+ time.substring(6,8) ;
		      }else if(time.length() == 6){    
		          newtime = time.substring(0,4) +'-'+ time.substring(4,6)  ;
		      }else if(time.length() == 14){
		          newtime = time.substring(0,4) +'-'+ time.substring(4,6) +'-'+ time.substring(6,8)+' '+time.substring(8,10)+':'+time.substring(10,12)+':'+time.substring(12,14);
		      } 
	      }
	      return newtime;
	 }
 
    private static final char[] NUMBERS = "0123456789".toCharArray();
    private final static Random random = new Random();
    
    /**
     * 随机生成指定长度的数字串
     * @param length
     * @return
     */
    public static String getRandomNum(int length) {
    	StringBuilder builder = new StringBuilder(length);
    	for (int i = 0; i < length; i++) {
    		builder.append(NUMBERS[random.nextInt(NUMBERS.length)]);
    	}
    	return builder.toString();
    }
    
	/**
	 * 验证是否为电话号码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean validatePhoneNumber(String phoneNumber) {
		if (phoneNumber.equals(null)) {
			return false;
		} else if (!phoneNumber.matches("^[0-9]{3,16}$")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 生成32位UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 将yyyyMMddHHmmss转为yyyy-MM-dd HH:mm:ss
	 * 
	 * @param yyyyMMddHHmmss
	 * @return
	 */
	public static String changeDateStyle(String yyyyMMddHHmmss) {
		if (yyyyMMddHHmmss == null) {
			return "";
		}
		yyyyMMddHHmmss = yyyyMMddHHmmss.trim();
		if (yyyyMMddHHmmss.length() != 14) {
			int length = 14 - yyyyMMddHHmmss.length();
			StringBuilder sb = new StringBuilder();
			sb.append(yyyyMMddHHmmss);
			for (int i = 0; i < length; i++) {
				sb.append("0");
			}
			yyyyMMddHHmmss = sb.toString();
		}
		if (yyyyMMddHHmmss.length() > 14) {
			yyyyMMddHHmmss = yyyyMMddHHmmss.substring(0, 13);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null;
		try {
			date = sdf.parse(yyyyMMddHHmmss);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	/**
	 * 将yyyyMMdd转为yyyy-MM-dd
	 * 
	 * @param yyyyMMdd
	 * @return
	 */
	public static String changeDateStyleForyyyyMMdd(String yyyyMMddHHmmss) {
		if (yyyyMMddHHmmss == null) {
			return "";
		}
		yyyyMMddHHmmss = yyyyMMddHHmmss.trim();
		if (yyyyMMddHHmmss.length() != 8) {
			int length = 8 - yyyyMMddHHmmss.length();
			StringBuilder sb = new StringBuilder();
			sb.append(yyyyMMddHHmmss);
			for (int i = 0; i < length; i++) {
				sb.append("0");
			}
			yyyyMMddHHmmss = sb.toString();
		}
		if (yyyyMMddHHmmss.length() > 8) {
			yyyyMMddHHmmss = yyyyMMddHHmmss.substring(0, 7);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = sdf.parse(yyyyMMddHHmmss);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	/**
	 * 返回当前时间的字符串，格式为yyyyMMddHHmmssS，精确到毫秒。
	 */
	public static String yyyyMMddHHmmssS() {
		return getCurrentTimeString("yyyyMMddHHmmssS", System.currentTimeMillis());
	}

	public static String getCurrentTimeString(String format, long mills) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(format, Locale.CHINA);
		return simpleFormat.format(new Date(mills));
	}

	public static String getCurrentFormatTime(String format) {
		return getCurrentTimeString(format, System.currentTimeMillis());
	}

	/**
	 * 返回当前时间的字符串，格式为yyyyMM。
	 */
	public static String yyyyMM() {
		return getCurrentTimeString(monthFormat, System.currentTimeMillis());
	}

	/**
	 * 返回当前时间的字符串，格式为yyyyMMddHHmmss。
	 */
	public static String yyyyMMddHHmmss() {
		return getCurrentTimeString(dateTimeFormat, System.currentTimeMillis());
	}

	/**
	 * 返回当前日期的字符串，格式为yyyyMMdd。
	 */
	public static String yyyyMMdd() {
		return getCurrentTimeString(dateFormat, System.currentTimeMillis());
	}

	/**
	 * 返回当前时间的字符串，格式为HHmmss。
	 */
	public static String HHmmss() {
		return getCurrentTimeString(timeFormat, System.currentTimeMillis());
	}

	/**
	 * 返回给定日期的字符串形式，格式为yyyyMMdd。
	 * 
	 * @param mills
	 *            时间，一般是通过System.currentTimeMillis()获得
	 * @return 输入时间的日期字符串
	 */
	public static String getFormatedDate(long mills) {
		return getCurrentTimeString(dateFormat, mills);
	}

	/**
	 * 返回给定时间的字符串形式，格式为yyyyMMddHHmmss。
	 * 
	 * @param mills
	 *            时间，一般是通过System.currentTimeMillis()获得
	 * @return 输入时间的时间字符串
	 */
	public static String getFormatedTime(long mills) {
		return getCurrentTimeString(dateTimeFormat, mills);
	}

	/**
	 * 时间格式转换
	 * 
	 * @param time
	 *            原有时间
	 * @return 正确的6位时间
	 */
	public static String fourToSix(String time) {
		String trueTime = "";
		if (time != null && time.length() == 6) {
			trueTime = time;
		} else if (time != null && time.length() == 4) {
			trueTime = time + "00";
		} else {
			return time;
		}
		return trueTime;
	}

	/**
	 * 返回给定时间的字符串yyyyMMddHHmmss与当前时间相差的分钟数。
	 * 
	 * @param mills
	 * @return
	 */
	public static long getDifferernce(String mills) {
		if (mills.length() != 14) {
			return 0;
		} else {
			// SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar c = Calendar.getInstance(); // 当时的日期和时间
			// System.out.println("获取系统当前时间为：" + df.format(c.getTime()));

			Calendar c2 = Calendar.getInstance(); // 当时的日期和时间
			c2.set(Integer.parseInt(mills.substring(0, 4)), Integer.parseInt(mills.substring(4, 6)) - 1,
					Integer.parseInt(mills.substring(6, 8)), Integer.parseInt(mills.substring(8, 10)),
					Integer.parseInt(mills.substring(10, 12)), Integer.parseInt(mills.substring(12, 14)));// Month
																											// 值是基于
																											// 0
																											// 的。例如，0
																											// 表示
																											// January
																											// System.out.println("传入的时间转换后为："
																											// +
																											// df.format(c2.getTime()));

			long ld = c.getTimeInMillis();// 返回ld 的时间值，以毫秒为单位
			// System.out.println("获取系统当前时间以毫秒计算为：" + ld);

			long ld2 = c2.getTimeInMillis();// 返回ld2 的时间值，以毫秒为单位
			// System.out.println("传入的时间转换后以毫秒计算为：" + ld2);

			long diff = (ld - ld2) / (60 * 1000);
			return diff;
		}
	}



	public static Date StrToDate(String str) { 
		  
		   SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); 
		   Date date = null; 
		   try { 
		    date = format.parse(str); 
		   } catch (ParseException e) { 
		    e.printStackTrace(); 
		   } 
		   return date; 
		}
	public static Date StrToDatess(String str) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 判断字符串为空
	 * 
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || "".equals(str.trim());
	}

	/**
	 * 判断字符串不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}
}
