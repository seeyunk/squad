package com.squad.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.PropertyPlaceholderDelegateRegistry;
import org.apache.camel.spi.Registry;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import com.squad.annotation.SquadComponent;
import com.squad.annotation.SquadRoute;
import com.squad.manager.IServiceResultHandler;
import com.squad.protobuf.packet.Squad.ServiceState;
import com.squad.util.FileUtil;
import com.squad.util.SquadClassLoader;


public class SquadCamelContext extends DefaultCamelContext {
	private final SquadClassLoader classLoader;
	private String targetScoutId = null;
	public static final String KEY_TARGET_SCOUT = "target";
	public static final String KEY_AUTOSTART = "autostart";
	public IServiceResultHandler resHandler = null;
	private String serviceId;
	
	public SquadCamelContext( SquadClassLoader classLoader, String serviceId, IServiceResultHandler resHandler ) {
		super( new JndiRegistry() );
		this.disableJMX();
		this.classLoader = classLoader;
		this.resHandler = resHandler;
		this.serviceId = serviceId;
	}
	
	public JndiRegistry getComponentRegistry() {
		JndiRegistry jndiRegistry = null;
		Registry registry = this.getRegistry();
		if ( registry instanceof PropertyPlaceholderDelegateRegistry ) { 
	        final PropertyPlaceholderDelegateRegistry ppdr = (PropertyPlaceholderDelegateRegistry) registry; 
	        jndiRegistry = (JndiRegistry)ppdr.getRegistry(); 
	    } 
	    else { 
	    	jndiRegistry = (JndiRegistry)this.getRegistry(); 
	    }
		
		return jndiRegistry;
	}
	
	public void loadSquadComponentClass( Class<?> clazz, SquadComponent squadComponent ) throws Exception {
		String id = squadComponent.id();
		if ( id == null || id.isEmpty() ) {
			throw new NullPointerException( "Component id must not be null..." );
		}
		
		JndiRegistry registry = this.getComponentRegistry();
		registry.bind( squadComponent.id().trim(), clazz.newInstance() );
	}
	
	@SuppressWarnings("unchecked")
	public static SquadCamelContext newSquadCamelContext( URL[] extLibs, String jarPath, String serviceId, IServiceResultHandler resHandler ) throws Exception {
		SquadClassLoader classLoader = new SquadClassLoader( extLibs, jarPath );
		SquadCamelContext ctx= new SquadCamelContext( classLoader, serviceId, resHandler );
		
		List<Class<?>> routes = new ArrayList<Class<?>>();
		InputStream is = new FileInputStream( jarPath );
		JarInputStream jis = new JarInputStream( is );
		for ( JarEntry entry = jis.getNextJarEntry(); entry != null; entry = jis.getNextJarEntry() ) {
			String entryName = entry.getName();
			if ( entry.isDirectory() || entryName.matches( ".*\\$(.+)" ) ) {
				continue;
			}
			else if ( entryName.matches( "(?i)(.+)\\.class" ) ) {
				final ClassNode cn = new ClassNode();
				ClassReader cr = new ClassReader( jis );
				cr.accept( cn, ClassReader.SKIP_DEBUG );
				List<AnnotationNode> annotationNodes = cn.visibleAnnotations;
				if ( annotationNodes != null ) {
					for ( AnnotationNode annotationNode : annotationNodes ) {
						String desc = annotationNode.desc;
						String className = entryName.replace( "/", "." ).substring( 0, entryName.lastIndexOf( "." ) );
						if ( desc.equals( "Lcom/squad/annotation/SquadRoute;" ) ) {
							Class<?> route = classLoader.loadClass( className );
							String configFile = route.getAnnotation( SquadRoute.class ).config();
							String configPath = new File( jarPath ).getParent() + File.separator + configFile;
							Properties props = FileUtil.loadProperties( configPath );
							
							ctx.setTargetScoutId( (String)props.get( KEY_TARGET_SCOUT ) );
							Boolean autoStartUp = props.get( KEY_AUTOSTART ) == null ? true : Boolean.parseBoolean( (String)props.get( KEY_AUTOSTART ) );
							ctx.setAutoStartup( autoStartUp );
							routes.add( route );
						}
						else if ( desc.equals( "Lcom/squad/annotation/SquadComponent;" ) ) {
							Class<?> clazz = classLoader.loadClass( className );
							SquadComponent squadCompoent = clazz.getAnnotation( SquadComponent.class );
							ctx.loadSquadComponentClass( clazz, squadCompoent );
						}
					}
				}
			}
			else {
				//TODO::add resourced:
				
			}
		}
		
		jis.close();
		is.close();
		
		ctx.setApplicationContextClassLoader( classLoader );
		for ( int n = 0; n < routes.size(); n++ ) {
			RoutesBuilder rb = (RoutesBuilder)routes.get( n ).newInstance();
			ctx.addRoutes( rb );
		}
		return ctx;
	}
	
	public void unload() throws Exception {
		this.stop();
		this.classLoader.close();
	}

	public String getTargetScoutId() {
		return targetScoutId;
	}

	public void setTargetScoutId(String targetScoutId) {
		this.targetScoutId = targetScoutId;
	}
	
	public void start( boolean forcedRun ) throws Exception {
		// TODO Auto-generated method stub
		if ( this.resHandler.handleServiceResult( this.serviceId, ServiceState.STARTED, forcedRun ) &&
			!this.isStarting() && 
			!this.isStarted() ) {
			super.start();
		}
	}

	public void stop( boolean forcedRun ) throws Exception {
		// TODO Auto-generated method stub
		if ( this.resHandler.handleServiceResult( this.serviceId, ServiceState.STOPPED, forcedRun ) &&
			!this.isStoppingOrStopped()) {
			super.stop();
		}
	}

	public void suspend( boolean forcedRun ) throws Exception {
		// TODO Auto-generated method stub
		if ( this.resHandler.handleServiceResult( this.serviceId, ServiceState.SUSPENDED, forcedRun ) &&
			!this.isSuspended() &&
			!this.isSuspending() &&
			this.isStarted() ) {
			super.suspend();
		}
	}

	public void resume( boolean forcedRun ) throws Exception {
		// TODO Auto-generated method stub
		if ( this.resHandler.handleServiceResult( this.serviceId, ServiceState.STARTED, forcedRun ) &&
			!this.isStarted() &&
			!this.isStarting() ) {
			super.resume();
		}
	}

	public void shutdown( boolean forcedRun ) throws Exception {
		// TODO Auto-generated method stub
		if ( this.resHandler.handleServiceResult( this.serviceId, ServiceState.SHUTDOWNED, forcedRun ) ) {
			super.shutdown();
		}
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Override
	public void start() throws Exception {
		// TODO Auto-generated method stub
		this.start( false );
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		this.stop( false );
	}

	@Override
	public void suspend() throws Exception {
		// TODO Auto-generated method stub
		this.suspend( false );
	}

	@Override
	public void resume() throws Exception {
		// TODO Auto-generated method stub
		this.resume( false );
	}

	@Override
	public void shutdown() throws Exception {
		// TODO Auto-generated method stub
		super.shutdown();
	}
}
