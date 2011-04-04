package org.kevoree.library.gossiperNetty

import org.jboss.netty.channel.ChannelFutureListener
import org.jboss.netty.channel.ChannelHandlerContext
import org.jboss.netty.channel.ExceptionEvent
import org.jboss.netty.channel.MessageEvent
import org.jboss.netty.channel.SimpleChannelUpstreamHandler
import org.kevoree.library.gossip.Gossip.VersionedModel
import org.kevoree.library.gossiperNetty.api.msg.KevoreeMessage.Message
import org.slf4j.LoggerFactory

class DataReceiverHandler(gossiperRequestSender : GossiperRequestSender[_]) extends SimpleChannelUpstreamHandler{
  
	private var logger = LoggerFactory.getLogger(classOf[DataReceiverHandler])
  
  override def messageReceived(ctx:ChannelHandlerContext, e:MessageEvent) {
	//println("response received")
	val message = e.getMessage.asInstanceOf[Message]
	if (message.getContentClass.equals(classOf[VersionedModel].getName)) {
	  gossiperRequestSender.endGossipAction(message)
	  //e.getChannel.getCloseFuture.awaitUninterruptibly
	  e.getChannel.getCloseFuture.addListener(ChannelFutureListener.CLOSE);
	}
  }
  
  override def exceptionCaught(ctx:ChannelHandlerContext, e:ExceptionEvent) {
    //NOOP
	logger.error(this.getClass + "\n" + e.getCause.getMessage + "\n" + e.getCause.getStackTraceString)
		//e.getCause.printStackTrace
	e.getChannel.close.awaitUninterruptibly
  }
}
