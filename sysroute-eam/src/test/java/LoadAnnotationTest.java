import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.spi.Registry;
import org.junit.Test;

import com.squad.annotation.SquadComponent;
import com.sys.eam.routes.EamRoute;


public class LoadAnnotationTest {	
	@Test
	public void test() throws Exception {
		CamelContext ctx = new DefaultCamelContext( new JndiRegistry() );
		ctx.start();
		
		Class<?> clazzPipeline = Class.forName( "com.sys.alm.protocol.AlarmPipelineFactory" );
		SquadComponent squadComponent = null;
		Annotation[] annotations = clazzPipeline.getAnnotations();
		for ( Annotation annotation : annotations ) {
			if ( annotation instanceof SquadComponent ) {
				squadComponent = (SquadComponent)annotation;
				break;
			}
		}
		
		String ctxBeanId = squadComponent.id();
		
		//@@__
		JndiRegistry jndiRegistry = null;
		Registry registry = ctx.getRegistry();
		if (registry instanceof PropertyPlaceholderDelegateRegistry) { 
            final PropertyPlaceholderDelegateRegistry ppdr = (PropertyPlaceholderDelegateRegistry) registry; 
            jndiRegistry = (JndiRegistry)ppdr.getRegistry(); 
        } 
        else { 
        	jndiRegistry = (JndiRegistry)ctx.getRegistry(); 
        }
		
		jndiRegistry.bind( ctxBeanId, clazzPipeline.newInstance() );
		//__@@
		
		Object obj = ctx.getRegistry().lookupByName( "sys-alm-cpf" );
		Class<?> clazzRoute = Class.forName( "com.sys.alm.routes.AlarmRoute" );
		RoutesBuilder rb = (RoutesBuilder)clazzRoute.newInstance();
		ctx.addRoutes( rb );
		
		ProducerTemplate pt = ctx.createProducerTemplate();
		Map<String, String> body = new HashMap<String, String>();
		body.put( EamRoute.ALM_IO_ADDR,"DUMMY_ELEV1" );
		body.put( EamRoute.ALM_STATE, "NORMAL" );
		body.put( EamRoute.ALM_DESC, "dummy elevator status" );
		pt.sendBody( "vm:sys-alm-queue", body );
		//Thread.sleep( 1000L );
		
		body.put( EamRoute.ALM_IO_ADDR,"DUMMY_ELEV2" );
		body.put( EamRoute.ALM_STATE, "NORMAL" );
		body.put( EamRoute.ALM_DESC, "dummy elevator status" );
		pt.sendBody( "vm:sys-alm-queue", body );
		//Thread.sleep( 1000L );
		
		body.put( EamRoute.ALM_IO_ADDR,"DUMMY_ELEV3" );
		body.put( EamRoute.ALM_STATE, "NORMAL" );
		body.put( EamRoute.ALM_DESC, "dummy elevator status" );
		pt.sendBody( "vm:sys-alm-queue", body );
		//Thread.sleep( 10000L );
	}
}
