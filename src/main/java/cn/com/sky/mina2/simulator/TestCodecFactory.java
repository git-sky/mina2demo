/**
 * 
 */
package cn.com.sky.mina2.simulator;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * @author fss
 *
 */

public class TestCodecFactory implements ProtocolCodecFactory {

    private TestDecoder testDecoder;

    private TestEncoder testEncoder;
    
    public TestCodecFactory(TestDecoder testDecoder,TestEncoder testEncoder) {
	this.testDecoder = testDecoder;
	this.testEncoder = testEncoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
	
	return testEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
	
	return testDecoder;
    }

}
