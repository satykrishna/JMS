package jms.amq.reliable.persistence;

import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.ActiveMQTopicSession;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.JmsException;

import jms.amq.chat.MyMessage;

public class Consumer {

  private ActiveMQConnectionFactory connectionFactory;
  private ActiveMQConnection        connection;
  private ActiveMQTopic             topic;
  private ActiveMQSession      topicSession;
  private ActiveMQObjectMessage     objectMessage;
  private ActiveMQMessageConsumer   consumer;

  public void receiveMessage() {

    try {

      connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
      connectionFactory.setTrustAllPackages(true);
      connection = (ActiveMQConnection) connectionFactory.createConnection();
      topicSession = (ActiveMQSession) connection.createSession(false, ActiveMQTopicSession.AUTO_ACKNOWLEDGE);
      topic = (ActiveMQTopic) topicSession.createTopic("PersistentDeliveryMode");
      consumer = (ActiveMQMessageConsumer) topicSession.createConsumer(topic);
      connection.start();
      objectMessage = (ActiveMQObjectMessage) consumer.receive();
      MyMessage message = (MyMessage) objectMessage.getObject();
      System.out.println("Message is : " + message);
    }
    catch (JMSException ex) {
      ex.printStackTrace();
    }
    finally {
      try {
        if (topicSession != null) {
          topicSession.close();
        }

        if (consumer != null) {
          try {
            consumer.close();
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }

        if (connection != null) {
          try {
            connection.close();
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      catch (JmsException | JMSException jmsException) {
        jmsException.printStackTrace();
      }

    }

  }

  public static void main(String[] args) {
    Consumer consumer = new Consumer();
    consumer.receiveMessage();
  }

  /*
   * private void intialize() throws JMSException { connectionFactory = new
   * ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
   * connectionFactory.setTrustAllPackages(true); connection =
   * (ActiveMQConnection) connectionFactory.createConnection();
   * connection.start(); }
   * 
   * public Consumer() { try { intialize(); } catch (JMSException e) {
   * e.printStackTrace(); } }
   * 
   * public void setupTopic(String topicName) throws JMSException { topicSession
   * = (ActiveMQTopicSession) connection.createTopicSession(false,
   * ActiveMQTopicSession.AUTO_ACKNOWLEDGE); topic = (ActiveMQTopic)
   * topicSession.createTopic(topicName); consumer = (ActiveMQMessageConsumer)
   * topicSession.createConsumer(topic); }
   * 
   * public void receive() throws JMSException { objectMessage =
   * (ActiveMQObjectMessage) consumer.receive(); MyMessage myMessage
   * =(MyMessage) objectMessage.getObject(); System.out.println(myMessage); }
   * 
   * public void close() throws JMSException { if(consumer != null) {
   * consumer.close(); }
   * 
   * if(topicSession != null){ topicSession.close(); }
   * 
   * if(connection != null) { connection.close(); }
   * 
   * }
   * 
   * public static void main(String[] args) {
   * 
   * Runnable[] jobs = new Runnable[1]; ExecutorService service =
   * Executors.newFixedThreadPool(1);
   * 
   * for(int i = 0; i < jobs.length; ++i ) { jobs[i] = new Runnable() { private
   * Consumer consumer = new Consumer();
   * 
   * @Override public void run() { try {
   * consumer.setupTopic("PersistentDeliveryMode"); consumer.receive();
   * consumer.close(); } catch (JMSException e) { e.printStackTrace(); } } };
   * service.submit(jobs[i]); }
   * 
   * }
   * 
   */

}
