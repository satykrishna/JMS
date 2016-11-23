package jms.amq.sync;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.ActiveMQQueueSession;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQQueue;

import jms.amq.chat.MyMessage;

public class SyncPublisher {

  private ActiveMQConnectionFactory connectionFactory;
  private ActiveMQConnection        connection;
  private ActiveMQQueue             queue;
  private ActiveMQQueueSession      session;
  private ActiveMQMessageProducer   publisher;
  private ActiveMQObjectMessage     activeMQObjectMessage;
  
  {
    try {
      initialize();
    }
    catch (JMSException e) {
      e.printStackTrace();
    }
  }

  private void initialize() throws JMSException {
    connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    connectionFactory.setTrustAllPackages(true);
    connection = (ActiveMQConnection) connectionFactory.createConnection();
    connection.start();
    session = (ActiveMQQueueSession) connection.createQueueSession(false, ActiveMQQueueSession.AUTO_ACKNOWLEDGE);
  }

  public SyncPublisher() {

  }

  public void initQueue(String queueName) throws JMSException {
    queue = (ActiveMQQueue) session.createQueue(queueName);
    publisher = (ActiveMQMessageProducer) session.createProducer(queue);
  }
  
  public void send(MyMessage msg) throws JMSException {
    activeMQObjectMessage = (ActiveMQObjectMessage) session.createObjectMessage(msg);
    publisher.send(activeMQObjectMessage);
  }
  
  public void close() throws JMSException {
    if(session != null ) {
      session.close();
    }
    
    if(publisher != null) {
      publisher.close();
    }
    
    if(connection != null) {
      connection.close();
    }
  }

  public static void main(String[] args) throws JMSException {
    SyncPublisher publisher = new SyncPublisher();
    
    MyMessage[] messages = new MyMessage[100];
    
    for(int i = 0; i < messages.length; ++i) {
      messages[i] = new MyMessage(i, "Text " + i);
    }
    
    publisher.initQueue("SynchronizedMessageSystem");
    
    for (int i = 0; i < messages.length; i++) {
      publisher.send(messages[i]);
    }
    
    publisher.close();
  }

}
