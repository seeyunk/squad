import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sys.tag.routes.TagRoute;


public class TagRouteTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() throws Exception {
		CamelContext ctx = new DefaultCamelContext();
		
		ctx.addRoutes( new TagRoute() );
		ctx.start();
		
		Thread.currentThread().join();
	}
}
