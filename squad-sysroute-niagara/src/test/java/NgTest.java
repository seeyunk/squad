import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sys.ng.handler.NgPipelineFactory;
import com.sys.ng.routes.NgRoute;


public class NgTest {
	private static final Logger logger = LoggerFactory.getLogger( NgTest.class );

	@Test
	public void test() throws Exception {
		CamelContext ctx = new DefaultCamelContext();
		JndiRegistry reg = ctx.getRegistry( JndiRegistry.class );
		reg.bind( "ng-spf", new NgPipelineFactory() );
		
		ctx.addRoutes( new NgRoute() );
		ctx.start();
		
		Thread.currentThread().join();
	}	
}
