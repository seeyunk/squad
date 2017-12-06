/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.comm.req;

import javax.baja.sys.*;

import com.tridium.ddf.comm.req.*;
import com.tridium.ddf.comm.*;
import com.tridium.ddf.comm.rsp.*;

import com.hsnc.squad.axdriver.comm.*;
import com.hsnc.squad.axdriver.comm.rsp.*;
import com.hsnc.squad.axdriver.identify.*;
import com.hsnc.squad.axdriver.point.*;

public class BAxSquadWriteRequest
  extends BDdfWriteRequest
{
  /*-
  class BAxSquadWriteRequest
  {
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.comm.req.BAxSquadWriteRequest(2355265845)1.0$ @*/
/* Generated Thu Dec 24 11:29:54 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadWriteRequest.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  public byte[] toByteArray()
  {
    // In the developer driver framework, most requests are automatically
    // assigned a deviceId when they are created. The developer driver
    // framework calls this method (function) after it creates the
    // request, therefore this particular request has already been
    // assigned a deviceId. The deviceId will be an instance of
    // BAxSquadDeviceId - that is how the developer driver works!
    BAxSquadDeviceId deviceId =
      (BAxSquadDeviceId)getDeviceId();
    
    // Likewise, all write requests are automatically assigned a write
    // parameters structure. The writeParameters will be an instance of
    // BAxSquadWriteParams - that is how the developer driver
    // works!
    BAxSquadWriteParams writeParams
      = (BAxSquadWriteParams)getWriteParameters();
    
    // TODO: Initialize a byte array or a byte array output stream to
    // construct the byte array that this method will return. Use the
    // data in both the 'deviceId' and the 'writeParams' to initialize
    // the byte array or byte array output stream. The deviceId has
    // all data necessary to identify the particular field-device. The
    // writeParams has all data necessary to identify "what" to write
    // within the field-device.
    
    
    // Gets an array of IDdfWritables (typically the data point proxy extensions)
    // NOTE: The 'writeParameters' of the IDdfWritable proxies in the following
    // array are all equivalent to the 'writeParams' structure for this request.
    // In the simplest case, the writeableSource will be an array of length 1.
    IDdfWritable[] pointsToWrite = getWritableSource();
    
    for (int i=0; i<pointsToWrite.length; i++)
    { 
      // TODO: Add data to the byte array to further instruct the field-device
      // as to which internal points to set and what values to set them to.
      if (pointsToWrite[i] instanceof BAxSquadProxyExt) 
      { 
        // NOTE: At present (as of Niagara 3.3.4), all of the IDdfWritables will
        // be proxy extensions. In the future, these could be a virtual point
        BAxSquadProxyExt proxy // Casts to BAxSquadProxyExt 
          = (BAxSquadProxyExt)pointsToWrite[i];
        
        BAxSquadPointId pointId // Gets information that uniquely identifies
          = (BAxSquadPointId)proxy.getPointId(); // the particular point
        
        // Converts the 'writeValue' property of the driver proxy point into a double
        // Some drivers might require different functionality here.
        double rawValue = getRawValue(proxy); 
        
        // TODO: Add bytes to the byte array or byte array output stream to
        // indicate each data point and its value
        
      }
    }
    
    // TODO: Return a byte array that asks the device identified by the
    // 'deviceId' to 'write' the data point that is
    // identified by the 'writeParams'. Good luck.
    return null;
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
    return new BAxSquadWriteResponse(receiveFrame);
  }

}