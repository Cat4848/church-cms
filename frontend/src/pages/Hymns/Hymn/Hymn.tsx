import { type Dispatch, type JSX, type SetStateAction, useState } from "react";
import styles from "./Hymn.module.css";
import globalStyles from "../../../css/global.module.css";
import searchAndCreateStyles from "../../../components/SearchAndCreate/SearchAndCreate.module.css";
import { toast } from "react-toastify";
import type { Author } from "../../../domain/Author.ts";
import type { HymnBook } from "../../../domain/HymnBook.ts";
import type { Topic } from "../../../domain/Topic.ts";
import type { Label } from "../../../domain/Label.ts";
import CreateAndUpdateHymnForm from "../../../components/CreateAndUpdateHymnForm/CreateAndUpdateHymnForm.tsx";
import type { CreateHymnPayload } from "../Hymns/Hymns.tsx";
import type { Hymn } from "../../../domain/Hymn.ts";

interface Props {
  author: Author;
  hymn: Hymn;
  hymnBook?: HymnBook;
  topic?: Topic;
  label?: Label;
  authors: Author[];
  hymnBooks: HymnBook[];
  topics: Topic[];
  labels: Label[];
  onUpdateHymn: (hymn: Hymn) => void;
}

const chevronRight: JSX.Element = <img height={20} src="../../../../public/chevron-right.svg" alt="chevron-right" />;
const chevronDown: JSX.Element = <img height={20} src="../../../../public/chevron-down.svg" alt="chevron-down" />;

const Hymn = (props: Props) => {
  // controlled only from the HymnHeading component
  const [isHeadingExpanded, setIsHeadingExpanded] = useState(false);
  const [isUpdatingHymn, setIsUpdatingHymn] = useState(false);
  const [updateHymnFormData, setUpdateHymnFormData] = useState<CreateHymnPayload>({
    authorId: props.author.authorId,
    authorExtras: props.hymn.authorExtras,
    title: props.hymn.title,
    lyrics: props.hymn.lyrics,
    hymnBookId: props.hymnBook?.hymnBookId,
    numberInHymnBook: props.hymn.numberInHymnBook,
    topicId: props.topic?.topicId,
    labelId: props.label?.labelId,
  });
  if (props.hymn.hymnId === 2) {
    console.log("updateHymnFormData", updateHymnFormData);
    console.log("update hymn props", props);
  }

  const handleSetUpdateHymnFormData = (
    key: keyof CreateHymnPayload,
    value: CreateHymnPayload[keyof CreateHymnPayload],
  ) => {
    setUpdateHymnFormData({ ...updateHymnFormData, [key]: value });
  };

  const handleCopyLyricsMenuAction = async () => {
    try {
      await navigator.clipboard.writeText(props.hymn.lyrics);
    } catch (e) {
      toast.error("Not copied. Please try again.");
    }
  };

  const handleUpdateHymnMenuAction = () => {
    setIsUpdatingHymn(true);
  };

  const handleUpdateHymn = () => {
    const hymn: Hymn = {
      ...updateHymnFormData,
      hymnId: props.hymn.hymnId,
    };
    props.onUpdateHymn(hymn);
  };

  return isUpdatingHymn ? (
    <div>
      {/*// todo remove some functionality from the hymn heading like the more button and collapse*/}
      <HymnHeading
        hymn={props.hymn}
        author={props.author}
        isUpdatingHymn={isUpdatingHymn}
        isHeadingExpanded={isHeadingExpanded}
        setIsHeadingExpanded={setIsHeadingExpanded}
        onCopyLyricsMenuAction={handleCopyLyricsMenuAction}
        onUpdateHymnMenuAction={handleUpdateHymnMenuAction}
      />
      <div className={styles["update-hymn-box"]}>
        <div className={searchAndCreateStyles["close-button-box"]}>
          <span onClick={() => setIsUpdatingHymn(false)}>⛌</span>
        </div>
        <CreateAndUpdateHymnForm
          hymnId={props.hymn.hymnId}
          formData={updateHymnFormData}
          setFormData={handleSetUpdateHymnFormData}
          setFormDataRaw={setUpdateHymnFormData}
          authors={props.authors}
          hymnBooks={props.hymnBooks}
          topics={props.topics}
          labels={props.labels}
        />
        <button onClick={handleUpdateHymn}>Update</button>
      </div>
    </div>
  ) : (
    <div>
      <HymnHeading
        hymn={props.hymn}
        author={props.author}
        isUpdatingHymn={isUpdatingHymn}
        isHeadingExpanded={isHeadingExpanded}
        setIsHeadingExpanded={setIsHeadingExpanded}
        onCopyLyricsMenuAction={handleCopyLyricsMenuAction}
        onUpdateHymnMenuAction={handleUpdateHymnMenuAction}
      />
      {isHeadingExpanded && (
        <div className={styles["grid-in-foldable"]}>
          <div className={styles["sections-in-foldable"]}>
            <span>Author Extras:</span>
            <span className={globalStyles["text-bold"]}>{props.hymn.authorExtras ? props.hymn.authorExtras : "–"}</span>
          </div>
          <div className={styles["sections-in-foldable"]}>
            <span>Hymn Book:</span>
            <span className={globalStyles["text-bold"]}>{props.hymnBook ? props.hymnBook.name : "–"}</span>
          </div>
          <div className={styles["sections-in-foldable"]}>
            <span>Number in Hymn Book:</span>
            <span className={globalStyles["text-bold"]}>
              {props.hymn.numberInHymnBook ? props.hymn.numberInHymnBook : "–"}
            </span>
          </div>
          <div className={styles["sections-in-foldable"]}>
            <span>Topic:</span>
            <span className={globalStyles["text-bold"]}>{props.topic ? props.topic.name : "–"}</span>
          </div>
          <div className={styles["sections-in-foldable"]}>
            <span>Label:</span>
            <span className={globalStyles["text-bold"]}>{props.label ? props.label.name : "–"}</span>
          </div>
          {/*this column is fake so that the lyrics can be on their own row*/}
          <div></div>
          <div className={`${styles["sections-in-foldable"]} ${styles["lyrics-in-foldable"]}`}>
            <div>Lyrics:</div>
            {props.hymn.lyrics}
          </div>
        </div>
      )}
    </div>
  );
};

