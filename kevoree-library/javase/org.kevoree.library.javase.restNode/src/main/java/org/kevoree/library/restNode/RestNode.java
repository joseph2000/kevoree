/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.kevoree.library.restNode;

import org.kevoree.ContainerRoot;
import org.kevoree.adaptation.deploy.osgi.BaseDeployOSGi;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.handler.KevoreeModelHandlerService;
import org.kevoree.framework.*;
import org.kevoree.framework.Constants;
import org.kevoree.kompare.KevoreeKompareBean;
import org.kevoree.library.defaultNodeTypes.JavaSENode;
import org.kevoree.library.restNode.JmDnsComponent;
import org.kevoree.remote.rest.Handler;
import org.kevoree.remote.rest.KevoreeRemoteBean;
import org.kevoreeAdaptation.AdaptationModel;
import org.kevoreeAdaptation.AdaptationPrimitive;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * @author ffouquet
 */
@NodeType
@DictionaryType({
        @DictionaryAttribute(name = "port", defaultValue = "8000", optional = true),
        @DictionaryAttribute(name = "autodiscovery", defaultValue = "false", optional = true, vals = {"false", "true"})
})
public class RestNode extends JavaSENode {
    private static final Logger logger = LoggerFactory.getLogger(RestNode.class);

    private JmDnsComponent jmDnsComponent = null;
    private KevoreeRemoteBean remoteBean = null;

    @Start
    @Override
    public void startNode() {
        super.startNode();
        Handler.setModelhandler(this.getModelService());
        remoteBean = new KevoreeRemoteBean(this.getDictionary().get("port").toString());
        remoteBean.start();

        if (this.getDictionary().get("autodiscovery").equals("true")) {
            jmDnsComponent = new JmDnsComponent(this.getNodeName(), remoteBean.getPort(), this.getModelService(), this.getClass().getSimpleName());
        }
    }

    @Stop
    @Override
    public void stopNode() {
        if (jmDnsComponent != null) {
            jmDnsComponent.close();
        }
        remoteBean.stop();
        remoteBean = null;
        super.stopNode();
    }

    @Override
    public void push(String targetNodeName, ContainerRoot root) {

        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            KevoreeXmiHelper.saveStream(outStream, root);
            outStream.flush();

            String IP = KevoreePlatformHelper
                    .getProperty(root, targetNodeName, Constants.KEVOREE_PLATFORM_REMOTE_NODE_IP());
            if (IP.equals("")) {
                IP = "127.0.0.1";
            }
            String PORT = this.getDictionary().get("port").toString();
            if (PORT.equals("")) {
                PORT = "8000";
            }
            URL url = new URL("http://" + IP + ":" + PORT + "/model/current");

            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(outStream.toString());
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = rd.readLine();
            while (line != null) {
//				System.out.println(line);
                line = rd.readLine();
            }
            wr.close();
            rd.close();

        } catch (Exception e) {
//			e.printStackTrace();
            logger.error("Unable to push a model on " + targetNodeName, e);

        }

    }

}
