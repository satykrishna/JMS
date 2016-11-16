package example.amq.send.object;

import java.io.Serializable;

public class EventMessage implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private int messageId;
  
  private String message;

  public int getMessageId() {
    return messageId;
  }

  public void setMessageId(int messageId) {
    this.messageId = messageId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public EventMessage() {
    super();
  }

  public EventMessage(int messageId, String message) {
    super();
    this.messageId = messageId;
    this.message = message;
  }
  
  @Override
  public String toString() {
    return "Message = [" + messageId + " Text : " + message + " ]" ;
  }
  

  @Override
  public int hashCode() {
    return messageId;
  }
  
  @Override
  public boolean equals(Object obj) {
    if(!(obj instanceof EventMessage) || obj == null) {
      return false;
    }
    
    EventMessage another = (EventMessage)obj;
    
    if(another.messageId == this.messageId) {
      return true;
    }
    
    return false;
  }
  

}
