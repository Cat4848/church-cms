import { type JSX, useState } from "react";
import styles from "./Hymn.module.css";
import globalStyles from "../../../css/global.module.css";

interface Props {
  hymnId: number;
  authorName: string;
  authorExtras?: string;
  title: string;
  lyrics: string;
  hymnBookName: string;
  numberInHymnBook: number | string | undefined;
  topicName: string;
  labelName: string;
}

const chevronRight: JSX.Element = <img height={20} src="../../../../public/chevron-right.svg" alt="chevron-right" />;
const chevronDown: JSX.Element = <img height={20} src="../../../../public/chevron-down.svg" alt="chevron-down" />;

const Hymn = (props: Props) => {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div className={globalStyles["text-align-left"]}>
      <div className={styles["hymn-heading"]}>
        <div className={styles["title-and-author"]}>
          <div className={styles["chevron"]} onClick={() => setIsExpanded(!isExpanded)}>
            {isExpanded ? chevronRight : chevronDown}
          </div>
          <div>
            <div>{props.title}</div>
            <div className={styles["author"]}>{props.authorName}</div>
          </div>
        </div>
        <div>3 dots</div>
      </div>
      {isExpanded && (
        <div>
          <div>
            <span>Author Extras</span>
            {props.authorExtras}
          </div>
          <div>
            <span>Hymn Book</span>
            {props.hymnBookName}
          </div>
          <div>
            <span>Number in Hymn Book</span>
            {props.numberInHymnBook}
          </div>
          <div>
            <span>Topic</span>
            {props.topicName}
          </div>
          <div>
            <span>Label</span>
            {props.labelName}
          </div>
          <div style={{ whiteSpace: "pre-wrap" }}>
            <span>Lyrics</span>
            {props.lyrics}
          </div>
        </div>
      )}
    </div>
  );
};

export default Hymn;
