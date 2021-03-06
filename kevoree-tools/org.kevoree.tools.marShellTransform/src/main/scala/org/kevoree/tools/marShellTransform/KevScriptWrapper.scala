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

package org.kevoree.tools.marShellTransform

import org.kevoree.tools.marShell.ast._
import scala.collection.JavaConversions._
import org.slf4j.LoggerFactory

object KevScriptWrapper {
	var logger = LoggerFactory.getLogger(this.getClass);

  val paramSep = ":"
  val instrSep = "/"

  def generateDictionaryString(dictionary: java.util.Properties): String = {
    if (dictionary == null) {
      return ""
    }
    val content = new StringBuilder
    var first = true
    dictionary.foreach {
      dic =>
        if (first) {
          content append dic._1 + "=" + dic._2
          first = false
        } else {
          content append "," + dic._1 + "=" + dic._2
        }
    }
    content.toString()
  }

  def generateKevScriptCompressed(script: Script): String = {
    if (script.blocks.isEmpty) return ""

    val content = new StringBuilder
    content append "{"
    var firstStatment = true
    script.blocks.foreach {
      block =>
        block.l.foreach {
          statement =>
            statement match {
              case s: UpdateDictionaryStatement => {
                if (!firstStatment) {content append instrSep }
                firstStatment = false
                content append "udi" + paramSep + s.instanceName + paramSep + generateDictionaryString(s.dictionary)
              }
              case s: AddComponentInstanceStatment => {
                if (!firstStatment) {content append instrSep }
                firstStatment = false
                content append "ain" + paramSep + s.cid.componentInstanceName + paramSep + s.typeDefinitionName
                if (s.dictionary != null) {
                  content append paramSep + generateDictionaryString(s.dictionary)
                }
              }
              case s: AddChannelInstanceStatment => {
                if (!firstStatment) {content append instrSep }
                firstStatment = false
                content append "ain" + paramSep + s.channelName + paramSep + s.channelType
                if (s.dictionary != null) {
                  content append paramSep + generateDictionaryString(s.dictionary)
                }
              }
              case s : RemoveComponentInstanceStatment => {
                if (!firstStatment) {content append instrSep }
                firstStatment = false
                content append "rin" + paramSep + s.cid.componentInstanceName
              }
              case s : RemoveChannelInstanceStatment => {
                if (!firstStatment) {content append instrSep }
                firstStatment = false
                content append "rin" + paramSep + s.channelName
              }
              case s: AddBindingStatment => {
                if (!firstStatment) {content append instrSep }
                firstStatment = false
                content append "abi" + paramSep + s.cid.componentInstanceName + paramSep + s.bindingInstanceName + paramSep + s.portName
              }
              case s: RemoveBindingStatment => {
                if (!firstStatment) {content append instrSep }
                firstStatment = false
                content append "rbi" + paramSep + s.cid.componentInstanceName + paramSep + s.bindingInstanceName + paramSep + s.portName
              }
              case _@s => logger.warn("Uncatch " + s) //DO NOTHING FOR OTHER STATEMENT
            }


        }
    }
    content append "}"
    content.toString()
  }

  def miniPlanKevScript(s: Script): Script = {
    var resultStatmentList: List[Statment] = List()
    //REMOVE BINDING FIRST
    s.blocks.foreach {
      block =>
        resultStatmentList = resultStatmentList ++ block.l.filter(statement => statement.isInstanceOf[RemoveBindingStatment])
    }
    //REMOVE CHANNEL INST
    s.blocks.foreach {
      block =>
        resultStatmentList = resultStatmentList ++ block.l.filter(statement => statement.isInstanceOf[RemoveChannelInstanceStatment])
    }
    //REMOVE COMPONENT INST 
    s.blocks.foreach {
      block =>
        resultStatmentList = resultStatmentList ++ block.l.filter(statement => statement.isInstanceOf[RemoveComponentInstanceStatment])
    }
    //ADD COMPONENT INST 
    s.blocks.foreach {
      block =>
        resultStatmentList = resultStatmentList ++ block.l.filter(statement => statement.isInstanceOf[AddComponentInstanceStatment])
    }
    //ADD CHANNEL INST 
    s.blocks.foreach {
      block =>
        resultStatmentList = resultStatmentList ++ block.l.filter(statement => statement.isInstanceOf[AddChannelInstanceStatment])
    }
    //ADD BINDING INST 
    s.blocks.foreach {
      block =>
        resultStatmentList = resultStatmentList ++ block.l.filter(statement => statement.isInstanceOf[AddBindingStatment])
    }
    //Update Param INST 
    s.blocks.foreach {
      block =>
        resultStatmentList = resultStatmentList ++ block.l.filter(statement => statement.isInstanceOf[UpdateDictionaryStatement])
    }
    Script(List(TransactionalBloc(resultStatmentList)))
  }


}
