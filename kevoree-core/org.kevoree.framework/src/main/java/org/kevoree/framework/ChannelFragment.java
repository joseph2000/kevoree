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
package org.kevoree.framework;

import java.util.HashMap;

import org.kevoree.api.service.core.handler.KevoreeModelHandlerService;
import org.kevoree.framework.message.Message;

/**
 *
 * @author ffouquet
 */
public interface ChannelFragment {

    public KevoreeModelHandlerService getModelService();

    public Object dispatch(Message msg);

    public java.util.List<KevoreePort> getBindedPorts();

    public java.util.List<KevoreeChannelFragment> getOtherFragments();

    public Object forward(KevoreeActor delegate, Message msg);

    public HashMap<String, Object> getDictionary();

    public ChannelFragmentSender createSender(String remoteNodeName,String remoteChannelName);

    public String getNodeName();

    public String getName();

    public Object remoteDispatch(Message msg);

}
