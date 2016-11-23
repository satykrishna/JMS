package jms.amq.consume;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQTopicSession;
import org.apache.activemq.ActiveMQTopicSubscriber;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTopic;

import jms.amq.chat.MyMessage;

public class MyTopicReceiver {

  private ActiveMQConnectionFactory factory;
  private ActiveMQConnection connection;
  private ActiveMQTopicSession session;
  private ActiveMQTopic topic;
  private ActiveMQTopicSubscriber subscriber;
  
  public MyTopicReceiver() {
    
  }
  
  public void receive() throws JMSException {
    factory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
    connection = (ActiveMQConnection) factory.createConnection();
    factory.setTrustAllPackages(true);
    connection.start();
    session = (ActiveMQTopicSession) connection.createTopicSession(false, ActiveMQTopicSession.AUTO_ACKNOWLEDGE);
    topic = (ActiveMQTopic) session.createTopic("AMQ");
    subscriber = (ActiveMQTopicSubscriber) session.createSubscriber(topic);
    ActiveMQObjectMessage message = (ActiveMQObjectMessage) subscriber.receive();
    if(message instanceof ActiveMQObjectMessage) {
      System.out.println((MyMessage)message.getObject());
    }
    session.close();
    connection.close();
  }
  
  public static void main(String []args) throws JMSException {
    
    MyTopicReceiver[] receivers = new MyTopicReceiver[3];
    
    Thread[] threads = new Thread[3];
    
    for(Thread thread : threads) {
      thread = new Thread(new Runnable() {
        MyTopicReceiver receiver = new MyTopicReceiver();
      
        @Override
        public void run() {
          try {
            receiver.receive();
          }
          catch (JMSException e) {
            e.printStackTrace();
          }
        }
      });
      thread.start();
    }
    
  }
}
