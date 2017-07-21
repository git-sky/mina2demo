package cn.com.sky.mina2.simulator3.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.sky.ios.mina.simulator3.MessageUtils;
import cn.com.sky.ios.mina.simulator3.Server;
import cn.com.sky.ios.mina.simulator3.WebConnectFactory;
import cn.com.sky.ios.mina.simulator3.http.AjaxResult;

@Controller
@RequestMapping("mt90")
public class MT90Controller {
	
	
	private String getDeviceType(){
		return "MT90";
	}
	
	@RequestMapping("/position.do")
	@ResponseBody
	public AjaxResult position(HttpSession session, Double lng,Double lat,String sn,Server server){
		
		String str=MessageUtils.getMessage(getDeviceType(), sn, 35, lng, lat);
		
		WebConnectFactory.send(session.getId(), server, str);
		return AjaxResult.success(null);
	}
	
	@RequestMapping("/alarm-infence.do")
	@ResponseBody
	public AjaxResult infence(HttpSession session, Double lng,Double lat,String sn,Server server){
		String str=MessageUtils.getMessage(getDeviceType(), sn, 20, lng, lat);
		WebConnectFactory.send(session.getId(), server, str);
		return AjaxResult.success(null);
	}
	
	@RequestMapping("/alarm-outfence.do")
	@ResponseBody
	public AjaxResult outfence(HttpSession session, Double lng,Double lat,String sn,Server server){
		String str=MessageUtils.getMessage(getDeviceType(), sn, 21, lng, lat);
		WebConnectFactory.send(session.getId(), server, str);
		return AjaxResult.success(null);
	}
	
	@RequestMapping("/alarm-hp.do")
	@ResponseBody
	public AjaxResult heighSpeed(HttpSession session, Double lng,Double lat,String sn,Server server){
		String str=MessageUtils.getMessage(getDeviceType(), sn, 19, lng, lat);
		WebConnectFactory.send(session.getId(), server, str);
		return AjaxResult.success(null);
	}
	
	@RequestMapping("/alarm-lb.do")
	@ResponseBody
	public AjaxResult lowBattery(HttpSession session, Double lng,Double lat,String sn,Server server){
		String str=MessageUtils.getMessage(getDeviceType(), sn, 17, lng, lat);
		WebConnectFactory.send(session.getId(), server, str);
		return AjaxResult.success(null);
	}
	
	
}
