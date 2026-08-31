import { useGetAllHymnsQuery, useCreateHymnMutation, useUpdateHymnMutation } from "../../../store/api/hymnsApi.ts";
import Loading from "../../../components/Loading/Loading.tsx";
import { ErrorFallback } from "../../../components/ErrorBoundary/ErrorBoundary.tsx";
import NoContent from "../../../components/NoContent/NoContent.tsx";
import Hymn from "../Hymn/Hymn.tsx";
import { useGetAllAuthorsQuery } from "../../../store/api/authorsApi.ts";
import { useGetAllHymnBooksQuery } from "../../../store/api/hymnBooksApi.ts";
import { useGetAllTopicsQuery } from "../../../store/api/topicsApi.ts";
import { useGetAllLabelsQuery } from "../../../store/api/labelsApi.ts";
import type { Author } from "../../../domain/Author.ts";
import type { HymnBook } from "../../../domain/HymnBook.ts";
import type { Topic } from "../../../domain/Topic.ts";
import type { Label } from "../../../domain/Label.ts";
import type { Hymn as HymnInterface, CreateOrUpdateHymnPayload } from "../../../domain/Hymn.ts";
import SearchAndCreate from "../../../components/SearchAndCreate/SearchAndCreate.tsx";
import { useEffect, useState } from "react";
import styles from "./Hymns.module.css";
import CreateOrUpdateHymnForm from "../../../components/CreateOrUpdateHymnForm/CreateOrUpdateHymnForm.tsx";

const initialCreateHymnForData: CreateOrUpdateHymnPayload = {
  // initialised as 0 but the isHymnValid marks a hymn invalid if the authorId is still 0
  // this is done merely to facilitate types for createHymn mutation
  authorId: 0,
  authorExtras: undefined,
  title: "",
  lyrics: "",
  hymnBookId: undefined,
  numberInHymnBook: undefined,
  topicId: undefined,
  labelId: undefined,
};

