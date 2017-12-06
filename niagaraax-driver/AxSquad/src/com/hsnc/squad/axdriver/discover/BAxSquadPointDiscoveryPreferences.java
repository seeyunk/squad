/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.discover; 
 
import javax.baja.sys.*;

import com.tridium.ddf.identify.*; 
import com.tridium.ddf.discover.auto.*;

import com.hsnc.squad.axdriver.identify.*; 
 
public class BAxSquadPointDiscoveryPreferences 
  extends BDdfAutoDiscoveryPreferences 
{ 
  /*- 
  class BAxSquadPointDiscoveryPreferences 
  { 
    properties 
    { 
      timeout : BRelTime 
        -- This is the amount of time to wait per field-bus request before timing out 
        default{[BRelTime.makeSeconds(3)]} 
        slotfacets{[BFacets.make(BFacets.make(BFacets.SHOW_MILLISECONDS,BBoolean.TRUE), 
                                 BFacets.MIN,BRelTime.make(0))]} 
      retryCount : int 
        -- This is the number of discovery field-message retransmissions 
        -- per request. 
        default{[1]} 
        slotfacets{[BFacets.make(BFacets.MIN,BInteger.make(0))]}
      min : BDdfIdParams 
        -- This is the id of the lowest data point for your driver to attempt to 
        -- learn by default 
        flags{hidden}
        default{[(BDdfIdParams)new BAxSquadPointDiscoverParams().getFirst()]} 
      max : BDdfIdParams 
        -- This is the id of the highest point for your driver to attempt to 
        -- learn by default
        flags{hidden} 
        default{[(BDdfIdParams)new BAxSquadPointDiscoverParams().getLast()]} 
    } 
  } 
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences(2056884065)1.0$ @*/
/* Generated Mon Dec 28 19:50:02 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "timeout"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>timeout</code> property.
   * This is the amount of time to wait per field-bus request
   * before timing out
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#getTimeout
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#setTimeout
   */
  public static final Property timeout = newProperty(0, BRelTime.makeSeconds(3),BFacets.make(BFacets.make(BFacets.SHOW_MILLISECONDS,BBoolean.TRUE), 
                                 BFacets.MIN,BRelTime.make(0)));
  
  /**
   * Get the <code>timeout</code> property.
   * This is the amount of time to wait per field-bus request
   * before timing out
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#timeout
   */
  public BRelTime getTimeout() { return (BRelTime)get(timeout); }
  
  /**
   * Set the <code>timeout</code> property.
   * This is the amount of time to wait per field-bus request
   * before timing out
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#timeout
   */
  public void setTimeout(BRelTime v) { set(timeout,v,null); }

////////////////////////////////////////////////////////////////
// Property "retryCount"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>retryCount</code> property.
   * This is the number of discovery field-message retransmissions
   * per request.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#getRetryCount
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#setRetryCount
   */
  public static final Property retryCount = newProperty(0, 1,BFacets.make(BFacets.MIN,BInteger.make(0)));
  
  /**
   * Get the <code>retryCount</code> property.
   * This is the number of discovery field-message retransmissions
   * per request.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#retryCount
   */
  public int getRetryCount() { return getInt(retryCount); }
  
  /**
   * Set the <code>retryCount</code> property.
   * This is the number of discovery field-message retransmissions
   * per request.
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#retryCount
   */
  public void setRetryCount(int v) { setInt(retryCount,v,null); }

////////////////////////////////////////////////////////////////
// Property "min"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>min</code> property.
   * This is the id of the lowest data point for your driver
   * to attempt to learn by default
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#getMin
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#setMin
   */
  public static final Property min = newProperty(Flags.HIDDEN, (BDdfIdParams)new BAxSquadPointDiscoverParams().getFirst(),null);
  
  /**
   * Get the <code>min</code> property.
   * This is the id of the lowest data point for your driver
   * to attempt to learn by default
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#min
   */
  public BDdfIdParams getMin() { return (BDdfIdParams)get(min); }
  
  /**
   * Set the <code>min</code> property.
   * This is the id of the lowest data point for your driver
   * to attempt to learn by default
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#min
   */
  public void setMin(BDdfIdParams v) { set(min,v,null); }

////////////////////////////////////////////////////////////////
// Property "max"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>max</code> property.
   * This is the id of the highest point for your driver
   * to attempt to learn by default
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#getMax
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#setMax
   */
  public static final Property max = newProperty(Flags.HIDDEN, (BDdfIdParams)new BAxSquadPointDiscoverParams().getLast(),null);
  
  /**
   * Get the <code>max</code> property.
   * This is the id of the highest point for your driver
   * to attempt to learn by default
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#max
   */
  public BDdfIdParams getMax() { return (BDdfIdParams)get(max); }
  
  /**
   * Set the <code>max</code> property.
   * This is the id of the highest point for your driver
   * to attempt to learn by default
   * @see com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryPreferences#max
   */
  public void setMax(BDdfIdParams v) { set(max,v,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadPointDiscoveryPreferences.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/ 



}