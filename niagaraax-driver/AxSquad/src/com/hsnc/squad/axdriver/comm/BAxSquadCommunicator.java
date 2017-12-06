/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */

package com.hsnc.squad.axdriver.comm;

import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

import com.tridium.ddf.comm.defaultComm.BDdfReceiver;

import com.tridium.ddfIp.tcp.comm.singleTransaction.*;

public class BAxSquadCommunicator  extends BDdfTcpSitCommunicator
{
  /*-
  class BAxSquadCommunicator
  {
    properties
    {
    
      receiver : BDdfReceiver
        -- Plugs yourDriver's custom receiver
        -- into yourDriver's communicator
        default{[new BAxSquadReceiver()]}
    
    }
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.comm.BAxSquadCommunicator(1389069841)1.0$ @*/
/* Generated Thu Dec 24 11:29:54 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "receiver"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>receiver</code> property.
   * Plugs yourDriver's custom receiver into yourDriver's
   * communicator
   * @see com.hsnc.squad.axdriver.comm.BAxSquadCommunicator#getReceiver
   * @see com.hsnc.squad.axdriver.comm.BAxSquadCommunicator#setReceiver
   */
  public static final Property receiver = newProperty(0, new BAxSquadReceiver(),null);
  
  /**
   * Get the <code>receiver</code> property.
   * Plugs yourDriver's custom receiver into yourDriver's
   * communicator
   * @see com.hsnc.squad.axdriver.comm.BAxSquadCommunicator#receiver
   */
  public BDdfReceiver getReceiver() { return (BDdfReceiver)get(receiver); }
  
  /**
   * Set the <code>receiver</code> property.
   * Plugs yourDriver's custom receiver into yourDriver's
   * communicator
   * @see com.hsnc.squad.axdriver.comm.BAxSquadCommunicator#receiver
   */
  public void setReceiver(BDdfReceiver v) { set(receiver,v,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadCommunicator.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/


}