/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver;

import javax.baja.sys.*;
import javax.baja.util.*;

import com.tridium.ddf.*;
import com.tridium.ddf.discover.*;

import com.hsnc.squad.axdriver.point.*;
import com.hsnc.squad.axdriver.discover.*;

public class BAxSquadPointDeviceExt
  extends BDdfPointDeviceExt
{
  /*-
  class BAxSquadPointDeviceExt
  {
    properties
    {
      discoveryPreferences : BDdfDiscoveryPreferences
        -- This saves the last set of discovery parameters that the user provided.
        -- It also allows the easy driver framework to automatically learn points
        flags{hidden}
        default{[ new BAxSquadPointDiscoveryPreferences()]}
    }
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.BAxSquadPointDeviceExt(2541016917)1.0$ @*/
/* Generated Mon Dec 28 21:29:45 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "discoveryPreferences"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>discoveryPreferences</code> property.
   * This saves the last set of discovery parameters that
   * the user provided. It also allows the easy driver framework to automatically learn points
   * @see com.hsnc.squad.axdriver.BAxSquadPointDeviceExt#getDiscoveryPreferences
   * @see com.hsnc.squad.axdriver.BAxSquadPointDeviceExt#setDiscoveryPreferences
   */
  public static final Property discoveryPreferences = newProperty(Flags.HIDDEN, new BAxSquadPointDiscoveryPreferences(),null);
  
  /**
   * Get the <code>discoveryPreferences</code> property.
   * This saves the last set of discovery parameters that
   * the user provided. It also allows the easy driver framework to automatically learn points
   * @see com.hsnc.squad.axdriver.BAxSquadPointDeviceExt#discoveryPreferences
   */
  public BDdfDiscoveryPreferences getDiscoveryPreferences() { return (BDdfDiscoveryPreferences)get(discoveryPreferences); }
  
  /**
   * Set the <code>discoveryPreferences</code> property.
   * This saves the last set of discovery parameters that
   * the user provided. It also allows the easy driver framework to automatically learn points
   * @see com.hsnc.squad.axdriver.BAxSquadPointDeviceExt#discoveryPreferences
   */
  public void setDiscoveryPreferences(BDdfDiscoveryPreferences v) { set(discoveryPreferences,v,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadPointDeviceExt.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  /**
   * Associates BAxSquadPointDeviceExt to BAxSquadDevice.
   */
  public Type getDeviceType()
  {
    return BAxSquadDevice.TYPE;
  }
  
  /**
   * Associates BAxSquadPointDeviceExt to BAxSquadPointFolder.
   */
  public Type getPointFolderType()
  {
    return BAxSquadPointFolder.TYPE;
  }
  
  /**
   * Associates BAxSquadPointDeviceExt to BAxSquadProxyExt
   */
  public Type getProxyExtType()
  {
    return BAxSquadProxyExt.TYPE;
  }
  
  /**
   * This can be left null, depending on how you decide to define
   * the discovery behavior in your driver. We will visit the
   * discovery process in further detail during another day's
   * lesson.
   */
  public BFolder getDiscoveryFolder()
  {
    return null;
  }
  
}