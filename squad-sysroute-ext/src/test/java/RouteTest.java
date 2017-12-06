
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.spi.Registry;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sys.ext.protocol.ExtPipelineFactory;
import com.sys.ext.routes.ExtRoute;


public class RouteTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public JndiRegistry getComponentRegistry( CamelContext ctx ) {
		JndiRegistry jndiRegistry = null;
		Registry registry = ctx.getRegistry();
		if ( registry instanceof PropertyPlaceholderDelegateRegistry ) { 
	        final PropertyPlaceholderDelegateRegistry ppdr = (PropertyPlaceholderDelegateRegistry) registry; 
	        jndiRegistry = (JndiRegistry)ppdr.getRegistry(); 
	    } 
	    else { 
	    	jndiRegistry = (JndiRegistry)ctx.getRegistry(); 
	    }
		
		return jndiRegistry;
	}
	
	@Test
	public void test() throws Exception {
		CamelContext ctx = new DefaultCamelContext();
		//JndiRegistry reg = this.getComponentRegistry( ctx );
		JndiRegistry reg = ctx.getRegistry( JndiRegistry.class );
		reg.bind( "sys-ext-spf", new ExtPipelineFactory() );
		
		Object o = ctx.getRegistry().lookupByName( "sys-ext-spf" );
		ctx.addRoutes( new ExtRoute() );
		ctx.start();
		int x = 0;
		x = 1;
	}
}
