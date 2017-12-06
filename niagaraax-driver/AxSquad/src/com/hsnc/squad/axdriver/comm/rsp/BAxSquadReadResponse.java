/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.comm.rsp;

import java.io.IOException;
import java.util.Map;

import javax.baja.control.BControlPoint;
import javax.baja.control.BEnumPoint;
import javax.baja.control.BStringPoint;
import javax.baja.io.ByteBuffer;
import javax.baja.status.BStatusBoolean;
import javax.baja.status.BStatusEnum;
import javax.baja.status.BStatusNumeric;
import javax.baja.status.BStatusString;
import javax.baja.status.BStatusValue;
import javax.baja.sys.BDynamicEnum;
import javax.baja.sys.BIBoolean;
import javax.baja.sys.BIEnum;
import javax.baja.sys.BINumeric;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;

import com.hsnc.ax.packet.IPacket;
import com.hsnc.ax.packet.ReadPointsValuePacket;
import com.hsnc.squad.axdriver.identify.BAxSquadPointId;
import com.hsnc.squad.axdriver.identify.BAxSquadReadParams;
import com.hsnc.squad.axdriver.point.BAxSquadProxyExt;
import com.tridium.ddf.comm.IDdfDataFrame;
import com.tridium.ddf.comm.req.IDdfReadable;
import com.tridium.ddf.comm.rsp.BDdfResponse;
import com.tridium.ddf.comm.rsp.BIDdfReadResponse;
import com.tridium.ddf.comm.rsp.DdfResponseException;

public class BAxSquadReadResponse
  extends BDdfResponse
  implements BIDdfReadResponse {
  /*-
  class BAxSquadReadResponse
  {
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.comm.rsp.BAxSquadReadResponse(849082468)1.0$ @*/
/* Generated Thu Dec 24 11:29:54 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadReadResponse.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/


  /**
   * This constructor is called by the processReceive method of
   * BAxSquadReadRequest.
   *
   * @param a reference to the data frame that matches up with
   * the request that was recently transmitted. The byte array
   * in this frame could be a direct reference to the dev
   * communicator's receiver's internal byte array so this
   * constructor copies the bytes into a safe instance array.
   * 
   * @param a reference to the read parameters structure that the
   * read request used to construct its own byte array.
   * 
   * @throws DdfResponseException if the given frame contains
   * information from the field-device that indicates that the
   * read request was unsuccessful or otherwise denied.
   */
  public BAxSquadReadResponse(IDdfDataFrame receiveFrame, BAxSquadReadParams readParams)
    throws DdfResponseException
  {
	  this.readParams = (BAxSquadReadParams)readParams.newCopy();
	  ByteBuffer buffer = new ByteBuffer( receiveFrame.getFrameBytes() );
	  try {
		if ( buffer.readByte() == IPacket.SOH ) {
			  int payloadLen = buffer.readInt();
			  byte command = buffer.readByte();

			  this.receiveBytes = new byte[ payloadLen - 5 ];
			  for ( int n = 0; n < receiveBytes.length; n++ ) {
				  this.receiveBytes[n] = buffer.readByte();
			  }

		  }
	  } catch (IOException e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
	  }                                
  }

  /**
   * This empty constructor allows Niagara AX to instantiate
   * a client-side proxy of this request. It is not presently
   * used but could be required in future versions of the
   * developer driver framework.
   */
  public BAxSquadReadResponse()
  {

  }

  /**
   * Implementing this method is fundamental to the ddf's
   * auto-poll feature for driver points. When one or more driver points
   * under a device need polled that share the equivalent read parameters,
   * the ddf will instantiate the read request type that
   * is identified by the read parameters, assign the read parameters to the
   * read request, assign all points that share the read parameters to the
   * request, and transmit the read request. Upon receiving a successful
   * read response (an instance that implements this interface), the ddf
   * driver framework will loop through all of the points under the device
   * that shared the same read parameters, cast each point to IDdfReadable,
   * and pass each point successively to this method. The developer driver
   * framework will take the return value from this method and pass it
   * to the readOk method on the point, thereby updating its value in Niagara.
   *
   * When implementing this interface, driver developers must implement this
   * method and parse a BStatusValue from the response data for the given
   * readableSource. If necessary, we suggest that the driver developer can
   * check if the readableSource object is an instance of their driver's proxy
   * extension class. If so, the driver developer can cast the readableSource
   * object to their driver's proxy extension class and then access the point's
   * pointId property. The driver developer should design the pointId property
   * in such a way that it provides the information necessary to parse the
   * particular point's value from the read response.
   *
   *
   * @param readableSource
   * @return a BStatusValue to pass to the readOk method of the readableSource
   */
  private ReadPointsValuePacket response = null;
  public BStatusValue parseReadValue(IDdfReadable readableSource)
  { 
	  if (readableSource instanceof BAxSquadProxyExt)
	  { 
		  // Casts the given readableSource into a BAxSquadDriverProxyExt
		  BAxSquadProxyExt proxy
		    = (BAxSquadProxyExt)readableSource;
		  
		  return getReadValue( proxy, this.readParams.getPointId() );
		}
		else
		{
		  return null;
		}
  }
  
  /**
   * This private method is called from the parseReadValue method.
   * It is not required by the developer driver framework, instead, 
   * this method exists in order to shorten the parseReadValue method.
   *
   * @param a reference to the BAxSquadProxyExt to parse the read
   * value for.
   *
   * @param a reference to the BAxSquadPointId to use to tell us
   * how to parse the read value from the response bytes.
   *
   * @return a BStatusNumeric, BStatusBoolean, BStatusEnum, or
   * BStatusString that appropriately matches the proxy's control
   * point type. If the proxy's control point is a BNumericWritable
   * or BNumericPoint then this will return a BStatusNumeric that
   * represents the present value of the point. If the proxy's
   * control point is a BBooleanWritable or a BBooleanPoint then
   * this returns a BStatusBoolean that represents the current value
   * of the point. If the proxy's control point is a BEnumWritable
   * or BEnumPoint then this returns BStatusEnum that represents the
   * current value of the point. If this proxy's control point is a
   * BStringPoint or BStringWritable then this returns a BStatusString
   * that represents the current value of the point.
   */
  private BStatusValue getReadValue(BAxSquadProxyExt proxy,
      String pointId)
  {	
	  Map result = ReadPointsValuePacket.decode( this.receiveBytes );
	  BControlPoint controlPoint = proxy.getParentPoint();
	  if ( controlPoint instanceof BStringPoint ) {
		  return new BStatusString( (String)result.get( pointId ) ); 
	  }
	  
	  return new BStatusString( "Invalid pointId" );
  }
  
  
  
  
  
    
////////////////////////////////////////////////////////////////
// Attributes
////////////////////////////////////////////////////////////////
  
  private byte[] receiveBytes; // This is populated by the constructor
  private BAxSquadReadParams readParams; // This is populated by the constructor

}