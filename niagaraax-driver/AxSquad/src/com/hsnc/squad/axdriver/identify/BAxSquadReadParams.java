/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.identify;

import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

import com.tridium.ddf.identify.*; 

import com.tridium.ddf.identify.BDdfReadParams;
import com.tridium.ddf.identify.BIDdfDiscoverParams;

import com.hsnc.squad.axdriver.comm.req.BAxSquadReadRequest;
import com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf;
import com.hsnc.squad.axdriver.identify.*;

public class BAxSquadReadParams
  extends BDdfReadParams
  {
  /*-
  class BAxSquadReadParams
  {
    properties
    {
    	pointId : String
        -- This property has nothing to do with the dev
        -- driver framework itself. Instead, we need to
        -- know the location of a point's value when in
        -- the parseReadValue method of AxSquad's
        -- read response
        default{[""]}
        slotfacets{[MGR_INCLUDE]}
    }
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.identify.BAxSquadReadParams(2750809101)1.0$ @*/
/* Generated Mon Jan 11 11:18:06 GMT+08:00 2016 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "pointId"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>pointId</code> property.
   * This property has nothing to do with the dev driver
   * framework itself. Instead, we need to know the location
   * of a point's value when in the parseReadValue method
   * of AxSquad's read response
   * @see com.hsnc.squad.axdriver.identify.BAxSquadReadParams#getPointId
   * @see com.hsnc.squad.axdriver.identify.BAxSquadReadParams#setPointId
   */
  public static final Property pointId = newProperty(0, "",MGR_INCLUDE);
  
  /**
   * Get the <code>pointId</code> property.
   * This property has nothing to do with the dev driver
   * framework itself. Instead, we need to know the location
   * of a point's value when in the parseReadValue method
   * of AxSquad's read response
   * @see com.hsnc.squad.axdriver.identify.BAxSquadReadParams#pointId
   */
  public String getPointId() { return getString(pointId); }
  
  /**
   * Set the <code>pointId</code> property.
   * This property has nothing to do with the dev driver
   * framework itself. Instead, we need to know the location
   * of a point's value when in the parseReadValue method
   * of AxSquad's read response
   * @see com.hsnc.squad.axdriver.identify.BAxSquadReadParams#pointId
   */
  public void setPointId(String v) { setString(pointId,v,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadReadParams.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  public Type getReadRequestType(){return BAxSquadReadRequest.TYPE;}

    

}