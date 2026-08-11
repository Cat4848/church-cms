interface Props {
  hymnId: number;
  authorName: string;
  authorExtras?: string;
  title: string;
  lyrics: string;
  hymnBookName?: string;
  numberInHymnBook?: number;
  topicName?: string;
  labelName?: string;
}

const Hymn = (props: Props) => {
  return (
    <>
      <div style={{ textAlign: "start" }}>
        <div>{props.title}</div>
        <div style={{ fontStyle: "italic", fontSize: "0.9rem" }}>{props.authorName}</div>
      </div>
      <div>3 dots</div>
    </>
  );
};

export default Hymn;
