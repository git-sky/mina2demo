package cn.com.sky.mina2.simulator3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("m2616")
public class M2616Controller extends MT90Controller{
	
	private String getDeviceType(){
		return "OBD";
	}
	
	
	
}
