/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.identify;

import javax.baja.sys.*;

import com.tridium.ddf.identify.*;

public class BAxSquadPointId
  extends BDdfIdParams
{
  /*-
  class BAxSquadPointId
  {
    properties
    {
      seq : int
        -- This property has nothing to do with the dev
        -- driver framework itself. Instead, we need to
        -- know the location of a point's value when in
        -- the parseReadValue method of AxSquad's
        -- read response
        default{[0]}
        slotfacets{[MGR_INCLUDE]}
    }
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.identify.BAxSquadPointId(492174501)1.0$ @*/
/* Generated Mon Jan 11 11:18:06 GMT+08:00 2016 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "seq"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>seq</code> property.
   * This property has nothing to do with the dev driver
   * framework itself. Instead, we need to know the location
   * of a point's value when in the parseReadValue method
   * of AxSquad's read response
   * @see com.hsnc.squad.axdriver.identify.BAxSquadPointId#getSeq
   * @see com.hsnc.squad.axdriver.identify.BAxSquadPointId#setSeq
   */
  public static final Property seq = newProperty(0, 0,MGR_INCLUDE);
  
  /**
   * Get the <code>seq</code> property.
   * This property has nothing to do with the dev driver
   * framework itself. Instead, we need to know the location
   * of a point's value when in the parseReadValue method
   * of AxSquad's read response
   * @see com.hsnc.squad.axdriver.identify.BAxSquadPointId#seq
   */
  public int getSeq() { return getInt(seq); }
  
  /**
   * Set the <code>seq</code> property.
   * This property has nothing to do with the dev driver
   * framework itself. Instead, we need to know the location
   * of a point's value when in the parseReadValue method
   * of AxSquad's read response
   * @see com.hsnc.squad.axdriver.identify.BAxSquadPointId#seq
   */
  public void setSeq(int v) { setInt(seq,v,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadPointId.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/


}