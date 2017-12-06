/*
 * Copyright 2015 HSNC, All Rights Reserved.
 */
package com.hsnc.squad.axdriver.comm;

import javax.baja.sys.*;

import com.hsnc.ax.packet.IPacket;
import com.tridium.ddf.comm.*;
import com.tridium.ddf.comm.defaultComm.*;
import com.tridium.ddfIp.tcp.comm.*;

public class BAxSquadReceiver
  extends BDdfTcpReceiver{

  /*-
  class BAxSquadReceiver
  {
  }
  -*/
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.hsnc.squad.axdriver.comm.BAxSquadReceiver(1933080865)1.0$ @*/
/* Generated Thu Dec 24 11:29:54 GMT+08:00 2015 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BAxSquadReceiver.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/


////////////////////////////////////////////////////////////////
// Methods
////////////////////////////////////////////////////////////////

  /**
   * This method is called by the default implementation of
   * BDdfReceiver. Its purpose is to recognize the starting
   * sequence of a data packet according to the driver's
   * protocol.
   * 
   * Descendants should override this method, and check the data
   * received so far. If the data received so far indicates that
   * this is the beginning of a data frame then the subclass
   * should return BDdfReceiver.YES. If the data cannot possibly
   * be the beginning of the frame for the driver then the
   * subclass should return BDdfReceiver.NO. If the descendant
   * needs more data before making a decision then it should
   * return BDdfReciver.MAYBE.
   * 
   * @param frameSoFar is the IDdfDataFrame that the reciever
   * is building up.
   * 
   * @return BDdfReciver.YES if the bytes that are in the given
   * frameSoFar are the start of a frame. Or BDdfReceiver.NO if
   * the bytes in the given frameSoFar cannot possibly be the
   * start of a frame. Or BDdfReciver.MAYBE if the bytes in the
   * given frameSoFar could be the start of a frame but more
   * bytes would be required to make a definite determination.
   */
  public int isStartOfFrame(IDdfDataFrame frameSoFar)
  {
	  if ( frameSoFar.getFrameBytes()[0] == IPacket.SOH ) {
		  return BDdfReceiver.YES;
	  }
	  
	  return BDdfReceiver.NO;
  }

  /**
   * This method is called by the default implementation of
   * BDdfReceiver. Its purpose is to recognize the ending
   * sequence of a data packet according to the driver's
   * protocol.
   * 
   * Descendants should override this method, and check
   * the data received so far. If the data received so
   * far indicates that this is a completed frame then
   * the subclass should return true. Otherwise, this
   * method should return false so that the receiver
   * can keep receiving and buffering data.
   * 
   * Descendants should override this method, and check
   * the data received so far. If the data received so
   * far indicates that this is a completed frame then
   * the subclass should return true. Otherwise, this
   * method should return false so that the receiver
   * can keep receiving and buffering data.
   */
  public boolean isCompleteFrame(IDdfDataFrame frameSoFar)
  {
	  return frameSoFar.getFrameSize() > 2 && frameSoFar.getFrameBytes()[ frameSoFar.getFrameSize() - 1 ] == IPacket.EOT;
  }

  /**
   * This method is called by the receiver after the isCompleteFrame message
   * returns true.
   * @param completeFrame the complete IDdfDataFrame received
   * @return true if the completeFrame passes check sum tests, or if no check sum
   * test is necessary (data in TCP/IP messages, for example, do not alwasy require
   * a separate check sum test since checking is built into TCP/IP). False if a check
   * is necessary and the check fails.
   */
  public boolean checkFrame(IDdfDataFrame frameSoFar)
  {
    // TODO: The given 'frameSoFar' is the same 'frameSoFar' that
    // your 'isCompleteFrame' method would have just returned 'true'
    // for. Please review this frame further and verify that it
    // passes any data integrity checks such as a check sum, CRC,
    // or LRC that your driver's protocol may require.
    return true; 
  }
}