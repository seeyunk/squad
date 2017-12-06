/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */

package com.hsnc.squad.axdriver.identify;

import javax.baja.sys.*;
import javax.baja.registry.*;

import com.tridium.ddf.identify.*;
import com.tridium.ddf.discover.*;

import com.hsnc.squad.axdriver.*;
import com.hsnc.squad.axdriver.comm.req.*;

public class BAxSquadDeviceId
  extends BDdfDeviceId
      //implements BIDdfDiscoveryLeaf
  
{
  /*-
  class BAxSquadDeviceId
  {
    properties
    {
      unitName : String
        -- This is the unitNumber in our hypothetical protocol.
        default{[""]}
        slotfacets{[MGR_INCLUDE]}
    }
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.identify.BAxSquadDeviceId(419020663)1.0$ @*/
/* Generated Mon Dec 28 18:50:06 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "unitName"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>unitName</code> property.
   * This is the unitNumber in our hypothetical protocol.
   * @see com.hsnc.squad.axdriver.identify.BAxSquadDeviceId#getUnitName
   * @see com.hsnc.squad.axdriver.identify.BAxSquadDeviceId#setUnitName
   */
  public static final Property unitName = newProperty(0, "",MGR_INCLUDE);
  
  /**
   * Get the <code>unitName</code> property.
   * This is the unitNumber in our hypothetical protocol.
   * @see com.hsnc.squad.axdriver.identify.BAxSquadDeviceId#unitName
   */
  public String getUnitName() { return getString(unitName); }
  
  /**
   * Set the <code>unitName</code> property.
   * This is the unitNumber in our hypothetical protocol.
   * @see com.hsnc.squad.axdriver.identify.BAxSquadDeviceId#unitName
   */
  public void setUnitName(String v) { setString(unitName,v,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadDeviceId.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  public Type getPingRequestType( )
  {
    return BAxSquadPingRequest.TYPE;
  }

  /**
   * Niagara AX requires a public, empty constructor, so that it can perform
   * Its own introspection operations.
   */
  public BAxSquadDeviceId(){}
  
  
    
  /**
   * When a control point is added to the station from the Dev
   * Point Manager, it is given this name by default (possibly
   * with a suffix to make it unique).
   * @return
   */
  public String getDiscoveryName()
  {
    return "NewDevice";
  } 
  
  /**
   * Descendants need to return an array of TypeInfo objects corresponding
   * to all valid Niagara Ax types for this discovery object. This is
   * important when the end-user clicks 'Add' from the user interface for
   * the manager.
   *
   * For this discovery object, please  return a list of the types
   * which may be used to model it as a BComponent in the station
   * database. If the discovery object represents a device in your
   * driver then then method should return an array with size of
   * at least one, filled with TypeInfo's that wrap the Niagara AX
   * TYPE's for your driver's device components.
   *
   * The type at index 0 in the array should be the type which
   * provides the best mapping.  Please return an empty array if the
   * discovery cannot be mapped.
   */
  public TypeInfo[] getValidDatabaseTypes()
  {
    return new TypeInfo[]{BAxSquadDevice.TYPE.getTypeInfo()};
  }  
  
////////////////////////////////////////////////////////////////
// Attributes
////////////////////////////////////////////////////////////////
  
  private static final int FIRST_UNIT_NBR = 0;
  private static final int LAST_UNIT_NBR = 50;
  
}