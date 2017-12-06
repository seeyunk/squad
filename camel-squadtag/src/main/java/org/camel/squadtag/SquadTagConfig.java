package org.camel.squadtag;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

@UriParams
public class SquadTagConfig implements Cloneable, Serializable {
	private static final long serialVersionUID = -1464561508140277361L;
	private static final Logger logger = LoggerFactory.getLogger( SquadTagConfig.class );
	
	@UriParam( label="producer, consumer", defaultValue="localhost" )
	private String host;
	
	@UriParam( label="producer, consumer", defaultValue="6379" )
	private int port;
	
	@UriParam( label="producer,consumer", defaultValue="0" )
	private int defaultDb;
	
	@UriParam( label="producer,consumer" )
	private String passwd;
	
	@UriParam( label="consumer", defaultValue="true" )
	private boolean loadOnStartup;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		SquadTagConfig config = (SquadTagConfig)super.clone();
		config.host = this.host;
		config.port = this.port;
		config.defaultDb = this.defaultDb;
		config.passwd = this.passwd;
		config.loadOnStartup = this.loadOnStartup;

		return config;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getDefaultDb() {
		return defaultDb;
	}

	public void setDefaultDb(int defaultDb) {
		this.defaultDb = defaultDb;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public boolean getLoadOnStartup() {
		return loadOnStartup;
	}

	public void setLoadOnStartup(boolean loadOnStartup) {
		this.loadOnStartup = loadOnStartup;
	}
	
}
