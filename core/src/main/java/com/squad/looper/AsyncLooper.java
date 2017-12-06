package com.squad.looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.entity.Message;
import com.squad.keeper.SquadKeeperContext;
import com.squad.util.SquadThreadFactory;

public abstract class AsyncLooper implements Runnable, IMessageNotifier, IMessageHandler {
	private static final Logger logger = LoggerFactory.getLogger( AsyncLooper.class );
	public enum MSG {
		EMPTY,
		SHUTDOWN,
		PING,
		PONG,
		JOIN_SQUAD,
		ACK_JOIN_SQUAD,
		SYNC_NODES,
		ACK_SYNC_NODES,
		CONTROL_SERVICE,
		ACK_CONTROL_SERVICE,
		NODE_OPERATION,
		ACK_NODE_OPERATION,
		NODE_OPERATION_COMMIT,
		NODE_OPERATION_ROLLBACK,
		
		CHANNEL_DISCONNECTED,
		
		HANDLE_JOIN_CLUSTER,
		SYNC_SERVICE,
		ELECT_NEW_LEADER,
		SCOUT_STATE_CHANGED,
		EXAMINE_SERVICES_SEND,
		HANDLE_EXAMINE_SERVICES,
		REQUEST_SERVICES_RECV,
		SYNC_SERVICES_END,
		OBSERVE_SYSTEM_STATE,
		TRY_RECONNECT,
		NEW_SERVICE_FOUND,
		
		LOCAL_SERVICE_STATE_CHANGED,
		SCOUT_DETECTED,
		SERVICE_STATE_CHANGED,
	}
	
	private static final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();
	private static final ExecutorService eventExecutor = Executors.newSingleThreadExecutor( SquadThreadFactory.newThreadFactory( "event-executor" ) );
	private volatile boolean isRunning = false;
	private static final BlockingQueue<IMessageHandler> handlers = new LinkedBlockingQueue<IMessageHandler>();
	
	public AsyncLooper() {
		if ( !this.isRunning ) {
			this.isRunning = true;
			eventExecutor.execute( this );
		}
		
		this.addHandler( this );
	}
	
	@Override
	public void sendMessage( Message message ) throws Exception {
		// TODO Auto-generated method stub
		messageQueue.put( message );
	}
	
	
	public void terminate() {
		this.isRunning = false;
		eventExecutor.shutdown();
	}
	
	public void addHandler( IMessageHandler handler ) {
		handlers.add( handler );
	}
	
	public boolean broadcast( Message message ) {
		boolean ret = false;
		synchronized( this ) {
			for ( IMessageHandler handler : handlers ) {
				if ( handler.handleMessage( message ) ) {
					ret = true;
					break;
				}
			}
		}
		
		return ret;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while ( isRunning ) {
			try {
				Message message = messageQueue.take();
				if ( message != null && 
					message.getMessage() != MSG.SHUTDOWN ) {
					if ( !broadcast( message ) ) {
						messageQueue.put( message );
					}
				}	
				else {
					this.terminate();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
