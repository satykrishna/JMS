package jms.amq.aysnc;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQTopicSession;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTopic;

import jms.amq.chat.MyMessage;

public class AsynchronizedConsumer implements MessageListener {

  private ActiveMQConnectionFactory activeMQConnectionFactory;
  private ActiveMQConnection activeMQConnection;
  private ActiveMQTopicSession activeMQTopicSession;
  private ActiveMQTopic activeMQTopic;
  private ActiveMQMessageConsumer activeMQConsumer;
  private ActiveMQObjectMessage activeMQObjectMessage;
  
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
    activeMQTopicSession = (ActiveMQTopicSession) activeMQConnection.createTopicSession(false, ActiveMQTopicSession.AUTO_ACKNOWLEDGE);
  }
  
  public void setTopicToListen(String topicName) throws JMSException {
    activeMQTopic = (ActiveMQTopic) activeMQTopicSession.createTopic(topicName);
    activeMQConsumer = (ActiveMQMessageConsumer) activeMQTopicSession.createConsumer(activeMQTopic);
    activeMQConsumer.setMessageListener(this);
    activeMQConnection.start();
    
    new Thread(new Runnable() {
      
      @Override
      public void run() {
        System.out.println("Will Wait until a message is arrived");
        while(true) {
          
        }
      }
    }).start();
        
  }
  
  @Override
  public void onMessage(Message message) {
    
    activeMQObjectMessage = (ActiveMQObjectMessage) message;
    
    try {
      MyMessage myMessage = (MyMessage)activeMQObjectMessage.getObject();
      System.out.println(myMessage);
      close();
    }   
    catch (JMSException e) {
      e.printStackTrace();
    }
    System.out.println("Going to exit");
    System.exit(-1);
  }
  
  public void close() throws JMSException {
    if(activeMQTopicSession != null ) {
      activeMQTopicSession.close();
    }
    
    if(activeMQConsumer != null) {
      activeMQConsumer.close();
    }
    
    if(activeMQConnection != null) {
      activeMQConnection.close();
    }
  }
  
  public static void main(String[] args) throws JMSException {
    AsynchronizedConsumer consumer  = new AsynchronizedConsumer();
    consumer.setTopicToListen("Asynchronous Topic ");
  }
  
}