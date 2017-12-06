package org.camel.squadsnmp;

import org.camel.squadsnmp.SquadSnmpConfig.VERSION;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.agent.mo.MOAccessImpl;

import com.hsnc.squad.snmp.SnmpAgent;

public class SnmpTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void test() throws Exception {
		/*
		TestAgent testAgent = new TestAgent(new File("SNMP4JTestAgentBC.cfg"), new File("SNMP4JTestAgentConfig.cfg"));
        testAgent.run();
        
        testAgent.test();
        */
		SquadSnmpConfig config = new SquadSnmpConfig();
		config.setHost( "10.211.55.5" );
		config.setPort( 6060 );
		config.setPoolSize( 4 );
		config.setVersion( VERSION.v2c );
		config.setTrapHost( "localhost" );
		config.setTrapPort( 1162 );
		
		SnmpAgent agent = new SnmpAgent( config);
		agent.run();
		
		agent.registerMO( "1.3.6.1.4.1.43024.0.0.0.0", "1" );
		agent.registerMO( "1.3.6.1.4.1.43024.0.0.1.0", "2" );
		agent.registerMO( "1.3.6.1.4.1.43024.0.0.2.0", "3" );
		agent.registerMO( "1.3.6.1.4.1.43024.0.0.3.0", "4" );
		agent.updateMO( "1.3.6.1.4.1.43024.0.0.3.0", "5" );
		//agent.registerMO( "1.3.6.1.4.1.43024.0", "point1" );
		//agent.registerMO( "1.3.6.1.4.1.43024.0", "point2" );
		
        Thread.currentThread().join();
       // testAgent1.sendTrap(); // ADDED
       //testAgent1.sendColdStartNotification();
        
		
		int x = 0;
		x = 1;
	}
}
