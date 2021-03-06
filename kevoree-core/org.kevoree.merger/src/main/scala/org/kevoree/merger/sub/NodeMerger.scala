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

package org.kevoree.merger.sub

import org.kevoree.ComponentInstance
import org.kevoree.ContainerNode
import org.kevoree.ContainerRoot
import scala.collection.JavaConversions._
import org.kevoree.framework.aspects.KevoreeAspects._


trait NodeMerger extends InstanceMerger {

  def mergeAllNode(actualModel : ContainerRoot,modelToMerge : ContainerRoot)={

    var toMergeNodes = modelToMerge.getNodes.toList ++ List()
    toMergeNodes.foreach{toMergeNode=> mergeNode(actualModel,toMergeNode)  }

  }

  private def mergeNode(actualModel : ContainerRoot,nodeToMerge : ContainerNode)= {
    actualModel.getNodes.find(loopNode=> loopNode.getName == nodeToMerge.getName ) match {
      case None => {
          actualModel.getNodes.add(nodeToMerge);
          mergeAllInstances(actualModel,nodeToMerge,nodeToMerge)
      }
      case Some(eNode) => mergeAllInstances(actualModel,eNode,nodeToMerge)
    }
  }
  
  private def mergeAllInstances(actualModel : ContainerRoot,targetInstance:ContainerNode,instanceToMerge : ContainerNode)={
    var componentsList = List() ++ instanceToMerge.getComponents
    componentsList.foreach{c=>
      targetInstance.getComponents.find(eC=> eC.isModelEquals(c) ) match {

        case None => {
            targetInstance.getComponents.add(c)
            mergeComponentInstance(actualModel,c)
        }

        case Some(e)=> 
          mergeComponentInstance(actualModel,c)
          //TODO CHECK BINDING

      }
    }
  }


}
