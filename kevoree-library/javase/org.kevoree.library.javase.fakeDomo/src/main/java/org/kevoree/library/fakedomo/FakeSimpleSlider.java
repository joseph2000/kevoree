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
package org.kevoree.library.fakedomo;

import org.kevoree.annotation.*;
import org.kevoree.framework.MessagePort;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * @author ffouquet
 */

@Requires({
		@RequiredPort(name = "send", type = PortType.MESSAGE, needCheckDependency = true)
})
@ComponentType
public class FakeSimpleSlider extends AbstractFakeStuffComponent {
	private MyFrame frame;

	@Override
	public void start () {
		frame = new MyFrame();
		frame.setVisible(true);
	}

	@Override
	public void stop () {
		frame.dispose();
		frame = null;
	}

	@Update
	public void update () {
		stop();
		start();
	}

	private class MyFrame extends JFrame implements MouseListener {

		private JSlider slider;

		public MyFrame () {
			slider = new JSlider();

			this.add(slider);
			slider.addMouseListener(this);

			if (isPortBinded("send")) {
				getPortByName("send", MessagePort.class).process(50);
			}

			pack();
		}

		@Override
		public void mouseClicked (MouseEvent e) {
		}

		@Override
		public void mousePressed (MouseEvent e) {
		}

		@Override
		public void mouseReleased (MouseEvent e) {
			if ((e.getSource()).equals(slider)) {
				if (isPortBinded("send")) {
					getPortByName("send", MessagePort.class).process(slider.getValue());
				}
			}
		}

		@Override
		public void mouseEntered (MouseEvent e) {
		}

		@Override
		public void mouseExited (MouseEvent e) {
		}
	}
}
