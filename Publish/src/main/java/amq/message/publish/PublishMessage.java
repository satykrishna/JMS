package amq.message.publish;

import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.amq.type.EventMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class PublishMessage {

  private ActiveMQConnectionFactory factory;
  private QueueConnection        connection;
  private Queue                  queue;
  private QueueSession           session;
  private QueueSender            sender;
  
  
  public static void main(String[] args) throws JMSException {
    PublishMessage publisher = new PublishMessage();
    publisher.send();
    System.out.println("Done");
    
  }
  
  public void send() throws JMSException {
    System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
    factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    factory.setTrustAllPackages(true);
    connection = factory.createQueueConnection();
    connection.start();
    session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
    queue = session.createQueue("test");
    sender = session.createSender(queue);
    EventMessage comm = new EventMessage(1000, "This is test message from satyakrishna kondapalli");
    Message message =  session.createObjectMessage(comm);
    sender.send(message);
    if(session != null ){
      session.close();
    }
    if(connection != null) {
      connection.close();
    }
    
  }

  public QueueConnectionFactory getFactory() {
    return factory;
  }

  public void setFactory(ActiveMQConnectionFactory factory) {
    this.factory = factory;
  }

  public QueueConnection getConnection() {
    return connection;
  }

  public void setConnection(QueueConnection connection) {
    this.connection = connection;
  }

  public Queue getQueue() {
    return queue;
  }

  public void setQueue(Queue queue) {
    this.queue = queue;
  }

  public QueueSession getSession() {
    return session;
  }

  public void setSession(QueueSession session) {
    this.session = session;
  }

  public QueueSender getSender() {
    return sender;
  }

  public void setSender(QueueSender sender) {
    this.sender = sender;
  }

  public PublishMessage() {
    System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
  }
  
}
