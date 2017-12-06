package com.squad.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class SquadClassLoader extends URLClassLoader {
	private final String jarPath;
	
	public SquadClassLoader( URL[] urls, String jarPath ) throws Exception {
		super( urls );
		this.jarPath = jarPath;
	}
	
	public void addResource( URL url ) {
		this.addURL( url );
	}
	
	@SuppressWarnings("unchecked")
	public String getConfigFileName( AnnotationNode annotationNode ) {
		String configFile = null;
		List<String> values = annotationNode.values;
		for ( int n = 0; n < values.size(); n++ ) {
			if ( "config".equals( values.get( n ) ) ) {
				configFile = values.get( n + 1 );
				break;
			}
		}
		
		return configFile;
	}
	
	@SuppressWarnings("unchecked")
	public byte[] applySquadRouteConfig( ClassNode cn, final String configPath ) throws Exception {
		List<MethodNode> methods = (List<MethodNode>)cn.methods;
		for ( MethodNode method : methods ) {
			final InsnList insnList = method.instructions;
			Iterator<?> i = insnList.iterator();
			for ( ; i.hasNext(); ) {
				final AbstractInsnNode oldNode = (AbstractInsnNode)i.next();
				oldNode.accept( new MethodVisitor( Opcodes.ASM4 ) {

					@Override
					public void visitLdcInsn(Object cst) {
						// TODO Auto-generated method stub
						if ( cst instanceof String ) {
							String oldString = (String)cst;
							Properties prop;
							try {
								prop = FileUtil.loadProperties( configPath );
								Enumeration<Object> keys = prop.keys();
								for ( ; keys.hasMoreElements(); ) {
									String key = (String)keys.nextElement();
									String value = prop.getProperty( key ).trim();
									String pattern = String.format( "#{%s}", key.trim() );
									oldString = oldString.replace( pattern, value );
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							finally {
								cst = oldString;
								insnList.set( oldNode, new LdcInsnNode( cst ) );
							}
						}
						super.visitLdcInsn( cst );
					}
					
				} );
			}
		}
		
		ClassWriter cw = new ClassWriter( ClassWriter.COMPUTE_MAXS );
		cn.accept( cw );
		
		return cw.toByteArray();
	}
	
	@SuppressWarnings("unchecked")
	private Class<?> loadClassFromServiceJar( String name, boolean resolve ) throws Exception {
		Class<?> clazz = null;
		final InputStream jarFileStream = new FileInputStream( this.jarPath );
		JarInputStream jis = new JarInputStream( jarFileStream );
		CodeSource code = new CodeSource( null, (Certificate[])null );
		Permissions permissions = new Permissions();
		permissions.add( new AllPermission() );
		ProtectionDomain pd = new ProtectionDomain( code, permissions );
		
		for ( JarEntry entry = jis.getNextJarEntry(); entry != null; entry = jis.getNextJarEntry() ) {
			String entryName = entry.getName();
			if ( entryName.equals( name.replace( ".", "/" ).concat( ".class" ) ) ) {
				final ClassNode cn = new ClassNode();
				ClassReader cr = new ClassReader( jis );
				cr.accept( cn, ClassReader.SKIP_DEBUG );
				
				byte[] classBuffer = cr.b;
				List<AnnotationNode> annotationNodes = cn.visibleAnnotations;
				if ( annotationNodes != null ) {
					for ( AnnotationNode annotationNode : annotationNodes ) {
						String desc = annotationNode.desc;
						if ( desc.equals( "Lcom/squad/annotation/SquadRoute;" ) ) {
							String configFile = this.getConfigFileName( annotationNode );
							String configPath = new File( this.jarPath ).getParent() + File.separator + configFile;
							classBuffer = this.applySquadRouteConfig( cn, configPath );
						}
					}
				}
				
				clazz = this.defineClass( name , classBuffer, 0, classBuffer.length, pd );
				if ( resolve ) {
					this.resolveClass( clazz );
				}
			}
		}
		
		jis.close();
		jarFileStream.close();
		
		return clazz;
	}
	
	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		// TODO Auto-generated method stub
		Class<?> clazz = this.findLoadedClass( name );
		try {
			clazz = ClassLoader.getSystemClassLoader().loadClass( name );
		}
		catch( Exception e ) {
			try {
				clazz = this.findClass( name );
			}
			catch( Exception e2 ) {
				try {
					clazz = this.loadClassFromServiceJar( name , resolve );
				}
				catch( Exception e3 ) {
					e3.printStackTrace();
					throw new ClassNotFoundException();
				}
			}
		}
		
		return clazz;
	}
}
