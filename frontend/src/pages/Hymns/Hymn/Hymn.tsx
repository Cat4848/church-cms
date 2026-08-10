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
      <div>
        <div>{props.title}</div>
        <div>{props.authorName}</div>
      </div>
      <div>3 dots</div>
    </>
  );
};

export default Hymn;
