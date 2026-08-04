import { useState } from "react";
import type { HymnBook } from "../../domain/HymnBook.ts";
import globalStyles from "../../css/global.module.css";
import { invalidNameErrorMessage, nameRegexp } from "../../lib/constants.ts";

interface Props {
  hymnBookId: number;
  name: string;
  onUpdate: (hymnBook: HymnBook) => void;
}

const HymnBook = ({ hymnBookId, name, onUpdate }: Props) => {
  const [isEdit, setIsEdit] = useState(false);
  const [hymnBookName, setHymnBookName] = useState("");
  const [updateHymnBookError, setUpdateHymnBookError] = useState("");

  const startEditing = (): void => {
    setIsEdit(true);
    setHymnBookName(name);
  };

  const handleSave = (): void => {
    if (name !== hymnBookName) {
      if (nameRegexp.test(hymnBookName)) {
        onUpdate({ hymnBookId, name: hymnBookName });
        setIsEdit(false);
      } else {
        setUpdateHymnBookError(invalidNameErrorMessage);
      }
    } else {
      setIsEdit(false);
    }
  };

  return isEdit ? (
    <>
      <div className={globalStyles["text-align-left"]}>
        <input
          value={hymnBookName}
          onChange={(e) => {
            setUpdateHymnBookError("");
            setHymnBookName(e.target.value);
          }}
          className={globalStyles["width-100"]}
        />
        <div className={globalStyles["error-box"]}>{updateHymnBookError && updateHymnBookError}</div>
      </div>
      <button onClick={handleSave} disabled={hymnBookName.length < 3 || !!updateHymnBookError}>
        Save
      </button>
    </>
  ) : (
    <>
      <div className={globalStyles["text-align-left"]}>{name}</div>
      <button onClick={startEditing}>Edit</button>
    </>
  );
};

export default HymnBook;
