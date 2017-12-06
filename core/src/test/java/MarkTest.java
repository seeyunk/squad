import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MarkTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() throws Exception {
		byte[] arr = new byte[]{ 1, 2, 3, 4};
		InputStream is = new ByteArrayInputStream( arr );
		is.mark( 0 );
		byte x = (byte)is.read();
		x = (byte)is.read();
		is.reset();
		x = (byte)is.read();
		x = (byte)is.read();
		is.close();
		
	}
}
