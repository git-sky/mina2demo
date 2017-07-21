package cn.com.sky.mina2.simulator3.controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.sky.ios.mina.simulator3.http.AjaxResult;

@Controller
@RequestMapping("response")
public class ResponseDataController {
	
	private static ConcurrentHashMap<String,BlockingQueue<String>> data=new ConcurrentHashMap<String,BlockingQueue<String>>();
	
	public static void add(String sessionid,String msg){
		BlockingQueue que=data.get(sessionid);
		if(que==null){
			que=new ArrayBlockingQueue(1000);
			data.put(sessionid, que);
		}
		que.add(msg);
		
	}
	
	@RequestMapping("/query.do")
	@ResponseBody
	public AjaxResult query(HttpSession session){
		
		BlockingQueue que=data.get(session.getId());
		
		try {
			return AjaxResult.success(que.poll(10, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			e.printStackTrace();
			return AjaxResult.fail(null);
		}
		
	}
	
}
