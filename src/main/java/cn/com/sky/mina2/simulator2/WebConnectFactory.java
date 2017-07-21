package cn.com.sky.mina2.simulator2;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class WebConnectFactory {
	
	private static Map<String,Server> sessionMapping=new HashMap();
	
	private static void conncet(String websession, Server server){
		
		String HOST = server.getIp();
		
		int PORT = server.getPort();
		
		int CONNECT_TIMEOUT_MILLS = 30000;
		
		int TIMOUT_CHECK_INTERVAL = 15000;
		
		NioSocketConnector connector = new NioSocketConnector();
		
		// 连接超时设置
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT_MILLS);
		// 连接超时检查间隔
		connector.setConnectTimeoutCheckInterval(TIMOUT_CHECK_INTERVAL);
		
		DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
		
		filterChain.addLast("codec", new ProtocolCodecFilter(new CodecFactory()));
		// 日志过滤器
		filterChain.addLast("logger", new LoggingFilter());
		
		TestHandler handler = new TestHandler();
		connector.setHandler(handler);
		int i=0;
		while (i<5) {
			try {
				ConnectFuture connFuture = connector.connect(new InetSocketAddress(HOST, PORT));
				connFuture.awaitUninterruptibly();
				IoSession session = connFuture.getSession();
				server.setSession(session);
				server.setConnector(connector);
				break;
			} catch (RuntimeIoException e) {
				i++;
				System.out.println(server.toString());
				e.printStackTrace();
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	public synchronized static void send(String websessionid,Server server,Object msg){
		Server  srv=sessionMapping.get(websessionid+"#"+server.toString());
		if(srv==null){
			conncet(websessionid,server);
			sessionMapping.put(websessionid+"#"+server.toString(), server);
		}else{
			if(srv.getDeviceType().equals(server.getDeviceType())){
				server=srv;
			}else{//换协议需要重连
				srv.disconnect();
				conncet(websessionid,server);
			}
			
		}
		
		server.getSession().write(msg);
		
		
	}
	
	public static void disconnect(long iosession){
		 Iterator<String> it = sessionMapping.keySet().iterator();  
	        while(it.hasNext()){
	            String key=it.next();  
				Server server=sessionMapping.get(key);
				if(server.getSession().getId()==iosession){
					server.disconnect();
					sessionMapping.remove(key);
					break;
				}
	        }
	}
	
	public static void disconnect(String websession){
	   Iterator<String> it = sessionMapping.keySet().iterator();  
        while(it.hasNext()){
            String key=it.next();  
            if(key.startsWith(websession)){
				Server server=sessionMapping.get(key);
				server.disconnect();
				sessionMapping.remove(key);
			}   
            
        }
		
	}
	
}
