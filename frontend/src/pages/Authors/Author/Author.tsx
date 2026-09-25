import { useState } from "react";
import type { Author } from "../../../domain/Author.ts";
import globalStyles from "../../../css/global.module.css";
import { nameRegexp, invalidNameErrorMessage } from "../../../lib/constants.ts";
import { useDeleteAuthorMutation } from "../../../store/api/authorsApi.ts";
import Loading from "../../../components/Loading/Loading.tsx";
import { ErrorFallback } from "../../../components/ErrorBoundary/ErrorBoundary.tsx";

interface Props {
  authorId: number;
  name: string;
  isDeletable: boolean;
  onUpdate: (author: Author) => void;
}

const Author = ({ authorId, name, isDeletable, onUpdate }: Props) => {
  const [isEdit, setIsEdit] = useState(false);
  const [authorName, setAuthorName] = useState("");
  const [updateAuthorError, setUpdateAuthorError] = useState("");
  const [deleteAuthor, { isLoading: isDeleteAuthorLoading, isError: isDeleteAuthorError, error: deleteAuthorError }] =
    useDeleteAuthorMutation();

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

  const handleDelete = () => {
    if (isDeletable) {
      deleteAuthor(authorId);
    }
  };

  if (isDeleteAuthorLoading) {
    return <Loading />;
  }
  if (isDeleteAuthorError || deleteAuthorError) {
    return <ErrorFallback error={deleteAuthorError.toString()} />;
  }
  
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
      <div className={globalStyles["flex-box-center-gap-1"]}>
        <button onClick={startEditing}>Edit</button>
        <button onClick={handleDelete} disabled={!isDeletable}>
          Delete
        </button>
      </div>
    </>
  );
};

export default Author;
