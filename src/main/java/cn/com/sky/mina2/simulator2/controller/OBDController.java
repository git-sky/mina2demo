package cn.com.sky.mina2.simulator2.controller;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.sky.ios.mina.simulator2.Server;
import cn.com.sky.ios.mina.simulator2.WebConnectFactory;
import cn.com.sky.ios.mina.simulator2.http.AjaxResult;
import cn.com.sky.ios.mina.simulator2.util.BytesConvert;

@Controller
@RequestMapping("obd")
public class OBDController {

	private void login(HttpSession session, Server server, String sn) {
		if (session.getAttribute("login-" + server.getDeviceType()) == null
				|| !sn.equals(session.getAttribute("login"))) {
			byte[] bs = BytesConvert.decodeHex("676701000B00010" + sn + "00");
			WebConnectFactory.send(session.getId(), server, bs);
			session.setAttribute("login-" + server.getDeviceType(), sn);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping("/position.do")
	@ResponseBody
	public AjaxResult position(HttpSession session, Float lng, Float lat,
			String sn, Server server) {
		login(session, server, sn);
		// byte[] bs = BytesConvert
		// .decodeHex("676702001B000953563DBD,044A1C410C7B5919AE015301CC000010DD001FF201");

		long times = Calendar.getInstance().getTimeInMillis() / 1000;

		String date = BytesConvert.encodeHexStr(BytesConvert.long2Bytes(times,
				4));

		byte[] bs = BytesConvert.decodeHex("676702001B0001" + date + "044A1C41"
				+ "0C7B5919" + "AE" + "0153" + "01CC000010DD001FF2" + "01");

		if (lng != null && lat != null) {
			BigDecimal bd = new BigDecimal(lng);
			byte[] lngByte = BytesConvert.getBytes(bd.multiply(
					new BigDecimal(1800000)).intValue());
			byte[] latByte = BytesConvert.getBytes(new BigDecimal(lat)
					.multiply(new BigDecimal(1800000)).intValue());
			for (int i = 11; i < 15; i++) {
				bs[i] = latByte[i - 11];
			}

			for (int i = 15; i < 19; i++) {
				bs[i] = lngByte[i - 15];
			}
		}
		System.out.println("position:" + BytesConvert.encodeHexStr(bs));
		WebConnectFactory.send(session.getId(), server, bs);

		return AjaxResult.success(null);

	}
}
