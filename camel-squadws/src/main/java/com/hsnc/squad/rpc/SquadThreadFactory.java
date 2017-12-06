package com.hsnc.squad.rpc;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SquadThreadFactory implements ThreadFactory {
	private static Logger logger = LoggerFactory.getLogger( SquadThreadFactory.class );
	private static final AtomicInteger FACTORY_NUMBER = new AtomicInteger( 0 );
	private final AtomicInteger threadNum = new AtomicInteger( 0 );
	private final String threadName;
	
	private SquadThreadFactory( String threadName ) {
		this.threadName = threadName;
	}
	
	public static ThreadFactory newThreadFactory( String threadName ) {
		return new SquadThreadFactory( threadName );
	}
	
	@Override
	public Thread newThread(Runnable r) {
		// TODO Auto-generated method stub
		Thread thread = new Thread( r, 
				String.format( "F%d-%s-%d", FACTORY_NUMBER.getAndIncrement(),
									this.threadName,
									this.threadNum.getAndIncrement() ) );
				thread.setDaemon( false );
				thread.setPriority( Thread.NORM_PRIORITY );

		return thread;
	}
}
