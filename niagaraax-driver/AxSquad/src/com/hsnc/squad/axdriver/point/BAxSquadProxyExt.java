/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.point;

import javax.baja.sys.*;

import com.tridium.ddf.point.*;
import com.tridium.ddf.identify.*;

import com.hsnc.squad.axdriver.*;
import com.hsnc.squad.axdriver.comm.req.*;
import com.hsnc.squad.axdriver.identify.*;


public class BAxSquadProxyExt
  extends BDdfProxyExt
{
  /*-
  class BAxSquadProxyExt
  {
    properties
    {
        pointId : BDdfIdParams 
        default{[new BAxSquadPointId()]} 
        slotfacets{[MGR_INCLUDE]} 
        
        readParameters : BDdfIdParams 
        default{[new BAxSquadReadParams()]} 
        slotfacets{[MGR_INCLUDE]}
        
        writeParameters : BDdfIdParams 
        default{[new BAxSquadWriteParams()]} 
        slotfacets{[MGR_INCLUDE]}
    }
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.point.BAxSquadProxyExt(3187214946)1.0$ @*/
/* Generated Mon Jan 11 10:45:55 GMT+08:00 2016 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "pointId"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>pointId</code> property.
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#getPointId
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#setPointId
   */
  public static final Property pointId = newProperty(0, new BAxSquadPointId(),MGR_INCLUDE);
  
  /**
   * Get the <code>pointId</code> property.
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#pointId
   */
  public BDdfIdParams getPointId() { return (BDdfIdParams)get(pointId); }
  
  /**
   * Set the <code>pointId</code> property.
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#pointId
   */
  public void setPointId(BDdfIdParams v) { set(pointId,v,null); }

////////////////////////////////////////////////////////////////
// Property "readParameters"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>readParameters</code> property.
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#getReadParameters
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#setReadParameters
   */
  public static final Property readParameters = newProperty(0, new BAxSquadReadParams(),MGR_INCLUDE);
  
  /**
   * Get the <code>readParameters</code> property.
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#readParameters
   */
  public BDdfIdParams getReadParameters() { return (BDdfIdParams)get(readParameters); }
  
  /**
   * Set the <code>readParameters</code> property.
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#readParameters
   */
  public void setReadParameters(BDdfIdParams v) { set(readParameters,v,null); }

////////////////////////////////////////////////////////////////
// Property "writeParameters"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>writeParameters</code> property.
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#getWriteParameters
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#setWriteParameters
   */
  public static final Property writeParameters = newProperty(0, new BAxSquadWriteParams(),MGR_INCLUDE);
  
  /**
   * Get the <code>writeParameters</code> property.
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#writeParameters
   */
  public BDdfIdParams getWriteParameters() { return (BDdfIdParams)get(writeParameters); }
  
  /**
   * Set the <code>writeParameters</code> property.
   * @see com.hsnc.squad.axdriver.point.BAxSquadProxyExt#writeParameters
   */
  public void setWriteParameters(BDdfIdParams v) { set(writeParameters,v,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadProxyExt.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/



  /**
   * This associates BAxSquadDeviceExt with
   * BAxSquadProxyExt within the Niagara AX
   * framework.
   */
  public Type getDeviceExtType()
  {
    return BAxSquadPointDeviceExt.TYPE;
  }
}