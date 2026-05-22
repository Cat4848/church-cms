package church.cms.domain;

public class Hymn {
  private Integer hymnId;
  private Integer authorId;
  private String authorExtras;
  private String title;
  private String lyrics;
  private Integer hymnBookId;
  private Integer numberInHymnBook;
  private Integer topicId;
  private Integer labelId;

  public Hymn(
          Integer hymnId,
          Integer authorId,
          String authorExtras,
          String title,
          String lyrics,
          Integer hymnBookId,
          Integer numberInHymnBook,
          Integer topicId,
          Integer labelId) {
    this.hymnId = hymnId;
    this.authorId = authorId;
    this.authorExtras = authorExtras;
    this.title = title;
    this.lyrics = lyrics;
    this.hymnBookId = hymnBookId;
    this.numberInHymnBook = numberInHymnBook;
    this.topicId = topicId;
    this.labelId = labelId;
  }

  public Hymn(
          Integer authorId,
          String authorExtras,
          String title,
          String lyrics,
          Integer hymnBookId,
          Integer numberInHymnBook,
          Integer topicId,
          Integer labelId) {
    this.authorId = authorId;
    this.authorExtras = authorExtras;
    this.title = title;
    this.lyrics = lyrics;
    this.hymnBookId = hymnBookId;
    this.numberInHymnBook = numberInHymnBook;
    this.topicId = topicId;
    this.labelId = labelId;
  }

  public Hymn() {
  }

  public void setHymnId(Integer hymnId) {
    this.hymnId = hymnId;
  }

  public Integer getHymnId() {
    return hymnId;
  }

  public void setAuthorId(Integer authorId) {
    this.authorId = authorId;
  }

  public Integer getAuthorId() {
    return authorId;
  }

  public void setAuthorExtras(String authorExtras) {
    this.authorExtras = authorExtras;
  }

  public String getAuthorExtras() {
    return authorExtras;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setLyrics(String lyrics) {
    this.lyrics = lyrics;
  }

  public String getLyrics() {
    return lyrics;
  }

  public void setHymnBookId(Integer hymnBookId) {
    this.hymnBookId = hymnBookId;
  }

  public Integer getHymnBookId() {
    return hymnBookId;
  }

  public void setNumberInHymnBook(Integer numberInHymnBook) {
    this.numberInHymnBook = numberInHymnBook;
  }

  public Integer getNumberInHymnBook() {
    return numberInHymnBook;
  }

  public void setTopicId(Integer topicId) {
    this.topicId = topicId;
  }

  public Integer getTopicId() {
    return topicId;
  }

  public void setLabelId(Integer labelId) {
    this.labelId = labelId;
  }

  public Integer getLabelId() {
    return labelId;
  }
}