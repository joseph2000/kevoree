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
package org.kevoree.tools.marShellTransform

import scala.collection.JavaConversions._
import org.kevoree.tools.marShell.ast._
import org.kevoreeAdaptation._
import org.slf4j.LoggerFactory
import org.kevoree.kompare.JavaSePrimitive
import org.kevoree._

object AdaptationModelWrapper {

	var logger = LoggerFactory.getLogger(this.getClass);

  def generateScriptFromAdaptModel(model: AdaptationModel): Script = {
    val statments = new java.util.ArrayList[Statment]()
    model.getAdaptations.foreach {
      adapt =>
      adapt.getPrimitiveType.getName match {
        case JavaSePrimitive.UpdateDictionaryInstance => {
            val dictionary = new java.util.Properties
            if(adapt.getRef.asInstanceOf[Instance].getDictionary != null){
              adapt.getRef.asInstanceOf[Instance].getDictionary.getValues.foreach{value =>
                dictionary.put(value.getAttribute.getName, value.getValue)
              }
            }
            adapt.getRef match {
              case ci : ComponentInstance => statments.add(UpdateDictionaryStatement(ci.getName,Some(ci.eContainer.asInstanceOf[ContainerNode].getName),dictionary))
              case ci : Channel => statments.add(UpdateDictionaryStatement(ci.getName,None,dictionary))  
              case _ => //TODO GROUP
            }
          } //statments.add(UpdateDictionaryStatement(statement.getRef.g))
        case JavaSePrimitive.AddBinding => statments.add(AddBindingStatment(ComponentInstanceID(adapt.getRef.asInstanceOf[org.kevoree.MBinding].getPort.eContainer.asInstanceOf[ComponentInstance].getName, Some(adapt.getRef.asInstanceOf[MBinding].getPort.eContainer.eContainer.asInstanceOf[ContainerNode].getName)), adapt.getRef.asInstanceOf[MBinding].getPort.getPortTypeRef.getName, adapt.getRef.asInstanceOf[MBinding].getHub.getName))
        case JavaSePrimitive.AddInstance => {

            val props = new java.util.Properties
            if (adapt.getRef.asInstanceOf[Instance].getDictionary != null) {
              adapt.getRef.asInstanceOf[Instance].getDictionary.getValues.foreach {
                value =>
                props.put(value.getAttribute.getName, value.getValue)
              }
            }

            adapt.getRef match {
              case c: Group => statments.add(AddGroupStatment(c.getName, c.getTypeDefinition.getName, props))
              case c: Channel => statments.add(AddChannelInstanceStatment(c.getName, c.getTypeDefinition.getName, props))
              case c: ComponentInstance => {
                  val cid = ComponentInstanceID(c.getName, Some(c.eContainer.asInstanceOf[ContainerNode].getName))
                  statments.add(AddComponentInstanceStatment(cid, c.getTypeDefinition.getName, props))
                }
                //TODO
              case _@uncatchInstance => logger.warn("uncatched=" + uncatchInstance)
            }
          }
        case JavaSePrimitive.RemoveBinding => statments.add(RemoveBindingStatment(ComponentInstanceID(adapt.getRef.asInstanceOf[MBinding].getPort.eContainer.asInstanceOf[ComponentInstance].getName, Some(adapt.getRef.asInstanceOf[MBinding].getPort.eContainer.eContainer.asInstanceOf[ContainerNode].getName)), adapt.getRef.asInstanceOf[MBinding].getPort.getPortTypeRef.getName, adapt.getRef.asInstanceOf[MBinding].getHub.getName))
        case JavaSePrimitive.RemoveInstance => {
            adapt.getRef match {
              case c: Group => statments.add(RemoveGroupStatment(c.getName))
              case c: Channel => statments.add(RemoveChannelInstanceStatment(c.getName))
              case c: ComponentInstance => {
                  val cid = ComponentInstanceID(c.getName, Some(c.eContainer.asInstanceOf[ContainerNode].getName))
                  statments.add(RemoveComponentInstanceStatment(cid))
                }

              case _@uncatchInstance => logger.warn("uncatched=" + uncatchInstance)
            }
          }
        case _@unCatched => logger.warn("uncatched=" + unCatched)
      }

    }
    Script(List(TransactionalBloc(statments.toList)))
  }

}
