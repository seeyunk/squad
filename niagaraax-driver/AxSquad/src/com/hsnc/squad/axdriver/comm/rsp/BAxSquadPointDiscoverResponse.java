/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.comm.rsp; 
 
import java.io.IOException;
import java.util.*; 

import javax.baja.io.ByteBuffer;
import javax.baja.sys.*;

import com.tridium.ddf.comm.*; 
import com.tridium.ddf.comm.rsp.*; 
import com.tridium.ddf.discover.*;
import com.hsnc.ax.packet.IPacket;
import com.hsnc.ax.packet.ReadPointsPacket;
import com.hsnc.squad.axdriver.discover.*; 
import com.hsnc.squad.axdriver.identify.*; 
 
public class BAxSquadPointDiscoverResponse 
  extends BDdfResponse 
  implements BIDdfDiscoverResponse 
{ 
  /*- 
  class BAxSquadPointDiscoverResponse 
  { 
  } 
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.comm.rsp.BAxSquadPointDiscoverResponse(4103381134)1.0$ @*/
/* Generated Thu Dec 24 11:29:54 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadPointDiscoverResponse.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/ 

  private byte[] receiveBytes;
  private Map results;
  
  public BAxSquadPointDiscoverResponse() 
  { 
  }
  
  /** 
   * This constructor does not necessarily need to take an IDdfDataFrame 
   * as a parameter. It could take any parameters that you wish to pass 
   * to the response from the request's processReceive method. The data 
   * that is passed to this constructor should be saved on instance variables 
   * and used in the parseDiscoveryObjects method to construct the return 
   * array. 
   */ 
  public BAxSquadPointDiscoverResponse(IDdfDataFrame receiveFrame) 
  { 
    // TODO: Make a copy of any bytes that you need from the receiveFrame 
    //       since the receive frame could be part of an internal buffer 
    //       of the receiver.
    
    // SUGGESTION: Plan on using This 'cached' data in the parseDiscoveryObjects
    // method below.
	  ByteBuffer buffer = new ByteBuffer( receiveFrame.getFrameBytes() );
	  try {
		  if ( buffer.readByte() == IPacket.SOH ) {
			  int payloadLen = buffer.readInt();
			  byte command = buffer.readByte();
			  this.receiveBytes = new byte[ payloadLen - 5 ];
			  for ( int n = 0; n < receiveBytes.length; n++ ) {
				  this.receiveBytes[n] = buffer.readByte();
			  }
			  
			  results = ReadPointsPacket.decode( this.receiveBytes );
		  }	
	  } catch (Exception e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
	  }             
  }
  
  /** 
   * This method parses the response byte array and returns an 
   * array of BAxSquadPointDiscoveryLeaf objects describing 
   * the data points that this response is able to identify. 
   * This is called during the auto discovery process. 
   */ 
  public BIDdfDiscoveryObject[] parseDiscoveryObjects(Context c) 
  { 
    // TODO: Convert the 'cached' data into an array of 
    // BAxSquadPointDiscoveryLeaf objects. Each particular
    // discovery leaf object should identify one atomic data
    // point in the field-device. An atomic data point is one that
    // can be represented in Niagara as a BStringPoint, BNumericPoint,
    // BEnumPoint, or BBooleanPoint. Each object in the array
    // represents an atomic data point available in or accessible by
    // the field-device. A row will automatically appear in the point
    // manager for each object in the array that is returned. Good luck.
	  final int length = this.results.size();
	  BAxSquadPointDiscoveryLeaf[] points = new BAxSquadPointDiscoveryLeaf[length];
	  Iterator i = this.results.keySet().iterator();
	  for ( int n = 0; i.hasNext(); n++ ) {
		  String pointId = (String)i.next();
		  BAxSquadPointDiscoveryLeaf point = new BAxSquadPointDiscoveryLeaf();
		  ((BAxSquadPointId)(point.getPointId())).setSeq( n );
		  ((BAxSquadReadParams)(point.getReadParameters())).setPointId( pointId );
		  points[n] = point;
	  }
	  
	  return points;
  }

}