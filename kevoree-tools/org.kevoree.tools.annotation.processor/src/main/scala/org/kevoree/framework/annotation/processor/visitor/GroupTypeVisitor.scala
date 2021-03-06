package org.kevoree.framework.annotation.processor.visitor

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

import com.sun.mirror.apt.AnnotationProcessorEnvironment
import com.sun.mirror.declaration.ClassDeclaration
import com.sun.mirror.declaration.MethodDeclaration
import com.sun.mirror.util.SimpleDeclarationVisitor
import scala.collection.JavaConversions._
import org.kevoree.framework.annotation.processor.visitor.sub.DeployUnitProcessor
import org.kevoree.framework.annotation.processor.visitor.sub.DictionaryProcessor
import org.kevoree.framework.annotation.processor.visitor.sub.LibraryProcessor
import org.kevoree.framework.annotation.processor.visitor.sub.LifeCycleMethodProcessor
import org.kevoree.framework.annotation.processor.visitor.sub.ThirdPartyProcessor
import org.kevoree.{GroupType}

case class GroupTypeVisitor(groupType: GroupType, env: AnnotationProcessorEnvironment)
  extends SimpleDeclarationVisitor
  with DeployUnitProcessor
  with DictionaryProcessor
  with LibraryProcessor
  with ThirdPartyProcessor
  with LifeCycleMethodProcessor {

  override def visitClassDeclaration(classdef: ClassDeclaration) = {

    if (classdef.getSuperclass != null) {
      val annotFragment = classdef.getSuperclass.getDeclaration.getAnnotation(classOf[org.kevoree.annotation.GroupType])
      if (annotFragment != null) {
        classdef.getSuperclass.getDeclaration.accept(this)
      }
    }

    //SUB PROCESSOR
    processDictionary(groupType, classdef)
    processDeployUnit(groupType, classdef, env)
    processLibrary(groupType, classdef)
    processThirdParty(groupType, classdef, env)
    classdef.getMethods.foreach {
      method => method.accept(this)
    }
  }

  override def visitMethodDeclaration(methoddef: MethodDeclaration) = {
    processLifeCycleMethod(groupType, methoddef)
  }

}
