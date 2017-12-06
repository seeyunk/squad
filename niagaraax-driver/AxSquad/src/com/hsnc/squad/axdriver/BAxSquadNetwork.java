/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver;

import javax.baja.sys.BValue;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

import com.tridium.ddf.discover.BDdfDiscoveryPreferences;

import com.tridium.ddfIp.tcp.*;
import com.hsnc.squad.axdriver.comm.BAxSquadCommunicator;
import com.hsnc.squad.axdriver.discover.*;


public class BAxSquadNetwork
   extends BDdfTcpNetwork{
  /*- 
  class BAxSquadNetwork 
  { 
    properties 
    { 
    } 
  } 
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.BAxSquadNetwork(2659858255)1.0$ @*/
/* Generated Thu Dec 24 11:29:54 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadNetwork.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  public Type getDeviceType()
  {
    return BAxSquadDevice.TYPE;
  }
  
  public Type getDeviceFolderType()
  {
    return BAxSquadDeviceFolder.TYPE;
  }
}