const Hymns = () => {
  const { data: hymns, error: getAllHymnsError, isLoading } = useGetAllHymnsQuery();
  const { data: authors, isLoading: isAuthorsLoading, error: authorsError } = useGetAllAuthorsQuery();
  const { data: hymnBooks, isLoading: isHymnBooksError, error: hymnBooksError } = useGetAllHymnBooksQuery();
  const { data: topics, isLoading: isTopicsLoading, error: topicsError } = useGetAllTopicsQuery();
  const { data: labels, isLoading: isLabelsLoading, error: labelsError } = useGetAllLabelsQuery();

  const [createHymn, { isLoading: isCreating, error: createError }] = useCreateHymnMutation();
  const [updateHymn, { isLoading: isUpdating, error: updateError }] = useUpdateHymnMutation();

  const [createHymnFormData, setCreateHymnFormData] = useState<CreateOrUpdateHymnPayload>(initialCreateHymnForData);

  const [searchHymnTerm, setSearchHymnTerm] = useState("");

  const handleSetCreateHymnFormData = (
    key: keyof CreateOrUpdateHymnPayload,
    value: CreateOrUpdateHymnPayload[keyof CreateOrUpdateHymnPayload],
  ): void => {
    setCreateHymnFormData({
      ...createHymnFormData,
      [key]: value,
    });
  };

  const initAuthorIdSelectElementValue = () => {
    const firstAuthor: Author | undefined = authors && authors[0];

    setCreateHymnFormData({
      ...initialCreateHymnForData,
      authorId: firstAuthor?.authorId ?? 0,
    });
  };

  useEffect(() => {
    initAuthorIdSelectElementValue();
  }, [authors, hymnBooks, topics, labels]);

  // todo: add loading for all entities
  if (
    isLoading ||
    isAuthorsLoading ||
    isHymnBooksError ||
    isTopicsLoading ||
    isLabelsLoading ||
    isCreating ||
    isUpdating
  ) {
    return <Loading />;
  }
  if (!hymns || getAllHymnsError) {
    return <ErrorFallback error={getAllHymnsError ? getAllHymnsError.toString() : "Error getting Hymns"} />;
  }
  if (!authors || authorsError) {
    return <ErrorFallback error={authorsError ? authorsError.toString() : "Error getting Authors"} />;
  }
  if (!hymnBooks || hymnBooksError) {
    return <ErrorFallback error={hymnBooksError ? hymnBooksError.toString() : "Error getting Hymn Books"} />;
  }
  if (!topics || topicsError) {
    return <ErrorFallback error={topicsError ? topicsError.toString() : "Error getting Topics"} />;
  }
  if (!labels || labelsError) {
    return <ErrorFallback error={labelsError ? labelsError.toString() : "Error getting Labels"} />;
  }
  if (createError) {
    return <ErrorFallback error={createError.toString()} />;
  }
  if (updateError) {
    return <ErrorFallback error={updateError.toString()} />;
  }
  // todo: add the errors for creating and updating + all other entities
  if (hymns && !hymns.length) {
    return <NoContent entity="Hymns" />;
  }

  if (authors && !authors.length) {
    return <NoContent entity="Authors" />;
  }
  if (hymnBooks && !hymnBooks.length) {
    return <NoContent entity="HymnBooks" />;
  }
  if (topics && !topics.length) {
    return <NoContent entity="Topics" />;
  }
  if (labels && !labels.length) {
    return <NoContent entity="Labels" />;
  }

  const handleEditSearchTerm = (searchTerm: string): void => {
    setSearchHymnTerm(searchTerm);
  };

  const handleCreateHymn = (closeCreateForm: () => void): void => {
    closeCreateForm();
    createHymn(createHymnFormData);
    initAuthorIdSelectElementValue();
  };

  const handleUpdateHymn = (hymn: HymnInterface) => {
    updateHymn(hymn);
  };

  const isHymnValid = (): boolean => {
    // includes 0 and undefined
    if (!createHymnFormData.authorId) return false;
    if (createHymnFormData.authorExtras && createHymnFormData.authorExtras.length > 200) return false;
    if (!createHymnFormData.title || createHymnFormData.title.length < 3 || createHymnFormData.title.length > 100)
      return false;
    if (!createHymnFormData.lyrics || createHymnFormData.lyrics.length < 3 || createHymnFormData.lyrics.length > 21000)
      return false;
    if (createHymnFormData.hymnBookId && createHymnFormData.hymnBookId < 1) return false;
    if (
      createHymnFormData.numberInHymnBook === 0 ||
      (createHymnFormData.numberInHymnBook && createHymnFormData.numberInHymnBook < 0)
    )
      return false;
    if (createHymnFormData.topicId && createHymnFormData.topicId < 1) return false;
    if (createHymnFormData.labelId && createHymnFormData.labelId < 1) return false;
    return true;
  };

  const handleResetCreateState = (): void => {
    setCreateHymnFormData(initialCreateHymnForData);
  };

  return (
    <div>
      <SearchAndCreate
        searchTerm={searchHymnTerm}
        onChange={handleEditSearchTerm}
        entity="Hymn"
        isCreateButtonDisabled={!isHymnValid()}
        isCreateFormValid={isHymnValid()}
        onCreate={handleCreateHymn}
        resetCreateState={handleResetCreateState}
      >
        <CreateOrUpdateHymnForm
          formData={createHymnFormData}
          setFormData={handleSetCreateHymnFormData}
          setFormDataRaw={setCreateHymnFormData}
          authors={authors}
          hymnBooks={hymnBooks}
          topics={topics}
          labels={labels}
        />
      </SearchAndCreate>
      <div className={styles["grid-one-column-for-hymns"]}>
        {hymns &&
          hymns.length &&
          hymns.map((hymn) => {
            const author: Author | undefined = authors.find((a) => a.authorId === hymn.authorId);
            if (!author) {
              return <NoContent entity="Authors" />;
            }

            const hymnBook: HymnBook | undefined = hymnBooks.find((h) => h.hymnBookId === hymn.hymnBookId);
            const topic: Topic | undefined = topics.find((t) => t.topicId === hymn.topicId);
            const label: Label | undefined = labels.find((l) => l.labelId === hymn.labelId);

            return (
              <Hymn
                key={hymn.hymnId}
                hymn={hymn}
                author={author}
                hymnBook={hymnBook}
                topic={topic}
                label={label}
                authors={authors}
                hymnBooks={hymnBooks}
                topics={topics}
                labels={labels}
                onUpdateHymn={handleUpdateHymn}
              />
            );
          })}
      </div>
    </div>
  );
};

export default Hymns;
