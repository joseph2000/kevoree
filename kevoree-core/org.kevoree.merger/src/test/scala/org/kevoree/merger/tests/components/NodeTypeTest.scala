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

package org.kevoree.merger.tests.components

import org.junit._
import org.scalatest.junit.AssertionsForJUnit
import org.kevoree.merger.KevoreeMergerComponent
import org.kevoree.merger.tests.MergerTestSuiteHelper
import org.kevoree.TypeDefinition
import org.kevoree.api.service.core.merger.MergerService

class NodeTypeTest extends MergerTestSuiteHelper {

  var component: MergerService = null

  @Before def initialize() {
    component = new KevoreeMergerComponent

  }

  @Test def verifyNodeTypeMerge() {
    var mergedModel = component.merge(model("nodeType/defchannels1.1.0.kev"), model("nodeType/fakestuff3.0.0.kev"))
    mergedModel testSave ("nodeType", "merged.kev")

    //test cast error

  }


}