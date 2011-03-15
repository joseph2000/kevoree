/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kevoree.library.gossiper

import java.util.UUID
import org.kevoree.library.gossiper.version.GossiperMessages.VectorClock
import org.kevoree.library.gossiper.version.GossiperMessages.VersionedModel

trait GossiperChannel {

    def getMsgUUIDSFromPeer(nodeName : String) : java.util.List[UUID]
    def getUUIDVectorClockFromPeer(nodeName:String,uuid:UUID) : VectorClock
    def getUUIDDataFromPeer(nodeName:String,uuid:UUID) : VersionedModel
   // def selectPeer(uuid:String)
    def notifyPeers()
    def notifyPeer(nodeName:String)
    def localDelivery(o : Object)
  
}
