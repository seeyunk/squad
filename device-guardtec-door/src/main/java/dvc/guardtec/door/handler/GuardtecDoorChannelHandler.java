package dvc.guardtec.door.handler;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.netty.NettyProducer;
import org.apache.camel.component.netty.handlers.ClientChannelHandler;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;

import dvc.guardtec.door.entity.AcuState;
import dvc.guardtec.door.entity.EventRequest;
import dvc.guardtec.door.entity.Header;

public class GuardtecDoorChannelHandler extends ClientChannelHandler {
	private final ProducerTemplate template;
	
	public GuardtecDoorChannelHandler( NettyProducer producer ) {
		super( producer );
		this.template = producer.getContext().createProducerTemplate();
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		Channel channel = e.getChannel();
		channel.write( new EventRequest() );
		Thread.sleep( 1000L );
		
		channel.write( new Header( Header.MESSAGE_ID_ACU_STATE ) );
		
		super.channelConnected(ctx, e);
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx,
			MessageEvent messageEvent) throws Exception {
		// TODO Auto-generated method stub
		final Channel channel = messageEvent.getChannel();
		if ( messageEvent.getMessage() instanceof AcuState[] ) {
			AcuState[] acuStates = (AcuState[])messageEvent.getMessage();
			for ( AcuState acuState : acuStates ) {
				Header header = new Header( Header.MESSAGE_ID_ACU_RELAY );
				header.setIdNumber( acuState.getAccuBankNo() );
				channel.write( header );
				Thread.sleep( 100L );
			}
			return;
		}
		
		template.sendBody( "direct:recv", messageEvent.getMessage() );
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		template.sendBody( "direct:send", new Header( Header.MESSAGE_ID_END_EVENT ) );
	}
}
