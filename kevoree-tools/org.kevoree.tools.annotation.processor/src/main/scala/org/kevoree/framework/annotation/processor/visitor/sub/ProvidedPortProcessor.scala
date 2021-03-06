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
import org.kevoree.ComponentType
import org.kevoree.framework.annotation.processor.LocalUtility
import org.kevoree.framework.annotation.processor.visitor.ServicePortTypeVisitor
import scala.collection.JavaConversions._

trait ProvidedPortProcessor {

  def processProvidedPort(componentType: ComponentType, classdef: TypeDeclaration, env: AnnotationProcessorEnvironment) = {

    //Collects all ProvidedPort annotations and creates a list
    var providedPortAnnotations: List[org.kevoree.annotation.ProvidedPort] = Nil

    val annotationProvided = classdef.getAnnotation(classOf[org.kevoree.annotation.ProvidedPort])
    if (annotationProvided != null) {
      providedPortAnnotations = providedPortAnnotations ++ List(annotationProvided)
    }

    val annotationProvides = classdef.getAnnotation(classOf[org.kevoree.annotation.Provides])
    if (annotationProvides != null) {
      providedPortAnnotations = providedPortAnnotations ++ annotationProvides.value.toList
    }

    //For each annotation in the list
    providedPortAnnotations.foreach {
      providedPort =>

      //Check if a port with the same name exist in the component scope
        val allComponentPorts: List[org.kevoree.PortTypeRef] = componentType.getRequired.toList ++ componentType.getProvided.toList
        allComponentPorts.find(existingPort => existingPort.getName == providedPort.name) match {

          //Port is unique and can be created
          case None => {

            val portTypeRef = KevoreeFactory.eINSTANCE.createPortTypeRef
            portTypeRef.setName(providedPort.name)

            //sets the reference to the type of the port
            portTypeRef.setRef(LocalUtility.getOraddPortType(providedPort.`type` match {

              case org.kevoree.annotation.PortType.SERVICE => {
                //Service port
                val visitor = new ServicePortTypeVisitor
                try {
                  providedPort.className
                } catch {
                  case e: com.sun.mirror.`type`.MirroredTypeException =>

                     //Checks the kind of the className attribute of the annotation
                    if (!e.getTypeMirror.toString.equals("java.lang.Void")) {
                      e.getTypeMirror.accept(visitor)
                    } else {
                      env.getMessager.printError("The className attribute of a Provided ServicePort declaration is mandatory, and must be a Class or an Interface.\n"
                        + "Have a check on ProvidedPort[name=" + providedPort.name + "] of " + componentType.getBean + "\n"
                        + "TypeMirror of " + providedPort.name + ", typeMirror : " + e.getTypeMirror + ",  qualifiedName : " + e.getQualifiedName + ", typeMirrorClass : " + e.getTypeMirror.getClass + "\n")
                    }

                }

                visitor.getDataType
              }

              case org.kevoree.annotation.PortType.MESSAGE => {
                //Message port
                val messagePortType = KevoreeFactory.eINSTANCE.createMessagePortType
                messagePortType.setName("org.kevoree.framework.MessagePort")
                providedPort.filter.foreach {
                  ndts =>
                    val newTypedElement = KevoreeFactory.eINSTANCE.createTypedElement
                    newTypedElement.setName(ndts)
                    messagePortType.getFilters.add(LocalUtility.getOraddDataType(newTypedElement))
                }
                messagePortType
              }
              case _ => null
            }))

            componentType.getProvided.add(portTypeRef)
          }

          //Two ports have the same name in the component scope
          case Some(e) => {
            env.getMessager.printError("Port name duplicated in " + componentType.getName + " Scope => " + providedPort.name)
          }
        }

    }
  }

}
