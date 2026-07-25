import { useState } from "react";
import type { Author } from "../../domain/Author.ts";
import globalStyles from "../../css/global.module.css";

interface Props {
  authorId: number;
  name: string;
  onChange: (author: Author) => void;
}

const Author = ({ authorId, name, onChange }: Props) => {
  const [isEdit, setIsEdit] = useState(false);
  const [authorName, setAuthorName] = useState("");

  const startEditing = (): void => {
    setIsEdit(true);
    setAuthorName(name);
  };

  const handleSave = (): void => {
    setIsEdit(false);

    if (name !== authorName) {
      onChange({ authorId, name: authorName });
    }
  };

  return isEdit ? (
    <div className={globalStyles["flex-box-center-gap-1-mar-top-2"]}>
      <input value={authorName} onChange={(e) => setAuthorName(e.target.value)} />
      <button onClick={handleSave}>Save</button>
    </div>
  ) : (
    <div className={globalStyles["flex-box-center-gap-1-mar-top-2"]}>
      <div>{name}</div>
      <button onClick={startEditing}>Edit</button>
    </div>
  );
};

export default Author;
