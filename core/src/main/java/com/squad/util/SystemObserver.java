package com.squad.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.context.ISquadContext;
import com.squad.entity.SystemState;

@Deprecated
public class SystemObserver implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(SystemObserver.class);
	private ScheduledExecutorService sysObserver;
	private final ISquadContext ctx;
	private boolean isStarted = false;
	public SystemObserver( ISquadContext ctx ) {
		this.ctx = ctx;
	}
	
	public void start() {
		if ( !isStarted ) {
			this.sysObserver = Executors.newSingleThreadScheduledExecutor( SquadThreadFactory.newThreadFactory( "sys-observer" ) );
			this.sysObserver.scheduleWithFixedDelay( this, 10000L, 3000L, TimeUnit.MILLISECONDS );
			this.isStarted = true;
		}
	}

	public void stop() {
		this.sysObserver.shutdown();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			SystemState systemState = new SystemState( CpuUtil.getAvailableProcessors(), 
					//CpuUtil.getCpuLoad(),
					CpuUtil.getCpuUsage(),
					CpuUtil.getSystemLoadAverage(),
					CpuUtil.getUpTime(),
					MemoryUtil.getMaxMemory(),
					MemoryUtil.getTotalMemory(),
					MemoryUtil.getUsedMemory(),
					MemoryUtil.getPermUsed(),
					MemoryUtil.getPermMax(),
					MemoryUtil.getPermCommitted() );
			//@@this.ctx.sendMessage( new Message( MSG.OBSERVE_SYSTEM_STATE, systemState ));
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		
	}
}
