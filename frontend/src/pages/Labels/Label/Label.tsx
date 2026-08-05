import { useState } from "react";
import type { Label } from "../../../domain/Label.ts";
import globalStyles from "../../../css/global.module.css";
import { nameRegexp, invalidNameErrorMessage } from "../../../lib/constants.ts";

interface Props {
  labelId: number;
  name: string;
  onUpdate: (author: Label) => void;
}

const Label = ({ labelId, name, onUpdate }: Props) => {
  const [isEdit, setIsEdit] = useState(false);
  const [labelName, setLabelName] = useState("");
  const [updateLabelError, setUpdateLabelError] = useState("");

  const startEditing = (): void => {
    setIsEdit(true);
    setLabelName(name);
  };

  const handleSave = (): void => {
    if (name !== labelName) {
      if (nameRegexp.test(labelName)) {
        onUpdate({ labelId, name: labelName });
        setIsEdit(false);
      } else {
        setUpdateLabelError(invalidNameErrorMessage);
      }
    } else {
      setIsEdit(false);
    }
  };

  return isEdit ? (
    <>
      <div className={globalStyles["text-align-left"]}>
        <input
          value={labelName}
          onChange={(e) => {
            setUpdateLabelError("");
            setLabelName(e.target.value);
          }}
          className={globalStyles["width-100"]}
        />
        <div className={globalStyles["error-box"]}>{updateLabelError && updateLabelError}</div>
      </div>
      <button onClick={handleSave} disabled={labelName.length < 3 || !!updateLabelError}>
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

export default Label;
