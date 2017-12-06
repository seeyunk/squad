import static org.junit.Assert.fail;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.spi.Registry;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sys.eam.protocol.SquadCliPipelineFactory;
import com.sys.eam.routes.EamRoute;


public class AlarmRouteTest {
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
	public void test() {
		CamelContext ctx = new DefaultCamelContext();
		try {
			JndiRegistry reg = this.getComponentRegistry( ctx );
			reg.bind( "sys-squad-cli-cpf", new SquadCliPipelineFactory() );
			ctx.addRoutes( new EamRoute() );
			ctx.start();
			
			Thread.currentThread().join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
