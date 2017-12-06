import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sys.ws.routes.WsRoutes;

public class RouteTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() throws Exception {
		CamelContext ctx = new DefaultCamelContext();
		ctx.addRoutes( new WsRoutes() );
		ctx.start();
		Thread.currentThread().join();
	}
}
