import java.util.Properties;

import org.junit.Test;


public class BciTest {
	
	public Properties loadRouteConfig( String config ) throws Exception {
		Properties prop = new Properties();
		prop.load( this.getClass().getResourceAsStream( config ) );
		return prop;
	}
	
	@SuppressWarnings("unchecked")
	public byte[] setConfigParams( final String className, final String config ) throws Exception {
		/*
		final ClassNode cn = new ClassNode();
		ClassReader cr = new ClassReader( className );
		cr.accept( cn, ClassReader.SKIP_DEBUG );
		
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
								prop = BciTest.this.loadRouteConfig( config );
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
		*/
		
		return null;
	}
	
	@Test
	public void test() throws Exception {
		/*
		Class<?> clazz = Class.forName( "com.sys.alm.routes.AlarmRoute" );
		SquadRoute squadRoute = null;
		Annotation[] annotations = clazz.getAnnotations();
		for ( Annotation annotation : annotations ) {
			if ( annotation instanceof SquadRoute ) {
				squadRoute = (SquadRoute)annotation;
				break;
			}
		}
		
		final String config = squadRoute.config();
		String className = clazz.getCanonicalName();
		className = className.replace( ".",  "/" );
		
		byte[] classBuffer = this.setConfigParams( className, config );
		ClassReader cr = new ClassReader( classBuffer );
		String s = cr.getClassName();
		cr.accept( new ClassVisitor(Opcodes.ASM4 ) {

			@Override
			public MethodVisitor visitMethod(int access, String name,
					String desc, String signature, String[] exceptions) {
				// TODO Auto-generated method stub
				return new MethodVisitor( Opcodes.ASM4 ) {

					@Override
					public void visitLdcInsn(Object cst) {
						// TODO Auto-generated method stub
						if ( cst instanceof String ) {
							String oldString = (String)cst;
							System.out.println( oldString );
						}
						super.visitLdcInsn( cst );
					}
					
				};
			}
		}, ClassReader.SKIP_DEBUG );
		*/
	}
}
