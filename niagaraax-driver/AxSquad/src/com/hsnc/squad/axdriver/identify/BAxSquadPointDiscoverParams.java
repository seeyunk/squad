/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.identify;

import javax.baja.sys.BFacets;
import javax.baja.sys.BInteger;
import javax.baja.sys.Property;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

import com.hsnc.squad.axdriver.comm.req.BAxSquadPointDiscoverRequest;
import com.hsnc.squad.axdriver.discover.BAxSquadPointDiscoveryLeaf;
import com.tridium.ddf.DdfFacets;
import com.tridium.ddf.identify.BDdfDiscoverParams;
import com.tridium.ddf.identify.BIDdfDiscoverParams;

public class BAxSquadPointDiscoverParams
  extends BDdfDiscoverParams
{
  // Hypothetical protocol example where a device can report about
  // data points that are connected to it based on a group index.
  public static final int MIN_GROUP_ID = 0;  // Hypothetical min group id = 0
  public static final int MAX_GROUP_ID = 1; // Hypothetical max group id = 10
  
  /*-
  class BAxSquadPointDiscoverParams
  {
    properties
    {
     	groupNumber : int
        -- This would work in a hypothetical protocol where a device
        -- is capable of reporting field-points by group index.
        default{[0]}
        slotfacets{[DdfFacets.combine(MGR_INCLUDE,
                    BFacets.make( BFacets.MIN, BInteger.make(MIN_GROUP_ID) ),
                    BFacets.make( BFacets.MAX, BInteger.make(MAX_GROUP_ID)) ) ]}
    
    }
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.identify.BAxSquadPointDiscoverParams(1731427481)1.0$ @*/
/* Generated Mon Dec 28 21:56:12 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Property "groupNumber"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the <code>groupNumber</code> property.
   * This would work in a hypothetical protocol where a
   * device is capable of reporting field-points by group
   * index.
   * @see com.hsnc.squad.axdriver.identify.BAxSquadPointDiscoverParams#getGroupNumber
   * @see com.hsnc.squad.axdriver.identify.BAxSquadPointDiscoverParams#setGroupNumber
   */
  public static final Property groupNumber = newProperty(0, 0,DdfFacets.combine(MGR_INCLUDE,
                    BFacets.make( BFacets.MIN, BInteger.make(MIN_GROUP_ID) ),
                    BFacets.make( BFacets.MAX, BInteger.make(MAX_GROUP_ID)) ));
  
  /**
   * Get the <code>groupNumber</code> property.
   * This would work in a hypothetical protocol where a
   * device is capable of reporting field-points by group
   * index.
   * @see com.hsnc.squad.axdriver.identify.BAxSquadPointDiscoverParams#groupNumber
   */
  public int getGroupNumber() { return getInt(groupNumber); }
  
  /**
   * Set the <code>groupNumber</code> property.
   * This would work in a hypothetical protocol where a
   * device is capable of reporting field-points by group
   * index.
   * @see com.hsnc.squad.axdriver.identify.BAxSquadPointDiscoverParams#groupNumber
   */
  public void setGroupNumber(int v) { setInt(groupNumber,v,null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadPointDiscoverParams.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/
  
  /**
   * Niagara AX requires a public, empty constructor, so that it can perform
   * Its own introspection operations.
   */
  public BAxSquadPointDiscoverParams()
  {
    
  }
  
  /**
   * Full specified constructor
   */
  public BAxSquadPointDiscoverParams(int groupNumber)
  {
	//this.groupId = groupId;
    setGroupNumber(groupNumber);
  }  

  public BIDdfDiscoverParams getFirst()
  {
    // TODO: Return an instance of this class that encapsulates the data
    // that would be transmitted as the byte array for the point
    // discovery request in order to request the first known point or
    // group of points in the field-device. Good luck. The following
    // auto-generated return would work for the hypothetical example
    // that this auto-generated class illustrates.
    return new BAxSquadPointDiscoverParams(MIN_GROUP_ID);
  }

  public BIDdfDiscoverParams getLast()
  {
    // TODO: Return an instance of this class that encapsulates the data
    // that would be transmitted as the byte array for the point
    // discovery request in order to request the last known point or
    // group of points in the field-device. Good luck!
    
    // NOTE: The following auto-generated return would work for the
    // hypothetical example that this auto-generated class illustrates.
    return new BAxSquadPointDiscoverParams(MAX_GROUP_ID); // TODO:
  }

  public BIDdfDiscoverParams getNext()
  {
    // TODO: Analyze the current instance of this class and return another
    // instance of this class that encapsulates the data that would be
    // transmitted as the byte array for the point discovery request
    // in order to request the next known point or group of points
    // in the field-device. Good luck.
    
    // NOTE: The following auto-generated return would work for the
    // hypothetical example that this auto-generated class illustrates.
    //return new BAxSquadPointDiscoverParams( ++groupId );
	  	int nextGroup = getGroupNumber() + 1;
		if (nextGroup >= MAX_GROUP_ID)
		  nextGroup = MAX_GROUP_ID;
		
		return new BAxSquadPointDiscoverParams( nextGroup ); // TODO:
  }
  
  /*
  public int getGroupNumber() {
	  return this.groupId;
  }
  */
  public boolean isAfter(BIDdfDiscoverParams anotherPtDiscParams)
  {
    // TODO: Analyze the current instance as well as the given instance
    // of this class. Return true if the current instance of this class
    // encapsulates data that would be transmitted as the byte array
    // for a point discovery request that would request a point or
    // group of points that is after those which the given instance's
    // encapsulated data would request. Good luck.
    
    // NOTE: The following auto-generated/ return would work for the
    // hypothetical example that this auto- generated class illustrates.
	  BAxSquadPointDiscoverParams otherPtDiscParams = 
		      (BAxSquadPointDiscoverParams)anotherPtDiscParams;
	  //@@System.out.println( Integer.toString( this.getGroupNumber() ) + "::"  + Integer.toString( otherPtDiscParams.getGroupNumber() ) );
	  if ( this.getGroupNumber() > MIN_GROUP_ID && this.getGroupNumber() == otherPtDiscParams.getGroupNumber() ) {
		  return true;
	  }
	  return false;
	  
    
  }

  public Type getDiscoverRequestType()
  {
    return BAxSquadPointDiscoverRequest.TYPE;
  }

  
  public Type[] getDiscoverRequestTypes() {
	// TODO Auto-generated method stub
	return new Type[] { BAxSquadPointDiscoverRequest.TYPE };
  }

/**
   * This tells the developer driver framework that
   * instances of BAxSquadDiscoveryLeaf will be
   * placed into the discovery list of the point
   * manager to represent each data point that the
   * driver discovers.
   */
  public Type getDiscoveryLeafType()
  {
    return BAxSquadPointDiscoveryLeaf.TYPE;
  }

}