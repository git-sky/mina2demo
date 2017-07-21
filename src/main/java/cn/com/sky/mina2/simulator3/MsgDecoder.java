/**
 * 
 */
package cn.com.sky.mina2.simulator3;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jy.zhao
 * 
 *         http://my.oschina.net/ielts0909/blog/92716
 */
public class MsgDecoder implements MessageDecoder {

	static Logger logger = LoggerFactory.getLogger(MsgDecoder.class);

	@Override
	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {

		return MessageDecoderResult.OK;
	}

	@Override
	public MessageDecoderResult decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {

		byte[] bytes = new byte[in.limit()];
		in.get(bytes);
		out.write(new String(bytes));
		return MessageDecoderResult.OK;
	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
	}

}
