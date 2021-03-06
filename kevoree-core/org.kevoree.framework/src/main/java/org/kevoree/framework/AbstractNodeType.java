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
package org.kevoree.framework;

import org.kevoree.ContainerRoot;
import org.kevoree.api.service.core.handler.KevoreeModelHandlerService;
import org.kevoreeAdaptation.AdaptationModel;
import org.kevoreeAdaptation.AdaptationPrimitive;

import java.util.HashMap;

public abstract class AbstractNodeType {

    public abstract void push(String targetNodeName, ContainerRoot root);

    private HashMap<String, Object> dictionary = new HashMap<String, Object>();

    public HashMap<String, Object> getDictionary() {
        return this.dictionary;
    }

    public void setDictionary(HashMap<String, Object> dic){
         dictionary = dic;
    }

    public void startNode(){}

    public void stopNode(){}

    public void updateNode(){}

    private KevoreeModelHandlerService modelService;
    public void setModelService(KevoreeModelHandlerService ms){
        modelService = ms;
    }
    public KevoreeModelHandlerService getModelService() {
        return modelService;
    }

    private String nodeName = "";
    public String getNodeName() {
        return nodeName;
    }
    public void setNodeName(String pnodeName) {
        nodeName = pnodeName;
    }


    public abstract AdaptationModel kompare(ContainerRoot actualModel,ContainerRoot targetModel);

    public abstract PrimitiveCommand getPrimitive(AdaptationPrimitive primitive);


}




