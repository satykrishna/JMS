package amq.message.pubsub;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQTopicPublisher;
import org.apache.activemq.ActiveMQTopicSession;
import org.apache.activemq.command.ActiveMQTopic;

public class TopicPublisher {
  
  private ActiveMQConnectionFactory connectionFactory;
  private ActiveMQConnection connection;
  private ActiveMQTopicSession topicSession;
  private ActiveMQTopic topic;
  private ActiveMQTopicPublisher publisher;
  
  public TopicPublisher() {
    
  }

}
