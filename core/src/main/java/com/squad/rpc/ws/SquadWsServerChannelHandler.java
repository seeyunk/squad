package com.squad.rpc.ws;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.context.ISquadContext;
import com.squad.util.AsyncFileWriter;

@Deprecated
public class SquadWsServerChannelHandler extends SimpleChannelUpstreamHandler {
	private static Logger logger = LoggerFactory.getLogger( SquadWsServerChannelHandler.class );
	private final ChannelGroup channelGroup = new DefaultChannelGroup( "squad-ws-channels" );
	private final ISquadContext ctx;
	private static final String WEB_SOCKET_CONTEXT = "/squad";
	private WebSocketServerHandshaker handshaker;
	private AsyncFileWriter writer = null;
	
	public enum WS_STATE {
		BEFORE_INITIALIZE,
		INITIALIZED,
		CLOSED
	}
	
	public SquadWsServerChannelHandler( ISquadContext ctx ) {
		this.ctx = ctx;
	}
	
	public void broadcast( Object object ) {
		if ( !this.channelGroup.isEmpty() ) {
			this.channelGroup.write( object );
		}
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		Object msg = e.getMessage();
		if ( msg instanceof HttpRequest ) {
			this.handleHttpRequest( ctx, (HttpRequest)msg );
		}
		else if ( msg instanceof WebSocketFrame ) {
			this.handleWebSocketFrame( ctx, (WebSocketFrame)msg );
		}
		super.messageReceived(ctx, e);
	}
	
	private void handleHttpRequest( ChannelHandlerContext ctx, HttpRequest request ) {
		if ( request.getMethod() != HttpMethod.GET ) {
			this.sendHttpResponse(ctx, request, new DefaultHttpResponse( HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN ) );
			return;
		}
		/*
		if ( request.getUri().equals( "/" ) ) {
			HttpResponse response = new DefaultHttpResponse( HttpVersion.HTTP_1_1, HttpResponseStatus.OK );
			//show index.html blahblah
		}
		*/
		WebSocketServerHandshakerFactory wsFactory = 
				new WebSocketServerHandshakerFactory( this.getWebSocketLocation( request ), null, false );
		this.handshaker = wsFactory.newHandshaker( request );
		if ( this.handshaker == null ) {
			wsFactory.sendUnsupportedWebSocketVersionResponse( ctx.getChannel() );
		}
		else {
			this.handshaker
				.handshake( ctx.getChannel(), request )
				.addListener( new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future)
							throws Exception {
						// TODO Auto-generated method stub
						if (!future.isSuccess()) {
							Channels.fireExceptionCaught(future.getChannel(), future.getCause());
			            }
						else {
							final Channel channel = future.getChannel();
							//channel.write( WsPacket.toPacket( new WsInitPacket() ) );
							channelGroup.add( channel );
						}
					}
					
				});
		}
	}
	
	private void sendHttpResponse( ChannelHandlerContext ctx, HttpRequest request, HttpResponse response ) {
		if ( response.getStatus().getCode() == 200 ) {
			response.setContent( ChannelBuffers.copiedBuffer( response.getStatus().toString(), CharsetUtil.UTF_8 ) );
			HttpHeaders.setContentLength( response, response.getContent().readableBytes() );
		}
		
		ChannelFuture future = ctx.getChannel().write( response );
		if ( HttpHeaders.isKeepAlive( request ) || 
			response.getStatus().getCode() != 200 ) {
			future.addListener( ChannelFutureListener.CLOSE );
		}
	}
	
	private void handleWebSocketFrame( ChannelHandlerContext ctx, WebSocketFrame frame ) {
		final Channel channel = ctx.getChannel();
		if ( frame instanceof CloseWebSocketFrame ) {
			this.handshaker.close( ctx.getChannel(), (CloseWebSocketFrame) frame );
		}
		else if ( frame instanceof PingWebSocketFrame ) {
			channel.write( new PongWebSocketFrame( frame.getBinaryData() ) );
		}
		else {
			throw new UnsupportedOperationException( String.format( "%s frame types not supported", frame.getClass().getName() ) );
		}
	}
	
	private String getWebSocketLocation( HttpRequest request ) {
		return "ws://" + HttpHeaders.getHeader( request, HttpHeaders.Names.HOST ) + WEB_SOCKET_CONTEXT;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		e.getCause().printStackTrace();
		e.getChannel().close();
		this.channelGroup.remove( e.getChannel() );
	}
}
