/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.comm.req;

import javax.baja.sys.Sys;
import javax.baja.sys.Type;

import com.hsnc.ax.packet.ReadPointsValuePacket;
import com.hsnc.squad.axdriver.comm.rsp.BAxSquadReadResponse;
import com.hsnc.squad.axdriver.identify.BAxSquadDeviceId;
import com.hsnc.squad.axdriver.identify.BAxSquadPointId;
import com.hsnc.squad.axdriver.identify.BAxSquadReadParams;
import com.hsnc.squad.axdriver.point.BAxSquadProxyExt;
import com.tridium.ddf.comm.IDdfDataFrame;
import com.tridium.ddf.comm.req.BDdfReadRequest;
import com.tridium.ddf.comm.req.IDdfReadable;
import com.tridium.ddf.comm.rsp.BIDdfResponse;
import com.tridium.ddf.comm.rsp.DdfResponseException;

public class BAxSquadReadRequest
  extends BDdfReadRequest
  {
  /*-
  class BAxSquadReadRequest
  {
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.comm.req.BAxSquadReadRequest(1962206704)1.0$ @*/
/* Generated Mon Dec 28 19:42:52 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadReadRequest.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  public byte[] toByteArray()
  {
   
    BAxSquadDeviceId deviceId =
      (BAxSquadDeviceId)getDeviceId();
    
    BAxSquadReadParams readParams
      = (BAxSquadReadParams)getReadParameters();

    byte[] packet = null;
    try {
    	String unitName = deviceId.getUnitName();
        String pointId = readParams.getPointId();
    	packet = new ReadPointsValuePacket( unitName, pointId ).encode();
    	
    	//@@System.out.println( "[REQUEST]::ReqadValue::" + pointId );
    }
    catch( Exception e ) {
    	e.printStackTrace();
    }
    
    return packet;
  }

  /**
   * After transmitting this request, the BDdfCommunicator will pass frames that it receives
   * here. If you implement the getTag method then the ddf communicator will only pass data frames
   * whose tag's hashcode matches your request tag's hashcode. If your request returns null from
   * the getTag method then all received data frames will be passed here, until the request times
   * out or returns a BIDdfResponse from this method.
   *
   *  This request needs to take one of the following steps:
   *   1. NOT TYPICAL: Ignore the frame and return null.
   *   2. NOT TYPICAL: Collect the frame and return a BIDdfMultiFrameResponse. In which case, you need to implement your own collection
   *      mechanism. For example, this could be as simple as putting them all in a Vector in the BIDdfMultiFrameResponse.
   *   3. TYPICAL: Return a BIDdfResponse for the data frame and NOT TYPICAL:> any previously collected frames that you determine together make up a completed response.
   *   4. TYPICAL: Throw an DdfResponseException or subclass there-of to indicate the the frame
   *   forms a complete message but indicates an error condition in the device preventing
   *   a successful response.
   *
   *   WARNING: In scenario's 2 and 3, please copy the frame's bytes as the frame's byte array could be a direct reference to an internal
   *   buffer in the receiver.
   * @param iDdfDataFrame
   * @return
   */
  public BIDdfResponse processReceive(IDdfDataFrame receiveFrame)
    throws DdfResponseException
  {
    return new BAxSquadReadResponse(receiveFrame,
      (BAxSquadReadParams)getReadParameters());
  }
}