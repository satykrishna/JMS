package jms.amq.priority;

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

public class MessagePriorityProducer {

  private static final Logger       logger = Logger.getLogger(MessagePriorityProducer.class);

  private ActiveMQConnectionFactory connectionFactory;
  private ActiveMQConnection        connection;
  private ActiveMQDestination       destination;
  private ActiveMQSession           session;
  private ActiveMQMessageProducer   producer;

  public static void main(String args[]) throws JMSException {

    ExecutorService service = Executors.newFixedThreadPool(3);
    
    Runnable[] jobs = new Runnable[4];
    for(int i = 0; i < jobs.length; ++i) {
      jobs[i] = new Runnable() {
        private  MessagePriorityProducer producer = new MessagePriorityProducer();
        @Override
        public void run() {
          try {
            producer.setDestination("AAAAAA");
          }
          catch (JMSException e) {
            e.printStackTrace();
          }
          MyMessage[] messages = new MyMessage[100];
          for (int i = 0; i < messages.length; ++i) {
            String text = Thread.currentThread().getName();
            messages[i] = new MyMessage(i, text + " ----message is ----" + i);
            try {
              producer.send(messages[i], (int) (Math.random() * 10));
            }
            catch (JMSException e) {
              e.printStackTrace();
            }
          }
          try {
            producer.close();
          }
          catch (JMSException e) {
            e.printStackTrace();
          }
        }
      };
    }
    
    for(Runnable job : jobs) {
      service.submit(job);
    }

    service.shutdown();

  }

  public void close() throws JMSException {
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

  private void initialize() throws JMSException {
    connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    connectionFactory.setTrustAllPackages(true);
    connection = (ActiveMQConnection) connectionFactory.createConnection();
    session = (ActiveMQSession) connection.createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);
    connection.start();
    logger.info("Connection has already started");
  }

  public void setDestination(String destinationName) throws JMSException {
    destination = (ActiveMQDestination) session.createTopic(destinationName);
    producer = (ActiveMQMessageProducer) session.createProducer(destination);
    logger.info("Producer is initalized to send messages");
  }

  public void send(MyMessage message, int priorityIndex) throws JMSException {
    ActiveMQObjectMessage payLoad = (ActiveMQObjectMessage) session.createObjectMessage(message);
    producer.setPriority(priorityIndex);
    logger.info(message + " is sent with priority " + priorityIndex);
    producer.send(payLoad);
  }

  public MessagePriorityProducer() {
    try {
      initialize();
    }
    catch (JMSException e) {
      e.printStackTrace();
    }
  }

}
