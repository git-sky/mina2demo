/**
 * 
 */
package cn.com.sky.mina2.simulator;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.springframework.stereotype.Component;

/**
 * @author fss
 * 
 */
@Component
public class TestEncoder extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception {
		if (message instanceof String) {

			byte[] body = ((String) message).getBytes("utf-8");
			IoBuffer buffer = IoBuffer.allocate(body.length);
			buffer.put(body);
			buffer.flip();
			out.write(buffer);
		}

	}
}
