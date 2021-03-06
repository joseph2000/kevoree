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

package org.kevoree.tools.ui.editor.command

import java.net.URL
import javax.swing.JOptionPane
import org.kevoree.tools.ui.editor.KevoreeUIKernel

class LoadRemoteModelUICommand extends Command {
  
  var kernel: KevoreeUIKernel = null

  def setKernel(k: KevoreeUIKernel) = kernel = k
  
  private var lcommand = new LoadModelCommand();
  
  def execute(p: Object) = {
    
    //ASK USER FOR ADRESS & PORT
    try{
      var result = JOptionPane.showInputDialog("Remote target node : ip@port", "localhost:8000")
      if (result != null && result != "") {
        var results = result.split(":").toList
        if(results.size >= 2){
          var ip = results(0)
          var port = results(1)
          //CALL POST REMOTE URL
          var url = new URL("http://"+ip+":"+port+"/model/current");

          lcommand.setKernel(kernel)
          lcommand.execute(url)
          
          /*var conn = url.openConnection();
          conn.setConnectTimeout(2000);
          var inputStream = conn.getInputStream
        */
        }
      }
    } catch {
      case _ @ e => e.printStackTrace
    }

  }
  
}
