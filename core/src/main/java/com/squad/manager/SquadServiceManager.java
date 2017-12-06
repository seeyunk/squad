package com.squad.manager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.WeakHashMap;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.squad.context.SquadCamelContext;
import com.squad.context.SquadContext;
import com.squad.entity.Message;
import com.squad.looper.AsyncLooper;
import com.squad.looper.AsyncLooper.MSG;
import com.squad.protobuf.packet.Squad.NotifyServicesPacket;
import com.squad.protobuf.packet.Squad.Packet;
import com.squad.protobuf.packet.Squad.Packet.Type;
import com.squad.protobuf.packet.Squad.ControlServicePacket;
import com.squad.protobuf.packet.Squad.RequestServicesPacket;
import com.squad.protobuf.packet.Squad.ScoutState;
import com.squad.protobuf.packet.Squad.ServicePacket;
import com.squad.protobuf.packet.Squad.ServiceState;
import com.squad.rpc.client.ScoutClient;
import com.squad.util.FileUtil;
import com.squad.util.ZipUtil;

public class SquadServiceManager extends AsyncLooper implements IServiceResultHandler {
	private static final Logger logger = LoggerFactory.getLogger( SquadServiceManager.class );
	private final SquadContext ctx;
	private ConcurrentMap<String, ServicePacket> requestedServices = new ConcurrentHashMap<String, ServicePacket>();
	private WeakHashMap<String, SquadCamelContext> camelCtxs = new WeakHashMap<String, SquadCamelContext>();
	private final SquadNodeManager nodeManager;
	private final ScoutClient scoutClient;
	
	public SquadServiceManager( SquadContext ctx ) {
		this.ctx = ctx;
		this.scoutClient = this.ctx.getClient();
		
		String libPath = this.ctx.getConfig().getSquadLibPath();
		this.unzipAllServices( new File( libPath ) );
		
		this.nodeManager = ctx.getNodeManager();
	}
		
	public Iterator<String> getAllServiceIds() {
		return this.camelCtxs.keySet().iterator();
	}
	
	public Iterator<String> getAllServiceIdsByScoutId( String scoutId ) {
		Set<String> serviceIds = new HashSet<String>();
		Iterator<SquadCamelContext> i = this.camelCtxs.values().iterator();
		for ( ; i.hasNext(); ) {
			SquadCamelContext ctx = i.next();
			String targetScoutId = ctx.getTargetScoutId();
			if ( targetScoutId != null &&
				scoutId.equals( ctx.getTargetScoutId() ) ) {
				serviceIds.add( ctx.getServiceId() );
			}
		}
		
		return serviceIds.iterator();
	}
	
