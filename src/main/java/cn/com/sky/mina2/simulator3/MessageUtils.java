/**
 * 
 */
package cn.com.sky.mina2.simulator3;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * @author fss
 * 
 */
public class MessageUtils {

	private static String obdReportFormat = "%.4f,E,%.4f,N,%.2f,%.2f";
	private static SimpleDateFormat sdf = new SimpleDateFormat("#ddMMyy#HHmmss.SSS##");
	private static Random random = new Random();

	public static String getMessage(String devType, String imei, int msgType, double lng, double lat) {
		if (devType.equals("MT90")) {
			return getMT90Message(imei, msgType, lng, lat);
		} else if (devType.equals("OBD")) {
			return getOBDMessage(imei, msgType, lng, lat);
		}
		return null;
	}

	public static String getMT90Message(String imei, int instructionCode, double lng, double lat) {
		DecimalFormat df = new DecimalFormat("0.000000");
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("Etc/Greenwich"));
		String time = sdf.format(new Date());
		
		StringBuffer sb = new StringBuffer();
		sb.append("," + imei);
		sb.append(",AAA,");
		sb.append(instructionCode);
		String latStr = df.format(lat);
		String lngStr = df.format(lng);
		sb.append("," + latStr + "," + lngStr + ",");
		sb.append(time);
		sb.append(",A,4,27," + String.valueOf(random.nextInt(120)) + ","
				+ String.valueOf(random.nextInt(360)));
		sb.append(",7,63,21207,272285,460|0|109F|CD2B,0000,0000|0000|0000|092A|0000,2,*");
		int tempLen = sb.toString().length() + 4;
		sb.insert(0, "$$A" + String.valueOf(tempLen));
		return validate(sb.toString());
	}

	
	
	public static String getOBDMessage(String sn, int type, double lng, double lat) {

		double speed = (random.nextInt(120) + Math.random());
		double head = (random.nextInt(360) + Math.random());
		StringBuffer sb = new StringBuffer();

		sb.append("#" + sn + "##3#0000#");
		String typeStr = null;
		switch (type) {
		case 35:
			typeStr = "AUT";
			break;

		case 20:
			typeStr = "BNDIN";
			break;

		case 19:
			typeStr = "SPDHI";
			break;

		case 21:
			typeStr = "BNDOUT";
			break;

		default:
			typeStr = "AUT";
			break;
		}
		sdf.setTimeZone(TimeZone.getTimeZone("Etc/Greenwich"));
		// 115.500000 >> 11530.0000
		sb.append(typeStr + "#1#46000011ee9a52,11eed2d6,11eee6a9#");
		// double tempLng = Math.floor(lng);
		// String lngStr = df.format(tempLng*100+(lng - tempLng)*60);
		double tempLng = Math.floor(lng) * 100 + (lng - Math.floor(lng)) * 60;
		double tempLat = Math.floor(lat) * 100 + (lat - Math.floor(lat)) * 60;
		String tempStr = String.format(obdReportFormat, tempLng, tempLat, speed, head)
				+ sdf.format(Calendar.getInstance().getTime());
		sb.append(tempStr);
		return sb.toString();
	}

	/**
	 * 生成消息
	 * @param msg
	 * @return
	 */
	public static String validate(String msg) {
		byte[] added = msg.toString().getBytes();
		int temp = 0;
		for (byte b : added) {
			short ub = (short) (b & 0xFF);
			temp += ub;
		}
		byte check = (byte) temp;
		String hex = Integer.toHexString(check & 0xFF);
		if (hex.length() == 1)
			hex = '0' + hex;

		return msg + hex.toUpperCase() + "\r\n";
	}

	public static void main(String[] args) {
		try {
			double speed = 92.2712;
			double test =(double)Math.round(speed*100)/100;//,Locale.ENGLISH..."Etc/Greenwich"
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Etc/Greenwich"));
			System.out.println(cal.getTime().getTime());
			System.out.println(new Date(cal.getTime().getTime()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
