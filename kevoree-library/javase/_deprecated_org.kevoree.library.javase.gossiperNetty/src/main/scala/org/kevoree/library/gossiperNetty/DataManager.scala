/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kevoree.library.gossiperNetty

import java.util.UUID
import version.Version.VectorClock

trait DataManager {
  def stop()
  def getData(uuid : UUID) : Tuple2[VectorClock,Any]
  def setData(uuid : UUID, tuple : Tuple2[VectorClock,Any], source : String)
  def removeData(uuid : UUID)
  def getUUIDVectorClock(uuid : UUID) : VectorClock
  def getUUIDVectorClocks() : java.util.Map[UUID, VectorClock]
  def mergeClock(uid: UUID,v : VectorClock, source : String):VectorClock

	// TODO include garbage into DataManager
	//def garbage()
}
