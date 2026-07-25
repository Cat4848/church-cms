import { useState } from "react";
import type { HymnBook } from "../../domain/HymnBook.ts";
import globalStyles from "../../css/global.module.css";

interface Props {
  hymnBookId: number;
  name: string;
  onChange: (hymnBook: HymnBook) => void;
}

const HymnBook = ({ hymnBookId, name, onChange }: Props) => {
  const [isEdit, setIsEdit] = useState(false);
  const [hymnBookName, setHymnBookName] = useState("");

  const startEditing = (): void => {
    setIsEdit(true);
    setHymnBookName(name);
  };

  const handleSave = (): void => {
    setIsEdit(false);

    if (name !== hymnBookName) {
      onChange({ hymnBookId, name: hymnBookName });
    }
  };

  return isEdit ? (
    <div className={globalStyles["flex-box-center-gap-1-mar-top-2"]}>
      <input value={hymnBookName} onChange={(e) => setHymnBookName(e.target.value)} />
      <button onClick={handleSave}>Save</button>
    </div>
  ) : (
    <div className={globalStyles["flex-box-center-gap-1-mar-top-2"]}>
      <div>{name}</div>
      <button onClick={startEditing}>Edit</button>
    </div>
  );
};

export default HymnBook;
