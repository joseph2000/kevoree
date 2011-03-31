package org.kevoree.library.gossiperNetty

import org.jboss.netty.channel.ChannelFutureListener
import org.jboss.netty.channel.ChannelHandlerContext
import org.jboss.netty.channel.ExceptionEvent
import org.jboss.netty.channel.MessageEvent
import org.jboss.netty.channel.SimpleChannelUpstreamHandler
import org.kevoree.library.gossip.Gossip.VersionedModel
import org.kevoree.library.gossiperNetty.api.msg.KevoreeMessage.Message
import org.slf4j.LoggerFactory

class DataReceiverHandler(gossiperRequestSender : GossiperRequestSender) extends SimpleChannelUpstreamHandler{
  
	private var logger = LoggerFactory.getLogger(classOf[DataReceiverHandler])
  
  override def messageReceived(ctx:ChannelHandlerContext, e:MessageEvent)={
	//println("response received")
	var message = e.getMessage.asInstanceOf[Message]
	if (message.getContentClass.equals(classOf[VersionedModel].getName)) {
	  //var versionModel = RichString(message.getContent.toStringUtf8).fromJSON(classOf[VersionedModel])
	  gossiperRequestSender.endGossipAction(message)
	  //e.getChannel.getCloseFuture.awaitUninterruptibly
	  e.getChannel.getCloseFuture.addListener(ChannelFutureListener.CLOSE);
	}
  }
  
  override def exceptionCaught(ctx:ChannelHandlerContext, e:ExceptionEvent)={
    //NOOP
	//println("Exception GossiperRequestReceiverHandler")
	//e.getCause().printStackTrace();
	//logger.debug("Exception in " + classOf[DataReceiverHandler].getName)
	logger.debug(e.getCause.getStackTraceString)
	e.getChannel.close.awaitUninterruptibly
  }
}