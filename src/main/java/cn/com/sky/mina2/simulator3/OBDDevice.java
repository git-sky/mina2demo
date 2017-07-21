package cn.com.sky.mina2.simulator3;

import java.net.InetSocketAddress;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.PatternLayout;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class OBDDevice {
	
	private IoSession session;

	private ConnectFuture connFuture;

	private NioSocketConnector connector;
	
	public void connect() throws Throwable {
		//String HOST = "219.142.87.104";
		//String HOST = "211.100.96.3";
//		String HOST = "127.0.0.1";
		String HOST = "172.168.2.77";  //测试
//		String HOST ="218.245.5.231"; //正式
		int PORT = 60004;

		int CONNECT_TIMEOUT_MILLS = 30000;

		int TIMOUT_CHECK_INTERVAL = 15000;

		connector = new NioSocketConnector();

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

		while (true) {
			try {
				connFuture = connector.connect(new InetSocketAddress(HOST, PORT));
				connFuture.awaitUninterruptibly();
				session = connFuture.getSession();
				break;
			} catch (RuntimeIoException e) {
				Thread.sleep(30000);
			}
		}
		
	}
	
	
	public void close() throws Throwable {
		connector.dispose();
	}
	
	public void sendMessage(byte[] msg) {
		if (session != null)
			session.write(msg);
	}
	
	public static byte[] getlogin(){
		return new byte[]{0x67,0x67,0x01,0x00,0x0B,0x00,0x05,0x03,0x54,0x18,(byte) 0x80,0x46,0x62,0x51,0x20,0x00};
	}
	
	public static byte[] getPosition(){
		return new byte[]{0x67,0x67,0x05,0x00,0x20,0x00,0x02,0x53,0x54,(byte)0xF2,(byte)0x88,0x04,0x49,(byte)0xF4,(byte)0xC9,0x0C,0x7B,0x53,(byte)0xD6,(byte)0xA9,0x00,0x00,0x01,(byte) 0xCC,0x00,0x00,0x10,(byte)0xDD,0x00,0x1F,(byte)0xF2,0x00,0x01,0x50,(byte)0xE2,0x27,0x12};
	}
	public static byte[] getHeartBeat(){
		return new byte[]{0x67,0x67,0x03,0x00,0x04,0x00,0x0F,0x00,0x01};
	}
	
	
	public static byte[] getOBD(){
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
	
	 /* 将十六进制字符数组转换为字节数组
     *
     * @param data
     *            十六进制char[]
     * @return byte[]
     * @throws RuntimeException
     *             如果源十六进制字符数组是一个奇怪的长度，将抛出运行时异常
     */
	 public static byte[] decodeHex(String str) {
		    char[] data=str.toCharArray();
	        int len = data.length;
	        if ((len & 0x01) != 0) {
	            throw new RuntimeException("Odd number of characters.");
	        }
	        byte[] out = new byte[len >> 1];
	        // two characters form the hex value.
	        for (int i = 0, j = 0; j < len; i++) {
	            int f = toDigit(data[j], j) << 4;
	            j++;
	            f = f | toDigit(data[j], j);
	            j++;
	            out[i] = (byte) (f & 0xFF);
	        }
	        return out;
	    }
	 
	 protected static int toDigit(char ch, int index) {
	        int digit = Character.digit(ch, 16);
	        if (digit == -1) {
	            throw new RuntimeException("Illegal hexadecimal character " + ch
	                    + " at index " + index);
	        }
	        return digit;
	    }
	 
	 public static void printHexString(byte[] b) {
		 for (int i = 0; i < b.length; i++) { 
			 String hex = Integer.toHexString(b[i] & 0xFF); 
			 if (hex.length() == 1) { 
				 hex = '0' + hex; 
			 } 
			 System.out.print(hex.toUpperCase() ); 
		 } 

	}
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 设置Log4j
				DailyRollingFileAppender appender = new DailyRollingFileAppender();
				appender.setFile("device" + ".log");
				appender.setDatePattern("'.'yyyy-MM-dd");
				appender.setName("ServerAppender");
				PatternLayout layout = new PatternLayout();
				layout.setConversionPattern("%d{dd MMM yyyy HH:mm:ss,SSS} %-4r [%t] %-5p %c %x - %m%n");
				appender.setLayout(layout);
				appender.activateOptions();
				BasicConfigurator.configure(appender);

				final OBDDevice client = new OBDDevice();
				try {
					client.connect();
					client.sendMessage(getlogin());
					Thread.sleep(5000);
					//client.sendMessage(getPosition());
					byte[] bs=decodeHex("6767070088000750E2270800FFFFFFFF02334455660333445566043344556605AA0000000A334455660B334455660C4E2000000DAA0000000E334455660F3344");
					client.sendMessage(bs);
					Thread.sleep(5000);
					bs=decodeHex("556610AAAA000011334455661C334455661F3344556620FFFFFFFF213344556640FFFFFFFF423344556646334455664D334455665C334455665E33445566890000036D8A000000008B00000000");
					client.sendMessage(bs);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				
				
	}

}
