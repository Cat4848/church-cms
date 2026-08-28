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
    <div>
      <div className={`${styles["hymn-heading"]} ${globalStyles["text-align-left"]}`}>
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
        <div className={styles["grid-in-foldable"]}>
          <div className={styles["sections-in-foldable"]}>
            <span>Author Extras:</span>
            <span className={globalStyles["text-bold"]}>{props.authorExtras}</span>
          </div>
          <div className={styles["sections-in-foldable"]}>
            <span>Hymn Book:</span>
            <span className={globalStyles["text-bold"]}>{props.hymnBookName}</span>
          </div>
          <div className={styles["sections-in-foldable"]}>
            <span>Number in Hymn Book:</span>
            <span className={globalStyles["text-bold"]}>{props.numberInHymnBook}</span>
          </div>
          <div className={styles["sections-in-foldable"]}>
            <span>Topic:</span>
            <span className={globalStyles["text-bold"]}>{props.topicName}</span>
          </div>
          <div className={styles["sections-in-foldable"]}>
            <span>Label:</span>
            <span className={globalStyles["text-bold"]}>{props.labelName}</span>
          </div>
          {/*this column is fake so that the lyrics can be on their own row*/}
          <div></div>
          <div className={`${styles["sections-in-foldable"]} ${styles["lyrics-in-foldable"]}`}>
            <span>Lyrics:</span>
            {props.lyrics}
          </div>
        </div>
      )}
    </div>
  );
};

export default Hymn;
