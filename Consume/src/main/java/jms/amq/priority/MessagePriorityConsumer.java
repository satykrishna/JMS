package jms.amq.priority;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.ActiveMQTopicSession;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;

import jms.amq.chat.MyMessage;

public class MessagePriorityConsumer {

  private static final Logger       logger = Logger.getLogger(MessagePriorityConsumer.class);

  private ActiveMQConnectionFactory connectionFactory;
  private ActiveMQConnection        connection;
  private ActiveMQDestination       destination;
  private ActiveMQSession           session;
  private ActiveMQMessageConsumer   consumer;
  private ActiveMQObjectMessage     objectMessage;

  private void intialize() throws JMSException {
    connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    connectionFactory.setTrustAllPackages(true);
    connection = (ActiveMQConnection) connectionFactory.createConnection();
    session = (ActiveMQSession) connection.createSession(false, ActiveMQTopicSession.AUTO_ACKNOWLEDGE);
    connection.start();
    logger.info("Connection is established");
  }

  public void close() throws JMSException {
    if (session != null) {
      session.close();
    }

    if (consumer != null) {
      consumer.close();
    }

    if (connection != null) {
      connection.close();
    }

  }

  public MessagePriorityConsumer() {

    try {
      intialize();
    }
    catch (JMSException e) {
      e.printStackTrace();
    }
  }

  public void setupReceivingDestination(String destinationName) throws JMSException {
    destination = (ActiveMQDestination) session.createTopic(destinationName);
    consumer = (ActiveMQMessageConsumer) session.createConsumer(destination);
    logger.info("consumer is ready to listen messages");
  }

  public void receiveMessages() {

    try {
      consumer.setMessageListener(new MyMessageListener());
      while (true) {

      }
    }
    catch (JMSException e) {
      e.printStackTrace();
    }

  }

  public static void main(String[] args) throws JMSException {
    ExecutorService service = Executors.newFixedThreadPool(10);

    Runnable[] jobs = new Runnable[5];

    for (int i = 0; i < jobs.length; i++) {
      jobs[i] = new Runnable() {
        private MessagePriorityConsumer consumer = new MessagePriorityConsumer();

        @Override
        public void run() {
          try {
            consumer.setupReceivingDestination("AAAAAA");
            consumer.receiveMessages();

          }
          catch (JMSException e) {
            e.printStackTrace();
          }
        }

      };
    }

    for (Runnable job : jobs) {
      service.submit(job);
    }

    service.shutdown();

  }

  private static int count = 0;

  private class MyMessageListener implements MessageListener {

    @Override
    public void onMessage(Message paramMessage) {
      objectMessage = (ActiveMQObjectMessage) paramMessage;
      try {
        logger.info((MyMessage) objectMessage.getObject());
        logger.info("Message Count " + (++count));
      }
      catch (JMSException e) {
        e.printStackTrace();
      }

    }

  }

}
