package church.cms.domain;

public class Topic {
  private Integer topicId;
  private String name;


  public Topic(Integer topicId, String name) {
    this.topicId = topicId;
    this.name = name;
  }

  public Topic(String name) {
    this.name = name;
  }

  public Topic() {
  }

  public void setTopicId(Integer topicId) {
    this.topicId = topicId;
  }

  public Integer getTopicId() {
    return topicId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}