package jms.amq.send;

import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQTopicPublisher;
import org.apache.activemq.ActiveMQTopicSession;
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
    System.out.println("1. Type 'CREATE' to either create/switch to new Topic\n" + "2. Type 'EXIT' to close");
    takeInputs();
  }

  private void takeInputs() throws JMSException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("\n Enter your choice : ");
    boolean invalidOption = false;
    while (true) {
      String option = scanner.nextLine();
      if (option.length() == 0) {
        logger.error("invalid option choosed");
        invalidOption = true;
        break;
      }

      else if (option.equalsIgnoreCase("EXIT")) {
        logger.info("Thanks for connecting to my messaging system.. GoodBye!!!");
        break;
      }

      else if (option.equalsIgnoreCase("CREATE")) {
        close();

        while (true) {
          System.out.println("Enter the new/existing topic : ");
          String topicName = scanner.nextLine();
          if (topicName.length() != 0) {
            connectToAMQ(topicName);
            publishMessages();
            break;
          }
        }
      }
      else {
        invalidOption = true;
        break;
      }
    }

    if (invalidOption) {
      logger.info("invalid option");
      showMenu();
    }

    scanner.close();
  }

  private void publishMessages() throws JMSException {

    System.out.println("Type Q to stop publishing to this Topic");
     Scanner scanner = new Scanner(System.in);
    
    while (true) {
      System.out.println("Enter message Id:  ");
      int messageId = Integer.MIN_VALUE;
      try {
        String in = scanner.nextLine();
        if (in.equalsIgnoreCase("Q")) {
          break;
        }
        int id = Integer.parseInt(in);
        messageId = id;
      }
      catch (Exception exception) {
        logger.warn("Invalid message id entered and hence assigning it a default value");
      }
      String text=null;
     l2: while (true) {
        System.out.println("Enter a valid message ---<TYPE Q to QUIT>---- : ");
        text = scanner.nextLine();
        if (text.equalsIgnoreCase("Q")) {
          break;
        }
        if(text.length() != 0) {
          MyMessage subject = new MyMessage(messageId, text);
          post(subject);
          break l2;
        }
      }
    }
    scanner.close();
    showMenu();
  }

  public static void main(String[] args) throws JMSException {

    MyTopicPublisher publisherClient = new MyTopicPublisher();
    publisherClient.showMenu();
  }

}
