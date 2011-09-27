package org.kevoree.sky.jclouds.ec2;

import org.jclouds.compute.ComputeServiceContextFactory;
import org.jclouds.ec2.EC2AsyncClient;
import org.jclouds.ec2.EC2Client;
import org.jclouds.rest.RestContext;
import org.kevoree.ContainerNode;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.DictionaryAttribute;
import org.kevoree.annotation.DictionaryType;
import org.kevoree.annotation.NodeType;
import org.kevoree.annotation.PrimitiveCommands;
import org.kevoree.framework.PrimitiveCommand;
import org.kevoree.library.sky.virtualCloud.VirtualCloudNode;
import org.kevoree.library.sky.virtualCloud.command.AddNodeCommand;
import org.kevoree.library.sky.virtualCloud.command.RemoveNodeCommand;
import org.kevoreeAdaptation.AdaptationPrimitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * User: Erwan Daubert - erwan.daubert@gmail.com
 * Date: 22/09/11
 * Time: 15:04
 *
 * @author Erwan Daubert
 * @version 1.0
 */

@NodeType
@DictionaryType({
		@DictionaryAttribute(name = "port", defaultValue = "7000", optional = false),
		@DictionaryAttribute(name = "accesskeyid", optional = false),
		@DictionaryAttribute(name = "secretkey", optional = false),
		@DictionaryAttribute(name = "statements", optional = true),
		@DictionaryAttribute(name = "endPoint", optional = true),
		@DictionaryAttribute(name = "provider", defaultValue = JCloudsNode.aws_ec2,
				vals = {JCloudsNode.aws_ec2, JCloudsNode.bluelock_vcloud_vcenterprise,
						JCloudsNode.bluelock_vcloud_zone01, JCloudsNode.cloudservers_uk,
						JCloudsNode.cloudservers_us, JCloudsNode.cloudsigma_zrh, JCloudsNode.elastichosts_lon_b,
						JCloudsNode.elastichosts_lon_p, JCloudsNode.elastichosts_sat_p,
						JCloudsNode.eucalyptus_partnercloud_ec2, JCloudsNode.gogrid,
						JCloudsNode.greenhousedata_element_vcloud, JCloudsNode.openhosting_east1,
						JCloudsNode.rimuhosting, JCloudsNode.savvis_symphonyvpdc, JCloudsNode.serverlove_z1_man,
						JCloudsNode.skalicloud_sdg_my, JCloudsNode.slicehost, JCloudsNode.stratogen_vcloud_mycloud,
						JCloudsNode.trmk_ecloud, JCloudsNode.trmk_vcloudexpress},
				optional = false)
})
@PrimitiveCommands(value = {}, values = {VirtualCloudNode.REMOVE_NODE, VirtualCloudNode.ADD_NODE/*, "UpdateNode"*/})
public class JCloudsNode extends VirtualCloudNode {
	private static final Logger logger = LoggerFactory.getLogger(JCloudsNode.class);

	static final String aws_ec2 = "aws-ec2";
	static final String bluelock_vcloud_vcenterprise = "bluelock-vcloud-vcenterprise";
	static final String bluelock_vcloud_zone01 = "bluelock-vcloud-zone01";
	static final String cloudservers_uk = "cloudservers-uk";
	static final String cloudservers_us = "cloudservers-us";
	static final String cloudsigma_zrh = "cloudsigma-zrh";
	static final String elastichosts_lon_b = "elastichosts-lon-b";
	static final String elastichosts_lon_p = "elastichosts-lon-p";
	static final String elastichosts_sat_p = "elastichosts-sat-p";
	static final String eucalyptus_partnercloud_ec2 = "eucalyptus-partnercloud-ec2";
	static final String gogrid = "gogrid";
	static final String greenhousedata_element_vcloud = "greenhousedata-element-vcloud";
	static final String openhosting_east1 = "openhosting-east1";
	static final String rimuhosting = "rimuhosting";
	static final String savvis_symphonyvpdc = "savvis-symphonyvpdc";
	static final String serverlove_z1_man = "serverlove-z1-man";
	static final String skalicloud_sdg_my = "skalicloud-sdg-my";
	static final String slicehost = "slicehost";
	static final String stratogen_vcloud_mycloud = "stratogen-vcloud-mycloud";
	static final String trmk_ecloud = "trmk-ecloud";
	static final String trmk_vcloudexpress = "trmk-vcloudexpress";


