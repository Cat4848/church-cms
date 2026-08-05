import { useState } from "react";
import type { Topic } from "../../../domain/Topic.ts";
import globalStyles from "../../../css/global.module.css";
import { nameRegexp, invalidNameErrorMessage } from "../../../lib/constants.ts";

interface Props {
  topicId: number;
  name: string;
  onUpdate: (topic: Topic) => void;
}

const Topic = ({ topicId, name, onUpdate }: Props) => {
  const [isEdit, setIsEdit] = useState(false);
  const [topicName, setTopicName] = useState("");
  const [updateTopicError, setUpdateTopicError] = useState("");

  const startEditing = (): void => {
    setIsEdit(true);
    setTopicName(name);
  };

  const handleSave = (): void => {
    if (name !== topicName) {
      if (nameRegexp.test(topicName)) {
        onUpdate({ topicId, name: topicName });
        setIsEdit(false);
      } else {
        setUpdateTopicError(invalidNameErrorMessage);
      }
    } else {
      setIsEdit(false);
    }
  };

  return isEdit ? (
    <>
      <div className={globalStyles["text-align-left"]}>
        <input
          value={topicName}
          onChange={(e) => {
            setUpdateTopicError("");
            setTopicName(e.target.value);
          }}
          className={globalStyles["width-100"]}
        />
        <div className={globalStyles["error-box"]}>{updateTopicError && updateTopicError}</div>
      </div>
      <button onClick={handleSave} disabled={topicName.length < 3 || !!updateTopicError}>
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

export default Topic;
