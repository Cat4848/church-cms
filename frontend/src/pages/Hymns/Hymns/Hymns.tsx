import { useGetAllHymnsQuery, useCreateHymnMutation, useUpdateHymnMutation } from "../../../store/api/hymnsApi.ts";
import Loading from "../../../components/Loading/Loading.tsx";
import { ErrorFallback } from "../../../components/ErrorBoundary/ErrorBoundary.tsx";
import NoContent from "../../../components/NoContent/NoContent.tsx";
import globalStyles from "../../../css/global.module.css";
import Hymn from "../Hymn/Hymn.tsx";
import { useGetAllAuthorsQuery } from "../../../store/api/authorsApi.ts";
import { useGetAllHymnBooksQuery } from "../../../store/api/hymnBooksApi.ts";
import { useGetAllTopicsQuery } from "../../../store/api/topicsApi.ts";
import { useGetAllLabelsQuery } from "../../../store/api/labelsApi.ts";
import type { Author } from "../../../domain/Author.ts";
import type { HymnBook } from "../../../domain/HymnBook.ts";
import type { Topic } from "../../../domain/Topic.ts";
import type { Label } from "../../../domain/Label.ts";
import SearchAndCreate from "../../../components/SearchAndCreate/SearchAndCreate.tsx";
import { useEffect, useState } from "react";
import styles from "./Hymns.module.css";
import MandatoryField from "../../../components/MandatoryField/MandatoryField.tsx";

type CreateHymnPayload = {
  authorId: number;
  authorExtras?: string;
  title: string;
  lyrics: string;
  hymnBookId?: number;
  numberInHymnBook?: number;
  topicId?: number;
  labelId?: number;
};

