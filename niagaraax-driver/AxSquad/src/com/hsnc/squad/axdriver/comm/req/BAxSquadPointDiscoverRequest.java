/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.comm.req;

import javax.baja.sys.Sys;
import javax.baja.sys.Type;

import com.hsnc.ax.packet.ReadPointsPacket;
import com.hsnc.squad.axdriver.comm.rsp.BAxSquadPointDiscoverResponse;
import com.hsnc.squad.axdriver.identify.BAxSquadDeviceId;
import com.tridium.ddf.comm.IDdfDataFrame;
import com.tridium.ddf.comm.req.BDdfDiscoveryRequest;
import com.tridium.ddf.comm.rsp.BIDdfResponse;
import com.tridium.ddf.comm.rsp.DdfResponseException;


public class BAxSquadPointDiscoverRequest
  extends BDdfDiscoveryRequest
{
  /*-
  class BAxSquadPointDiscoverRequest
  {
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.comm.req.BAxSquadPointDiscoverRequest(4272156727)1.0$ @*/
/* Generated Thu Dec 24 11:29:54 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadPointDiscoverRequest.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  private volatile boolean hasRequested = false;
  private static final Object lock = new Object();
  
  public byte[] toByteArray()
  {
    // In the developer driver framework, all requests are automatically
    // assigned a deviceId when they are created. The developer driver
    // framework calls this method (function) after it creates the
    // request, therefore this particular request has already been
    // assigned a deviceId. The deviceId will be an instance of
    // BAxSquadDeviceId - that is how the developer driver works!
	  BAxSquadDeviceId deviceId =
		      (BAxSquadDeviceId)getDeviceId();
	  byte[] packet = null;
	    try {
	    	String unitName = deviceId.getUnitName();
	    	packet = new ReadPointsPacket( unitName ).encode();
	    }
	    catch( Exception e ) {
	    	e.printStackTrace();
	    }
	    finally {
	      synchronized( lock ) {
	  		  this.hasRequested = true;
	  	  }
	    }
	    
	  return packet;

  }
  
  public BIDdfResponse processReceive(IDdfDataFrame receiveFrame) 
     throws DdfResponseException 
  {  
    // TODO: Pass any data to the response that it will need to 
    // implement its parseDiscoveryChildren method.
	  synchronized( lock ) {
		  this.hasRequested = false;
	  }
	  
	  return new BAxSquadPointDiscoverResponse(receiveFrame); 
  }   
}