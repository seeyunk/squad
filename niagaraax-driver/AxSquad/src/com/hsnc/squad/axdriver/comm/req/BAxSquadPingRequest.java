/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */

package com.hsnc.squad.axdriver.comm.req;

import javax.baja.sys.*;

import com.tridium.ddf.identify.*;
import com.tridium.ddf.comm.req.*;
import com.tridium.ddf.comm.rsp.*;
import com.tridium.ddf.comm.*;
import com.hsnc.ax.packet.PingPacket;
import com.hsnc.squad.axdriver.identify.*;
import com.hsnc.squad.axdriver.comm.rsp.*;

public class BAxSquadPingRequest
  extends BDdfPingRequest
  {
   
  
    /*- 
  class BAxSquadPingRequest 
  { 
  } 
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.comm.req.BAxSquadPingRequest(3658974195)1.0$ @*/
/* Generated Thu Dec 24 11:29:54 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadPingRequest.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/ 
    
  

  public byte[] toByteArray()
  {
	  return new PingPacket().encode();
  }

  /**
   * For this example, we will assume that the mere fact that a data
   * frame was received after transmitting this response means that
   * the equipment must have responded to the request. Since in
   * Niagara AX, the primary purpose of a ping request-response
   * transaction is to determine whether or not the corresponding
   * field-device is online, then this will suffice.
   */
  public BIDdfResponse processReceive(IDdfDataFrame recieveFrame)
    throws DdfResponseException
  {
    return new BAxSquadPingResponse();
  }
  
  
}