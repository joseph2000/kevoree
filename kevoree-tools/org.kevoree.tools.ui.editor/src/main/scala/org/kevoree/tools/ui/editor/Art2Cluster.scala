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
///**
// * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * 	http://www.gnu.org/licenses/lgpl-3.0.txt
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package org.kevoree.tools.ui.editor
//
//import java.io.ByteArrayInputStream
//import org.jgroups.JChannel
//import org.jgroups.Message
//import org.kevoree.framework.Art2XmiHelper
//
//object Art2Cluster {
//  var jchannel  : JChannel = null
//  def start = {
//    //System.setProperty("java.net.preferIPv4Stack", "true");
//    jchannel = new JChannel//(this.getClass.getClassLoader.getResource("udp.xml"))
//    jchannel.connect("Art2DefaultNameSpace")
//    jchannel.setReceiver(new org.jgroups.ReceiverAdapter(){
//        override def receive(msg : Message )={
//          println("ART2 Cluster Mesage =>"+msg)
//        }
//        override def getState : Array[Byte] = synchronized {
//          println("Cluster GetState")
//          super.getState
//        }
//        override def setState(state : Array[Byte])= synchronized {
//
//          println("Set STATE")
//
//          var input = new ByteArrayInputStream(state);
//          var root = Art2XmiHelper.loadStream(input)
//
//          println("Cluster SetState")
//          println(root)
//
//        }
//
//      })
//    jchannel.getState(null, 5000)
//
//    Runtime.getRuntime().addShutdownHook(new Thread() {
//        override def run() {
//          println("Close Channel Cluster")
//          jchannel.close
//        }
//      })
//
//  }
//
//  def push(content : String)= {
//    println("Push model ")
//    var msg=new Message(null, null, content);
//    jchannel.send(msg);
//  }
//
//
//}
