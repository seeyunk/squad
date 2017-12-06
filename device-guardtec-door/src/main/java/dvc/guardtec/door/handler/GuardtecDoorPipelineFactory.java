package dvc.guardtec.door.handler;

import org.apache.camel.component.netty.ClientPipelineFactory;
import org.apache.camel.component.netty.NettyProducer;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;

import com.squad.annotation.SquadComponent;

import dvc.guardtec.door.codec.GuardtecDoorDecoder;
import dvc.guardtec.door.codec.GuardtecDoorEncoder;

@SquadComponent( id="device-guradtec-door-cpf" )
public class GuardtecDoorPipelineFactory extends ClientPipelineFactory {
	private NettyProducer producer;
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// TODO Auto-generated method stubs
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast( "encoder", new GuardtecDoorEncoder() );
		pipeline.addLast( "decoder", new GuardtecDoorDecoder() );
		pipeline.addLast( "handler", new GuardtecDoorChannelHandler( this.producer ) );
		return pipeline;
	}

	@Override
	public ClientPipelineFactory createPipelineFactory(NettyProducer producer) {
		// TODO Auto-generated method stub
		this.producer = producer;
		return this;
	}

}
