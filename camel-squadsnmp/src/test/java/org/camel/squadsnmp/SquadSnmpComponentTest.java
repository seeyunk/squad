package org.camel.squadsnmp;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SquadSnmpComponentTest extends CamelTestSupport {

    @Test
    public void testSquadSnmp() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
            	from( "direct:in" )
            	.to( "squadsnmp://10.211.55.5:6060?version=v2c&poolSize=4&trapHost=localhost&trapPort=162" );
            }
        };
    }
}
