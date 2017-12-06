package com.squad.context;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config implements Serializable {
	private static final long serialVersionUID = 6689944721928816556L;
	private String scoutId;
	private String leaderId;
	private final ConcurrentMap<String, InetSocketAddress> scoutAddresses = new ConcurrentHashMap<String, InetSocketAddress>();
	private String squadLibPath;
	private int cliPort;
	private static final String DEFAULT_CONFIG_FILE = "squad.cfg";
	private static final String KEY_SCOUT_ID = "id";
	private static final String KEY_LEADER_ID = "leader";
	private static final String KEY_SQUAD_LIB_PATH = "repository";
	private static final String KEY_SCOUT_ADDRESSES = "cluster";
	private static final String KEY_CLI_PORT = "cli_port";
	
	public static Config load() throws Exception {
		return load( DEFAULT_CONFIG_FILE );
	}
	
	public static Config load( String cfgFile ) throws Exception {
		Properties prop = new Properties();		
		InputStream is = new FileInputStream( cfgFile );
		if ( is != null ) {
			prop.load( is );
		}
		is.close();
		
		Config config = new Config();
		config.setScoutId( prop.getProperty( KEY_SCOUT_ID ).trim() );
		config.setLeaderId( prop.getProperty( KEY_LEADER_ID ).trim() );
		config.setSquadLibPath( prop.getProperty( KEY_SQUAD_LIB_PATH ).trim() );
		config.setCliPort( Integer.parseInt( prop.getProperty( KEY_CLI_PORT ).trim() ) );
		String value = prop.getProperty( KEY_SCOUT_ADDRESSES ).trim();
		String[] addresses = value.split( "," );
		
		if ( addresses.length == 0 ) {
			throw new Exception();
		}
		
		for ( String address : addresses ) {
			address = address.trim();
			Pattern pattern = Pattern.compile( "^(.+)@(.+):(.+)$" );
			Matcher matcher = pattern.matcher( address );
			if ( matcher.matches() ) {
				String scoutId = matcher.group( 1 );
				String host = matcher.group( 2 );
				int port = Integer.parseInt( matcher.group( 3 ) );
				config.putScoutAddresses( scoutId, new InetSocketAddress( host, port ) );
			}
		}
		
		return config;
	}

	public String getScoutId() {
		return this.scoutId;
	}
	public void setScoutId(String scoutId) {
		this.scoutId = scoutId;
	}
	
	public InetSocketAddress getMyScoutAddress() {
		return scoutAddresses.get( this.scoutId );
	}
	
	public InetSocketAddress getDefaultLeaderAddress() {
		return scoutAddresses.get( this.leaderId );
	}
	
	public InetSocketAddress getScoutAddress( String scoutId ) {
		return scoutAddresses.get( scoutId );
	}
	
	public void putScoutAddresses( String scoutId, InetSocketAddress address ) {
		scoutAddresses.put( scoutId, address );
	}
	
	public ConcurrentMap<String, InetSocketAddress> getScoutGroup() {
		return scoutAddresses;
	}
	public String getLeaderId() {
		return this.leaderId;
	}
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
	public String getSquadLibPath() {
		return this.squadLibPath;
	}
	public void setSquadLibPath(String squadLibPath) {
		this.squadLibPath = squadLibPath;
	}
	public int getCliPort() {
		return this.cliPort;
	}
	public void setCliPort(int cliPort) {
		this.cliPort = cliPort;
	}
	
	public boolean isLeader() {
		return this.leaderId.equals( this.scoutId );
	}
	
	
	
}