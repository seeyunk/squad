import java.net.URL;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.spi.Registry;
import org.junit.Test;

import dvc.guardtec.door.entity.Header;
import dvc.guardtec.door.handler.GuardtecDoorPipelineFactory;
import dvc.guardtec.door.routes.GuardtecDoorRoute;


public class BufferTest {
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
		JndiRegistry reg = ctx.getRegistry( JndiRegistry.class );
		//JndiRegistry reg = this.getComponentRegistry( ctx );
		reg.bind( "device-guradtec-door-cpf", new GuardtecDoorPipelineFactory() );
		
		ctx.addRoutes( new GuardtecDoorRoute() );
		ctx.start();
		
		Thread.currentThread().join();
	}
}
