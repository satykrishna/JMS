package jms.amq.send;

import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQTopicPublisher;
import org.apache.activemq.ActiveMQTopicSession;
import org.apache.activemq.broker.PublishedAddressPolicy.PublishedHostStrategy;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.log4j.Logger;

import jms.amq.chat.MyMessage;

public class MyTopicPublisher {

  private final static Logger       logger = Logger.getLogger(MyTopicPublisher.class);

  private ActiveMQConnectionFactory connectionFactory;
  private ActiveMQConnection        connection;
  private ActiveMQTopicSession      topicSession;
  private ActiveMQTopic             topic;
  private ActiveMQTopicPublisher    publisher;
  private ActiveMQObjectMessage     payLoad;

  public void connectToAMQ(String topicName) throws JMSException {
    connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    connection = (ActiveMQConnection) connectionFactory.createConnection();
    connection.start();
    topicSession = (ActiveMQTopicSession) connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
    topic = (ActiveMQTopic) topicSession.createTopic(topicName);
    publisher = (ActiveMQTopicPublisher) topicSession.createPublisher(topic);
    logger.info("Connection to " + topicName + " is established, You can post messages related to this news");
  }

  public void close() throws JMSException {
    if (topicSession == null && connection == null) {
      return;
    }

    if (topicSession != null) {
      topicSession.close();
    }

    if (connection != null) {
      connection.close();
    }

    logger.info("Closing your session for the given topic " + topic.getTopicName() + ", Please re-run to publish ");
  }

  public void post(MyMessage subject) throws JMSException {
    payLoad = (ActiveMQObjectMessage) topicSession.createObjectMessage(subject);
    publisher.send(payLoad);
    logger.info(subject + " is  posted in the topic and interested subscribers will be able to view");
  }

  public void showMenu() throws JMSException {

    System.out.println("====================================");
    System.out.println("WELCOME TO MY MESSAGING SYSTEM");
    System.out.println("====================================");
    System.out.println("1. Type '1' to either create/switch to new Topic\n" + "2. Type 'EXIT' to close");
  }


  public static void main(String[] args) throws JMSException {

    MyTopicPublisher publisherClient = new MyTopicPublisher();
    publisherClient.showMenu();
    publisherClient.connectToAMQ("AMQ");
    for(int i = 0; i < 100; i++) {
      publisherClient.post(new MyMessage(i, "Text"+i));
    }
    publisherClient.close();
  }

}