interface HymnHeadingProps {
  hymn: Hymn;
  author: Author;
  isUpdatingHymn: boolean;
  isHeadingExpanded: boolean;
  setIsHeadingExpanded: Dispatch<SetStateAction<boolean>>;
  onCopyLyricsMenuAction: () => void;
  onUpdateHymnMenuAction: () => void;
}

const HymnHeading = (props: HymnHeadingProps) => {
  const [isMenuShow, setIsMenuShow] = useState(false);

  const handleUpdateHymnMenuAction = () => {
    props.setIsHeadingExpanded(true);
    setIsMenuShow(false);
    props.onUpdateHymnMenuAction();
  };

  const handleCopyLyricsMenuAction = () => {
    setIsMenuShow(false);
    props.onCopyLyricsMenuAction();
  };

  const handleToggleMenu = () => {
    if (!props.isUpdatingHymn) {
      setIsMenuShow(!isMenuShow);
    }
  };

  const handleToggleExpandHeading = () => {
    if (!props.isUpdatingHymn) {
      props.setIsHeadingExpanded(!props.isHeadingExpanded);
    }
  };

  return (
    <div className={`${styles["hymn-heading"]} ${globalStyles["text-align-left"]}`}>
      <div className={styles["title-and-author"]}>
        {!props.isUpdatingHymn && (
          <div
            className={`${styles["chevron"]} ${props.isUpdatingHymn && globalStyles["disabled-div"]}`}
            onClick={handleToggleExpandHeading}
          >
            {props.isHeadingExpanded ? chevronRight : chevronDown}
          </div>
        )}
        <div className={`${props.isUpdatingHymn && globalStyles["padding-left-point-five-rem"]}`}>
          <div>{props.hymn.title}</div>
          <div className={styles["author"]}>{props.author.name}</div>
        </div>
      </div>
      {!props.isUpdatingHymn && (
        <div className={styles["menu-dots"]}>
          <span onClick={handleToggleMenu}>
            <img height={20} src="../../../../public/menu-dots-vertical.svg" alt="three-dots-menu-icon" />
          </span>
          {isMenuShow && (
            <div className={styles["hymn-menu"]}>
              <div className={styles["menu-item"]} onClick={handleCopyLyricsMenuAction}>
                Copy Lyrics
              </div>
              <div className={styles["menu-item"]} onClick={handleUpdateHymnMenuAction}>
                Update
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default Hymn;
