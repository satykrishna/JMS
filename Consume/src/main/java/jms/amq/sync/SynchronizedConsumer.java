package jms.amq.sync;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQQueueSession;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQQueue;

import jms.amq.chat.MyMessage;

public class SynchronizedConsumer {
  
  private ActiveMQConnectionFactory connectionFactory;
  private ActiveMQConnection connection;
  private ActiveMQQueueSession session;
  private ActiveMQObjectMessage activeMQObjectMessage;
  private ActiveMQMessageConsumer activeMQconsumer;
  private ActiveMQQueue activeMQQueue;
    
  {
    try {
      initialize();
    }
    catch (JMSException e) {
      e.printStackTrace();
    }
  }
  
  private void initialize() throws JMSException {
    connectionFactory  = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    connectionFactory.setTrustAllPackages(true);
    connection = (ActiveMQConnection) connectionFactory.createConnection();
    connection.start();
    session = (ActiveMQQueueSession) connection.createQueueSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);
  }
  

  public void initializeQueue(String queueName) throws JMSException {
    activeMQQueue = (ActiveMQQueue) session.createQueue(queueName);
    activeMQconsumer = (ActiveMQMessageConsumer) session.createConsumer(activeMQQueue);
  }
  
  public MyMessage receive() throws JMSException {
    activeMQObjectMessage = (ActiveMQObjectMessage) activeMQconsumer.receive();
    return (MyMessage)activeMQObjectMessage.getObject();
  }
  
  public void close() throws JMSException {
    if(activeMQconsumer != null) {
      activeMQconsumer.close();
    }

    if(session != null) {
      session.close();
    }
    
    if(connection != null) {
      connection.close();
    }
  }
  
  public SynchronizedConsumer() {
  }

  public static void main(String[] args) throws JMSException {
    SynchronizedConsumer consumer = new SynchronizedConsumer();
    consumer.initializeQueue("SynchronizedMessageSystem");
    MyMessage[] messages = new MyMessage[100];
    for(int i = 0; i < 100; i++) {
      messages[i] = consumer.receive();
      System.out.println(messages[i]);
    }
    consumer.close();
  }
}
