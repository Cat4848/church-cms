import globalStyles from "../../css/global.module.css";
import styles from "./SearchAndCreate.module.css";
import { type JSX, useState } from "react";

interface Props {
  searchTerm: string;
  onChange: (searchTerm: string) => void;
  children: JSX.Element;
  entity: "Author" | "Hymn Book" | "Topic" | "Label" | "Hymn";
  isCreateButtonDisabled?: boolean;
  isCreateFormValid?: boolean;
  onCreate: (closeCreateForm: () => void) => void;
  resetCreateState?: () => void;
}

const SearchAndCreate = (props: Props) => {
  const [isCreating, setIsCreating] = useState(false);
  const createOn = () => setIsCreating(true);
  const [isSearchLyrics, setIsSearchLyrics] = useState(false);

  const createOff = () => {
    setIsCreating(false);
    if (props?.resetCreateState) {
      props.resetCreateState();
    }
  };

  const handleCreate = () => {
    props.onCreate(createOff);
  };

  return (
    <div className={styles["box"]}>
      <div className={globalStyles["flex-box-center-gap-1-mar-top-2"]}>
        <input value={props.searchTerm} onChange={(e) => props.onChange(e.target.value)} />
        {props.entity === "Hymn" && (
          <div className={styles["toggle-and-text-box"]}>
            <div className={`${styles["toggle-box"]} ${isSearchLyrics && styles["green-background"]}`}>
              <div
                className={`${styles["toggle-knob"]} ${isSearchLyrics ? styles["animation-out"] : styles["animation-in"]}`}
                onClick={() => setIsSearchLyrics(!isSearchLyrics)}
              ></div>
            </div>
            <div className={styles["search-on-hymns-box"]}>Search on lyrics</div>
          </div>
        )}
        <button onClick={createOn} disabled={isCreating}>
          Create ＋
        </button>
      </div>

      {isCreating && (
        <div className={styles["create-box"]}>
          <div className={styles["close-button-box"]}>
            <span onClick={createOff}>⛌</span>
          </div>
          {props.children}
          <button
            disabled={props.isCreateButtonDisabled || !props.isCreateFormValid}
            onClick={handleCreate}
          >{`Create ${props.entity}`}</button>
        </div>
      )}
    </div>
  );
};

export default SearchAndCreate;
