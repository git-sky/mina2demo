/**
 * 
 */
package cn.com.sky.mina2.simulator2;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.springframework.stereotype.Component;

/**
 * @author fss
 * 
 */
@Component
public class TestDecoder extends CumulativeProtocolDecoder {

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		byte[] bytes = new byte[in.limit()];
		in.get(bytes);
		out.write(new String(bytes));
		return false;

	}

}
