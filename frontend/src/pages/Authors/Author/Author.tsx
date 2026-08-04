import { useState } from "react";
import type { Author } from "../../../domain/Author.ts";
import globalStyles from "../../../css/global.module.css";
import { nameRegexp, invalidNameErrorMessage } from "../../../lib/constants.ts";

interface Props {
  authorId: number;
  name: string;
  onUpdate: (author: Author) => void;
}

const Author = ({ authorId, name, onUpdate }: Props) => {
  const [isEdit, setIsEdit] = useState(false);
  const [authorName, setAuthorName] = useState("");
  const [updateAuthorError, setUpdateAuthorError] = useState("");

  const startEditing = (): void => {
    setIsEdit(true);
    setAuthorName(name);
  };

  const handleSave = (): void => {
    if (name !== authorName) {
      if (nameRegexp.test(authorName)) {
        onUpdate({ authorId, name: authorName });
        setIsEdit(false);
      } else {
        setUpdateAuthorError(invalidNameErrorMessage);
      }
    } else {
      setIsEdit(false);
    }
  };

  return isEdit ? (
    <>
      <div className={globalStyles["text-align-left"]}>
        <input
          value={authorName}
          onChange={(e) => {
            setUpdateAuthorError("");
            setAuthorName(e.target.value);
          }}
          className={globalStyles["width-100"]}
        />
        <div className={globalStyles["error-box"]}>{updateAuthorError && updateAuthorError}</div>
      </div>
      <button onClick={handleSave} disabled={authorName.length < 3 || !!updateAuthorError}>
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

export default Author;
