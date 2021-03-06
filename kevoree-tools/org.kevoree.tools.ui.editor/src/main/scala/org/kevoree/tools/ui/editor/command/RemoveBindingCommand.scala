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
package org.kevoree.tools.ui.editor.command

import reflect.BeanProperty
import org.kevoree.tools.ui.editor.KevoreeUIKernel
import org.kevoree.tools.ui.editor.aspects.MBindingAspect
import org.kevoree.tools.ui.editor.aspects.Art2UIAspects._
import org.kevoree.MBinding

/**
 * User: ffouquet
 * Date: 04/07/11
 * Time: 10:28
 */

class RemoveBindingCommand(mb:MBinding) extends Command {

  @BeanProperty
  var kernel: KevoreeUIKernel = null

  def execute(p: AnyRef) {

    mb.removeModelAndUI(kernel)
    kernel.getEditorPanel.unshowPropertyEditor()
  }

}