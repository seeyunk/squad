/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.discover; 
 
import javax.baja.sys.*;

import com.tridium.ddf.identify.*; 
import com.tridium.ddf.discover.*; 

import com.hsnc.squad.axdriver.identify.*; 
  
public class BAxSquadPointDiscoveryLeaf 
  extends BDdfPointDiscoveryLeaf 
{ 
  /*- 
  class BAxSquadPointDiscoveryLeaf 
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
/*@ $com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf(3712626663)1.0$ @*/
/* Generated Mon Jan 11 10:45:55 GMT+08:00 2016 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "pointId"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>pointId</code> property.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#getPointId
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#setPointId
   */
  public static final Property pointId = newProperty(0, new BAxSquadPointId(),MGR_INCLUDE);
  
  /**
   * Get the <code>pointId</code> property.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#pointId
   */
  public BDdfIdParams getPointId() { return (BDdfIdParams)get(pointId); }
  
  /**
   * Set the <code>pointId</code> property.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#pointId
   */
  public void setPointId(BDdfIdParams v) { set(pointId,v,null); }

////////////////////////////////////////////////////////////////
// Property "readParameters"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>readParameters</code> property.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#getReadParameters
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#setReadParameters
   */
  public static final Property readParameters = newProperty(0, new BAxSquadReadParams(),MGR_INCLUDE);
  
  /**
   * Get the <code>readParameters</code> property.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#readParameters
   */
  public BDdfIdParams getReadParameters() { return (BDdfIdParams)get(readParameters); }
  
  /**
   * Set the <code>readParameters</code> property.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#readParameters
   */
  public void setReadParameters(BDdfIdParams v) { set(readParameters,v,null); }

////////////////////////////////////////////////////////////////
// Property "writeParameters"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>writeParameters</code> property.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#getWriteParameters
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#setWriteParameters
   */
  public static final Property writeParameters = newProperty(0, new BAxSquadWriteParams(),MGR_INCLUDE);
  
  /**
   * Get the <code>writeParameters</code> property.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#writeParameters
   */
  public BDdfIdParams getWriteParameters() { return (BDdfIdParams)get(writeParameters); }
  
  /**
   * Set the <code>writeParameters</code> property.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf#writeParameters
   */
  public void setWriteParameters(BDdfIdParams v) { set(writeParameters,v,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadPointDiscoveryLeaf.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/ 
 
 
  /** 
   * When a control point is added to the station from the 
   * Point Manager, it is given this name by default (possibly 
   * with a suffix to make it unique). 
   */ 
  public String getDiscoveryName() 
  {
    // TODO: Return a string to serve as the default name for this
    // item if the end-user selects this item's row in the
    // "Discovered" list on the point point manager and clicks the
    // "Add" button to add this item to the station database.
    // Good luck.
    return "Point"; // TODO
  }
  
}