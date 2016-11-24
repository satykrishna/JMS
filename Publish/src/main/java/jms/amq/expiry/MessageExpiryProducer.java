package jms.amq.expiry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;

import jms.amq.chat.MyMessage;

public class MessageExpiryProducer {
  private static int                count  = 0;
  private static final Logger       logger = Logger.getLogger(MessageExpiryProducer.class);
  private ActiveMQConnectionFactory connectionFactory;
  private ActiveMQConnection        connection;
  private ActiveMQSession           session;
  private ActiveMQDestination       destination;
  private ActiveMQMessageProducer   producer;
  private ActiveMQObjectMessage     payLoad;

  private void initialize() throws JMSException {
    connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    connectionFactory.setTrustAllPackages(true);
    connection = (ActiveMQConnection) connectionFactory.createConnection();
    session = (ActiveMQSession) connection.createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);
    logger.info("Intialize Connection to AMQ");
  }

  private void setupDestination(String destinationName) throws JMSException {
    destination = (ActiveMQDestination) session.createQueue(destinationName);
    producer = (ActiveMQMessageProducer) session.createProducer(destination);
    producer.setTimeToLive(10*100*100*100);
    logger.info("Ready to publish Messages to AMQ");
  }

  private void send(MyMessage message) throws JMSException {
    payLoad = (ActiveMQObjectMessage) session.createObjectMessage(message);
    producer.send(payLoad);
    logger.info("Count : " + count++);
  }

  private void close() throws JMSException {
    if (producer != null) {
      producer.close();
    }
    if (session != null) {
      session.close();
    }
    if (connection != null) {
      connection.close();
    }
  }

  public MessageExpiryProducer() {
    try {
      initialize();
    }
    catch (JMSException e) {
      e.printStackTrace();
    }
  }

  public void sendMessages() {
    ExecutorService service = Executors.newFixedThreadPool(3);
    Runnable[] runnables = new Runnable[10];
    for (int i = 0; i < runnables.length; ++i) {
      runnables[i] = new MyRunnable();
    }

    for (Runnable job : runnables) {
      service.submit(job);
    }

    service.shutdown();
  }

  public static void main(String[] args) {

    MessageExpiryProducer producer = new MessageExpiryProducer();
    producer.sendMessages();
      
  }

  public class MyRunnable implements Runnable {

    private MessageExpiryProducer producer = new MessageExpiryProducer();

    private MyMessage[]           messages = new MyMessage[100];

    public MyRunnable() {
      setupMessages();
    }

    private void setupMessages() {
      for (int i = 0; i < messages.length; ++i) {
        messages[i] = new MyMessage(i, Thread.currentThread().getName() + " ... " + i);
      }
    }

    @Override
    public void run() {
      try {
        producer.setupDestination("MessageExpiryProducer");
        for (int i = 0; i < messages.length; i++) {
          producer.send(messages[i]);
          logger.info("Sent : " + messages[i]);
        }
        producer.close();
      }
      catch (JMSException e) {
        e.printStackTrace();
      }

    }

  }

}