const initialForData: CreateHymnPayload = {
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

enum FormHelpers {
  Reset = "Reset",
}

const Hymns = () => {
  const { data: hymns, error: getAllHymnsError, isLoading } = useGetAllHymnsQuery();
  const { data: authors, isLoading: isAuthorsLoading, error: authorsError } = useGetAllAuthorsQuery();
  const { data: hymnBooks, isLoading: isHymnBooksError, error: hymnBooksError } = useGetAllHymnBooksQuery();
  const { data: topics, isLoading: isTopicsLoading, error: topicsError } = useGetAllTopicsQuery();
  const { data: labels, isLoading: isLabelsLoading, error: labelsError } = useGetAllLabelsQuery();

  const [createHymn, { isLoading: isCreating, error: createError }] = useCreateHymnMutation();

  const [createHymnFormData, setCreateHymnFormData] = useState<CreateHymnPayload>(initialForData);

  const [searchHymnTerm, setSearchHymnTerm] = useState("");

  const setHymnFormData = (key: keyof CreateHymnPayload, value: CreateHymnPayload[keyof CreateHymnPayload]): void => {
    setCreateHymnFormData({
      ...createHymnFormData,
      [key]: value,
    });
  };

  const initAuthorIdSelectElementValue = () => {
    const firstAuthor: Author | undefined = authors && authors[0];

    setCreateHymnFormData({
      ...initialForData,
      authorId: firstAuthor?.authorId ?? 0,
    });
  };

  useEffect(() => {
    initAuthorIdSelectElementValue();
  }, [authors, hymnBooks, topics, labels]);

  // todo: add loading for all entities
  if (isLoading || isAuthorsLoading || isHymnBooksError || isTopicsLoading || isLabelsLoading || isCreating) {
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
    setCreateHymnFormData(initialForData);
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
        <>
          <div className={globalStyles["input-group-grid-two-columns"]}>
            <div className={globalStyles["input-group"]}>
              <label htmlFor="author-id">
                <MandatoryField /> Author
              </label>
              <select id="author-id" onChange={(e) => setHymnFormData("authorId", Number(e.target.value))}>
                {authors.map((author) => (
                  <option key={author.authorId} value={author.authorId}>
                    {author.name}
                  </option>
                ))}
              </select>
            </div>

            <div className={`${globalStyles["input-group"]} ${styles["align-right"]}`}>
              <div className={`${globalStyles["input-group"]}`}>
                <label htmlFor="author-extras">Author Extras</label>
                <input
                  id="author-extras"
                  value={createHymnFormData.authorExtras}
                  onChange={(e) => setHymnFormData("authorExtras", e.target.value)}
                />
              </div>
            </div>
          </div>

          <div className={globalStyles["input-group-grid-two-columns"]}>
            <div className={globalStyles["input-group"]}>
              <label htmlFor="hymn-book">Hymn Book</label>
              <select
                id="hymn-book"
                onChange={(e) => {
                  if (e.target.value === FormHelpers.Reset) {
                    setCreateHymnFormData({ ...createHymnFormData, hymnBookId: undefined });
                  } else {
                    setHymnFormData("hymnBookId", Number(e.target.value));
                  }
                }}
              >
                <option key="select-value-hymn-book" value={FormHelpers.Reset}>
                  -- Select --
                </option>
                {hymnBooks.map((hymnBook) => (
                  <option key={hymnBook.hymnBookId} value={hymnBook.hymnBookId}>
                    {hymnBook.name}
                  </option>
                ))}
              </select>
            </div>

            <div className={`${globalStyles["input-group"]} ${styles["align-right"]}`}>
              <div className={globalStyles["input-group"]}>
                <label id="hymn-title">
                  <MandatoryField /> Title
                </label>
                <input
                  id="hymn-title"
                  value={createHymnFormData.title}
                  onChange={(e) => setHymnFormData("title", e.target.value)}
                />
              </div>
            </div>
          </div>

          <div className={globalStyles["input-group-grid-two-columns"]}>
            <div className={globalStyles["input-group"]}>
              <label htmlFor="topic">Topic</label>
              <select
                id="topic"
                onChange={(e) => {
                  if (e.target.value === FormHelpers.Reset) {
                    setCreateHymnFormData({ ...createHymnFormData, topicId: undefined });
                  } else {
                    setHymnFormData("topicId", Number(e.target.value));
                  }
                }}
              >
                <option key="select-value-topic" value={FormHelpers.Reset}>
                  -- Select --
                </option>
                {topics.map((topic) => (
                  <option key={topic.topicId} value={topic.topicId}>
                    {topic.name}
                  </option>
                ))}
              </select>
            </div>

            <div className={`${globalStyles["input-group"]} ${styles["align-right"]}`}>
              <div className={globalStyles["input-group"]}>
                <label htmlFor="number-in-hymn-book">Number in Hymn Book</label>
                <input
                  id="number-in-hymn-book"
                  type="number"
                  step="1"
                  min="1"
                  value={createHymnFormData.numberInHymnBook ?? ""}
                  onChange={(e) => setHymnFormData("numberInHymnBook", Number(e.target.value))}
                />
              </div>
            </div>
          </div>

          <div className={globalStyles["input-group-grid-two-columns"]}>
            <div className={globalStyles["input-group"]}>
              <label htmlFor="label">Label</label>
              <select
                id="label"
                onChange={(e) => {
                  if (e.target.value === FormHelpers.Reset) {
                    setCreateHymnFormData({ ...createHymnFormData, labelId: undefined });
                  } else {
                    setHymnFormData("labelId", Number(e.target.value));
                  }
                }}
              >
                <option key="select-value-hymn-topic" value={FormHelpers.Reset}>
                  -- Select --
                </option>
                {labels.map((label) => (
                  <option key={label.labelId} value={label.labelId}>
                    {label.name}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className={`${globalStyles["input-group"]} ${styles["lyrics-input-group"]}`}>
            <label htmlFor="hymn-lyrics">
              <MandatoryField /> Lyrics
            </label>
            <textarea
              id="hymn-lyrics"
              value={createHymnFormData.lyrics}
              onChange={(e) => setHymnFormData("lyrics", e.target.value)}
            />
          </div>
        </>
      </SearchAndCreate>
      <div className={globalStyles["grid-two-columns"]}>
        {hymns &&
          hymns.length &&
          hymns.map((hymn) => {
            const author: Author | undefined = authors.find((a) => a.authorId === hymn.authorId);
            if (!author) {
              return <NoContent entity="Authors" />;
            }

            const hymnBook: HymnBook | undefined = hymnBooks.find((h) => h.hymnBookId === hymn.hymnBookId);
            if (!hymnBook) {
              return <NoContent entity="Hymn Book" />;
            }

            const topic: Topic | undefined = topics.find((t) => t.topicId === hymn.topicId);
            if (!topic) {
              return <NoContent entity="Topic" />;
            }

            const label: Label | undefined = labels.find((l) => l.labelId === hymn.labelId);
            if (!label) {
              return <NoContent entity="Label" />;
            }
            return (
              <Hymn
                key={hymn.hymnId}
                hymnId={hymn.hymnId}
                authorName={author.name}
                authorExtras={hymn.authorExtras}
                title={hymn.title}
                lyrics={hymn.lyrics}
                hymnBookName={hymnBook.name}
                numberInHymnBook={hymn.numberInHymnBook}
                topicName={topic.name}
                labelName={label.name}
              />
            );
          })}
      </div>
    </div>
  );
};

export default Hymns;
