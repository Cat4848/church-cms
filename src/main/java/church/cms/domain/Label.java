package church.cms.domain;

public class Label {
  private Integer labelId;
  private String name;


  public Label(Integer labelId, String name) {
    this.labelId = labelId;
    this.name = name;
  }

  public Label(String name) {
    this.name = name;
  }

  public Label() {
  }

  public void setLabelId(Integer labelId) {
    this.labelId = labelId;
  }

  public Integer getLabelId() {
    return labelId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}