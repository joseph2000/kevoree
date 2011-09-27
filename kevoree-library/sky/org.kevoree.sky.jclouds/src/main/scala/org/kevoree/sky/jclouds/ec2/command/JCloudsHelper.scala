package org.kevoree.sky.jclouds.ec2.command

import org.kevoree.sky.jclouds.ec2.JCloudsNode
import org.jclouds.rest.RestContext
import org.slf4j.LoggerFactory
import com.google.common.collect.{Iterables, Sets}
import org.jclouds.ec2.EC2Client
import org.jclouds.ec2.domain.{Reservation, InstanceState, RunningInstance}
import java.util.{HashSet, Properties}
import com.google.common.base.Predicate

/**
 * User: Erwan Daubert - erwan.daubert@gmail.com
 * Date: 26/09/11
 * Time: 15:11
 *
 * @author Erwan Daubert
 * @version 1.0
 */

class JCloudsHelper (node: JCloudsNode) {

  private final val logger = LoggerFactory.getLogger(classOf[JCloudsHelper])

  private def initProperties: Properties = {
    val properties: Properties = new Properties
    if (node.getDictionary.get("accesskeyid") == null || node.getDictionary.get("secretkey") == null) {
      logger.error("missing value on parameter \"accesskeyid\" or \"secretkey\"")
      return null
    }
    else {
      properties.put("accesskeyid", node.getDictionary.get("accesskeyid"))
      properties.put("secretkey", node.getDictionary.get("secretkey"))
    }
    val endPoint: String = node.getDictionary.get("endPoint").asInstanceOf[String]
    if (endPoint != null && !(endPoint == "")) {
      properties.put("endpoint", endPoint)
    }
    properties
  }

  private def getComputeServiceContextFactory() : RestContext[_, _] = {
    null
  }

  private def findInstanceByKeyName (client: EC2Client, keyName: String): RunningInstance = {
    val reservations = client.getInstanceServices.describeInstancesInRegion(null)
    val allInstances = new HashSet[RunningInstance]()
    import scala.collection.JavaConversions._
    reservations.foreach{
      reservation =>
      allInstances.add(reservation)
    }
    allInstances.find(instance => instance.getKeyName == keyName && instance.getInstanceState != InstanceState.TERMINATED) match {
      case Some(i) => i
      case None => null
    }
  }

}