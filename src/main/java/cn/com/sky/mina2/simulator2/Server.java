package cn.com.sky.mina2.simulator2;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class Server {
	private String ip;
	private int port;
	private String deviceType;
	private IoSession session;
	
	private NioSocketConnector connector;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public NioSocketConnector getConnector() {
		return connector;
	}

	public void setConnector(NioSocketConnector connector) {
		this.connector = connector;
	}
	
	public void disconnect(){
		if(this.session !=null){
			this.session.close(true);
			this.connector.dispose();
		}
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		disconnect();
	}

	@Override
	public String toString() {
		return ip + ", " + port;
	}

}
