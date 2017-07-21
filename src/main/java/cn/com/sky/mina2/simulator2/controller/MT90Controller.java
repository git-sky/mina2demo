package cn.com.sky.mina2.simulator2.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.sky.ios.mina.simulator2.MessageUtils;
import cn.com.sky.ios.mina.simulator2.Server;
import cn.com.sky.ios.mina.simulator2.WebConnectFactory;
import cn.com.sky.ios.mina.simulator2.http.AjaxResult;

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
}
