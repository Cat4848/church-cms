import {
  useGetAllHymnBooksQuery,
  useCreateHymnBookMutation,
  useUpdateHymnBookMutation,
} from "../../store/api/hymnBooksApi.ts";
import Loading from "../../components/Loading/Loading.tsx";
import { ErrorFallback } from "../../components/ErrorBoundary/ErrorBoundary.tsx";
import HymnBook from "./HymnBook.tsx";
import NoContent from "../../components/NoContent/NoContent.tsx";
import { nameRegexp, invalidNameErrorMessage } from "../../lib/constants.ts";
import { useState } from "react";
import SearchAndCreate from "../../components/SearchAndCreate/SearchAndCreate.tsx";
import globalStyles from "../../css/global.module.css";

const HymnBooks = () => {
  const { data: hymnBooks, error: getAllHymnBooksError, isLoading } = useGetAllHymnBooksQuery();
  const [createHymnBook, { isLoading: isCreating, error: createError }] = useCreateHymnBookMutation();
  const [updateHymnBook, { isLoading: isUpdating, error: updateError }] = useUpdateHymnBookMutation();
  const [searchHymnBookName, setSearchHymnBookName] = useState("");
  const [newHymnBookName, setNewHymnBookName] = useState("");
  const [createHymnBookError, setCreateHymnBookError] = useState("");

  if (isLoading || isCreating || isUpdating) {
    return <Loading />;
  }
  if (getAllHymnBooksError) {
    return <ErrorFallback error={getAllHymnBooksError.toString()} />;
  }
  if (createError) {
    return <ErrorFallback error={createError.toString()} />;
  }
  if (updateError) {
    return <ErrorFallback error={updateError.toString()} />;
  }
  if (hymnBooks && !hymnBooks.length) {
    return <NoContent entity="Hymn Books" />;
  }

  const handleEditSearchTerm = (searchTerm: string) => {
    setSearchHymnBookName(searchTerm);
  };

  const handleCreateHymnBook = (closeCreateForm: () => void) => {
    if (!nameRegexp.test(newHymnBookName)) {
      setCreateHymnBookError(invalidNameErrorMessage);
    } else {
      closeCreateForm();
      createHymnBook(newHymnBookName);
    }
  };

  return (
    <div>
      <SearchAndCreate
        searchTerm={searchHymnBookName}
        onChange={handleEditSearchTerm}
        entity={"Hymn Book"}
        isCreateButtonDisabled={newHymnBookName.length < 3 || newHymnBookName.length > 100}
        isCreateFormValid={!createHymnBookError}
        onCreate={handleCreateHymnBook}
        resetCreateState={() => setNewHymnBookName("")}
      >
        <div className={globalStyles["flex-box-column-center-gap-1"]}>
          <div className={globalStyles["flex-box-center-gap-1"]}>
            <label htmlFor="hymn-book-name">Hymn Book Name</label>
            <input
              id="hymn-book-name"
              value={newHymnBookName}
              onChange={(e) => {
                setCreateHymnBookError("");
                setNewHymnBookName(e.target.value);
              }}
            />
          </div>
          <span className={globalStyles["error-box"]}>{createHymnBookError && createHymnBookError}</span>
        </div>
      </SearchAndCreate>
      <div className={globalStyles["grid-two-columns"]}>
        {hymnBooks &&
          hymnBooks.length &&
          hymnBooks
            .filter((hymnBook) => hymnBook.name.toLowerCase().includes(searchHymnBookName.toLowerCase()))
            .map((hymnBook) => {
              return (
                <HymnBook
                  key={hymnBook.hymnBookId}
                  hymnBookId={hymnBook.hymnBookId}
                  name={hymnBook.name}
                  onUpdate={updateHymnBook}
                />
              );
            })}
      </div>
    </div>
  );
};

export default HymnBooks;
