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

package org.kevoree.framework.annotation.processor.visitor.sub

import com.sun.mirror.apt.AnnotationProcessorEnvironment
import com.sun.mirror.declaration.TypeDeclaration
import org.kevoree.KevoreeFactory
import org.kevoree.ContainerRoot
import org.kevoree.NodeType
import org.kevoree.TypeDefinition
import scala.collection.JavaConversions._


/* Common Sub process to deal with ThirdParty definition */

trait ThirdPartyProcessor {

  def processThirdParty(componentType : TypeDefinition,classdef : TypeDeclaration,env : AnnotationProcessorEnvironment)={
    val root : ContainerRoot = componentType.eContainer.asInstanceOf[ContainerRoot]

    var thirdPartyAnnotations : List[org.kevoree.annotation.ThirdParty] = Nil

    val annotationThirdParty = classdef.getAnnotation(classOf[org.kevoree.annotation.ThirdParty])
    if(annotationThirdParty != null){ thirdPartyAnnotations = thirdPartyAnnotations ++ List(annotationThirdParty) }

    val annotationThirdParties = classdef.getAnnotation(classOf[org.kevoree.annotation.ThirdParties])
    if(annotationThirdParties != null){ thirdPartyAnnotations = thirdPartyAnnotations ++ annotationThirdParties.value.toList }

    
    val thirdParties = env.getOptions.find({op => op._1.contains("thirdParties")}).getOrElse{("key=","")}._1.split('=').toList.get(1)
    val thirdPartiesList : List[String] = thirdParties.split(";").filter(r=> r != null && r != "").toList

    val nodeTypeNames = env.getOptions.find({op => op._1.contains("nodeTypeNames")}).getOrElse{("key=","")}._1.split('=').toList.get(1)
    val nodeTypeNameList : List[String] = nodeTypeNames.split(";").filter(r=> r != null && r != "").toList
    
    
    /* CHECK THIRDPARTIES */
    thirdPartyAnnotations.foreach{tp=>
      root.getDeployUnits.find({etp => etp.getName == tp.name}) match {
        case Some(e) => {
            componentType.getDeployUnits.get(0).getRequiredLibs.add(e)
          }
        case None => {
            val newThirdParty = KevoreeFactory.eINSTANCE.createDeployUnit
            newThirdParty.setName(tp.name)
            newThirdParty.setUrl(tp.url)
            root.getDeployUnits.add(newThirdParty)
            componentType.getDeployUnits.get(0).getRequiredLibs.add(newThirdParty)
          }
      }
    }
    
    /* CHECK TP from POM */
    thirdPartiesList.foreach{tp=>
      val name = tp.split("!").toList.get(0)
      val url = tp.split("!").toList.get(1)
      
      root.getDeployUnits.find({etp => etp.getName == name}) match {
        case Some(e) => {
            componentType.getDeployUnits.get(0).getRequiredLibs.add(e)
          }
        case None => {
            val newThirdParty = KevoreeFactory.eINSTANCE.createDeployUnit
            newThirdParty.setName(name)
            newThirdParty.setUrl(url)
            root.getDeployUnits.add(newThirdParty)
            componentType.getDeployUnits.get(0).getRequiredLibs.add(newThirdParty)
          }
      }
    }
    
    /* POST PROCESS ADD NODE TYPE TO ALL THIRDPARTY */
    componentType.getDeployUnits.get(0).getRequiredLibs.foreach{tp =>
      nodeTypeNameList.foreach{nodeTypeName=>
        /* ROOT ADD NODE TYPE IF NECESSARY */
        nodeTypeNameList.foreach{nodeTypeName =>
          componentType.eContainer.asInstanceOf[ContainerRoot].getTypeDefinitions.filter(p=> p.isInstanceOf[NodeType]).find(nt => nt.getName == nodeTypeName) match {
            case Some(existingNodeType)=>tp.setTargetNodeType(existingNodeType.asInstanceOf[NodeType])
            case None => {
                val nodeType = KevoreeFactory.eINSTANCE.createNodeType
                nodeType.setName(nodeTypeName)
                root.getTypeDefinitions.add(nodeType)
                tp.setTargetNodeType(nodeType)
              }
          }
        }
        
      }
    }
  }
}
