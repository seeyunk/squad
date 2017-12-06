package com.squad.manager;

import com.squad.protobuf.packet.Squad.ServiceState;

public interface IServiceResultHandler {
	public boolean handleServiceResult( String serviceId, ServiceState serviceState, boolean forcedRun ) throws Exception;
}
