package jms.amq.expiry;

import java.io.IOException;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.log4j.Logger;

public class MessageExpiryConsumer {
  
  private static final Logger logger = Logger.getLogger(MessageExpiryConsumer.class);
  private ActiveMQConnectionFactory connectionFactory;
  private ActiveMQConnection connection;
  private ActiveMQDestination destination;
  private ActiveMQMessageConsumer consumer;
  private MessageExpiryListener listener;
  private ActiveMQSession session;
  
  public MessageExpiryConsumer() {
    listener = new MessageExpiryListener();
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
    session = (ActiveMQSession) connection.createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);
    connection.start();
  }
  
  public void setupDestination(String name) throws JMSException {
    destination = (ActiveMQDestination) session.createQueue(name);
    consumer = (ActiveMQMessageConsumer) session.createConsumer(destination);
    consumer.setMessageListener(listener);
  }
  
  public static void main(String[] args) throws Exception {
    MessageExpiryConsumer consumer = new MessageExpiryConsumer();
    consumer.setupDestination("MessageExpiryProducer");
    consumer.receive();
    consumer.close();
  }  
  
  public void receive() throws IOException {
    while(System.in.available() == 0) {
      
    }
    
    logger.info("Thanks for receiving messages!!!.. Please publsh and re run this program" );
    
  }
  
  public void close() throws JMSException {
    if(consumer != null){
      consumer.close();
    }
    
    if(session != null) {
      session.close();
    }
    
    if(connection != null) {
      connection.close();
    }
  }

}
 