	private static final String KEVOREE_JCLOUDS_SERVICE = "kevoree.jclouds.service";

	@Override
	public void startNode () {
		super.startNode();
	}

	@Override
	public void stopNode () {
		super.stopNode();
	}

	@Override
	public PrimitiveCommand getPrimitive (AdaptationPrimitive adaptationPrimitive) {
		logger.debug("ask for primitiveCommand corresponding to " + adaptationPrimitive.getPrimitiveType().getName());
		PrimitiveCommand command = null;
		if (adaptationPrimitive.getPrimitiveType().getName().equals(VirtualCloudNode.REMOVE_NODE)) {
			command = new RemoveNodeCommand((ContainerNode) adaptationPrimitive.getRef(),
					(ContainerRoot) (adaptationPrimitive.getRef().eContainer()), kevoreeNodeManager);
		} else if (adaptationPrimitive.getPrimitiveType().getName().equals(VirtualCloudNode.ADD_NODE)) {
			command = new AddNodeCommand((ContainerNode) adaptationPrimitive.getRef(),
					(ContainerRoot) (adaptationPrimitive.getRef().eContainer()), kevoreeNodeManager);
		} else if (adaptationPrimitive.getPrimitiveType().getName().equals("UpdateNode")) {
			// TODO ?
		}
		return command;
	}


	@Override
	public void push (String physicalNodeName, ContainerRoot root) {
		// Init
		Properties properties = initProperties();

		/*RestContext<EC2Client, EC2AsyncClient> context = new ComputeServiceContextFactory().createContext(
				(String) this.getDictionary().get("provider"), (String) this.getDictionary().get("accesskeyid"),
				(String) this.getDictionary().get("secretkey")).getProviderSpecificContext();*/

		if (properties != null) {

			RestContext<EC2Client, EC2AsyncClient> context = new ComputeServiceContextFactory()
					.createContext((String) this.getDictionary().get("provider"), properties)
					.getProviderSpecificContext();
			// Get a synchronous client
			EC2Client client = context.getApi();

			// try to know if the virtual machine is already deployed
			

			// if not, we create it

			// we push the model

/*
			try {
				if (command.equals("create")) {

					KeyPair pair = createKeyPair(client, name);

					RunningInstance instance = createSecurityGroupKeyPairAndInstance(client, name);

					System.out.printf("instance %s ready%n", instance.getId());
					System.out.printf("ip address: %s%n", instance.getIpAddress());
					System.out.printf("dns name: %s%n", instance.getDnsName());
					System.out.printf("login identity:%n%s%n", pair.getKeyMaterial());

				} else if (command.equals("destroy")) {
					destroySecurityGroupKeyPairAndInstance(client, name);
				} else {
					throw new IllegalArgumentException(INVALID_SYNTAX);
				}
			} finally {
				// Close connecton
				context.close();
			}*/
		}
	}

	private Properties initProperties () {
		Properties properties = new Properties();

		if (this.getDictionary().get("accesskeyid") == null ||  this.getDictionary().get("secretkey") == null) {
			logger.error("missing value on parameter \"accesskeyid\" or \"secretkey\"");
			return null;
		} else {
			properties.put("accesskeyid", this.getDictionary().get("accesskeyid"));
			properties.put("secretkey", this.getDictionary().get("secretkey"));
		}

		String endPoint = (String) this.getDictionary().get("endPoint");
		if (endPoint != null && !endPoint.equals("")) {
			properties.put("endpoint", endPoint);
		}
		return properties;
	}
}
