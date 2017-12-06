package com.squad.rpc;


public abstract class ScoutSocketState {
	private ChannelState beforeSocketState = ChannelState.NOT_CONNECTED;
	private ChannelState curSocketState = ChannelState.NOT_CONNECTED;
	
	public ChannelState getSocketState() {
		return this.curSocketState;
	}
	
	public void setSocketState( ChannelState socketState ) {
		this.beforeSocketState = this.curSocketState;
		this.curSocketState = socketState;
	}
	
	public ChannelState getBeforeSocketState() {
		return this.beforeSocketState;
	}
	
	public boolean isCommEnabled() {
		return ( this.curSocketState == ChannelState.CONNECTED ) ? true : false;
	}
	
	public void toConnected() {
		this.setSocketState( ChannelState.CONNECTED );
	}

	public void toBeingClosedByServer() {
		this.setSocketState( ChannelState.BEING_CLOSED_BY_SERVER );
	}
	
	public void toClosedByServer() {
		this.setSocketState( ChannelState.CLOSED_BY_SERVER );
	}
	
	public void toUnexpectedClosedByServer() {
		this.setSocketState( ChannelState.UNEXPECTED_CLOSED_BY_SERVER );
	}
}
