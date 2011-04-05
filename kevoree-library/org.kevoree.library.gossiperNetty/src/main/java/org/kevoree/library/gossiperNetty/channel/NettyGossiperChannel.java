package org.kevoree.library.gossiperNetty.channel;

import org.kevoree.library.gossiperNetty.NettyGossipAbstractElement;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.kevoree.annotation.ChannelTypeFragment;
import org.kevoree.annotation.DictionaryAttribute;
import org.kevoree.annotation.DictionaryType;
import org.kevoree.annotation.Library;
import org.kevoree.annotation.Start;
import org.kevoree.annotation.Stop;
import org.kevoree.annotation.Update;
import org.kevoree.api.service.core.handler.KevoreeModelHandlerService;
import org.kevoree.framework.AbstractChannelFragment;
import org.kevoree.framework.ChannelFragmentSender;
import org.kevoree.framework.KevoreeChannelFragment;
import org.kevoree.framework.KevoreePlatformHelper;
import org.kevoree.framework.NoopChannelFragmentSender;
import org.kevoree.framework.message.Message;
import org.kevoree.library.gossiperNetty.DataManager;
import org.kevoree.library.gossiperNetty.DataManagerForChannel;
import org.kevoree.library.gossiperNetty.GossiperActor;
import org.kevoree.library.gossiperNetty.Serializer;
import org.kevoree.library.version.Version.ClockEntry;
import org.kevoree.library.version.Version.VectorClock;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

/**
 *
 * @author Erwan Daubert
 * TODO add a DictionaryAttribute to define the number of uuids sent by response when a VectorClockUUIDsRequest is sent
 */
@Library(name = "Kevoree-Netty")
@DictionaryType({
	@DictionaryAttribute(name = "interval", defaultValue = "30000", optional = true),
	@DictionaryAttribute(name = "port", defaultValue = "9000", optional = true),
	@DictionaryAttribute(name = "FullUDP", defaultValue = "true", optional = true)
})
@ChannelTypeFragment
public class NettyGossiperChannel extends AbstractChannelFragment implements NettyGossipAbstractElement {

	private DataManager dataManager = null;//new DataManager();
	private Serializer serializer = null;
	private GossiperActor actor = null;
	private ServiceReference sr;
	private KevoreeModelHandlerService modelHandlerService = null;
	private Logger logger = LoggerFactory.getLogger(NettyGossiperChannel.class);

	@Start
	public void startGossiperChannel() {
		Bundle bundle = (Bundle) this.getDictionary().get("osgi.bundle");
		sr = bundle.getBundleContext().getServiceReference(KevoreeModelHandlerService.class.getName());
		modelHandlerService = (KevoreeModelHandlerService) bundle.getBundleContext().getService(sr);

		dataManager = new DataManagerForChannel();
		serializer = new ChannelSerializer();

		actor = new GossiperActor(Long.parseLong((String)this.getDictionary().get("interval")),
				this,
				dataManager,
				parsePortNumber(getNodeName()),
				parseFullUDPParameter(),
				true, serializer);
	}

	@Stop
	public void stopGossiperChannel() {
		if (actor != null) {
			actor.stop();
			actor = null;
		}
		if (dataManager != null) {
			dataManager.stop();
		}
		if (modelHandlerService != null) {
			Bundle bundle = (Bundle) this.getDictionary().get("osgi.bundle");
			bundle.getBundleContext().ungetService(sr);
			modelHandlerService = null;
		}
	}

	@Update
	public void updateGossiperChannel() {
		// TODO how to update Gossiper Channel
	}

	@Override
	public Object dispatch(Message msg) {
		//Local delivery
		localNotification(msg);

		//CREATE NEW MESSAGE
		long timestamp = System.currentTimeMillis();
		UUID uuid = UUID.randomUUID();
		Tuple2<VectorClock, Object> tuple = new Tuple2<VectorClock, Object>(VectorClock.newBuilder().
				addEnties(ClockEntry.newBuilder().setNodeID(this.getNodeName()).setTimestamp(timestamp).setVersion(2l).build()).setTimestamp(timestamp).build(), msg);
		dataManager.setData(uuid, tuple);

		actor.notifyPeers();
		//SYNCHRONOUS NON IMPLEMENTED
		return null;
	}

	@Override
	public ChannelFragmentSender createSender(String remoteNodeName, String remoteChannelName) {
		return new NoopChannelFragmentSender();
	}

	@Override
	public void localNotification(Object o) {
		if (o instanceof Message) {
			for (org.kevoree.framework.KevoreePort p : getBindedPorts()) {
				forward(p, (Message) o);
			}
		}
	}

	@Override
	public List<String> getAllPeers() {
		List<String> peers = new ArrayList<String>();
		for (KevoreeChannelFragment fragment : getOtherFragments()) {
			peers.add(fragment.getNodeName());
		}
		return peers;
	}

	@Override
	public String getAddress(String remoteNodeName) {
		String ip = KevoreePlatformHelper.getProperty(modelHandlerService.getLastModel(), remoteNodeName, org.kevoree.framework.Constants.KEVOREE_PLATFORM_REMOTE_NODE_IP());
		if (ip == null || ip.equals("")) {
			ip = "127.0.0.1";
		}
		return ip;
	}
	private String name = "[A-Za-z0-9_]*";
	private String portNumber = "(65535|5[0-9]{4}|4[0-9]{4}|3[0-9]{4}|2[0-9]{4}|1[0-9]{4}|[0-9]{0,4})";
	private String separator = ",";
	private String affectation = "=";
	private String portPropertyRegex = "((" + name + affectation + portNumber + ")" + separator + ")*(" + name + affectation + portNumber + ")";

	@Override
	public int parsePortNumber(String nodeName) {
		String portProperty = this.getDictionary().get("port").toString();
		if (portProperty.matches(portPropertyRegex)) {
			String[] definitionParts = portProperty.split(separator);
			for (String part : definitionParts) {
				if (part.contains(nodeName + affectation)) {
					//System.out.println(Integer.parseInt(part.substring((nodeName + affectation).length(), part.length())));
					return Integer.parseInt(part.substring((nodeName + affectation).length(), part.length()));
				}
			}
		} else {
			return Integer.parseInt(portProperty);
		}
		return 0;
	}

	@Override
	public Boolean parseFullUDPParameter() {
		if (this.getDictionary().get("FullUDP").toString().equals("true")) {
			return true;
		}
		return false;

	}

	@Override
	public String selectPeer() {
		int othersSize = this.getOtherFragments().size();
		if (othersSize > 0) {
			SecureRandom diceRoller = new SecureRandom();
			int peerIndex = diceRoller.nextInt(othersSize);
			return this.getOtherFragments().get(peerIndex).getNodeName();
		} else {
			return "";
		}
	}
}