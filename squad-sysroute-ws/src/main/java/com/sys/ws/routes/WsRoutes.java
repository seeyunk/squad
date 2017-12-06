package com.sys.ws.routes;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squad.annotation.SquadRoute;

@SquadRoute( config="squad-sysroute-ws.cfg" )
public class WsRoutes extends RouteBuilder {
	private final static Logger logger = LoggerFactory.getLogger(WsRoutes.class);
	private Gson gson = new GsonBuilder().create();
	int i = 0;
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from( "squadtag://#{redisHost}:#{redisPort}?defaultDb=#{defaultDb}&passwd=#{passwd}&loadOnStartup=true" )
		.to( "squadws://#{wsHost}:#{wsPort}/#{baseUri}?type=#{type}" );
	}
}
