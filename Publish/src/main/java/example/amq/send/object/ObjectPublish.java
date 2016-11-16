package example.amq.send.object;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ObjectPublish {

  private ConnectionFactory connectionFactory;
  private Connection        connection;
  private Session           session;
  private ObjectMessage     message;
  private Destination       destination;
  private MessageProducer  messageProducer;
  
  private EventMessage eventMessageObject;

  public ObjectPublish(EventMessage eventMessageObject) throws JMSException {
    connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    connection = connectionFactory.createConnection();
    connection.start();
    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    this.eventMessageObject = eventMessageObject;
  }
  
  public void send(String queue) throws JMSException {
    destination = session.createQueue(queue);
    messageProducer = session.createProducer(destination);
    message = session.createObjectMessage(eventMessageObject);
    messageProducer.send(message);
  }
  
  public void close() throws JMSException {
    
    if(session != null ) {
      session.close();
    }
    if(connection != null ) {
      connection.close();
    }
  }
  
  public static void main(String[] args) throws JMSException {
    ObjectPublish publish = new ObjectPublish(new EventMessage(1, "this is second message"));
    publish.send("SendObjectMessage");
    publish.close();
  }

}
