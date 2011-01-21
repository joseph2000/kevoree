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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kevoree.framework

import org.kevoree.framework.message._
import org.slf4j.LoggerFactory
import scala.actors.Actor
import scala.collection.JavaConversions._
import org.kevoree.framework.aspects.Art2Aspects._

abstract class KevoreeComponent(c : AbstractComponentType) extends KevoreeActor {

  def getArt2ComponentType : ComponentType = c

  var logger = LoggerFactory.getLogger(this.getClass);

  override def internal_process(msg : Any) = msg match {

    case UpdateDictionaryMessage(d) => {
        d.keySet.foreach{v=>
          getArt2ComponentType.getDictionary.put(v, d.get(v))
        }
        reply(true)
    }
    
    case StartMessage => {
        new Actor{ def act = startComponent }.start()
        //Wake Up Hosted Port
        getArt2ComponentType.getHostedPorts.foreach{hp=>
          var port = hp._2.asInstanceOf[KevoreePort]
          if(port.isInPause){
            port.resume
          }
        }
        reply(true)
      }
    case StopMessage => {
        //Pause Hosted Port
        getArt2ComponentType.getHostedPorts.foreach{hp=>
          var port = hp._2.asInstanceOf[KevoreePort]
          if(!port.isInPause){
            port.pause
          }
        }
        new Actor{ def act= stopComponent }.start()
        reply(true)
      }
      case _ @ msg => logger.error("unknow message "+msg)
  }


  def startComponent
  def stopComponent

}