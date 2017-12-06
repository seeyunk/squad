/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.identify;

import javax.baja.sys.*;

import com.tridium.ddf.identify.*;

import com.hsnc.squad.axdriver.comm.req.*;
import com.hsnc.squad.axdriver.identify.*;

public class BAxSquadWriteParams
  extends BDdfIdParams
  implements BIDdfWriteParams
{
  /*-
  class BAxSquadWriteParams
  {
    properties
    {
      forceWrite : boolean
        -- This property has nothing to do with the dev
        -- driver framework itself. Instead, we need to
        -- know the location of a point's value when in
        -- the parseReadValue method of AxSquad's
        -- read response
        default{[true]}
        slotfacets{[MGR_INCLUDE]}
    }
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.identify.BAxSquadWriteParams(599551595)1.0$ @*/
/* Generated Mon Jan 11 10:44:15 GMT+08:00 2016 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "forceWrite"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>forceWrite</code> property.
   * This property has nothing to do with the dev driver
   * framework itself. Instead, we need to know the location
   * of a point's value when in the parseReadValue method
   * of AxSquad's read response
   * @see com.hsnc.squad.axdriver.identify.BAxSquadWriteParams#getForceWrite
   * @see com.hsnc.squad.axdriver.identify.BAxSquadWriteParams#setForceWrite
   */
  public static final Property forceWrite = newProperty(0, true,MGR_INCLUDE);
  
  /**
   * Get the <code>forceWrite</code> property.
   * This property has nothing to do with the dev driver
   * framework itself. Instead, we need to know the location
   * of a point's value when in the parseReadValue method
   * of AxSquad's read response
   * @see com.hsnc.squad.axdriver.identify.BAxSquadWriteParams#forceWrite
   */
  public boolean getForceWrite() { return getBoolean(forceWrite); }
  
  /**
   * Set the <code>forceWrite</code> property.
   * This property has nothing to do with the dev driver
   * framework itself. Instead, we need to know the location
   * of a point's value when in the parseReadValue method
   * of AxSquad's read response
   * @see com.hsnc.squad.axdriver.identify.BAxSquadWriteParams#forceWrite
   */
  public void setForceWrite(boolean v) { setBoolean(forceWrite,v,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadWriteParams.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  public Type getWriteRequestType(){return BAxSquadWriteRequest.TYPE;}
  }