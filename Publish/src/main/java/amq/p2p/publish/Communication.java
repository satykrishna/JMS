package amq.p2p.publish;

import java.io.Serializable;

public class Communication implements Serializable {

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

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public Communication() {
    super();
    // TODO Auto-generated constructor stub
  }

  public Communication(int id, String text) {
    super();
    this.id = id;
    this.text = text;
  }

  @Override
  public String toString() {
    return "[ id " + id + " text: " + text + " ]";
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {

    Communication another = (Communication) obj;

    if (another == null) {
      return false;
    }

    if (!(another instanceof Communication)) {
      return false;
    }

    if (another.text != this.text) {
      return false;
    }

    if (another.id != this.id) {
      return false;
    }

    return true;
  }
}
