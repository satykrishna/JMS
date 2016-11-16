package example.amq.consume.object;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ConsumeObject {

  private ConnectionFactory connectionFactory;
  private Connection        connection;
  private Session           session;
  private Destination       destination;
  private MessageConsumer   consumer;
  private Message           message;

  public static void main(String[] args) throws JMSException {
    ConsumeObject consume = new ConsumeObject();
    consume.receive("SendObjectMessage");
    consume.close();
  }

  public ConsumeObject() throws JMSException {
    super();
    connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    connection = connectionFactory.createConnection();
    connection.start();
    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
  }

  public void receive(String queue) throws JMSException {
    consumer = session.createConsumer(session.createQueue(queue));
    message = consumer.receive();
    if(message instanceof ObjectMessage) {
      Object object = ((ObjectMessage) message).getObject();
      EventMessage eventMessage = (EventMessage)object;
      System.out.println(eventMessage);
    }
  }

  public void close() throws JMSException {
    if (connection != null) {
      connection.close();
    }

    if (session != null) {
      session.close();
    }
  }
}
