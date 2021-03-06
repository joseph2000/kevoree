/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kevoree.adaptation.deploy.osgi.command

import org.kevoree._
import framework.{PrimitiveCommand, KevoreeChannelFragment, Constants}
import org.kevoree.adaptation.deploy.osgi.context.KevoreeDeployManager
import org.kevoree.framework.message.FragmentBindMessage
import org.slf4j.LoggerFactory
import scala.collection.JavaConversions._

case class AddFragmentBindingCommand(c : Channel,remoteNodeName:String, ctx : KevoreeDeployManager,nodeName:String) extends PrimitiveCommand {

  var logger = LoggerFactory.getLogger(this.getClass)

  def execute() : Boolean= {
    
    val KevoreeChannelFound = ctx.bundleMapping.find(map=>map.objClassName == c.getClass.getName && map.name == c.getName) match {
      case None => logger.error("Channel Fragment Mapping not found");None
      case Some(mapfound)=> {
          val channelBundle = ctx.getBundleContext().getBundle(mapfound.bundleId)
          channelBundle.getRegisteredServices.find({sr=> sr.getProperty(Constants.KEVOREE_NODE_NAME)==nodeName && sr.getProperty(Constants.KEVOREE_INSTANCE_NAME)==c.getName }) match {
            case None => logger.error("Channel Fragment Service not found");None
            case Some(sr)=> Some(channelBundle.getBundleContext.getService(sr).asInstanceOf[KevoreeChannelFragment])}}
    }

    KevoreeChannelFound match {
      case None => false
      case Some(channel)=> {
          val bindmsg = new FragmentBindMessage
          bindmsg.setChannelName(c.getName)
          bindmsg.setFragmentNodeName(remoteNodeName)
          (channel !? bindmsg).asInstanceOf[Boolean]
        }
    }
  }

  def undo() {
    RemoveFragmentBindingCommand(c,remoteNodeName,ctx,nodeName).execute
  }

}
