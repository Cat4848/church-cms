package church.cms.domain;

public class HymnBook {
  private Integer hymnBookId;
  private String name;


  public HymnBook(Integer hymnBookId, String name) {
    this.hymnBookId = hymnBookId;
    this.name = name;
  }

  public HymnBook(String name) {
    this.name = name;
  }

  public HymnBook() {
  }

  public void setHymnBookId(Integer hymnBookId) {
    this.hymnBookId = hymnBookId;
  }

  public Integer getHymnBookId() {
    return hymnBookId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}