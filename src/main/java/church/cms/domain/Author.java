package church.cms.domain;

public class Author {
  private Integer authorId;
  private String name;


  public Author(Integer authorId, String name) {
    this.authorId = authorId;
    this.name = name;
  }

  public Author(String name) {
    this.name = name;
  }

  public Author() {
  }

  public void setAuthorId(Integer authorId) {
    this.authorId = authorId;
  }

  public Integer getAuthorId() {
    return authorId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}