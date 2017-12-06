/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */

package com.hsnc.squad.axdriver;

import javax.baja.sys.*;

import com.tridium.ddf.identify.BDdfIdParams;

import com.tridium.ddfIp.tcp.*;

import com.hsnc.squad.axdriver.comm.*;
import com.hsnc.squad.axdriver.identify.BAxSquadDeviceId;


public class BAxSquadDevice
  extends BDdfTcpDevice
{
  /*-
  class BAxSquadDevice
  {
    properties
    {
      communicator : BValue
        -- This plugs in an instance of yourDriver's
        -- communicator onto the device component. This
        -- causes the developer driver framework to address
        -- data packets directly to the field-device. This
        -- has the MGR_INCLUDE slot facet so that the ip
        -- address and port appear in the device manager.
        default{[ new BAxSquadCommunicator() ]}
        slotfacets{[MGR_INCLUDE]}
                
                
      deviceId : BDdfIdParams
        -- This plugs in an instance of AxSquad's
        -- device id as this device's deviceId
        default {[new BAxSquadDeviceId()]}
        slotfacets{[MGR_INCLUDE]}
      points : BAxSquadPointDeviceExt
        -- Adds the special point device extension
        -- component to the property sheet and the
        -- Niagara AX navigation tree. This special
        -- component will contain and process all
        -- control points for YourDriver
        default{[new BAxSquadPointDeviceExt()]}
    }
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.BAxSquadDevice(702005023)1.0$ @*/
/* Generated Thu Dec 24 11:29:54 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "communicator"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>communicator</code> property.
   * This plugs in an instance of yourDriver's communicator
   * onto the device component. This causes the developer
   * driver framework to address data packets directly to the field-device. This has the MGR_INCLUDE slot facet so that the ip address and port appear in the device manager.
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#getCommunicator
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#setCommunicator
   */
  public static final Property communicator = newProperty(0, new BAxSquadCommunicator(),MGR_INCLUDE);
  
  /**
   * Get the <code>communicator</code> property.
   * This plugs in an instance of yourDriver's communicator
   * onto the device component. This causes the developer
   * driver framework to address data packets directly to the field-device. This has the MGR_INCLUDE slot facet so that the ip address and port appear in the device manager.
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#communicator
   */
  public BValue getCommunicator() { return (BValue)get(communicator); }
  
  /**
   * Set the <code>communicator</code> property.
   * This plugs in an instance of yourDriver's communicator
   * onto the device component. This causes the developer
   * driver framework to address data packets directly to the field-device. This has the MGR_INCLUDE slot facet so that the ip address and port appear in the device manager.
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#communicator
   */
  public void setCommunicator(BValue v) { set(communicator,v,null); }

////////////////////////////////////////////////////////////////
// Property "deviceId"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>deviceId</code> property.
   * This plugs in an instance of AxSquad's device id as
   * this device's deviceId
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#getDeviceId
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#setDeviceId
   */
  public static final Property deviceId = newProperty(0, new BAxSquadDeviceId(),MGR_INCLUDE);
  
  /**
   * Get the <code>deviceId</code> property.
   * This plugs in an instance of AxSquad's device id as
   * this device's deviceId
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#deviceId
   */
  public BDdfIdParams getDeviceId() { return (BDdfIdParams)get(deviceId); }
  
  /**
   * Set the <code>deviceId</code> property.
   * This plugs in an instance of AxSquad's device id as
   * this device's deviceId
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#deviceId
   */
  public void setDeviceId(BDdfIdParams v) { set(deviceId,v,null); }

////////////////////////////////////////////////////////////////
// Property "points"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>points</code> property.
   * Adds the special point device extension component to the property sheet and the Niagara AX navigation tree. This special component will contain and process all control points for YourDriver
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#getPoints
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#setPoints
   */
  public static final Property points = newProperty(0, new BAxSquadPointDeviceExt(),null);
  
  /**
   * Get the <code>points</code> property.
   * Adds the special point device extension component to the property sheet and the Niagara AX navigation tree. This special component will contain and process all control points for YourDriver
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#points
   */
  public BAxSquadPointDeviceExt getPoints() { return (BAxSquadPointDeviceExt)get(points); }
  
  /**
   * Set the <code>points</code> property.
   * Adds the special point device extension component to the property sheet and the Niagara AX navigation tree. This special component will contain and process all control points for YourDriver
   * @see com.hsnc.squad.axdriver.BAxSquadDevice#points
   */
  public void setPoints(BAxSquadPointDeviceExt v) { set(points,v,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadDevice.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/
   
  public Type getNetworkType()
  {
    return BAxSquadNetwork.TYPE;
  }
}