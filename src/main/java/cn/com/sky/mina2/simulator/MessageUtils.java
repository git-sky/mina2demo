/**
 * 
 */
package cn.com.sky.mina2.simulator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * @author fss
 * 
 */
public class MessageUtils {

	private static String obdReportFormat = "%.4f,E,%.4f,N,%.2f,%.2f";
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"#ddMMyy#HHmmss.SSS##");
	private static Random random = new Random();

	public static String getMessage(String devType, String imei,
			String msgType, double lng, double lat, String cell,
			String pos_type, int status, int nDayBefore) {
		if (devType.equals("MT90")) {
			return getMT90Message(imei, msgType, lng, lat, cell);
		} else if (devType.equals("OBD")) {
			return getOBDMessage(imei, msgType, lng, lat, cell, pos_type,
					status, nDayBefore);
		} else if (devType.equals("M2616")) {
			return getOBDMessage(imei, msgType, lng, lat, cell, pos_type,
					status, nDayBefore);
		}
		return null;
	}

	public static String getMessage(String devType, String imei,
			String msgType, double lng, double lat, String cell, int status,
			int nDayBefore) {
		if (devType.equals("MT90")) {
			return getMT90Message(imei, msgType, lng, lat, cell);
		} else if (devType.equals("OBD")) {
			return getOBDMessage(imei, msgType, lng, lat, cell, "A", status,
					nDayBefore);
		} else if (devType.equals("M2616")) {
			return getOBDMessage(imei, msgType, lng, lat, cell, "A", status,
					nDayBefore);
		}
		return null;
	}

	public static String getMT90Message(String imei, String instructionCode,
			double lng, double lat, String cell) {
		DecimalFormat df = new DecimalFormat("0.000000");
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String time = sdf.format(new Date());
		StringBuffer sb = new StringBuffer();
		sb.append("," + imei);
		sb.append(",AAA,");

		int code = 35;
		if (instructionCode.equals("AUT")) {
			code = 35;
		} else if (instructionCode.equals("LPD")) {
			code = 17;
		} else if (instructionCode.equals("SPDHI")) {
			code = 19;
		} else if (instructionCode.equals("BNDIN")) {
			code = 20;
		} else if (instructionCode.equals("BNDOUT")) {
			code = 21;
		}

		sb.append(code);

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

	public static String getOBDMessage(String sn, String typeStr, double lng,
			double lat, String cell, String pos_type, int ss, int dayBefore) {

		double speed = (random.nextInt(120) + Math.random());
		double head = (random.nextInt(360) + Math.random());
		StringBuffer sb = new StringBuffer();

		int status = 2;
		if (pos_type.equalsIgnoreCase("V")) {
			status = 0;
		}

		status = status | ss;

		sb.append("#" + sn + "#000001#" + status + "#0000#");

		sdf.setTimeZone(TimeZone.getTimeZone("Etc/Greenwich"));

		Date d = Calendar.getInstance().getTime();
		long l = d.getTime() - dayBefore * 3600 * 24 * 1000;
		Date nday = new Date(l);

		// 115.500000 >> 11530.0000
		String cellstr = "#1#" + cell + "#";
		sb.append(typeStr + cellstr);
		// double tempLng = Math.floor(lng);
		// String lngStr = df.format(tempLng*100+(lng - tempLng)*60);
		double tempLng = Math.floor(lng) * 100 + (lng - Math.floor(lng)) * 60;
		double tempLat = Math.floor(lat) * 100 + (lat - Math.floor(lat)) * 60;
		String tempStr = String.format(obdReportFormat, tempLng, tempLat,
				speed, head) + sdf.format(nday);
		sb.append(tempStr);
		return sb.toString();
	}

	/**
	 * 生成消息
	 * 
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
			double test = (double) Math.round(speed * 100) / 100;// ,Locale.ENGLISH..."Etc/Greenwich"
			Calendar cal = Calendar.getInstance(TimeZone
					.getTimeZone("Etc/Greenwich"));
			System.out.println(cal.getTime().getTime());
			System.out.println(new Date(cal.getTime().getTime()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
