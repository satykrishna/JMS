package jms.amq.expiry;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;

import jms.amq.chat.MyMessage;

public class MessageExpiryListener implements MessageListener {

  private static int count = 1;
  private static final Logger logger = Logger.getLogger(MessageExpiryListener.class);

  public MessageExpiryListener() {
  }

  @Override
  public void onMessage(Message paramMessage) {
    ActiveMQObjectMessage message = (ActiveMQObjectMessage) paramMessage;
    try {
      MyMessage received = (MyMessage) message.getObject();
      logger.info("Recieved Message:  "+ (count++) +  " " + received);
    }
    catch (JMSException e) {
      e.printStackTrace();
    }
  }

}
