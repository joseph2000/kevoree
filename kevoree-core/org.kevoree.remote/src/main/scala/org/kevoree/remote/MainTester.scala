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

package org.kevoree.remote;

import rest.KevoreeRemoteBean

object MainTester {

  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]) {

    System.setProperty("org.kevoree.remote.provisioning","file:///Users/ffouquet/.m2/repository")

    val component = new KevoreeRemoteBean(null)
    component.start()
    Thread.sleep(30*1000)
    component.stop()

  }

}
