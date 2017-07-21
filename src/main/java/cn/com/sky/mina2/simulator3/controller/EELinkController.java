package cn.com.sky.mina2.simulator3.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.sky.ios.mina.simulator3.Server;
import cn.com.sky.ios.mina.simulator3.WebConnectFactory;
import cn.com.sky.ios.mina.simulator3.http.AjaxResult;
import cn.com.sky.ios.mina.simulator3.util.BytesConvert;
import cn.com.sky.ios.mina.simulator3.util.StringUtil;



@Controller
@RequestMapping("eelink")
public class EELinkController {
	
	
	private void login(HttpSession session,Server server,String sn){
		if(session.getAttribute("login-"+server.getDeviceType())==null||!sn.equals(session.getAttribute("login"))){
			byte[] bs=BytesConvert.decodeHex("676701000B00010"+sn+"00");
			WebConnectFactory.send(session.getId(), server, bs);
			session.setAttribute("login-"+server.getDeviceType(), sn);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	@RequestMapping("/position.do")
	@ResponseBody
	public AjaxResult position(HttpSession session, Float lng,Float lat,String sn,Server server){
		login(session,server,sn);       
		byte[] bs=BytesConvert.decodeHex("676702001B000953563DBD,044A1C410C7B5919AE015301CC000010DD001FF201");
		if(lng!=null&&lat!=null){
			BigDecimal bd=new BigDecimal(lng);
			byte[] lngByte = BytesConvert.getBytes(bd.multiply(new BigDecimal(1800000)).intValue());
			byte[] latByte = BytesConvert.getBytes(new BigDecimal(lat).multiply(new BigDecimal(1800000)).intValue());
			for(int i=11;i<15;i++){
				bs[i]=latByte[i-11];
			}
			
			for(int i=15;i<19;i++){
				bs[i]=lngByte[i-15];
			}
		}
		System.out.println("position:"+BytesConvert.encodeHexStr(bs));
		WebConnectFactory.send(session.getId(), server, bs);
		
		return AjaxResult.success(null);
		
	}
	
	private  byte[] getOBD(){
		return new byte[]{0x67,0x67,0x07,0x00,(byte)0x88,0x00,0x03,
				0x53,0x55,0x61,(byte)0xCF,//时间
				0x00,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
				0x02,0x33,0x44,0x55,0x66,
				0x03,0x33,0x44,0x55,0x66,
				0x04,0x33,0x44,0x55,0x66,
				0x05,(byte)0xAA,0x00,0x00,0x00,
				0x0A,0x33,0x44,0x55,0x66,
				0x0B,0x33,0x44,0x55,0x66,
				0x0C,0x4E,0x20,0x00,0x00,
				0x0D,(byte)0xAA,0x00,0x00,0x00,
				0x0E,0x33,0x44,0x55,0x66,
				0x0F,0x33,0x44,0x55,0x66,
				0x10,(byte)0xAA,(byte)0xAA,0x00,0x00,
				0x11,0x33,0x44,0x55,0x66,
				0x1C,0x33,0x44,0x55,0x66,
				0x1F,0x33,0x44,0x55,0x66,
				0x20,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
				0x21,0x33,0x44,0x55,0x66,
				0x40,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,
				0x42,0x33,0x44,0x55,0x66,
				0x46,0x33,0x44,0x55,0x66,
				0x4D,0x33,0x44,0x55,0x66,
				0x5C,0x33,0x44,0x55,0x66,
				0x5E,0x33,0x44,0x55,0x66,
				(byte)0x89,0x00,0x00,0x03,0x6D,
				(byte)0x8A,0x00,0x00,0x00,0x00,
				(byte)0x8B,0x00,0x00,0x00,0x00
				};
	}
	
	@RequestMapping("/obd.do")
	@ResponseBody
	public AjaxResult obd(HttpSession session,String sn,Server server,String data){
		login(session,server,sn);
		byte[] obd=getOBD();
		if(!StringUtil.empty(data)){
			obd=BytesConvert.decodeHex(data);
		}
		
		WebConnectFactory.send(session.getId(), server, obd);
		return AjaxResult.success(null);
		
	}
	
	@RequestMapping("/obderror.do")
	@ResponseBody
	public AjaxResult obderror(HttpSession session,String sn,Server server,String data){
		login(session,server,sn);
		
		
		return AjaxResult.success(null);
		
	}
	
	
}
