package org.jms.receive;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Consume {

  public static void main(String[] args) throws NamingException, JMSException {
/*    Properties initialProperties = new Properties();
    initialProperties.put(InitialContext.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
    initialProperties.put(InitialContext.PROVIDER_URL, "tcp://localhost:6161");
    Context context = new InitialContext(initialProperties); */
    ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    Connection connection = factory.createConnection();
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    Destination destination = session.createQueue("SAMPLEQUEUE");
    MessageConsumer consumer = session.createConsumer(destination);
    connection.start();
    Message message = consumer.receive();
    if (message instanceof TextMessage) {
    TextMessage text = (TextMessage) message;
    System.out.println("Message is : " + text.getText());
    }
  }

}
