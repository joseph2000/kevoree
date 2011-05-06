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
package org.kevoree.tools.marShell.parser.sub

import org.kevoree.tools.marShell.ast.{NetworkPropertyStatement, RemoveLibraryStatment, AddLibraryStatment, Statment}

trait KevsNetworkPropertyParser extends KevsAbstractParser with KevsPropertiesParser {

  val networkPropertyFormat = "network <NodeInstanceName> {key=\"val\",\"key2\"=\"val2\"}"
  def parseNetworkProperty : Parser[List[Statment]] = "network" ~ orFailure(ident,networkPropertyFormat) ~ parseProperties ^^{ case _ ~ nodeName ~ props =>
      List(NetworkPropertyStatement(nodeName,props))
  }


}