	public void startAllServices( boolean forcedRun ) {
		Iterator<SquadCamelContext> i = this.camelCtxs.values().iterator();
		for ( ; i.hasNext(); ) {
			SquadCamelContext camelCtx = i.next();
			try {
				camelCtx.start( forcedRun );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void startService( String serviceId, boolean forcedRun ) {
		try {
			SquadCamelContext camelCtx = this.camelCtxs.get( serviceId );
			camelCtx.start( forcedRun );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Set<String> getServiceDirectories() {	
		Set<String> serviceDirectories = new HashSet<String>();
		File libDir = new File( this.ctx.getConfig().getSquadLibPath() );
		for ( File file : libDir.listFiles() ) {
			if ( file != null && file.isDirectory() ) {
				final String fileName = file.getName();
				serviceDirectories.add( fileName );
			}
		}
		
		return serviceDirectories;
	}
	
	/*
	public Set<String> getMetaFileNames() {
		Set<String> names = new HashSet<String>();
		File libDir = new File( this.ctx.getConfig().getSquadLibPath() );
		for ( File file : libDir.listFiles() ) {
			String fileName = file.getName();
			if ( fileName.matches( "(?i)(.+)\\.managed" ) ) {
				names.add( fileName );
			}
		}
		
		return names;
	}
	*/
	
	public URL[] getDependencyUrls( String extLibPath ) {
		List<URL> urls = new ArrayList<URL>();
		File libDir = new File( extLibPath );
		File[] files = libDir.listFiles();
		if ( files != null ) {
			for ( File file : files ) {
				String jarPath = file.getAbsolutePath();
				try {
					urls.add( new File( jarPath ).toURI().toURL() );
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return urls.toArray( new URL[]{} );
	}
		
	public void updateService( final String serviceId, SquadCamelContext camelCtx ) throws Exception {
		if ( this.camelCtxs.containsKey( serviceId ) ) {
			camelCtx.stop( false );
			this.camelCtxs.remove( serviceId );
		}
		this.camelCtxs.put( serviceId, camelCtx );
	}
	
	public void loadService( String jarPath ) {
		this.loadService( new File( jarPath ) );
	}
	
	public String loadService( File jarFile ) {
		String serviceId = null;
		try {
			serviceId = new File( jarFile.getParent() ).getName();
			URL[] urls = this.getDependencyUrls( jarFile.getParent() + File.separator + "lib" );
			SquadCamelContext scc = SquadCamelContext.newSquadCamelContext( urls, jarFile.getAbsolutePath(), serviceId, this );
			this.updateService( serviceId, scc );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return serviceId;
	}
	
	public void loadAllServices() {
		String libPath = this.ctx.getConfig().getSquadLibPath();
		this.loadAllServices( new File( libPath ) );
	}
	
	public void loadAllServices( File path ) {
		File[] files = path.listFiles();
		for ( File file : files ) {
			if ( file.isDirectory() && !file.getName().equals( "lib" ) ) {
				this.loadAllServices( file );
			}
			else {
				String fileName = file.getName();
				String ext = fileName.substring( fileName.lastIndexOf( "." ) + 1 ).toLowerCase();
				if ( ext.equals( "jar" ) ) {
					this.loadService( file );
				}
			}
		}
	}
	
	public String unzipService( File file ) throws Exception {
		String jarPath = null;
		String zipName = file.getName();
		
		Pattern pattern = Pattern.compile( "(?i)(.+)\\.zip" );
		Matcher matcher = pattern.matcher( zipName );
		if ( file.isFile() && matcher.find() ) {
			String serviceRootDir = matcher.group( 1 );
			String dirPath = file.getParent() + File.separator + serviceRootDir;
			File serviceDir = new File( dirPath );
			if ( serviceDir.exists() ) {
				FileUtil.delete( serviceDir );
			}
			
			ZipFile zipFile = new ZipFile( file.getAbsolutePath() );
			zipFile.extractAll( file.getParentFile().getAbsolutePath() );
			for ( Object header : zipFile.getFileHeaders() ) {
				FileHeader fh = (FileHeader)header;
				String fileName = fh.getFileName();
				if ( fileName.contains( "lib" ) ) {
					continue;
				}
				
				if ( fileName.contains( "jar" ) ) {
					jarPath = this.ctx.getConfig().getSquadLibPath() + File.separator + fileName;
					break;
				}
			}
			
			zipFile.getFile().delete();
		}
		
		return jarPath;
	}
	
	public void unzipAllServices( File libRoot ) {
		if ( !libRoot.isDirectory() ) {
			logger.info( "Invalid SQUAD lib path" );
		}
		
		File[] files = libRoot.listFiles();
		for ( File file : files ) {
			try {
				this.unzipService( file );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
	}
	
	public String newService( String metaFilePath ) throws Exception {
		String serviceId = null;
		String jarPath = this.unzipService( new File( metaFilePath ) );
		if ( jarPath != null ) {
			serviceId = this.loadService( new File( jarPath ) );
			logger.info( "New service loaded from [{}]", metaFilePath );
		}
		
		return serviceId;
	}

	@Override
	public boolean handleServiceResult( String serviceId, ServiceState serviceState, boolean forcedRun ) throws Exception {
		// TODO Auto-generated method stub
		String scoutId = this.ctx.getConfig().getScoutId();
		String nodeTargetId = this.nodeManager.getData( this.nodeManager.getServiceTargetNode( serviceId ), String.class );
		if ( nodeTargetId == null ) {
			nodeTargetId = this.camelCtxs.get( serviceId ).getTargetScoutId();
		}
		
		if ( nodeTargetId.equals( scoutId ) ||
			nodeTargetId.equals( SquadNodeManager.NODE_SERVICE_TARGET_ALL_VALUE ) ||
			forcedRun ) {
			this.nodeManager.setServiceTarget( serviceId, scoutId );
			this.nodeManager.changeServiceState( scoutId, serviceId, serviceState);
			logger.info( "[SERVICE]::[{}]::[{}]", serviceId, serviceState );
			return true;
		}
		
		logger.info( "[SERVICE]::[{}]::[{}]_NOT_AUTHORIZED", scoutId, serviceId );
		return false;		
	}
	
	public void relayServices( String oldScoutId, String newScoutId ) {
		String oldServiceStateNode = this.nodeManager.getServiceStateNode( oldScoutId, "" );
		Iterator<Entry<String, ByteString>> i = this.nodeManager.readAll( oldServiceStateNode ).iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, ByteString> entry = i.next();
			ServiceState ss = ServiceState.valueOf( entry.getValue().toStringUtf8() );
			String node = entry.getKey();
			String serviceId = node.substring( node.lastIndexOf( SquadNodeManager.NODE_DELIMITER ) + 1 );
			String curTargetId = this.nodeManager.getServiceTarget( serviceId );
			if ( !ss.equals( ServiceState.STARTED ) &&
				this.nodeManager.getScoutState( curTargetId ) == ScoutState.INACTIVATED ) {
				SquadCamelContext camelCtx = this.camelCtxs.get( serviceId );
				try {
					camelCtx.start( true );
					this.nodeManager.changeServiceState( oldScoutId, serviceId, ServiceState.STOPPED );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void clear() {
		Iterator<SquadCamelContext> i = this.camelCtxs.values().iterator();
		for ( ; i.hasNext(); ) {
			SquadCamelContext camelCtx = i.next();
			try {
				camelCtx.shutdown();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void sendExamineServices( String targetId ) {
		String selfId = this.ctx.getConfig().getScoutId();
		Iterator<String> i = this.getServiceDirectories().iterator();
		
		NotifyServicesPacket.Builder nspBuilder = NotifyServicesPacket.newBuilder();
		nspBuilder.setScoutId( selfId );
		for ( ; i.hasNext(); ) {
			String fileName = i.next();
			ServicePacket servicePacket = ServicePacket.newBuilder()
										.setFileName( fileName )
										.build();
			nspBuilder.addServicePacket( servicePacket );
		}
		
		Packet packet = Packet.newBuilder()
						.setType( Type.EXAMINE_SERVICES )
						.setNotifyServicesPacket( nspBuilder )
						.addTargetId( targetId )
						.build();
		logger.debug( "[SEND]::EXAMINE_SERVICES::[{}]", packet.toString() );
		this.scoutClient.write( packet );
	}
	
	public void handleExamineServices( NotifyServicesPacket nsp ) {
		logger.debug( "[RECV]::EXAMINE_SERVICES::[{}]", nsp.toString() );
		String selfId = this.ctx.getConfig().getScoutId();
		String targetId = nsp.getScoutId();
		
		Set<String> names = this.getServiceDirectories();
		List<ServicePacket> recvList = nsp.getServicePacketList();
		final List<ServicePacket> results = new ArrayList<ServicePacket>();
		for ( ServicePacket sp : recvList ) {
			String fileName = sp.getFileName();
			if ( !names.remove( fileName ) 
					&& requestedServices.get( fileName ) == null ) {
				results.add( sp );
				this.requestedServices.put( fileName, sp );
				logger.debug( "{} is prepared to request to[{}]...", sp.getFileName(), targetId );
			}
		}
		
		if ( results.size() > 0 ) {
			RequestServicesPacket rsp = RequestServicesPacket.newBuilder()
										.setScoutId( selfId )
										.addAllServicePacket( requestedServices.values() )
										.build();
			Packet packet = Packet.newBuilder()
							.setType( Type.REQUEST_SERVICES )
							.addTargetId( targetId )
							.setRequestServicesPacket( rsp )
							.build();
			
			this.scoutClient.write( packet );
		}
	}
	
	public void handleRequestServicesRecv( RequestServicesPacket rsp ) {
		logger.debug( "[RECV]::REQUEST_SERVICES" );
		List<String> targetIds = new ArrayList<String>();
		targetIds.add( rsp.getScoutId() );
		
		String libPath = this.ctx.getConfig().getSquadLibPath();
		List<ServicePacket> recvList = rsp.getServicePacketList();
		for ( ServicePacket sp : recvList ) {
			String servicePath = libPath + File.separator + sp.getFileName();
			File file;
			try {
				file = FileUtil.zip( new File( servicePath ) );
				this.scoutClient.syncServiceFile( targetIds, file.getName() );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void handleNewServiceFound( String metaFilePath ) {
		try {
			String serviceId = this.newService( metaFilePath );
			this.startService( serviceId, false );
			File file = new File( metaFilePath );
			String managedFile = file.getName() + ".managed";
			this.requestedServices.remove( managedFile );
			
			logger.debug( "Remain services to sync::count::[{}]", this.requestedServices.size() );
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private void handleControlService( String sourceId, String serviceId, ServiceState serviceState ) {
		try {
			SquadCamelContext ctx = this.camelCtxs.get( serviceId );
			if ( serviceState.equals( ServiceState.STARTED ) ) {
				ctx.start( true );
			}
			else if ( serviceState.equals( ServiceState.STOPPED ) ) {
				ctx.stop( true );
			}
			else if ( serviceState.equals( ServiceState.SUSPENDED ) ) {
				ctx.suspend( true );
			}
			else if ( serviceState.equals( ServiceState.SHUTDOWNED ) ) {
				ctx.shutdown( true );
			}
			
			/*
			ControlServicePacket csp = ControlServicePacket.newBuilder()
									.setServiceId( serviceId )
									.setServiceState( serviceState )
									.build();
			Packet packet = Packet.newBuilder()
							.setType( Type.ACK_CONTROL_SERVICE )
							.addTargetId( sourceId )
							.setControlServicePacket( csp )
							.build();
			this.scoutClient.write( packet );
			*/
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	private void handleAckControlService( String serviceId, ServiceState serviceState ) {
		try {
			SquadCamelContext ctx = this.camelCtxs.get( serviceId );
			if ( serviceState.equals( ServiceState.STARTED ) ) {
				ctx.stop( true );
			}
			else {
				ctx.start();
			}
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean handleMessage(Message message) {
		// TODO Auto-generated method stub
		if ( message.getMessage() == MSG.HANDLE_EXAMINE_SERVICES ) {
			NotifyServicesPacket nsp = (NotifyServicesPacket)message.getParams()[0];
			this.handleExamineServices( nsp );
			return true;
		}
		else if ( message.getMessage() == MSG.REQUEST_SERVICES_RECV ) {
			RequestServicesPacket rsp = (RequestServicesPacket)message.getParams()[0];
			this.handleRequestServicesRecv( rsp );
			return true;
		}
		else if ( message.getMessage() == MSG.NEW_SERVICE_FOUND ) {
			this.handleNewServiceFound( (String)message.getParams()[0] );
			return true;
		}
		else if ( message.getMessage() == MSG.CONTROL_SERVICE ) {
			String sourceId = (String)message.getParams()[0];
			String serviceId = (String)message.getParams()[1];
			ServiceState serviceState = (ServiceState)message.getParams()[2];
			this.handleControlService( sourceId, serviceId, serviceState );
			return true;
		}
		else if ( message.getMessage() == MSG.ACK_CONTROL_SERVICE ) {
			String serviceId = (String)message.getParams()[0];
			ServiceState serviceState = (ServiceState)message.getParams()[1];
			this.handleAckControlService( serviceId, serviceState );
			return true;
		}
		return false;
	}
}
