import {
  useGetAllAuthorsQuery,
  useCreateAuthorMutation,
  useUpdateAuthorMutation,
} from "../../../store/api/authorsApi.ts";
import Loading from "../../../components/Loading/Loading.tsx";
import { ErrorFallback } from "../../../components/ErrorBoundary/ErrorBoundary.tsx";
import Author from "../Author/Author.tsx";
import NoContent from "../../../components/NoContent/NoContent.tsx";
import SearchAndCreate from "../../../components/SearchAndCreate/SearchAndCreate.tsx";
import { useState } from "react";
import globalStyles from "../../../css/global.module.css";
import { nameRegexp, invalidNameErrorMessage } from "../../../lib/constants.ts";

const Authors = () => {
  const { data: authors, error: getAllAuthorsError, isLoading } = useGetAllAuthorsQuery();
  const [createAuthor, { isLoading: isCreating, error: createError }] = useCreateAuthorMutation();
  const [updateAuthor, { isLoading: isUpdating, error: updateError }] = useUpdateAuthorMutation();
  const [searchAuthorName, setSearchAuthorName] = useState("");
  const [newAuthorName, setNewAuthorName] = useState("");
  const [createAuthorError, setCreateAuthorError] = useState("");

  if (isLoading || isCreating || isUpdating) {
    return <Loading />;
  }
  if (getAllAuthorsError) {
    return <ErrorFallback error={getAllAuthorsError.toString()} />;
  }
  if (createError) {
    return <ErrorFallback error={createError.toString()} />;
  }
  if (updateError) {
    return <ErrorFallback error={updateError.toString()} />;
  }
  if (authors && !authors.length) {
    return <NoContent entity="Authors" />;
  }

  const handleEditSearchTerm = (searchTerm: string) => {
    setSearchAuthorName(searchTerm);
  };

  const handleCreateAuthor = (closeCreateForm: () => void) => {
    if (!nameRegexp.test(newAuthorName)) {
      setCreateAuthorError(invalidNameErrorMessage);
    } else {
      closeCreateForm();
      createAuthor(newAuthorName);
    }
  };

  return (
    <div>
      <SearchAndCreate
        searchTerm={searchAuthorName}
        onChange={handleEditSearchTerm}
        entity="Author"
        isCreateButtonDisabled={newAuthorName.length < 3 || newAuthorName.length > 100}
        isCreateFormValid={!createAuthorError}
        onCreate={handleCreateAuthor}
        resetCreateState={() => setNewAuthorName("")}
      >
        <div className={globalStyles["flex-box-column-center-gap-1"]}>
          <div className={globalStyles["flex-box-center-gap-1"]}>
            <label htmlFor="author-name">Author Name</label>
            <input
              id="author-name"
              value={newAuthorName}
              onChange={(e) => {
                setCreateAuthorError("");
                setNewAuthorName(e.target.value);
              }}
            />
          </div>
          <span className={globalStyles["error-box"]}>{createAuthorError && createAuthorError}</span>
        </div>
      </SearchAndCreate>
      <div className={globalStyles["grid-two-columns"]}>
        {authors &&
          authors.length &&
          authors
            .filter((author) => author.name.toLowerCase().includes(searchAuthorName.toLowerCase()))
            .map((author) => {
              return (
                <Author key={author.authorId} authorId={author.authorId} name={author.name} onUpdate={updateAuthor} />
              );
            })}
      </div>
    </div>
  );
};

export default Authors;
