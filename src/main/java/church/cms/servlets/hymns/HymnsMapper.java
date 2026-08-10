package church.cms.servlets.hymns;

import church.cms.domain.Hymn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HymnsMapper {
  public List<Hymn> map(ResultSet rs) throws SQLException {
    List<Hymn> hymns = new ArrayList<>();

    while (rs.next()) {
      Integer hymnId = rs.getInt("hymn_id");
      Integer authorId = rs.getInt("author_id");
      String authorExtras = rs.getString("author_extras");
      String title = rs.getString("title");
      String lyrics = rs.getString("lyrics");
      Integer hymnBookId = rs.getInt("hymn_book_id");
      Integer numberInHymnBook = rs.getInt("number_in_hymn_book");
      Integer topicId = rs.getInt("topic_id");
      Integer labelId = rs.getInt("label_id");

      Hymn hymn = new Hymn(
              hymnId,
              authorId,
              authorExtras,
              title,
              lyrics,
              hymnBookId,
              numberInHymnBook,
              topicId,
              labelId
      );
      hymns.add(hymn);
    }

    return hymns;
  }
}
