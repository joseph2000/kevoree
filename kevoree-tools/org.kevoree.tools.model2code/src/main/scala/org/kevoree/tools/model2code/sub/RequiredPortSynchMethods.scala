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
package org.kevoree.tools.model2code.sub

import scala.collection.JavaConversions._
import japa.parser.ast.body.TypeDeclaration
import org.kevoree.tools.model2code.sub.AnnotationsSynchMethods._
import java.util.ArrayList
import org.kevoree.tools.model2code.sub.AnnotationsSynchMethods._
import org.kevoree.tools.model2code.sub.ImportSynchMethods._
import japa.parser.ast.expr._
import japa.parser.ast.CompilationUnit
import org.kevoree.annotation.{ComponentType, PortType, RequiredPort, Requires}
import org.kevoree.tools.model2code.sub.ImportSynchMethods._

/**
 * Created by IntelliJ IDEA.
 * User: Gregory NAIN
 * Date: 26/07/11
 * Time: 10:49
 */

trait RequiredPortSynchMethods extends ImportSynchMethods {

  def compilationUnit : CompilationUnit
  def componentType : ComponentType

  def checkOrAddRequiresAnnotation(td : TypeDeclaration) {
    val annotation : SingleMemberAnnotationExpr = td.getAnnotations.find({annot =>
        annot.getName.toString.equals(classOf[Requires].getSimpleName)}) match {
      case Some(annot : SingleMemberAnnotationExpr) => annot
      case None =>  {
          val annot = createRequiresAnnotation
          td.getAnnotations.add(annot)
          annot
        }
    }

    val requiredPortAnnotationsList : java.util.List[AnnotationExpr] = if(annotation.getMemberValue == null) {
      new ArrayList[AnnotationExpr]
    } else {
      annotation.getMemberValue match {
        case arrayInitExpr : ArrayInitializerExpr => arrayInitExpr.getValues.asInstanceOf[java.util.List[AnnotationExpr]]
        case _ => new ArrayList[AnnotationExpr]
      }
    }

    componentType.getRequired.foreach { requiredPort =>
      printf("Dealing with " + requiredPort.getName + " RequiredPort")
      checkOrAddRequiredPortAnnotation(requiredPortAnnotationsList, requiredPort, td)
    }

    annotation.setMemberValue(new ArrayInitializerExpr(requiredPortAnnotationsList.toList))

  }

  def checkOrAddRequiredPortAnnotation(annotList : java.util.List[AnnotationExpr], requiredPort : PortTypeRef, td : TypeDeclaration) {

    val annotation : NormalAnnotationExpr = annotList.filter({annot =>
        annot.getName.toString.equals(classOf[RequiredPort].getSimpleName)}).find({annot =>
        annot.asInstanceOf[NormalAnnotationExpr].getPairs.find({pair =>
            pair.getName.equals("name")}).head.getValue.asInstanceOf[StringLiteralExpr].getValue.equals(requiredPort.getName)}) match {
      case Some(annot : NormalAnnotationExpr) => annot
      case None =>  {
          val annot = createRequiredPortAnnotation
          annotList.add(annot)
          annot
        }
    }

    val pairs = new ArrayList[MemberValuePair]

    val portName = new MemberValuePair("name", new StringLiteralExpr(requiredPort.getName))
    pairs.add(portName)

    checkOrAddImport(compilationUnit, classOf[PortType].getName)
    requiredPort.getRef match {
      case portTypeRef:MessagePortType => {
          val portType = new MemberValuePair("type", new FieldAccessExpr(new NameExpr("PortType"),"MESSAGE"))
          pairs.add(portType)

        }
      case portTypeRef:ServicePortType => {
          val portType = new MemberValuePair("type", new FieldAccessExpr(new NameExpr("PortType"),"SERVICE"))
          pairs.add(portType)
          val serviceClass = new MemberValuePair("className",
                                                 new FieldAccessExpr(
              new NameExpr(portTypeRef.getName.substring(portTypeRef.getName.lastIndexOf(".")+1)),
              "class") )
          pairs.add(serviceClass)
          checkOrAddImport(compilationUnit, portTypeRef.getName)
        }
      case _ =>
    }

    val optional = new MemberValuePair("optional", new BooleanLiteralExpr(requiredPort.getOptional.booleanValue))
    pairs.add(optional)

    annotation.setPairs(pairs)

  }

  def createRequiresAnnotation : SingleMemberAnnotationExpr = {
    val newAnnot = new SingleMemberAnnotationExpr(new NameExpr(classOf[Requires].getSimpleName), null)
    checkOrAddImport(compilationUnit, classOf[Requires].getName)
    newAnnot
  }
  
  def createRequiredPortAnnotation : NormalAnnotationExpr = {
    val newAnnot = new NormalAnnotationExpr(new NameExpr(classOf[RequiredPort].getSimpleName), null)
    checkOrAddImport(compilationUnit, classOf[RequiredPort].getName)
    newAnnot
  }

  
}