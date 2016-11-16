package example.jms.send;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Publisher {

  public static void main(String[] args) throws NamingException, JMSException {

    Context context = null;
    ConnectionFactory connectionFactory = null;
    Connection connection = null;
    Destination destination = null;
    Session session = null;
    MessageProducer messageProducer = null;

    // This is OPEN JMS configuration add openjms related libraries to the application
    /*
     * Properties initialProperties = new Properties();
     * initialProperties.put(InitialContext.INITIAL_CONTEXT_FACTORY,
     * "org.exolab.jms.jndi.InitialContextFactory");
     * initialProperties.put(InitialContext.PROVIDER_URL,
     * "tcp://localhost:3035"); context = new InitialContext(initialProperties);
     * connectionFactory = (ConnectionFactory)
     * context.lookup("ConnectionFactory"); destination = (Destination)
     * context.lookup("queue1"); connection =
     * connectionFactory.createConnection(); session =
     * connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
     * messageProducer = session.createProducer(destination);
     * connection.start(); TextMessage textMessage =
     * session.createTextMessage(); textMessage.setText("Hi");
     * messageProducer.send(textMessage); System.out.println("Sent : " +
     * textMessage.getText()); context.close(); connection.close();
     */

    connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    
    for(int i = 0; i < 100; i++) {
      connection = connectionFactory.createConnection();
      connection.start();
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      destination = session.createQueue("SAMPLEQUEUE");
      messageProducer = session.createProducer(destination);
      TextMessage message = session.createTextMessage();
      message.setJMSPriority(8);
      message.setText("Hello ...This is message " + i);
      messageProducer.send(message);
      session.close();
      connection.close();      
    }
  }

}
