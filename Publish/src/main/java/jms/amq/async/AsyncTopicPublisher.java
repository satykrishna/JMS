package jms.amq.async;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQTopicPublisher;
import org.apache.activemq.ActiveMQTopicSession;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTopic;

import jms.amq.chat.MyMessage;

public class AsyncTopicPublisher {

  private ActiveMQConnectionFactory activeMQConnectionFactory;
  private ActiveMQConnection activeMQConnection;
  private ActiveMQTopicSession activeMQTopicSession;
  private ActiveMQTopic activeMQTopic;
  private ActiveMQTopicPublisher activeMQTopicPublisher;
  private ActiveMQObjectMessage activeMQTopicMessage;
  
  {
    try {
      initialize();
    }
    catch (JMSException e) {
      e.printStackTrace();
    }
  }
  
  private void initialize() throws JMSException {
    activeMQConnectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    activeMQConnectionFactory.setTrustAllPackages(true);
    activeMQConnection = (ActiveMQConnection) activeMQConnectionFactory.createConnection();
    activeMQConnection.start();
    activeMQTopicSession = (ActiveMQTopicSession) activeMQConnection.createTopicSession(false,  ActiveMQTopicSession.AUTO_ACKNOWLEDGE);
  }
  
  public void initalizeTopic(String topicName) throws JMSException {
    activeMQTopic = (ActiveMQTopic) activeMQTopicSession.createTopic(topicName);
    activeMQTopicPublisher = (ActiveMQTopicPublisher) activeMQTopicSession.createPublisher(activeMQTopic);
  }
  
  public void send(MyMessage message) throws JMSException {
    activeMQTopicMessage = (ActiveMQObjectMessage) activeMQTopicSession.createObjectMessage(message);
    activeMQTopicPublisher.publish(activeMQTopicMessage);
  }
  
  public void close() throws JMSException {
    if(activeMQTopicSession != null ) {
      activeMQTopicSession.close();
    }
    
    if(activeMQTopicPublisher != null) {
      activeMQTopicPublisher.close();
    }
    
    if(activeMQConnection != null) {
      activeMQConnection.close();
    }
  }
  
  public AsyncTopicPublisher() {
  }
  
  public static void main(String[] args) throws JMSException {
    
    AsyncTopicPublisher publisher = new AsyncTopicPublisher();
    
    MyMessage[] messages = new MyMessage[100];
    
    for(int i = 0; i < messages.length; ++i) {
      messages[i] = new MyMessage(i, "Asynchronous Text Message " + i);
    }
    
    publisher.initalizeTopic("Asynchronous Topic");
    
    for(int i =0; i < messages.length; ++i) {
      publisher.send(messages[i]);
    }
    
    publisher.close();
    
  }

}
