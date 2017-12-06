package org.camel.squadsnmp;

import java.io.Serializable;

import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;

@UriParams
public class SquadSnmpConfig implements Serializable {

	private static final long serialVersionUID = -8344119600232699722L;
	public enum VERSION {
		v1,
		v2c,
		v3
	};
	
	@UriParam( label="producer, consumer", defaultValue="localhost" ) 
	private String host;
	
	@UriParam( label="producer, consumer", defaultValue="1123" )
	private int port;
	
	@UriParam( label="producer, consumer", defaultValue="v2c" )
	VERSION version;
	
	@UriParam( label="producer, consumer", defaultValue="4" )
	private int poolSize;
	
	@UriParam( label="producer, consumer" )
	private String trapHost;
	
	@UriParam( label="producer, consumer", defaultValue="162" )
	private int trapPort;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		SquadSnmpConfig config = (SquadSnmpConfig)super.clone();
		config.host = this.host;
		config.port = this.port;
		config.version = this.version;
		config.poolSize = this.poolSize;
		config.trapHost = this.trapHost;
		config.trapPort = this.trapPort;
		
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


	public VERSION getVersion() {
		return version;
	}


	public void setVersion(VERSION version) {
		this.version = version;
	}


	public int getPoolSize() {
		return poolSize;
	}


	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public String getTrapHost() {
		return trapHost;
	}


	public void setTrapHost(String trapHost) {
		this.trapHost = trapHost;
	}
	
	public int getTrapPort() {
		return trapPort;
	}


	public void setTrapPort(int trapPort) {
		this.trapPort = trapPort;
	}
}
