package jms.reliable.persistence;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQTopicPublisher;
import org.apache.activemq.ActiveMQTopicSession;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTopic;

import jms.amq.chat.MyMessage;

public class Publisher {

  private ActiveMQConnectionFactory connectionFactory;
  private ActiveMQConnection        connection;
  private ActiveMQTopic             topic;
  private ActiveMQTopicSession      topicSession;
  private ActiveMQObjectMessage     objectMessage;
  private ActiveMQTopicPublisher    topicPublisher;

  public Publisher() {

    try {
      initialize();
    }
    catch (JMSException exception) {
      System.out.println(exception);
    }

  }

  private void initialize() throws JMSException {
    connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    connectionFactory.setTrustAllPackages(true);
    connection = (ActiveMQConnection) connectionFactory.createConnection();
    connection.start();
  }

  public void setupTopic(String topicName) throws JMSException {
    topicSession = (ActiveMQTopicSession) connection.createTopicSession(false, ActiveMQTopicSession.AUTO_ACKNOWLEDGE);
    topic = (ActiveMQTopic) topicSession.createTopic(topicName);
    topicPublisher = (ActiveMQTopicPublisher) topicSession.createPublisher(topic);
    topicPublisher.setDeliveryMode(DeliveryMode.PERSISTENT);
  }

  public void sendtoTopic(MyMessage messageToSend) throws JMSException {
    objectMessage = (ActiveMQObjectMessage) topicSession.createObjectMessage(messageToSend);
    topicPublisher.send(objectMessage);
  }

  public void close() throws JMSException {
    if (topicPublisher != null) {
      topicPublisher.close();
    }
    
    if (topicSession != null) {
      topicSession.close();
    }
    
    if (connection != null) {
      connection.close();
    }
  }
  
  public static void main(String[] args) throws JMSException {
    Publisher publisher = new Publisher();
    publisher.setupTopic("PersistentDeliveryMode");
    MyMessage[] messages = new MyMessage[100];
    
    for(int i = 0; i < messages.length; ++i) {
      messages[i] = new MyMessage(i, "Text " + i);
      publisher.sendtoTopic(messages[i]);
    }
    
    publisher.close();
  }
}

