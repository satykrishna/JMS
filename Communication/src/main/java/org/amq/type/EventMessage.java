package org.amq.type;

import java.io.Serializable;

public class EventMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  private int               id;

  private String            text;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public EventMessage() {
    super();
    // TODO Auto-generated constructor stub
  }

  public EventMessage(int id, String text) {
    super();
    this.id = id;
    this.text = text;
  }
  
  @Override
  public String toString() {
    return "id " + id + " text : " + text;
  }

}
