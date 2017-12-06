/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.comm.rsp;

import javax.baja.sys.*;

import com.tridium.ddf.comm.*;
import com.tridium.ddf.comm.rsp.*;

public class BAxSquadWriteResponse
  extends BDdfResponse
{
  /*-
  class BAxSquadWriteResponse
  {
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.comm.rsp.BAxSquadWriteResponse(3647821887)1.0$ @*/
/* Generated Thu Dec 24 11:29:54 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadWriteResponse.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/
  
  public BAxSquadWriteResponse()
  {
  }

  /**
   * The BAxSquadWriteRequest.processReceive method calls
   * this constructor and passes in the data frame that it
   * receives.
   * 
   * @param IDdfDataFrame receiveFrame - this is the data that
   * the driver receives from the field-device just after
   * transmitting the byte array representation of a 
   * BAxSquadWriteRequest.
   * 
   * @throws DdfResponseException if the given frame contains
   * information from the field-device that indicates that the
   * write request was unsuccessful or otherwise denied.
   */
  public BAxSquadWriteResponse(IDdfDataFrame receiveFrame)
    throws DdfResponseException
  {
    // HINT: Write responses originate from the field-device itself.
    // Write responses typically indicate whether or not the write
    // request was successful
    
    verifyResponse(receiveFrame);
  }
  
  /**
   * This function is called by the constructor to analyze the
   * given receive frame following the AxSquad protocol
   * and determine whether or not the previous write request
   * was successful.
   * 
   * @param receiveFrame
   * 
   * @return true if the receiveFrame indicates success or
   * acknowledgment. Returns false if the receiveFrame
   * indicates an field-device validation error, negative
   * acknowledgment, denial, etc.
   */
  void verifyResponse(IDdfDataFrame receiveFrame)
    throws DdfResponseException
  {
    // TODO: Analyze the given receiveFrame and throw
    // an instance of DdfResponseException if the data
    // inside the given receiveFrame indicates a
    // negative acknowledgment from the field-device.
    // If so then throw an instance of DdfResponseException
    // whose string indicates the problem.
  }
}