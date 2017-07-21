/**
 * 
 */
package cn.com.sky.mina2.simulator;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * @author jy.zhao
 * 
 */
public class MsgEncoder implements MessageEncoder<Object> {
	
	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {

		if (message instanceof String) {

			byte[] body = ((String) message).getBytes("utf-8");
			IoBuffer buffer = IoBuffer.allocate(body.length);
			buffer.put(body);
			buffer.flip();
			out.write(buffer);
		}
	}

}
