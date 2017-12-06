import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.context.Config;
import com.squad.context.SquadContext;


public class LoadRouteTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() throws Exception {
		//String configPath = "D:\\works\\workspace_gant\\squad\\core\\src\\main\\resources\\squad.cfg";
		String configPath = "/home/seeyunk/works/workspace_gant/squad/core/src/main/resources/squad.cfg";
		Config config = Config.load( configPath );
		
		CamelContext ctx2 = new DefaultCamelContext();
		
		/*
		ctx2.addRoutes( new TestRouteBuilder() );
		ctx2.start();
		*/
		SquadContext ctx = new SquadContext( config );
		//ctx.startCamelCtx();
		
		int x = 0;
		x = 1;
		
		//Thread.currentThread().join();
	
		/*
		SquadCamelContext camel = new SquadCamelContext( ctx );
		camel.start();
		*/
		//Thread.sleep( 10000L );
	}
}
