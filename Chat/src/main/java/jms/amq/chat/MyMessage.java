package jms.amq.chat;

import java.io.Serializable;

public class MyMessage implements Serializable {

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

  public MyMessage() {
    super();
  }

  public MyMessage(int id, String text) {
    super();
    this.id = id;
    this.text = text;
  }

  @Override
  public String toString() {
    return "MyMessage [id=" + id + ", text=" + text + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + ((text == null) ? 0 : text.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MyMessage other = (MyMessage) obj;
    if (id != other.id)
      return false;
    if (text == null) {
      if (other.text != null)
        return false;
    }
    else if (!text.equals(other.text))
      return false;
    return true;
  }

}
