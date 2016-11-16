package amq.p2p.queue;

import java.util.Arrays;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;

import org.amq.type.EventMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ReceiveMessage {

  private ActiveMQConnectionFactory factory;

  private QueueConnection           connection;
  private Queue                     queue;
  private QueueSession              session;
  private MessageConsumer           consumer;

  public ReceiveMessage() {
 
  }

  public void consume() throws JMSException {
    System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
    factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
//    factory.setTrustAllPackages(true);
    connection = factory.createQueueConnection();
    connection.start();
    session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
    queue = session.createQueue("test");
    consumer = session.createConsumer(queue);
    Message message = consumer.receive();
    session.close();
    connection.close();

    if (message instanceof ObjectMessage) {
      Object obj = ((ObjectMessage) message).getObject();
      System.out.println(this.getClass().getName() + " has received a message : " + (EventMessage) obj);
    }
  }

  public static void main(String[] args) throws JMSException {
    ReceiveMessage r = new ReceiveMessage();
    r.consume();
  }

}
