package cn.com.sky.mina2.simulator2;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import cn.com.sky.ios.mina.simulator2.util.BytesConvert;


@Component
public class TestHandler implements IoHandler {

	// --------------------------------------------------------------
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if(message instanceof String) {
			//Device.displayMsg((String)message);
			System.out.println("收到数据：string:"+(String)message);
		}else if(message instanceof byte[]) {
			System.out.println("client 收到数据："+BytesConvert.encodeHexStr((byte[]) message));
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("服务器关闭:"+session.getId());
		WebConnectFactory.disconnect(session.getId());
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		if(status.equals(IdleStatus.BOTH_IDLE))
			session.close(true);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
//		((SocketSessionConfig) session.getConfig()).setKeepAlive(true);
//		((SocketSessionConfig) session.getConfig()).setBothIdleTime(300);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
	}

}
